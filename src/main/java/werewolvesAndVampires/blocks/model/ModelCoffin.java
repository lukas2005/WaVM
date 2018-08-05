package werewolvesAndVampires.blocks.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Coffin - AguilaDaddy
 * Created using Tabula 7.0.0
 */
public class ModelCoffin extends ModelBase {
    public ModelRenderer coffin_bed;
    public ModelRenderer wall1;
    public ModelRenderer wall2;
    public ModelRenderer wall3;
    public ModelRenderer wall4;
    public ModelRenderer coffin_bottom;
    public ModelRenderer coffin_top;
    public ModelRenderer coffin_top2;
    public ModelRenderer coffin_top3;

    public ModelCoffin() {
        this.textureWidth = 200;
        this.textureHeight = 200;

        this.coffin_bed = new ModelRenderer(this, 0, 0);
        this.coffin_bed.setRotationPoint(-7.0F, 15.0F, -7.0F);
        this.coffin_bed.addBox(0.0F, 0.0F, 0.0F, 14, 8, 30, 0.0F);

        this.coffin_bottom = new ModelRenderer(this, 100, 60);
        this.coffin_bottom.setRotationPoint(-8.0F, 23.0F, -8.0F);
        this.coffin_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 1, 32, 0.0F);

		this.wall2 = new ModelRenderer(this, 100, 100);
		this.wall2.setRotationPoint(6.0F, 10.0F, -6.0F);
        this.wall2.addBox(0.0F, 0.0F, 0.0F, 1, 5, 28, 0.0F);

        this.wall1 = new ModelRenderer(this, 100, 20);
        this.wall1.setRotationPoint(-7.0F, 10.0F, 22.0F);
        this.wall1.addBox(0.0F, 0.0F, 0.0F, 14, 5, 1, 0.0F);

        this.wall4 = new ModelRenderer(this, 100, 150);
        this.wall4.setRotationPoint(-7.0F, 10.0F, -6.0F);
        this.wall4.addBox(0.0F, 0.0F, 0.0F, 1, 5, 28, 0.0F);

        this.wall3 = new ModelRenderer(this, 100, 40);
        this.wall3.setRotationPoint(-7.0F, 10.0F, -7.0F);
        this.wall3.addBox(0.0F, 0.0F, 0.0F, 14, 5, 1, 0.0F);

		this.coffin_top2 = new ModelRenderer(this, 0, 60);
		this.coffin_top2.setRotationPoint(1.0F, -1.0F, -14.0F);
		this.coffin_top2.addBox(0.0F, 0.0F, 0.0F, 14, 1, 30, 0.0F);

		this.coffin_top3 = new ModelRenderer(this, 0, 150);
        this.coffin_top3.setRotationPoint(1.0F, -1.0F, 1.0F);
        this.coffin_top3.addBox(0.0F, 0.0F, 0.0F, 12, 1, 28, 0.0F);

        this.coffin_top = new ModelRenderer(this, 0, 100);
        this.coffin_top.setRotationPoint(-8.0F, 9.0F, 7.0F);
        this.coffin_top.addBox(0.0F, 0.0F, -15.0F, 16, 1, 32, 0.0F);

        this.coffin_top.addChild(this.coffin_top2);
        this.coffin_top2.addChild(this.coffin_top3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.coffin_bed.render(f5);
        this.coffin_bottom.render(f5);
        this.wall2.render(f5);
        this.wall1.render(f5);
        this.wall4.render(f5);
        this.wall3.render(f5);
        this.coffin_top.render(f5);
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
