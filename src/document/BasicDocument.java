package document;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A naive implementation of the Document abstract class.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class BasicDocument extends Document {
	/**
	 * Create a new BasicDocument object
	 * 
	 * @param text
	 *            The full text of the Document.
	 */
	public BasicDocument(String text) {
		super(text);
	}

	/**
	 * Get the number of words in the document. "Words" are defined as
	 * contiguous strings of alphabetic characters i.e. any upper or lower case
	 * characters a-z or A-Z
	 * 
	 * @return The number of words in the document.
	 */
	@Override
	public int getNumWords() {
		List<String> word = getTokens("[a-zA-Z]+");
		return word.size();
	}

	/**
	 * Get the number of sentences in the document. Sentences are defined as
	 * contiguous strings of characters ending in an end of sentence punctuation
	 * (. ! or ?) or the last contiguous set of characters in the document, even
	 * if they don't end with a punctuation mark.
	 * 
	 * @return The number of sentences in the document.
	 */
	@Override
	public int getNumSentences() { // [^0-9\\.0-9]+
									// 1 4 0 3 12 0 1 13 0 1 4 0 1 7 0 115 1000
									// 0
									// 1 4 0 3 12 0 1 13 0 3 4 0 2 7 0 115 1000
									// 0
		List<String> senttences = getTokens("[!?.]+|[a-zA-Z]+$");

		return senttences.size();
	}

	/**
	 * Get the number of sentences in the document. Words are defined as above.
	 * Syllables are defined as: a contiguous sequence of vowels, except for an
	 * "e" at the end of a word if the word has another set of contiguous
	 * vowels, makes up one syllable. y is considered a vowel.
	 * 
	 * @return The number of syllables in the document.
	 */
	@Override
	public int getNumSyllables() {
		int num = 0;
		List<String> words = getTokens("[a-zA-Z]+");
		for (String word : words) {
			String pattern = "[aeiouyAEIOUY]+";
			Pattern tokSplitter = Pattern.compile(pattern);
			Matcher m = tokSplitter.matcher(word);
			String lastToken = "";
			int numOfNonEs = 0;
			while (m.find()) {
				num++;
				lastToken = m.group();
				if(word.split("[e]").length > 0 &&
						word.endsWith("e") &&
						lastToken.equalsIgnoreCase("e") &&
						numOfNonEs > 0 &&
						m.start() == word.lastIndexOf("e")){
					num --;
				}else{
					numOfNonEs ++;
				}
			}
		}
		return num;
	}

	/*
	 * The main method for testing this class. You are encouraged to add your
	 * own tests.
	 */
	public static void main(String[] args) {
		testCase(
				new BasicDocument(
						"This is a test.  How many???  " + "Senteeeeeeeeeences are here... there should be 5!  Right?"),
				16, 13, 5);
		testCase(new BasicDocument(""), 0, 0, 0);
		testCase(new BasicDocument("sentence, with, lots, of, commas.!  " + "(And some poaren)).  The output is: 7.5."),
				15, 11, 4);
		testCase(new BasicDocument("many???  Senteeeeeeeeeences are"), 6, 3, 2);
	}

}
