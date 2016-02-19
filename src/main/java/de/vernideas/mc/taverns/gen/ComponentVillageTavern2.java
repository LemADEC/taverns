package de.vernideas.mc.taverns.gen;

import de.vernideas.mc.common.gen.BiomeSpecificBlock;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageTavern2 extends ComponentVillageBase {
	public static boolean initialized = false;
	private static final int HEIGHT = 10;
	
	public static void init(Settings config) {
		if (!initialized) {
			initialized = true;
			
			ComponentVillageTavern.init(config);
			
			MapGenStructureIO.func_143031_a(ComponentVillageTavern2.class, "taverns:ViT2");
		}
	}
	
	public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random, int x, int y, int z, int direction, int type) {
		StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 14, 10, 8, direction);
		if (canVillageGoDeeper(structBB)) {
			if (null == StructureComponent.findIntersecting(pieces, structBB)) {
				return new ComponentVillageTavern2(startPiece, type, random, structBB, direction);
			}
		}
		return null;
	}
	
	private int averageGroundLevel = -1;
	
	private boolean hasMadeChest;
	
	private int carpetColor;
	private int flower;
	private boolean villagerSpawned = false;
	
	public ComponentVillageTavern2() {
	}
	
	public ComponentVillageTavern2(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction) {
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
			this.averageGroundLevel = (getAverageGroundLevel(world, bb) + 5);
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
		
		return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned) {
		return Taverns.config.barWenchID > 0 ? Taverns.config.barWenchID : super.getVillagerType(alreadySpawned);
	}
}
