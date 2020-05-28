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
	public List<Title> neighbors;
	public sPolygon poly;
	private int id;
	public Title master;
	public Color c;
	private Area a;
	public Person owner = null;
	public boolean occupied;
	private Language lang;
	public float development = 0;
	public int fort = 5;

	public Title(Language lang, sPolygon poly, int id) {
		this.lang = lang;
		this.level = 0;
		this.poly = poly;
		this.a = poly.getArea();
		this.neighbors = new ArrayList<Title>();
		this.id = id;

		this.c = Color.getHSBColor((float) Math.random(), 0.25f + ((float) Math.random() * 0.75f), 0.75f + ((float) Math.random() * 0.25f));
	}

	public Title(Language lang, List<Title> titles, int level, int id) {
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

			this.c = Color.getHSBColor((float) Math.random(), 0.25f + ((float) Math.random() * 0.75f), 0.75f + ((float) Math.random() * 0.25f));
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
		g2.setColor(Color.getHSBColor(0.5f, 1f - (float) Math.min(1f, development / 1000f), Math.min(1f, development / 1000f)));

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

		//if (p.getLord() == null || p.getLord().main.level > level) {
			int amt = 0;
			for (Title t : necessary) {
				if (counties.contains(t))
					amt++;
			}
			if (amt >= (necessary.size() / 2) + 1)
				return true;
		//}

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
		if (this.level == 0)
			return "Province " + lang.genName("Province", String.valueOf(id)).transpose();
		else if (this.level == 1)
			return "Duchy " +  lang.genName("Duchy", String.valueOf(id)).transpose();
		else if (this.level == 2)
			return "Kingdom " + lang.genName("Kingdom", String.valueOf(id)).transpose();
		else if (this.level == 3)
			return "Empire " + lang.genName("Empire", String.valueOf(id)).transpose();
		else
			return null;
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
}
