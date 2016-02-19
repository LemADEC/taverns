package de.vernideas.mc.common.ai;

import de.vernideas.mc.common.util.TimePeriod;
import java.util.UUID;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;

public class GuardAI extends EntityAIBase implements StorableAI {
	private AttributeModifier goingHome = null;
	
	private final EntityLiving entity;
	private final ChunkCoordinates pos;
	private final float roamDist;
	private final double roamDistSq;
	private final float pathSearchDist;
	private final double pathSearchDistSq;
	private final TimePeriod guardPeriod;
	private final boolean shouldExitTransport;
	public GuardState state;
	public int cooldownTicks;
	
	public static GuardAI createFromNBT(EntityLiving living, NBTTagCompound data) {
		int posX = data.getInteger("PosX");
		int posY = data.getInteger("PosY");
		int posZ = data.getInteger("PosZ");
		int startTime = data.getInteger("GuardStartTime");
		int endTime = data.getInteger("GuardEndTime");
		GuardAI ai = new GuardAI(living, new ChunkCoordinates(posX, posY, posZ), data.getFloat("RoamDist"), data.getFloat("PathSearchDist"), new TimePeriod(startTime, endTime),
				data.getBoolean("ShouldExitTransport"));
		
		try {
			ai.state = GuardState.valueOf(data.getString("State"));
		} catch (Exception e) {
			ai.state = GuardState.IDLE;
		}
		ai.cooldownTicks = data.getInteger("CooldownTicks");
		return ai;
	}
	
	public GuardAI(EntityLiving owner, ChunkCoordinates position, float roamingDistance, float pathSearchDistance, TimePeriod guardingPeriod, boolean leaveTransports) {
		if (null == owner) {
			throw new IllegalArgumentException("Missing required argument: entity");
		}
		this.entity = owner;
		if (null == position) {
			this.pos = new ChunkCoordinates(MathHelper.floor_double(this.entity.posX + 0.5D), MathHelper.floor_double(this.entity.posY), MathHelper.floor_double(this.entity.posZ + 0.5D));
			
		} else {
			
			this.pos = position;
		}
		this.roamDist = Math.max(roamingDistance, 1.0F);
		this.roamDistSq = (this.roamDist * this.roamDist);
		this.pathSearchDist = Math.max(pathSearchDistance, roamingDistance + 1.0F);
		this.pathSearchDistSq = (this.pathSearchDist * this.pathSearchDist);
		this.guardPeriod = (null == guardingPeriod ? TimePeriod.fullDay : guardingPeriod);
		this.shouldExitTransport = leaveTransports;
		this.state = GuardState.IDLE;
		this.cooldownTicks = 0;
		setMutexBits(3);
		this.goingHome = new AttributeModifier(UUID.fromString("4454b0d8-75ef-4689-8fce-daab60a7e1b0"), "Vernideas:GoingHome", pathSearchDistance / 16.0D - 1.0D, 2);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data) {
		data.setInteger("PosX", this.pos.posX);
		data.setInteger("PosY", this.pos.posY);
		data.setInteger("PosZ", this.pos.posZ);
		data.setFloat("RoamDist", this.roamDist);
		data.setFloat("PathSearchDist", this.pathSearchDist);
		data.setInteger("GuardStartTime", this.guardPeriod.startTick);
		data.setInteger("GuardEndTime", this.guardPeriod.endTick);
		data.setBoolean("ShouldExitTransport", this.shouldExitTransport);
		data.setString("State", this.state.name());
		data.setInteger("CooldownTicks", this.cooldownTicks);
	}
	
	@Override
	public boolean shouldExecute() {
		if ((null == this.entity) || (this.entity.isDead)) {
			return false;
		}
		if (((this.entity instanceof EntityAgeable)) && (((EntityAgeable) this.entity).getGrowingAge() < 0)) {
			
			return false;
		}
		if ((this.guardPeriod != TimePeriod.fullDay) && (!this.guardPeriod.contains((int) (this.entity.worldObj.getWorldTime() % 24000L)))) {
			
			return false;
		}
		if ((null != this.entity.ridingEntity) && (!this.shouldExitTransport)) {
			
			return false;
		}
		double distanceToGuardPointSq = this.entity.getDistanceSq(this.pos.posX + 0.5D, this.pos.posY, this.pos.posZ + 0.5D);
		return (distanceToGuardPointSq > this.roamDistSq) && (distanceToGuardPointSq <= this.pathSearchDistSq);
	}
	
	@Override
	public void startExecuting() {
		this.state = GuardState.RUNNING;
		this.entity.getEntityAttribute(SharedMonsterAttributes.followRange).removeModifier(this.goingHome);
		this.entity.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(this.goingHome);
		double speed = this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
		this.entity.getNavigator().tryMoveToXYZ(this.pos.posX + 0.5D, this.pos.posY, this.pos.posZ + 0.5D, speed);
	}
	
	@Override
	public boolean continueExecuting() {
		switch (this.state) {
		case RUNNING:
			if ((this.entity.getNavigator().noPath()) || (this.entity.getDistanceSq(this.pos.posX + 0.5D, this.pos.posY, this.pos.posZ + 0.5D) < this.roamDistSq / 2.0D)) {
				
				this.state = GuardState.COOLDOWN;
				return true;
			}
		case COOLDOWN:
			if (this.cooldownTicks < 300) {
				this.cooldownTicks += 1;
				return true;
			}
			
			this.cooldownTicks = 0;
			this.state = GuardState.IDLE;
			return false;
		}
		
		return false;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.state = GuardState.IDLE;
		this.entity.getEntityAttribute(SharedMonsterAttributes.followRange).removeModifier(this.goingHome);
	}
	
	static enum GuardState {
		IDLE, RUNNING, COOLDOWN;
		
		private GuardState() {
		}
	}
}
