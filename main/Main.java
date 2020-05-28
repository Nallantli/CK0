package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import gen.GenerateMap;

public class Main {
	// List<GraphEdge> allEdges;
	/*
	 * static HashMap<Integer, sPolygon> Polygons = new HashMap<Integer,
	 * sPolygon>(); static HashMap<Integer, List<sPolygon>> Areas = new
	 * HashMap<Integer, List<sPolygon>>(); static List<Area> AreaShapes = new
	 * ArrayList<Area>(); static List<Area> PlateShapes = new ArrayList<Area>();
	 */

	public static final int SIZE_X = 700, SIZE_Y = 700, SEED = (int) (Math.random() * 100000.0);

	Color[][] pixels = new Color[SIZE_X][SIZE_Y];

	public Display ttp;

	public static Random rand;

	public static final List<String> COOL_NAMES = new ArrayList<String>(Arrays.asList(
		"Benjamin",
		"Benji",
		"Ćarlotte",
		"Ćarlot",
		"Mafu",
		"Lilli",
		"Zacari",
		"Fuzzwad",
		"Amandēp",
		"Carter"
	));

	public static enum CCOLOR {
		RESET	("\033[0m"),
		BLACK	("\033[0;30m"),
		RED		("\033[0;31m"),
		GREEN	("\033[0;32m"),
		YELLOW	("\033[0;33m"),
		BLUE	("\033[0;34m"),
		MAGENTA	("\033[0;35m"),
		CYAN	("\033[0;36m"),
		WHITE	("\033[0;37m");

		public final String token;
		private CCOLOR(String token) {
			this.token = token;
		}
		public String toString() {
			return token;
		}
	}

	public static void main(String s[]) {
		try {
			System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new InternalError("VM does not support mandatory encoding UTF-8");
		}
		rand = new Random(SEED);
		Main app = new Main();
		app.init();
	}

	public void init() {
		GenerateMap map = new GenerateMap(SIZE_X, SIZE_Y, 7, 7, 50, 800, 800, 8);
		map.GenerateNOPIXEL();

		JFrame f = new JFrame("Simulation");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ttp = new Display(map);
		f.getContentPane().add("Center", ttp);
		f.pack();
		f.setSize(new Dimension(SIZE_X * 2, SIZE_Y + 35));
		f.setVisible(true);

		/*
		 * Polygons = map.getPolygons(); Areas = map.getAreas(); AreaShapes =
		 * map.getAreaShapes(); PlateShapes = map.getPlateShapes();
		 */

		loop();
	}

	public void loop() {
		while (true) {
			long start = System.currentTimeMillis();
			if (ttp.stepping) {
				ttp.timeline.step();
				double total = (System.currentTimeMillis() - start);
				if (total > 30) {
					System.out.println(CCOLOR.RED + "[" + ttp.timeline.semimonth + "]\tStep Time: " + total + " ("
							+ String.format("%.4f", total / (double) ttp.timeline.person_manager.size())
							+ "ms per person)" + CCOLOR.RESET);
				} else if (total > 20) {
					System.out.println(CCOLOR.YELLOW + "[" + ttp.timeline.semimonth + "]\tStep Time: " + total + " ("
							+ String.format("%.4f", total / (double) ttp.timeline.person_manager.size())
							+ "ms per person)" + CCOLOR.RESET);
				}
				if (total < 30) {
					try {
						Thread.sleep(30 - (long) total);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			if (ttp.drawing)
				ttp.repaint();
		}
	}

	public static String convertToRN(int n) {
		int values[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String roman[] = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
		String res = "";

		int i = 0;
		while (n > 0) {
			int v = values[i];
			String r = roman[i];
			if (n < v)
				i++;
			else {
				n -= v;
				res += r;
			}
		}
		return res;
	}
}