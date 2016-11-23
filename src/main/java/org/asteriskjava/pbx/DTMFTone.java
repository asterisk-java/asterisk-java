package org.asteriskjava.pbx;

/**
 * Used to indicate the set of possible DTMF tones which can be sent.
 * 
 * @author bsutton
 * 
 */
public enum DTMFTone
{
	ZERO('0'), ONE('1'), TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'),
	HASH('#'), STAR('*');

	private final String character;

	DTMFTone(final char character)
	{
		this.character = new String(new char[] { character });
	}

	@Override
	public String toString()
	{
		return this.character;
	}

}
