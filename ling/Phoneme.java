package ling;

public class Phoneme {
	public String value;

	public Phoneme(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		Phoneme p = (Phoneme) o;
		return p.value.equals(this.value);
	}

	@Override
	public Phoneme clone() {
		return new Phoneme(value);
	}
}