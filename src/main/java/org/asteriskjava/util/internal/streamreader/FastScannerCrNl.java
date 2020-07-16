package org.asteriskjava.util.internal.streamreader;

public class FastScannerCrNl extends FastScannerNl
{

    private char crChar = '\r';
    private boolean seenReturn = false;

    public FastScannerCrNl(Readable reader)
    {
        super(reader);

    }

    protected String getLine(boolean endOfLine)
    {
        // iterate the builder looking for the delimiter chars
        for (int i = start; i < end; i++)
        {
            if (cbuf.get(i) == crChar)
            {
                // flag that we've encountered the first of the two delimiter
                // characters
                seenReturn = true;
            }
            else if (seenReturn && cbuf.get(i) == nlChar)
            {
                // we've seen both of the delimiter characters, package up the
                // data and return it...
                if (i == start)
                {
                    // back track to remove a /r in the last packet
                    result.setLength(result.length() - 1);
                }
                if (i > start)
                {
                    // append the data to the StringBuilder
                    result.append(cbuf.subSequence(start, start + (i - start) - 1));
                }
                // skip the delimiter character
                start = i + 1;

                // get the resulting line
                String tmp = result.toString();

                // clear the StringBuilder
                result.setLength(0);

                // clear the delimiter seen flag
                seenReturn = false;

                return tmp;
            }
            else
            {
                // clear the delimiter seen flag
                seenReturn = false;
            }
        }
        if (end >= start)
        {
            // we've hit the end of the buffer, add the data to the
            // StringBuilder and return null so as to get
            // the next part of the line
            result.append(cbuf.subSequence(start, start + (end - start))).toString();
            start = 0;
            end = 0;
        }
        return null;
    }

}
