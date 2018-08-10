package com.aguila.wavm.entity;

import com.aguila.wavm.entity.ai.AIVampireAvoidRunningWater;
import com.aguila.wavm.entity.ai.AIVampireAvoidSun;
import com.aguila.wavm.init.WVDamageSources;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityVampire extends EntityMob {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityVampire.class, DataSerializers.VARINT);

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
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(getRNG().nextInt(4)));

    }


    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(2, new AIVampireAvoidSun(this, 0.6D));
        this.tasks.addTask(2, new AIVampireAvoidRunningWater(this, 0.6D));
        this.tasks.addTask(3, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 0.6D, true));
        applyEntityAI();

    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));

        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPig.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntitySheep.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityChicken.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!immunities.contains(damageSrc)) {
            if (damageSrc.getTrueSource() instanceof EntityPlayer && itemVulnerabilities.contains(((EntityPlayer) damageSrc.getTrueSource()).inventory.getCurrentItem().getUnlocalizedName())) {
                System.out.println(damageAmount);
                System.out.println(damageSrc.getTrueSource());
                super.damageEntity(damageSrc, damageAmount);
            } else if (!(damageSrc.getTrueSource() instanceof EntityPlayer)) {
                super.damageEntity(damageSrc, damageAmount);
            }
        }

    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isDaytime() && !this.world.isRemote && this.shouldBurnInDay() && !world.isRaining() && world.canSeeSky(this.getPosition())) {
            this.setFire(4);
        }
        if (this.ticksExisted % 10 == 0) {
            if (world.getBlockState(this.getPosition()).getMaterial() == Material.WATER && world.getBlockState(this.getPosition()).getValue(BlockLiquid.LEVEL) > 0) {
                this.attackEntityFrom(WVDamageSources.DAMAGE_SOURCE_MOVING_WATER, 1.0F);
            }
        }
    }


    public int getVariant()
    {
        return Math.max(this.dataManager.get(VARIANT).intValue(), 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("variant", getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(VARIANT, compound.getInteger("variant"));
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
        return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

}
