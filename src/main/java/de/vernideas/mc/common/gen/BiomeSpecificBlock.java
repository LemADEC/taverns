package de.vernideas.mc.common.gen;

import cpw.mods.fml.common.eventhandler.Event.Result;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class BiomeSpecificBlock {
	public final int metadata;
	public final Block block;
	public static final BiomeSpecificBlock air = new BiomeSpecificBlock(Blocks.air, 0);
	
	private static BiomeSpecificBlock getDesertReplacement(Block original, Integer originalMeta) {
		Pair<Block, Integer> replacement = desertReplaceMap.get(ImmutablePair.of(original, originalMeta));
		if (null == replacement) {
			
			replacement = desertReplaceMap.get(ImmutablePair.of(original, Integer.valueOf(-1)));
		}
		
		if (null != replacement) {
			return new BiomeSpecificBlock(replacement.getLeft(), (replacement.getRight().intValue() >= 0 ? (Integer) replacement.getRight() : originalMeta).intValue());
		}
		
		return new BiomeSpecificBlock(original, originalMeta.intValue());
	}
	
	public static BiomeSpecificBlock queryVanilla(Block block, int meta, BiomeGenBase biome) {
		if ((biome == BiomeGenBase.desert) || (biome == BiomeGenBase.desertHills)) {
			return getDesertReplacement(block, Integer.valueOf(meta));
		}
		
		return new BiomeSpecificBlock(block, meta);
	}
	
	public static BiomeSpecificBlock query(Block block, int meta, BiomeGenBase biome) {
		Block newBlock = null;
		int newMeta = -1;
		
		BiomeEvent.GetVillageBlockID eventBlock = new BiomeEvent.GetVillageBlockID(biome, block, meta);
		MinecraftForge.TERRAIN_GEN_BUS.post(eventBlock);
		if (eventBlock.getResult() == Result.DENY) {
			newBlock = eventBlock.replacement;
		}
		
		BiomeEvent.GetVillageBlockMeta eventMeta = new BiomeEvent.GetVillageBlockMeta(biome, block, meta);
		MinecraftForge.TERRAIN_GEN_BUS.post(eventMeta);
		if (eventMeta.getResult() == Result.DENY) {
			newMeta = eventMeta.replacement;
		}
		
		if ((null != newBlock) && (newMeta >= 0)) {
			
			return new BiomeSpecificBlock(newBlock, newMeta);
		}
		if ((null == newBlock) && (newMeta < 0)) {
			
			if ((biome == BiomeGenBase.desert) || (biome == BiomeGenBase.desertHills)) {
				return getDesertReplacement(block, Integer.valueOf(meta));
			}
			
			return new BiomeSpecificBlock(block, meta);
		}
		
		if (null != newBlock) {
			
			if ((biome == BiomeGenBase.desert) || (biome == BiomeGenBase.desertHills)) {
				BiomeSpecificBlock desertReplacement = getDesertReplacement(block, Integer.valueOf(meta));
				return new BiomeSpecificBlock(newBlock, desertReplacement.metadata);
			}
			
			return new BiomeSpecificBlock(newBlock, meta);
		}
		
		if ((biome == BiomeGenBase.desert) || (biome == BiomeGenBase.desertHills)) {
			BiomeSpecificBlock desertReplacement = getDesertReplacement(block, Integer.valueOf(meta));
			return new BiomeSpecificBlock(desertReplacement.block, newMeta);
		}
		
		return new BiomeSpecificBlock(block, newMeta);
	}
	
	protected BiomeSpecificBlock(Block blockInstance, int meta) {
		this.metadata = meta;
		this.block = blockInstance;
	}
	
	private static void addDesertReplacement(Block from, Integer fromMeta, Block to, Integer toMeta) {
		desertReplaceMap.put(ImmutablePair.of(from, fromMeta), ImmutablePair.of(to, toMeta));
	}
	
	private static final Map<Pair<Block, Integer>, Pair<Block, Integer>> desertReplaceMap = new HashMap();
	
	static {
		addDesertReplacement(Blocks.log, Integer.valueOf(-1), Blocks.sandstone, Integer.valueOf(0));
		addDesertReplacement(Blocks.planks, Integer.valueOf(-1), Blocks.sandstone, Integer.valueOf(0));
		addDesertReplacement(Blocks.cobblestone, Integer.valueOf(-1), Blocks.sandstone, Integer.valueOf(0));
		addDesertReplacement(Blocks.mossy_cobblestone, Integer.valueOf(-1), Blocks.sandstone, Integer.valueOf(1));
		addDesertReplacement(Blocks.gravel, Integer.valueOf(-1), Blocks.sandstone, Integer.valueOf(0));
		addDesertReplacement(Blocks.oak_stairs, Integer.valueOf(-1), Blocks.sandstone_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.jungle_stairs, Integer.valueOf(-1), Blocks.sandstone_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.stone_stairs, Integer.valueOf(-1), Blocks.sandstone_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.birch_stairs, Integer.valueOf(-1), Blocks.quartz_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.spruce_stairs, Integer.valueOf(-1), Blocks.stone_brick_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.acacia_stairs, Integer.valueOf(-1), Blocks.sandstone_stairs, Integer.valueOf(-1));
		addDesertReplacement(Blocks.dark_oak_stairs, Integer.valueOf(-1), Blocks.stone_brick_stairs, Integer.valueOf(-1));
		
		addDesertReplacement(Blocks.stone_slab, Integer.valueOf(0), Blocks.stone_slab, Integer.valueOf(1));
		addDesertReplacement(Blocks.stone_slab, Integer.valueOf(3), Blocks.stone_slab, Integer.valueOf(1));
		addDesertReplacement(Blocks.stone_slab, Integer.valueOf(8), Blocks.stone_slab, Integer.valueOf(9));
		addDesertReplacement(Blocks.stone_slab, Integer.valueOf(11), Blocks.stone_slab, Integer.valueOf(9));
		addDesertReplacement(Blocks.double_stone_slab, Integer.valueOf(0), Blocks.double_stone_slab, Integer.valueOf(1));
		addDesertReplacement(Blocks.double_stone_slab, Integer.valueOf(3), Blocks.double_stone_slab, Integer.valueOf(1));
		
		for (int meta = 0; meta <= 5; meta++) {
			addDesertReplacement(Blocks.wooden_slab, Integer.valueOf(meta), Blocks.stone_slab, Integer.valueOf(1));
			addDesertReplacement(Blocks.wooden_slab, Integer.valueOf(meta + 8), Blocks.stone_slab, Integer.valueOf(9));
			addDesertReplacement(Blocks.double_wooden_slab, Integer.valueOf(meta), Blocks.double_stone_slab, Integer.valueOf(1));
		}
		
		addDesertReplacement(Blocks.dirt, Integer.valueOf(-1), Blocks.sand, Integer.valueOf(9));
	}
}
