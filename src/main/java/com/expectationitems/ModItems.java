package com.expectationitems;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item EXPECTATION_SWORD = new ExpectationSword(new ExpectationToolMaterial(), 9999, 1.5f, new Item.Settings());
    
    public static void registerModItems() {
        Registry.register(Registries.ITEM, new Identifier("expectationitems", "expectation_sword"), EXPECTATION_SWORD);
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