package com.aguila.wavm.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.List;

public class VampireHelpers {

	static Class<EntityPlayer> playerClass = EntityPlayer.class;
	static Field sleepTimerField;
	static Field sleepingField;

	static {
		try {
			sleepingField   = playerClass.getDeclaredField("sleeping");
			sleepTimerField = playerClass.getDeclaredField("sleepTimer");

			sleepingField.setAccessible(true);
			sleepTimerField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static EntityPlayer.SleepResult trySleep(EntityPlayer player, BlockPos bedLocation) {
		if (!player.world.isRemote) {
			if (player.isPlayerSleeping() || !player.isEntityAlive()) {
				return EntityPlayer.SleepResult.OTHER_PROBLEM;
			}

			if (!player.world.provider.isSurfaceWorld()) {
				return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
			}

			if (!player.world.isDaytime()) {
				return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
			}

			if (Math.abs(player.posX - (double) bedLocation.getX()) > 3.0D || Math.abs(player.posY - (double) bedLocation.getY()) > 2.0D || Math.abs(player.posZ - (double) bedLocation.getZ()) > 3.0D) {
				return EntityPlayer.SleepResult.TOO_FAR_AWAY;
			}

			double d0 = 8.0D;
			double d1 = 5.0D;

			//new AxisAlignedBB((double) bedLocation.getX() - d0, (double) bedLocation.getY() - d1, (double) bedLocation.getZ() - d0, (double) bedLocation.getX() + d0, (double) bedLocation.getY() + d1, (double) bedLocation.getZ() + d0)
			AxisAlignedBB bb = player.getEntityBoundingBox().expand(d0, d1, d0);
			List<EntityMob> list = player.world.getEntitiesWithinAABB(EntityMob.class, bb);

			if (!list.isEmpty()) {
				return EntityPlayer.SleepResult.NOT_SAFE;
			}
		}

		if (player.isRiding()) {
			player.dismountRidingEntity();
		}

		IBlockState state = null;

		if (player.world.isBlockLoaded(bedLocation)) state = player.world.getBlockState(bedLocation);
		if (state != null && state.getBlock().isBed(state, player.world, bedLocation, player)) {
			EnumFacing enumfacing = state.getBlock().getBedDirection(state, player.world, bedLocation);

			float f = 0.5F;
			float f1 = 0.5F;

			switch (enumfacing) {
				case SOUTH:
					f1 = 0.9F;
					break;
				case NORTH:
					f1 = 0.1F;
					break;
				case WEST:
					f = 0.1F;
					break;
				case EAST:
					f = 0.9F;
					break;
			}

			//player.setRenderOffsetForSleep(enumfacing);
			player.setPosition((double) ((float) bedLocation.getX() + f), (double) ((float) bedLocation.getY() + 0.6875F), (double) ((float) bedLocation.getZ() + f1));
		} else {
			player.setPosition((double) ((float) bedLocation.getX() + 0.5F), (double) ((float) bedLocation.getY() + 0.6875F), (double) ((float) bedLocation.getZ() + 0.5F));
		}


		player.capabilities.isFlying = false;
		player.sendPlayerAbilities();

		try {
			sleepingField.setBoolean(player, true);
			sleepTimerField.setInt(player  , 0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		player.noClip = true;
		player.bedLocation = bedLocation;
		player.motionX = player.motionZ = player.motionY = 0.0D;

		if (!player.world.isRemote) {
			DaySleepHelper.updateAllPlayersSleeping(player.world);
		}

		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			Packet<?> packet = new SPacketUseBed(player, bedLocation);

			playerMP.getServerWorld().getEntityTracker().sendToTracking(playerMP, packet);

			playerMP.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			playerMP.connection.sendPacket(packet);
		}

		return EntityPlayer.SleepResult.OK;
	}

}
