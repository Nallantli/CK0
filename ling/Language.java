package ling;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class Cluster {
	List<Pair> p;

	public Cluster(String s[], Pair[] pairs) {
		p = new ArrayList<Pair>();
		for (String t : s) {
			boolean flag = false;
			for (Pair c : pairs) {
				if (c.p.value.equals(t)) {
					p.add(c);
					flag = true;
					break;
				}
			}
			if (!flag)
				System.err.println("ERR:\t" + t);
		}
	}

	public List<Phoneme> getPhonemes() {
		List<Phoneme> phonemes = new ArrayList<Phoneme>();
		for (Pair a : p) {
			phonemes.add(a.p);
		}
		return phonemes;
	}

	public double getProbability() {
		double prob = 1;
		for (Pair a : p) {
			prob *= a.usage;
		}
		return prob;
	}

	public boolean applicable(List<Phoneme> total, List<Phoneme> canStart) {
		int i = 0;
		for (Pair a : p) {
			if (canStart != null && i > 0 && !canStart.contains(a.p))
				return false;
			if (!total.contains(a.p))
				return false;
			i++;
		}
		return true;
	}

	public String toString() {
		return "[" + p.toString() + " : " + getProbability() + "]";
	}
}

public class Language {
	/*
	 * public static final Phoneme[] CON = { new Phoneme("b"), new Phoneme("k"), new
	 * Phoneme("d"), new Phoneme("f"), new Phoneme("g"), new Phoneme("h"), new
	 * Phoneme("m"), new Phoneme("n"), new Phoneme("p"), new Phoneme("s"), new
	 * Phoneme("t"), new Phoneme("v"), new Phoneme("st"), new Phoneme("sk"), new
	 * Phoneme("th"), new Phoneme("y"), new Phoneme("w"), new Phoneme("r"), new
	 * Phoneme("l"), new Phoneme("ś"), new Phoneme("ć"), new Phoneme("j"), new
	 * Phoneme("z"), new Phoneme("sm"), new Phoneme("sp"), new Phoneme("kh"), new
	 * Phoneme("sn"), new Phoneme("zh"), new Phoneme("ts") };
	 * 
	 * public static final Phoneme[] FIN = { new Phoneme("b"), new Phoneme("p"), new
	 * Phoneme("kh"), new Phoneme("f"), new Phoneme("zh"), new Phoneme("k"), new
	 * Phoneme("d"), new Phoneme("l"), new Phoneme("s"), new Phoneme("t"), new
	 * Phoneme("rt"), new Phoneme("rd"), new Phoneme("ft"), new Phoneme("ng"), new
	 * Phoneme("ns"), new Phoneme("nk"), new Phoneme("lt"), new Phoneme("ld"), new
	 * Phoneme("rs"), new Phoneme("ls"), new Phoneme("st"), new Phoneme("ks"), new
	 * Phoneme("ts"), new Phoneme("ś"), new Phoneme("ć"), new Phoneme("z"), new
	 * Phoneme("ps"), new Phoneme("pt"), new Phoneme("rk"), new Phoneme("lk"), new
	 * Phoneme("r"), new Phoneme("rf"), new Phoneme("lf"), new Phoneme("rn"), new
	 * Phoneme("rm"), new Phoneme("th"), new Phoneme("nd"), new Phoneme("nt"), new
	 * Phoneme("n"), new Phoneme("m") };
	 * 
	 * public static final Phoneme[] FLU = { new Phoneme("l"), new Phoneme("r"), new
	 * Phoneme("w") };
	 * 
	 * public static final Phoneme[] VOW = { new Phoneme("a"), new Phoneme("e"), new
	 * Phoneme("i"), new Phoneme("o"), new Phoneme("u") };
	 */
	public static final Pair CON[] = { new Pair(0.96, "m"), new Pair(0.90, "k"), new Pair(0.90, "j"),
			new Pair(0.86, "p"), new Pair(0.82, "w"), new Pair(0.78, "n"), new Pair(0.68, "t"), new Pair(0.68, "l"),
			new Pair(0.67, "s"), new Pair(0.63, "b"),
			// new Pair(0.63, "ŋ"),
			new Pair(0.57, "g"), new Pair(0.56, "h"), new Pair(0.46, "d"), new Pair(0.44, "r"), new Pair(0.44, "f"),
			new Pair(0.42, "ɲ"), new Pair(0.40, "tʃ"), new Pair(0.37, "ʔ"), new Pair(0.37, "ʃ"), new Pair(0.30, "z"),
			new Pair(0.27, "dʒ"), new Pair(0.27, "v"), new Pair(0.22, "ts"), new Pair(0.19, "x"), new Pair(0.16, "ʒ"),
			new Pair(0.12, "kʷ"), new Pair(0.10, "dz"), new Pair(0.06, "gʷ"), new Pair(0.05, "ð"), new Pair(0.05, "ç"),
			new Pair(0.03, "kʲ"), new Pair(0.03, "tʲ"), new Pair(0.03, "gʲ"), new Pair(0.03, "xʷ"),
			new Pair(0.02, "mʲ"), new Pair(0.02, "tɬ"), new Pair(0.02, "lʲ"), new Pair(0.02, "pʲ"),
			new Pair(0.02, "mʷ"), new Pair(0.02, "hʷ"), new Pair(0.02, "dʲ"), new Pair(0.02, "bʲ"),
			new Pair(0.02, "bʷ"), new Pair(0.02, "sʲ"), new Pair(0.02, "pʷ"), new Pair(0.02, "tʷ"),
			new Pair(0.01, "fʷ"), new Pair(0.01, "fʲ"), new Pair(0.01, "sʷ"), new Pair(0.01, "rʲ"),
			new Pair(0.01, "hʲ"), new Pair(0.01, "ʃʷ"), new Pair(0.01, "nʷ"), new Pair(0.01, "vʲ"),
			new Pair(0.01, "zʲ"), new Pair(0.01, "lʷ"), new Pair(0.01, "zʷ"), new Pair(0.01, "ʃʲ"),
			new Pair(0.01, "xʲ"), new Pair(0.01, "ʒʷ") };

	public static final Pair FIN[] = { new Pair(0.90, "m"), new Pair(0.70, "k"),
			// new Pair(0.90, "j"),
			new Pair(0.20, "p"), new Pair(0.05, "ç"),
			// new Pair(0.82, "w"),
			new Pair(0.96, "n"), new Pair(0.86, "t"), new Pair(0.68, "l"), new Pair(0.86, "s"), new Pair(0.20, "b"),
			new Pair(0.35, "ŋ"), new Pair(0.20, "g"), new Pair(0.04, "tɬ"),
			// new Pair(0.56, "h"),
			new Pair(0.70, "d"), new Pair(0.86, "r"), new Pair(0.44, "f"),
			// new Pair(0.42, "ɲ"),
			new Pair(0.50, "tʃ"), new Pair(0.28, "h"),
			// new Pair(0.37, "ʔ"),
			new Pair(0.55, "ʃ"), new Pair(0.70, "z"), new Pair(0.45, "dʒ"), new Pair(0.27, "v"), new Pair(0.63, "ts"),
			new Pair(0.19, "x"), new Pair(0.50, "ʒ"),
			// new Pair(0.12, "kʷ"),
			new Pair(0.10, "dz"),
			// new Pair(0.06, "gʷ"),
			new Pair(0.05, "ð"),
			// new Pair(0.03, "kʲ"),
			// new Pair(0.03, "tʲ"),
			// new Pair(0.03, "gʲ"),
			// new Pair(0.03, "xʷ"),
			// new Pair(0.02, "mʲ"),
			// new Pair(0.02, "lʲ"),
			// new Pair(0.02, "pʲ"),
			// new Pair(0.02, "mʷ"),
			// new Pair(0.02, "hʷ"),
			// new Pair(0.02, "dʲ"),
			// new Pair(0.02, "bʲ"),
			// new Pair(0.02, "bʷ"),
			// new Pair(0.02, "sʲ"),
			// new Pair(0.02, "pʷ"),
			// new Pair(0.02, "tʷ"),
			// new Pair(0.01, "fʷ"),
			// new Pair(0.01, "fʲ"),
			// new Pair(0.01, "sʷ"),
			// new Pair(0.01, "rʲ"),
			// new Pair(0.01, "hʲ"),
			// new Pair(0.01, "ʃʷ"),
			// new Pair(0.01, "nʷ"),
			// new Pair(0.01, "vʲ"),
			// new Pair(0.01, "zʲ"),
			// new Pair(0.01, "lʷ"),
			// new Pair(0.01, "zʷ"),
			// new Pair(0.01, "ʃʲ"),
			// new Pair(0.01, "xʲ"),
			// new Pair(0.01, "ʒʷ")
	};

	public static final Pair VOW[] = { new Pair(0.92, "i"), new Pair(0.88, "u"), new Pair(0.86, "a"),
			new Pair(0.61, "e"), new Pair(0.60, "o") };

	public static final Cluster CLU_VOW[] = { new Cluster(new String[] { "a", "i" }, VOW),
			new Cluster(new String[] { "e", "i" }, VOW), new Cluster(new String[] { "o", "i" }, VOW),
			new Cluster(new String[] { "e", "o" }, VOW), new Cluster(new String[] { "a", "u" }, VOW),
			new Cluster(new String[] { "e", "u" }, VOW), new Cluster(new String[] { "o", "u" }, VOW),
			new Cluster(new String[] { "i", "u" }, VOW), new Cluster(new String[] { "i", "a" }, VOW),
			new Cluster(new String[] { "i", "o" }, VOW), new Cluster(new String[] { "i", "e" }, VOW),
			new Cluster(new String[] { "a", "e" }, VOW), new Cluster(new String[] { "u", "a" }, VOW),
			new Cluster(new String[] { "u", "e" }, VOW), new Cluster(new String[] { "u", "i" }, VOW),
			new Cluster(new String[] { "u", "o" }, VOW) };

	public static final Cluster CLU_CON[] = { new Cluster(new String[] { "s", "m" }, CON),
			new Cluster(new String[] { "s", "n" }, CON), new Cluster(new String[] { "s", "t" }, CON),
			new Cluster(new String[] { "s", "t", "r" }, CON), new Cluster(new String[] { "s", "p" }, CON),
			new Cluster(new String[] { "s", "p", "r" }, CON), new Cluster(new String[] { "s", "p", "l" }, CON),
			new Cluster(new String[] { "s", "k" }, CON), new Cluster(new String[] { "s", "k", "r" }, CON),
			new Cluster(new String[] { "s", "v" }, CON), new Cluster(new String[] { "s", "l" }, CON),
			new Cluster(new String[] { "p", "l" }, CON), new Cluster(new String[] { "p", "r" }, CON),
			new Cluster(new String[] { "t", "r" }, CON), new Cluster(new String[] { "b", "l" }, CON),
			new Cluster(new String[] { "b", "r" }, CON), new Cluster(new String[] { "g", "l" }, CON),
			new Cluster(new String[] { "g", "r" }, CON), new Cluster(new String[] { "d", "r" }, CON),
			new Cluster(new String[] { "f", "l" }, CON), new Cluster(new String[] { "f", "r" }, CON),
			new Cluster(new String[] { "tʃ", "r" }, CON), new Cluster(new String[] { "ʃ", "n" }, CON),
			new Cluster(new String[] { "ʃ", "m" }, CON), new Cluster(new String[] { "ʃ", "t" }, CON),
			new Cluster(new String[] { "ʃ", "t", "r" }, CON), new Cluster(new String[] { "ʃ", "k" }, CON),
			new Cluster(new String[] { "ʃ", "k", "r" }, CON), new Cluster(new String[] { "ʃ", "k", "l" }, CON),
			new Cluster(new String[] { "ʃ", "p" }, CON), new Cluster(new String[] { "ʃ", "p", "r" }, CON),
			new Cluster(new String[] { "ʃ", "p", "l" }, CON), new Cluster(new String[] { "ʃ", "l" }, CON),
			new Cluster(new String[] { "v", "l" }, CON), new Cluster(new String[] { "v", "r" }, CON) };

	public static final Cluster CLU_FIN[] = { new Cluster(new String[] { "s", "t" }, FIN),
			new Cluster(new String[] { "s", "p" }, FIN), new Cluster(new String[] { "s", "k" }, FIN),
			new Cluster(new String[] { "n", "s" }, FIN), new Cluster(new String[] { "n", "k" }, FIN),
			new Cluster(new String[] { "n", "z" }, FIN), new Cluster(new String[] { "n", "tʃ" }, FIN),
			new Cluster(new String[] { "n", "s", "t" }, FIN), new Cluster(new String[] { "n", "t" }, FIN),
			new Cluster(new String[] { "n", "ts" }, FIN), new Cluster(new String[] { "n", "d" }, FIN),
			new Cluster(new String[] { "n", "dz" }, FIN), new Cluster(new String[] { "n", "dʒ" }, FIN),
			new Cluster(new String[] { "r", "s" }, FIN), new Cluster(new String[] { "r", "ts" }, FIN),
			new Cluster(new String[] { "r", "p" }, FIN), new Cluster(new String[] { "r", "n" }, FIN),
			new Cluster(new String[] { "r", "n", "s" }, FIN), new Cluster(new String[] { "r", "n", "t" }, FIN),
			new Cluster(new String[] { "r", "n", "ts" }, FIN), new Cluster(new String[] { "r", "m" }, FIN),
			new Cluster(new String[] { "r", "m", "s" }, FIN), new Cluster(new String[] { "r", "b" }, FIN),
			new Cluster(new String[] { "r", "k" }, FIN), new Cluster(new String[] { "r", "z" }, FIN),
			new Cluster(new String[] { "r", "tʃ" }, FIN), new Cluster(new String[] { "r", "s", "t" }, FIN),
			new Cluster(new String[] { "r", "t" }, FIN), new Cluster(new String[] { "r", "d" }, FIN),
			new Cluster(new String[] { "r", "dz" }, FIN), new Cluster(new String[] { "r", "dʒ" }, FIN),
			new Cluster(new String[] { "r", "f" }, FIN), new Cluster(new String[] { "l", "s" }, FIN),
			new Cluster(new String[] { "l", "n" }, FIN), new Cluster(new String[] { "l", "n", "z" }, FIN),
			new Cluster(new String[] { "l", "m" }, FIN), new Cluster(new String[] { "l", "m", "z" }, FIN),
			new Cluster(new String[] { "l", "p" }, FIN), new Cluster(new String[] { "l", "b" }, FIN),
			new Cluster(new String[] { "l", "k" }, FIN), new Cluster(new String[] { "l", "z" }, FIN),
			new Cluster(new String[] { "l", "tʃ" }, FIN), new Cluster(new String[] { "l", "ts" }, FIN),
			new Cluster(new String[] { "l", "s", "t" }, FIN), new Cluster(new String[] { "l", "t" }, FIN),
			new Cluster(new String[] { "l", "d" }, FIN), new Cluster(new String[] { "l", "f" }, FIN),
			new Cluster(new String[] { "p", "t" }, FIN), new Cluster(new String[] { "p", "s" }, FIN),
			new Cluster(new String[] { "k", "t" }, FIN), new Cluster(new String[] { "k", "s" }, FIN),
			new Cluster(new String[] { "g", "z" }, FIN) };

	public static final Pair[] SYL = { new Pair(0.9, "CV"), new Pair(0.85, "CVF"), new Pair(0.6, "VF"),
			new Pair(0.5, "V") };

	public List<Phoneme> canStart = new ArrayList<Phoneme>();

	public List<Phoneme> consonants = new ArrayList<Phoneme>();
	public List<Phoneme> finals = new ArrayList<Phoneme>();
	public List<Cluster> clu_consonants = new ArrayList<Cluster>();
	public List<Cluster> clu_finals = new ArrayList<Cluster>();
	public List<Cluster> clu_vowels = new ArrayList<Cluster>();
	public List<Phoneme> vowels = new ArrayList<Phoneme>();
	public List<String> syl = new ArrayList<String>();
	public Color c;

	private boolean plural_on_article, com;
	private boolean remove_gender;
	private boolean harmony;
	private boolean front_plural;

	private static List<Phoneme> phones = new ArrayList<Phoneme>();
	private static List<Syllable> formedSyllables = new ArrayList<Syllable>();

	int max_length, min_length, genders;

	List<Syllable> gender_endings = new ArrayList<Syllable>();
	List<Syllable> plural_endings = new ArrayList<Syllable>();
	List<Word> articles = new ArrayList<Word>();

	HashMap<String, NounContainer> nouns = new HashMap<String, NounContainer>();
	HashMap<Word, Integer> gen = new HashMap<Word, Integer>();

	HashMap<String, Word> names = new HashMap<String, Word>();

	Syllable male, female, absolutive;
	Phoneme combining = null;
	Phoneme filler = null;

	public Orthography ortho;

	Word name;

	public Random rand;

	public Language(Random rand) {
		this.rand = rand;

		this.ortho = Orthography.O1;

		for (Pair p : CON) {
			if (rand.nextDouble() < p.usage)
				consonants.add(p.p);
		}
		for (Pair p : FIN) {
			if (rand.nextDouble() < Math.pow(p.usage, 2))
				finals.add(p.p);
		}
		while (vowels.isEmpty()) {
			for (Pair p : VOW) {
				if (rand.nextDouble() < p.usage)
					vowels.add(p.p);
			}
		}
		while (syl.isEmpty()) {
			for (Pair p : SYL) {
				if (rand.nextDouble() < p.usage)
					syl.add(p.p.value);
			}
			if (!syl.contains("CV") && !syl.contains("CVF")) {
				consonants.clear();
			}
			if (!syl.contains("VF") && !syl.contains("CVF")) {
				finals.clear();
			}
		}

		canStart.addAll(consonants);
		for (Phoneme p : finals) {
			boolean flag = false;
			for (Phoneme p2 : canStart) {
				if (p2.value == p.value) {
					flag = true;
					break;
				}
			}
			if (!flag)
				canStart.add(p);
		}

		if (!canStart.isEmpty()) {
			Collections.shuffle(canStart);
			int amt = (int) Math.round(
					Math.pow(rand.nextInt(canStart.size()) / (double) canStart.size(), 1) * (double) canStart.size());
			for (int i = 0; i < amt; i++)
				canStart.remove(0);
		}

		/*
		 * ArrayList<Phoneme> total = new ArrayList<Phoneme>();
		 * total.addAll(consonants); total.addAll(finals);
		 */

		if (rand.nextBoolean()) {
			for (Cluster c : CLU_CON) {
				if (c.applicable(consonants, canStart) && rand.nextDouble() < c.getProbability())
					clu_consonants.add(c);
			}
		}
		if (rand.nextBoolean()) {
			for (Cluster c : CLU_FIN) {
				if (c.applicable(finals, canStart) && rand.nextDouble() < c.getProbability())
					clu_finals.add(c);
			}
		}
		for (Cluster v : CLU_VOW) {
			if (v.applicable(vowels, null) && rand.nextDouble() < v.getProbability())
				clu_vowels.add(v);
		}

		if (rand.nextInt(3) == 0 && !consonants.isEmpty())
			this.combining = getConsonant();

		this.filler = getVowel();
		this.harmony = rand.nextBoolean();

		Collections.shuffle(consonants);
		Collections.shuffle(finals);
		Collections.shuffle(vowels);
		Collections.shuffle(syl);
		Collections.shuffle(clu_consonants);
		Collections.shuffle(clu_finals);
		Collections.shuffle(clu_vowels);

		this.c = Color.getHSBColor((float) Math.random(), 1f, 1f);

		if (rand.nextBoolean())
			this.genders = rand.nextInt(5) + 2;
		else {
			this.genders = 0;
			if (rand.nextBoolean())
				absolutive = genSyllable();
			else
				absolutive = null;
		}

		this.min_length = rand.nextInt(this.genders > 0 ? 1 : 2) + 1;
		this.max_length = rand.nextInt(this.genders > 0 ? 2 : 3) + min_length + 1;

		this.remove_gender = rand.nextBoolean();

		this.front_plural = rand.nextBoolean();

		genGenders();
		genArticles();
		genPluralEndings();
		genNameGenders();

		this.com = rand.nextBoolean() && !articles.isEmpty();

		System.out.println("CONS\t" + consonants);
		System.out.println("STRT\t" + canStart);
		System.out.println("VOWS\t" + vowels);
		System.out.println("FINS\t" + finals);
		System.out.println("CLUC\t" + clu_consonants);
		System.out.println("CLUF\t" + clu_finals);
		System.out.println("CLUV\t" + clu_vowels);
		System.out.println("SYLL\t" + syl);
		System.out.println("COMB\t" + combining);
		System.out.println("FILL\t" + filler);
		System.out.println("ABSL\t" + absolutive);
		System.out.println("Vowel Harmony\t" + harmony);
		System.out.println("Max Length: " + (this.max_length));
		System.out.println("Min Length: " + (this.min_length));
		System.out.println("Masc Name: " + male);
		System.out.println("Fem Name: " + female);
		if (genders > 0) {
			System.out.println("Noun Genders: " + (this.genders));
			System.out.println(gender_endings);
			System.out.println("Remove Gender:\t" + remove_gender);
		}
		if (!articles.isEmpty()) {
			System.out.println("Combine Article:\t" + com);
			System.out.println("Plural Article:\t" + plural_on_article);
			System.out.println("Articles: " + articles);
		}
		System.out.println("Plural on front:\t" + front_plural);
		System.out.println("Plurality: " + plural_endings);
		System.out.println("------------------------------");

		name = getNoun("language", false);
	}

	public List<Pair> genAllSyllables() {
		List<Pair> syllables = new ArrayList<Pair>();
		if (syl.contains("CV") || syl.contains("CVF")) {
			for (Phoneme c : consonants) {
				double c_usage = getProbability(c, consonants);
				for (Phoneme v : vowels) {
					double v_usage = getProbability(v, vowels);
					if (syl.contains("CV")) {
						double s_usage = getProbability("CV", syl);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.add(c);
						phonemes.add(v);
						syllables.add(new Pair(s_usage * c_usage * v_usage, new Syllable(phonemes)));
					}
					if (syl.contains("CVF")) {
						double s_usage = getProbability("CVF", syl);
						for (Phoneme f : finals) {
							double f_usage = getProbability(f, finals);
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.add(c);
							phonemes.add(v);
							phonemes.add(f);
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
						for (Cluster f : clu_finals) {
							double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.add(c);
							phonemes.add(v);
							phonemes.addAll(f.getPhonemes());
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
					}
				}
				for (Cluster v : clu_vowels) {
					double v_usage = getProbability(v, clu_vowels) * 1.0 / 6.0;
					if (syl.contains("CV")) {
						double s_usage = getProbability("CV", syl);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.add(c);
						phonemes.addAll(v.getPhonemes());
						syllables.add(new Pair(s_usage * c_usage * v_usage, new Syllable(phonemes)));
					}
					if (syl.contains("CVF")) {
						double s_usage = getProbability("CVF", syl);
						for (Phoneme f : finals) {
							double f_usage = getProbability(f, finals);
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.add(c);
							phonemes.addAll(v.getPhonemes());
							phonemes.add(f);
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
						for (Cluster f : clu_finals) {
							double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.add(c);
							phonemes.addAll(v.getPhonemes());
							phonemes.addAll(f.getPhonemes());
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
					}
				}
			}
			for (Cluster c : clu_consonants) {
				double c_usage = getProbability(c, clu_consonants) * 1.0 / 6.0;
				for (Phoneme v : vowels) {
					double v_usage = getProbability(v, vowels);
					if (syl.contains("CV")) {
						double s_usage = getProbability("CV", syl);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.addAll(c.getPhonemes());
						phonemes.add(v);
						syllables.add(new Pair(s_usage * c_usage * v_usage, new Syllable(phonemes)));
					}
					if (syl.contains("CVF")) {
						double s_usage = getProbability("CVF", syl);
						for (Phoneme f : finals) {
							double f_usage = getProbability(f, finals);
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.addAll(c.getPhonemes());
							phonemes.add(v);
							phonemes.add(f);
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
						for (Cluster f : clu_finals) {
							double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.addAll(c.getPhonemes());
							phonemes.add(v);
							phonemes.addAll(f.getPhonemes());
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
					}
				}
				for (Cluster v : clu_vowels) {
					double v_usage = getProbability(v, clu_vowels) * 1.0 / 6.0;
					if (syl.contains("CV")) {
						double s_usage = getProbability("CV", syl);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.addAll(c.getPhonemes());
						phonemes.addAll(v.getPhonemes());
						syllables.add(new Pair(s_usage * c_usage * v_usage, new Syllable(phonemes)));
					}
					if (syl.contains("CVF")) {
						double s_usage = getProbability("CVF", syl);
						for (Phoneme f : finals) {
							double f_usage = getProbability(f, finals);
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.addAll(c.getPhonemes());
							phonemes.addAll(v.getPhonemes());
							phonemes.add(f);
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
						for (Cluster f : clu_finals) {
							double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
							List<Phoneme> phonemes = new ArrayList<Phoneme>();
							phonemes.addAll(c.getPhonemes());
							phonemes.addAll(v.getPhonemes());
							phonemes.addAll(f.getPhonemes());
							syllables.add(new Pair(s_usage * c_usage * v_usage * f_usage, new Syllable(phonemes)));
						}
					}
				}
			}
		}
		if (syl.contains("V") || syl.contains("VF")) {
			for (Phoneme v : vowels) {
				double v_usage = getProbability(v, vowels);
				if (syl.contains("V")) {
					double s_usage = getProbability("V", syl);
					List<Phoneme> phonemes = new ArrayList<Phoneme>();
					phonemes.add(v);
					syllables.add(new Pair(s_usage * v_usage, new Syllable(phonemes)));
				}
				if (syl.contains("VF")) {
					double s_usage = getProbability("VF", syl);
					for (Phoneme f : finals) {
						double f_usage = getProbability(f, finals);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.add(v);
						phonemes.add(f);
						syllables.add(new Pair(s_usage * v_usage * f_usage, new Syllable(phonemes)));
					}
					for (Cluster f : clu_finals) {
						double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.add(v);
						phonemes.addAll(f.getPhonemes());
						syllables.add(new Pair(s_usage * v_usage * f_usage, new Syllable(phonemes)));
					}
				}
			}
			for (Cluster v : clu_vowels) {
				double v_usage = getProbability(v, clu_vowels) * 1.0 / 6.0;
				if (syl.contains("V")) {
					double s_usage = getProbability("V", syl);
					List<Phoneme> phonemes = new ArrayList<Phoneme>();
					phonemes.addAll(v.getPhonemes());
					syllables.add(new Pair(s_usage * v_usage, new Syllable(phonemes)));
				}
				if (syl.contains("VF")) {
					double s_usage = getProbability("VF", syl);
					for (Phoneme f : finals) {
						double f_usage = getProbability(f, finals);
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.addAll(v.getPhonemes());
						phonemes.add(f);
						syllables.add(new Pair(s_usage * v_usage * f_usage, new Syllable(phonemes)));
					}
					for (Cluster f : clu_finals) {
						double f_usage = getProbability(f, clu_finals) * 1.0 / 6.0;
						List<Phoneme> phonemes = new ArrayList<Phoneme>();
						phonemes.addAll(v.getPhonemes());
						phonemes.addAll(f.getPhonemes());
						syllables.add(new Pair(s_usage * v_usage * f_usage, new Syllable(phonemes)));
					}
				}
			}
		}
		Collections.sort(syllables);
		return syllables;
	}

	private void genNameGenders() {
		if (rand.nextBoolean()) {
			if (rand.nextBoolean())
				this.male = this.genSyllable();
			this.female = this.genSyllable();
		}
	}

	public Word genDynastyName(String r, boolean save) {
		if (!this.names.containsKey(r + "d")) {
			Word w = genRoot();

			while (justify(w).transpose().length() == 1)
				w = combine(w, genSyllable());
			if (save)
				this.names.put(r + "d", justify(w));
			else
				return justify(w);
		}

		return this.names.get(r + "d");
	}

	public Word genPersonName(String r, boolean ismale, boolean save) {
		if (!this.names.containsKey(r + ismale)) {
			Word w = genRoot();

			if (ismale) {
				w = combine(w, this.male);
			} else
				w = combine(w, this.female);

			while (justify(w).transpose().length() == 1)
				w = combine(w, genSyllable());

			if (save)
				this.names.put(r + ismale, justify(w));
			else
				return justify(w);
		}

		return this.names.get(r + ismale);
	}

	private void genPluralEndings() {
		this.plural_endings.add(genSyllable());
		if (rand.nextBoolean()) {
			for (int i = 1; i < this.genders; i++) {
				this.plural_endings.add(genSyllable());
			}
		}

		if (articles.isEmpty())
			this.plural_on_article = false;
		else
			this.plural_on_article = rand.nextBoolean();
	}

	public static boolean isVowel(Phoneme p) {
		String test = p.value;
		if (test.charAt(test.length() - 1) == 'ː')
			test = test.substring(0, test.length() - 1);
		for (int i = 0; i < VOW.length; i++) {
			if (VOW[i].p.value.equals(test))
				return true;
		}
		return false;
	}

	private Word combine(Word w, Word e) {
		if (e == null || e.syllables.isEmpty()) {
			return w;
		} else if (w == null || w.syllables.isEmpty())
			return e;

		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		syllables.addAll(w.syllables);
		syllables.addAll(e.syllables);
		return new Word(syllables, ortho);
	}

	private Word combine(Word w, Syllable s) {
		if (s == null)
			return w;
		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		syllables.addAll(w.syllables);
		syllables.add(s);
		return new Word(syllables, ortho);
	}

	public Word getNoun(String word, boolean p) {
		if (!this.nouns.containsKey(word))
			genNoun(word);

		if (p)
			return this.nouns.get(word).plural;
		else
			return this.nouns.get(word).singular;
	}

	private void genNoun(String word) {
		if (!this.nouns.containsKey(word)) {
			int gen = 0;
			if (this.genders > 0)
				gen = rand.nextInt(genders);

			Word root = genRoot();

			Word ws = new Word(ortho);
			Word wp = new Word(ortho);
			ws.syllables.addAll(root.syllables);
			wp.syllables.addAll(root.syllables);
			Word as = new Word(ortho);
			Word ap = new Word(ortho);
			if (!this.articles.isEmpty()) {
				Word a = this.articles.get(gen);
				as.syllables.addAll(a.syllables);
				ap.syllables.addAll(a.syllables);
			}

			if (this.genders > 0) {
				ws.syllables.add(this.gender_endings.get(gen));
				if (!remove_gender)
					wp.syllables.add(this.gender_endings.get(gen));
			} else if (absolutive != null) {
				ws.syllables.add(absolutive);
			}

			Syllable plural = this.plural_endings.get(0);
			if (this.plural_endings.size() > 1)
				plural = this.plural_endings.get(gen);

			if (plural_on_article && ap != null) {
				if (front_plural)
					ap.syllables.add(0, plural);
				else
					ap.syllables.add(plural);
			} else {
				if (front_plural)
					wp.syllables.add(0, plural);
				else
					wp.syllables.add(plural);
			}

			if (com) {
				ws.syllables.add(new Syllable(2));
				ws.syllables.addAll(as.syllables);
				wp.syllables.add(new Syllable(2));
				wp.syllables.addAll(ap.syllables);
			} else if (!articles.isEmpty()) {
				as.syllables.add(new Syllable(1));
				as.syllables.addAll(ws.syllables);
				ws = as;
				ap.syllables.add(new Syllable(1));
				ap.syllables.addAll(wp.syllables);
				wp = ap;
			}

			ws = justify(ws);
			wp = justify(wp);

			this.nouns.put(word, new NounContainer(ws, wp, root, gen));
		}
	}

	public NounContainer getNounRaw(String word) {
		if (!this.nouns.containsKey(word))
			genNoun(word);
		return this.nouns.get(word);
	}

	private void genGenders() {
		for (int i = 0; i < this.genders; i++) {
			Syllable s = genSyllable();
			while (gender_endings.contains(s)) {
				s = genSyllable();
			}
			gender_endings.add(s);
		}
	}

	private void genArticles() {
		if (rand.nextInt(2) == 0) {
			if (this.genders != 0) {
				for (int i = 0; i < this.genders; i++) {
					articles.add(genRoot());
				}
			}
		}
	}

	private Syllable genSyllable() {
		String rand_syl = getSyllable();

		ArrayList<Phoneme> phonemes = new ArrayList<Phoneme>();

		for (int i = 0; i < rand_syl.length(); i++) {
			if (Character.toString(rand_syl.charAt(i)).equals("C")) {
				if (!consonants.isEmpty() && (rand.nextInt(6) > 0 || clu_consonants.isEmpty()))
					phonemes.add(getConsonant());
				else if (!clu_consonants.isEmpty())
					phonemes.addAll(getCluster());
			} else if (Character.toString(rand_syl.charAt(i)).equals("F")) {
				if (!finals.isEmpty() && (rand.nextInt(6) > 0 || clu_finals.isEmpty()))
					phonemes.add(getFinal());
				else if (!clu_finals.isEmpty())
					phonemes.addAll(getFinalCluster());
			} else if (Character.toString(rand_syl.charAt(i)).equals("V")) {
				if (!vowels.isEmpty() && (rand.nextInt(6) > 0 || clu_vowels.isEmpty()))
					phonemes.add(getVowel());
				else if (!clu_vowels.isEmpty())
					phonemes.addAll(getDipthong());
			}
		}

		return assertSyllable(new Syllable(phonemes));
	}

	public Word justify(Word w) {
		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		Syllable last = null;
		for (Syllable s : w.syllables) {
			Syllable news = s.clone();
			if (news.type == 0 && !news.phonemes.isEmpty()) {
				if (last != null && isVowel(last.phonemes.get(last.phonemes.size() - 1))
						&& isVowel(news.phonemes.get(0))
						&& !last.phonemes.get(last.phonemes.size() - 1).value.equals(news.phonemes.get(0).value)) {
					if (combining != null) {
						ArrayList<Phoneme> list = new ArrayList<Phoneme>();
						list.add(combining);
						list.addAll(news.phonemes);
						news = new Syllable(list);
					}
				} else if (last != null && isVowel(news.phonemes.get(0))
						&& last.phonemes.get(last.phonemes.size() - 1).value.equals(news.phonemes.get(0).value)) {
					Phoneme newp = last.phonemes.get(last.phonemes.size() - 1).clone();
					newp.value += "ː";
					last.phonemes.remove(last.phonemes.size() - 1);
					last.phonemes.add(newp);
					news.phonemes.remove(0);
					if (!news.phonemes.isEmpty()) {
						boolean flag = true;
						for (Phoneme p : news.phonemes) {
							if (isVowel(p)) {
								flag = false;
								break;
							}
						}
						if (flag) {
							last.phonemes.addAll(news.phonemes);
							news.phonemes.clear();
							if (!finals.contains(last.phonemes.get(last.phonemes.size() - 1))) {
								news.phonemes.add(last.phonemes.get(last.phonemes.size() - 1));
								Phoneme p = filler;
								if (last != null && harmony) {
									for (int i = last.phonemes.size() - 1; i >= 0; i--) {
										if (isVowel(last.phonemes.get(i))) {
											p = last.phonemes.get(i);
											break;
										}
									}
								}
								news.phonemes.add(p.clone());
								last.phonemes.remove(last.phonemes.size() - 1);
							}
						}
					}
				} else if (last != null && !isVowel(last.phonemes.get(last.phonemes.size() - 1))
						&& !last.phonemes.get(last.phonemes.size() - 1).value.equals(news.phonemes.get(0).value)
						&& !isVowel(news.phonemes.get(0)) && !canStart.contains(news.phonemes.get(0))) {
					Phoneme p = filler;
					if (last != null && harmony) {
						for (int i = last.phonemes.size() - 1; i >= 0; i--) {
							if (isVowel(last.phonemes.get(i))) {
								p = last.phonemes.get(i);
								break;
							}
						}
					}
					if (last != null && !isVowel(last.phonemes.get(last.phonemes.size() - 1))
							&& consonants.contains(last.phonemes.get(last.phonemes.size() - 1))) {
						syllables.add(new Syllable(Arrays.asList(new Phoneme[] {
								last.phonemes.get(last.phonemes.size() - 1), assertPhone(p.clone()) })));
						last.phonemes.remove(last.phonemes.size() - 1);
					} else
						syllables.add(new Syllable(Arrays.asList(new Phoneme[] { p.clone() })));
				} else if (last != null && !isVowel(news.phonemes.get(0))
						&& news.phonemes.get(0).value.equals(last.phonemes.get(last.phonemes.size() - 1).value)) {
					Phoneme newp = last.phonemes.get(last.phonemes.size() - 1).clone();
					newp.value += "ː";
					news.phonemes.remove(0);
					news.phonemes.add(0, newp);
					last.phonemes.remove(last.phonemes.size() - 1);
				} else if (last != null && !isVowel(last.phonemes.get(last.phonemes.size() - 1))
						&& isVowel(news.phonemes.get(0))
						&& consonants.contains(last.phonemes.get(last.phonemes.size() - 1))) {
					news.phonemes.add(0, last.phonemes.get(last.phonemes.size() - 1));
					last.phonemes.remove(last.phonemes.size() - 1);
				}
			}

			if (!news.phonemes.isEmpty() || news.type > 0) {
				syllables.add(news);
				if (news.type == 0)
					last = news;
				else if (news.type == 1)
					last = null;
			}
		}
		return assertWord(new Word(syllables, ortho));
	}

	public static Word assertWord(Word w) {
		List<Syllable> syllables = new ArrayList<Syllable>();
		for (int i = 0; i < w.syllables.size(); i++)
			syllables.add(assertSyllable(w.syllables.get(i)));
		return new Word(syllables, w.ortho);
	}

	public static Phoneme assertPhone(Phoneme newp) {
		for (int i = 0; i < phones.size(); i++)
			if (newp.equals(phones.get(i)))
				return phones.get(i);
		phones.add(newp);
		return newp;
	}

	public static Syllable assertSyllable(Syllable news) {
		List<Phoneme> phonemes = new ArrayList<Phoneme>();
		for (int i = 0; i < news.phonemes.size(); i++)
			phonemes.add(assertPhone(news.phonemes.get(i)));
		news = new Syllable(phonemes);
		for (int i = 0; i < formedSyllables.size(); i++)
			if (news.equals(formedSyllables.get(i)))
				return formedSyllables.get(i);
		formedSyllables.add(news);
		return news;
	}

	public static <T> double getProbability(T o, List<T> list) {
		return 1 - Math.pow(list.indexOf(o) / (double) list.size(), 1.0 / 1.25);
	}

	public Phoneme getConsonant() {
		int r = rand.nextInt(consonants.size());
		int rv = (int) Math.round(Math.pow(r / (double) consonants.size(), 1.25) * (double) consonants.size());
		return consonants.get(rv);
	}

	public List<Phoneme> getCluster() {
		int r = rand.nextInt(clu_consonants.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_consonants.size(), 1.25) * (double) clu_consonants.size());
		return clu_consonants.get(rv).getPhonemes();
	}

	public Phoneme getFinal() {
		int r = rand.nextInt(finals.size());
		int rv = (int) Math.round(Math.pow(r / (double) finals.size(), 1.25) * (double) finals.size());
		return finals.get(rv);
	}

	public List<Phoneme> getFinalCluster() {
		int r = rand.nextInt(clu_finals.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_finals.size(), 1.25) * (double) clu_finals.size());
		return clu_finals.get(rv).getPhonemes();
	}

	public List<Phoneme> getDipthong() {
		int r = rand.nextInt(clu_vowels.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_vowels.size(), 1.25) * (double) clu_vowels.size());
		return clu_vowels.get(rv).getPhonemes();
	}

	public Phoneme getVowel() {
		int r = rand.nextInt(vowels.size());
		int rv = (int) Math.round(Math.pow(r / (double) vowels.size(), 1.25) * (double) vowels.size());
		return vowels.get(rv);
	}

	public String getSyllable() {
		int r = rand.nextInt(syl.size());
		int rv = (int) Math.round(Math.pow(r / (double) syl.size(), 1.25) * (double) syl.size());
		return syl.get(rv);
	}

	public Word genRoot() {
		ArrayList<Syllable> s = new ArrayList<Syllable>();

		for (int i = 0; i < rand.nextInt((this.max_length - this.min_length) + 1) + this.min_length; i++) {
			s.add(genSyllable());
		}

		return new Word(s, ortho);
	}

	public String getName() {
		return name.transpose();
	}
}
