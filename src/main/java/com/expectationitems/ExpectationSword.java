package com.expectationitems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.List;

public class ExpectationSword extends SwordItem {
    public ExpectationSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            if (user.isSneaking()) {
                forceClearAllEntities(world, user);
            } else {
                boolean newState = !EventHandler.isDeathInterceptorEnabled(user);
                EventHandler.setDeathInterceptorEnabled(user, newState);
                
                String status = newState ? "✅ 启用" : "❌ 禁用";
                user.sendMessage(Text.literal(status + " 死亡拦截器"), false);
            }
        }
        
        return TypedActionResult.success(itemStack);
    }
    
    private void forceClearAllEntities(World world, PlayerEntity user) {
        Box area = new Box(
            user.getBlockPos().add(-200, -100, -200),
            user.getBlockPos().add(200, 100, 200)
        );
        
        List<Entity> entities = world.getOtherEntities(user, area);
        int removedCount = 0;
        int totalCount = 0;
        
        for (Entity entity : entities) {
            totalCount++;
            if (EntityRemovalForcer.shouldRemoveEntity(entity, user)) {
                if (EntityRemovalForcer.forceRemoveEntity(entity)) {
                    removedCount++;
                }
            }
        }
        
        if (removedCount > 0) {
            user.sendMessage(Text.literal("💥 强制清除了 " + removedCount + "/" + totalCount + " 个实体"), false);
        } else {
            user.sendMessage(Text.literal("ℹ️ 范围内没有可清除的实体"), false);
        }
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
