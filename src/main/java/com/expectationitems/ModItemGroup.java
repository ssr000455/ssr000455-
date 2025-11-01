package com.expectationitems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup EXPECTATION_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(net.minecraft.item.Items.DIAMOND_SWORD))
            .displayName(Text.translatable("itemGroup.expectationitems.expectation"))
            .entries((context, entries) -> {
                entries.add(ModItems.EXPECTATION_SWORD);
                entries.add(ModItems.TOOL_BLOCK_ITEM);
                entries.add(ModItems.WRENCH_ITEM);
            })
            .build();
    
    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("expectationitems", "expectation"), EXPECTATION_GROUP);
    }
}
