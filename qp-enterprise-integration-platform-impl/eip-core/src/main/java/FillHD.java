import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author bhausen
 */
public class FillHD {
	public static void main(final String[] args) {
		long l = System.currentTimeMillis();
		File dir = new File(Long.toString(l));
		byte[] bytes = new byte[1024 * 1024];
		byte x = (byte) 255;
		if (l % 2 == 1) {
			x = (byte) 0;
			System.out.println("Write 0 bits");
		} else {
			System.out.println("Write 1 bits");
		}
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = x;
		}
		try {
			run(dir, bytes);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void run(final File dir, final byte[] bytes)
			throws IOException {
		if (dir.mkdirs()) {
			File f;
			for (int i = 0; i < 128; i++) {
				f = new File(dir, new StringBuffer().append(i).append(".hex")
						.toString());
				if (f.createNewFile()) {
					write(f, bytes);
				}
			}
			run(new File(dir, Long.toString(System.currentTimeMillis())), bytes);
		}
	}

	private static void write(final File f, final byte[] bytes)
			throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(f));
		for (int i = 0; i < 32; i++) {
			bos.write(bytes);
		}
		bos.flush();
		bos.close();
	}
}
