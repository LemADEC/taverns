package de.vernideas.mc.taverns;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.vernideas.mc.common.entity.properties.GuardAIProperties;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityEvent;

public class EntityEventHandler {
	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if ((event.entity instanceof EntityVillager)) {
			EntityVillager villager = (EntityVillager) event.entity;
			if (null == GuardAIProperties.get(villager)) {
				GuardAIProperties.register(villager);
			}
		}
	}
}
