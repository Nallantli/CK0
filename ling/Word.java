package ling;

import java.util.ArrayList;

public class Word {
	ArrayList<Syllable> syllables;
	String cached = null;

	public Word(ArrayList<Syllable> syllables) {
		this.syllables = syllables;
	}

	public String transpose() {
		if (cached == null) {
			String s = "";
			for (Syllable o : syllables) {
				String temp = o.toString();
				s += temp;
			}
			s = s.replace("ka", "ca");
			s = s.replace("ko", "co");
			s = s.replace("ku", "cu");
			s = s.replace("ð", "th");
			s = s.replace("kʷ", "qu");
			s = s.replace("hʲ", "ç");
			s = s.replace("ŋ", "ng");
			s = s.replace("x", "ħ");
			s = s.replace("ʔ", "q");
			s = s.replace("ɲ", "ń");
			s = s.replace("j", "y");
			s = s.replace("dʒ", "j");
			s = s.replace("ʒ", "ź");
			s = s.replace("ks", "x");
			s = s.replace("tʃ", "ć");
			s = s.replace("ʃ", "ś");
			s = s.replace("tś", "ć");
			s = s.replace("ʷ", "w");
			s = s.replace("ʲ", "y");

			String split[] = s.split(" ");
			cached = "";
			for (int i = 0; i < split.length; i++) {
					cached += " " + split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
			}
			cached = cached.substring(1);
		}

		return cached;
	}

	public String toString() {
		String s = "";
		for (Syllable o : syllables)
			s += o.toString() + ".";
		return s.substring(0, s.length() - 1);
	}
}