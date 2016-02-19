package de.vernideas.mc.common.ai;

import net.minecraft.nbt.NBTTagCompound;

public abstract interface StorableAI {
	public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound);
}
