package de.vernideas.mc.common.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract interface StorableAIBuilder<T extends EntityAIBase> extends StorableAI {
	public abstract T createFromNBT(EntityLiving paramEntityLiving, NBTTagCompound paramNBTTagCompound);
	
	public abstract Class<T> getType();
	
	public abstract String getRoutineName();
}
