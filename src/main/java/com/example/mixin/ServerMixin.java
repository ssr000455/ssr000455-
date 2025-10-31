package com.limingze.luckymod.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerMixin {
    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void onWorldLoad(CallbackInfo info) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        server.getPlayerManager().broadcast(Text.literal("§6欢迎使用幸运模组！"), false);
    }
}