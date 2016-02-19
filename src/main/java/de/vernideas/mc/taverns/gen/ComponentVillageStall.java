package de.vernideas.mc.taverns.gen;

import de.vernideas.mc.common.ai.GuardAI;
import de.vernideas.mc.common.gen.BiomeSpecificBlock;
import de.vernideas.mc.common.util.TimePeriod;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;
import de.vernideas.mc.taverns.direction.Dir;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageStall extends ComponentVillageBase {
	public static final String STALL_CHEST = "taverns_stallChest";
	public static final WeightedRandomChestContent[] stallChestContents = { new WeightedRandomChestContent(Items.diamond, 0, 1, 1, 3), new WeightedRandomChestContent(Items.golden_apple, 1, 1, 1, 1),
			new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 40),
			new WeightedRandomChestContent(Items.golden_carrot, 0, 1, 3, 10), new WeightedRandomChestContent(Items.apple, 0, 1, 8, 50),
			new WeightedRandomChestContent(new ItemStack(Blocks.pumpkin), 1, 3, 25), new WeightedRandomChestContent(new ItemStack(Blocks.hay_block), 1, 5, 30),
			new WeightedRandomChestContent(Items.carrot, 0, 1, 5, 30), new WeightedRandomChestContent(Items.leather, 0, 1, 9, 30), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10) };
	
	public static boolean initialized = false;
	
	public static void init(Settings config) {
		if (!initialized) {
			initialized = true;
			ChestGenHooks.getInfo("taverns_stallChest").setMin(1);
			ChestGenHooks.getInfo("taverns_stallChest").setMax(10);
			for (int i = 0; i < stallChestContents.length; i++) {
				ChestGenHooks.addItem("taverns_stallChest", stallChestContents[i]);
			}
			MapGenStructureIO.func_143031_a(ComponentVillageStall.class, "taverns:ViS");
		}
	}
	
	public static boolean postInitialized = false;
	private static final int HEIGHT = 7;
	
	public static void postInit(Settings config) {
		if (!postInitialized) {
			postInitialized = true;
			if (!config.grimoireOfGaia2) {
			}
		}
	}
	
	public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random, int x, int y, int z, int direction, int type) {
		StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 11, 7, 10, direction);
		if (canVillageGoDeeper(structBB)) {
			if (null == StructureComponent.findIntersecting(pieces, structBB)) {
				return new ComponentVillageStall(startPiece, type, random, structBB, direction);
			}
		}
		return null;
	}
	
	private int averageGroundLevel = -1;
	private boolean horseSpawned = false;
	private boolean villagerSpawned = false;
	private int chestPosX;
	private boolean hasMadeChest = false;
	
	public ComponentVillageStall() {
	}
	
	public ComponentVillageStall(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction) {
		super(startPiece, type, rnd, structBB, direction);
		this.chestPosX = (rnd.nextBoolean() ? 3 : 7);
	}
	
	@Override
	protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("HorseSpawned", this.horseSpawned);
		par1NBTTagCompound.setBoolean("VillagerSpawned", this.villagerSpawned);
		par1NBTTagCompound.setInteger("ChestPosX", this.chestPosX);
		par1NBTTagCompound.setBoolean("Chest", this.hasMadeChest);
	}
	
	@Override
	protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound);
		this.horseSpawned = par1NBTTagCompound.getBoolean("HorseSpawned");
		this.villagerSpawned = par1NBTTagCompound.getBoolean("VillagerSpawned");
		this.chestPosX = par1NBTTagCompound.getInteger("ChestPosX");
		this.hasMadeChest = par1NBTTagCompound.getBoolean("Chest");
	}
	
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox bb) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = getAverageGroundLevel(world, bb);
			if (this.averageGroundLevel < 0) {
				return true;
			}
			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 2, 0);
		}
		
		BiomeGenBase biome = this.startPiece == null ? null : this.startPiece.biome;
		
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
		BiomeSpecificBlock cobbleWall = BiomeSpecificBlock.query(Blocks.cobblestone_wall, 0, biome);
		BiomeSpecificBlock cobbleMossy = getMossyCobble(biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock woodNorth = getSpecificBlock(Blocks.log, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock woodWest = getSpecificBlock(Blocks.log, ForgeDirection.WEST, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock woodSlab = getWoodSlab(0, ForgeDirection.DOWN, biome);
		
		BiomeSpecificBlock roofNorth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock roofSouth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.SOUTH, biome);
		BiomeSpecificBlock roofNorthUpside = BiomeSpecificBlock.query(Blocks.oak_stairs, Dir.getMeta(Blocks.oak_stairs, ForgeDirection.NORTH, this.coordBaseMode) + 4, biome);
		
		BiomeSpecificBlock roofSouthUpside = BiomeSpecificBlock.query(Blocks.oak_stairs, Dir.getMeta(Blocks.oak_stairs, ForgeDirection.SOUTH, this.coordBaseMode) + 4, biome);
		
		fill(world, bb, 0, 0, 0, 10, 6, 9, BiomeSpecificBlock.air, BiomeSpecificBlock.air);
		for (int xx = 0; xx < 11; xx++) {
			for (int zz = 0; zz < 10; zz++) {
				clearCurrentPositionBlocksUpwards(world, xx, 0, zz, bb);
				fillDownwards(world, Blocks.cobblestone, 0, xx, -1, zz, bb);
			}
		}
		
		fill(world, bb, 0, 0, 0, 10, 0, 9, dirt, dirt);
		fill(world, bb, 2, 0, 2, 8, 0, 7, cobble, cobble);
		fillRandomly(world, bb, random, 0.2F, 2, 0, 2, 8, 0, 7, cobbleMossy, cobbleMossy);
		fill(world, bb, 4, 0, 0, 6, 0, 1, gravel, gravel);
		
		placeTorch(world, bb, 2, 3, 2, ForgeDirection.SOUTH);
		placeTorch(world, bb, 5, 3, 2, ForgeDirection.SOUTH);
		placeTorch(world, bb, 8, 3, 2, ForgeDirection.SOUTH);
		placeTorch(world, bb, 3, 4, 5, ForgeDirection.EAST);
		placeTorch(world, bb, 7, 4, 5, ForgeDirection.WEST);
		
		for (int yy = 1; yy <= 3; yy++) {
			placeBlock(world, woodUp, 2, yy, 3, bb);
			placeBlock(world, woodUp, 5, yy, 3, bb);
			placeBlock(world, woodUp, 8, yy, 3, bb);
			placeBlock(world, woodUp, 2, yy, 7, bb);
			placeBlock(world, woodUp, 5, yy, 7, bb);
			placeBlock(world, woodUp, 8, yy, 7, bb);
		}
		for (int xx = 2; xx <= 8; xx++) {
			placeBlock(world, woodWest, xx, 4, 3, bb);
			placeBlock(world, woodWest, xx, 6, 5, bb);
			placeBlock(world, woodWest, xx, 4, 7, bb);
		}
		placeBlock(world, woodWest, 1, 6, 5, bb);
		placeBlock(world, woodWest, 9, 6, 5, bb);
		for (int zz = 4; zz <= 6; zz++) {
			placeBlock(world, woodNorth, 2, 4, zz, bb);
			placeBlock(world, woodNorth, 5, 5, zz, bb);
			placeBlock(world, woodNorth, 8, 4, zz, bb);
		}
		
		for (int xx = 2; xx <= 8; xx += 6) {
			placeBlock(world, cobble, xx, 1, 4, bb);
			placeBlock(world, planks, xx, 1, 5, bb);
			placeBlock(world, cobble, xx, 1, 6, bb);
			
			placeBlock(world, planks, xx, 2, 4, bb);
			placeBlock(world, cobble, xx, 2, 5, bb);
			placeBlock(world, planks, xx, 2, 6, bb);
			
			placeBlock(world, cobble, xx, 3, 4, bb);
			placeBlock(world, planks, xx, 3, 5, bb);
			placeBlock(world, cobble, xx, 3, 6, bb);
			
			placeBlock(world, cobble, xx, 5, 4, bb);
			placeBlock(world, Blocks.stone_slab, 11, xx, 5, 5, bb);
			placeBlock(world, cobble, xx, 5, 6, bb);
		}
		placeBlock(world, cobble, 3, 1, 7, bb);
		placeBlock(world, cobble, 4, 1, 7, bb);
		placeBlock(world, cobble, 6, 1, 7, bb);
		placeBlock(world, cobble, 7, 1, 7, bb);
		placeBlock(world, cobbleWall, 3, 2, 7, bb);
		placeBlock(world, cobbleWall, 4, 2, 7, bb);
		placeBlock(world, cobbleWall, 6, 2, 7, bb);
		placeBlock(world, cobbleWall, 7, 2, 7, bb);
		
		placeBlock(world, fence, 5, 1, 4, bb);
		placeBlock(world, fence, 5, 1, 5, bb);
		placeBlock(world, fence, 5, 1, 6, bb);
		placeBlock(world, fence, 5, 3, 4, bb);
		placeBlock(world, fence, 5, 3, 6, bb);
		placeBlock(world, Blocks.fence_gate, ForgeDirection.NORTH, 3, 1, 3, bb);
		placeBlock(world, Blocks.fence_gate, ForgeDirection.NORTH, 4, 1, 3, bb);
		placeBlock(world, Blocks.fence_gate, ForgeDirection.NORTH, 6, 1, 3, bb);
		placeBlock(world, Blocks.fence_gate, ForgeDirection.NORTH, 7, 1, 3, bb);
		
		for (int xx = 1; xx <= 9; xx++) {
			placeBlock(world, roofNorth, xx, 4, 2, bb);
			placeBlock(world, roofNorth, xx, 5, 3, bb);
			placeBlock(world, woodSlab, xx, 6, 4, bb);
			placeBlock(world, woodSlab, xx, 6, 6, bb);
			placeBlock(world, roofSouth, xx, 5, 7, bb);
			placeBlock(world, roofSouth, xx, 4, 8, bb);
		}
		placeBlock(world, roofSouthUpside, 1, 4, 3, bb);
		placeBlock(world, roofSouthUpside, 9, 4, 3, bb);
		placeBlock(world, roofSouthUpside, 1, 5, 4, bb);
		placeBlock(world, roofSouthUpside, 9, 5, 4, bb);
		placeBlock(world, roofNorthUpside, 1, 5, 6, bb);
		placeBlock(world, roofNorthUpside, 9, 5, 6, bb);
		placeBlock(world, roofNorthUpside, 1, 4, 7, bb);
		placeBlock(world, roofNorthUpside, 9, 4, 7, bb);
		
		placeBlock(world, Blocks.hay_block, random.nextInt(3) * 4, 3, 1, 6, bb);
		placeBlock(world, Blocks.hay_block, random.nextInt(3) * 4, 7, 1, 6, bb);
		if (!this.hasMadeChest) {
			int chestX = getXWithOffset(this.chestPosX, 6);
			int chestY = getYWithOffset(0);
			int chestZ = getZWithOffset(this.chestPosX, 6);
			if (bb.isVecInside(chestX, chestY, chestZ)) {
				this.hasMadeChest = true;
				generateStructureChestContents(world, bb, random, this.chestPosX, 0, 6, ChestGenHooks.getItems("taverns_stallChest", random), ChestGenHooks.getCount("taverns_stallChest", random));
			}
		}
		
		if (!this.horseSpawned) {
			this.horseSpawned = (spawnEntity(world, bb, random.nextBoolean() ? 4 : 6, 1, 5, 1) == 1);
		}
		if (!this.villagerSpawned) {
			EntityVillager villager = spawnVillager(world, bb, 5, 1, 1);
			if (null != villager) {
				this.villagerSpawned = true;
				int globalX = getXWithOffset(5, 1);
				int globalY = getYWithOffset(1);
				int globalZ = getZWithOffset(5, 1);
				villager.tasks.addTask(4, new GuardAI(villager, new ChunkCoordinates(globalX, globalY, globalZ), 3.0F, 64.0F, new TimePeriod(0.02D, 0.45D), false));
			}
		}
		
		return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned) {
		return Taverns.config.hostlerID > 0 ? Taverns.config.hostlerID : super.getVillagerType(alreadySpawned);
	}
	
	@Override
	protected net.minecraft.entity.EntityLivingBase getEntity(World world, int index) {
		return new EntityHorse(world);
	}
}
