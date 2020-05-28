package sim;

import java.util.ArrayList;
import java.util.List;

import ling.Word;
import main.Main;
import ling.Language;

public class Dynasty {
	Word name;
	private List<Person> members;
	private Timeline time;

	public boolean original = false;
	// Person origin;

	public Dynasty(Language lang, Timeline time) {
		members = new ArrayList<Person>();
		name = lang.genDynastyName(String.valueOf(Main.rand.nextLong()));
		this.time = time;
		time.dynasty_count++;
	}

	public Person getHighestMember() {
		Person heighest = null;
		for (int i = 0; i < members.size(); i++) {
			Person p = members.get(i);
			if (p.isDead())
				continue;

			if (heighest == null)
				heighest = p;
			else if (heighest.main == null && p.main == null)
				heighest = (heighest.age > p.age ? heighest : p);
			else if (heighest.main == null && p.main != null)
				heighest = p;
			else if (heighest.main != null && p.main != null) {
				if (heighest.main.level == p.main.level)
					heighest = (heighest.age > p.age ? heighest : p);
				else
					heighest = (heighest.main.level > p.main.level ? heighest : p);
			}
		}
		return heighest;
	}

	public void remove(Person p) {
		members.remove(p);
		if (members.isEmpty()) {
			time.dynasty_count--;
			if (original)
				time.original_dynasties--;
		}
	}

	public void add(Person p) {
		members.add(p);
	}

	public int getSize() {
		return members.size();
	}

	public Person getMemberAt(int i) {
		return members.get(i);
	}

	public String toString() {
		return name.transpose();
	}
}