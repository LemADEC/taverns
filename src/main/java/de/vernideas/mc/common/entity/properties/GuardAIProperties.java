package de.vernideas.mc.common.entity.properties;

import de.vernideas.mc.common.ai.GuardAI;
import de.vernideas.mc.common.ai.GuardAIBuilder;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GuardAIProperties implements net.minecraftforge.common.IExtendedEntityProperties {
	public static final String EXT_PROP_NAME = "Vernideas:GuardAIProp";
	private EntityLiving owner;
	
	public static final void register(EntityLiving entity) {
		entity.registerExtendedProperties("Vernideas:GuardAIProp", new GuardAIProperties());
	}
	
	public static final GuardAIProperties get(EntityLiving entity) {
		return (GuardAIProperties) entity.getExtendedProperties("Vernideas:GuardAIProp");
	}
	
	private final GuardAIBuilder guardAIBuilder = new GuardAIBuilder();
	
	@Override
	public void saveNBTData(NBTTagCompound data) {
		if (null != this.owner) {
			for (Object taskEntryObj : this.owner.tasks.taskEntries) {
				EntityAITasks.EntityAITaskEntry taskEntry = (EntityAITasks.EntityAITaskEntry) taskEntryObj;
				
				if ((taskEntry.action instanceof GuardAI)) {
					NBTTagCompound properties = new NBTTagCompound();
					((GuardAI) taskEntry.action).writeToNBT(properties);
					properties.setInteger("AIPriority", taskEntry.priority);
					data.setTag("Vernideas:GuardAIProp", properties);
					break;
				}
			}
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound data) {
		NBTTagCompound properties = (NBTTagCompound) data.getTag("Vernideas:GuardAIProp");
		if ((null != this.owner) && (null != properties)) {
			int priority = properties.getInteger("AIPriority");
			GuardAI guardAITask = this.guardAIBuilder.createFromNBT(this.owner, properties);
			this.owner.tasks.addTask(priority, guardAITask);
		}
	}
	
	@Override
	public void init(net.minecraft.entity.Entity entity, World world) {
		if ((entity instanceof EntityLiving)) {
			this.owner = ((EntityLiving) entity);
		} else {
			throw new IllegalArgumentException("Initializing GuardAIProperties with a non-EntityLiving class (" + entity.getClass().toString() + ")");
		}
	}
}
