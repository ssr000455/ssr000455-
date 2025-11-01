package com.expectationitems.mixin;

import com.expectationitems.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class AbsoluteDefenseMixin {
    
    @Inject(method = "setVelocityClient", at = @At("HEAD"), cancellable = true)
    private void interceptKnockback(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
                entity.setVelocity(entity.getVelocity());
            }
        }
    }
    
    @Inject(method = "requestTeleport", at = @At("HEAD"), cancellable = true)
    private void interceptTeleport(double destX, double destY, double destZ, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
            }
        }
    }
    
    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void interceptPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (EventHandler.isEventInterceptorEnabled(player) && 
            EventHandler.isHoldingExpectationSword(player)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void interceptPushAway(Entity entity, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
            }
        }
    }
    
    @Inject(method = "setOnFireFor", at = @At("HEAD"), cancellable = true)
    private void interceptFire(int seconds, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                ci.cancel();
                entity.setFireTicks(0);
            }
        }
    }
}

@Mixin(LivingEntity.class)
class AbsoluteDefenseLivingMixin {
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void interceptAllDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                cir.setReturnValue(false);
                cir.cancel();
                
                if (player.getHealth() < player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
                player.clearStatusEffects();
            }
        }
    }
    
    @Inject(method = "addStatusEffect", at = @At("HEAD"), cancellable = true)
    private void interceptStatusEffects(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
    
    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void interceptFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (EventHandler.isEventInterceptorEnabled(player) && 
                EventHandler.isHoldingExpectationSword(player)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
