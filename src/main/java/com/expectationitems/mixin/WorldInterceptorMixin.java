package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class WorldInterceptorMixin {
    
    // 使用更通用的方法 - 拦截所有实体移除相关的方法
    @Inject(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    private void interceptDuringEntityTick(Entity entity, CallbackInfo ci) {
        // 在实体tick期间检查是否需要保护
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isDeathInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player) && 
                player.isRemoved()) {
                
                // 如果玩家被标记为移除，取消移除状态
                player.setRemoved(net.minecraft.entity.Entity.RemovalReason.UNLOADED_TO_CHUNK);
                player.setHealth(player.getMaxHealth());
            }
        }
    }
}
