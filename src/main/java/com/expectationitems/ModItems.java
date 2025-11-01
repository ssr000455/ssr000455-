package com.expectationitems;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item EXPECTATION_SWORD = new ExpectationSword(new ExpectationToolMaterial(), 9999, 1.5f, new Item.Settings());
    public static final Block TOOL_BLOCK = new ToolBlock(Block.Settings.copy(Blocks.BRICKS));
    public static final Item TOOL_BLOCK_ITEM = new BlockItem(TOOL_BLOCK, new Item.Settings());
    public static final Item WRENCH_ITEM = new WrenchItem(new Item.Settings().maxCount(1));
    
    public static void registerModItems() {
        Registry.register(Registries.ITEM, new Identifier("expectationitems", "expectation_sword"), EXPECTATION_SWORD);
        Registry.register(Registries.BLOCK, new Identifier("expectationitems", "tool_block"), TOOL_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("expectationitems", "tool_block"), TOOL_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier("expectationitems", "wrench"), WRENCH_ITEM);
    }
    
    private static class ExpectationToolMaterial implements ToolMaterial {
        @Override
        public int getDurability() {
            return -1;
        }
        
        @Override
        public float getMiningSpeedMultiplier() {
            return 10.0f;
        }
        
        @Override
        public float getAttackDamage() {
            return 9999.0f;
        }
        
        @Override
        public int getMiningLevel() {
            return 4;
        }
        
        @Override
        public int getEnchantability() {
            return 30;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    }
}
