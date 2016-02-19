package de.vernideas.mc.taverns;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.vernideas.mc.common.CommonSidedProxy;
import de.vernideas.mc.taverns.gen.BakeryCreationHandler;
import de.vernideas.mc.taverns.gen.BarnCreationHandler;
import de.vernideas.mc.taverns.gen.ChickencoopCreationHandler;
import de.vernideas.mc.taverns.gen.ComponentVillageBakery;
import de.vernideas.mc.taverns.gen.ComponentVillageBarn;
import de.vernideas.mc.taverns.gen.ComponentVillageChickencoop;
import de.vernideas.mc.taverns.gen.ComponentVillageStall;
import de.vernideas.mc.taverns.gen.ComponentVillageTavern;
import de.vernideas.mc.taverns.gen.StallCreationHandler;
import de.vernideas.mc.taverns.gen.TavernCreationHandler;
import de.vernideas.mc.taverns.trader.VillageHandlerBaker;
import de.vernideas.mc.taverns.trader.VillageHandlerBarWench;
import de.vernideas.mc.taverns.trader.VillageHandlerHostler;
import de.vernideas.mc.taverns.trader.VillageHandlerShepherdess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = "taverns", name = "Village Taverns", version = "@version@")
public class Taverns {
	@Mod.Instance
	public static Taverns instance;
	@SidedProxy(clientSide = "de.vernideas.mc.common.client.ClientProxy", serverSide = "de.vernideas.mc.common.server.ServerProxy")
	public static CommonSidedProxy sidedProxy;
	public static final String modID = "taverns";
	public static final String modName = "Village Taverns";
	public static final String modVersion = "@version@";
	public static Settings config;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Settings(new Configuration(event.getSuggestedConfigurationFile()));
		if (config.generateTaverns) {
			if (config.barWenchID > 0) {
				VillageHandlerBarWench.init(config);
			}
			TavernCreationHandler.init(config);
			ComponentVillageTavern.init(config);
		}
		
		if (config.generateBarns) {
			if (config.shepherdessID > 0) {
				VillageHandlerShepherdess.init(config);
			}
			BarnCreationHandler.init(config);
			ComponentVillageBarn.init(config);
		}
		if (config.generateStalls) {
			if (config.hostlerID > 0) {
				VillageHandlerHostler.init(config);
			}
			StallCreationHandler.init(config);
			ComponentVillageStall.init(config);
		}
		if (config.generateBakery) {
			if (config.bakerID > 0) {
				VillageHandlerBaker.init(config);
			}
			BakeryCreationHandler.init(config);
			ComponentVillageBakery.init(config);
		}
		if (config.generateChickencoop) {
			ChickencoopCreationHandler.init(config);
			ComponentVillageChickencoop.init(config);
		}
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (config.generateTaverns) {
			ComponentVillageTavern.postInit(config);
		}
		if (config.generateStalls) {
			ComponentVillageStall.postInit(config);
		}
		if (config.generateBakery) {
			ComponentVillageBakery.postInit(config);
		}
	}
}
