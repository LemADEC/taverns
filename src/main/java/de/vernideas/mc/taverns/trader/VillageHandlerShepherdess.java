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

public class VillageHandlerShepherdess extends VillageHandlerBase implements VillagerRegistry.IVillageTradeHandler {
	public static VillageHandlerShepherdess villageHandler = null;
	
	public static void init(Settings config) {
		if (null == villageHandler) {
			villageHandler = new VillageHandlerShepherdess();
			VillagerRegistry.instance().registerVillagerId(config.shepherdessID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.shepherdessID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.shepherdessID, "taverns", "textures/entity/village_shepherdess.png");
		}
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		if (Taverns.config.grimoireOfGaia2) {
		}
		
		recipeList.add(new MerchantRecipe(new ItemStack(Blocks.wool, 1), new ItemStack(Items.string, 4)));
		
		switch (random.nextInt(5)) {
		
		case 0:
			recipeList.add(new MerchantRecipe(this.emerald, randomItem(Blocks.wool, 14, 18, 0, random)));
			break;
		
		case 1:
			recipeList.add(new MerchantRecipe(this.emerald, randomItem(Blocks.wool, 10, 14, 8, random)));
			break;
		
		case 2:
			recipeList.add(new MerchantRecipe(this.emerald, randomItem(Blocks.wool, 10, 14, 7, random)));
			break;
		
		case 3:
			recipeList.add(new MerchantRecipe(this.emerald, randomItem(Blocks.wool, 6, 12, 15, random)));
			break;
		
		case 4:
			recipeList.add(new MerchantRecipe(this.emerald, randomItem(Blocks.wool, 6, 12, random.nextInt(16), random)));
			break;
		}
		
	}
}
