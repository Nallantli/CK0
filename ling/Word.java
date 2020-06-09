package ling;

import java.util.ArrayList;
import java.util.List;

public class Word {
	List<Syllable> syllables;
	String cached = null;
	Orthography ortho;

	public Word(Orthography ortho) {
		this.syllables = new ArrayList<Syllable>();
		this.ortho = ortho;
	}

	public Word(List<Syllable> syllables, Orthography ortho) {
		this.syllables = syllables;
		this.ortho = ortho;
	}

	public String transpose() {
		if (cached == null) {
			String s = "";
			for (Syllable o : syllables) {
				String temp = o.toString();
				s += temp;
			}
			s = ortho.apply(s);

			String split[] = s.split(" ");
			cached = "";
			for (int i = 0; i < split.length; i++) {
				String temp = split[i];
				if (temp.charAt(0) == '\'')
					cached += " " + temp.substring(0, 2).toUpperCase() + temp.substring(2);
				else
					cached += " " + temp.substring(0, 1).toUpperCase() + temp.substring(1);
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