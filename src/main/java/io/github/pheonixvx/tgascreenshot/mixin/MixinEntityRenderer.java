package io.github.pheonixvx.tgascreenshot.mixin;

import io.github.pheonixvx.tgascreenshot.interfaces.EntityRendererAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer implements EntityRendererAccess {

	private double cameraZoom;
	private double cameraYaw;
	private double cameraPitch;

	@Shadow
	private float field_1387_i;
	@Shadow
	private Minecraft mc;
	@Shadow
	private float func_914_d(float var1) {
		return 0.0f;
	}
	@Shadow
	private void func_920_e(float var1) {
	}
	@Shadow
	private void func_917_f(float var1) {
	}
	@Shadow
	private void func_4138_g(float var1) {
	}


	@Inject(at = @At("TAIL"), method = "<init>")
	private void constructorInject (Minecraft minecraft, CallbackInfo ci) {
		cameraZoom = 1.0D;
		cameraYaw = 0.0D;
		cameraPitch = 0.0D;
	}

	/**
	 * @author PheonixVX
	 * @reason Too lazy lmfao
	 */
	@Overwrite
	private void func_4139_a(float var1, int var2) {
		field_1387_i = 256 >> mc.field_6304_y.renderDistance;
		GL11.glMatrixMode(5889);
		GL11.glLoadIdentity();
		float f1 = 0.07F;
		if(mc.field_6304_y.anaglyph)
		{
			GL11.glTranslatef((float)(-(var2 * 2 - 1)) * f1, 0.0F, 0.0F);
		}
		if(cameraZoom != 1.0D)
		{
			GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
			GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
			GLU.gluPerspective(func_914_d(var1), (float)mc.field_6326_c / (float)mc.field_6325_d, 0.05F, field_1387_i);
		} else
		{
			GLU.gluPerspective(func_914_d(var1), (float)mc.field_6326_c / (float)mc.field_6325_d, 0.05F, field_1387_i);
		}

		GL11.glMatrixMode(5888);
		GL11.glLoadIdentity();
		if(mc.field_6304_y.anaglyph)
		{
			GL11.glTranslatef((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}
		func_920_e(var1);
		if(mc.field_6304_y.viewBobbing)
		{
			func_917_f(var1);
		}
		float f2 = mc.field_6322_g.field_4133_d + (mc.field_6322_g.field_4134_c - mc.field_6322_g.field_4133_d) * var1;
		if(f2 > 0.0F)
		{
			float f3 = 5F / (f2 * f2 + 5F) - f2 * 0.04F;
			f3 *= f3;
			GL11.glRotatef(f2 * f2 * 1500F, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
			GL11.glRotatef(-f2 * f2 * 1500F, 0.0F, 1.0F, 1.0F);
		}
		func_4138_g(var1);
	}

	public void func_21152_a(double d, double d1, double d2)
	{
		cameraZoom = d;
		cameraYaw = d1;
		cameraPitch = d2;
	}

	public void resetZoom()
	{
		cameraZoom = 1.0D;
	}
}
