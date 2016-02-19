package de.vernideas.mc.taverns.trader;

import cpw.mods.fml.common.registry.VillagerRegistry;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;
import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import org.millenaire.common.forge.Mill;

public class VillageHandlerBarWench extends VillageHandlerBase implements VillagerRegistry.IVillageTradeHandler {
	public static VillageHandlerBarWench villageHandler = null;
	
	public static void init(Settings config) {
		if (null == villageHandler) {
			villageHandler = new VillageHandlerBarWench();
			VillagerRegistry.instance().registerVillagerId(config.barWenchID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.barWenchID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.barWenchID, "taverns", "textures/entity/barwench.png");
		}
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(new ItemStack(Items.potionitem, 1, 16), new ItemStack(Items.potionitem, 1, 16), new ItemStack(Items.emerald, 1)));
		
		addRandomTrade(recipeList, this.emerald, new ItemStack(Items.potionitem, 2, 33), 0.5D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Items.baked_potato, 28, 40, random), 0.9D, random);
		addRandomTrade(recipeList, this.emerald, randomItem(Items.magma_cream, 4, 8, random), 0.3D, random);
		
		addRandomTrade(recipeList, new ItemStack(Items.bone, 12), this.emerald, 0.5D, random);
		addRandomTrade(recipeList, new ItemStack(Items.glowstone_dust, 12), this.emerald, 0.5D, random);
		addRandomTrade(recipeList, new ItemStack(Items.blaze_powder, 12), this.emerald, 0.5D, random);
		
		if (Taverns.config.millenaire) {
			
			addRandomTrade(recipeList, this.emerald, new ItemStack(Mill.calva, 2), 0.3D, random);
			addRandomTrade(recipeList, this.emerald, new ItemStack(Mill.cider, 4), 0.5D, random);
			addRandomTrade(recipeList, this.emerald, randomItem(Mill.wineBasic, 4, 8, random), 0.8D, random);
			addRandomTrade(recipeList, this.emerald, new ItemStack(Mill.wineFancy, 1), 0.2D, random);
			addRandomTrade(recipeList, this.emerald, new ItemStack(Mill.cacauhaa, 1), 0.2D, random);
			addRandomTrade(recipeList, this.emerald, new ItemStack(Mill.sake, 2), 0.3D, random);
			
			addRandomTrade(recipeList, randomItem(Mill.ciderapple, 10, 14, random), this.emerald, 0.5D, random);
			addRandomTrade(recipeList, randomItem(Mill.grapes, 6, 10, random), this.emerald, 0.5D, random);
		}
	}
}
