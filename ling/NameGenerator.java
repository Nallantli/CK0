package ling;

import java.util.Random;

public class NameGenerator {
	public static void main(String[] args) {
		Language lang = new Language(new Random());

		for (int i = 0; i < 10; i++) {
			Syllable syl = lang.genSyllable();
			System.out.println(syl);
		}
		for (int i = 0; i < 10; i++) {
			Word word = lang.genNoun("" + i, false);
			System.out.println("[" + word + "]\t" + word.transpose());
			Word word2 = lang.genNoun("" + i, true);
			System.out.println("[" + word2 + "]\t" + word2.transpose());
		}
	}
}
