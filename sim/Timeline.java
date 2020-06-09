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
import java.awt.Color;

public class Timeline {
	private final int DUCHY_SIZE_MIN = 4;
	private final int DUCHY_SIZE_MAX = 6;
	private final int KINGDOM_SIZE_MIN = 8;
	private final int KINGDOM_SIZE_MAX = 16;
	private final int EMPIRE_SIZE_MIN = 3;
	private final int EMPIRE_SIZE_MAX = 5;

	HashMap<Title, Title> county_map = new HashMap<Title, Title>();

	long time;
	GenerateMap map;
	List<Title> counties;
	List<Title> duchies;
	List<Title> kingdoms;
	List<Title> empires;
	List<Island> islands;
	List<Title> curr_wars;
	private HashMap<Title, List<Title>> total_waterways;
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
		this.islands = new ArrayList<Island>();
		this.curr_wars = new ArrayList<Title>();
		this.person_manager = new ArrayList<Person>();
		this.to_be_added = new ArrayList<Person>();
		this.total_waterways = new HashMap<Title, List<Title>>();

		this.GenerateCounties();
		this.setNeighbors();
		this.GenerateIslands();
		this.GenerateWaterways();
		this.GenerateDuchies();
		this.GenerateKingdoms();
		this.GenerateEmpires();

		this.setPeople();

		System.out.println("Total Counties: " + counties.size());
		// this.step();
	}

	public Person searchForSpouse(Person p) {
		double min_a = p.age - 20;
		double max_a = p.age + 5;

		List<Person> pl = new ArrayList<Person>();

		for (int i = 0; i < this.person_manager.size(); i++) {
			Person pp = this.person_manager.get(i);
			if (pp.getSpouse() == null && pp.age < max_a && pp.age > min_a && !pp.isDead() && pp.ismale != p.ismale
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
			System.out.println("Can't find spouse!");
			return null;
		} else
			return pl.get((int) (Math.pow((double) Main.rand.nextInt(pl.size()) / (double) pl.size(), 2)
					* (double) pl.size()));
	}

	public Person searchForSpouseLanguage(Person p) {
		double min_a = p.age - 20;
		double max_a = p.age + 5;

		List<Person> pl = new ArrayList<Person>();

		for (int i = 0; i < this.person_manager.size(); i++) {
			Person pp = this.person_manager.get(i);
			if (pp.getSpouse() == null && pp.age < max_a && pp.age > min_a && !pp.isDead() && pp.ismale != p.ismale
					&& p.lang == pp.lang && pp.dynasty != p.dynasty) {
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

			original_dynasties += 2;

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
				if (neighbors.get(j).isLand())
					this.counties.get(i).addLandNeighbor(neighbors.get(j).title);
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

			/*
			 * if (!p.getClaims().isEmpty()) { ArrayList<Title> new_list = new
			 * ArrayList<Title>(); for (int j = 0; j < p.getClaims().size(); j++) { Title t
			 * = p.getClaims().get(j); if (!p.demesne.contains(t)) new_list.add(t); }
			 * p.setClaims(new_list); }
			 */

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
							&& Main.rand.nextInt(200 + (int) (p.getLord().capital.development * 0.2)) < Math
									.abs(p.getLord().intelligence - p.intelligence)) {
						Person dumb = p.getLord();
						if (Main.rand.nextInt(3) == 0) {
							dumb.addHistory(Main.CCOLOR.RED, "$ is overrun in a coup d'état lead by $!", dumb, p);
							p.addHistory(Main.CCOLOR.GREEN, "$ launches a coup d'état against $!", p, dumb);

							while (!dumb.empires.isEmpty())
								addEmpire(p, dumb.empires.get(0), false);
							while (!dumb.kingdoms.isEmpty())
								addKingdom(p, dumb.kingdoms.get(0), false);
							while (!dumb.duchies.isEmpty())
								addDuchy(p, dumb.duchies.get(0), false);

							p.setLord(dumb.getLord());
							dumb.setLord(p);

							while (dumb.getSubSize() > 0)
								dumb.getSubAt(0).setLord(p);

							List<Title> demesne = new ArrayList<Title>();
							demesne.addAll(dumb.demesne);
							for (int j = 0; j < demesne.size(); j++)
								addDemesne(p, demesne.get(j), false);
							/*
							 * if (p.main.level >= dumb.main.level) { for (int j = 0; j < dumb.getSubSize();
							 * j++) { if (dumb.main.level <= dumb.getSubAt(j).main.level)
							 * dumb.getSubAt(j).setLord(p); } if (p.main.level > dumb.main.level)
							 * dumb.setLord(p); }
							 */
							p.coup = 240;
						} else {
							dumb.addHistory(Main.CCOLOR.GREEN, "$ crushes an attempted coup d'état lead by $!", dumb,
									p);
							p.addHistory(Main.CCOLOR.RED, "$ fails leading a coup d'état against $!", p, dumb);
							List<Title> demesne = new ArrayList<Title>();
							demesne.addAll(p.demesne);
							for (int j = 0; j < demesne.size(); j++)
								addDemesne(dumb, demesne.get(j), false);
							dumb.coup = 240;
						}
					}
				}

				if (p.getRes() != null) {
					p.setRes(null);
					// p.getRes().removeCourt(p);
				}

				if (p.getLord() != null) {
					if (p.lang != p.getLord().lang) {
						if (Main.rand.nextInt(300 * (p.main.level + 1)) == 0) {
							p.lang = p.getLord().lang;
							p.addHistory(Main.CCOLOR.MAGENTA,
									"$ converts to the culture of their lord $: " + p.lang.getName(), p, p.getLord());
							p.getLord().addHistory(Main.CCOLOR.MAGENTA,
									"$'s vassal $ converts to their lord's culture: " + p.lang.getName(), p.getLord(),
									p);
						}
					}
				}

				List<Title> new_claims = new ArrayList<Title>();

				for (int x = 0; x < p.getClaims().size(); x++) {
					Title c = p.getClaims().get(x);
					if ((c.owner != null && c.owner != p && !isVassalOf(c.owner, p))
							|| (c.owner == null && c.level > 0)) {
						new_claims.add(c);
					}
				}

				p.setClaims(new_claims);

				if (p.main != null && p.capital != null) {
					if ((p.main.level > 0 || p.demesne.size() < p.max_c + p.duchies.size())
							&& Main.rand.nextInt(300) == 0)
						this.searchForClaim(p);
				}

				if (Main.rand.nextInt(10) == 0 && !p.getClaims().isEmpty() && p.wars.isEmpty() && p.age > 16) {
					List<Title> claims = new ArrayList<Title>();

					for (int x = 0; x < p.getClaims().size(); x++) {
						Title c = p.getClaims().get(x);
						if (c.owner == null)
							continue;
						Person potential = this.getTopMost(p, c.owner);
						if ((this.inSameRealm(potential, p) || potential.getLord() == null) && potential.levy <= p.levy
								&& !isVassalOf(p, potential) && !isVassalOf(potential, p) && !this.curr_wars.contains(c)
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

				if (p.main != null && p.capital != null) {
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
					p.addHistory(Main.CCOLOR.CYAN,
							"$'s " + (p.getSpouse().ismale ? "husband" : "wife") + ", $, has passed away", p,
							p.getSpouse());
					p.setSpouse(null);
				}

			p.LessenTruces();
			double chance = Math.pow(p.age / 100.0, 9);

			if (Main.rand.nextDouble() < chance) {
				p.setDead(false);
				if (p.capital != null && p.capital.development > 750) {
					boolean success = this.inherit(p, null);
					if (!success && p.dynasty.getHighestMember() != null)
						this.inherit(p, p.dynasty.getHighestMember());
				} else {
					this.gavelkind(p);
				}
				p.die(false);
			} else
				p.age = p.age + 1.0 / 24.0;

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
					p.setRes(null);
					// p.getRes().removeCourt(p);
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
		HashMap<Title, Double> change = new HashMap<Title, Double>();
		for (int i = 0; i < counties.size(); i++) {
			Title t = counties.get(i);
			if (!t.occupied) {
				Title heighest = null;
				for (int j = 0; j < t.getNeighbors().size(); j++) {
					Title n = t.getNeighbors().get(j);
					if (heighest == null)
						heighest = n;
					else if (n.development > heighest.development) {
						heighest = n;
					}
				}
				if (heighest != null && heighest.development > t.development)
					change.put(t,
							(double) Math.max(0,
									Math.min(1000,
											t.development + Math.min(
													0.03 * (double) ((getHighestLord(t.owner).main.level + 1)
															* ((double) t.owner.intelligence / 20.0)),
													(heighest.development - t.development) / 3.0))));
			}
		}

		for (Map.Entry<Title, Double> e : change.entrySet())
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

			p.addHistory(Main.CCOLOR.BLUE, "$ dies and distributes their lands to " + children, p);
			for (int i = 0; i < children.size(); i++) {
				children.get(i).addHistory(Main.CCOLOR.BLUE,
						"$ inherits some of the lands of $ (including: " + children + ")", children.get(i), p);
				for (int j = 0; j < p.demesne.size(); j++)
					addClaim(children.get(i), p.demesne.get(j));
				for (int j = 0; j < p.duchies.size(); j++)
					addClaim(children.get(i), p.duchies.get(j));
				for (int j = 0; j < p.kingdoms.size(); j++)
					addClaim(children.get(i), p.kingdoms.get(j));
				for (int j = 0; j < p.empires.size(); j++)
					addClaim(children.get(i), p.empires.get(j));

				for (int j = 0; j < children.size(); j++) {
					if (j == i)
						continue;
					addTruce(children.get(i), children.get(j), 120);
				}
			}

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

			p.addHistory(Main.CCOLOR.BLUE, "$ dies and gives their lands to $", p, child);
			child.addHistory(Main.CCOLOR.BLUE, "$ inherits the lands of $", child, p);

			if (p.containsSub(child)) {
				child.setLord(null);
				// p.removeSub(child);
			}

			if (child.getRes() != null) {
				child.setRes(null);
				// child.getRes().removeCourt(child);
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
		long time = -1;
		for (int i = 0; i < s.getHistory().size(); i++) {
			if (time != s.getHistory().get(i).time)
				System.out.print("[" + s.getHistory().get(i).time + "]\t");
			else
				System.out.print("\t");
			System.out.println(s.getHistory().get(i));
			time = s.getHistory().get(i).time;
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
				a.setRes(null);
				// a.getRes().removeCourt(a);
			}
			if (a.getLord() != null) {
				a.setLord(null);
			}

			a.setRes(b);
		}
	}

	public void marry(Person a, Person b) {
		// System.out.println(a.getName() + " Marries " + b.getName());

		a.addHistory(Main.CCOLOR.CYAN, "$ marries $", a, b);
		b.addHistory(Main.CCOLOR.CYAN, "$ marries $", b, a);

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
		List<Title> total_d = new ArrayList<Title>();

		total_d.addAll(p.demesne);
		total_d.addAll(p.getAllSubDemesne());

		Title closest = null;
		for (int i = 0; i < total_d.size(); i++) {
			List<Title> neighbors = total_d.get(i).getNeighbors();

			for (int j = 0; j < neighbors.size(); j++) {
				Title t = neighbors.get(j);
				if (t.owner != null && !total_d.contains(t) && !p.getClaims().contains(t)) { // p.capital.master.master)
					if (closest == null)
						closest = t;
					else if (closest.getCenter().distance(p.capital.getCenter()) > t.getCenter()
							.distance(p.capital.getCenter()))
						closest = t;
				}
			}
		}

		if (closest != null) {
			if (p.main.level > 0 && Main.rand.nextInt(10) == 0 && closest.master.owner != null
					&& closest.master.owner != p)
				this.addClaim(p, closest.master);
			else
				this.addClaim(p, closest);
		}
	}

	public void addTruce(Person a, Person b, int time) {
		a.truces.put(b, time);
		b.truces.put(a, time);
	}

	public boolean isVassalOf(Person vassal, Person lord) {
		Person curr = vassal.getLord();
		while (curr != null) {
			if (curr == lord)
				return true;
			curr = curr.getLord();
		}
		return false;
	}

	public void addClaim(Person p, Title claim) {
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
		Person def = this.getTopMost(p, p2);

		War war = new War(claim, p, def);

		p.removeClaim(claim);

		p.wars.add(war);
		def.wars.add(war);

		p.addHistory(Main.CCOLOR.YELLOW, "$ declares war on $ for the province of " + claim.toString(), p, def);
		def.addHistory(Main.CCOLOR.YELLOW, "$ is declared war upon by $ for the province of " + claim.toString(), def,
				p);
	}

	public void setSub(Person new_lord, Person vassal) {
		// System.out.println(p.id + " - Setting Sub of " + s.id);

		if (new_lord.equals(vassal)) {
			System.err.println("Can't set sub of same person!");
			System.out.println(new_lord.getHistory());
			System.exit(0);
		} else {

			/*
			 * if (vassal.getLord() != null) { vassal.setLord(null);
			 * //vassal.getLord().removeSub(vassal); }
			 */
			if (vassal.getRes() != null) {
				vassal.setRes(null);
				// vassal.getRes().removeCourt(vassal);
			}

			vassal.setLord(new_lord);
		}
	}

	void releaseSubs(Person p) {
		p.addHistory(Main.CCOLOR.RED, "$ releases their vassals!", p);
		// System.out.println(p.id + " - Releasing Vassals!");
		while (p.getSubSize() > 0) {
			Person s = p.getSubAt(0);
			s.setLord(p.getLord());
			/*
			 * if (p.containsSub(s)) p.removeSub(s);
			 */
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
				p.addHistory(Main.CCOLOR.GREEN, "$ gains the " + t.toString() + " (Former: $)", p, t.owner);

				Person f = t.owner;
				removeDemesne(t.owner, t);

				if (!f.hasTerritory() && !f.isDead()) {
					this.transferCourt(f, p);
					this.addToCourt(f, p);
				}
			} else {
				p.addHistory(Main.CCOLOR.GREEN, "$ gains the " + t.toString(), p);
			}

			// System.out.println(p.id + " - Adding Province: " + t.id);

			t.owner = p;
			p.demesne.add(t);
			p.update_area = true;
			p.recalculateMain();

			if (p.hasClaim(t))
				p.removeClaim(t);

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
			System.err.println(p.getName() + " already has duchy " + t.toString() + ", " + t.owner.getName());
		else {
			p.duchies.add(t);
			if (p.main == null || p.main.level < 1)
				p.main = t;
		}

		if (t.owner != null) {
			// System.out.println("Pre-owned");
			oldp = t.owner;

			p.addHistory(Main.CCOLOR.GREEN, "$ usurps the " + t.toString() + " from $", p, oldp);
			oldp.addHistory(Main.CCOLOR.RED, "$ has the " + t.toString() + " usurped by $", oldp, p);

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
			p.addHistory(Main.CCOLOR.GREEN, "$ creates the " + t.toString(), p);

		for (int i = 0; i < t.titles.size(); i++) {
			this.addClaim(p, t.titles.get(i));
		}

		if (p.hasClaim(t))
			p.removeClaim(t);
		t.owner = p;
	}

	public void transferCourt(Person a, Person b) {
		// System.out.println(a.id + " - Transferring Court to " + b.id);
		if (a.containsCourt(b)) {
			// a.removeCourt(b);
			b.setRes(null);
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

		p1.addHistory(Main.CCOLOR.BLUE, "$ transfers their subs to $", p1, p2);
		p2.addHistory(Main.CCOLOR.BLUE, "$ recieves subs from $", p2, p1);
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

			p.addHistory(Main.CCOLOR.GREEN, "$ usurps the " + t.toString() + " from $", p, oldp);
			oldp.addHistory(Main.CCOLOR.RED, "$ has the " + t.toString() + " usurped by $", oldp, p);

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
			p.addHistory(Main.CCOLOR.GREEN, "$ creates the " + t.toString(), p);
		}

		if (p.hasClaim(t))
			p.removeClaim(t);
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

			p.addHistory(Main.CCOLOR.GREEN, "$ usurps the " + t.toString() + " from $", p, oldp);
			oldp.addHistory(Main.CCOLOR.RED, "$ has the " + t.toString() + " usurped by $", oldp, p);

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
			p.addHistory(Main.CCOLOR.GREEN, "$ creates the " + t.toString(), p);

		if (p.hasClaim(t))
			p.removeClaim(t);
		t.owner = p;
	}

	public void removeDemesne(Person p, Title t) {
		// System.out.println(p.id + " - Removing County!");

		p.addHistory(Main.CCOLOR.RED, "$ loses the province: " + t.toString(), p);

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
		t.owner.addHistory(Main.CCOLOR.RED, "$ loses the " + t.toString(), t.owner);
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	public void removeKingdom(Person p, Title t) {
		p.kingdoms.remove(t);
		t.owner.addHistory(Main.CCOLOR.RED, "$ loses the " + t.toString(), t.owner);
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	public void removeEmpire(Person p, Title t) {
		p.empires.remove(t);
		t.owner.addHistory(Main.CCOLOR.RED, "$ loses the " + t.toString(), t.owner);
		t.owner = null;

		if (p.main == t) {
			p.recalculateMain();
		}
	}

	private void GenerateIslands() {
		System.out.println("Generating Island Map...");
		List<Title> open_set = new ArrayList<Title>();
		open_set.addAll(counties);
		int i = 0;
		while (!open_set.isEmpty()) {
			Title root = open_set.get(0);
			if (root.island == null) {
				root.island = new Island(i++);
				root.island.territory.add(root);
				islands.add(root.island);
			}

			List<Title> novelties = new ArrayList<Title>();
			novelties.add(root);

			while (!novelties.isEmpty()) {
				Title curr = novelties.get(Main.rand.nextInt(novelties.size()));
				novelties.remove(curr);
				open_set.remove(curr);

				List<Title> neighbors = curr.getLandNeighbors();
				for (Title n : neighbors) {
					if (open_set.contains(n)) {
						n.island = root.island;
						root.island.territory.add(n);
						novelties.add(n);
					}
				}
			}
		}
	}

	private void GenerateWaterways() {
		System.out.println("Generating Waterways...");
		List<String> searched = new ArrayList<String>();
		for (Island i1 : islands) {
			System.out.println("Island A " + i1.id + "...");
			for (Island i2 : islands) {
				if (i1 == i2)
					continue;
				if (searched.contains(i1.id + "," + i2.id))
					continue;
				searched.add(i1.id + "," + i2.id);
				searched.add(i2.id + "," + i1.id);
				System.out.println("Island B " + i2.id + "...");
				Title[] closest = i1.getClosest(i2, islands);
				if (closest[0] != null && closest[0].getCenter().distance(closest[1].getCenter()) < 50) {
					closest[0].addSeaNeighbor(closest[1]);
					closest[1].addSeaNeighbor(closest[0]);
					if (!total_waterways.containsKey(closest[0]))
						total_waterways.put(closest[0], new ArrayList<Title>());
					if (!total_waterways.containsKey(closest[1]))
						total_waterways.put(closest[1], new ArrayList<Title>());
					total_waterways.get(closest[0]).add(closest[1]);
					total_waterways.get(closest[1]).add(closest[0]);
					System.out.println("Waterway: " + closest[0].id + " to " + closest[1].id);
				}
			}
		}
	}

	private void GenerateCounties() {
		System.out.println("Generating County Map...");

		HashMap<Integer, sPolygon> Polygons = map.getPolygons();

		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if (poly.isLand()) {
				Title t = new Title(null, poly, key);

				poly.title = t;

				counties.add(t);
			}
		}
	}

	private void GenerateDuchies() {
		System.out.println("Generating Duchy Map...");

		ArrayList<Title> temp_c = new ArrayList<Title>();
		ArrayList<Title> single_d = new ArrayList<Title>();
		temp_c.addAll(counties);

		int c = 0;
		while (!temp_c.isEmpty()) {
			Title root = temp_c.get(Main.rand.nextInt(temp_c.size()));
			temp_c.remove(root);
			ArrayList<Title> inner = new ArrayList<Title>();
			inner.add(root);
			int size = Main.rand.nextInt(DUCHY_SIZE_MAX - DUCHY_SIZE_MIN) + DUCHY_SIZE_MIN;
			for (int i = 0; i < size; i++) {
				ArrayList<Title> possible = new ArrayList<Title>();
				for (int j = 0; j < inner.size(); j++) {
					for (int k = 0; k < inner.get(j).getNeighbors().size(); k++) {
						if (temp_c.contains(inner.get(j).getNeighbors().get(k))) {
							possible.add(inner.get(j).getNeighbors().get(k));
						}
					}
				}
				if (possible.isEmpty())
					break;
				Title newt = null;
				for (int j = 0; j < possible.size(); j++) {
					if (newt == null)
						newt = possible.get(j);
					else if (newt.getCenter().distance(root.getCenter()) > possible.get(j).getCenter()
							.distance(root.getCenter()))
						newt = possible.get(j);
				}
				temp_c.remove(newt);
				inner.add(newt);
			}
			if (inner.size() > 1)
				duchies.add(new Title(null, inner, 1, c++));
			else
				single_d.add(inner.get(0));
		}

		for (int i = 0; i < single_d.size(); i++) {
			ArrayList<Title> neighbor_duchies = new ArrayList<Title>();
			for (int j = 0; j < single_d.get(i).getNeighbors().size(); j++) {
				Title d = single_d.get(i).getNeighbors().get(j).master;
				if (d != null && !neighbor_duchies.contains(d))
					neighbor_duchies.add(d);
			}
			Title closest = null;
			for (int j = 0; j < neighbor_duchies.size(); j++) {
				if (closest == null)
					closest = neighbor_duchies.get(j);
				else if (closest.titles.size() > neighbor_duchies.get(j).titles.size())
					closest = neighbor_duchies.get(j);
			}
			if (closest == null) {
				ArrayList<Title> temp = new ArrayList<Title>();
				temp.add(single_d.get(i));
				duchies.add(new Title(null, temp, 1, c++));
			} else {
				closest.titles.add(single_d.get(i));
				single_d.get(i).master = closest;
			}
		}

		for (int i = 0; i < duchies.size(); i++) {
			Title t = duchies.get(i);
			ArrayList<Title> p_n = new ArrayList<Title>();
			for (int j = 0; j < t.titles.size(); j++) {
				for (int k = 0; k < t.titles.get(j).getNeighbors().size(); k++) {
					Title p = t.titles.get(j).getNeighbors().get(k);
					if (p.master != t)
						p_n.add(p);
				}
			}
			for (int j = 0; j < p_n.size(); j++) {
				if (!t.getNeighbors().contains(p_n.get(j).master))
					t.addLandNeighbor(p_n.get(j).master);
			}
		}
	}

	private void GenerateKingdoms() {
		System.out.println("Generating Kingdom Map...");

		ArrayList<Title> temp_d = new ArrayList<Title>();
		ArrayList<Title> single_k = new ArrayList<Title>();
		temp_d.addAll(duchies);

		int c = 0;
		while (!temp_d.isEmpty()) {
			Title root = temp_d.get(Main.rand.nextInt(temp_d.size()));
			temp_d.remove(root);
			ArrayList<Title> inner = new ArrayList<Title>();
			inner.add(root);
			int size = Main.rand.nextInt(KINGDOM_SIZE_MAX - KINGDOM_SIZE_MIN) + KINGDOM_SIZE_MIN;
			for (int i = 0; i < size; i++) {
				ArrayList<Title> possible = new ArrayList<Title>();
				for (int j = 0; j < inner.size(); j++) {
					for (int k = 0; k < inner.get(j).getNeighbors().size(); k++) {
						if (temp_d.contains(inner.get(j).getNeighbors().get(k))) {
							possible.add(inner.get(j).getNeighbors().get(k));
						}
					}
				}
				if (possible.isEmpty())
					break;
				Title newt = null;
				for (int j = 0; j < possible.size(); j++) {
					if (newt == null)
						newt = possible.get(j);
					else if (newt.getCenter().distance(root.getCenter()) > possible.get(j).getCenter()
							.distance(root.getCenter()))
						newt = possible.get(j);
				}
				temp_d.remove(newt);
				inner.add(newt);
			}
			if (inner.size() > 1)
				kingdoms.add(new Title(null, inner, 2, c++));
			else
				single_k.add(inner.get(0));
		}

		for (int i = 0; i < single_k.size(); i++) {
			ArrayList<Title> neighbor_kingdoms = new ArrayList<Title>();
			for (int j = 0; j < single_k.get(i).getNeighbors().size(); j++) {
				Title k = single_k.get(i).getNeighbors().get(j).master;
				if (k != null && !neighbor_kingdoms.contains(k))
					neighbor_kingdoms.add(k);
			}
			Title closest = null;
			for (int j = 0; j < neighbor_kingdoms.size(); j++) {
				if (closest == null)
					closest = neighbor_kingdoms.get(j);
				else if (closest.titles.size() > neighbor_kingdoms.get(j).titles.size())
					closest = neighbor_kingdoms.get(j);
			}
			if (closest == null) {
				ArrayList<Title> temp = new ArrayList<Title>();
				temp.add(single_k.get(i));
				kingdoms.add(new Title(null, temp, 2, c++));
			} else {
				closest.titles.add(single_k.get(i));
				single_k.get(i).master = closest;
			}
		}

		for (int i = 0; i < kingdoms.size(); i++) {
			Title t = kingdoms.get(i);
			ArrayList<Title> p_n = new ArrayList<Title>();
			for (int j = 0; j < t.titles.size(); j++) {
				for (int k = 0; k < t.titles.get(j).getNeighbors().size(); k++) {
					Title p = t.titles.get(j).getNeighbors().get(k);
					if (p.master != t)
						p_n.add(p);
				}
			}
			for (int j = 0; j < p_n.size(); j++) {
				if (!t.getNeighbors().contains(p_n.get(j).master))
					t.addLandNeighbor(p_n.get(j).master);
			}
		}
	}

	private void GenerateEmpires() {
		System.out.println("Generating Empire Map...");

		ArrayList<Title> temp_k = new ArrayList<Title>();
		ArrayList<Title> single_e = new ArrayList<Title>();
		temp_k.addAll(kingdoms);

		int c = 0;
		while (!temp_k.isEmpty()) {
			Title root = temp_k.get(Main.rand.nextInt(temp_k.size()));
			temp_k.remove(root);
			ArrayList<Title> inner = new ArrayList<Title>();
			inner.add(root);
			int size = Main.rand.nextInt(EMPIRE_SIZE_MAX - EMPIRE_SIZE_MIN) + EMPIRE_SIZE_MIN;
			for (int i = 0; i < size; i++) {
				ArrayList<Title> possible = new ArrayList<Title>();
				for (int j = 0; j < inner.size(); j++) {
					for (int k = 0; k < inner.get(j).getNeighbors().size(); k++) {
						if (temp_k.contains(inner.get(j).getNeighbors().get(k))) {
							possible.add(inner.get(j).getNeighbors().get(k));
						}
					}
				}
				if (possible.isEmpty())
					break;
				Title newt = null;
				for (int j = 0; j < possible.size(); j++) {
					if (newt == null)
						newt = possible.get(j);
					else if (newt.getCenter().distance(root.getCenter()) > possible.get(j).getCenter()
							.distance(root.getCenter()))
						newt = possible.get(j);
				}
				temp_k.remove(newt);
				inner.add(newt);
			}
			if (inner.size() > 1)
				empires.add(new Title(new Language(Main.rand), inner, 3, c++));
			else
				single_e.add(inner.get(0));
		}

		for (int i = 0; i < single_e.size(); i++) {
			ArrayList<Title> neighbor_empires = new ArrayList<Title>();
			for (int j = 0; j < single_e.get(i).getNeighbors().size(); j++) {
				Title e = single_e.get(i).getNeighbors().get(j).master;
				if (e != null && !neighbor_empires.contains(e))
					neighbor_empires.add(e);
			}
			Title closest = null;
			for (int j = 0; j < neighbor_empires.size(); j++) {
				if (closest == null)
					closest = neighbor_empires.get(j);
				else if (closest.titles.size() > neighbor_empires.get(j).titles.size())
					closest = neighbor_empires.get(j);
			}
			if (closest == null) {
				ArrayList<Title> temp = new ArrayList<Title>();
				temp.add(single_e.get(i));
				empires.add(new Title(new Language(Main.rand), temp, 3, c++));
			} else {
				closest.titles.add(single_e.get(i));
				single_e.get(i).master = closest;
			}
		}

		for (int i = 0; i < empires.size(); i++) {
			Title t = empires.get(i);
			ArrayList<Title> p_n = new ArrayList<Title>();
			for (int j = 0; j < t.titles.size(); j++) {
				for (int k = 0; k < t.titles.get(j).getNeighbors().size(); k++) {
					Title p = t.titles.get(j).getNeighbors().get(k);
					if (p.master != t)
						p_n.add(p);
				}
			}
			for (int j = 0; j < p_n.size(); j++) {
				if (!t.getNeighbors().contains(p_n.get(j).master))
					t.addLandNeighbor(p_n.get(j).master);
			}
		}
	}

	public void drawCounties(Graphics2D g2) {
		for (int i = 0; i < this.counties.size(); i++) {
			this.counties.get(i).draw(g2);
		}
	}

	public void drawIslands(Graphics2D g2) {
		for (int i = 0; i < this.islands.size(); i++) {
			this.islands.get(i).draw(g2);
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

	public void drawWaterways(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.setStroke(Display.dashed_light);
		for (Map.Entry<Title, List<Title>> e : total_waterways.entrySet()) {
			for (int i = 0; i < e.getValue().size(); i++) {
				if (e.getKey().id > e.getValue().get(i).id)
					g2.drawLine((int) e.getKey().getCenter().getX(), (int) e.getKey().getCenter().getY(),
							(int) e.getValue().get(i).getCenter().getX(), (int) e.getValue().get(i).getCenter().getY());
			}
		}
		g2.setStroke(Display.default_stroke);
	}

	public void drawPeople(Graphics2D g2) {
		ArrayList<Person> temp = new ArrayList<Person>();
		temp.addAll(person_manager);
		for (Person p : temp) {
			if (p != null && p.getLord() == null) {
				try {
					p.draw(g2, null, false);
				} catch (NullPointerException e) {
					System.err.println("getArea() returned null for " + p);
				}
			}
		}
	}
}
