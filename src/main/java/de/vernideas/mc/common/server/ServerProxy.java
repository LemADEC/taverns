package de.vernideas.mc.common.server;

import de.vernideas.mc.common.CommonSidedProxy;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public class ServerProxy implements CommonSidedProxy {
	@Override
	public void registerItemRenderer(Item item, IItemRenderer itemRenderer) {
	}
	
	@Override
	public void registerVillagerSkin(int villagerId, String resourceDomain, String resourcePath) {
	}
}
