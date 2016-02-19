package de.vernideas.mc.taverns.gen;

import de.vernideas.mc.common.ai.GuardAI;
import de.vernideas.mc.common.gen.BiomeSpecificBlock;
import de.vernideas.mc.common.util.TimePeriod;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;
import de.vernideas.mc.taverns.direction.Dir;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageBakery extends ComponentVillageBase {
	public static final String BAKERY_CHEST = "taverns_bakeryChest";
	public static final WeightedRandomChestContent[] bakeryChestContents = { new WeightedRandomChestContent(Items.wheat, 0, 1, 10, 12), new WeightedRandomChestContent(Items.reeds, 0, 1, 8, 15),
			new WeightedRandomChestContent(Items.sugar, 0, 1, 10, 12), new WeightedRandomChestContent(Items.egg, 0, 1, 12, 8), new WeightedRandomChestContent(Items.milk_bucket, 0, 1, 2, 3),
			new WeightedRandomChestContent(Items.bucket, 0, 1, 3, 6), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 1, 15, 5),
			new WeightedRandomChestContent(new ItemStack(Blocks.pumpkin), 1, 2, 3), new WeightedRandomChestContent(Items.melon_seeds, 0, 1, 3, 2),
			new WeightedRandomChestContent(new ItemStack(Blocks.melon_block), 1, 2, 1), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 1),
			new WeightedRandomChestContent(Items.apple, 0, 1, 3, 3), new WeightedRandomChestContent(Items.carrot, 0, 1, 3, 3) };
	
	public static boolean initialized = false;
	
	public static void init(Settings config) {
		if (!initialized) {
			initialized = true;
			ChestGenHooks.getInfo("taverns_bakeryChest").setMin(1);
			ChestGenHooks.getInfo("taverns_bakeryChest").setMax(8);
			for (int i = 0; i < bakeryChestContents.length; i++) {
				ChestGenHooks.addItem("taverns_bakeryChest", bakeryChestContents[i]);
			}
			net.minecraft.world.gen.structure.MapGenStructureIO.func_143031_a(ComponentVillageBakery.class, "taverns:ViBak");
		}
	}
	
	public static boolean postInitialized = false;
	private static final int HEIGHT = 14;
	
	public static void postInit(Settings config) {
		if (!postInitialized) {
			postInitialized = true;
			if (!config.grimoireOfGaia2) {
			}
		}
	}
	
	public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random, int x, int y, int z, int direction, int type) {
		StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 12, 14, 9, direction);
		if (canVillageGoDeeper(structBB)) {
			if (null == StructureComponent.findIntersecting(pieces, structBB)) {
				return new ComponentVillageBakery(startPiece, type, random, structBB, direction);
			}
		}
		return null;
	}
	
	private int averageGroundLevel = -1;
	
	private boolean hasMadeChest;
	private int carpetColor;
	private boolean villagerSpawned = false;
	
	public ComponentVillageBakery() {
	}
	
	public ComponentVillageBakery(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction) {
		super(startPiece, type, rnd, structBB, direction);
		this.carpetColor = rnd.nextInt(16);
	}
	
	@Override
	protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Chest", this.hasMadeChest);
		par1NBTTagCompound.setInteger("CarpetC", this.carpetColor);
		par1NBTTagCompound.setBoolean("VillagerSpawned", this.villagerSpawned);
	}
	
	@Override
	protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound);
		this.hasMadeChest = par1NBTTagCompound.getBoolean("Chest");
		this.carpetColor = par1NBTTagCompound.getInteger("CarpetC");
		this.villagerSpawned = par1NBTTagCompound.getBoolean("VillagerSpawned");
	}
	
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox bb) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = getAverageGroundLevel(world, bb);
			if (this.averageGroundLevel < 0) {
				return true;
			}
			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 14 - 2, 0);
		}
		
		BiomeGenBase biome = this.startPiece == null ? null : this.startPiece.biome;
		
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock grass = BiomeSpecificBlock.queryVanilla(Blocks.grass, 0, null);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock woodWest = getSpecificBlock(Blocks.log, ForgeDirection.WEST, biome);
		BiomeSpecificBlock woodNorth = getSpecificBlock(Blocks.log, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock woodSlabDown = getWoodSlab(0, ForgeDirection.DOWN, biome);
		BiomeSpecificBlock woodSlabUp = getWoodSlab(0, ForgeDirection.UP, biome);
		BiomeSpecificBlock glassPane = BiomeSpecificBlock.query(Blocks.glass_pane, 0, biome);
		
		BiomeSpecificBlock stoneSlabDouble = BiomeSpecificBlock.query(Blocks.double_stone_slab, 0, biome);
		
		BiomeSpecificBlock roofNorth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock roofSouth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.SOUTH, biome);
		
		BiomeSpecificBlock cobbleChimney = BiomeSpecificBlock.queryVanilla(Blocks.cobblestone, 0, null);
		BiomeSpecificBlock cobbleSlopeEast = BiomeSpecificBlock.queryVanilla(Blocks.stone_stairs, Dir.getMeta(Blocks.stone_stairs, ForgeDirection.EAST, this.coordBaseMode), null);
		BiomeSpecificBlock cobbleSlopeNorth = BiomeSpecificBlock.queryVanilla(Blocks.stone_stairs, Dir.getMeta(Blocks.stone_stairs, ForgeDirection.NORTH, this.coordBaseMode), null);
		BiomeSpecificBlock cobbleSlopeSouth = BiomeSpecificBlock.queryVanilla(Blocks.stone_stairs, Dir.getMeta(Blocks.stone_stairs, ForgeDirection.SOUTH, this.coordBaseMode), null);
		
		BiomeSpecificBlock carpet = BiomeSpecificBlock.query(Blocks.carpet, this.carpetColor, biome);
		BiomeSpecificBlock smoke = BiomeSpecificBlock.queryVanilla(Blocks.web, 0, null);
		
		fill(world, bb, 0, 0, 0, 11, 5, 6, BiomeSpecificBlock.air, BiomeSpecificBlock.air, false);
		for (int xx = 0; xx < 12; xx++) {
			for (int zz = 0; zz < 7; zz++) {
				clearCurrentPositionBlocksUpwards(world, xx, 0, zz, bb);
				fillDownwards(world, Blocks.cobblestone, 0, xx, -1, zz, bb);
			}
		}
		
		fill(world, bb, 0, 0, 0, 11, 0, 6, dirt, dirt);
		fill(world, bb, 1, 0, 0, 10, 0, 6, cobble, cobble);
		fill(world, bb, 2, 0, 1, 4, 0, 5, planks, planks);
		fill(world, bb, 5, 0, 1, 9, 0, 5, stoneSlabDouble, stoneSlabDouble);
		
		placeTorch(world, bb, 0, 3, 1, ForgeDirection.WEST);
		placeTorch(world, bb, 0, 3, 5, ForgeDirection.WEST);
		placeTorch(world, bb, 2, 3, 1, ForgeDirection.EAST);
		placeTorch(world, bb, 2, 3, 5, ForgeDirection.EAST);
		placeTorch(world, bb, 5, 3, 5, ForgeDirection.SOUTH);
		placeTorch(world, bb, 9, 3, 2, ForgeDirection.WEST);
		placeTorch(world, bb, 9, 3, 4, ForgeDirection.WEST);
		placeTorch(world, bb, 11, 3, 0, ForgeDirection.EAST);
		placeTorch(world, bb, 11, 3, 6, ForgeDirection.EAST);
		placeTorch(world, bb, 1, 3, -1, ForgeDirection.SOUTH);
		placeTorch(world, bb, 4, 3, -1, ForgeDirection.SOUTH);
		placeTorch(world, bb, 9, 3, -1, ForgeDirection.SOUTH);
		placeTorch(world, bb, 1, 3, 7, ForgeDirection.NORTH);
		placeTorch(world, bb, 6, 3, 7, ForgeDirection.NORTH);
		placeTorch(world, bb, 10, 3, 7, ForgeDirection.NORTH);
		placeTorch(world, bb, 9, 8, 3, ForgeDirection.WEST);
		
		fill(world, bb, 1, 1, 0, 1, 5, 6, cobble, cobble);
		fill(world, bb, 1, 2, 1, 1, 4, 5, planks, planks);
		fill(world, bb, 1, 6, 2, 1, 6, 4, cobble, cobble);
		
		fill(world, bb, 10, 1, 0, 10, 5, 6, cobble, cobble);
		fill(world, bb, 10, 6, 2, 10, 6, 4, cobble, cobble);
		
		fill(world, bb, 4, 1, 0, 9, 1, 0, cobble, cobble);
		placeBlock(world, cobble, 2, 1, 0, bb);
		fill(world, bb, 2, 2, 0, 9, 4, 0, planks, planks);
		placeAir(world, 3, 2, 0, bb);
		
		fill(world, bb, 2, 1, 6, 9, 1, 6, cobble, cobble);
		fill(world, bb, 2, 2, 6, 9, 4, 6, planks, planks);
		
		fill(world, bb, 1, 2, 2, 1, 2, 4, glassPane, glassPane);
		fill(world, bb, 5, 2, 0, 8, 2, 0, glassPane, glassPane);
		placeBlock(world, glassPane, 3, 2, 6, bb);
		placeBlock(world, glassPane, 4, 2, 6, bb);
		placeBlock(world, glassPane, 8, 2, 6, bb);
		
		for (int xx = 1; xx <= 10; xx++) {
			placeBlock(world, roofNorth, xx, 4, -1, bb);
			placeBlock(world, roofNorth, xx, 5, 0, bb);
			placeBlock(world, roofNorth, xx, 6, 1, bb);
			placeBlock(world, roofNorth, xx, 7, 2, bb);
			placeBlock(world, woodWest, xx, 7, 3, bb);
			placeBlock(world, roofSouth, xx, 7, 4, bb);
			placeBlock(world, roofSouth, xx, 6, 5, bb);
			placeBlock(world, roofSouth, xx, 5, 6, bb);
			placeBlock(world, roofSouth, xx, 4, 7, bb);
		}
		fill(world, bb, 2, 6, 2, 2, 6, 4, woodNorth, woodNorth);
		fill(world, bb, 9, 6, 2, 9, 6, 4, woodNorth, woodNorth);
		
		for (int yy = 2; yy <= 3; yy++) {
			for (int zz = 1; zz <= 5; zz += 2) {
				placeBlock(world, Blocks.furnace, 0, 9, yy, zz, bb);
				setMetadata(world, Dir.getMeta(Blocks.furnace, ForgeDirection.WEST, this.coordBaseMode), 9, yy, zz, bb);
			}
		}
		for (int zz = 1; zz <= 5; zz++) {
			placeBlock(world, stoneSlabDouble, 9, 4, zz, bb);
			placeBlock(world, cobbleSlopeEast, 9, 5, zz, bb);
		}
		fill(world, bb, 10, 1, 1, 10, 5, 5, cobbleChimney, cobbleChimney);
		fill(world, bb, 10, 6, 2, 10, 7, 4, cobbleChimney, cobbleChimney);
		placeBlock(world, cobbleSlopeNorth, 10, 8, 2, bb);
		placeBlock(world, cobbleChimney, 10, 8, 3, bb);
		placeBlock(world, cobbleSlopeSouth, 10, 8, 4, bb);
		placeBlock(world, cobbleChimney, 10, 9, 3, bb);
		
		fill(world, bb, 3, 1, 1, 3, 1, 3, carpet, carpet);
		placeBlock(world, smoke, 10, 10, 3, bb);
		fillRandomly(world, bb, random, 0.1F, 9, 11, 2, 11, 13, 4, smoke, smoke);
		placeBlock(world, stoneSlabDouble, 5, 1, 3, bb);
		placeBlock(world, stoneSlabDouble, 5, 1, 5, bb);
		placeBlock(world, stoneSlabDouble, 9, 1, 1, bb);
		placeBlock(world, stoneSlabDouble, 9, 1, 3, bb);
		placeBlock(world, stoneSlabDouble, 9, 1, 5, bb);
		placeBlock(world, Blocks.trapdoor, Dir.getMeta(Blocks.trapdoor, ForgeDirection.NORTH, this.coordBaseMode) + 8, 5, 1, 4, bb);
		placeBlock(world, Blocks.stone_slab, 8, 5, 1, 1, bb);
		placeBlock(world, Blocks.stone_slab, 8, 5, 1, 2, bb);
		
		placeBlock(world, Blocks.crafting_table, 0, 7, 1, 2, bb);
		placeBlock(world, Blocks.crafting_table, 0, 7, 1, 3, bb);
		
		placeDoor(world, bb, random, 3, 1, 0, ForgeDirection.SOUTH);
		
		if (!this.hasMadeChest) {
			int chestX = getXWithOffset(2, 5);
			int chestY = getYWithOffset(1);
			int chestZ = getZWithOffset(2, 5);
			if (bb.isVecInside(chestX, chestY, chestZ)) {
				this.hasMadeChest = true;
				generateStructureChestContents(world, bb, random, 2, 1, 5, ChestGenHooks.getItems("taverns_bakeryChest", random), ChestGenHooks.getCount("taverns_bakeryChest", random) + 1);
			}
		}
		
		if (!this.villagerSpawned) {
			EntityVillager villager = spawnVillager(world, bb, 6, 1, 3);
			if (null != villager) {
				this.villagerSpawned = true;
				int globalX = getXWithOffset(6, 3);
				int globalY = getYWithOffset(1);
				int globalZ = getZWithOffset(6, 3);
				villager.tasks.addTask(4, new GuardAI(villager, new ChunkCoordinates(globalX, globalY, globalZ), 4.0F, 48.0F, new TimePeriod(0.01D, 0.47D), false));
			}
		}
		
		return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned) {
		return Taverns.config.bakerID > 0 ? Taverns.config.bakerID : super.getVillagerType(alreadySpawned);
	}
}
