package werewolvesAndVampires.vampires.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelVamp
 * Created using Tabula 5.1.0
 */
public class ModelVamp extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer leg_right;
    public ModelRenderer leg_left;
    public ModelRenderer body;
    public ModelRenderer robe;
    public ModelRenderer arm_right;
    public ModelRenderer armbar;
    public ModelRenderer nose;
    public ModelRenderer ear_right;
    public ModelRenderer ear_left;
    public ModelRenderer arm_left;

    public ModelVamp() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.robe = new ModelRenderer(this, 0, 38);
        this.robe.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.robe.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
        this.arm_left = new ModelRenderer(this, 44, 22);
        this.arm_left.mirror = true;
        this.arm_left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.arm_left.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.leg_left = new ModelRenderer(this, 0, 22);
        this.leg_left.mirror = true;
        this.leg_left.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.leg_left.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.ear_right = new ModelRenderer(this, 56, 0);
        this.ear_right.setRotationPoint(-5.1F, -5.9F, 0.7F);
        this.ear_right.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(ear_right, 0.0F, 0.0F, -0.24434609527920614F);
        this.nose = new ModelRenderer(this, 24, 0);
        this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.nose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
        this.leg_right = new ModelRenderer(this, 0, 22);
        this.leg_right.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.leg_right.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.ear_left = new ModelRenderer(this, 56, 0);
        this.ear_left.setRotationPoint(3.1F, -6.3F, 0.7F);
        this.ear_left.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(ear_left, 0.0F, 0.0F, 0.24434609527920614F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.arm_right = new ModelRenderer(this, 44, 22);
        this.arm_right.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.arm_right.addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(arm_right, -0.7499679795819634F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 16, 20);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.armbar = new ModelRenderer(this, 40, 38);
        this.armbar.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.armbar.addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0.0F);
        this.setRotateAngle(armbar, -0.7499679795819634F, 0.0F, 0.0F);
        this.arm_right.addChild(this.arm_left);
        this.head.addChild(this.ear_right);
        this.head.addChild(this.nose);
        this.head.addChild(this.ear_left);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.robe.render(f5);
        this.leg_left.render(f5);
        this.leg_right.render(f5);
        this.head.render(f5);
        this.arm_right.render(f5);
        this.body.render(f5);
        this.armbar.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.arm_right.rotationPointY = 3.0F;
        this.arm_right.rotationPointZ = -1.0F;
        this.arm_right.rotateAngleX = -0.75F;
        this.leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.leg_right.rotateAngleY = 0.0F;
        this.leg_left.rotateAngleY = 0.0F;
    }
}
