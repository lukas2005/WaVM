package com.aguila.wavm.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.Random;

public abstract class EntityAIAvoid extends EntityAIBase {

    protected EntityCreature entity;
    private final double speed;
    private Path path;
    private final PathNavigate navigation;

    private int executionChance = 120;

    public EntityAIAvoid(EntityCreature entity,  double farSpeed) {
        this.entity = entity;
        this.speed = farSpeed;
        this.navigation = entity.getNavigator();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!shouldAvoidPosition(entity.world, entity.getPosition())) {
            return false;
        }
        Vec3d vec3d = findRandomTarget(this.entity, 16, 7, new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ));
        if (vec3d != null) {
            entity.world.setBlockToAir(new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z));
            this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);

            return this.path != null;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.navigation.setPath(this.path, this.speed);
    }

    protected abstract boolean shouldAvoidPosition(World world, BlockPos pos);

    private Vec3d findRandomTarget(EntityCreature entity, int xz, int y, Vec3d posToAvoid) {
        Random random = entity.getRNG();
        BlockPos currLoc = new BlockPos(posToAvoid);
        for (int k = 0; k < 10; ++k) {
            BlockPos newPos = currLoc.add(random.nextInt(xz) - (xz / 2), random.nextInt(xz), random.nextInt(xz) - (xz / 2));
            if (!shouldAvoidPosition(entity.world, newPos)) {
                System.out.println(currLoc + ": " + newPos + ": " + entity.world.canSeeSky(newPos));
                return new Vec3d(newPos.getX(), newPos.getY(), newPos.getZ());
            }
        }
        return null;
    }
}
