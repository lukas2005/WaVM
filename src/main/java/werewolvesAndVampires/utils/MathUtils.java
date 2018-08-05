package werewolvesAndVampires.utils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class MathUtils {

    @Nullable
    public static Vec3d generateRandomPosForVamp(EntityCreature creature, int xz, int p_191379_2_, @Nullable Vec3d vec3d, boolean p_191379_4_)
    {
        PathNavigate pathnavigate = creature.getNavigator();
        Random random = creature.getRNG();
        boolean flag;

        if (creature.hasHome())
        {
            double d0 = creature.getHomePosition().distanceSq((double) MathHelper.floor(creature.posX), (double)MathHelper.floor(creature.posY), (double)MathHelper.floor(creature.posZ)) + 4.0D;
            double d1 = (double)(creature.getMaximumHomeDistance() + (float)xz);
            flag = d0 < d1 * d1;
        }
        else
        {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k)
        {
            int l = random.nextInt(2 * xz + 1) - xz;
            int i1 = random.nextInt(2 * p_191379_2_ + 1) - p_191379_2_;
            int j1 = random.nextInt(2 * xz + 1) - xz;

            if (vec3d == null || (double)l * vec3d.x + (double)j1 * vec3d.z >= 0.0D)
            {
                if (creature.hasHome() && xz > 1)
                {
                    BlockPos blockpos = creature.getHomePosition();

                    if (creature.posX > (double)blockpos.getX())
                    {
                        l -= random.nextInt(xz / 2);
                    }
                    else
                    {
                        l += random.nextInt(xz / 2);
                    }

                    if (creature.posZ > (double)blockpos.getZ())
                    {
                        j1 -= random.nextInt(xz / 2);
                    }
                    else
                    {
                        j1 += random.nextInt(xz / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double)l + creature.posX, (double)i1 + creature.posY, (double)j1 + creature.posZ);

                if (isDangerousDestination(blockpos1, creature)) {
                    continue;
                }

                if ((!flag || creature.isWithinHomeDistanceFromPosition(blockpos1)) && pathnavigate.canEntityStandOnPos(blockpos1))
                {
                    if (!p_191379_4_)
                    {
                        blockpos1 = moveAboveSolid(blockpos1, creature);

                        if (isWaterDestination(blockpos1, creature))
                        {
                            continue;
                        }

                    }

                    float f1 = creature.getBlockPathWeight(blockpos1);

                    if (f1 > f)
                    {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1)
        {
            return new Vec3d((double)k1 + creature.posX, (double)i + creature.posY, (double)j + creature.posZ);
        }
        else
        {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos p_191378_0_, EntityCreature p_191378_1_)
    {
        if (!p_191378_1_.world.getBlockState(p_191378_0_).getMaterial().isSolid())
        {
            return p_191378_0_;
        }
        else
        {
            BlockPos blockpos;

            for (blockpos = p_191378_0_.up(); blockpos.getY() < p_191378_1_.world.getHeight() && p_191378_1_.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up())
            {
                ;
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos p_191380_0_, EntityCreature p_191380_1_)
    {
        return p_191380_1_.world.getBlockState(p_191380_0_).getMaterial() == Material.WATER;
    }

    private static boolean isDangerousDestination(BlockPos pos, EntityCreature creature) {
        if (!creature.world.canSeeSky(creature.getPosition()) && creature.world.canSeeSky(pos))
            return true;
        return creature.world.isDaytime() && creature.world.canSeeSky(pos) && creature.world.getLight(pos) > creature.world.getLight(creature.getPosition());
    }

}
