package test;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ling.Language;
import ling.Pair;
import ling.Word;

public class LangTest {
	public static void main(String[] args) {
		Language lang = new Language(new Random());
		Scanner scanner = new Scanner(System.in);
		
		String line;
		while(!(line = scanner.nextLine()).equals("q")) {
			if (line.equals(".")) {
				lang = new Language(new Random());
				continue;
			}
			if (line.equals("s")) {
				double moraic_sum = 0;
				List<Pair> syllables = lang.genAllSyllables();
				for (Pair s : syllables) {
					System.out.println(String.format("%.5f", s.usage * 100.0) + "%\t/" + s.toString() + "/\t" + s.s.moraicValue());
					moraic_sum += s.s.moraicValue();
				}
				System.out.println("Count:\t" + syllables.size());
				System.out.println("Avg Weight:\t" + (moraic_sum / (double)syllables.size()));
				continue;
			}
			String in = String.valueOf(Math.random());
			System.out.println("--------------------------------------------------");
			Word word = lang.getNoun(in, false);
			System.out.println(word.transpose());
			Word word2 = lang.getNoun(in, true);
			System.out.println(word2.transpose());
			lang.getNounRaw(in).print();
		}

		scanner.close();
	}
}
