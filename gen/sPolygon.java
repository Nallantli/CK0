package gen;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import sim.Person;
import sim.Title;

public class sPolygon {
	List<Line2D> lines = new ArrayList<Line2D>();
	public List<sPolygon> neighbors = new ArrayList<sPolygon>();
	Point2D.Double center;
	Color color;
	private boolean land = false;
	boolean border = false;
	boolean inlandlake = false;
	public int location_id;
	float height = 0, moisture = 0;
	float c;
	int plate = -1;
	List<Person> residents;
	Person owner = null;
	public Title title;

	public sPolygon(Point2D.Double center) {
		center.setLocation(Math.floor(center.getX()), Math.floor(center.getY()));
		this.center = center;
	}

	public Point2D getCenter() {
		return center;
	}

	public void addNeighbor(sPolygon poly) {
		this.neighbors.add(poly);
	}

	public void addLine(Line2D line) {
		Line2D line2 = new Line2D.Double(Math.floor(line.getX1()), Math.floor(line.getY1()), Math.floor(line.getX2()),
				Math.floor(line.getY2()));
		this.lines.add(line2);
	}

	public Area getArea() {
		Area a = new Area();

		List<Polygon> tri = this.getTriangles();
		for (int j = 0; j < tri.size(); j++) {
			a.add(new Area(tri.get(j)));
		}

		return a;
	}

	Color[][] BIOME_COLORS = new Color[5][6];

	public static Color BEACH = new Color(172, 159, 139);
	public static Color WATER = new Color(54, 54, 97);
	public static Color LAKE = new Color(85, 125, 166);

	public static Color SUBTROPICAL_DESERT = new Color(233, 221, 199);
	public static Color ARID_DESERT = new Color(230, 222, 148);
	public static Color CANYONS = new Color(255, 215, 162);
	public static Color GRASSLAND = new Color(196, 212, 170);
	public static Color PLAINS = new Color(221, 230, 154);
	public static Color TROPICAL_SEASONAL_FOREST = new Color(169, 204, 164);
	public static Color TROPICAL_RAIN_FOREST = new Color(156, 187, 169);
	public static Color MARSH = new Color(102, 155, 105);
	public static Color SAVANNA = new Color(228, 232, 202);
	public static Color TEMPERATE_DECIDUOUS_FOREST = new Color(180, 201, 169);
	public static Color TEMPERATE_RAIN_FOREST = new Color(164, 196, 168);
	public static Color SHRUBLAND = new Color(196, 204, 187);
	public static Color TAIGA = new Color(204, 212, 187);
	public static Color SCORCHED = new Color(153, 153, 153);
	public static Color BARE = new Color(187, 187, 187);
	public static Color TUNDRA = new Color(221, 221, 187);
	public static Color BOREAL_FOREST = new Color(213, 221, 213);
	public static Color SNOW = new Color(248, 248, 248);
	public static Color FROZEN = new Color(217, 251, 255);

	String[][] names = new String[5][6];

	public String getBiomeName() {
		if (this.inlandlake)
			return "Lake";
		else if (this.land == false)
			return "Ocean";
		// else if (GenerateMap.bordersOcean(this))
		// return "Coast";

		names[0][0] = "Coach";
		names[0][1] = "Desert";
		names[0][2] = "Savanna";
		names[0][3] = "Seasonal Forest";
		names[0][4] = "Tropical Rain Forest";
		names[0][5] = "Marshes";

		names[1][0] = "Desert";
		names[1][1] = "Plains";
		names[1][2] = "Plains";
		names[1][3] = "Forest";
		names[1][4] = "Forest";
		names[1][5] = "Rain Forest";

		names[2][0] = "Arid Desert";
		names[2][1] = "Plains";
		names[2][2] = "Grassland";
		names[2][3] = "Grassland";
		names[2][4] = "Forest";
		names[2][5] = "Rain Forest";

		names[3][0] = "Canyons";
		names[3][1] = "Plains";
		names[3][2] = "Shrublands";
		names[3][3] = "Shrublands";
		names[3][4] = "Taiga";
		names[3][5] = "Boreal Forest";

		names[4][0] = "Scorched Lands";
		names[4][1] = "Barren Fields";
		names[4][2] = "Tundra";
		names[4][3] = "Ice Cap";
		names[4][4] = "Ice Cap";
		names[4][5] = "Frozen Waters";

		return names[getHeight()][getMoisture()];
	}

	public Color getColor() {
		BIOME_COLORS[0][0] = BEACH;
		BIOME_COLORS[0][1] = SUBTROPICAL_DESERT;
		BIOME_COLORS[0][2] = SAVANNA;
		BIOME_COLORS[0][3] = TROPICAL_SEASONAL_FOREST;
		BIOME_COLORS[0][4] = TROPICAL_RAIN_FOREST;
		BIOME_COLORS[0][5] = MARSH;

		BIOME_COLORS[1][0] = SUBTROPICAL_DESERT;
		BIOME_COLORS[1][1] = PLAINS;
		BIOME_COLORS[1][2] = PLAINS;
		BIOME_COLORS[1][3] = TEMPERATE_DECIDUOUS_FOREST;
		BIOME_COLORS[1][4] = TEMPERATE_DECIDUOUS_FOREST;
		BIOME_COLORS[1][5] = TEMPERATE_RAIN_FOREST;

		BIOME_COLORS[2][0] = ARID_DESERT;
		BIOME_COLORS[2][1] = PLAINS;
		BIOME_COLORS[2][2] = GRASSLAND;
		BIOME_COLORS[2][3] = GRASSLAND;
		BIOME_COLORS[2][4] = TEMPERATE_DECIDUOUS_FOREST;
		BIOME_COLORS[2][5] = TEMPERATE_RAIN_FOREST;

		BIOME_COLORS[3][0] = CANYONS;
		BIOME_COLORS[3][1] = PLAINS;
		BIOME_COLORS[3][2] = SHRUBLAND;
		BIOME_COLORS[3][3] = SHRUBLAND;
		BIOME_COLORS[3][4] = TAIGA;
		BIOME_COLORS[3][5] = BOREAL_FOREST;

		BIOME_COLORS[4][0] = SCORCHED;
		BIOME_COLORS[4][1] = BARE;
		BIOME_COLORS[4][2] = TUNDRA;
		BIOME_COLORS[4][3] = SNOW;
		BIOME_COLORS[4][4] = SNOW;
		BIOME_COLORS[4][5] = FROZEN;

		if (this.land) {
			// if (GenerateMap.bordersOcean(this))
			// return BEACH;
			// else
			// {
			// return new Color(getMoisture()/6.0f, getMoisture()/6.0f, getMoisture()/6.0f);
			return BIOME_COLORS[getHeight()][getMoisture()];
			// }
		} else if (this.inlandlake == true)
			return LAKE;
		else if (this.border == true)
			return WATER;
		else
			return WATER;
	}

	int POWER = 2;

	public int getHeight() {
		if (this.inlandlake)
			return 0;

		int h = (int) Math
				.floor(Math.pow(1.0 - (this.height - GenerateMap.min_height) / (GenerateMap.max_height), POWER) * 5.0);

		if (h == 5)
			h = 4;
		if (h == -1)
			h = 0;

		// System.out.println(h);

		return h;
	}

	public int getMoisture() {
		int m = (int) Math
				.floor(Math.pow(1.0 - (this.moisture - GenerateMap.min_moisture) / GenerateMap.max_moisture, 3) * 6.0);

		if (m == 6)
			m = 5;

		return m;
	}

	public List<Line2D> getLines() {
		// return lines;

		return lines;
	}

	public List<Line2D> getTriangleLines() {
		List<Point2D> points = getPoint2Ds();
		List<Line2D> lines = new ArrayList<Line2D>();

		for (int i = 0; i < points.size(); i++) {
			Point2D point1 = points.get(i);

			lines.add(new Line2D.Double(center, point1));
		}

		return lines;

	}

	public List<Polygon> getTriangles() {
		// System.out.println("Getting Triangles");
		List<Point2D> points = getPoint2Ds();
		List<Polygon> polygons = new ArrayList<Polygon>();
		List<Line2D> tri_lines = getTriangleLines();

		for (int i = 0; i < tri_lines.size(); i++) {
			Point2D point1 = tri_lines.get(i).getP2();

			List<Point2D> near_points = findNearestPoint2Ds(points, point1);

			if (!near_points.isEmpty()) {
				List<Point2D> possible = new ArrayList<Point2D>();
				for (int j = 0; j < near_points.size(); j++) {
					Point2D point2 = near_points.get(j);

					if (!this.bisects(tri_lines, new Line2D.Double(point1, point2)))
						possible.add(point2);
				}

				// System.out.println(possible);
				for (int k = 0; k < possible.size(); k++) {
					Point2D point2 = possible.get(k);

					Polygon poly = new Polygon();
					poly.addPoint((int) center.x, (int) center.y);
					poly.addPoint((int) point1.getX(), (int) point1.getY());
					poly.addPoint((int) point2.getX(), (int) point2.getY());

					if (PolygonExists(polygons, poly) == false)
						polygons.add(poly);
				}
			}
		}

		// System.out.println(polygons.size());

		return polygons;
	}

	public boolean PolygonExists(List<Polygon> polygons, Polygon poly) {
		for (int i = 0; i < polygons.size(); i++) {
			if (poly.getBounds2D().getWidth() == polygons.get(i).getBounds2D().getWidth()
					&& poly.getBounds2D().getHeight() == polygons.get(i).getBounds2D().getHeight()
					&& poly.getBounds2D().getX() == polygons.get(i).getBounds2D().getX()
					&& poly.getBounds2D().getY() == polygons.get(i).getBounds2D().getY()
					&& poly.toString().equals(polygons.get(i).toString())) {
				// System.out.println("True");
				return true;
			}
		}

		return false;
	}

	public boolean PolygonEqual(final Polygon p1, final Polygon p2) {
		if (p1 == null) {
			return (p2 == null);
		}
		if (p2 == null) {
			return false;
		}
		if (p1.npoints != p2.npoints) {
			return false;
		}
		if (!Arrays.equals(p1.xpoints, p2.xpoints)) {
			return false;
		}
		if (!Arrays.equals(p1.ypoints, p2.ypoints)) {
			return false;
		}
		return true;
	}

	public List<Point2D> getPoint2Ds() {
		List<Point2D> Point2Ds = new ArrayList<Point2D>();
		for (int i = 0; i < lines.size(); i++) {
			Point2D point1 = lines.get(i).getP1();
			Point2D point2 = lines.get(i).getP2();
			if (point1.getX() != 0 && point1.getY() != 0 && point1.getX() != GenerateMap.SIZE_X
					&& point1.getY() != GenerateMap.SIZE_Y) {
				if (!Point2Ds.contains(point1))
					Point2Ds.add(point1);
			} else
				return new ArrayList<Point2D>();
			if (point2.getX() != 0 && point2.getY() != 0 && point2.getX() != GenerateMap.SIZE_X
					&& point2.getY() != GenerateMap.SIZE_Y) {
				if (!Point2Ds.contains(point2))
					Point2Ds.add(point2);
			} else
				return new ArrayList<Point2D>();
		}

		return Point2Ds;
	}

	public List<Line2D> processLines(List<Point2D> voronoi) {
		List<Line2D> lines_sorted = new ArrayList<Line2D>();
		List<Line2D> lines = new ArrayList<Line2D>();
		List<Line2D> lines_pruned = new ArrayList<Line2D>();

		for (int i = 0; i < voronoi.size(); i++) {
			List<Point2D> q = findNearestPoint2Ds(voronoi, voronoi.get(i));

			for (int j = 0; j < q.size(); j++) {
				Line2D line = new Line2D.Float(q.get(j), voronoi.get(i));

				if (!this.containsLine(lines, line)) {
					lines.add(line);
				}
			}
		}

		for (int i = 0; i < lines.size(); i++) {
			double length = lines.get(i).getP1().distance(lines.get(i).getP2());

			if (lines_sorted.isEmpty()) {
				lines_sorted.add(lines.get(i));
			} else {
				for (int k = 0; k < lines_sorted.size(); k++) {
					double old = lines_sorted.get(k).getP1().distance(lines_sorted.get(k).getP2());

					if (length <= old) {
						lines_sorted.add(k, lines.get(i));

						break;
					} else if (k == lines_sorted.size() - 1) {
						lines_sorted.add(lines.get(i));
						break;
					}
				}
			}
		}

		for (int i = 0; i < lines_sorted.size(); i++) {
			if (lines_pruned.isEmpty()) {
				lines_pruned.add(lines_sorted.get(i));
			} else {
				if (!this.bisects(lines_pruned, lines_sorted.get(i)))
					lines_pruned.add(lines_sorted.get(i));
			}
		}

		/**
		 * for (int j = 0; j < 3; j++) { for (int i = 0; i < voronoi.size(); i++) {
		 * System.out.println("Testing"); List<Point2D> q = findNearestPoint2Ds(voronoi,
		 * voronoi.get(i));
		 * 
		 * Line2D line = new Line2D.Float(q.get(j), voronoi.get(i));
		 * 
		 * if (!bisects(lines, line)) lines.add(line); } }
		 **/

		return lines_pruned;
	}

	public List<Point2D> findNearestPoint2Ds(List<Point2D> voronoi, Point2D p) {
		List<Point2D> s = new ArrayList<Point2D>();

		for (int i = 0; i < voronoi.size(); i++) {
			Point2D q = voronoi.get(i);

			double dist = q.distance(p);

			if (dist != 0) {
				if (s.isEmpty()) {
					s.add(q);
				} else {
					for (int j = 0; j < s.size(); j++) {
						if (dist < s.get(j).distance(p)) {
							s.add(j, q);

							break;
						}
					}
					if (!s.contains(q)) {
						s.add(q);
					}
				}
			}
		}

		return s;
	}

	public boolean containsLine(List<Line2D> lines, Line2D line) {
		for (int i = 0; i < lines.size(); i++) {
			if (((line.getP1().getX() == lines.get(i).getP1().getX()
					&& line.getP1().getY() == lines.get(i).getP1().getY())
					&& (line.getP2().getX() == lines.get(i).getP2().getX()
							&& line.getP2().getY() == lines.get(i).getP2().getY()))

					||

					((line.getP1().getX() == lines.get(i).getP1().getX()
							&& line.getP2().getY() == lines.get(i).getP2().getY())
							&& (line.getP2().getX() == lines.get(i).getP2().getX()
									&& line.getP1().getY() == lines.get(i).getP1().getY()))) {
				// System.out.println("Contains Line");
				return true;
			}
		}

		return false;
	}

	public boolean bisects(List<Line2D> lines, Line2D line) {
		for (int i = 0; i < lines.size(); i++) {
			Line2D line2 = lines.get(i);

			if (this.intersectsProper(line, line2)) {
				return true;
			}
		}

		return false;
	}

	public boolean intersectsProper(Line2D line1, Line2D line2) {
		Point2D.Double temp1 = new Point2D.Double(line1.getX1(), line1.getY1());
		// ending Point2D of line 1
		Point2D.Double temp2 = new Point2D.Double(line1.getX2(), line1.getY2());
		// starting Point2D of line 2
		Point2D.Double temp3 = new Point2D.Double(line2.getX1(), line2.getY1());
		// ending Point2D of line 2
		Point2D.Double temp4 = new Point2D.Double(line2.getX2(), line2.getY2());

		// determine if the lines intersect
		boolean intersects = Line2D.linesIntersect(temp1.x, temp1.y, temp2.x, temp2.y, temp3.x, temp3.y, temp4.x,
				temp4.y);

		// determines if the lines share an endPoint2D
		boolean shareAnyPoint2D = shareAnyPoint2D(temp1, temp2, temp3, temp4);

		if (intersects && shareAnyPoint2D) {
			// System.out.println("Lines share an endPoint2D.");
		} else if (intersects && !shareAnyPoint2D) {
			return true;
		} else {
			// System.out.println("Lines neither intersect nor share a share an
			// endPoint2D.");
		}

		return false;
	}

	public boolean shareAnyPoint2D(Point2D.Double A, Point2D.Double B, Point2D.Double C, Point2D.Double D) {
		if (isPoint2DOnTheLine(A, B, C))
			return true;
		else if (isPoint2DOnTheLine(A, B, D))
			return true;
		else if (isPoint2DOnTheLine(C, D, A))
			return true;
		else if (isPoint2DOnTheLine(C, D, B))
			return true;
		else
			return false;
	}

	public boolean isPoint2DOnTheLine(Point2D.Double A, Point2D.Double B, Point2D.Double P) {
		double m = (B.y - A.y) / (B.x - A.x);

		// handle special case where the line is vertical
		if (Double.isInfinite(m)) {
			if (A.x == P.x)
				return true;
			else
				return false;
		}

		if ((P.y - A.y) == m * (P.x - A.x))
			return true;
		else
			return false;
	}

	public boolean isLand() {
		return this.land;
	}

	public void setLand(boolean b) {
		this.land = b;
	}
}
