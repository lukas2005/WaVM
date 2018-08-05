package com.aguila.wavm.potions;

import com.aguila.wavm.init.WVCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class BasePotion extends Potion {

	ResourceLocation icon = null;

	protected BasePotion(boolean isBadEffectIn, int liquidColorIn, String name) {
		super(isBadEffectIn, liquidColorIn);

		setRegistryName(new ResourceLocation(WVCore.MODID, name));
		setIconResource(new ResourceLocation(WVCore.MODID, "textures/effects/"+name+".png"));
		setPotionName("potion."+WVCore.MODID+"."+name);
	}

	public BasePotion setIconResource(ResourceLocation icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		if (icon != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(icon);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		if (icon != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(icon);
			Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
		}
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);

		onPotionStarted(entityLivingBaseIn, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);

		onPotionEnded(entityLivingBaseIn, amplifier);
	}

	public void onPotionEnded(EntityLivingBase entity, int amplifier) {}
	public void onPotionStarted(EntityLivingBase entity, int amplifier) {}
}
