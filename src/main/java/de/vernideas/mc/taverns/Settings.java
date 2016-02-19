package de.vernideas.mc.taverns;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.VillagerRegistry;
import java.util.Collection;
import net.minecraftforge.common.config.Configuration;

public class Settings {
	private static final String CATEGORY_VILLAGERS = "villagers";
	private static final String CATEGORY_COMPATIBILITY = "compatibility";
	public final boolean generateTaverns;
	public final boolean generateStalls;
	public final boolean generateBarns;
	public final boolean generateBakery;
	public final boolean generateChickencoop;
	public final int weightTaverns;
	public final int weightBasementTaverns;
	public final int weightStalls;
	public final int weightBarns;
	public final int weightBakery;
	public final int weightChickencoop;
	public final int barWenchID;
	public final int hostlerID;
	public final int shepherdessID;
	public final int bakerID;
	public final boolean useBOPBlocks;
	public final boolean minecraftComesAlive;
	public final boolean biomesOPlenty;
	public final boolean grimoireOfGaia2;
	public final boolean millenaire;
	
	private int getVillagerID(Configuration cfg, String category, String configName, int defaultID) {
		Collection<Integer> registeredVillagers = VillagerRegistry.getRegisteredVillagers();
		while (registeredVillagers.contains(Integer.valueOf(defaultID))) {
			defaultID++;
		}
		return cfg.get(category, configName, defaultID).getInt(defaultID);
	}
	
	public Settings(Configuration config) {
		config.load();
		
		config.getCategory("general")
				.setComment(
						"Enable or disable village parts as needed.\n\n\"Weight\" determines how likely this structure is to spawn,\nuseful values are in the range 1-50.\nA higher value means the structure is more likely to be created.\n\nDisabling a structure disables the corresponding custom villager.");
		
		config.getCategory(CATEGORY_VILLAGERS).setComment("IDs of custom villagers. Set it to -1 to disable\nthis specific villager type.");
		
		config.getCategory(CATEGORY_COMPATIBILITY).setComment("Miscellaneous settings for compatibility with different mods.\n");
		
		config.get(CATEGORY_COMPATIBILITY, "useBOPBlocks", true).comment = "Use Biome o'Plenty's block types when generating biome-specific block replacements";
		
		this.generateTaverns = config.get("general", "generateTaverns", true).getBoolean(true);
		this.generateStalls = config.get("general", "generateStalls", true).getBoolean(true);
		this.generateBarns = config.get("general", "generateBarns", true).getBoolean(true);
		this.generateBakery = config.get("general", "generateBakery", true).getBoolean(true);
		this.generateChickencoop = config.get("general", "generateChickenCoop", true).getBoolean(true);
		
		this.weightTaverns = config.get("general", "weightTaverns", 4).getInt(4);
		this.weightBasementTaverns = 0;
		this.weightStalls = config.get("general", "weightStalls", 8).getInt(8);
		this.weightBarns = config.get("general", "weightBarns", 10).getInt(10);
		this.weightBakery = config.get("general", "weightBakery", 6).getInt(6);
		this.weightChickencoop = config.get("general", "weightChickenCoop", 5).getInt(5);
		
		this.barWenchID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDBarWench", 42);
		this.hostlerID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDHostler", this.barWenchID + 1);
		this.shepherdessID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDShepherdess", this.hostlerID + 1);
		this.bakerID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDBaker", this.shepherdessID + 1);
		
		this.useBOPBlocks = config.get(CATEGORY_COMPATIBILITY, "useBOPBlocks", true).getBoolean(true);
		
		if (config.hasChanged()) {
			config.save();
		}
		
		this.minecraftComesAlive = Loader.isModLoaded("MCA");
		this.biomesOPlenty = Loader.isModLoaded("BiomesOPlenty");
		this.grimoireOfGaia2 = Loader.isModLoaded("GrimoireGaia2");
		this.millenaire = Loader.isModLoaded("millenaire");
	}
}
