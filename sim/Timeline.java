package sim;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gen.GenerateMap;
import ling.Language;
import gen.sPolygon;
import main.Display;
import main.Main;

public class Timeline {
	private final int DUCHY_AMOUNT = 60;
	private final int KINGDOM_AMOUNT = 15;
	private final int EMPIRE_AMOUNT = 6;

	HashMap<Title, Title> county_map = new HashMap<Title, Title>();

	long time;
	GenerateMap map;
	List<Title> counties;
	List<Title> duchies;
	List<Title> kingdoms;
	List<Title> empires;
	List<Title> curr_wars;
	public List<Person> person_manager;
	public List<Person> to_be_added;
	public HashMap<Integer, Person> person_registry = new HashMap<Integer, Person>();
	int id;
	public long semimonth;
	public long dynasty_count = 0;
	public long original_dynasties = 0;

	public long births, deaths;
	public double replacementrate = 0;

	public Timeline(GenerateMap map) {
		this.map = map;

		this.counties = new ArrayList<Title>();
		this.duchies = new ArrayList<Title>();
		this.kingdoms = new ArrayList<Title>();
		this.empires = new ArrayList<Title>();
		this.curr_wars = new ArrayList<Title>();
		this.person_manager = new ArrayList<Person>();
		this.to_be_added = new ArrayList<Person>();

		this.GenerateCounties();
		this.GenerateDuchies();
		this.GenerateKingdoms();
		this.GenerateEmpires();
		this.setNeighbors();

		this.setPeople();

		System.out.println("Total Counties: " + counties.size());
		// this.step();
	}

	public Person searchForSpouse(Person p) {
		float min_a = p.age - 20;
		float max_a = p.age + 5;

		List<Person> pl = new ArrayList<Person>();

		for (int i = 0; i < this.person_manager.size(); i++) {
			Person pp = this.person_manager.get(i);
			if (pp.age < max_a && pp.age > min_a && !pp.isDead() && pp.ismale != p.ismale && pp.dynasty != p.dynasty) {
				pl.add(pp);
			}
		}

		Collections.sort(pl, new Comparator<Person>() {
			@Override
			public int compare(Person a, Person b) {
				return b.intelligence - a.intelligence;
			}
		});

		if (pl.isEmpty()) {
			System.out.println("Can't find spouse!");
			return null;
		} else
			return pl.get((int) (Math.pow((double) Main.rand.nextInt(pl.size()) / (double) pl.size(), 2)
					* (double) pl.size()));
	}

	public Person searchForSpouseLanguage(Person p) {
		float min_a = p.age - 20;
		float max_a = p.age + 5;

		List<Person> pl = new ArrayList<Person>();

		for (int i = 0; i < this.person_manager.size(); i++) {
			Person pp = this.person_manager.get(i);
			if (pp.age < max_a && pp.age > min_a && !pp.isDead() && pp.ismale != p.ismale && p.lang == pp.lang
					&& pp.dynasty != p.dynasty) {
				pl.add(pp);
			}
		}

		Collections.sort(pl, new Comparator<Person>() {
			@Override
			public int compare(Person a, Person b) {
				return b.intelligence - a.intelligence;
			}
		});

		if (pl.isEmpty()) {
			return null;
		} else
			return pl.get((int) (Math.pow((double) Main.rand.nextInt(pl.size()) / (double) pl.size(), 2)
					* (double) pl.size()));
	}

	public void setPeople() {
		for (int i = 0; i < this.duchies.size(); i++) {
			Person p = new Person(null, this, true, this.duchies.get(i).getLanguage(), Main.rand.nextInt(14) + 16,
					Main.rand.nextInt(21), new ArrayList<Person>());
			Person p2 = new Person(null, this, false, this.duchies.get(i).getLanguage(), Main.rand.nextInt(14) + 16,
					Main.rand.nextInt(21), new ArrayList<Person>());

			p.dynasty.original = true;
			p2.dynasty.original = true;

			original_dynasties+=2;

			for (int j = 0; j < this.duchies.get(i).titles.size(); j++) {
				addDemesne(p, this.duchies.get(i).titles.get(j), false);
			}
			marry(p, p2);
		}
	}

	public void setNeighbors() {
		System.out.println("Setting Neighbors...");
		for (int i = 0; i < this.counties.size(); i++) {
			List<sPolygon> neighbors = this.counties.get(i).poly.neighbors;

			for (int j = 0; j < neighbors.size(); j++) {
				if (neighbors.get(j).getHeight() < 4 && neighbors.get(j).isLand())
					this.counties.get(i).neighbors.add(neighbors.get(j).title);
			}
		}
	}

	List<Person> to_be_pruned;

	public void step() {
		semimonth += 1;

		if (semimonth % 24 == 0) {
			replacementrate = (double) births / (double) deaths;
			births = 0;
			deaths = 0;
		}

		this.to_be_pruned = new ArrayList<Person>();

		person_manager.addAll(to_be_added);
		to_be_added.clear();

		for (int i = 0; i < this.person_manager.size(); i++) {
			Person p = this.person_manager.get(i);

			if (p.gestation > 0)
				p.gestation--;

			if (p.move > 0)
				p.move--;

			if (p.coup > 0)
				p.coup--;

			if (p.getLord() != null) {
				if (!p.getLord().containsSub(p)) {
					p.setLord(p.getLord());
					System.err.println("Peculiar Error...");
				}
			}

			if (!p.getClaims().isEmpty()) {
				ArrayList<Title> new_list = new ArrayList<Title>();
				for (int j = 0; j < p.getClaims().size(); j++) {
					Title t = p.getClaims().get(j);
					if (!p.demesne.contains(t))
						new_list.add(t);
				}
				p.setClaims(new_list);
			}

			if (!p.getCourtList().isEmpty()) {
				List<Person> new_court = new ArrayList<Person>();

				for (int x = 0; x < p.getCourtSize(); x++) {
					Person c = p.getCourtAt(x);
					if (!c.isDead()) {
						new_court.add(c);
					}
				}

				p.setCourtList(new_court);
			}

			if (!p.hasTerritory()) {
				p.capital = null;
				p.main = null;
			}

			if (p.hasTerritory()) {
				if (p.getLord() != null && p.getLord().capital != null) {
					if (p.getLord().intelligence < p.intelligence - 5 && p.getLord().coup == 0 && p.age > 16
							&& Main.rand.nextInt(200 + (int) (p.getLord().capital.development * 0.2f)) < Math
									.abs(p.getLord().intelligence - p.intelligence)) {
						Person dumb = p.getLord();
						Title t = dumb.main;
						if (Main.rand.nextInt(3) == 0) {
							dumb.addHistory(Main.CCOLOR.RED,
									dumb.getName() + " is overrun in a coup d'état lead by " + p.getName() + "!");
							p.addHistory(Main.CCOLOR.GREEN,
									p.getName() + " launches a coup d'état against " + dumb.getName() + "!");
							switch (t.level) {
								case 1:
									addDuchy(p, t, false);
									break;
								case 2:
									addKingdom(p, t, false);
									break;
								case 3:
									addEmpire(p, t, false);
									break;
							}
							p.setLord(dumb.getLord());
							if (p.main.level >= dumb.main.level) {
								for (int j = 0; j < dumb.getSubSize(); j++) {
									if (dumb.main.level <= dumb.getSubAt(j).main.level)
										dumb.getSubAt(j).setLord(p);
								}
								if (p.main.level > dumb.main.level)
									dumb.setLord(p);
							}
							List<Title> demesne = new ArrayList<Title>();
							demesne.addAll(dumb.demesne);
							for (int j = 0; j < demesne.size(); j++)
								addDemesne(p, demesne.get(j), false);
							p.coup = 240;
						} else {
							dumb.addHistory(Main.CCOLOR.GREEN,
									dumb.getName() + " crushes an attempted coup d'état lead by " + p.getName() + "!");
							p.addHistory(Main.CCOLOR.RED,
									p.getName() + " fails leading a coup d'état against " + dumb.getName() + "!");
							List<Title> demesne = new ArrayList<Title>();
							demesne.addAll(p.demesne);
							for (int j = 0; j < demesne.size(); j++)
								addDemesne(dumb, demesne.get(j), false);
							dumb.coup = 240;
						}
					}
				}

				if (p.getRes() != null) {
					p.getRes().removeCourt(p);
				}

				if (p.getLord() != null) {
					if (p.lang != p.getLord().lang) {
						if (Main.rand.nextInt(300 * (p.main.level+1)) == 0) {
							p.lang = p.getLord().lang;
							p.addHistory(Main.CCOLOR.MAGENTA, p.getName() + " converts to the culture of their lord " + p.getLord().getName() + ", " + p.lang.getName());
							p.getLord().addHistory(Main.CCOLOR.MAGENTA, p.getLord().getName() + "'s vassal " + p.getName() + " converts to their lord's culture " + p.lang.getName());
						}
					}
				}

				if (p.main != null && p.capital != null) {
					if (p.main.level > 0 || p.demesne.size() < p.max_c + p.duchies.size())
						this.searchForClaim(p);
				}

				if (Main.rand.nextInt(10) == 0 && !p.getClaims().isEmpty() && p.wars.isEmpty() && p.age > 16) {
					List<Title> claims = new ArrayList<Title>();

					for (int x = 0; x < p.getClaims().size(); x++) {
						Title c = p.getClaims().get(x);
						Person potential = this.getTopMost(p, c.owner);
						if ((this.inSameRealm(potential, p) || potential.getLord() == null) && potential.levy <= p.levy
								&& p.getLord() != c.owner && !this.curr_wars.contains(c)
								&& (p.getSpouse() == null
										|| (p.getSpouse() != null && !potential.children.contains(p.getSpouse())))
								&& !p.truces.containsKey(this.getTopMost(p, c.owner)))
							claims.add(c);
					}

					if (!claims.isEmpty()) {
						Title claim = claims.get(Main.rand.nextInt(claims.size()));

						this.addWar(p, claim);
					}
				}

				List<Title> new_claims = new ArrayList<Title>();

				for (int x = 0; x < p.getClaims().size(); x++) {
					Title c = p.getClaims().get(x);
					if (!this.isVassalOf(p, c.owner)) {
						new_claims.add(c);
					} /*
						 * else if (p.demesne.size() < p.max_d) { this.addDemesne(p, c); }
						 */
				}

				p.setClaims(new_claims);

				List<War> to_be_removed = new ArrayList<War>();

				for (int x = 0; x < p.wars.size(); x++) {
					War w = p.wars.get(x);
					if (w.getDone() && w.getDeleted() == false) {
						w.end();
						to_be_removed.add(w);
					} else if (w.getDone() == false && w.getDeleted() == false)
						w.execute(p);
				}

				for (int x = 0; x < to_be_removed.size(); x++) {
					to_be_removed.get(x).delete();
				}

				if (p.main != null) {
					if (p.demesne.size() > p.max_c + p.duchies.size())
						p.ManageDemesne();

					if (p.duchies.size() > p.max_d)
						p.ManageDuchies();

					/*
					 * if (p.kingdoms.size() > p.max_k) p.ManageKingdoms();
					 */
				}

				// expand(p);
				p.updateDemesne();
				p.updateLevy();
			} else if (!p.getSubList().isEmpty()) {
				this.releaseSubs(p);
			}

			if (Main.rand.nextInt((p.children.size() + 1) * 100) == 0 && p.age > 12 && p.isActive()
					&& (p.getSpouse() != null && p.getSpouse().age < 45 && p.getSpouse().age > 12
							&& p.getSpouse().gestation == 0)
					&& p.ismale)
				p.makeChild();

			if (p.getSpouse() == null && Main.rand.nextInt(40) == 0 && p.age > 10 && p.ismale
					&& ((p.getRes() != null && (p.getRes().children.contains(p))) || p.hasTerritory())) {
				Person s = this.searchForSpouseLanguage(p);
				if (s == null)
					s = this.searchForSpouse(p);

				if (s != null)
					this.marry(s, p);
			}

			if (p.getSpouse() != null)
				if (p.getSpouse().isDead()) {
					p.addHistory(Main.CCOLOR.CYAN, p.getName() + "'s " + (p.getSpouse().ismale ? "husband" : "wife")
							+ ", " + p.getSpouse().getName() + ", has passed away");
					p.setSpouse(null);
				}

			p.LessenTruces();
			double chance = Math.pow(p.age / 100.0f, 9);

			if (Main.rand.nextDouble() < chance) {
				p.setDead(false);
				if (p.capital != null && p.capital.development > 500) {
					boolean success = this.inherit(p, null);
					if (!success && p.dynasty.getHighestMember() != null)
						this.inherit(p, p.dynasty.getHighestMember());
				} else {
					this.gavelkind(p);
				}
				p.die(false);
			} else
				p.age = p.age + 1.0f / 24.0f;

			if (p.getRes() != null) {
				if (p.getRes().isDead() && !p.hasTerritory()) {
					if (p.getSpouse() != null) {
						if (p.getSpouse().getRes() != null && p.getSpouse().getRes() != p)
							p.setRes(p.getSpouse().getRes());
						else if (p.getSpouse().hasTerritory())
							p.setRes(p.getSpouse());
						else
							p.die(true);
					} else
						p.die(true);
				} else if (p.getRes().isDead() && p.hasTerritory()) {
					p.getRes().removeCourt(p);
				}
			}

			if (!p.isActive() && !to_be_pruned.contains(p))
				to_be_pruned.add(p);
			else if (p.getRes() != null) {
				if (!p.getRes().isActive() && !p.hasTerritory() && !to_be_pruned.contains(p))
					to_be_pruned.add(p);
			}
		}

		develop();

		if (!this.to_be_pruned.isEmpty()) {
			for (int i = 0; i < this.to_be_pruned.size(); i++) {
				Person tbp = to_be_pruned.get(i);
				// tbp.addHistory(to_be_pruned.get(i).getName() + " is being removed");
				if (!tbp.isDead())
					tbp.die(true);

				this.person_manager.remove(tbp);
				this.person_registry.remove(tbp.id);
			}
		}
	}

	private void develop() {
		HashMap<Title, Float> change = new HashMap<Title, Float>();
		for (int i = 0; i < counties.size(); i++) {
			Title t = counties.get(i);
			if (!t.occupied) {
				Title heighest = null;
				for (int j = 0; j < t.neighbors.size(); j++) {
					Title n = t.neighbors.get(j);
					if (heighest == null)
						heighest = n;
					else if (n.development > t.development) {
						heighest = n;
					}
				}
				if (heighest.development > t.development)
					change.put(t, (float) Math.max(0, Math.min(1000, t.development + Math.min(0.04
							* (float) ((getHighestLord(t.owner).main.level + 1) * ((float) t.owner.intelligence / 20f)),
							(heighest.development - t.development) / 3.0f))));
			}
		}

		for (Map.Entry<Title, Float> e : change.entrySet())
			e.getKey().development = e.getValue();
	}

	private Person getHighestLord(Person p) {
		if (p.getLord() == null)
			return p;
		return getHighestLord(p.getLord());
	}

	private boolean gavelkind(Person p) {
		if (p.children.isEmpty())
			return false;

		List<Person> children = new ArrayList<Person>();

		for (int i = 0; i < p.children.size(); i++) {
			Person c = p.children.get(i);
			if (!c.isDead()) {
				if (c.ismale) {
					children.add(c);
				}
			}
		}

		if (children.isEmpty()) {
			for (int i = 0; i < p.children.size(); i++) {
				Person c = p.children.get(i);
				if (!c.isDead()) {
					children.add(c);
				}
			}
		}

		if (children.isEmpty())
			return false;

		if (p.hasTerritory()) {
			List<Person> inheritors = new ArrayList<Person>();

			Person curr = p.getLord();
			if (children.contains(curr)) {
				curr = curr.getLord();
			}

			p.addHistory(Main.CCOLOR.BLUE, p.getName() + " dies and distributes their lands to " + children);
			for (int i = 0; i < children.size(); i++)
				children.get(i).addHistory(Main.CCOLOR.BLUE, children.get(i).getName()
						+ " inherits some of the lands of " + p.getName() + " (including: " + children + ")");

			addDemesne(children.get(0), p.capital, true);
			if (curr == null || (curr.main != null && children.get(0).main.level < curr.main.level))
				children.get(0).setLord(curr);

			Title best = p.getBestTitle();
			if (best != null) {
				switch (best.level) {
					case 1:
						addDuchy(children.get(0), best, true);
						break;
					case 2:
						addKingdom(children.get(0), best, true);
						break;
					case 3:
						addEmpire(children.get(0), best, true);
						break;
				}
			}

			while (!p.empires.isEmpty()) {
				for (int i = (best.level == 3 && children.size() > 1 ? 1 : 0); i < children.size(); i++) {
					if (p.empires.isEmpty())
						break;
					if (children.get(i).main != null && curr != null) {
						if (children.get(i).main.level < curr.main.level) {
							addEmpire(children.get(i), p.getBestTitle(), true);
							if (!inheritors.contains(children.get(i)))
								inheritors.add(children.get(i));
						} else
							addEmpire(curr, p.getBestTitle(), true);
					} else {
						addEmpire(children.get(i), p.getBestTitle(), true);
						if (!inheritors.contains(children.get(i)))
							inheritors.add(children.get(i));
					}
				}
			}

			while (!p.kingdoms.isEmpty()) {
				for (int i = (best.level == 2 && children.size() > 1 ? 1 : 0); i < children.size(); i++) {
					if (p.kingdoms.isEmpty())
						break;
					if (children.get(i).main != null && curr != null) {
						if (children.get(i).main.level < curr.main.level) {
							addKingdom(children.get(i), p.getBestTitle(), true);
							if (!inheritors.contains(children.get(i)))
								inheritors.add(children.get(i));
						} else
							addKingdom(curr, p.getBestTitle(), true);
					} else {
						addKingdom(children.get(i), p.getBestTitle(), true);
						if (!inheritors.contains(children.get(i)))
							inheritors.add(children.get(i));
					}
				}
			}

			while (!p.duchies.isEmpty()) {
				for (int i = (best.level == 1 && children.size() > 1 ? 1 : 0); i < children.size(); i++) {
					if (p.duchies.isEmpty())
						break;
					if (children.get(i).main != null && curr != null) {
						if (children.get(i).main.level < curr.main.level) {
							addDuchy(children.get(i), p.getBestTitle(), true);
							if (!inheritors.contains(children.get(i)))
								inheritors.add(children.get(i));
						} else
							addDuchy(curr, p.getBestTitle(), true);
					} else {
						addDuchy(children.get(i), p.getBestTitle(), true);
						if (!inheritors.contains(children.get(i)))
							inheritors.add(children.get(i));
					}
				}
			}

			while (!p.demesne.isEmpty()) {
				for (int i = 0; i < children.size(); i++) {
					if (p.demesne.isEmpty())
						break;
					if (children.get(i).main != null && curr != null) {
						if (children.get(i).main.level < curr.main.level) {
							addDemesne(children.get(i), p.getBestTitle(), true);
							if (!inheritors.contains(children.get(i)))
								inheritors.add(children.get(i));
						} else
							addDemesne(curr, p.getBestTitle(), true);
					} else {
						addDemesne(children.get(i), p.getBestTitle(), true);
						if (!inheritors.contains(children.get(i)))
							inheritors.add(children.get(i));
					}
				}
			}

			List<Person> subs = new ArrayList<Person>();
			subs.addAll(p.getSubList());
			for (int i = 0; i < subs.size(); i++) {
				if (!inheritors.contains(subs.get(i)) && subs.get(i).main.level < children.get(0).main.level)
					subs.get(i).setLord(children.get(0));
				else
					subs.get(i).setLord(curr);
			}

			for (int i = 0; i < inheritors.size(); i++) {
				Person c = inheritors.get(i);
				if (c.main != null && c.main.level < children.get(0).main.level)
					c.setLord(children.get(0));
				else
					c.setLord(curr);
			}

			transferCourt(p, children.get(0));
		}

		return true;
	}

	private boolean inherit(Person p, Person child) {
		if (child == null) {
			if (p.children.isEmpty())
				return false;
			for (int i = 0; i < p.children.size(); i++) {
				Person c = p.children.get(i);
				if (!c.isDead()) {
					if (c.ismale) {
						child = c;
						break;
					}
				}
			}

			if (child == null && !p.children.get(0).isDead())
				child = p.children.get(0);

			if (child == null)
				return false;
		}

		// System.out.println(p.getName() + " goes to " + child.getName());

		if (p.hasTerritory()) {
			Person curr = p.getLord();
			if (curr == child) {
				curr = curr.getLord();
			}

			p.addHistory(Main.CCOLOR.BLUE, p.getName() + " dies and gives their lands to " + child.getName());
			child.addHistory(Main.CCOLOR.BLUE, child.getName() + " inherits the lands of " + p.getName());

			if (p.containsSub(child)) {
				p.removeSub(child);
			}

			if (child.getRes() != null) {
				child.getRes().removeCourt(child);
			}

			if (p.duchies.isEmpty() && p.kingdoms.isEmpty() && p.empires.isEmpty()) {
				while (child.demesne.size() < child.max_c && !p.demesne.isEmpty()) {
					if (child.main != null && curr != null) {
						if (child.main.level < curr.main.level)
							addDemesne(child, p.getBestTitle(), true);
						else
							addDemesne(curr, p.getBestTitle(), true);
					} else
						addDemesne(child, p.getBestTitle(), true);
				}
				if (curr != null) {
					if (child.main.level < curr.main.level) {
						child.setLord(curr);
					}
				}
			} else {
				addDemesne(child, p.capital, true);
				if (curr == null || (curr.main != null && child.main.level < curr.main.level))
					child.setLord(curr);
				while (p.empires.size() > 0) {
					if (child.getLord() == null)
						this.addEmpire(child, p.empires.get(0), true);
					else
						this.addEmpire(child.getLord(), p.empires.get(0), true);
					// if (p.empires.size() > 0 && p.empires.get(0).owner != p)
					// p.empires.remove(p.empires.get(0));
				}
				while (p.kingdoms.size() > 0) {
					if (child.getLord() == null)
						this.addKingdom(child, p.kingdoms.get(0), true);
					else if (child.getLord() != null && child.getLord().main.level >= 3)
						this.addKingdom(child, p.kingdoms.get(0), true);
					else
						this.addKingdom(child.getLord(), p.kingdoms.get(0), true);
					// if (p.kingdoms.size() > 0 && p.kingdoms.get(0).owner != p)
					// p.kingdoms.remove(p.kingdoms.get(0));
				}
				while (p.duchies.size() > 0) {
					if (child.getLord() == null)
						this.addDuchy(child, p.duchies.get(0), true);
					else if (child.getLord() != null && child.getLord().main.level >= 2)
						this.addDuchy(child, p.duchies.get(0), true);
					else
						this.addDuchy(child.getLord(), p.duchies.get(0), true);
					// if (p.duchies.size() > 0 && p.duchies.get(0).owner != p)
					// p.duchies.remove(p.duchies.get(0));
				}
				while (p.demesne.size() > 0) {
					if (child.getLord() == null)
						this.addDemesne(child, p.demesne.get(0), true);
					else if (child.getLord() != null && child.getLord().main.level >= 1)
						this.addDemesne(child, p.demesne.get(0), true);
					else
						this.addDemesne(child.getLord(), p.demesne.get(0), true);
					// if (p.demesne.size() > 0 && p.demesne.get(0).owner != p)
					// p.demesne.remove(p.demesne.get(0));
				}

				List<Person> subs = new ArrayList<Person>();
				subs.addAll(p.getSubList());
				for (int i = 0; i < subs.size(); i++) {
					subs.get(i).setLord(child);
				}
			}
			transferCourt(p, child);

			child.recalculateMain();
		}

		return true;
	}

	public void getPerson(Person p) {
		Person s = p;

		if (s == null) {
			System.out.println("-----------------------------------------------");

			System.out.print("ID: ");
			int id = Display.sc.nextInt();
			s = person_registry.get(id);
		}

		System.out.println("-----------------------------------------------");
		System.out.println(s.id + " - " + s.getName());
		System.out.println("[" + s.name.toString() + "]");
		System.out.println("INT: " + s.intelligence);
		System.out.println("LORD: " + s.getLord());
		System.out.println("COURT: " + s.getRes());
		System.out.println("Vassals(" + s.getSubSize() + "): ");
		s.printVassalChart(0);
		System.out.println("Demesne(" + s.demesne.size() + "): ");
		System.out.println(s.demesne);
		System.out.println("Duchies(" + s.duchies.size() + "): ");
		System.out.println(s.duchies);
		System.out.println("Kingdoms(" + s.kingdoms.size() + "): ");
		System.out.println(s.kingdoms);
		System.out.println("Empires(" + s.empires.size() + "): ");
		System.out.println(s.empires);
		System.out.println("-----------------------------------------------");
		for (Person n : s.forefathers)
			System.out.println(n + " - " + n.lang.getName());
		System.out.println("-----------------------------------------------");
		getHistory(s);
		System.out.println("-----------------------------------------------");
	}

	public void getHistory(Person s) {
		for (int i = 0; i < s.getHistory().size(); i++) {
			System.out.println(s.getHistory().get(i));
		}
	}

	public void getDynasty(Person s) {
		System.out.println("-----------------------------------------------");
		System.out.println("Dynasty Name: " + s.dynasty + " (" + s.dynasty.getSize() + ")");
		// System.out.println("Dynasty Origin: " + s.dynasty.origin.getName());
		System.out.println("-----------------------------------------------");
		for (int i = 0; i < s.dynasty.getSize(); i++) {
			Person p = s.dynasty.getMemberAt(i);
			if (!p.isDead()) {
				if (p.main == null)
					System.out.print("\t");
				System.out.println(p.getName() + " - " + p.lang.getName());
			}
		}
		System.out.println("-----------------------------------------------");
	}

	public void setForRemove(Person p) {
		this.to_be_pruned.add(p);
	}

	public void addToCourt(Person a, Person b) {
		if (a.demesne.size() > 0) {
			System.err.println("Can't add a person who has territory to another's court!");
			getPerson(a);
			getPerson(b);
			System.exit(0);
		} else if (b.containsCourt(a)) {
			// System.err.println(a.getName() + " already lives in the court of " +
			// b.getName());
		} else {
			// System.out.println(a.id + " - Adding to Court of " + b.id);
			if (a.getRes() != null) {
				a.getRes().removeCourt(a);
			}
			if (a.getLord() != null) {
				a.setLord(null);
			}

			a.setRes(b);
		}
	}

	public void marry(Person a, Person b) {
		// System.out.println(a.getName() + " Marries " + b.getName());

		a.addHistory(Main.CCOLOR.CYAN, a.getName() + " Marries " + b.getName());
		b.addHistory(Main.CCOLOR.CYAN, b.getName() + " Marries " + a.getName());

		if (a.hasTerritory() && !b.hasTerritory())
			this.addToCourt(b, a);
		if (b.hasTerritory() && !a.hasTerritory())
			this.addToCourt(a, b);

		a.setSpouse(b);
	}

	public Person getTopMost(Person a, Person b) {
		Person curr = b;
		while (curr.getLord() != null) {
			if (this.inSameRealm(a, b)) {
				break;
			}
			if (curr == b || curr == a)
				break;

			curr = curr.getLord();
		}

		return curr;
	}

	public void searchForClaim(Person p) {
		List<Title> bellum = new ArrayList<Title>();

		List<Title> total_d = new ArrayList<Title>();

		total_d.addAll(p.demesne);
		total_d.addAll(p.getAllSubDemesne());

		for (int i = 0; i < total_d.size(); i++) {
			List<Title> neighbors = total_d.get(i).neighbors;

			for (int j = 0; j < neighbors.size(); j++) {
				Title t = neighbors.get(j);
				if (t.owner != null && !total_d.contains(t) && !p.getClaims().contains(t)) { // p.capital.master.master)
					bellum.add(t);
				}
			}
		}

		if (Main.rand.nextInt(200) == 0 && !bellum.isEmpty()) {
			Title closest = null;
			for (int i = 0; i < bellum.size(); i++) {
				Title t = bellum.get(i);
				if (closest == null)
					closest = t;
				else if (closest.getCenter().distance(p.capital.getCenter()) > t.getCenter()
						.distance(p.capital.getCenter()))
					closest = t;
			}
			this.addClaimDemesne(p, closest);
		}
	}

	public void addTruce(Person a, Person b, int time) {
		a.truces.put(b, time);
		b.truces.put(a, time);
	}

	public boolean isVassalOf(Person a, Person b) {
		return b.getLord() == a;
	}

	public void addClaimDemesne(Person p, Title claim) {
		if (claim.owner == null)
			return;
		else {
			p.addClaim(claim);
		}
	}

	public void addWar(Person p, Title claim) {
		// System.out.println("Declaring War...");
		this.curr_wars.add(claim);

		Person p2 = claim.owner;

		War war = new War(claim, p, this.getTopMost(p, p2));

		p.removeClaim(claim);

		p.wars.add(war);
		this.getTopMost(p, p2).wars.add(war);

		p.addHistory(Main.CCOLOR.YELLOW, p.getName() + " declares war on " + this.getTopMost(p, p2).getName()
				+ " for the province of " + claim.toString());
		this.getTopMost(p, p2).addHistory(Main.CCOLOR.YELLOW, this.getTopMost(p, p2).getName()
				+ " is declared war upon by " + p.getName() + " for the province of " + claim.toString());
	}

	public void setSub(Person new_lord, Person vassal) {
		// System.out.println(p.id + " - Setting Sub of " + s.id);

		if (new_lord.equals(vassal)) {
			System.err.println("Can't set sub of same person!");
			System.out.println(new_lord.getHistory());
			System.exit(0);
		} else {

			if (vassal.getLord() != null) {
				vassal.getLord().removeSub(vassal);
			}
			if (vassal.getRes() != null) {
				vassal.getRes().removeCourt(vassal);
			}

			vassal.setLord(new_lord);
		}
	}

	void releaseSubs(Person p) {
		p.addHistory(Main.CCOLOR.RED, p.getName() + " releases their vassals");
		// System.out.println(p.id + " - Releasing Vassals!");
		while (p.getSubSize() > 0) {
			Person s = p.getSubAt(0);
			s.setLord(p.getLord());
			if (p.containsSub(s))
				p.removeSub(s);
			/*
			 * if (p.getLord() != null) setSub(p.getLord(), s); else
			 * p.removeSub(p.getSubAt(0));
			 */
		}
	}

	private boolean inSameRealm(Person a, Person b) {
		if (a.getLord() == null && b.getLord() == null)
			return true;
		else if (a.getLord() == b.getLord())
			return true;
		else
			return false;
	}

	public void addDemesne(Person p, Title t, boolean inherit) {
		if (t.level != 0)
			System.err.println("Not a county!");

		if (p.isDead())
			System.err.println(p.id + " - Can't give territory to a dead man!");
		else {
			if (t.owner != null) {
				p.addHistory(Main.CCOLOR.GREEN,
						p.getName() + " gains the " + t.toString() + " (Former: " + t.owner.getName() + ")");

				Person f = t.owner;
				removeDemesne(t.owner, t);

				if (!f.hasTerritory() && !f.isDead()) {
					this.transferCourt(f, p);
					this.addToCourt(f, p);
				}
			} else {
				p.addHistory(Main.CCOLOR.GREEN, p.getName() + " gains the " + t.toString());
			}

			// System.out.println(p.id + " - Adding Province: " + t.id);

			t.owner = p;
			p.demesne.add(t);
			p.update_area = true;
			p.recalculateMain();

			if (!inherit) {
				Person curr = p;
				while (curr != null) {
					curr.checkRequirements(t);
					curr = curr.getLord();
				}
			}
		}
	}

	public void addDuchy(Person p, Title t, boolean inherit) {
		if (t == null) {
			System.err.println("ERROR:\tGiven title is null");
			System.exit(1);
		}
		if (t.level != 1)
			System.err.println("Not a duchy!");

		// System.out.println(p.id + " - Adding Duchy: " + t.id);

		Person oldp = null;
		if (p.duchies.contains(t))
			System.err.println(p.getName() + "Already has duchy " + t.toString() + ", " + t.owner.getName());
		else {
			p.duchies.add(t);
			if (p.main == null || p.main.level < 1)
				p.main = t;
		}

		if (t.owner != null) {
			// System.out.println("Pre-owned");
			oldp = t.owner;

			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " usurps " + t.toString() + " from " + oldp.getName());
			oldp.addHistory(Main.CCOLOR.RED, oldp.getName() + " has " + t.toString() + " usurped by " + p.getName());

			removeDuchy(t.owner, t);

			if (inherit) {
				List<Person> under = new ArrayList<Person>();
				for (int i = 0; i < oldp.getSubSize(); i++) {
					Person s = oldp.getSubAt(i);
					if (s.main == null && s.hasTerritory())
						s.recalculateMain();
					if (s.main != null && s.main.level < t.level)
						if (s != p && s.main.hasMaster(t))
							under.add(s);
				}
				for (int i = 0; i < under.size(); i++) {
					under.get(i).setLord(p);
				}

				List<Title> old_t = new ArrayList<Title>();
				old_t.addAll(oldp.demesne);
				for (int i = 0; i < old_t.size(); i++) {
					Title k = old_t.get(i);
					if (t.titles.contains(k))
						addDemesne(p, k, true);
				}
			}
		} else
			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " creates the " + t.toString());

		for (int i = 0; i < t.titles.size(); i++) {
			this.addClaimDemesne(p, t.titles.get(i));
		}

		t.owner = p;
	}

	public void transferCourt(Person a, Person b) {
		// System.out.println(a.id + " - Transferring Court to " + b.id);
		if (a.containsCourt(b)) {
			a.removeCourt(b);
		}

		while (!a.getCourtList().isEmpty()) {
			if (!a.getCourtAt(0).hasTerritory())
				this.addToCourt(a.getCourtAt(0), b);
			else {
				a.getCourtAt(0).setRes(null);
				// a.court.remove(0);
			}
		}
	}

	public void transferSub(Person p1, Person p2, Title t) {
		// System.out.println("Transferring Subs from " + p1.id + " to " + p2.id);

		p1.addHistory(Main.CCOLOR.BLUE, p1.getName() + " transfers their subs to " + p2.getName());
		p2.addHistory(Main.CCOLOR.BLUE, p2.getName() + " recieves subs from " + p1.getName());
		List<Person> subs = new ArrayList<Person>();

		for (int i = 0; i < p1.getSubSize(); i++) {
			Person p = p1.getSubAt(i);
			if (p != p2 && p.main != null)
				if (p.main.master == t)
					subs.add(p);
		}

		for (int i = 0; i < subs.size(); i++) {
			this.setSub(p2, subs.get(i));
		}
	}

	public void addKingdom(Person p, Title t, boolean inherit) {
		if (t.level != 2)
			System.err.println("Not a kingdom!");

		// System.out.println(p.toString() + " - Adding Kingdom: " + t.toString());

		Person oldp = null;
		if (p.kingdoms.contains(t))
			System.err.println(p.toString() + "Already has kingdom " + t.toString());
		else {
			p.kingdoms.add(t);
			if (p.main == null || p.main.level < 2)
				p.main = t;
		}

		if (t.owner != null) {
			// System.out.println("Pre-owned");
			oldp = t.owner;

			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " usurps " + t.toString() + " from " + oldp.getName());
			oldp.addHistory(Main.CCOLOR.RED, oldp.getName() + " has " + t.toString() + " usurped by " + p.getName());

			removeKingdom(t.owner, t);

			if (inherit) {
				List<Person> under = new ArrayList<Person>();
				for (int i = 0; i < oldp.getSubSize(); i++) {
					Person s = oldp.getSubAt(i);
					if (s != p && s.main.hasMaster(t))
						under.add(s);
				}
				for (int i = 0; i < under.size(); i++) {
					under.get(i).setLord(p);
				}

				List<Title> old_t = new ArrayList<Title>();
				old_t.addAll(oldp.duchies);
				for (int i = 0; i < old_t.size(); i++) {
					Title k = old_t.get(i);
					if (t.titles.contains(k))
						addDuchy(p, k, true);
				}
			}
		} else {
			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " creates the " + t.toString());
		}

		t.owner = p;
	}

	public void addEmpire(Person p, Title t, boolean inherit) {
		if (t.level != 3)
			System.err.println("Not an Empire!");

		// System.out.println(p.toString() + " - Adding Empire: " + t.toString());

		Person oldp = null;
		if (p.empires.contains(t))
			System.err.println(p.toString() + "Already has Empire " + t.toString());
		else {
			p.empires.add(t);
			if (p.main == null || p.main.level < 3)
				p.main = t;
		}

		if (t.owner != null) {
			// System.out.println("Pre-owned");
			oldp = t.owner;

			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " usurps " + t.toString() + " from " + oldp.getName());
			oldp.addHistory(Main.CCOLOR.RED, oldp.getName() + " has " + t.toString() + " usurped by " + p.getName());

			removeEmpire(t.owner, t);

			if (inherit) {
				List<Person> under = new ArrayList<Person>();
				for (int i = 0; i < oldp.getSubSize(); i++) {
					Person s = oldp.getSubAt(i);
					if (s != p && s.main.hasMaster(t))
						under.add(s);
				}
				for (int i = 0; i < under.size(); i++) {
					under.get(i).setLord(p);
				}

				List<Title> old_t = new ArrayList<Title>();
				old_t.addAll(oldp.kingdoms);
				for (int i = 0; i < old_t.size(); i++) {
					Title k = old_t.get(i);
					if (t.titles.contains(k))
						addKingdom(p, k, true);
				}
			}
		} else
			p.addHistory(Main.CCOLOR.GREEN, p.getName() + " creates the " + t.toString());

		t.owner = p;
	}

	public void removeDemesne(Person p, Title t) {
		// System.out.println(p.id + " - Removing County!");

		p.addHistory(Main.CCOLOR.RED, p.getName() + " loses the province: " + t.toString());

		p.demesne.remove(t);
		p.update_area = true;
		t.owner = null;

		if (p.main == t || p.capital == t) {
			p.recalculateMain();
		}
	}

	public void removeDuchy(Person p, Title t) {
		// System.out.println(p.getName() + " - Removing Duchy!" + p.duchies);
		p.duchies.remove(t);

		t.owner.addHistory(Main.CCOLOR.RED, t.owner.getName() + " loses the Duchy " + t.toString());
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	public void removeKingdom(Person p, Title t) {
		p.kingdoms.remove(t);
		t.owner.addHistory(Main.CCOLOR.RED, t.owner.getName() + " loses the Kingdom " + t.toString());
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	public void removeEmpire(Person p, Title t) {
		p.empires.remove(t);
		t.owner.addHistory(Main.CCOLOR.RED, t.owner.getName() + " loses the Empire " + t.toString());
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	private void GenerateCounties() {
		System.out.println("Generating County Map...");

		HashMap<Integer, sPolygon> Polygons = map.getPolygons();

		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if (poly.getHeight() < 4 && poly.isLand()) {
				Title t = new Title(null, poly, key);

				poly.title = t;

				counties.add(t);
			}
		}
	}

	private void GenerateDuchies() {
		System.out.println("Generating Duchy Map...");

		int[] xval = new int[DUCHY_AMOUNT * DUCHY_AMOUNT];
		int[] yval = new int[DUCHY_AMOUNT * DUCHY_AMOUNT];

		HashMap<Integer, List<Title>> to_be_added = new HashMap<Integer, List<Title>>();

		int amt = 0;
		int size = (GenerateMap.SIZE_X / this.DUCHY_AMOUNT);

		for (int i = 0; i < this.DUCHY_AMOUNT; i++) {
			for (int j = 0; j < this.DUCHY_AMOUNT; j++) {
				xval[amt] = (int) (i * size) + Main.rand.nextInt((int) (size / 2.0f));
				yval[amt] = (int) (j * size) + Main.rand.nextInt((int) (size / 2.0f));

				to_be_added.put(amt, new ArrayList<Title>());

				amt++;
			}
		}

		for (int i = 0; i < this.counties.size(); i++) {
			int shortest = 99999;
			int sp = 0;
			int x = (int) this.counties.get(i).getCenter().getX();
			int y = (int) this.counties.get(i).getCenter().getY();
			for (int j = 0; j < to_be_added.size(); j++) {
				int dist = (int) (Math.abs(x - xval[j]) + Math.abs(y - yval[j]));

				if (sp == 0 || dist < shortest) {
					shortest = dist;
					sp = j;
				}
			}

			to_be_added.get(sp).add(this.counties.get(i));
		}

		for (int i = 0; i < to_be_added.size(); i++) {
			if (to_be_added.containsKey(i))
				if (!to_be_added.get(i).isEmpty())
					this.duchies.add(new Title(null, to_be_added.get(i), 1, i));
		}
	}

	private void GenerateKingdoms() {
		System.out.println("Generating Kingdom Map...");

		int[] xval = new int[KINGDOM_AMOUNT * KINGDOM_AMOUNT];
		int[] yval = new int[KINGDOM_AMOUNT * KINGDOM_AMOUNT];

		HashMap<Integer, List<Title>> to_be_added = new HashMap<Integer, List<Title>>();

		int amt = 0;
		int size = (GenerateMap.SIZE_X / this.KINGDOM_AMOUNT);

		for (int i = 0; i < this.KINGDOM_AMOUNT; i++) {
			for (int j = 0; j < this.KINGDOM_AMOUNT; j++) {
				xval[amt] = (int) (i * size) + Main.rand.nextInt((int) (size / 2.0f));
				yval[amt] = (int) (j * size) + Main.rand.nextInt((int) (size / 2.0f));

				to_be_added.put(amt, new ArrayList<Title>());

				amt++;
			}
		}

		for (int i = 0; i < this.duchies.size(); i++) {
			int shortest = 99999;
			int sp = 0;
			int x = (int) this.duchies.get(i).getCenter().getX();
			int y = (int) this.duchies.get(i).getCenter().getY();
			for (int j = 0; j < to_be_added.size(); j++) {
				int dist = (int) (Math.abs(x - xval[j]) + Math.abs(y - yval[j]));

				if (sp == 0 || dist < shortest) {
					shortest = dist;
					sp = j;
				}
			}

			to_be_added.get(sp).add(this.duchies.get(i));
		}

		for (int i = 0; i < to_be_added.size(); i++) {
			if (to_be_added.containsKey(i))
				if (!to_be_added.get(i).isEmpty())
					this.kingdoms.add(new Title(null, to_be_added.get(i), 2, i));
		}
	}

	private void GenerateEmpires() {
		System.out.println("Generating Empire Map...");

		int[] xval = new int[EMPIRE_AMOUNT * EMPIRE_AMOUNT];
		int[] yval = new int[EMPIRE_AMOUNT * EMPIRE_AMOUNT];

		HashMap<Integer, List<Title>> to_be_added = new HashMap<Integer, List<Title>>();

		int amt = 0;
		int size = (GenerateMap.SIZE_X / this.EMPIRE_AMOUNT);

		for (int i = 0; i < this.EMPIRE_AMOUNT; i++) {
			for (int j = 0; j < this.EMPIRE_AMOUNT; j++) {
				xval[amt] = (int) (i * size) + Main.rand.nextInt((int) (size / 2.0f));
				yval[amt] = (int) (j * size) + Main.rand.nextInt((int) (size / 2.0f));

				to_be_added.put(amt, new ArrayList<Title>());

				amt++;
			}
		}

		for (int i = 0; i < this.kingdoms.size(); i++) {
			int shortest = 99999;
			int sp = 0;
			int x = (int) this.kingdoms.get(i).getCenter().getX();
			int y = (int) this.kingdoms.get(i).getCenter().getY();
			for (int j = 0; j < to_be_added.size(); j++) {
				int dist = (int) (Math.abs(x - xval[j]) + Math.abs(y - yval[j]));

				if (sp == 0 || dist < shortest) {
					shortest = dist;
					sp = j;
				}
			}

			to_be_added.get(sp).add(this.kingdoms.get(i));
		}

		for (int i = 0; i < to_be_added.size(); i++) {
			if (to_be_added.containsKey(i))
				if (!to_be_added.get(i).isEmpty())
					this.empires.add(new Title(new Language(Main.rand), to_be_added.get(i), 3, i));
		}
	}

	public void drawCounties(Graphics2D g2) {
		for (int i = 0; i < this.counties.size(); i++) {
			this.counties.get(i).draw(g2);
		}
	}

	public void drawLanguages(Graphics2D g2) {
		for (int i = 0; i < this.counties.size(); i++) {
			this.counties.get(i).drawLanguage(g2);
		}
	}

	public void drawDevelopment(Graphics2D g2) {
		for (int i = 0; i < this.counties.size(); i++) {
			this.counties.get(i).drawDevelopment(g2);
		}
	}

	public void drawDuchies(Graphics2D g2) {
		for (int i = 0; i < this.duchies.size(); i++) {
			this.duchies.get(i).draw(g2);
		}
	}

	public void drawKingdoms(Graphics2D g2) {
		for (int i = 0; i < this.kingdoms.size(); i++) {
			this.kingdoms.get(i).draw(g2);
		}
	}

	public void drawEmpires(Graphics2D g2) {
		for (int i = 0; i < this.empires.size(); i++) {
			this.empires.get(i).draw(g2);
		}
	}

	public Title FindCounty(sPolygon p) {
		for (int i = 0; i < this.counties.size(); i++) {
			if (this.counties.get(i).poly == p)
				return this.counties.get(i);
		}

		return null;
	}

	public Title FindDuchy(sPolygon p) {
		for (int i = 0; i < this.duchies.size(); i++) {
			if (this.duchies.get(i).containsProvince(p))
				return this.duchies.get(i);
		}

		return null;
	}

	public Title FindKingdom(sPolygon p) {
		for (int i = 0; i < this.kingdoms.size(); i++) {
			if (this.kingdoms.get(i).containsProvince(p))
				return this.kingdoms.get(i);
		}

		return null;
	}

	public Title FindEmpire(sPolygon p) {
		for (int i = 0; i < this.empires.size(); i++) {
			if (this.empires.get(i).containsProvince(p))
				return this.empires.get(i);
		}

		return null;
	}

	public void drawPeople(Graphics2D g2) {
		ArrayList<Person> temp = new ArrayList<Person>();
		temp.addAll(person_manager);
		for (Person p : temp) {
			if (p != null && p.getLord() == null)// && this.person_manager.get(i).hasTerritory())
				p.draw(g2, null, false);
		}
	}
}
