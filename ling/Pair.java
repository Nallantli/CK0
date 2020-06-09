package ling;

public class Pair implements Comparable<Pair> {
	int type;
	public double usage;
	public Phoneme p;
	public Syllable s;

	public Pair(double usage, String p) {
		this.type = 0;
		this.p = new Phoneme(p);
		this.usage = usage;
	}

	public Pair(double usage, Syllable s) {
		this.type = 1;
		this.s = s;
		this.usage = usage;
	}

	public String toString() {
		switch (type) {
			case 0:
				return p.toString();
			case 1:
				return s.toString();
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		Pair p = (Pair) o;
		if (type != p.type)
			return false;
		switch (type) {
			case 0:
				return p.usage == this.usage && p.p.equals(this.p);
			case 1:
				return p.usage == this.usage && p.s.equals(this.s);
		}
		return false;
	}

	@Override
	public int compareTo(Pair o) {
		return Double.compare(usage, o.usage);
	}
}