package features;

/**
 * JEP 254: Compact Strings
 * <p>
 * The current implementation of the String class stores characters in a char array, using two bytes (sixteen bits) for each character. Data
 * gathered from many different applications indicates that strings are a major component of heap usage and, moreover, that most String
 * objects contain only Latin-1 characters. Such characters require only one byte of storage, hence half of the space in the internal char
 * arrays of such String objects is going unused.
 * <p>
 * We propose to change the internal representation of the String class from a UTF-16 char array to a byte array plus an encoding-flag
 * field. The new String class will store characters encoded either as ISO-8859-1/Latin-1 (one byte per character), or as UTF-16 (two bytes
 * per character), based upon the contents of the string. The encoding flag will indicate which encoding is used.
 * <p>
 * COMPACT_STRINGS = true -> StringLatin1; false -> StringUTF16.
 * <p>
 * <a href="https://openjdk.org/jeps/254">Doc</a>
 */
public class CompactStrings {

    public static void main(String[] args) {

    }

}
