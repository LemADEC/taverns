package de.vernideas.mc.common;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public abstract interface CommonSidedProxy {
	public abstract void registerItemRenderer(Item paramItem, IItemRenderer paramIItemRenderer);
	
	public abstract void registerVillagerSkin(int paramInt, String paramString1, String paramString2);
}
