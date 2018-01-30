package org.asteriskjava.util.internal;

import java.io.IOException;
import java.io.Reader;

public class LineReader extends Reader
{
	private Reader in;
	private char[] pattern;
	private char[] cbuf;
	private int off, len;

	/**
	 * @param pattern the pattern used as delimiter for line breaks in {@link #readLine()}
	 */
	public LineReader(Reader in, String pattern)
	{
		if (pattern.isEmpty())
		{
			throw new IllegalArgumentException("pattern must not be empty");
		}
		this.in = in;
		this.pattern = pattern.toCharArray();
		cbuf = new char[8192 + pattern.length()];
	}

	@Override
	public void close() throws IOException
	{
		synchronized (lock)
		{
			if (in == null)
			{
				return;
			}
			try
			{
				in.close();
			}
			finally
			{
				// free memory
				in = null;
				pattern = null;
				cbuf = null;

				// disable reading
				len = -1;
			}
		}
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		if (len <= 0)
		{
			return 0;
		}
		if (off < 0 || off + len >= cbuf.length)
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		synchronized (lock)
		{
			// fill buffer from stream
			if (this.len == 0)
			{
				this.off = 0;
				this.len = in.read(this.cbuf);
			}

			// end of stream?
			if (this.len < 0)
			{
				// free memory
				pattern = null;
				this.cbuf = null;
				return -1;
			}

			// copy buffer to result
			len = Math.min(len, this.len);
			System.arraycopy(this.cbuf, this.off, cbuf, off, len);
			this.off += len;
			this.len -= len;
			return len;
		}
	}

	/**
	 * Reads a line of text.
	 * A line is considered to be terminated by {@link #pattern}.
	 *
	 * @return A String containing the contents of the line, not including the pattern {@link #pattern}, or null if the end of the stream has been reached
	 * @exception IOException If an I/O error occurs
	 * @see java.io.BufferedReader#readLine()
	 */
	public String readLine() throws IOException
	{
		synchronized (lock)
		{
			if (len < 0)
			{
				return null;
			}

			StringBuilder result = new StringBuilder(80);
			while (true)
			{
				// search pattern in buffer
				int matches = 0;
				int limit = len - off;
				for (int i = off; i < limit; )
				{
					if (cbuf[i++] == pattern[matches])
					{
						matches++;
						if (matches == pattern.length)
						{
							int size = i - off;
							result.append(cbuf, off, size - matches);
							off += size;
							len -= size;
							return result.toString();
						}
					}
					else
					{
						// backtrack
						i -= matches;
						matches = 0;
					}
				}

				// copy buffer to result except for matched suffix
				result.append(cbuf, off, len - matches);

				// fill buffer from stream
				// (leaving some space to prepend matched suffix later)
				len = in.read(cbuf, matches, cbuf.length - matches);

				// end of stream?
				if (len < 0)
				{
					// append matched suffix
					result.append(pattern, 0, matches);
					// free memory
					pattern = null;
					cbuf = null;
					return result.length() > 0 ? result.toString() : null;
				}

				// prepend previously matched suffix as new prefix (for search during next iteration)
				System.arraycopy(pattern, 0, cbuf, 0, matches);
				off = 0;
				len += matches; // chars from stream + previously matched suffix
			}
		}
	}
}
