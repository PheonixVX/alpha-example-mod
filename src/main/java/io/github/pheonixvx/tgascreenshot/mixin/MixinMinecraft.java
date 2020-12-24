package io.github.pheonixvx.tgascreenshot.mixin;

import io.github.pheonixvx.tgascreenshot.interfaces.EntityRendererAccess;
import io.github.pheonixvx.tgascreenshot.interfaces.ScreenShotHelperAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.ScreenShotHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Shadow
	public GuiIngame field_6308_u;
	@Shadow
	private static File field_6275_Z;
	@Shadow
	public int field_6326_c;
	@Shadow
	public int field_6325_d;
	@Shadow
	public RenderEngine field_6315_n;
	@Shadow
	public EntityRenderer field_6311_r;


	@Inject(
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z", ordinal = 1),
		method = "func_6248_s")
	private void func_6248_sInject (CallbackInfo ci) {
		if(Keyboard.isKeyDown(42))
		{
			field_6308_u.func_552_a(func_21001_a(field_6275_Z, field_6326_c, field_6325_d, 36450, 17700));
		} else
		{
			field_6308_u.func_552_a(ScreenShotHelper.func_4148_a(field_6275_Z, field_6326_c, field_6325_d));
		}
	}

	private String func_21001_a(File file, int i, int j, int k, int l)
	{
		try
		{
			ByteBuffer bytebuffer = BufferUtils.createByteBuffer(i * j * 3);
			ScreenShotHelper screenshothelper = new ScreenShotHelper();
			((ScreenShotHelperAccess) screenshothelper).InstantiateScreenShot(file, k, l, j);
			double d = (double)k / (double)i;
			double d1 = (double)l / (double)j;
			double d2 = d <= d1 ? d1 : d;
			for(int i1 = ((l - 1) / j) * j; i1 >= 0; i1 -= j)
			{
				for(int j1 = 0; j1 < k; j1 += i)
				{
					int k1 = i;
					int l1 = j;
					GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, field_6315_n.getTexture("/terrain.png"));
					double d3 = ((double)(k - i) / 2D) * 2D - (double)(j1 * 2);
					double d4 = ((double)(l - j) / 2D) * 2D - (double)(i1 * 2);
					d3 /= i;
					d4 /= j;
					((EntityRendererAccess) field_6311_r).func_21152_a(d2, d3, d4);
					field_6311_r.func_4134_c(1.0F);
					((EntityRendererAccess) field_6311_r).resetZoom();
					Display.update();
					try
					{
						Thread.sleep(10L);
					}
					catch(InterruptedException interruptedexception)
					{
						interruptedexception.printStackTrace();
					}
					Display.update();
					bytebuffer.clear();
					GL11.glPixelStorei(3333 /*GL_PACK_ALIGNMENT*/, 1);
					GL11.glPixelStorei(3317 /*GL_UNPACK_ALIGNMENT*/, 1);
					GL11.glReadPixels(0, 0, k1, l1, 32992 /*GL_BGR_EXT*/, 5121 /*GL_UNSIGNED_BYTE*/, bytebuffer);
					((ScreenShotHelperAccess) screenshothelper).func_21189_a(bytebuffer, j1, i1, k1, l1);
				}

				((ScreenShotHelperAccess) screenshothelper).func_21191_a();
			}

			return ((ScreenShotHelperAccess) screenshothelper).func_21190_b();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			return (new StringBuilder()).append("Failed to save image: ").append(exception).toString();
		}
	}
}
