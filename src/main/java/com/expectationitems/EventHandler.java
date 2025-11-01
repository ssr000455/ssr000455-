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
    // 改为实例变量，每个玩家独立状态
    private static final java.util.Map<PlayerEntity, PlayerInterceptorState> playerStates = new java.util.HashMap<>();
    
    public static void registerEvents() {
        AttackEntityCallback.EVENT.register(EventHandler::onAttackEntity);
    }
    
    private static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, 
                                             net.minecraft.entity.Entity entity, @Nullable EntityHitResult hitResult) {
        PlayerInterceptorState state = playerStates.get(player);
        if (state != null && state.isEventInterceptorEnabled() && isHoldingExpectationSword(player)) {
            if (!world.isClient) {
                player.sendMessage(Text.literal("🔒 攻击被期望之剑拦截!"), false);
            }
            return ActionResult.FAIL;
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
            
            // 持剑时持续启用能力
            if (holdingSword) {
                if (!state.isEventInterceptorEnabled()) {
                    state.setEventInterceptorEnabled(true);
                    player.getAbilities().allowFlying = true;
                    player.sendAbilitiesUpdate();
                    if (!player.getWorld().isClient) {
                        player.sendMessage(Text.literal("✨ 已启用事件拦截和飞行能力"), false);
                    }
                }
                
                if (!state.isDeathInterceptorEnabled()) {
                    state.setDeathInterceptorEnabled(true);
                }
                
                // 持续保持飞行能力
                player.getAbilities().allowFlying = true;
                player.sendAbilitiesUpdate();
                
                // 持续缓降效果
                if (player.getVelocity().y < 0) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false, false));
                }
                
                // 持续强制保持存活状态
                if (player.getHealth() <= 0) {
                    player.setHealth(player.getMaxHealth());
                }
                
            } else {
                // 不持剑时持续禁用能力
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
            
            // 清理离线玩家状态
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
    
    // 玩家拦截状态类
    private static class PlayerInterceptorState {
        private boolean eventInterceptorEnabled = false;
        private boolean deathInterceptorEnabled = false;
        
        public boolean isEventInterceptorEnabled() { return eventInterceptorEnabled; }
        public void setEventInterceptorEnabled(boolean enabled) { this.eventInterceptorEnabled = enabled; }
        
        public boolean isDeathInterceptorEnabled() { return deathInterceptorEnabled; }
        public void setDeathInterceptorEnabled(boolean enabled) { this.deathInterceptorEnabled = enabled; }
    }
}
