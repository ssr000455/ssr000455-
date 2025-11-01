package com.expectationitems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
        ModConfig.loadConfig();
        
        // 注册简化命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SimpleConfigCommand.register(dispatcher);
        });
        
        ServerTickEvents.END_SERVER_TICK.register(EventHandler::onServerTick);
        
        // 新的加载成功提示
        LOGGER.info("╔══════════════════════════════════╗");
        LOGGER.info("║            作者                  ║");
        LOGGER.info("║        QQ3992763243              ║");
        LOGGER.info("║        许可证书cc0               ║");
        LOGGER.info("║   期待的物品模组加载成功！        ║");
        LOGGER.info("╚══════════════════════════════════╝");
        LOGGER.info("expectationitems version:1.0.1");
    }
}
