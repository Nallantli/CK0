package ling;

import java.util.ArrayList;
import java.util.List;

public class Syllable {
	List<Phoneme> phonemes;
	int type;

	public Syllable(List<Phoneme> list) {
		this.phonemes = list;
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

	public int moraicValue() {
		int value = 0;
		boolean flag = false;
		for (int i = 0; i < phonemes.size(); i++) {
			if (Language.isVowel(phonemes.get(i)))
				flag = true;
			
			if (flag) {
				if (phonemes.get(i).value.charAt(phonemes.get(i).value.length() - 1) == 'ː') {
					value+=2;
				} else {
					value++;
				}
			}
		}
		return value;
	}

	@Override
	public boolean equals(Object o) {		
		if (o == this)
			return true;
		Syllable s = (Syllable) o;
		if (s.phonemes.size() != phonemes.size())
			return false;
		if (s.type != type)
			return false;
		for (int i = 0; i < phonemes.size(); i++) {
			if (!s.phonemes.get(i).equals(phonemes.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public Syllable clone() {
		if (type == 0) {
			List<Phoneme> ph = new ArrayList<Phoneme>();
			for (int i = 0; i < phonemes.size(); i++)
				ph.add(phonemes.get(i).clone());
			return new Syllable(ph);
		}
		else
			return new Syllable(type);
	}
}