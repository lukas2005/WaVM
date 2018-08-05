package werewolvesAndVampires.vampires.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import werewolvesAndVampires.core.WVCore;
import werewolvesAndVampires.vampires.entity.EntityVampire;
import werewolvesAndVampires.werewolves.entity.WerewolfEntity;
import werewolvesAndVampires.werewolves.rendering.WerewolfRenderMob;

import javax.annotation.Nullable;

public class RenderEntityVampire extends RenderLiving<EntityVampire> {

    public static final Factory FACTORY = new Factory();

    public RenderEntityVampire(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelVamp(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityVampire entity) {
        return new ResourceLocation(WVCore.MODID, "textures/entity/vamp.png");
    }

    public static class Factory implements IRenderFactory<EntityVampire> {

        @Override
        public RenderLiving<? super EntityVampire> createRenderFor(RenderManager manager) {
            return new RenderEntityVampire(manager);
        }

    }
}
