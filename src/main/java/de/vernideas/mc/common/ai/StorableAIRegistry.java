package de.vernideas.mc.common.ai;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

public final class StorableAIRegistry {
	private static final Map<String, StorableAIBuilder<?>> builderMap = new HashMap();
	private static final Map<Class<?>, StorableAIBuilder<?>> classMap = new HashMap();
	private static final StorableAIBuilder nullBuilder = new NullAIBuilder();
	
	public static <T extends EntityAIBase> void registerBuilder(StorableAIBuilder<T> builder) {
		builderMap.put(builder.getRoutineName(), builder);
		classMap.put(builder.getType(), builder);
	}
	
	public static <T extends EntityAIBase> StorableAIBuilder<T> getBuilder(String id) {
		StorableAIBuilder<T> builder = (StorableAIBuilder) builderMap.get(id);
		return null == builder ? nullBuilder : builder;
	}
	
	public static <T extends EntityAIBase> StorableAIBuilder<T> getBuilder(Class<T> clazz) {
		StorableAIBuilder<T> builder = (StorableAIBuilder) classMap.get(clazz);
		return null == builder ? nullBuilder : builder;
	}
	
	public static <T extends EntityAIBase> StorableAIBuilder<T> getBuilder(T classInstance) {
		StorableAIBuilder<T> builder = (StorableAIBuilder) classMap.get(classInstance.getClass());
		return null == builder ? nullBuilder : builder;
	}
	
	private static class NullAI extends EntityAIBase implements StorableAI {
		@Override
		public void writeToNBT(NBTTagCompound data) {
		}
		
		@Override
		public boolean shouldExecute() {
			return false;
		}
	}
	
	private static class NullAIBuilder implements StorableAIBuilder<StorableAIRegistry.NullAI> {
		@Override
		public StorableAIRegistry.NullAI createFromNBT(EntityLiving living, NBTTagCompound data) {
			return null;
		}
		
		@Override
		public Class<StorableAIRegistry.NullAI> getType() {
			return StorableAIRegistry.NullAI.class;
		}
		
		@Override
		public String getRoutineName() {
			return "Vernideas:NULL";
		}
		
		@Override
		public void writeToNBT(NBTTagCompound paramNBTTagCompound) {
			// TODO Auto-generated method stub
			
		}
	}
}
