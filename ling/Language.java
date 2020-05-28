package ling;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

class Pair {
	double usage;
	Phoneme p;

	public Pair(double usage, String p) {
		this.p = new Phoneme(p);
		this.usage = usage;
	}

	public String toString() {
		return p.toString();
	}
}

class Cluster {
	ArrayList<Pair> p;

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

	public ArrayList<Phoneme> getPhonemes() {
		ArrayList<Phoneme> phonemes = new ArrayList<Phoneme>();
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

	public boolean applicable(ArrayList<Phoneme> total) {
		for (Pair a : p) {
			if (!total.contains(a.p))
				return false;
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
			new Pair(0.12, "kʷ"), new Pair(0.10, "dz"), new Pair(0.06, "gʷ"), new Pair(0.05, "ð"), new Pair(0.03, "kʲ"),
			new Pair(0.03, "tʲ"), new Pair(0.03, "gʲ"), new Pair(0.03, "xʷ"), new Pair(0.02, "mʲ"),
			new Pair(0.02, "lʲ"), new Pair(0.02, "pʲ"), new Pair(0.02, "mʷ"), new Pair(0.02, "hʷ"),
			new Pair(0.02, "dʲ"), new Pair(0.02, "bʲ"), new Pair(0.02, "bʷ"), new Pair(0.02, "sʲ"),
			new Pair(0.02, "pʷ"), new Pair(0.02, "tʷ"), new Pair(0.01, "fʷ"), new Pair(0.01, "fʲ"),
			new Pair(0.01, "sʷ"), new Pair(0.01, "rʲ"), new Pair(0.01, "hʲ"), new Pair(0.01, "ʃʷ"),
			new Pair(0.01, "nʷ"), new Pair(0.01, "vʲ"), new Pair(0.01, "zʲ"), new Pair(0.01, "lʷ"),
			new Pair(0.01, "zʷ"), new Pair(0.01, "ʃʲ"), new Pair(0.01, "xʲ"), new Pair(0.01, "ʒʷ") };

	public static final Pair FIN[] = { new Pair(0.90, "m"), new Pair(0.70, "k"),
			// new Pair(0.90, "j"),
			new Pair(0.20, "p"),
			// new Pair(0.82, "w"),
			new Pair(0.96, "n"), new Pair(0.86, "t"), new Pair(0.68, "l"), new Pair(0.86, "s"), new Pair(0.20, "b"),
			new Pair(0.63, "ŋ"), new Pair(0.20, "g"),
			// new Pair(0.56, "h"),
			new Pair(0.70, "d"), new Pair(0.86, "r"), new Pair(0.44, "f"),
			// new Pair(0.42, "ɲ"),
			new Pair(0.50, "tʃ"),
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
			new Cluster(new String[] { "a", "e" }, VOW), new Cluster(new String[] { "e", "o" }, VOW),
			new Cluster(new String[] { "a", "u" }, VOW), new Cluster(new String[] { "e", "u" }, VOW),
			new Cluster(new String[] { "o", "u" }, VOW), new Cluster(new String[] { "i", "u" }, VOW),
			new Cluster(new String[] { "i", "a" }, VOW), new Cluster(new String[] { "i", "o" }, VOW),
			new Cluster(new String[] { "i", "e" }, VOW), new Cluster(new String[] { "e", "a" }, VOW),
			new Cluster(new String[] { "u", "a" }, VOW), new Cluster(new String[] { "u", "e" }, VOW),
			new Cluster(new String[] { "u", "i" }, VOW), new Cluster(new String[] { "u", "o" }, VOW) };

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
			new Cluster(new String[] { "n", "d" }, FIN), new Cluster(new String[] { "n", "dz" }, FIN),
			new Cluster(new String[] { "n", "dʒ" }, FIN), new Cluster(new String[] { "r", "s" }, FIN),
			new Cluster(new String[] { "r", "p" }, FIN), new Cluster(new String[] { "r", "n" }, FIN),
			new Cluster(new String[] { "r", "n", "z" }, FIN), new Cluster(new String[] { "r", "n", "t" }, FIN),
			new Cluster(new String[] { "r", "n", "ts" }, FIN), new Cluster(new String[] { "r", "m" }, FIN),
			new Cluster(new String[] { "r", "m", "z" }, FIN), new Cluster(new String[] { "r", "b" }, FIN),
			new Cluster(new String[] { "r", "k" }, FIN), new Cluster(new String[] { "r", "z" }, FIN),
			new Cluster(new String[] { "r", "tʃ" }, FIN), new Cluster(new String[] { "r", "s", "t" }, FIN),
			new Cluster(new String[] { "r", "t" }, FIN), new Cluster(new String[] { "r", "d" }, FIN),
			new Cluster(new String[] { "r", "dz" }, FIN), new Cluster(new String[] { "r", "dʒ" }, FIN),
			new Cluster(new String[] { "r", "f" }, FIN), new Cluster(new String[] { "l", "s" }, FIN),
			new Cluster(new String[] { "l", "n" }, FIN), new Cluster(new String[] { "l", "n", "z" }, FIN),
			new Cluster(new String[] { "l", "m" }, FIN), new Cluster(new String[] { "l", "m", "z" }, FIN),
			new Cluster(new String[] { "l", "p" }, FIN), new Cluster(new String[] { "l", "b" }, FIN),
			new Cluster(new String[] { "l", "k" }, FIN), new Cluster(new String[] { "l", "z" }, FIN),
			new Cluster(new String[] { "l", "tʃ" }, FIN), new Cluster(new String[] { "l", "s", "t" }, FIN),
			new Cluster(new String[] { "l", "t" }, FIN), new Cluster(new String[] { "l", "d" }, FIN),
			new Cluster(new String[] { "l", "dz" }, FIN), new Cluster(new String[] { "l", "dʒ" }, FIN),
			new Cluster(new String[] { "l", "f" }, FIN), new Cluster(new String[] { "ʃ", "t" }, FIN),
			new Cluster(new String[] { "ʃ", "k" }, FIN), new Cluster(new String[] { "ʃ", "p" }, FIN),
			new Cluster(new String[] { "p", "t" }, FIN), new Cluster(new String[] { "p", "s" }, FIN),
			new Cluster(new String[] { "k", "t" }, FIN), new Cluster(new String[] { "k", "s" }, FIN),
			new Cluster(new String[] { "g", "z" }, FIN) };

	public static final String[] SYL = { "CV", "CVF", "VF", "V" };

	public ArrayList<Phoneme> consonants = new ArrayList<Phoneme>();
	public ArrayList<Phoneme> finals = new ArrayList<Phoneme>();
	public ArrayList<Cluster> clu_consonants = new ArrayList<Cluster>();
	public ArrayList<Cluster> clu_finals = new ArrayList<Cluster>();
	public ArrayList<Cluster> clu_vowels = new ArrayList<Cluster>();
	public ArrayList<Phoneme> vowels = new ArrayList<Phoneme>();
	public ArrayList<String> syl = new ArrayList<String>();
	public Color c;

	private boolean plural_on_article, com;
	private boolean remove_gender;

	int max_length, min_length, genders;

	ArrayList<Syllable> gender_endings = new ArrayList<Syllable>();
	ArrayList<Syllable> plural_endings = new ArrayList<Syllable>();
	ArrayList<Word> articles = new ArrayList<Word>();
	HashMap<String, Word> pronouns = new HashMap<String, Word>();

	HashMap<String, Word> nouns = new HashMap<String, Word>();
	HashMap<Word, Integer> gen = new HashMap<Word, Integer>();
	HashMap<String, Word> places = new HashMap<String, Word>();

	HashMap<String, Word> names = new HashMap<String, Word>();

	Syllable male, female;
	Phoneme combining = null;

	Word name;

	public static Random rand;

	public Language(Random rand) {
		Language.rand = rand;

		syl.addAll(Arrays.asList(SYL));
		Collections.shuffle(syl, new Random(rand.nextInt()));
		int r_s = rand.nextInt(syl.size() - 1);

		for (int i = 0; i < r_s; i++) {
			syl.remove(0);
		}

		for (Pair p : CON) {
			if (rand.nextDouble() < p.usage)
				consonants.add(p.p);
		}
		for (Pair p : FIN) {
			if (rand.nextDouble() < Math.pow(p.usage, 2))
				finals.add(p.p);
		}
		for (Pair p : VOW) {
			if (rand.nextDouble() < p.usage)
				vowels.add(p.p);
		}

		/*
		 * ArrayList<Phoneme> total = new ArrayList<Phoneme>();
		 * total.addAll(consonants); total.addAll(finals);
		 */

		if (rand.nextBoolean()) {
			for (Cluster c : CLU_CON) {
				if (c.applicable(consonants) && rand.nextDouble() < c.getProbability())
					clu_consonants.add(c);
			}
		}
		if (rand.nextBoolean()) {
			for (Cluster c : CLU_FIN) {
				if (c.applicable(finals) && rand.nextDouble() < c.getProbability())
					clu_finals.add(c);
			}
		}
		for (Cluster v : CLU_VOW) {
			if (v.applicable(vowels) && rand.nextDouble() < v.getProbability())
				clu_vowels.add(v);
		}

		this.combining = getConsonant();

		Collections.shuffle(consonants);
		Collections.shuffle(finals);
		Collections.shuffle(vowels);
		Collections.shuffle(clu_consonants);
		Collections.shuffle(clu_finals);
		Collections.shuffle(clu_vowels);

		this.c = Color.getHSBColor((float) Math.random(), 1f, 1f);

		this.min_length = rand.nextInt(2) + 1;
		this.max_length = rand.nextInt(3) + min_length + 1;

		this.genders = rand.nextInt(3) * 2;

		this.com = rand.nextBoolean();
		this.remove_gender = rand.nextBoolean();

		genGenders();
		genArticles();
		genPronouns();
		genPluralEndings();
		genNameGenders();

		name = genNoun("language", false);

		System.out.println("CONS\t" + consonants);
		System.out.println("VOWS\t" + vowels);
		System.out.println("FINS\t" + finals);
		System.out.println("CLUC\t" + clu_consonants);
		System.out.println("CLUF\t" + clu_finals);
		System.out.println("CLUV\t" + clu_vowels);
		System.out.println("SYLL\t" + syl);
		System.out.println("COMB\t" + combining);
		System.out.println("Max Length: " + (this.max_length));
		System.out.println("Min Length: " + (this.min_length));
		System.out.println("Masc Name: " + male);
		System.out.println("Fem Name: " + female);
		System.out.println("Noun Genders: " + (this.genders));
		System.out.println(gender_endings);
		System.out.println("Articles: " + articles);
		System.out.println("Pronouns: " + pronouns);
		System.out.println("Plurality: " + plural_endings);
		System.out.println("------------------------------");
	}

	public void genNameGenders() {
		if (rand.nextBoolean()) {
			if (rand.nextBoolean())
				this.male = this.genSyllable();
			this.female = this.genSyllable();
		}
	}

	public Word genDynastyName(String w) {
		Word noun;

		if (!this.names.containsKey(w + "d")) {
			noun = genRoot();

			while (noun.transpose().length() == 1)
				noun = combine(noun, genSyllable());

			this.names.put(w + "d", noun);
			// this.gen.put(noun, gen);
		} else {
			noun = this.names.get(w + "d");
			// gen = this.gen.get(noun);
		}

		return noun;
	}

	public Word genPersonName(String w, boolean ismale) {
		Word noun;

		if (!this.names.containsKey(w + ismale)) {
			noun = genRoot();

			if (ismale) {
				noun = combine(noun, this.male);
			} else
				noun = combine(noun, this.female);

			while (noun.transpose().length() == 1)
				noun = combine(noun, genSyllable());

			this.names.put(w + ismale, noun);
		} else {
			noun = this.names.get(w + ismale);
		}

		return noun;
	}

	public void genPronouns() {
		pronouns.put("1S", genRoot());
		pronouns.put("2S", genRoot());

		if (this.genders != 0) {
			for (int i = 0; i < this.genders; i++) {
				pronouns.put("G" + i + "S", genRoot());
			}
		} else {
			pronouns.put("G0S", genRoot());
		}
	}

	public Word genName(String w, String l) {
		Word n;
		if (!this.places.containsKey(w + l)) {
			n = genNoun(l, false);
			while (n.transpose().length() == 1)
				n = combine(n, genSyllable());
			this.places.put(w + l, n);
		} else
			n = this.places.get(w + l);

		return n;
	}

	public void genPluralEndings() {
		if (rand.nextInt(2) == 0 && this.articles.size() != 0) {
			for (int i = 0; i < this.articles.size(); i++) {
				this.plural_endings.add(genSyllable());
			}
			this.plural_on_article = true;
		} else {
			if (this.genders == 0) {
				this.plural_endings.add(genSyllable());
			} else {
				for (int i = 0; i < this.genders; i++) {
					this.plural_endings.add(genSyllable());
				}
			}
			this.plural_on_article = false;
		}
	}

	public boolean isVowel(Phoneme p) {
		for (int i = 0; i < VOW.length; i++) {
			if (VOW[i].p.value == p.value)
				return true;
		}
		return false;
	}

	public Word combine(Word w, Word e) {
		if (e == null || e.syllables.isEmpty()) {
			return w;
		} else if (w == null || w.syllables.isEmpty())
			return e;

		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		syllables.addAll(w.syllables);
		syllables.addAll(e.syllables);
		return justify(new Word(syllables));
	}

	public Word combine(Word w, Syllable s) {
		if (s == null)
			return w;
		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		syllables.addAll(w.syllables);
		syllables.add(s);
		return justify(new Word(syllables));
	}

	public Word genNoun(String word, boolean p) {
		Word noun;
		Word a;
		Syllable pl;
		int gen;

		if (!this.nouns.containsKey(word)) {
			if (this.genders != 0)
				gen = rand.nextInt(genders);
			else
				gen = 0;

			if (this.genders != 0)
				noun = combine(genRoot(), gender_endings.get(gen));
			else
				noun = genRoot();

			this.nouns.put(word, noun);
			this.gen.put(noun, gen);
		} else {
			noun = this.nouns.get(word);
			gen = this.gen.get(noun);
		}

		if (!this.articles.isEmpty()) {
			a = this.articles.get(gen);
		} else
			a = null;

		if (p) {
			if (remove_gender && this.genders > 0)
				noun.syllables.remove(noun.syllables.size() - 1);
			if (this.plural_endings.size() > 0)
				pl = this.plural_endings.get(gen);
			else
				pl = this.plural_endings.get(0);
		} else
			pl = null;

		if (!com) {
			if (this.plural_on_article)
				return combine(combine(combine(a, pl), new Syllable(1)), noun);
			else {
				if (a != null)
					return combine(combine(a, new Syllable(1)), combine(noun, pl));
				else
					return combine(noun, pl);
			}
		} else {
			if (this.plural_on_article)
				return combine(combine(noun, new Syllable(2)), combine(a, pl));
			else {
				if (a != null)
					return combine(combine(combine(noun, pl), new Syllable(2)), a);
				else
					return combine(noun, pl);
			}
		}
	}

	public void genGenders() {
		for (int i = 0; i < this.genders; i++) {
			Syllable s = genSyllable();
			while (gender_endings.contains(s)) {
				s = genSyllable();
			}
			gender_endings.add(s);
		}
	}

	public void genArticles() {
		if (rand.nextInt(2) == 0) {
			if (this.genders != 0) {
				for (int i = 0; i < this.genders; i++) {
					articles.add(genRoot());
				}
			}
		}
	}

	public Syllable genSyllable() {
		String rand_syl = getSyllable();

		ArrayList<Phoneme> phonemes = new ArrayList<Phoneme>();

		for (int i = 0; i < rand_syl.length(); i++) {
			if (Character.toString(rand_syl.charAt(i)).equals("C")) {
				if (rand.nextInt(6) > 0 || clu_consonants.isEmpty())
					phonemes.add(getConsonant());
				else
					phonemes.addAll(getCluster());
			} else if (Character.toString(rand_syl.charAt(i)).equals("F")) {
				if (rand.nextInt(6) > 0 || clu_finals.isEmpty())
					phonemes.add(getFinal());
				else
					phonemes.addAll(getFinalCluster());
			} else if (Character.toString(rand_syl.charAt(i)).equals("V")) {
				if (rand.nextInt(6) > 0 || clu_vowels.isEmpty())
					phonemes.add(getVowel());
				else
					phonemes.addAll(getDipthong());
			} else {
				phonemes.add(new Phoneme(String.valueOf(rand_syl.charAt(i))));
			}
		}

		return new Syllable(phonemes);
	}

	public Word justify(Word w) {
		ArrayList<Syllable> syllables = new ArrayList<Syllable>();
		Syllable last = null;
		for (Syllable s : w.syllables) {
			Syllable news = s;
			if (last != null && s.type == 0 && last.type == 0) {
				if (isVowel(last.phonemes.get(last.phonemes.size() - 1)) && isVowel(s.phonemes.get(0))) {
					ArrayList<Phoneme> list = new ArrayList<Phoneme>();
					list.add(combining);
					list.addAll(s.phonemes);
					news = new Syllable(list);
				}
			}
			syllables.add(news);
			if (news.type == 0)
				last = news;
		}
		return new Word(syllables);
	}

	public Phoneme getConsonant() {
		int r = rand.nextInt(consonants.size());
		int rv = (int) Math.round(Math.pow(r / (double) consonants.size(), 1.5) * (double) consonants.size());
		return consonants.get(rv);
	}

	public ArrayList<Phoneme> getCluster() {
		int r = rand.nextInt(clu_consonants.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_consonants.size(), 1.5) * (double) clu_consonants.size());
		return clu_consonants.get(rv).getPhonemes();
	}

	public Phoneme getFinal() {
		int r = rand.nextInt(finals.size());
		int rv = (int) Math.round(Math.pow(r / (double) finals.size(), 1.5) * (double) finals.size());
		return finals.get(rv);
	}

	public ArrayList<Phoneme> getFinalCluster() {
		int r = rand.nextInt(clu_finals.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_finals.size(), 1.5) * (double) clu_finals.size());
		return clu_finals.get(rv).getPhonemes();
	}

	public ArrayList<Phoneme> getDipthong() {
		int r = rand.nextInt(clu_vowels.size());
		int rv = (int) Math.round(Math.pow(r / (double) clu_vowels.size(), 1.5) * (double) clu_vowels.size());
		return clu_vowels.get(rv).getPhonemes();
	}

	public Phoneme getVowel() {
		int r = rand.nextInt(vowels.size());
		// int rand = (int) (Math.pow(r, 1.5) / (double)vowels.size());
		return vowels.get(r);
	}

	public String getSyllable() {
		int r = rand.nextInt(syl.size());
		int rv = (int) Math.round(Math.pow(r / (double) syl.size(), 1.5) * (double) syl.size());
		return syl.get(rv);
	}

	public Word genRoot() {
		ArrayList<Syllable> s = new ArrayList<Syllable>();

		for (int i = 0; i < rand.nextInt(this.max_length - this.min_length) + this.min_length; i++) {
			s.add(genSyllable());
		}

		return justify(new Word(s));
	}

	public String getName() {
		return name.transpose();
	}
}
