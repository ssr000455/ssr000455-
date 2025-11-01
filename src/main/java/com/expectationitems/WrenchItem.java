package com.expectationitems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            user.sendMessage(Text.literal("🔧 期待物品模组命令列表"), false);
            user.sendMessage(Text.literal("/expectationitems 1 Block <1-64> - 剑清除范围"), false);
            user.sendMessage(Text.literal("/expectationitems 1 clear <true/false> - 剑清除开关"), false);
            user.sendMessage(Text.literal("/expectationitems 1 invincible <true/false> - 事件拦截"), false);
            user.sendMessage(Text.literal("/expectationitems 1 flight <true/false> - 飞行功能"), false);
            user.sendMessage(Text.literal("/expectationitems 2 toolBlockPeace <true/false> - 和平区域"), false);
            user.sendMessage(Text.literal("/expectationitems 2 toolBlockRadius <1-32> - 保护范围"), false);
            user.sendMessage(Text.literal("/expectationitems 2 toolBlockForceLoad <true/false> - 强制加载"), false);
            user.sendMessage(Text.literal("/expectationitems 2 toolBlockBiogenesis <true/false> - 生物生成"), false);
            user.sendMessage(Text.literal("/expectationitems help - 显示完整帮助"), false);
        }
        
        return TypedActionResult.success(itemStack);
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
