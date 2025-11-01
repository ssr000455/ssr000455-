package com.expectationitems;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

public class SimpleConfigCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("expectationitems")
            // 剑设置 (1)
            .then(literal("1")
                .then(literal("Block")
                    .then(argument("range", IntegerArgumentType.integer(1, 64))
                        .executes(context -> {
                            int range = IntegerArgumentType.getInteger(context, "range");
                            ModConfig.swordClearRange = range;
                            context.getSource().sendMessage(Text.literal("✅ 剑清除范围设置为: " + range + " 区块"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("clear")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.swordClearEnabled = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 剑清除功能已启用" : "❌ 剑清除功能已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("invincible")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.swordInvincibleEnabled = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 事件拦截功能已启用" : "❌ 事件拦截功能已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("flight")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.swordFlightEnabled = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 飞行功能已启用" : "❌ 飞行功能已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
            )
            
            // 工具方块设置 (2)
            .then(literal("2")
                .then(literal("toolBlockPeace")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.toolBlockPeaceEnabled = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 和平区域已启用" : "❌ 和平区域已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("toolBlockRadius")
                    .then(argument("radius", IntegerArgumentType.integer(1, 32))
                        .executes(context -> {
                            int radius = IntegerArgumentType.getInteger(context, "radius");
                            ModConfig.toolBlockRadius = radius;
                            context.getSource().sendMessage(Text.literal("✅ 保护范围设置为: " + radius + " 格"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("toolBlockForceLoad")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.toolBlockForceLoad = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 强制加载已启用" : "❌ 强制加载已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
                .then(literal("toolBlockBiogenesis")
                    .then(argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            ModConfig.toolBlockBiogenesis = enabled;
                            context.getSource().sendMessage(Text.literal(enabled ? "✅ 生物生成已启用" : "❌ 生物生成已禁用"));
                            ModConfig.saveConfig();
                            return 1;
                        })))
            )
            
            // 帮助命令
            .then(literal("help")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("🔧 期待物品模组命令帮助"));
                    context.getSource().sendMessage(Text.literal("1 Block <1-64> - 剑清除范围"));
                    context.getSource().sendMessage(Text.literal("1 clear <true/false> - 剑清除开关"));
                    context.getSource().sendMessage(Text.literal("1 invincible <true/false> - 事件拦截"));
                    context.getSource().sendMessage(Text.literal("1 flight <true/false> - 飞行功能"));
                    context.getSource().sendMessage(Text.literal("2 toolBlockPeace <true/false> - 和平区域"));
                    context.getSource().sendMessage(Text.literal("2 toolBlockRadius <1-32> - 保护范围"));
                    context.getSource().sendMessage(Text.literal("2 toolBlockForceLoad <true/false> - 强制加载"));
                    context.getSource().sendMessage(Text.literal("2 toolBlockBiogenesis <true/false> - 生物生成"));
                    return 1;
                }))
        );
    }
}
