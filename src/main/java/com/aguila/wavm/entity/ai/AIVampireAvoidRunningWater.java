package com.aguila.wavm.entity.ai;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AIVampireAvoidRunningWater extends EntityAIAvoid {

    public AIVampireAvoidRunningWater(EntityCreature vampire, double speed) {
        super(vampire, speed);
    }

    @Override
    protected boolean shouldAvoidPosition(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        int height = blockState.getValue(BlockLiquid.LEVEL);
        if (height < 15) {
            System.out.println(pos);
            return true;
        }
        return false;
    }
}
