package gen;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import main.Main;
import voronoi.GraphEdge;
import voronoi.Voronoi;

public class GenerateMap {
	static List<GraphEdge> allEdges, plateEdges;
	static HashMap<Integer, sPolygon> Polygons = new HashMap<Integer, sPolygon>();
	static HashMap<Integer, sPolygon> Plates = new HashMap<Integer, sPolygon>();
	static HashMap<Integer, List<sPolygon>> Areas = new HashMap<Integer, List<sPolygon>>();
	static HashMap<Integer, List<sPolygon>> PlateAreas = new HashMap<Integer, List<sPolygon>>();

	static double[] x_val = new double[99999];
	static double[] y_val = new double[99999];

	boolean done = false;

	Color[][] pixels = new Color[1024][1024];

	public static int SIZE_X;
	public static int SIZE_Y;
	public static int SIZE, VARY;

	static int FEATURE_SIZE, I_SIZE, MAX_ISLANDS;

	private static List<Point> island_points = new ArrayList<Point>();
	// private static float[][] islandmods = new float[SIZE_X][SIZE_Y];
	static HashMap<Point, Integer> islands = new HashMap<Point, Integer>();

	static int amt = 0;

	static OpenSimplexNoise noise;

	/**
	 * @param size_x             Width of map.
	 * @param size_y             Height of map.
	 * @param size               Size of each unit.
	 * @param vary               Variance of each node.
	 * @param i_size             Size of the islands.
	 * @param max_islands        Maximum number of islands.
	 * @param feature_size_lakes Size of lake noise gen.
	 */
	public GenerateMap(int size_x, int size_y, int size, int vary, int i_size, int max_islands, int feature_size_lakes,
			int plate) {
		SIZE_X = size_x;
		SIZE_Y = size_y;
		SIZE = size;
		VARY = vary;
		I_SIZE = i_size;
		MAX_ISLANDS = max_islands;
		FEATURE_SIZE = feature_size_lakes;
		GenerateMap.PLATE_COUNT = plate;
	}

	public HashMap<Integer, sPolygon> getPolygons() {
		return Polygons;
	}

	public void Generate() {
		init();
		TectonicPlates();
		sortPlates();
		sortSites();
		setLand();
		setInlandLakes();
		setHeights();
		setMoisture();
		setLocations();
		genPixelMap();
		done = true;
	}

	public void GenerateNOPIXEL() {
		init();
		TectonicPlates();
		sortPlates();
		sortSites();
		setLand();
		setInlandLakes();
		setHeights();
		setMoisture();
		setLocations();
		done = true;
	}

	public HashMap<Integer, List<sPolygon>> getAreas() {
		System.out.println("Getting areas...");
		return GenerateMap.Areas;
	}

	public void genPixelMap() {
		System.out.println("Gen Pixel Map...");
		for (int i = 0; i < SIZE_X; i++) {
			for (int j = 0; j < SIZE_Y; j++) {
				pixels[i][j] = Polygons.get(getNearestPoly(i, j)).getColor();
			}

			System.out.println(i);
		}
		System.out.println("Done");
	}

	public Color getPixel(int x, int y) {
		if (x >= 0 && y >= 0 && x < SIZE_X && y < SIZE_Y)
			return pixels[x][y];
		else
			return sPolygon.WATER;
	}

	public int getNearestPoly(int x, int y) {
		int shortest = 99999;
		int sp = 0;
		for (int i = 0; i < amt; i++) {
			int dist = (int) (Math.abs(x - x_val[i]) + Math.abs(y - y_val[i]));

			if (sp == 0 || dist < shortest) {
				shortest = dist;
				sp = i;
			}
		}

		return sp;
	}

	public static int getNearestPlate(int x, int y) {
		int shortest = 99999;
		int sp = -1;
		for (int i = 0; i < PLATE_COUNT; i++) {
			int dist = (int) (Math.abs(x - xplates[i]) + Math.abs(y - yplates[i]));

			if (sp == -1 || dist < shortest) {
				shortest = dist;
				sp = i;
			}
		}
		Plates.get(sp).center = new Point2D.Double(xplates[sp], yplates[sp]);

		return sp;
	}

	private static int PLATE_COUNT;
	static double[] xplates;
	static double[] yplates;

	public static void TectonicPlates() {
		xplates = new double[PLATE_COUNT];
		yplates = new double[PLATE_COUNT];

		System.out.println("Adding tectonic plates...");
		Voronoi v = new Voronoi(.000001f);

		for (int i = 0; i < PLATE_COUNT; i++) {
			int randX = Main.rand.nextInt(SIZE_X);
			int randY = Main.rand.nextInt(SIZE_Y);

			xplates[i] = randX;
			yplates[i] = randY;
		}

		plateEdges = v.generateVoronoi(xplates, yplates, 0, SIZE_X, 0, SIZE_Y);
	}

	public sPolygon getRandomLand() {
		List<sPolygon> possible = new ArrayList<sPolygon>();

		for (int i = 0; i < Polygons.size(); i++) {
			if (Polygons.get(i).isLand())
				possible.add(Polygons.get(i));
		}

		return possible.get(Main.rand.nextInt(possible.size()));
	}

	public sPolygon getRandomUnowned() {
		List<sPolygon> possible = new ArrayList<sPolygon>();

		for (int i = 0; i < Polygons.size(); i++) {
			if (Polygons.get(i).isLand() && Polygons.get(i).owner == null)
				possible.add(Polygons.get(i));
		}

		if (possible.isEmpty())
			return null;
		else
			return possible.get(Main.rand.nextInt(possible.size()));
	}

	public static void init() {
		System.out.println("Adding Islands...");
		noise = new OpenSimplexNoise(Main.SEED);
		Voronoi v = new Voronoi(.000001f);

		int amount = (int) (Main.rand.nextInt((int) (MAX_ISLANDS / 2.0f)) + MAX_ISLANDS / 2.0f);
		int radius = (Math.min(SIZE_X, SIZE_Y) - I_SIZE) / 2;

		for (int i = 0; i < amount; i++) {
			double angle = Main.rand.nextDouble() * 2.0 * Math.PI;

			int randX = (int) (Math.cos(angle) * Main.rand.nextInt(radius) + SIZE_X / 2);
			int randY = (int) (Math.sin(angle) * Main.rand.nextInt(radius) + SIZE_Y / 2);

			// int randX = (int) (I_SIZE / 2 + Main.rand.nextInt((int) (SIZE_X - I_SIZE)));
			// int randY = (int) (I_SIZE / 2 + Main.rand.nextInt((int) (SIZE_Y - I_SIZE)));
			int size = (int) (I_SIZE / 4.0 + Main.rand.nextInt((int) (I_SIZE - (I_SIZE * 0.25f))));

			addIsland(randX, randY, size);
		}
		// setBorder();

		for (int i = 0; i < SIZE_X / SIZE; i++) {
			for (int j = 0; j < SIZE_Y / SIZE; j++) {
				int randX = (int) ((Math.random() * VARY) - VARY / 2) + i * SIZE;
				int randY = (int) ((Math.random() * VARY) - VARY / 2) + j * SIZE;

				if (randX <= 0)
					randX = 1;
				if (randY <= 0)
					randY = 1;

				if (randX >= SIZE_X)
					randX = SIZE_X - 1;
				if (randY >= SIZE_Y)
					randY = SIZE_Y - 1;

				addCoords(randX, randY);
			}
		}

		allEdges = v.generateVoronoi(x_val, y_val, 0, SIZE_X, 0, SIZE_Y);
	}

	public static void addIsland(int x, int y, int r) {
		// System.out.println(x + "_" + y);
		Point p = new Point(x, y);

		island_points.add(p);
		islands.put(p, r);
	}

	public static void addCoords(int x, int y) {
		x_val[amt] = x;
		y_val[amt] = y;
		amt++;
	}

	public static void setLand() {
		System.out.println("Generating land data...");
		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			double mod = 0;
			
			int i = (int) poly.center.x;
			int j = (int) poly.center.y;
			
			double value = noise.eval(i / (double)FEATURE_SIZE, j / (double)FEATURE_SIZE);

			for (int k = 0; k < island_points.size(); k++) {
				int pos_x = (island_points.get(k).x);
				int pos_y = (island_points.get(k).y);
				int size = islands.get(island_points.get(k));
				float dist = (float) (1 - Math.sqrt(Math.pow(i - pos_x, 2) + Math.pow(j - pos_y, 2)) / (size / 2));

				if (dist < 0)
					dist = 0;
				mod = (dist + mod) - (0.005 * Math.pow(1 - Math.abs(value), 10));
			}

			if (mod > 1)
				mod = 1;

			poly.setLand(mod > 0.0);
		}
	}

	public static boolean bordersOcean(sPolygon poly) {
		List<sPolygon> neighbors = poly.neighbors;

		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i).isLand() == false && neighbors.get(i).inlandlake == false)
				return true;
		}
		return false;
	}

	public static boolean bordersWater(sPolygon poly) {
		List<sPolygon> neighbors = poly.neighbors;

		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i).isLand() == false)
				return true;
		}
		return false;
	}

	public void setLocations() {
		System.out.println("Setting locations...");

		List<sPolygon> ocean = new ArrayList<sPolygon>();

		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if ((poly.isLand() || poly.inlandlake) && poly.location_id == 0) {
				explored = new ArrayList<sPolygon>();

				getConnected(poly);
				// System.out.println(explored.size());

				float c = (float) Math.random();

				List<sPolygon> a = new ArrayList<sPolygon>();

				for (int i = 0; i < explored.size(); i++) {
					explored.get(i).location_id = LOCATIONS;
					explored.get(i).c = c;
					a.add(explored.get(i));
				}

				// System.out.println(LOCATIONS);

				Areas.put(LOCATIONS, a);

				LOCATIONS = LOCATIONS + 1;
			} else if (poly.isLand() == false && poly.inlandlake == false) {
				ocean.add(poly);
			}
		}

		Areas.put(0, ocean);
	}

	public static void setInlandLakes() {
		System.out.println("Finding inland lakes...");
		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if ((poly.isLand() == false) && poly.border == false) {
				explored = new ArrayList<sPolygon>();
				connected = false;
				isConnectedToBorder(poly);
				poly.inlandlake = !connected;
			}
		}
	}

	static int LOCATIONS = 1;

	public static void getConnected(sPolygon poly) {
		List<sPolygon> neighbors = poly.neighbors;

		explored.add(poly);

		// System.out.println(poly);

		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i).location_id == 0) {
				if (neighbors.get(i).getBiomeName().equals(poly.getBiomeName())) {
					if (!explored.contains(neighbors.get(i))) {
						getConnected(neighbors.get(i));
					}
				}
			}
		}
	}

	public List<Area> getAreaShapes() {
		List<Area> a = new ArrayList<Area>();
		for (int i = 1; i < Areas.size(); i++) {
			if (Areas.containsKey(i)) {
				System.out.println("Getting Area " + i);
				a.add(getShapeFromArea(Areas.get(i)));
			}
		}

		return a;
	}

	public List<Area> getPlateShapes() {
		List<Area> a = new ArrayList<Area>();
		for (int i = 0; i < PlateAreas.size() + 1; i++) {
			if (PlateAreas.containsKey(i)) {
				System.out.println("Getting Plate " + i);
				a.add(getShapeFromPlate(PlateAreas.get(i)));
			}
		}

		return a;
	}

	public static Area getShapeFromArea(List<sPolygon> list) {
		Area a = new Area();

		for (int i = 0; i < list.size(); i++) {
			List<Polygon> tri = list.get(i).getTriangles();
			for (int j = 0; j < tri.size(); j++) {
				a.add(new Area(tri.get(j)));
			}
		}

		return a;
	}

	public static Area getShapeFromPlate(List<sPolygon> list) {
		Area a = new Area();

		for (int i = 0; i < list.size(); i++) {
			List<Polygon> tri = list.get(i).getTriangles();
			for (int j = 0; j < tri.size(); j++) {
				a.add(new Area(tri.get(j)));
			}
		}

		return a;
	}

	public static void sortPlates() {
		System.out.println("Generating tectonic plate map from voronoi sites...");
		for (int i = 0; i < plateEdges.size(); i++) {
			int site1 = plateEdges.get(i).site1;
			int site2 = plateEdges.get(i).site2;

			Line2D line = new Line2D.Float((float) plateEdges.get(i).x1, (float) plateEdges.get(i).y1,
					(float) plateEdges.get(i).x2, (float) plateEdges.get(i).y2);

			if (!Plates.containsKey(site1))
				Plates.put(site1, new sPolygon(new Point2D.Double(x_val[site1], y_val[site1])));

			if (!Plates.containsKey(site2))
				Plates.put(site2, new sPolygon(new Point2D.Double(x_val[site2], y_val[site2])));

			Plates.get(site1).addLine(line);
			Plates.get(site2).addLine(line);

			if (Plates.get(site1).center.x < SIZE * 2)
				Plates.get(site1).border = true;
			if (Plates.get(site1).center.y < SIZE * 2)
				Plates.get(site1).border = true;
			if (Plates.get(site2).center.x < SIZE * 2)
				Plates.get(site2).border = true;
			if (Plates.get(site2).center.y < SIZE * 2)
				Plates.get(site2).border = true;
			if (Plates.get(site1).center.x > SIZE_X - SIZE * 3)
				Plates.get(site1).border = true;
			if (Plates.get(site1).center.y > SIZE_Y - SIZE * 3)
				Plates.get(site1).border = true;
			if (Plates.get(site2).center.x > SIZE_X - SIZE * 3)
				Plates.get(site2).border = true;
			if (Plates.get(site2).center.y > SIZE_Y - SIZE * 3)
				Plates.get(site2).border = true;

			if (!Plates.get(site1).neighbors.contains(Plates.get(site2)))
				Plates.get(site1).addNeighbor(Plates.get(site2));
			if (!Plates.get(site2).neighbors.contains(Plates.get(site1)))
				Plates.get(site2).addNeighbor(Plates.get(site1));
		}
	}

	public static void sortSites() {
		System.out.println("Generating terrain map from voronoi sites...");
		for (int i = 0; i < allEdges.size(); i++) {
			int site1 = allEdges.get(i).site1;
			int site2 = allEdges.get(i).site2;

			Line2D line = new Line2D.Float((float) allEdges.get(i).x1, (float) allEdges.get(i).y1,
					(float) allEdges.get(i).x2, (float) allEdges.get(i).y2);

			if (!Polygons.containsKey(site1))
				Polygons.put(site1, new sPolygon(new Point2D.Double(x_val[site1], y_val[site1])));

			if (!Polygons.containsKey(site2))
				Polygons.put(site2, new sPolygon(new Point2D.Double(x_val[site2], y_val[site2])));

			Polygons.get(site1).addLine(line);
			Polygons.get(site2).addLine(line);

			if (Polygons.get(site1).center.x < SIZE * 2)
				Polygons.get(site1).border = true;
			if (Polygons.get(site1).center.y < SIZE * 2)
				Polygons.get(site1).border = true;
			if (Polygons.get(site2).center.x < SIZE * 2)
				Polygons.get(site2).border = true;
			if (Polygons.get(site2).center.y < SIZE * 2)
				Polygons.get(site2).border = true;
			if (Polygons.get(site1).center.x > SIZE_X - SIZE * 3)
				Polygons.get(site1).border = true;
			if (Polygons.get(site1).center.y > SIZE_Y - SIZE * 3)
				Polygons.get(site1).border = true;
			if (Polygons.get(site2).center.x > SIZE_X - SIZE * 3)
				Polygons.get(site2).border = true;
			if (Polygons.get(site2).center.y > SIZE_Y - SIZE * 3)
				Polygons.get(site2).border = true;

			if (!Polygons.get(site1).neighbors.contains(Polygons.get(site2)))
				Polygons.get(site1).addNeighbor(Polygons.get(site2));
			if (!Polygons.get(site2).neighbors.contains(Polygons.get(site1)))
				Polygons.get(site2).addNeighbor(Polygons.get(site1));

			int plate1 = getNearestPlate((int) Polygons.get(site1).center.x, (int) Polygons.get(site1).center.y);
			int plate2 = getNearestPlate((int) Polygons.get(site2).center.x, (int) Polygons.get(site2).center.y);

			if (Polygons.get(site1).plate == -1)
				Polygons.get(site1).plate = plate1;
			if (Polygons.get(site2).plate == -1)
				Polygons.get(site2).plate = plate2;

			if (PlateAreas.containsKey(plate1)) {
				if (!PlateAreas.get(plate1).contains(Polygons.get(site1)))
					PlateAreas.get(plate1).add(Polygons.get(site1));
			} else {
				List<sPolygon> a = new ArrayList<sPolygon>();
				a.add(Polygons.get(site1));
				PlateAreas.put(plate1, a);
			}

			if (PlateAreas.containsKey(plate2)) {
				if (!PlateAreas.get(plate2).contains(Polygons.get(site2)))
					PlateAreas.get(plate2).add(Polygons.get(site2));
			} else {
				List<sPolygon> a = new ArrayList<sPolygon>();
				a.add(Polygons.get(site2));
				PlateAreas.put(plate2, a);
			}
		}
	}

	static List<sPolygon> explored;
	static boolean connected = false;

	public static void isConnectedToBorder(sPolygon poly) {
		List<sPolygon> neighbors = poly.neighbors;

		explored.add(poly);

		if (poly.border == true)
			connected = true;

		// System.out.println(poly);

		for (int i = 0; i < neighbors.size(); i++) {
			if (!neighbors.get(i).isLand()) {
				if (connected == true)
					break;
				if (!explored.contains(neighbors.get(i))) {
					isConnectedToBorder(neighbors.get(i));
				}
			}
		}
	}

	public static void setHeights() {
		System.out.println("Setting height map...");
		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if (poly.isLand()) {
				float dist = distanceFromPlateEdge((int) poly.center.x, (int) poly.center.y) * 1.5f;

				float h = (float) (findClosestOcean(poly));// + ((value + 1.0) * 2.0f));

				h = dist - h / 2.0f;

				poly.height = h;

				if (max_height < h) {
					max_height = h;
				}

				if (min_height > h) {
					min_height = h;
				}
			}
		}

		System.out.println("Min: " + min_height);
		System.out.println("Max: " + max_height);
	}

	public static float distanceFromPlateCenter(int x, int y) {
		int p = getNearestPlate(x, y);
		sPolygon poly = Plates.get(p);
		float dist = (float) Math.sqrt(Math.pow(poly.center.x - x, 2) + Math.pow(poly.center.y - y, 2));

		return (float) dist;
	}

	public static float distanceFromPlateEdge(int x, int y) {
		double shortest = 999999;

		for (int i = 0; i < Plates.size(); i++) {
			List<Line2D> g = Plates.get(i).getLines();

			for (int j = 0; j < g.size(); j++) {
				double dist = g.get(j).ptSegDist(x, y);

				if (dist < shortest) {
					shortest = dist;
				}
			}
		}

		return (float) shortest;
	}

	public static void setMoisture() {
		System.out.println("Setting moisture map...");
		for (int key : Polygons.keySet()) {
			sPolygon poly = Polygons.get(key);

			if (poly.isLand()) {
				float h = findClosestLake(poly);

				poly.moisture = h;

				if (max_moisture < h) {
					max_moisture = h;
				}

				if (min_moisture == 0 || min_moisture > h) {
					min_moisture = h;
				}
			}
		}
	}

	public static float max_height = 0, max_moisture = 0, min_height = 0, min_moisture = 0;

	public static float findClosestOcean(sPolygon poly) {
		float shortest = 999999;
		sPolygon p = null;

		for (int key : Polygons.keySet()) {
			sPolygon poly2 = Polygons.get(key);

			if (!poly2.isLand() && !poly2.inlandlake) {
				float dist = (float) poly2.center.distance(poly.center);

				if (p == null || dist < shortest) {
					p = poly2;
					shortest = dist;
				}
			}
		}

		// System.out.println(shortest);

		return shortest;
	}

	public static float findClosestLake(sPolygon poly) {
		float shortest = 999999;
		sPolygon p = null;

		for (int key : Polygons.keySet()) {
			sPolygon poly2 = Polygons.get(key);

			if (!poly2.isLand() && poly2.inlandlake) {
				float dist = (float) poly2.center.distance(poly.center);

				if (p == null || dist < shortest) {
					p = poly2;
					shortest = dist;
				}
			}
		}

		// System.out.println(shortest);

		return shortest;
	}
}
