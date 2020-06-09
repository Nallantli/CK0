package sim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gen.sPolygon;
import ling.Language;

public class Title {
	/*
	 * LEVELS: 0 - County 1 - Duchy 2 - Kingdom 3 - Empire
	 */
	public int level;
	public List<Title> titles;
	private List<Title> neighbors;
	private List<Title> waterway_neighbors;
	List<Title> neighbor_cache;
	public sPolygon poly;
	public int id;
	public Title master;
	public Color c;
	private Area a;
	public Person owner = null;
	public boolean occupied;
	private Language lang;
	public double development = 0;
	public int fort = 5;
	public Island island;

	public Title(Language lang, sPolygon poly, int id) {
		this.lang = lang;
		this.level = 0;
		this.poly = poly;
		this.a = poly.getArea();
		this.neighbors = new ArrayList<Title>();
		this.waterway_neighbors = new ArrayList<Title>();
		this.id = id;

		this.c = Color.getHSBColor((float) Math.random(), 0.25f + ((float) Math.random() * 0.75f),
				0.75f + ((float) Math.random() * 0.25f));
	}

	public Title(Language lang, List<Title> titles, int level, int id) {
		this.neighbors = new ArrayList<Title>();
		this.lang = lang;
		if (level == 0) {
			System.err.println("Cannot invoke county using this constructor!");
		} else {
			this.level = level;
			this.titles = titles;
			this.id = id;

			for (int i = 0; i < titles.size(); i++) {
				if (titles.get(i).master != this)
					titles.get(i).master = this;
			}

			this.c = Color.getHSBColor((float) Math.random(), 0.25f + ((float) Math.random() * 0.75f),
					0.75f + ((float) Math.random() * 0.25f));
		}
	}

	public List<Title> getLandNeighbors() {
		return neighbors;
	}

	public List<Title> getSeaNeighbors() {
		return waterway_neighbors;
	}

	public List<Title> getNeighbors() {
		switch (level) {
			case 0:
				if (neighbor_cache == null) {
					neighbor_cache = new ArrayList<Title>();
					neighbor_cache.addAll(neighbors);
					neighbor_cache.addAll(waterway_neighbors);
				}
				return neighbor_cache;
			default:
				return neighbors;
		}
	}

	public boolean containsProvince(sPolygon p) {
		if (this.getPolygons().contains(p))
			return true;
		else
			return false;
	}

	public boolean containsTitle(Title t) {
		if (this.getAllTitles().contains(t))
			return true;
		else
			return false;
	}

	private List<Title> getAllTitles() {
		List<Title> t = new ArrayList<Title>();

		if (this.level == 0)
			return t;
		else {
			for (int i = 0; i < titles.size(); i++)
				t.addAll(titles.get(i).getAllTitles());
		}

		return t;
	}

	public List<sPolygon> getPolygons() {
		List<sPolygon> p = new ArrayList<sPolygon>();

		if (this.poly == null) {
			for (int i = 0; i < this.titles.size(); i++) {
				p.addAll(this.titles.get(i).getPolygons());
			}
		} else
			p.add(this.poly);

		return p;
	}

	public Area getArea() {
		if (this.a == null) {
			a = new Area();

			List<sPolygon> p = this.getPolygons();

			for (int i = 0; i < p.size(); i++) {
				a.add(p.get(i).getArea());
			}

			return a;
		} else {
			return a;
		}
	}

	public Point2D getCenter() {
		return new Point2D.Double(this.getArea().getBounds2D().getCenterX(), this.getArea().getBounds2D().getCenterY());
	}

	public void draw(Graphics2D g2) {
		g2.setColor(this.c);

		g2.fill(this.getArea());

		g2.setColor(Color.BLACK);

		if (this.level > 0) {
			for (int i = 0; i < this.titles.size(); i++) {
				g2.draw(this.titles.get(i).getArea());
			}
		}

		g2.draw(this.getArea());
	}

	public void drawLanguage(Graphics2D g2) {
		g2.setColor(getLanguage().c);

		g2.fill(this.getArea());

		/*
		 * g2.setColor(Color.BLACK);
		 * 
		 * if (this.level > 0) { for (int i = 0; i < this.titles.size(); i++) {
		 * g2.draw(this.titles.get(i).getArea()); } }
		 * 
		 * g2.draw(this.getArea());
		 */
	}

	public void drawDevelopment(Graphics2D g2) {
		g2.setColor(Color.getHSBColor(0.5f, 1f - (float) Math.min(1f, development / 1000f),
				Math.min(1f, (float) development / 1000f)));

		g2.fill(this.getArea());

		/*
		 * g2.setColor(Color.BLACK);
		 * 
		 * if (this.level > 0) { for (int i = 0; i < this.titles.size(); i++) {
		 * g2.draw(this.titles.get(i).getArea()); } }
		 * 
		 * g2.draw(this.getArea());
		 */
	}

	public boolean hasRequirements(Person p) {
		List<Title> counties = new ArrayList<Title>();
		counties.addAll(p.getAllSubDemesne());
		counties.addAll(p.demesne);

		List<Title> necessary = getLowest();

		// if (p.getLord() == null || p.getLord().main.level > level) {
		int amt = 0;
		for (Title t : necessary) {
			if (counties.contains(t))
				amt++;
		}
		double prop = (double) amt / (double) necessary.size();
		switch (level) {
			case 1:
			case 2:
				return prop >= 0.51;
			case 3:
				return prop >= 0.81;
		}
		// }

		return false;
	}

	public List<Title> getLowest() {
		List<Title> counties = new ArrayList<Title>();
		if (level == 0)
			counties.add(this);
		else {
			for (Title t : titles) {
				counties.addAll(t.getLowest());
			}
		}
		return counties;
	}

	public String getName(Language lang) {
		StringBuilder s = new StringBuilder();
		StringBuilder n = new StringBuilder();
		n.append(level);
		n.append(' ');
		n.append(id);
		switch (level) {
			case 0:
				s.append("Province ");
				break;
			case 1:
				s.append("Duchy ");
				break;
			case 2:
				s.append("Kingdom ");
				break;
			case 3:
				s.append("Empire ");
				break;
			default:
				return null;
		}		
		s.append(lang.getNoun(n.toString(), false).transpose());
		return s.toString();
	}

	public String toString() {
		return getName(getLanguage());
	}

	public Language getLanguage() {
		if (owner != null)
			return owner.lang;
		Title curr = this;
		while (curr.lang == null) {
			curr = curr.master;
		}
		return curr.lang;
	}

	public boolean hasMaster(Title t) {
		Title curr = master;
		while (curr != null) {
			if (curr == t)
				return true;
			curr = curr.master;
		}
		return false;
	}

	public void addLandNeighbor(Title title) {
		this.neighbors.add(title);
	}

	public void addSeaNeighbor(Title title) {
		this.waterway_neighbors.add(title);
	}
}
