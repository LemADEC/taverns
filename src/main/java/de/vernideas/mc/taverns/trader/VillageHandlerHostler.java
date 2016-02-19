package de.vernideas.mc.taverns.trader;

import cpw.mods.fml.common.registry.VillagerRegistry;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;
import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class VillageHandlerHostler extends VillageHandlerBase implements VillagerRegistry.IVillageTradeHandler {
	public static VillageHandlerHostler villageHandler = null;
	
	public static void init(Settings config) {
		if (null == villageHandler) {
			villageHandler = new VillageHandlerHostler();
			VillagerRegistry.instance().registerVillagerId(config.hostlerID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.hostlerID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.hostlerID, "taverns", "textures/entity/village_hostler.png");
		}
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		if (Taverns.config.grimoireOfGaia2) {
		}
		
		recipeList.add(new MerchantRecipe(this.emerald, randomItem(Items.lead, 12, 16, random)));
		addRandomTrade(recipeList, this.emerald, randomItem(Blocks.hay_block, 3, 5, random), 0.9D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Items.apple, 8, 12, random), 0.9D, random);
		addRandomTrade(recipeList, this.emerald, new ItemStack(Items.saddle, 1), 0.9D, random);
		addRandomTrade(recipeList, new ItemStack(Items.emerald, 3), new ItemStack(Items.iron_horse_armor), 0.5D, random);
		addRandomTrade(recipeList, this.emerald, new ItemStack(Items.name_tag, 1), 0.5D, random);
		addRandomTrade(recipeList, this.emerald, new ItemStack(Items.golden_carrot, 1), 0.3D, random);
		
		addRandomTrade(recipeList, randomItem(Items.slime_ball, 12, 20, random), this.emerald, 0.9D, random);
		addRandomTrade(recipeList, randomItem(Items.wheat, 18, 22, random), this.emerald, 0.9D, random);
	}
}
