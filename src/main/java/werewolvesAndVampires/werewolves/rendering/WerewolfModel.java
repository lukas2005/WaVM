package werewolvesAndVampires.werewolves.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created using Tabula 7.0.0
 */
public class WerewolfModel extends ModelBase {
    public ModelRenderer Arm1;
    public ModelRenderer leg1;
    public ModelRenderer Arm2;
    public ModelRenderer leg2;
    public ModelRenderer Head;
    public ModelRenderer Snout;
    public ModelRenderer Ear1;
    public ModelRenderer Ear2;
    public ModelRenderer Tail;
    public ModelRenderer Torso;

    public WerewolfModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.Ear1 = new ModelRenderer(this, 0, 0);
        this.Ear1.setRotationPoint(-5.0F, -9.0F, -2.0F);
        this.Ear1.addBox(1.0F, -1.0F, 2.0F, 2, 2, 2, 0.0F);
        this.Arm1 = new ModelRenderer(this, 40, 16);
        this.Arm1.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.Arm1.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Torso = new ModelRenderer(this, 16, 16);
        this.Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Torso.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.Arm2 = new ModelRenderer(this, 32, 48);
        this.Arm2.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.Arm2.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Snout = new ModelRenderer(this, 0, 32);
        this.Snout.setRotationPoint(-2.0F, -3.0F, -7.0F);
        this.Snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 4, 0.0F);
        this.Tail = new ModelRenderer(this, 16, 32);
        this.Tail.setRotationPoint(-2.0F, 8.0F, 0.0F);
        this.Tail.addBox(1.0F, -1.0F, 2.0F, 2, 2, 8, 0.0F);
        this.setRotateAngle(Tail, -1.0016444577195458F, 0.0F, 0.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg2 = new ModelRenderer(this, 16, 48);
        this.leg2.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Ear2 = new ModelRenderer(this, 0, 0);
        this.Ear2.setRotationPoint(1.0F, -9.0F, -2.0F);
        this.Ear2.addBox(1.0F, -1.0F, 2.0F, 2, 2, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head.render(f5);
        this.Ear1.render(f5);
        this.Arm1.render(f5);
        this.Torso.render(f5);
        this.Arm2.render(f5);
        this.Snout.render(f5);
        this.Tail.render(f5);
        this.leg1.render(f5);
        this.leg2.render(f5);
        this.Ear2.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
