package com.expectationitems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("expectationitems");

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModItemGroup.registerItemGroups();
        EventHandler.registerEvents();
        
        ServerTickEvents.END_SERVER_TICK.register(EventHandler::onServerTick);
        
        LOGGER.info("Expectation Items mod initialized!");
    }
}