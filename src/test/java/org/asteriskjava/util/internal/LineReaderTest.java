package org.asteriskjava.util.internal;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author sere
 * @since 2018-01-30
 */
public class LineReaderTest {
	public static void main(String[] args) throws Exception {
		final byte[] bytes = new byte[1000_000_000];
		byte[] str = "Hallo hello: one line with text!\r\n".getBytes(StandardCharsets.US_ASCII);
		for (int i = 0; i < bytes.length; i += str.length) {
			System.arraycopy(str, 0, bytes, i, str.length);
		}

		for (int i = 10; i-- > 0; ) {
			System.out.print(i + ":\t");

			InputStream inputStream = new ByteArrayInputStream(bytes);

			LineReader reader = new LineReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8), "\r\n");
			Scanner scanner = new Scanner(reader);
			scanner.useDelimiter(Pattern.compile("\r\n"));

			long start = System.currentTimeMillis();
			try {
				while (reader.readLine() != null) {
				}
			} catch (NoSuchElementException e) {
			}
			System.out.println((System.currentTimeMillis() - start));
		}
	}

	public static void main2(String[] args) throws IOException {
		Charset charset = StandardCharsets.UTF_8;
		PipedOutputStream out = new PipedOutputStream();
		final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out, charset));
		LineReader r = new LineReader(new InputStreamReader(new PipedInputStream(out), charset), "123");

		new Thread() {
			@Override
			public void run() {
				try {
					write("ab");
					write("12");
					write("3");
					write("ab");
					write("12");
					write("ab");
					write("121");
					write("2");
					write("3");
					write("321");
					write("2312");

					System.out.println("--");
					w.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void write(String s) throws Exception {
				System.out.println("<< " + s);
				w.write(s);
				w.flush();
				Thread.sleep(1000);
			}
		}.start();

		System.out.println("1: " + r.readLine());
		System.out.println("2: " + r.readLine());
		System.out.println("3: " + r.readLine());
		System.out.println("4: " + r.readLine());
		System.out.println("5: " + r.readLine());
	}
}
