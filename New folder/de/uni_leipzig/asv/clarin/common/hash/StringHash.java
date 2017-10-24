/* 
 * "$Id: StringHash.java,v 1.2 2002/02/28 09:52:37 boehm Exp $"
 * (c) Copyright TextTech Leipzig GmbH, 2000-2001. All rights reserved.
 */
package de.uni_leipzig.asv.clarin.common.hash;

/**
 * Contains hash algorithms for strings. So far there is only one algorithm used
 * in table saetze from Wortschatz project.
 * 
 * @author Timo Boehme
 * @version 1.0
 */
// ----------------------------------------------------------------------------
public class StringHash {
	// ----------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	public int getHashNew(final String _str) {
		// --------------------------------------------------------------------------

		return _str.hashCode();
	}

	/**
	 * Calculates the hash value for a given string.
	 * 
	 * The algorithm uses every character up to strings with 15 chars. For
	 * larger strings the first 15 chars added by a maximum of 16 other chars
	 * equally distributed over the rest of the string are used. (algorithm was
	 * taken from JDK 1.1 but changed (T.Boehme))
	 * 
	 **/
	// --------------------------------------------------------------------------
	public int getHash(final String str) {
		// --------------------------------------------------------------------------

		/** hash value */
		int h = 0;

		/** current character in string */
		int off = 0;

		/** using a char array we don't have to call a method o access a char */
		final char val[] = str.toCharArray();

		/** save some time by storing the length of the string */
		final int wholeLen = val.length;

		// calculate hash for the first 15 characters
		final int tmpLen = (wholeLen < 16) ? wholeLen : 15;

		for (int i = tmpLen; i > 0; i--) {
			h = (h * 37) + val[off++];
		}

		// process the rest of the string
		if (wholeLen >= 16) {
			// only sample some characters from rest
			final int skip = wholeLen / 16;
			for (int i = wholeLen; i > 15; i -= skip, off += skip) {
				h = (h * 39) + val[off];
			}
		}

		return (h < 0) ? -h : h;
	}
}
