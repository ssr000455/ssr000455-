package com.expectationitems;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class EventHandler {
    private static boolean isHoldingSword = false;
    
    public static void registerEvents() {
        AttackEntityCallback.EVENT.register(EventHandler::onAttackEntity);
    }
    
    private static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, 
                                             net.minecraft.entity.Entity entity, @Nullable EntityHitResult hitResult) {
        if (isHoldingSword) {
            if (!world.isClient) {
                player.sendMessage(Text.literal("攻击被拦截!"), false);
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
    
    public static void onServerTick(net.minecraft.server.MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(player -> {
            boolean holdingSword = player.getMainHandStack().getItem() == ModItems.EXPECTATION_SWORD || 
                                 player.getOffHandStack().getItem() == ModItems.EXPECTATION_SWORD;
            
            if (holdingSword && !isHoldingSword) {
                player.getAbilities().allowFlying = true;
                player.sendAbilitiesUpdate();
                if (!player.getWorld().isClient) {
                    player.sendMessage(Text.literal("已启用飞行能力"), false);
                }
                isHoldingSword = true;
            } else if (!holdingSword && isHoldingSword) {
                if (!player.isCreative()) {
                    player.getAbilities().allowFlying = false;
                    player.getAbilities().flying = false;
                }
                player.sendAbilitiesUpdate();
                isHoldingSword = false;
            }
            
            if (holdingSword && player.getVelocity().y < 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 5, 0, false, false));
            }
        });
    }
}
