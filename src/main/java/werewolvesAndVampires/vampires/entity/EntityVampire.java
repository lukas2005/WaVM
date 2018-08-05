package werewolvesAndVampires.vampires.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import werewolvesAndVampires.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// At the moment we have the possibility of vampire babies. This may need removing in future, or we may want to make them more aggressive.
public class EntityVampire extends EntityAgeable {

    private static List<DamageSource> immunities = new ArrayList<>();
    private static List<String> itemVulnerabilities = new ArrayList<>();

    public EntityVampire(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(false); // villagers usually can though.

        immunities.add(DamageSource.FALL);
        immunities.add(DamageSource.DROWN);

        itemVulnerabilities.add(Items.WOODEN_SWORD.getUnlocalizedName());
        itemVulnerabilities.add(Items.WOODEN_AXE.getUnlocalizedName());
        itemVulnerabilities.add(Items.WOODEN_HOE.getUnlocalizedName());
        itemVulnerabilities.add(Items.WOODEN_PICKAXE.getUnlocalizedName());
        itemVulnerabilities.add(Items.WOODEN_SHOVEL.getUnlocalizedName());
        itemVulnerabilities.add(Items.STICK.getUnlocalizedName());
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(3, new EntityVampire.AIVampireMoveIndoors(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityVampire.AIVampireTarget(this, EntityPlayer.class));
        this.tasks.addTask(7, new EntityVampire.AIVampireTarget(this, EntityVillager.class));
        this.tasks.addTask(8, new EntityVampire.AIVampireWander(this, 0.6D)); // TODO: This still needs more development
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!immunities.contains(damageSrc)) {
            super.damageEntity(damageSrc, damageAmount);

        }
    }

    /**
     *
     * @param entityIn
     * @return Should the attack go ahead?
     */
    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer) {
            if (!itemVulnerabilities.contains(((EntityPlayer) entityIn).inventory.getCurrentItem().getUnlocalizedName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityVampire vampire = new EntityVampire(this.world);
        vampire.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(vampire)), null);
        return vampire;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isDaytime() && !this.world.isRemote && this.shouldBurnInDay() && world.canSeeSky(this.getPosition())) {
            this.setFire(8);
        }
    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE; // TODO: Try this out
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH; // TODO: Try this out
    }

    static class AIVampireTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
        public AIVampireTarget(EntityVampire vampire, Class<T> classTarget) {
            super(vampire, classTarget, true);
        }

        @Override
        public boolean shouldExecute() {
            float f = this.taskOwner.getBrightness();
            return true;//f < 0.5F && super.shouldExecute();
        }
    }

    static class AIVampireMoveIndoors extends EntityAIBase {
        private final EntityCreature entity;
        private VillageDoorInfo doorInfo;
        private int insidePosX = -1;
        private int insidePosZ = -1;

        public AIVampireMoveIndoors(EntityCreature entityIn)
        {
            this.entity = entityIn;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            BlockPos blockpos = new BlockPos(this.entity);

            if ((this.entity.world.isDaytime() || this.entity.world.isRaining() && !this.entity.world.getBiome(blockpos).canRain()) && this.entity.world.provider.hasSkyLight())
            {
                if (this.entity.getRNG().nextInt(50) != 0)
                {
                    return false;
                }
                else if (this.insidePosX != -1 && this.entity.getDistanceSq((double)this.insidePosX, this.entity.posY, (double)this.insidePosZ) < 4.0D)
                {
                    return false;
                }
                else
                {
                    Village village = this.entity.world.getVillageCollection().getNearestVillage(blockpos, 14);

                    if (village == null)
                    {
                        return false;
                    }
                    else
                    {
                        this.doorInfo = village.getDoorInfo(blockpos);
                        return this.doorInfo != null;
                    }
                }
            }
            else
            {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return !this.entity.getNavigator().noPath();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.insidePosX = -1;
            BlockPos blockpos = this.doorInfo.getInsideBlockPos();
            int i = blockpos.getX();
            int j = blockpos.getY();
            int k = blockpos.getZ();

            if (this.entity.getDistanceSq(blockpos) > 256.0D)
            {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));

                if (vec3d != null)
                {
                    this.entity.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
            else
            {
                this.entity.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
            this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
            this.doorInfo = null;
        }
    }

    static class AIVampireRestrictOpenDoor extends EntityAIBase {
        private final EntityCreature entity;
        private VillageDoorInfo frontDoor;

        public AIVampireRestrictOpenDoor(EntityCreature creatureIn)
        {
            this.entity = creatureIn;

            if (!(creatureIn.getNavigator() instanceof PathNavigateGround))
            {
                throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
            }
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (!this.entity.world.isDaytime())
            {
                return false;
            }
            else
            {
                BlockPos blockpos = new BlockPos(this.entity);
                Village village = this.entity.world.getVillageCollection().getNearestVillage(blockpos, 16);

                if (village == null)
                {
                    return false;
                }
                else
                {
                    this.frontDoor = village.getNearestDoor(blockpos);

                    if (this.frontDoor == null)
                    {
                        return false;
                    }
                    else
                    {
                        return (double)this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D;
                    }
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            if (!this.entity.world.isDaytime())
            {
                return false;
            }
            else
            {
                return !this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.isInsideSide(new BlockPos(this.entity));
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(false);
            ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(false);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            ((PathNavigateGround)this.entity.getNavigator()).setBreakDoors(true);
            ((PathNavigateGround)this.entity.getNavigator()).setEnterDoors(true);
            this.frontDoor = null;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            this.frontDoor.incrementDoorOpeningRestrictionCounter();
        }
    }

    static class AIVampireWander extends EntityAIWander {

        public AIVampireWander(EntityCreature creatureIn, double speedIn)
        {
            super(creatureIn, speedIn, 120);
        }

        @Nullable
        protected Vec3d getPosition()
        {
            return MathUtils.generateRandomPosForVamp(this.entity, 10, 7, null, true);
        }

    }


}
