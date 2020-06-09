package sim;

import java.util.ArrayList;
import java.util.List;

import main.Main;

public class War {
	private Title claim;
	private Person a, b;
	private boolean done, deleted;
	private List<Title> occ_a;
	private List<Title> occ_b;
	private double war_score;

	public War(Title claim, Person a, Person b) {
		this.claim = claim;
		this.a = a;
		this.b = b;

		this.occ_a = new ArrayList<Title>();
		this.occ_b = new ArrayList<Title>();
	}

	public Person getInitiator() {
		return this.a;
	}

	public Person getVictim() {
		return this.b;
	}

	public boolean deadcheck() {
		if (!b.vassalHas(claim))
			return true;
		if (!this.a.hasTerritory() || !this.b.hasTerritory())
			return true;
		if (this.a.isDead() || this.b.isDead())
			return true;
		else
			return false;
	}

	/**
	 * Execute a war - unfinished
	 */
	public void execute(Person p) {
		// System.out.println("Executing War...");

		if (this.deadcheck())
			done = true;
		else if (a.capital != null && b.capital != null) {
			if (p.equals(a)) {
				if (occ_a.isEmpty()) {
					this.war_score += 0.005;
					Title t = b.getNearestUnoccupiedDemesne(a);
					if (t != null) {
						if (this.fight(a, b) == a) {
							t.fort--;
							if (t.fort == 0) {
								t.occupied = true;
								occ_b.add(t);
								t.development *= 0.9;
								this.war_score += + 1.0 / b.totalAreaSize();
							}
						} else if (t.fort < 5)
							t.fort++;
					}
				} else {
					this.war_score -= 0.005;
					Title t = a.getNearestOccupiedDemesne(a);
					if (t != null) {
						if (this.fight(a, b) == a) {
							t.fort++;
							if (t.fort == 5) {
								t.occupied = false;
								occ_a.remove(t);
								this.war_score += 1.0 / a.totalAreaSize();
							}
						} else if (t.fort > 0)
							t.fort--;
					}
				}
			} else if (p.equals(b)) {
				if (occ_b.isEmpty()) {
					this.war_score -= 0.005;
					Title t = a.getNearestUnoccupiedDemesne(b);
					if (t != null) {
						if (this.fight(b, a) == b) {
							t.fort--;
							if (t.fort == 0) {
								t.occupied = true;
								occ_a.add(t);
								t.development *= 0.9;
								this.war_score -= 1.0 / a.totalAreaSize();
							}
						} else if (t.fort < 5)
							t.fort++;
					}
				} else {
					this.war_score += 0.005;
					Title t = b.getNearestOccupiedDemesne(b);
					if (t != null) {
						if (this.fight(b, a) == b) {
							t.fort++;
							if (t.fort == 5) {
								t.occupied = false;
								occ_b.remove(t);
								this.war_score -= 1.0 / b.totalAreaSize();
							}
						} else if (t.fort > 0)
							t.fort--;
					}
				}
			}

			if (Math.abs(this.war_score) >= 0.75)// || a.levy == 0 || b.levy == 0)
				this.done = true;
		}
	}

	public Person fight(Person a, Person b) {
		int army_a = a.levy;
		int army_b = b.levy;

		if (army_a <= 0)
			return b;
		else if (army_b <= 0)
			return a;

		int loss = Main.rand.nextInt(2000);
		int die_a = Main.rand.nextInt(army_a);
		int die_b = Main.rand.nextInt(army_b);
		if (die_a > die_b) {
			a.levy = (int) (a.levy - loss * 0.1);
			b.levy = (int) (b.levy - loss * 0.3);
			return a;
		} else {
			a.levy = (int) (a.levy - loss * 0.3);
			b.levy = (int) (b.levy - loss * 0.1);
			return b;
		}
	}

	public void end() {
		// System.out.println("Ending War between " + a.getName() + " and " +
		// b.getName());

		if (this.deadcheck() == false) {
			if (this.war_score >= 0) {
				switch (claim.level) {
					case 0:
						a.time.addDemesne(a, claim, false);
						break;
					case 1:
						a.time.addDuchy(a, claim, true);
						break;
				}
			}
		}

		for (int i = 0; i < this.occ_a.size(); i++) {
			this.occ_a.get(i).occupied = false;
			this.occ_a.get(i).fort = 5;
		}

		for (int i = 0; i < this.occ_b.size(); i++) {
			this.occ_b.get(i).occupied = false;
			this.occ_b.get(i).fort = 5;
		}

		if (this.war_score >= 0) {
			a.addHistory(Main.CCOLOR.GREEN, "$ won an aggressive war against $", a, b);
			b.addHistory(Main.CCOLOR.RED, "$ lost a defensive war against $", b, a);
		} else {
			a.addHistory(Main.CCOLOR.YELLOW, "$ lost an aggressive war against $", a, b);
			b.addHistory(Main.CCOLOR.GREEN, "$ won a defensive war against $", b, a);
		}

		a.time.curr_wars.remove(this.claim);
		a.time.addTruce(a, b, 240);
	}

	public boolean equals(War war) {
		if (war.claim.equals(this.claim) && this.a.equals(war.a) && this.b.equals(war.b))
			return true;
		else {
			System.err.println("Not Same War");
			return false;
		}
	}

	public void delete() {
		// System.out.println("Deleting War...");

		if (a.wars.contains(this) && !a.wars.remove(this))
			System.err.println("Can't delete war!");
		if (b.wars.contains(this) && !b.wars.remove(this))
			System.err.println("Can't delete war!");

		this.deleted = true;
	}

	public Person getAttacker() {
		return a;
	}

	public Person getDefender() {
		return b;
	}

	public boolean getDone() {
		return done;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public double getWarScore() {
		return war_score;
	}
}
