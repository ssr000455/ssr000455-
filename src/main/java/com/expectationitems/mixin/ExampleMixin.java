package com.expectationitems.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void onLoadWorld(CallbackInfo ci) {
        System.out.println("MinecraftServer loadWorld called");
    }
}
