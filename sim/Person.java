package sim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ling.Language;
import ling.Word;
import main.Display;
import main.Main;

public class Person {
	public List<Title> demesne;
	public List<Title> duchies;
	public List<Title> kingdoms;
	public List<Title> empires;
	private List<Title> claims;
	public ArrayList<War> wars;
	private List<Person> sub;
	private List<Person> court;
	public List<Person> children;
	private List<String> history;
	public HashMap<Person, Integer> truces;
	private Person res;
	int max_c = 12;
	int max_d = 6;
	int max_k = 9999;
	public int levy;
	public int id;
	public Title main = null;
	Timeline time;
	private Person lord, spouse;
	public Title capital = null;
	float age;
	private boolean dead;
	boolean ismale;
	public Language lang;

	public int gestation = 0;
	public int intelligence;
	public int move = 0;
	public int coup = 0;

	Word name;
	public Dynasty dynasty;

	private Area cache = null;
	public boolean update_area = true;

	public ArrayList<Person> forefathers = new ArrayList<Person>();
	private String roman_numeral = "";

	public Person(Dynasty dynasty, Timeline time, boolean ismale, Language lang, float age, int intelligence,
			ArrayList<Person> pn) {
		if (dynasty == null) {
			this.dynasty = new Dynasty(lang, time);
			// this.dynasty.origin = this;
		} else {
			this.dynasty = dynasty;
		}
		this.dynasty.add(this);

		this.time = time;
		this.ismale = ismale;
		this.age = age;
		this.intelligence = intelligence;
		this.name = lang.genPersonName(String.valueOf(time.id), ismale);

		this.lang = lang;
		this.demesne = new ArrayList<Title>();
		this.duchies = new ArrayList<Title>();
		this.kingdoms = new ArrayList<Title>();
		this.empires = new ArrayList<Title>();
		this.claims = new ArrayList<Title>();
		this.sub = new ArrayList<Person>();
		this.court = new ArrayList<Person>();
		this.children = new ArrayList<Person>();
		this.wars = new ArrayList<War>();
		this.truces = new HashMap<Person, Integer>();

		this.history = new ArrayList<String>();

		if (ismale) {
			this.forefathers.addAll(pn);
			this.forefathers.add(this);
			int sum = 0;
			for (int i = 0; i < this.forefathers.size(); i++) {
				if (this.forefathers.get(i).name.transpose().equals(name.transpose()))
					sum++;
			}

			if (sum > 1)
				roman_numeral = " " + Main.convertToRN(sum);
		}

		time.to_be_added.add(this);

		this.id = time.id;
		time.id++;

		time.person_registry.put(this.id, this);

		time.births++;

		if (Main.COOL_NAMES.contains(name.transpose())) {
			System.out.println("\033[0;34mBehold ye mortals, God has given us " + this.getName() + ", a "
					+ (ismale ? "man" : "woman") + " destined for greatness!\033[0m");
		}
	}

	public boolean isActive() {
		if (dead)
			return false;

		if (this.spouse != null && !this.spouse.dead)
			return true;
		else if (!this.sub.isEmpty())
			return true;
		else if (this.hasTerritory())
			return true;
		else if (this.res != null)
			return true;
		else if (this.lord != null)
			return true;

		return false;
	}

	public void die(boolean culled) {
		if (!dead)
			setDead(culled);

		int amt = this.demesne.size();
		for (int i = 0; i < amt; i++) {
			Title t = this.getLeastImportantCounty();

			Person p = this.getFromCourt();

			time.addDemesne(p, t, false);
			p.main = t;
			removeCourt(p);
			p.lord = this;
			p.res = null;
			sub.add(p);
		}

		if (!this.sub.isEmpty())
			time.releaseSubs(this);

		if (this.hasTerritory()) {
			while (!this.duchies.isEmpty())
				time.removeDuchy(this, this.duchies.get(0));

			while (!this.kingdoms.isEmpty())
				time.removeKingdom(this, this.kingdoms.get(0));

			while (!this.empires.isEmpty())
				time.removeEmpire(this, this.empires.get(0));
		}

		if (lord != null)
			lord.removeSub(this);
		if (res != null)
			res.removeCourt(this);

		this.main = null;
		this.capital = null;

		time.setForRemove(this);
		this.dynasty.remove(this);
	}

	public boolean hasTerritory() {
		if (!this.empires.isEmpty())
			return true;
		else if (!this.kingdoms.isEmpty())
			return true;
		else if (!this.duchies.isEmpty())
			return true;
		else if (!this.demesne.isEmpty())
			return true;
		else
			return false;
	}

	int last_c, last_d, last_k, last_e;

	public List<Title> getAllTitles() {
		List<Title> titles = new ArrayList<Title>();

		titles.addAll(this.demesne);
		titles.addAll(this.getAllSubDemesne());
		titles.addAll(this.duchies);
		titles.addAll(this.getSubDuchies());
		titles.addAll(this.kingdoms);
		titles.addAll(this.getSubKingdoms());
		titles.addAll(this.empires);

		return titles;
	}

	public boolean canExpand() {
		if (this.demesne.size() > this.max_c && duchies.isEmpty())
			return false;
		else
			return true;
	}

	public void recalculateMain() {
		move = 240;
		if (!this.hasTerritory())
			return;
		// if (this.capital == null || (this.capital != null && this.capital.owner !=
		// this))
		this.capital = getBestCounty();
		this.main = this.getBestTitle();
	}

	public Title getBestCounty() {
		Title best = null;
		for (int i = 0; i < demesne.size(); i++) {
			Title t = demesne.get(i);
			if (best == null || t.development > best.development)
				best = t;
		}
		return best;
	}

	public void updateDemesne() {
		if (this.lord != null) {
			if (this.lord.dead) {
				Person curr = lord;
				while (curr != null) {
					if (!curr.dead)
						break;
					curr = curr.getLord();
				}
				setLord(curr);
			}
		}

		if (this.lord != null) {
			if (this.lord.main != null) {
				this.CheckDemesne();
			} else {
				System.err.println(this.id + " - Lord is not fully initialized - Assuming Inactive");
				this.lord.removeSub(this);
			}
		} else
			this.CheckDemesne();

		if (move == 0)
			recalculateMain();

		List<Person> to_be_pruned = new ArrayList<Person>();
		for (int i = 0; i < sub.size(); i++) {
			Person s = sub.get(i);
			if (s.main != null && main != null) {
				if (s.lord != this && s.hasTerritory())
					to_be_pruned.add(s);
				if (s.main.level >= main.level && s.lord == this) {
					to_be_pruned.add(s);
				}
			}
		}

		for (int i = 0; i < to_be_pruned.size(); i++)
			to_be_pruned.get(i).setLord(lord);

		this.CheckSubs();

		if (capital != null && main != null)
			capital.development = (float) Math.max(0,
					Math.min(1000, capital.development + (0.08 * main.level * ((float) intelligence / 20f - 0.25f))));

		/*
		 * if (!demesne.isEmpty() && capital != null) this.develop();
		 */
	}

	/*
	 * public void develop() { for (int i = 0; i < demesne.size(); i++) { Title t =
	 * demesne.get(i); if (!t.occupied) { double dist = Math.max(0, ((100 -
	 * t.getCenter().distance(capital.getCenter())) / 2000) * (1 + main.level));
	 * t.development = (float) Math.min(1000, t.development + dist); } } }
	 */

	public void updateLevy() {
		if (this.levy < this.getMaxLevy(this, null))
			this.levy = (int) (this.levy + (this.getMaxLevy(this, null) / 12.0f));

		if (this.levy > this.getMaxLevy(this, null))
			this.levy = this.getMaxLevy(this, null);
	}

	public int getMaxLevy(Person o, Person past) {
		int sub_l = 0;
		int main_l = 0;
		if (main != null) {
			switch (main.level) {
				case 1:
					main_l = 1000;
					break;
				case 2:
					main_l = 2000;
					break;
				case 3:
					main_l = 4000;
					break;
			}
		}

		for (int i = 0; i < sub.size(); i++) {
			Person s = sub.get(i);
			if (s == o) {
				if (s.lord == null) {
					if (past != null) {
						past.removeSub(s);
						continue;
					}
					System.err.println("ERR: That's not supposed to happen!");
					System.exit(1);
				}
				System.err.println("ERR: That's not supposed to happen!");
				System.exit(1);
			}
			sub_l += (int) (s.getMaxLevy(o, this) * 0.5f);
		}

		float dev_l = 0;
		for (int i = 0; i < demesne.size(); i++) {
			dev_l += demesne.get(i).development;
		}

		return (int) ((dev_l + sub_l + main_l) / (age < 16 ? 2 : 1));
	}

	/*
	 * private void PruneDuchies() { for (int i = 0; i < this.duchies.size(); i++) {
	 * if (!this.stillOwnsDuchy(this.duchies.get(i))) {
	 * System.out.println("Pruning Duchy..."); time.removeDuchy(this,
	 * this.duchies.get(i)); } } }
	 * 
	 * private void PruneKingdoms() { for (int i = 0; i < this.kingdoms.size(); i++)
	 * { if (!this.stillOwnsKingdom(this.kingdoms.get(i))) {
	 * System.out.println("Pruning Kingdom..."); time.removeKingdom(this,
	 * this.kingdoms.get(i)); } } }
	 */
	public void addClaim(Title t) {
		if (claims.contains(t))
			return;
		addHistory(Main.CCOLOR.GREEN, getName() + " claims " + t.toString());
		claims.add(t);
	}

	public void removeClaim(Title t) {
		claims.remove(t);
	}

	public List<Title> getClaims() {
		return claims;
	}

	public void setClaims(List<Title> new_claims) {
		claims = new_claims;
	}

	/*
	 * private boolean stillOwnsDuchy(Title d) { List<Title> titles =
	 * getDuchyDemesneWithSub(d);
	 * 
	 * if (titles.isEmpty()) return false; else return true; }
	 * 
	 * private boolean stillOwnsKingdom(Title d) { List<Title> titles =
	 * getKingdomDemesneWithSub(d);
	 * 
	 * if (titles.isEmpty()) return false; else return true; }
	 */

	private void CheckSubs() {
		List<Person> to_be_removed = new ArrayList<Person>();

		for (int i = 0; i < this.sub.size(); i++) {
			Person s = sub.get(i);
			if (s.demesne.isEmpty()) {
				// System.out.println("Retiring Sub...");
				to_be_removed.add(s);
				time.releaseSubs(s);

				time.addToCourt(s, this);
			}
		}

		for (int i = 0; i < to_be_removed.size(); i++) {
			this.removeSub(to_be_removed.get(i));
		}
	}

	private void CheckDemesne() {
		if (demesne.isEmpty()) {
			time.releaseSubs(this);

			while (!this.duchies.isEmpty())
				time.removeDuchy(this, this.duchies.get(0));

			while (!this.kingdoms.isEmpty())
				time.removeKingdom(this, this.kingdoms.get(0));

			while (!this.empires.isEmpty())
				time.removeEmpire(this, this.empires.get(0));
		} /*
			 * else { List<Title> titles = new ArrayList<Title>();
			 * titles.addAll(this.demesne); titles.addAll(this.getSubDemesne()); for (int i
			 * = 0; i < titles.size(); i++) { Title t = titles.get(i).master;
			 * 
			 * if (t.hasRequirements(this) && !vassalHas(t)) { time.addDuchy(this, t,
			 * false); }
			 * 
			 * t = titles.get(i).master.master;
			 * 
			 * if (t.hasRequirements(this) && !vassalHas(t)) { time.addKingdom(this, t,
			 * false); }
			 * 
			 * t = titles.get(i).master.master.master;
			 * 
			 * if (t.hasRequirements(this) && !vassalHas(t)) { time.addEmpire(this, t,
			 * false); } } }
			 */
	}

	public void checkRequirements(Title t) {
		t = t.master;

		if (t.hasRequirements(this) && !vassalHas(t)
				&& (getLord() == null || (getLord() != null && getLord().main.level > 1))) {
			time.addDuchy(this, t, false);
		}

		t = t.master;

		if (t.hasRequirements(this) && !vassalHas(t)
				&& (getLord() == null || (getLord() != null && getLord().main.level > 2))) {
			time.addKingdom(this, t, false);
		}

		t = t.master;

		if (t.hasRequirements(this) && !vassalHas(t) && getLord() == null) {
			time.addEmpire(this, t, false);
		}
	}

	public boolean vassalHas(Title t) {
		if (demesne.contains(t) || duchies.contains(t) || kingdoms.contains(t) || empires.contains(t))
			return true;

		for (int i = 0; i < sub.size(); i++) {
			if (sub.get(i).vassalHas(t))
				return true;
		}

		return false;
	}

	public void ManageKingdoms() {
		if (this.main.level == 3) {
			// System.out.println(this.id + " - Reorganizing Kingdoms");

			Title t = this.getRandomDuchySub();
			if (t == null)
				t = this.getRandomKingdomSub();

			if (t == null) {
				System.err.println("No applicable subs - Kingdom!");
			} else {
				Title give_away = getLeastImportantKingdom();

				addHistory(Main.CCOLOR.MAGENTA,
						getName() + " transfers " + give_away.toString() + " to " + t.owner.getName());
				t.owner.addHistory(Main.CCOLOR.MAGENTA,
						t.owner.getName() + " is transferred " + give_away.toString() + " by " + getName());

				time.addKingdom(t.owner, give_away, true);
			}
		}
	}

	public void ManageDuchies() {
		if (this.main.level >= 2) {
			// System.out.println(this.id + " - Reorganizing Duchies");

			Title t = this.getRandomCountySub();
			if (t == null)
				t = this.getRandomDuchySub();
			if (t == null)
				t = this.getRandomKingdomSub();

			if (t == null)
				return;
				//System.err.println("No applicable subs - Duchy!");
			else {
				Title give_away = getLeastImportantDuchy();

				addHistory(Main.CCOLOR.MAGENTA, getName() + " transfers " + t.toString() + " to " + t.owner.getName());
				t.owner.addHistory(Main.CCOLOR.MAGENTA,
						t.owner.getName() + " is transferred " + give_away.toString() + " by " + getName());

				time.addDuchy(t.owner, give_away, true);
			}
		}
	}

	public List<Title> getDuchyDemesne(Title t) {
		List<Title> titles = new ArrayList<Title>();

		for (int i = 0; i < t.titles.size(); i++) {
			Title t2 = t.titles.get(i);
			if (this.demesne.contains(t2))
				titles.add(t2);
		}

		return titles;
	}

	public List<Title> getDuchyDemesneWithSub(Title t) {
		List<Title> titles = new ArrayList<Title>();
		List<Title> total_d = new ArrayList<Title>();
		total_d.addAll(this.demesne);
		total_d.addAll(this.getAllSubDemesne());

		for (int i = 0; i < t.titles.size(); i++) {
			Title t2 = t.titles.get(i);
			if (total_d.contains(t2))
				titles.add(t2);
		}

		return titles;
	}

	public List<Title> getKingdomDemesneWithSub(Title t) {
		List<Title> titles = new ArrayList<Title>();
		List<Title> total_d = new ArrayList<Title>();
		total_d.addAll(this.duchies);
		total_d.addAll(this.getSubDuchies());

		for (int i = 0; i < t.titles.size(); i++) {
			Title t2 = t.titles.get(i);
			if (total_d.contains(t2))
				titles.add(t2);
		}

		return titles;
	}

	public void ManageDemesne() {
		if (this.main != null && this.capital != null) {
			if (this.main.level > 0) {
				Title t = this.getLeastImportantCounty();

				Person p = this.getFromCourt();
				removeCourt(p);
				p.lord = this;
				sub.add(p);

				addHistory(Main.CCOLOR.MAGENTA, getName() + " transfers " + t.toString() + " to " + p.getName());
				p.addHistory(Main.CCOLOR.MAGENTA, p.getName() + " is transferred " + t.toString() + " by " + getName());

				time.addDemesne(p, t, true);
			}
		}
	}

	public Title getLeastImportantCounty() {
		double far = 0;
		Title t = null;

		for (int i = 0; i < this.demesne.size(); i++) {
			Title temp = demesne.get(i);
			if (temp.getCenter().distance(this.capital.getCenter()) > far || t == null) {
				far = temp.getCenter().distance(this.capital.getCenter());
				t = temp;
			}
		}

		return t;
	}

	public Title getLeastImportantDuchy() {
		double far = 0;
		Title t = null;

		for (int i = 0; i < this.duchies.size(); i++) {
			Title temp = duchies.get(i);
			if (temp.getCenter().distance(this.capital.getCenter()) > far || t == null) {
				far = temp.getCenter().distance(this.capital.getCenter());
				t = temp;
			}
		}

		return t;
	}

	public Title getLeastImportantKingdom() {
		double far = 0;
		Title t = null;

		for (int i = 0; i < this.kingdoms.size(); i++) {
			Title temp = kingdoms.get(i);
			if (temp.getCenter().distance(this.capital.getCenter()) > far || t == null) {
				far = temp.getCenter().distance(this.capital.getCenter());
				t = temp;
			}
		}

		return t;
	}

	public Person getFromCourt() {
		if (this.court.isEmpty())
			return new Person(null, this.time, true, lang, Main.rand.nextInt(14) + 16, Main.rand.nextInt(21),
					new ArrayList<Person>());
		else {
			List<Person> possible = new ArrayList<Person>();

			for (int i = 0; i < this.court.size(); i++) {
				Person p = court.get(i);
				if (!p.dead && p.ismale)
					possible.add(p);
			}

			if (possible.isEmpty()) {
				return new Person(null, this.time, true, lang, Main.rand.nextInt(14) + 16, Main.rand.nextInt(21),
						new ArrayList<Person>());
			} else {
				Person p = possible.get(Main.rand.nextInt(possible.size()));
				court.remove(p);
				return p;
			}
		}
	}

	public Title getRandomDuchy() {
		List<Title> possible = new ArrayList<Title>();

		for (int i = 0; i < this.duchies.size(); i++) {
			Title t = duchies.get(i);
			if (!t.containsTitle(this.capital)) {
				// && this.demesne.containsAll(this.duchies.get(i).titles)) {
				possible.add(t);
			}
		}

		if (possible.isEmpty())
			return null;

		return possible.get(Main.rand.nextInt(possible.size()));
	}

	/*
	 * public Title getRandomDuchyNonCapital() { List<Title> possible = new
	 * ArrayList<Title>();
	 * 
	 * for (int i = 0; i < this.duchies.size(); i++) { if
	 * (!this.duchies.get(i).containsTitle(this.capital)) {
	 * possible.add(this.duchies.get(i)); } }
	 * 
	 * if (possible.isEmpty()) return null;
	 * 
	 * return possible.get(GenerateMap.rand.nextInt(possible.size())); }
	 */

	public Title getRandomKingdomSub() {
		List<Title> possible = new ArrayList<Title>();
		List<Title> sub_k = this.getSubKingdoms();

		for (int i = 0; i < sub_k.size(); i++) {
			Title t = sub_k.get(i);
			if (t.master != this.capital.master.master) {
				possible.add(t);
			}
		}

		if (possible.isEmpty())
			return null;

		return possible.get(Main.rand.nextInt(possible.size()));
	}

	public Title getRandomDuchySub() {
		List<Title> possible = new ArrayList<Title>();
		List<Title> sub_d = this.getSubDuchies();

		for (int i = 0; i < sub_d.size(); i++) {
			Title t = sub_d.get(i);
			if (t.master != this.capital.master.master) {
				possible.add(t);
			}
		}

		if (possible.isEmpty())
			return null;

		return possible.get(Main.rand.nextInt(possible.size()));
	}

	public Title getRandomCountySub() {
		List<Title> possible = new ArrayList<Title>();
		List<Title> sub_c = this.getSubDemesne();

		for (int i = 0; i < sub_c.size(); i++) {
			Title t = sub_c.get(i);
			if (t.master != this.capital.master) {
				possible.add(t);
			}
		}

		if (possible.isEmpty())
			return null;

		return possible.get(Main.rand.nextInt(possible.size()));
	}

	public Area getSubDemesneDraw() {
		Area area = new Area();
		List<Person> temp = new ArrayList<Person>();
		temp.addAll(sub);

		for (Person s : temp) {
			if (s != null && s.cache != null) {
				area.add(s.cache);
				area.add(s.getSubDemesneDraw());
			}
			/*
			 * if (!sub.get(i).sub.isEmpty()) { try {
			 * list.addAll(sub.get(i).getSubDemesne()); } catch (Exception e) {
			 * e.printStackTrace(); System.out.println(sub.get(i)); } }
			 */
		}

		return area;
	}

	public List<Title> getSubDemesne() {
		List<Title> list = new ArrayList<Title>();

		for (int i = 0; i < sub.size(); i++) {
			Person s = sub.get(i);
			list.addAll(s.demesne);
		}

		return list;
	}

	public List<Title> getAllSubDemesne() {
		List<Title> list = new ArrayList<Title>();

		for (int i = 0; i < sub.size(); i++) {
			Person s = sub.get(i);
			list.addAll(s.demesne);
			list.addAll(s.getAllSubDemesne());
			/*
			 * if (!sub.get(i).sub.isEmpty()) { try {
			 * list.addAll(sub.get(i).getSubDemesne()); } catch (Exception e) {
			 * e.printStackTrace(); System.out.println(sub.get(i)); } }
			 */
		}

		return list;
	}

	public List<Title> getSubDuchies() {
		List<Title> list = new ArrayList<Title>();

		for (int i = 0; i < this.sub.size(); i++) {
			list.addAll(sub.get(i).duchies);
		}

		return list;
	}

	public List<Title> getSubKingdoms() {
		List<Title> list = new ArrayList<Title>();

		for (int i = 0; i < this.sub.size(); i++) {
			list.addAll(sub.get(i).kingdoms);
		}

		return list;
	}

	public void makeChild() {
		// System.out.println(this.getName() + " - Had a Child!");
		Dynasty d = (ismale ? this.dynasty : spouse.dynasty);
		Person c = new Person(d, this.time, Main.rand.nextBoolean(), lang, 0,
				Math.min(20, Math.max(0, (intelligence + spouse.intelligence) / 2 + Main.rand.nextInt(10) - 5)),
				this.forefathers);

		this.children.add(c);
		spouse.children.add(c);
		spouse.gestation = 18;
		addHistory(Main.CCOLOR.CYAN, c.getName() + " is born to " + this.getName() + " and " + spouse.getName() + "!");
		spouse.addHistory(Main.CCOLOR.CYAN,
				c.getName() + ", is born to " + spouse.getName() + " and " + getName() + "!");

		c.addHistory(Main.CCOLOR.CYAN, c.getName() + " is born to " + getName() + " and " + spouse.getName() + "!");

		if (this.hasTerritory())
			time.addToCourt(c, this);
		else if (this.spouse.hasTerritory())
			time.addToCourt(c, this.spouse);
		else if (this.res != null && this.res.hasTerritory())
			time.addToCourt(c, this.res);
		else if (this.spouse.res != null && this.spouse.res.hasTerritory())
			time.addToCourt(c, this.spouse.res);
		else
			c.die(true);
	}

	public Title getBestTitle() {
		if (capital == null) {
			capital = getBestCounty();
		}
		if (!this.empires.isEmpty()) {
			Title closest = empires.get(0);
			if (capital != null) {
				for (int i = 0; i < empires.size(); i++) {
					Title t = empires.get(i);
					if (closest == null || t.getCenter().distance(capital.getCenter()) < closest.getCenter()
							.distance(capital.getCenter()))
						closest = t;
				}
			}
			return closest;
		} else if (!this.kingdoms.isEmpty()) {
			Title closest = kingdoms.get(0);
			if (capital != null) {
				for (int i = 0; i < kingdoms.size(); i++) {
					Title t = kingdoms.get(i);
					if (closest == null || t.getCenter().distance(capital.getCenter()) < closest.getCenter()
							.distance(capital.getCenter()))
						closest = t;
				}
			}
			return closest;
		} else if (!this.duchies.isEmpty()) {
			Title closest = duchies.get(0);
			if (capital != null) {
				for (int i = 0; i < duchies.size(); i++) {
					Title t = duchies.get(i);
					if (closest == null || t.getCenter().distance(capital.getCenter()) < closest.getCenter()
							.distance(capital.getCenter()))
						closest = t;
				}
			}
			return closest;
		} else if (!this.demesne.isEmpty())
			return getBestCounty();
		else
			return null;
	}

	public ArrayList<Title> getSortedCounties() {
		ArrayList<Title> sorted = new ArrayList<Title>();
		sorted.addAll(demesne);
		Collections.sort(sorted, new Comparator<Title>() {
			public int compare(Title t1, Title t2) {
				double dista = t1.getCenter().distance(capital.getCenter());
				double distb = t2.getCenter().distance(capital.getCenter());
				return (dista > distb ? 1 : (dista < distb ? -1 : 0));
			}
		});
		return sorted;
	}

	public void draw(Graphics2D g2, Color c, boolean selected) {
		if (c == null) {
			if (main == null)
				c = Color.RED;
			else if (getLord() == null)
				c = main.c;
			else {
				int depth = 1;
				Person curr = this.getLord();
				while (curr.getLord() != null) {
					curr = curr.getLord();
					depth++;
				}
				c = new Color((int) (curr.main.c.getRed() * Math.pow(0.9f, depth)),
						(int) (curr.main.c.getGreen() * Math.pow(0.9f, depth)),
						(int) (curr.main.c.getBlue() * Math.pow(0.9f, depth)));
			}
		}

		// g2.setColor(c);

		if (!this.sub.isEmpty()) {
			for (int i = 0; i < this.sub.size(); i++) {
				Color c2 = new Color((int) (c.getRed() * 0.9f), (int) (c.getGreen() * 0.9f),
						(int) (c.getBlue() * 0.9f));
				sub.get(i).draw(g2, c2, false);
			}
		}

		if (update_area) {
			update_area = false;
			cache = new Area();
			List<Title> draw_demesne = new ArrayList<Title>();
			draw_demesne.addAll(demesne);
			for (int i = 0; i < draw_demesne.size(); i++) {
				if (draw_demesne.get(i) != null)
					cache.add(draw_demesne.get(i).getArea());
			}
		}

		g2.setColor(c);
		g2.fill(cache);
		// g2.setColor(Color.GRAY);
		// g2.draw(a);

		// if (this.lord == null)
		/*
		 * else { if (this.main != null) { if (this.main.level == 2)
		 * g2.setColor(Color.BLACK); else g2.setColor(c.darker()); } else
		 * g2.setColor(c.darker()); }
		 */

		if (getLord() == null) {
			Area a2 = new Area();
			a2.add(cache);
			try {
				a2.add(getSubDemesneDraw());
			} catch (Exception e) {
				System.err.println("DRAW ERROR\t" + getName());
			}

			if (selected) {
				g2.setStroke(Display.dashed);
				g2.setColor(Color.WHITE);
			} else {
				g2.setColor(Color.BLACK);
			}
			g2.draw(a2);
			g2.setStroke(Display.default_stroke);
		} else if (selected) {
			Area a2 = new Area();
			a2.add(cache);
			try {
				a2.add(getSubDemesneDraw());
			} catch (Exception e) {
				System.err.println("DRAW ERROR\t" + getName());
			}

			g2.setStroke(Display.dashed);
			g2.setColor(Color.WHITE);
			g2.draw(a2);
			g2.setStroke(Display.default_stroke);
		}

		if (this.capital != null && this.main != null) {
			g2.setColor(c.darker());
			g2.drawOval((int) (this.capital.getCenter().getX() - (main.level + 1)),
					(int) (this.capital.getCenter().getY() - (main.level + 1)), (main.level + 1) * 2,
					(main.level + 1) * 2);
		}

		boolean flag = false;
		Person curr = this;
		while (curr != null) {
			if (!curr.wars.isEmpty()) {
				flag = true;
				break;
			}
			curr = curr.getLord();
		}
		if (flag) {
			g2.setColor(Color.RED);
			g2.setStroke(Display.dashed);
			for (int i = 0; i < demesne.size(); i++) {
				if (demesne.get(i).occupied) {
					g2.draw(demesne.get(i).getArea());
				}
			}
			g2.setStroke(Display.default_stroke);
		}
	}

	public String toString() {
		return this.getName();
	}

	public Title getNearestUnoccupiedDemesne(Person p) {
		List<Title> demesne = new ArrayList<Title>();
		demesne.addAll(this.demesne);
		demesne.addAll(this.getAllSubDemesne());

		Title t = null;
		double shortest = 99999;

		for (int i = 0; i < demesne.size(); i++) {
			if (!demesne.get(i).occupied) {
				Title d = demesne.get(i);

				if (d.getCenter().distance(p.capital.getCenter()) < shortest || t == null) {
					t = d;
					shortest = d.getCenter().distance(p.capital.getCenter());
				}
			}
		}

		return t;
	}

	public Title getNearestOccupiedDemesne(Person p) {
		List<Title> demesne = new ArrayList<Title>();
		demesne.addAll(this.demesne);
		demesne.addAll(this.getAllSubDemesne());

		Title t = null;
		double shortest = 99999;

		for (int i = 0; i < demesne.size(); i++) {
			if (demesne.get(i).occupied) {
				Title d = demesne.get(i);

				if (d.getCenter().distance(p.capital.getCenter()) < shortest || t == null) {
					t = d;
					shortest = d.getCenter().distance(p.capital.getCenter());
				}
			}
		}

		return t;
	}

	public void LessenTruces() {
		HashMap<Person, Integer> new_truces = new HashMap<Person, Integer>();

		for (Person key : truces.keySet()) {
			if (truces.get(key) - 1 > 0)
				new_truces.put(key, truces.get(key) - 1);
		}

		this.truces = new_truces;
	}

	public String getName() {
		String dead = "";
		if (this.dead)
			dead = " DEAD";
		String title = "";
		if (main != null) {
			switch (main.level) {
				case 0:
					title = (ismale ? "Count " : "Countess ");
					break;
				case 1:
					title = (ismale ? "Duke " : "Duchess ");
					break;
				case 2:
					title = (ismale ? "King " : "Queen ");
					break;
				case 3:
					title = (ismale ? "Emperor " : "Empress ");
					break;
			}
		}
		return title + this.name.transpose() + " " + this.dynasty + roman_numeral + " (" + this.intelligence + ","
				+ (this.ismale ? "M" : "F") + "," + (int) this.age + ")" + dead;
	}

	public void printVassalChart(int tab) {
		for (int i = 0; i < tab; i++)
			System.out.print("\t");
		System.out.println(getName() + " - " + lang.getName());
		for (Person v : sub) {
			v.printVassalChart(tab + 1);
		}
	}

	public int getSubSize() {
		return sub.size();
	}

	public Person getSubAt(int i) {
		return sub.get(i);
	}

	public List<Person> getSubList() {
		return sub;
	}

	public List<Person> getCourtList() {
		return court;
	}

	public void setCourtList(List<Person> new_court) {
		court.clear();
		court.addAll(new_court);
	}

	public Person getCourtAt(int i) {
		return court.get(i);
	}

	public int getCourtSize() {
		return court.size();
	}

	public Person getLord() {
		return lord;
	}

	public Person getRes() {
		return res;
	}

	public Person getSpouse() {
		return spouse;
	}

	public void setSpouse(Person p) {
		if (p != null)
			p.spouse = this;
		spouse = p;
	}

	public void removeCourt(Person p) {
		addHistory(Main.CCOLOR.MAGENTA, p.getName() + " leaves the court of " + getName());
		p.addHistory(Main.CCOLOR.MAGENTA, p.getName() + " leaves the court of " + getName());
		court.remove(p);
		p.res = null;
	}

	public void removeSub(Person p) {
		addHistory(Main.CCOLOR.YELLOW, getName() + " loses their vassal " + p.getName());
		p.addHistory(Main.CCOLOR.GREEN, p.getName() + " breaks from from " + getName());
		sub.remove(p);
		p.lord = null;
	}

	public boolean containsSub(Person p) {
		return sub.contains(p);
	}

	public boolean containsCourt(Person p) {
		return court.contains(p);
	}

	public int totalAreaSize() {
		return demesne.size() + getAllSubDemesne().size();
	}

	public void setLord(Person new_lord) {
		if (new_lord == this) {
			System.err.println("ERROR: Cannot set lord to one's self!");
			System.exit(1);
		}
		if (lord != null)
			lord.removeSub(this);
		if (new_lord != null) {
			if (new_lord.main == null || main == null) {
				System.err.println("ERROR: Vassals and lords must own land!");
				System.exit(1);
			}
			if (new_lord.main.level <= main.level) {
				System.err.println("ERROR: Cannot vassalize someone at the same level or higher!");
				// System.exit(1);
				System.out.println(getName() + " > " + new_lord.getName());
				return;
			}
			new_lord.addHistory(Main.CCOLOR.GREEN, new_lord.getName() + " vassalizes " + getName());
			addHistory(Main.CCOLOR.YELLOW, getName() + " is vassalized by " + new_lord.getName());
			new_lord.sub.add(this);
		}
		lord = new_lord;
	}

	public void setRes(Person new_res) {
		if (new_res == this) {
			System.err.println("ERROR: Cannot set res to one's self!");
			System.exit(1);
		}
		if (res != null)
			res.removeCourt(this);
		res = new_res;
		if (new_res != null) {
			new_res.addHistory(Main.CCOLOR.MAGENTA, new_res.getName() + " is joined at court by " + getName());
			addHistory(Main.CCOLOR.MAGENTA, getName() + " arrives at the court of " + new_res.getName());
			new_res.court.add(this);
		}
	}

	public void addHistory(Main.CCOLOR color, String s) {
		history.add("[" + time.semimonth + "]\t" + color + s + Main.CCOLOR.RESET);
	}

	public List<String> getHistory() {
		return history;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean culled) {
		dead = true;
		time.deaths++;

		if (culled)
			this.addHistory(Main.CCOLOR.CYAN,
					this.getName() + ", at age " + this.age + ", vanishes into obscurity, never to be seen again...");
		else
			this.addHistory(Main.CCOLOR.CYAN, this.getName() + " dies at age " + this.age);

		for (Person p : children) {
			p.addHistory(Main.CCOLOR.CYAN,
					(ismale ? "Father" : "Mother") + " " + getName() + " of " + p.getName() + " dies at age " + age);
		}

		if (Main.COOL_NAMES.contains(name.transpose())) {
			time.getPerson(this);
			System.out.println("\033[0;34mWeep, my friends, for " + this.getName() + ", a great "
					+ (ismale ? "man" : "woman") + ", has passed to the next world...\033[0m");
		}
	}
}
