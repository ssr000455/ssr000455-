package com.limingze.luckymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class LuckyMod implements ModInitializer {
    public static final String MOD_ID = "luckymod";
    
    public static final SwordItem ORDINARY_SWORD = new SwordItem(new ToolMaterial() {
        @Override public int getDurability() { return 9999; }
        @Override public float getMiningSpeedMultiplier() { return 10.0F; }
        @Override public float getAttackDamage() { return 9999.0F; }
        @Override public int getMiningLevel() { return 5; }
        @Override public int getEnchantability() { return 30; }
        @Override public Ingredient getRepairIngredient() { 
            return Ingredient.EMPTY; 
        }
    }, 0, 1.5F, new SwordItem.Settings());

    private static final ItemGroup LUCKY_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(Items.DIAMOND_SWORD))
        .displayName(Text.literal("幸运模组"))
        .entries((context, entries) -> {
            entries.add(ORDINARY_SWORD);
        })
        .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "ordinary_sword"), ORDINARY_SWORD);
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "lucky_group"), LUCKY_GROUP);
        
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity) {
                if (player.getMainHandStack().getItem() == ORDINARY_SWORD || 
                    player.getOffHandStack().getItem() == ORDINARY_SWORD) {
                    
                    if (!world.isClient()) {
                        player.sendMessage(Text.literal("哦！这听起来很疼！"), false);
                    }
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
    }
}