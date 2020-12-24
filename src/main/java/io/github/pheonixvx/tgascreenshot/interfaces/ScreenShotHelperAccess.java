package io.github.pheonixvx.tgascreenshot.interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface ScreenShotHelperAccess {
	void InstantiateScreenShot(File file, int i, int j, int k) throws IOException;
	void func_21189_a(ByteBuffer bytebuffer, int i, int j, int k, int l);
	void func_21191_a() throws IOException;
	String func_21190_b() throws IOException;
}
