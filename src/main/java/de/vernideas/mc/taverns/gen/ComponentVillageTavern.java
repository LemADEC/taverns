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

public class ComponentVillageTavern extends ComponentVillageBase {
	public static final String TAVERN_CHEST = "taverns_tavernChest";
	public static final WeightedRandomChestContent[] tavernChestContents = { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 1), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
			new WeightedRandomChestContent(Items.bed, 0, 1, 3, 30), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
			new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5),
			new WeightedRandomChestContent(new ItemStack(Blocks.obsidian), 1, 7, 5), new WeightedRandomChestContent(new ItemStack(Blocks.sapling), 3, 7, 5) };
	
	public static boolean initialized = false;
	
	public static void init(Settings config) {
		if (!initialized) {
			initialized = true;
			ChestGenHooks.getInfo("taverns_tavernChest").setMin(1);
			ChestGenHooks.getInfo("taverns_tavernChest").setMax(10);
			for (int i = 0; i < tavernChestContents.length; i++) {
				ChestGenHooks.addItem("taverns_tavernChest", tavernChestContents[i]);
			}
			net.minecraft.world.gen.structure.MapGenStructureIO.func_143031_a(ComponentVillageTavern.class, "taverns:ViT");
		}
	}
	
	public static boolean postInitialized = false;
	private static final int HEIGHT = 10;
	
	public static void postInit(Settings config) {
		if (!postInitialized) {
			postInitialized = true;
			if (!config.grimoireOfGaia2) {
			}
		}
	}
	
	public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random, int x, int y, int z, int direction, int type) {
		StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 14, 10, 8, direction);
		if (canVillageGoDeeper(structBB)) {
			if (null == StructureComponent.findIntersecting(pieces, structBB)) {
				return new ComponentVillageTavern(startPiece, type, random, structBB, direction);
			}
		}
		return null;
	}
	
	private int averageGroundLevel = -1;
	
	private boolean hasMadeChest;
	
	private int carpetColor;
	private int flower;
	private boolean villagerSpawned = false;
	
	public ComponentVillageTavern() {
	}
	
	public ComponentVillageTavern(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction) {
		super(startPiece, type, rnd, structBB, direction);
		this.carpetColor = rnd.nextInt(16);
		this.flower = (rnd.nextInt(16) - 4);
	}
	
	@Override
	protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Chest", this.hasMadeChest);
		par1NBTTagCompound.setInteger("CarpetC", this.carpetColor);
		par1NBTTagCompound.setInteger("Flower", this.flower);
		par1NBTTagCompound.setBoolean("VillagerSpawned", this.villagerSpawned);
	}
	
	@Override
	protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound);
		this.hasMadeChest = par1NBTTagCompound.getBoolean("Chest");
		this.carpetColor = par1NBTTagCompound.getInteger("CarpetC");
		this.flower = par1NBTTagCompound.getInteger("Flower");
		this.villagerSpawned = par1NBTTagCompound.getBoolean("VillagerSpawned");
	}
	
	@Override
	public boolean addComponentParts(World world, Random rnd, StructureBoundingBox bb) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = getAverageGroundLevel(world, bb);
			if (this.averageGroundLevel < 0) {
				return true;
			}
			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 10 - 2, 0);
		}
		
		BiomeGenBase biome = this.startPiece == null ? null : this.startPiece.biome;
		
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock woodWest = getSpecificBlock(Blocks.log, ForgeDirection.WEST, biome);
		BiomeSpecificBlock glassPane = BiomeSpecificBlock.query(Blocks.glass_pane, 0, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock carpet = BiomeSpecificBlock.query(Blocks.carpet, this.carpetColor, biome);
		
		BiomeSpecificBlock roofNorth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock roofSouth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.SOUTH, biome);
		BiomeSpecificBlock roofWest = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.WEST, biome);
		BiomeSpecificBlock roofEast = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		
		BiomeSpecificBlock stairsEast = getStairs(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		
		BiomeSpecificBlock benchWest = getBench(Blocks.oak_stairs, ForgeDirection.WEST, biome);
		BiomeSpecificBlock benchEast = getBench(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		BiomeSpecificBlock darkBenchNorth = getBench(Blocks.spruce_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock darkBenchEast = getBench(Blocks.spruce_stairs, ForgeDirection.EAST, biome);
		
		fill(world, bb, 0, 0, 0, 14, 9, 7, BiomeSpecificBlock.air, BiomeSpecificBlock.air);
		for (int xx = 0; xx < 14; xx++) {
			for (int zz = 0; zz < 8; zz++) {
				clearCurrentPositionBlocksUpwards(world, xx, 0, zz, bb);
				fillDownwards(world, Blocks.cobblestone, 0, xx, -1, zz, bb);
			}
		}
		
		fill(world, bb, 0, 0, 1, 14, 0, 7, dirt, dirt);
		fill(world, bb, 0, 0, 0, 14, 0, 0, gravel, gravel);
		fill(world, bb, 1, 0, 1, 4, 0, 6, cobble, cobble);
		fill(world, bb, 5, 0, 1, 12, 0, 6, planks, planks);
		fill(world, bb, 1, 4, 1, 12, 4, 6, planks, planks);
		fill(world, bb, 7, 8, 1, 12, 8, 6, planks, planks);
		
		fill(world, bb, 1, 1, 1, 1, 4, 1, woodUp, woodUp);
		fill(world, bb, 1, 1, 2, 1, 3, 5, cobble, cobble);
		fill(world, bb, 1, 1, 6, 1, 4, 6, woodUp, woodUp);
		
		fill(world, bb, 2, 3, 1, 3, 3, 1, planks, planks);
		fill(world, bb, 2, 1, 1, 3, 1, 1, cobble, cobble);
		fill(world, bb, 2, 2, 1, 3, 2, 1, glassPane, glassPane);
		fill(world, bb, 4, 1, 1, 4, 3, 1, woodUp, woodUp);
		fill(world, bb, 5, 3, 1, 6, 3, 1, woodUp, woodUp);
		fill(world, bb, 7, 1, 1, 7, 7, 1, woodUp, woodUp);
		placeBlock(world, woodWest, 4, 3, 1, bb);
		placeBlock(world, woodWest, 7, 3, 1, bb);
		fill(world, bb, 8, 1, 1, 11, 1, 1, cobble, cobble);
		fill(world, bb, 8, 2, 1, 11, 7, 1, planks, planks);
		fill(world, bb, 8, 2, 1, 11, 2, 1, glassPane, glassPane);
		fill(world, bb, 12, 1, 1, 12, 7, 1, woodUp, woodUp);
		
		fill(world, bb, 12, 1, 2, 12, 1, 5, cobble, cobble);
		fill(world, bb, 12, 2, 2, 12, 7, 5, planks, planks);
		fill(world, bb, 12, 2, 3, 12, 2, 4, glassPane, glassPane);
		fill(world, bb, 12, 6, 3, 12, 6, 4, glassPane, glassPane);
		fill(world, bb, 12, 1, 6, 12, 7, 6, woodUp, woodUp);
		
		fill(world, bb, 7, 1, 6, 11, 1, 6, cobble, cobble);
		fill(world, bb, 7, 2, 6, 11, 7, 6, planks, planks);
		placeBlock(world, glassPane, 10, 6, 6, bb);
		fill(world, bb, 6, 1, 6, 6, 8, 6, woodUp, woodUp);
		placeBlock(world, woodUp, 5, 3, 6, bb);
		fill(world, bb, 4, 1, 6, 4, 3, 6, woodUp, woodUp);
		fill(world, bb, 2, 1, 6, 3, 3, 6, cobble, cobble);
		
		fill(world, bb, 7, 5, 2, 7, 7, 5, planks, planks);
		fill(world, bb, 6, 5, 5, 6, 8, 5, planks, planks);
		
		for (int xx = 0; xx <= 13; xx++) {
			placeBlock(world, roofNorth, xx, 4, 0, bb);
			placeBlock(world, roofSouth, xx, 4, 7, bb);
		}
		
		for (int zz = 0; zz <= 7; zz++) {
			placeBlock(world, roofEast, 0, 4, zz, bb);
			placeBlock(world, roofWest, 13, 4, zz, bb);
		}
		
		for (int xx = 6; xx <= 13; xx++) {
			placeBlock(world, roofNorth, xx, 7, 0, bb);
			placeBlock(world, roofSouth, xx, 7, 7, bb);
			placeBlock(world, roofNorth, xx, 8, 1, bb);
			placeBlock(world, roofSouth, xx, 8, 6, bb);
		}
		
		for (int xx = 7; xx <= 13; xx++) {
			placeBlock(world, roofNorth, xx, 9, 2, bb);
			placeBlock(world, roofSouth, xx, 9, 5, bb);
		}
		
		for (int zz = 2; zz <= 5; zz++) {
			placeBlock(world, roofEast, 6, 8, zz, bb);
		}
		
		for (int zz = 3; zz <= 4; zz++) {
			placeBlock(world, roofWest, 13, 9, zz, bb);
			placeBlock(world, roofEast, 12, 9, zz, bb);
			placeBlock(world, roofEast, 7, 9, zz, bb);
			placeBlock(world, roofWest, 8, 9, zz, bb);
		}
		
		for (int zz = 5; zz <= 7; zz++) {
			placeBlock(world, roofEast, 5, 7, zz, bb);
		}
		
		fillWithAir(world, bb, 6, 8, 4, 6, 8, 4);
		
		fill(world, bb, 1, 5, 6, 5, 5, 6, fence, fence);
		fill(world, bb, 1, 5, 1, 1, 5, 5, fence, fence);
		fill(world, bb, 2, 5, 1, 6, 5, 1, fence, fence);
		
		for (int yy = 5; yy <= 8; yy++) {
			placeBlock(world, Blocks.ladder, ForgeDirection.WEST, 6, yy, 4, bb);
		}
		
		placeBlock(world, Blocks.bookshelf, 0, 11, 5, 2, bb);
		placeBlock(world, Blocks.torch, 0, 11, 6, 2, bb);
		
		placeBlock(world, Blocks.bed, ForgeDirection.SOUTH, 10, 5, 3, bb);
		placeBlock(world, Blocks.bed, Dir.getMeta(Blocks.bed, ForgeDirection.SOUTH, this.coordBaseMode) + 8, 10, 5, 2, bb);
		
		if (!this.hasMadeChest) {
			int chestX = getXWithOffset(9, 2);
			int chestY = getYWithOffset(5);
			int chestZ = getZWithOffset(9, 2);
			if (bb.isVecInside(chestX, chestY, chestZ)) {
				this.hasMadeChest = true;
				generateStructureChestContents(world, bb, rnd, 9, 5, 2, ChestGenHooks.getItems("taverns_tavernChest", rnd), ChestGenHooks.getCount("taverns_tavernChest", rnd));
			}
		}
		
		placeDoor(world, bb, rnd, 7, 5, 2, ForgeDirection.WEST);
		placeDoor(world, bb, rnd, 6, 1, 1, ForgeDirection.SOUTH);
		placeDoor(world, bb, rnd, 5, 1, 1, ForgeDirection.SOUTH);
		placeDoor(world, bb, rnd, 5, 1, 6, ForgeDirection.NORTH);
		
		fill(world, bb, 4, 1, 5, 4, 3, 5, woodUp, woodUp);
		fill(world, bb, 7, 1, 2, 7, 3, 2, woodUp, woodUp);
		
		placeBlock(world, stairsEast, 7, 1, 5, bb);
		placeBlock(world, stairsEast, 8, 2, 5, bb);
		placeBlock(world, stairsEast, 9, 3, 5, bb);
		placeBlock(world, stairsEast, 10, 4, 5, bb);
		fillWithAir(world, bb, 7, 4, 5, 9, 4, 5);
		
		placeBlock(world, planks, 8, 1, 5, bb);
		placeBlock(world, darkBenchNorth, 9, 1, 5, bb);
		placeBlock(world, darkBenchNorth, 10, 1, 5, bb);
		placeBlock(world, darkBenchNorth, 11, 1, 5, bb);
		placeBlock(world, darkBenchEast, 11, 1, 4, bb);
		placeBlock(world, darkBenchEast, 11, 1, 3, bb);
		placeBlock(world, planks, 11, 1, 2, bb);
		placeBlock(world, benchEast, 10, 1, 2, bb);
		placeBlock(world, fence, 9, 1, 2, bb);
		placeBlock(world, Blocks.wooden_pressure_plate, 0, 9, 2, 2, bb);
		placeBlock(world, benchWest, 8, 1, 2, bb);
		
		fill(world, bb, 4, 1, 2, 4, 1, 3, planks, planks);
		fill(world, bb, 2, 1, 2, 2, 1, 3, planks, planks);
		
		placeBlock(world, Blocks.brewing_stand, 0, 2, 2, 3, bb);
		placeBlock(world, Blocks.cauldron, rnd.nextInt(3) + 1, 3, 1, 5, bb);
		placeBlock(world, Blocks.trapdoor, ForgeDirection.NORTH, 4, 2, 4, bb);
		
		placeBlock(world, Blocks.furnace, 0, 2, 1, 5, bb);
		setMetadata(world, Dir.getMeta(Blocks.furnace, ForgeDirection.SOUTH, this.coordBaseMode), 2, 1, 5, bb);
		
		fill(world, bb, 8, 1, 3, 10, 1, 4, carpet, carpet);
		
		if (this.flower >= 0) {
			placeBlock(world, Blocks.flower_pot, this.flower, 6, 6, 1, bb);
		}
		
		placeBlock(world, Blocks.torch, 0, 1, 6, 6, bb);
		placeBlock(world, Blocks.torch, 0, 1, 6, 1, bb);
		placeBlock(world, Blocks.torch, 0, 9, 9, 4, bb);
		placeBlock(world, Blocks.torch, 0, 4, 3, 0, bb);
		placeBlock(world, Blocks.torch, 0, 7, 3, 0, bb);
		placeBlock(world, Blocks.torch, 0, 5, 3, 7, bb);
		placeBlock(world, Blocks.torch, 0, 2, 3, 5, bb);
		
		placeBlock(world, Blocks.stone, 0, 4, 2, 2, bb);
		placeBlock(world, Blocks.torch, ForgeDirection.NORTH, 4, 3, 2, bb);
		placeAir(world, 4, 2, 2, bb);
		
		placeBlock(world, Blocks.stone, 0, 9, 2, 2, bb);
		placeBlock(world, Blocks.torch, ForgeDirection.NORTH, 9, 3, 2, bb);
		placeBlock(world, Blocks.wooden_pressure_plate, 0, 9, 2, 2, bb);
		
		if (!this.villagerSpawned) {
			EntityVillager villager = spawnVillager(world, bb, 2, 1, 2);
			if (null != villager) {
				this.villagerSpawned = true;
				int globalX = getXWithOffset(2, 2);
				int globalY = getYWithOffset(1);
				int globalZ = getZWithOffset(2, 2);
				villager.tasks.addTask(4, new GuardAI(villager, new ChunkCoordinates(globalX, globalY, globalZ), 1.0F, 48.0F, new TimePeriod(0.15D, 0.75D), false));
			}
		}
		
		return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned) {
		return Taverns.config.barWenchID > 0 ? Taverns.config.barWenchID : super.getVillagerType(alreadySpawned);
	}
}
