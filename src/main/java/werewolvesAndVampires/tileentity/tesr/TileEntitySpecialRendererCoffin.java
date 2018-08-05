package werewolvesAndVampires.tileentity.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import werewolvesAndVampires.blocks.model.ModelCoffin;
import werewolvesAndVampires.core.WVCore;
import werewolvesAndVampires.core.WVMath;
import werewolvesAndVampires.tileentity.TileEntityCoffin;

import static net.minecraft.block.BlockHorizontal.FACING;
import static net.minecraft.client.renderer.GlStateManager.*;


public class TileEntitySpecialRendererCoffin extends TileEntitySpecialRenderer<TileEntityCoffin> {

	public static final ModelCoffin model = new ModelCoffin();
	public static final ResourceLocation res = new ResourceLocation(WVCore.MODID, "textures/blocks/coffin.png");

	@Override
	public void render(TileEntityCoffin te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		pushMatrix();

		translate(x, y, z);
		scale(0.065, 0.065, 0.065);

		rotate(WVMath.eulerToQuaternionDegrees(0, 359, 0));

		translate(-7.5, -24, 7);

		switch (te.getWorld().getBlockState(te.getPos()).getValue(FACING)) {
			case NORTH:
				break;
			case SOUTH:
				translate(0, 0, -16);
				break;
			case WEST:
				rotate(WVMath.eulerToQuaternionDegrees(-180, 0, 0));
				break;
			case EAST:
				rotate(WVMath.eulerToQuaternionDegrees(180, 0, 0));
				break;
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(res);
		model.render(null, 0, 0, 0, 0, 0, 1);

		popMatrix();
	}
}
