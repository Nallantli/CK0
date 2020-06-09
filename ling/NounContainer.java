package ling;

public class NounContainer {
	Word singular;
	Word plural;
	Word root;
	int gender;

	public NounContainer(Word singular, Word plural, Word root, int gender) {
		this.singular = singular;
		this.plural = plural;
		this.root = root;
		this.gender = gender;
	}

	public void print() {
		System.out.println("R\t/" + root + "/");
		System.out.println("S\t[" + singular + "]");
		System.out.println("P\t[" + plural + "]");
		System.out.println("G\t" + gender);
	}
}