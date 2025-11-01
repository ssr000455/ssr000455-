package com.expectationitems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ToolBlock extends Block {
    public ToolBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.literal("🛠️ 工具方块已激活"), false);
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        
        if (!world.isClient) {
            // 放置时强制设置为强加载区块
            ChunkPos chunkPos = new ChunkPos(pos);
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.setChunkForced(chunkPos.x, chunkPos.z, true);
            }
            
            // 清理危险方块
            final int blocksRemoved = clearDangerousBlocks(world, pos);
            
            // 使用final变量传递给lambda
            final ChunkPos finalChunkPos = chunkPos;
            world.getPlayers().forEach(p -> {
                p.sendMessage(Text.literal("🔒 区块 " + finalChunkPos.x + ", " + finalChunkPos.z + " 已强制加载并设为和平区域"), false);
                if (blocksRemoved > 0) {
                    p.sendMessage(Text.literal("🧹 移除了 " + blocksRemoved + " 个危险方块"), false);
                }
            });
        }
        
        return super.getPlacementState(ctx);
    }

    private int clearDangerousBlocks(World world, BlockPos center) {
        int radius = 16;
        int blocksRemoved = 0;
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);
                    
                    // 移除TNT和岩浆
                    if (state.getBlock() == Blocks.TNT || state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FIRE) {
                        world.removeBlock(checkPos, false);
                        blocksRemoved++;
                    }
                }
            }
        }
        
        return blocksRemoved;
    }
}
