package com.expectationitems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityRemovalForcer {
    
    /**
     * 强制移除实体，绕过所有可能的拦截器
     */
    public static boolean forceRemoveEntity(Entity entity) {
        if (entity == null || entity.isRemoved()) return true;
        
        // 尝试多种移除方法
        boolean removed = false;
        
        // 方法1: 直接设置移除状态
        try {
            entity.setRemoved(Entity.RemovalReason.KILLED);
            removed = true;
        } catch (Exception e) {}
        
        // 方法2: 调用discard方法
        try {
            entity.discard();
            removed = true;
        } catch (Exception e) {}
        
        // 方法3: 对于生物实体，设置死亡
        try {
            if (entity instanceof LivingEntity living) {
                living.setHealth(0);
                living.kill();
                removed = true;
            }
        } catch (Exception e) {}
        
        // 方法4: 使用反射直接修改内部字段（终极手段）
        try {
            java.lang.reflect.Field removedField = Entity.class.getDeclaredField("removed");
            removedField.setAccessible(true);
            removedField.set(entity, true);
            removed = true;
        } catch (Exception e) {}
        
        // 方法5: 设置为无效状态
        try {
            entity.setInvulnerable(false);
            entity.setInvisible(true);
            entity.setNoGravity(true);
            entity.setPosition(0, -1000, 0); // 传送到地底
            removed = true;
        } catch (Exception e) {}
        
        return removed;
    }
    
    /**
     * 检查实体是否应该被清除（排除玩家）
     */
    public static boolean shouldRemoveEntity(Entity entity, PlayerEntity excludingPlayer) {
        if (entity == null || entity.isRemoved() || !entity.isAlive()) {
            return false;
        }
        
        // 不清除玩家
        if (entity instanceof PlayerEntity) {
            return false;
        }
        
        // 不清除排除的玩家自己
        if (entity == excludingPlayer) {
            return false;
        }
        
        // 清除所有其他实体
        return true;
    }
}
