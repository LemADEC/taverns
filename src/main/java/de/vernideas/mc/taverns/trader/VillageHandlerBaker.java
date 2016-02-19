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
import org.millenaire.common.forge.Mill;

public class VillageHandlerBaker extends VillageHandlerBase implements VillagerRegistry.IVillageTradeHandler {
	public static VillageHandlerBaker villageHandler = null;
	
	public static void init(Settings config) {
		if (null == villageHandler) {
			villageHandler = new VillageHandlerBaker();
			VillagerRegistry.instance().registerVillagerId(config.bakerID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.bakerID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.bakerID, "taverns", "textures/entity/village_baker.png");
		}
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		if (Taverns.config.grimoireOfGaia2) {
		}
		
		recipeList.add(new MerchantRecipe(this.emerald, randomItem(Items.sugar, 24, 32, random)));
		addRandomTrade(recipeList, this.emerald, randomItem(Items.bread, 4, 8, random), 0.9D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Blocks.cake, 1, 3, random), 0.4D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Items.pumpkin_pie, 3, 5, random), 0.5D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Items.cookie, 18, 22, random), 0.5D, random);
		
		addRandomTrade(recipeList, randomItem(Items.wheat, 20, 28, random), this.emerald, 0.9D, random);
		addRandomTrade(recipeList, randomItem(Items.reeds, 4, 8, random), this.emerald, 0.9D, random);
		addRandomTrade(recipeList, randomItem(Items.egg, 12, 16, random), this.emerald, 0.5D, random);
		addRandomTrade(recipeList, randomItem(Items.coal, 16, 24, random), this.emerald, 0.5D, random);
		
		if (Taverns.config.millenaire) {
			addRandomTrade(recipeList, new ItemStack(Items.emerald, 3), new ItemStack(Mill.rasgulla, 1), 0.5D, random);
		}
	}
}
