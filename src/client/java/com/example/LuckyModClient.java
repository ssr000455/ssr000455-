package com.limingze.luckymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class LuckyModClient implements ClientModInitializer {
    public static KeyBinding clearEntitiesKey;

    @Override
    public void onInitializeClient() {
        clearEntitiesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.luckymod.clear_entities",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.luckymod.general"
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clearEntitiesKey.wasPressed()) {
                if (client.player != null && client.world != null) {
                    if (client.player.getMainHandStack().getItem() == LuckyMod.ORDINARY_SWORD || 
                        client.player.getOffHandStack().getItem() == LuckyMod.ORDINARY_SWORD) {
                        
                        client.world.getEntities().forEach(entity -> {
                            if (entity instanceof LivingEntity living && living != client.player) {
                                living.discard();
                            }
                        });
                        client.player.sendMessage(Text.literal("已清除所有生物！"), false);
                    }
                }
            }
        });
    }
}