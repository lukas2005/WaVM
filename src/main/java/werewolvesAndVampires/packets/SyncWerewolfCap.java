package werewolvesAndVampires.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import werewolvesAndVampires.werewolves.capability.ControlLevel;
import werewolvesAndVampires.werewolves.capability.IWerewolf;
import werewolvesAndVampires.werewolves.capability.WerewolfProvider;
import werewolvesAndVampires.werewolves.capability.WerewolfType;

public class SyncWerewolfCap implements IMessage {

	private boolean isTransformed;
	private int werewolfType;
	private int controlLevel;
	private int transformCount;
	
	public SyncWerewolfCap() {}
	
	public SyncWerewolfCap(IWerewolf w) {
		isTransformed = w.getIsTransformed();
		werewolfType = w.getWerewolfType().id;
		controlLevel = w.getControlLevel().id;
		transformCount = w.getTransformCount();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isTransformed);
		buf.writeInt(werewolfType);
		buf.writeInt(controlLevel);
		buf.writeInt(transformCount);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		isTransformed = buf.readBoolean();
		werewolfType = buf.readInt();
		controlLevel = buf.readInt();
		transformCount = buf.readInt();
	}

	public static class Handler implements IMessageHandler<SyncWerewolfCap, IMessage> {

		@Override
		public IMessage onMessage(SyncWerewolfCap message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(SyncWerewolfCap message, MessageContext ctx) {
			IWerewolf were = Minecraft.getMinecraft().player.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
			were.setWerewolfType(WerewolfType.getEnumFromId(message.werewolfType));
			were.setIsTransformed(message.isTransformed);
			were.setControlLevel(ControlLevel.getEnumFromId(message.controlLevel));
			were.setTransformCount(message.transformCount);
		}

	}
}
