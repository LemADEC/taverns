package de.vernideas.mc.common.client;

import cpw.mods.fml.common.registry.VillagerRegistry;
import de.vernideas.mc.common.CommonSidedProxy;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy implements CommonSidedProxy {
	@Override
	public void registerItemRenderer(Item item, IItemRenderer itemRenderer) {
		MinecraftForgeClient.registerItemRenderer(item, itemRenderer);
	}
	
	@Override
	public void registerVillagerSkin(int villagerId, String resourceDomain, String resourcePath) {
		VillagerRegistry.instance().registerVillagerSkin(villagerId, new ResourceLocation(resourceDomain, resourcePath));
	}
}
