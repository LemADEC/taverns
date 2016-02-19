package de.vernideas.mc.taverns.gen;

import cpw.mods.fml.common.registry.VillagerRegistry;
import de.vernideas.mc.taverns.Settings;
import de.vernideas.mc.taverns.Taverns;

import java.util.List;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class BarnCreationHandler implements VillagerRegistry.IVillageCreationHandler {
	public static BarnCreationHandler instance = null;
	
	public static void init(Settings config) {
		if (null == instance) {
			instance = new BarnCreationHandler();
			VillagerRegistry.instance().registerVillageCreationHandler(instance);
		}
	}
	
	@Override
	public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
		return new StructureVillagePieces.PieceWeight(ComponentVillageBarn.class, Taverns.config.weightBarns, MathHelper.getRandomIntegerInRange(random, 0, 2));
	}
	
	@Override
	public Class<?> getComponentClass() {
		return ComponentVillageBarn.class;
	}
	
	@Override
	public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
		return ComponentVillageBarn.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
}
