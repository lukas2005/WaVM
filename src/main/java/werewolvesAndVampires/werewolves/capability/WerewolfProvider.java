package werewolvesAndVampires.werewolves.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class WerewolfProvider implements ICapabilitySerializable<NBTBase>{
	
	 @CapabilityInject(IWerewolf.class)
	 public static final Capability<IWerewolf> WEREWOLF_CAP = null;

	 
	 private IWerewolf instance = WEREWOLF_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == WEREWOLF_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == WEREWOLF_CAP ? WEREWOLF_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return WEREWOLF_CAP.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		WEREWOLF_CAP.readNBT(instance, null, nbt);
	}

}
