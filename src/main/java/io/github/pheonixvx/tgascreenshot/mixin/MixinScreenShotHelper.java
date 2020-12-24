package io.github.pheonixvx.tgascreenshot.mixin;

import io.github.pheonixvx.tgascreenshot.interfaces.ScreenShotHelperAccess;
import net.minecraft.src.ScreenShotHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

@Mixin(ScreenShotHelper.class)
public class MixinScreenShotHelper implements ScreenShotHelperAccess {

	@Shadow
	private static DateFormat field_4287_a;
	@Shadow
	private static ByteBuffer field_4286_b;
	@Shadow
	private static byte[] field_4289_c;
	@Shadow
	private static int[] field_4288_d;

	private int field_21197_e;
	private DataOutputStream field_21196_f;
	private byte field_21195_g[];
	private int field_21194_h;
	private int field_21193_i;
	private File field_21192_j;

	/**
	 * @author PheonixVX
	 * @reason Just a quick-and-dirty implementation
	 */
	@Overwrite
	public static String func_4148_a(File file, int i, int j) {
		try
		{
			File file1 = new File(file, "screenshots");
			file1.mkdir();
			if(field_4286_b == null || field_4286_b.capacity() < i * j * 3)
			{
				field_4286_b = BufferUtils.createByteBuffer(i * j * 3);
				field_4289_c = new byte[i * j * 3];
				field_4288_d = new int[i * j];
			}
			GL11.glPixelStorei(3333, 1);
			GL11.glPixelStorei(3317, 1);
			field_4286_b.clear();
			GL11.glReadPixels(0, 0, i, j, 6407, 5121, field_4286_b);
			field_4286_b.clear();
			String s = "" + field_4287_a.format(new Date());
			File file2;
			for(int k = 1; (file2 = new File(file1, s + (k != 1 ? "_" + k : "") + ".png")).exists(); k++) { }
			field_4286_b.get(field_4289_c);
			for(int l = 0; l < i; l++)
			{
				for(int i1 = 0; i1 < j; i1++)
				{
					int j1 = l + (j - i1 - 1) * i;
					int k1 = field_4289_c[j1 * 3] & 0xff;
					int l1 = field_4289_c[j1 * 3 + 1] & 0xff;
					int i2 = field_4289_c[j1 * 3 + 2] & 0xff;
					int j2 = 0xff000000 | k1 << 16 | l1 << 8 | i2;
					field_4288_d[l + i1 * i] = j2;
				}

			}

			BufferedImage bufferedimage = new BufferedImage(i, j, 1);
			bufferedimage.setRGB(0, 0, i, j, field_4288_d, 0, i);
			ImageIO.write(bufferedimage, "png", file2);
			return "Saved screenshot as " + file2.getName();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			return "Failed to save: " + exception;
		}
	}

	public final void InstantiateScreenShot(File file, int i, int j, int k) throws IOException {
		field_21194_h = i;
		field_21193_i = j;
		field_21197_e = k;
		File file1 = new File(file, "screenshots");
		file1.mkdir();
		String s = "huge_" + field_4287_a.format(new Date());
		for(int l = 1; (field_21192_j = new File(file1, s + (l != 1 ? "_" + l : "") + ".tga")).exists(); l++) { }
		byte abyte0[] = new byte[18];
		abyte0[2] = 2;
		abyte0[12] = (byte)(i % 256);
		abyte0[13] = (byte)(i / 256);
		abyte0[14] = (byte)(j % 256);
		abyte0[15] = (byte)(j / 256);
		abyte0[16] = 24;
		field_21195_g = new byte[i * k * 3];
		field_21196_f = new DataOutputStream(new FileOutputStream(field_21192_j));
		field_21196_f.write(abyte0);
	}

	public void func_21189_a(ByteBuffer bytebuffer, int i, int j, int k, int l)
	{
		int i1 = k;
		int j1 = l;
		if(i1 > field_21194_h - i)
		{
			i1 = field_21194_h - i;
		}
		if(j1 > field_21193_i - j)
		{
			j1 = field_21193_i - j;
		}
		field_21197_e = j1;
		for(int k1 = 0; k1 < j1; k1++)
		{
			bytebuffer.position((l - j1) * k * 3 + k1 * k * 3);
			int l1 = (i + k1 * field_21194_h) * 3;
			bytebuffer.get(field_21195_g, l1, i1 * 3);
		}

	}

	public void func_21191_a() throws IOException
	{
		field_21196_f.write(field_21195_g, 0, field_21194_h * 3 * field_21197_e);
	}

	public String func_21190_b() throws IOException
	{
		field_21196_f.close();
		return "Saved screenshot as " + field_21192_j.getName();
	}
}
