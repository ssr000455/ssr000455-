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
import net.minecraft.text.Text;

public class EventHandler {
    private static final java.util.Map<PlayerEntity, PlayerInterceptorState> playerStates = new java.util.HashMap<>();
    
    public static void registerEvents() {
        AttackEntityCallback.EVENT.register(EventHandler::onAttackEntity);
    }
    
    private static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, 
                                             net.minecraft.entity.Entity entity, @Nullable EntityHitResult hitResult) {
        PlayerInterceptorState state = playerStates.get(player);
        
        if (entity instanceof PlayerEntity targetPlayer) {
            if (isHoldingExpectationSword(targetPlayer) && 
                isEventInterceptorEnabled(targetPlayer)) {
                if (!world.isClient) {
                    player.sendMessage(Text.translatable("message.expectationitems.attack_blocked"), false);
                }
                return ActionResult.FAIL;
            }
        }
        
        return ActionResult.PASS;
    }
    
    public static void onServerTick(net.minecraft.server.MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(player -> {
            boolean holdingSword = isHoldingExpectationSword(player);
            PlayerInterceptorState state = playerStates.get(player);
            
            if (state == null) {
                state = new PlayerInterceptorState();
                playerStates.put(player, state);
            }
            
            // 检查副手是否有工具方块
            boolean hasToolBlockInOffhand = player.getOffHandStack().getItem() == ModItems.TOOL_BLOCK_ITEM;
            
            if (hasToolBlockInOffhand) {
                // 副手持有时增加移动速度和抗性提升
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 40, 1, false, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1, false, false));
                
                // 简单的事件拦截器 - 防止火焰和岩浆伤害
                player.setFireTicks(0);
            }
            
            if (holdingSword) {
                if (!state.isEventInterceptorEnabled()) {
                    state.setEventInterceptorEnabled(true);
                    player.getAbilities().allowFlying = true;
                    player.sendAbilitiesUpdate();
                    if (!player.getWorld().isClient) {
                        player.sendMessage(Text.translatable("message.expectationitems.flight_enabled"), false);
                    }
                }
                
                if (!state.isDeathInterceptorEnabled()) {
                    state.setDeathInterceptorEnabled(true);
                }
                
                player.getAbilities().allowFlying = true;
                player.sendAbilitiesUpdate();
                
                if (player.getVelocity().y < 0) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false, false));
                }
                
                if (player.getHealth() <= 0) {
                    player.setHealth(player.getMaxHealth());
                }
                
            } else {
                if (state.isEventInterceptorEnabled()) {
                    state.setEventInterceptorEnabled(false);
                    if (!player.isCreative()) {
                        player.getAbilities().allowFlying = false;
                        player.getAbilities().flying = false;
                    }
                    player.sendAbilitiesUpdate();
                }
                state.setDeathInterceptorEnabled(false);
            }
            
            playerStates.keySet().removeIf(p -> p.isRemoved() || !p.isAlive());
        });
    }
    
    public static boolean isHoldingExpectationSword(PlayerEntity player) {
        return player.getMainHandStack().getItem() == ModItems.EXPECTATION_SWORD || 
               player.getOffHandStack().getItem() == ModItems.EXPECTATION_SWORD;
    }
    
    public static boolean isEventInterceptorEnabled(PlayerEntity player) {
        PlayerInterceptorState state = playerStates.get(player);
        return state != null && state.isEventInterceptorEnabled();
    }
    
    public static void setEventInterceptorEnabled(PlayerEntity player, boolean enabled) {
        PlayerInterceptorState state = playerStates.get(player);
        if (state != null) {
            state.setEventInterceptorEnabled(enabled);
        }
    }
    
    public static boolean isDeathInterceptorEnabled(PlayerEntity player) {
        PlayerInterceptorState state = playerStates.get(player);
        return state != null && state.isDeathInterceptorEnabled();
    }
    
    public static void setDeathInterceptorEnabled(PlayerEntity player, boolean enabled) {
        PlayerInterceptorState state = playerStates.get(player);
        if (state != null) {
            state.setDeathInterceptorEnabled(enabled);
        } else {
            state = new PlayerInterceptorState();
            state.setDeathInterceptorEnabled(enabled);
            playerStates.put(player, state);
        }
    }
    
    private static class PlayerInterceptorState {
        private boolean eventInterceptorEnabled = false;
        private boolean deathInterceptorEnabled = false;
        
        public boolean isEventInterceptorEnabled() { return eventInterceptorEnabled; }
        public void setEventInterceptorEnabled(boolean enabled) { this.eventInterceptorEnabled = enabled; }
        
        public boolean isDeathInterceptorEnabled() { return deathInterceptorEnabled; }
        public void setDeathInterceptorEnabled(boolean enabled) { this.deathInterceptorEnabled = enabled; }
    }
}
