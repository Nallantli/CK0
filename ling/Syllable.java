package ling;

import java.util.ArrayList;

public class Syllable {
	ArrayList<Phoneme> phonemes;
	int type;

	public Syllable(ArrayList<Phoneme> phonemes) {
		this.phonemes = phonemes;
		type = 0;
	}

	public Syllable(int type) {
		phonemes = new ArrayList<Phoneme>();
		this.type = type;
	}

	public String toString() {
		switch (type) {
			case 1:
				return " ";
			case 2:
				return "-";
		}
		String s = "";
		for (Phoneme p : phonemes)
			s += p.toString();
		return s;
	}
}