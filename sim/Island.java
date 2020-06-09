package sim;

import java.util.ArrayList;
import java.util.List;

import gen.GenerateMap;
import gen.sPolygon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;

import java.awt.geom.Line2D;

public class Island {
	List<Title> territory;
	int id;
	Color c;
	private Area a;

	public Island(int id) {
		this.id = id;
		this.territory = new ArrayList<Title>();

		this.c = Color.getHSBColor((float) Math.random(), 0.25f + ((float) Math.random() * 0.75f),
				0.75f + ((float) Math.random() * 0.25f));
	}

	public void draw(Graphics2D g2) {
		g2.setColor(this.c);

		g2.fill(this.getArea());

		g2.setColor(Color.BLACK);

		g2.draw(this.getArea());
	}

	private Area getArea() {
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

	public List<sPolygon> getPolygons() {
		List<sPolygon> p = new ArrayList<sPolygon>();

		for (int i = 0; i < this.territory.size(); i++) {
			p.addAll(this.territory.get(i).getPolygons());
		}

		return p;
	}

	public Title[] getClosest(Island i, List<Island> islands) {
		Title[] closest = new Title[] { null, null };
		for (Title t1 : territory) {
			if (!GenerateMap.bordersWater(t1.poly))
				continue;
			for (Title t2 : i.territory) {
				if (!GenerateMap.bordersWater(t2.poly))
					continue;
				if ((closest[0] == null || t1.getCenter().distance(t2.getCenter()) < closest[0].getCenter()
						.distance(closest[1].getCenter())) && !crossesLand(t1, t2, islands)) {
					closest[0] = t1;
					closest[1] = t2;
				}
			}
		}
		return closest;
	}

	public boolean crossesLand(Title a, Title b, List<Island> islands) {
		Line2D.Double path = new Line2D.Double(a.getCenter(), b.getCenter());
		for (Island i : islands) {
			if (a.island == i || b.island == i)
				continue;
			if (intersectsLand(i.getArea(), path))
				return true;
		}
		return false;
	}

	public static boolean intersectsLand(final Area poly, final Line2D.Double line) {

		final PathIterator polyIt = poly.getPathIterator(null); // Getting an iterator along the polygon path
		final double[] coords = new double[6]; // Double array with length 6 needed by iterator
		final double[] firstCoords = new double[2]; // First point (needed for closing polygon path)
		final double[] lastCoords = new double[2]; // Previously visited point
		// final Set<Point2D> intersections = new HashSet<Point2D>(); // List to hold
		// found intersections
		polyIt.currentSegment(firstCoords); // Getting the first coordinate pair
		lastCoords[0] = firstCoords[0]; // Priming the previous coordinate pair
		lastCoords[1] = firstCoords[1];
		polyIt.next();
		while (!polyIt.isDone()) {
			final int type = polyIt.currentSegment(coords);
			switch (type) {
				case PathIterator.SEG_LINETO: {
					final Line2D.Double currentLine = new Line2D.Double(lastCoords[0], lastCoords[1], coords[0],
							coords[1]);
					if (currentLine.intersectsLine(line))
						return true;
					// intersections.add(getIntersection(currentLine, line));
					lastCoords[0] = coords[0];
					lastCoords[1] = coords[1];
					break;
				}
				case PathIterator.SEG_CLOSE: {
					final Line2D.Double currentLine = new Line2D.Double(coords[0], coords[1], firstCoords[0],
							firstCoords[1]);
					if (currentLine.intersectsLine(line))
						return true;
					// intersections.add(getIntersection(currentLine, line));
					break;
				}
				/*
				 * default: { System.err.println("ERR: " + type); System.exit(1); }
				 */
			}
			polyIt.next();
		}
		return false;
		// return intersections;
	}

	public static Point2D getIntersection(final Line2D.Double line1, final Line2D.Double line2) {
		final double x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = line1.x1;
		y1 = line1.y1;
		x2 = line1.x2;
		y2 = line1.y2;
		x3 = line2.x1;
		y3 = line2.y1;
		x4 = line2.x2;
		y4 = line2.y2;
		final double x = ((x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3) * (x1 * y2 - x2 * y1))
				/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		final double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2) * (x3 * y4 - x4 * y3))
				/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

		return new Point2D.Double(x, y);

	}
}