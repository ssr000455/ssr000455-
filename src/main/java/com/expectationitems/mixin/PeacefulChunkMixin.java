package com.expectationitems.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public class PeacefulChunkMixin {
    
    @Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
    private static void preventSpawningInToolBlockChunks(ServerWorld world, WorldChunk chunk, BlockPos pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        ChunkPos chunkPos = chunk.getPos();
        
        // 简单实现：检查区块中心附近是否有工具方块
        BlockPos center = new BlockPos(chunkPos.getStartX() + 8, 64, chunkPos.getStartZ() + 8);
        
        // 扫描Y轴寻找工具方块
        for (int y = world.getBottomY(); y < world.getTopY(); y++) {
            BlockPos checkPos = new BlockPos(center.getX(), y, center.getZ());
            if (world.getBlockState(checkPos).getBlock().getTranslationKey().contains("tool_block")) {
                cir.setReturnValue(false);
                cir.cancel();
                return;
            }
        }
    }
}
