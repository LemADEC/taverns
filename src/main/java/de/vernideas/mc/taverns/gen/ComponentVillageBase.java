package de.vernideas.mc.taverns.gen;

import com.google.common.collect.Sets;

import de.vernideas.mc.common.gen.BiomeSpecificBlock;
import de.vernideas.mc.taverns.Taverns;
import de.vernideas.mc.taverns.direction.Dir;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ComponentVillageBase extends StructureVillagePieces.Village {
	protected StructureVillagePieces.Start startPiece;
	
	public ComponentVillageBase() {
	}
	
	protected ComponentVillageBase(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction) {
		super(startPiece, type);
		this.coordBaseMode = direction;
		this.boundingBox = structBB;
		this.startPiece = startPiece;
	}
	
	protected void fill(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock, boolean alwaysreplace) {
		fillWithBlocks(worldObj, structBB, minX, minY, minZ, maxX, maxY, maxZ, placeBlock, replaceBlock, alwaysreplace);
	}
	
	protected void fill(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BiomeSpecificBlock placeBlock, BiomeSpecificBlock replaceBlock,
			boolean alwaysreplace) {
		fillWithMetadataBlocks(worldObj, structBB, minX, minY, minZ, maxX, maxY, maxZ, placeBlock.block, placeBlock.metadata, replaceBlock.block, replaceBlock.metadata, alwaysreplace);
	}
	
	protected void fill(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock) {
		fillWithBlocks(worldObj, structBB, minX, minY, minZ, maxX, maxY, maxZ, placeBlock, replaceBlock, false);
	}
	
	protected void fill(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BiomeSpecificBlock placeBlock, BiomeSpecificBlock replaceBlock) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if ((x != minX) && (x != maxX) && (y != minY) && (y != maxY) && (z != minZ) && (z != maxZ)) {
						
						placeBlock(worldObj, replaceBlock, x, y, z, structBB);
					} else {
						placeBlock(worldObj, placeBlock, x, y, z, structBB);
					}
				}
			}
		}
	}
	
	protected void fillRandomly(World world, StructureBoundingBox structBB, Random rnd, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock) {
		randomlyFillWithBlocks(world, structBB, rnd, chance, minX, minY, minZ, maxX, maxY, maxZ, placeBlock, replaceBlock, false);
	}
	
	protected void fillRandomly(World worldObj, StructureBoundingBox structBB, Random rnd, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BiomeSpecificBlock placeBlock,
			BiomeSpecificBlock replaceBlock) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (rnd.nextFloat() <= chance) {
						if ((x != minX) && (x != maxX) && (y != minY) && (y != maxY) && (z != minZ) && (z != maxZ)) {
							
							placeBlock(worldObj, replaceBlock, x, y, z, structBB);
						} else {
							placeBlock(worldObj, placeBlock, x, y, z, structBB);
						}
					}
				}
			}
		}
	}
	
	protected void fillDownwards(World world, Block block, int par3, int xx, int par5, int zz, StructureBoundingBox structBB) {
		func_151554_b(world, block, par3, xx, par5, zz, structBB);
	}
	
	protected void placeBlock(World world, Block block, int metadata, int posX, int posY, int posZ, StructureBoundingBox structBB) {
		placeBlockAtCurrentPosition(world, block, metadata, posX, posY, posZ, structBB);
	}
	
	protected void placeBlock(World world, Block block, ForgeDirection dir, int posX, int posY, int posZ, StructureBoundingBox structBB) {
		placeBlockAtCurrentPosition(world, block, Dir.getMeta(block, dir, this.coordBaseMode), posX, posY, posZ, structBB);
	}
	
	protected void placeBlock(World world, BiomeSpecificBlock block, int posX, int posY, int posZ, StructureBoundingBox structBB) {
		int globalX = getXWithOffset(posX, posZ);
		int globalY = getYWithOffset(posY);
		int globalZ = getZWithOffset(posX, posZ);
		
		if (structBB.isVecInside(globalX, globalY, globalZ)) {
			world.setBlock(globalX, globalY, globalZ, block.block, block.metadata, 2);
		}
	}
	
	protected void placeAir(World world, int posX, int posY, int posZ, StructureBoundingBox structBB) {
		placeBlockAtCurrentPosition(world, Blocks.air, 0, posX, posY, posZ, structBB);
	}
	
	protected void placeDoor(World world, StructureBoundingBox structBB, Random rnd, int posX, int posY, int posZ, ForgeDirection dir) {
		placeDoorAtCurrentPosition(world, structBB, rnd, posX, posY, posZ, Dir.getMeta(Blocks.wooden_door, dir, this.coordBaseMode));
	}
	
	protected void placeTorch(World world, StructureBoundingBox structBB, int posX, int posY, int posZ, ForgeDirection dir) {
		placeBlock(world, Blocks.stone, 0, posX, posY - 1, posZ, structBB);
		placeBlock(world, Blocks.torch, dir, posX, posY, posZ, structBB);
		placeAir(world, posX, posY - 1, posZ, structBB);
	}
	
	protected void setMetadata(World world, int metadata, int posX, int posY, int posZ, StructureBoundingBox structBB) {
		int globalX = getXWithOffset(posX, posZ);
		int globalY = getYWithOffset(posY);
		int globalZ = getZWithOffset(posX, posZ);
		
		if (structBB.isVecInside(globalX, globalY, globalZ)) {
			world.setBlockMetadataWithNotify(globalX, globalY, globalZ, metadata, 2);
		}
	}
	
	protected int spawnEntity(World world, StructureBoundingBox structBB, int posX, int posY, int posZ, int amount) {
		int spawned = 0;
		for (int idx = 0; idx < amount; idx++) {
			int globalX = getXWithOffset(posX, posZ);
			int globalY = getYWithOffset(posY);
			int globalZ = getZWithOffset(posX, posZ);
			
			if (structBB.isVecInside(globalX, globalY, globalZ)) {
				EntityLivingBase entity = getEntity(world, idx);
				if (null != entity) {
					spawned++;
					entity.setLocationAndAngles(globalX + 0.5D, globalY, globalZ + 0.5D, 0.5F, 0.0F);
					world.spawnEntityInWorld(entity);
				}
			}
		}
		
		return spawned;
	}
	
	protected EntityVillager spawnVillager(World world, StructureBoundingBox structBB, int posX, int posY, int posZ) {
		return spawnVillager(world, structBB, posX, posY, posZ, 0);
	}
	
	protected EntityVillager spawnVillager(World world, StructureBoundingBox structBB, int posX, int posY, int posZ, int index) {
		int globalX = getXWithOffset(posX, posZ);
		int globalY = getYWithOffset(posY);
		int globalZ = getZWithOffset(posX, posZ);
		
		EntityVillager entityvillager = null;
		if (structBB.isVecInside(globalX, globalY, globalZ)) {
			entityvillager = new EntityVillager(world, getVillagerType(index));
			entityvillager.setLocationAndAngles(globalX + 0.5D, globalY, globalZ + 0.5D, 0.0F, 0.0F);
			world.spawnEntityInWorld(entityvillager);
		}
		
		return entityvillager;
	}
	
	protected EntityLivingBase getEntity(World world, int index) {
		return null;
	}
	
	private Map<Integer, Block> biomesOPlentyStairMapping = null;
	
	private void initBOPStairMapping() {
		if (null == this.biomesOPlentyStairMapping) {
			this.biomesOPlentyStairMapping = new HashMap(14);
			this.biomesOPlentyStairMapping.put(Integer.valueOf(0), getBiomesOPlentyBlock("sacredoakStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(2), getBiomesOPlentyBlock("cherryStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(2), getBiomesOPlentyBlock("darkStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(3), getBiomesOPlentyBlock("firStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(4), getBiomesOPlentyBlock("holyStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(5), getBiomesOPlentyBlock("magicStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(6), getBiomesOPlentyBlock("mangroveStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(7), getBiomesOPlentyBlock("palmStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(8), getBiomesOPlentyBlock("redwoodStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(9), getBiomesOPlentyBlock("willowStairs"));
			
			this.biomesOPlentyStairMapping.put(Integer.valueOf(11), getBiomesOPlentyBlock("pineStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(12), getBiomesOPlentyBlock("hellBarkStairs"));
			this.biomesOPlentyStairMapping.put(Integer.valueOf(13), getBiomesOPlentyBlock("jacarandaStairs"));
		}
	}
	
	private Map<Integer, Pair<Block, Integer>> biomesOPlentyWoodslabMapping = null;
	
	private void initBOPWoodslabMapping() {
		if (null == this.biomesOPlentyWoodslabMapping) {
			
			this.biomesOPlentyWoodslabMapping = new HashMap(14);
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(0), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(0)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(1), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(1)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(2), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(2)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(3), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(3)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(4), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(4)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(5), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(5)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(6), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(6)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(7), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab1"), Integer.valueOf(7)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(8), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab2"), Integer.valueOf(0)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(9), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab2"), Integer.valueOf(1)));
			
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(11), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab2"), Integer.valueOf(2)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(12), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab2"), Integer.valueOf(3)));
			this.biomesOPlentyWoodslabMapping.put(Integer.valueOf(13), ImmutablePair.of(getBiomesOPlentyBlock("woodenSingleSlab2"), Integer.valueOf(4)));
		}
	}
	
	public BiomeSpecificBlock getSpecificBlock(Block block, ForgeDirection dir, BiomeGenBase biome) {
		return BiomeSpecificBlock.query(block, Dir.getMeta(block, dir, this.coordBaseMode), biome);
	}
	
	private Set<Block> stairBlocks = Sets.newHashSet(new Block[] { Blocks.brick_stairs, Blocks.stone_stairs, Blocks.nether_brick_stairs, Blocks.quartz_stairs, Blocks.sandstone_stairs,
			Blocks.stone_brick_stairs, Blocks.birch_stairs, Blocks.jungle_stairs, Blocks.oak_stairs, Blocks.spruce_stairs, Blocks.acacia_stairs, Blocks.dark_oak_stairs });
	
	public BiomeSpecificBlock getStairs(Block wood, ForgeDirection dir, BiomeGenBase biome) {
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		if ((Taverns.config.biomesOPlenty) && (Taverns.config.useBOPBlocks)) {
			initBOPStairMapping();
			if (planks.block == getBiomesOPlentyBlock("planks")) {
				Block bopStairBlock = this.biomesOPlentyStairMapping.get(Integer.valueOf(planks.metadata));
				if (null != bopStairBlock) {
					return BiomeSpecificBlock.queryVanilla(bopStairBlock, Dir.getMeta(Blocks.oak_stairs, dir, this.coordBaseMode), null);
				}
			}
		}
		
		BiomeSpecificBlock stairs = BiomeSpecificBlock.query(wood, Dir.getMeta(Blocks.oak_stairs, dir, this.coordBaseMode), biome);
		
		if (!this.stairBlocks.contains(stairs.block)) {
			Block replacementStairBlock = null;
			
			if (planks.block == Blocks.planks) {
				switch (planks.metadata) {
				case 0:
					replacementStairBlock = Blocks.oak_stairs;
					break;
				case 1:
					replacementStairBlock = Blocks.spruce_stairs;
					break;
				case 2:
					replacementStairBlock = Blocks.birch_stairs;
					break;
				case 3:
					replacementStairBlock = Blocks.jungle_stairs;
					break;
				case 4:
					replacementStairBlock = Blocks.acacia_stairs;
					break;
				case 5:
					replacementStairBlock = Blocks.dark_oak_stairs;
				}
			}
			if (planks.block == Blocks.brick_block) {
				replacementStairBlock = Blocks.brick_stairs;
			}
			if ((planks.block == Blocks.cobblestone) || (planks.block == Blocks.mossy_cobblestone)) {
				replacementStairBlock = Blocks.stone_stairs;
			}
			if ((planks.block == Blocks.nether_brick) || (planks.block == Blocks.netherrack)) {
				replacementStairBlock = Blocks.nether_brick_stairs;
			}
			if (planks.block == Blocks.quartz_block) {
				replacementStairBlock = Blocks.quartz_stairs;
			}
			if (planks.block == Blocks.sandstone) {
				replacementStairBlock = Blocks.sandstone_stairs;
			}
			if ((planks.block == Blocks.stonebrick) || (planks.block == Blocks.stone)) {
				replacementStairBlock = Blocks.stone_brick_stairs;
			}
			
			if (null != replacementStairBlock) {
				return BiomeSpecificBlock.queryVanilla(replacementStairBlock, Dir.getMeta(Blocks.oak_stairs, dir, this.coordBaseMode), null);
			}
		}
		
		return stairs;
	}
	
	public BiomeSpecificBlock getBench(Block wood, ForgeDirection dir, BiomeGenBase biome) {
		return getStairs(wood, dir, biome);
	}
	
	public BiomeSpecificBlock getWoodSlab(int plankMeta, ForgeDirection dir, BiomeGenBase biome) {
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, plankMeta, biome);
		if ((Taverns.config.biomesOPlenty) && (Taverns.config.useBOPBlocks)) {
			initBOPWoodslabMapping();
			if (planks.block == getBiomesOPlentyBlock("planks")) {
				Pair<Block, Integer> bopPlankBlock = this.biomesOPlentyWoodslabMapping.get(Integer.valueOf(planks.metadata));
				if ((null != bopPlankBlock) && (null != bopPlankBlock.getLeft())) {
					return BiomeSpecificBlock.queryVanilla(bopPlankBlock.getLeft(), bopPlankBlock.getRight().intValue() + (dir == ForgeDirection.UP ? 8 : 0), null);
				}
			}
		}
		BiomeSpecificBlock slab = BiomeSpecificBlock.query(Blocks.wooden_slab, plankMeta + (dir == ForgeDirection.UP ? 8 : 0), biome);
		
		if (slab.block == Blocks.stone_slab) {
			
			if (planks.block == Blocks.planks) {
				return BiomeSpecificBlock.queryVanilla(Blocks.wooden_slab, planks.metadata + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if (planks.block == Blocks.stone) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 0 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if (planks.block == Blocks.sandstone) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 1 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if ((planks.block == Blocks.cobblestone) || (planks.block == Blocks.mossy_cobblestone)) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 3 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if (planks.block == Blocks.brick_block) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 4 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if (planks.block == Blocks.stonebrick) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 5 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if ((planks.block == Blocks.nether_brick) || (planks.block == Blocks.netherrack)) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 6 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
			if (planks.block == Blocks.quartz_block) {
				return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 7 + (dir == ForgeDirection.UP ? 8 : 0), biome);
			}
		}
		
		return slab;
	}
	
	public BiomeSpecificBlock getMossyCobble(BiomeGenBase biome) {
		if ((Taverns.config.biomesOPlenty) && (Taverns.config.useBOPBlocks)) {
			
			BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
			
			if ((cobble.block == getBiomesOPlentyBlock("logs1")) || (cobble.block == getBiomesOPlentyBlock("logs2")) || (cobble.block == getBiomesOPlentyBlock("logs3"))
					|| (cobble.block == getBiomesOPlentyBlock("logs4"))) {
				
				return BiomeSpecificBlock.queryVanilla(getBiomesOPlentyBlock("logs3"), 2, null);
			}
		}
		return BiomeSpecificBlock.query(Blocks.mossy_cobblestone, 0, biome);
	}
	
	private static Block getBiomesOPlentyBlock(String blockName) {
		return Block.getBlockFromName("BiomesOPlenty:" + blockName);
	}
}
