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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import gen.GenerateMap;

class GameLoop extends TimerTask {
	private Display ttp;

	public GameLoop(Display ttp) {
		this.ttp = ttp;
	}

	@Override
	public void run() {
		if (ttp.stepping) {
			long start = System.currentTimeMillis();
			ttp.timeline.step();
			double total = (System.currentTimeMillis() - start);
			if (total > 30) {
				System.out.println(Main.CCOLOR.RED + "[" + ttp.timeline.semimonth + "]\tStep Time: " + total + " ("
						+ String.format("%.4f", total / (double) ttp.timeline.person_manager.size()) + "ms per person)"
						+ Main.CCOLOR.RESET);
			} else if (total > 25) {
				System.out.println(Main.CCOLOR.YELLOW + "[" + ttp.timeline.semimonth + "]\tStep Time: " + total + " ("
						+ String.format("%.4f", total / (double) ttp.timeline.person_manager.size()) + "ms per person)"
						+ Main.CCOLOR.RESET);
			}
		}
		if (ttp.drawing)
			ttp.repaint();
	}

}

public class Main {
	public static final int SIZE_X = 700, SIZE_Y = 700, SEED = (int) (Math.random() * 100000.0);

	Color[][] pixels = new Color[SIZE_X][SIZE_Y];

	public Display ttp;

	public static Random rand;

	public static final List<String> COOL_NAMES = new ArrayList<String>(Arrays.asList("Benjamin", "Benji", "Ćarlotte",
			"Ćarlot", "Mafu", "Lilli", "Zacari", "Fuzzwad", "Amandip", "Carter"));

	public static enum CCOLOR {
		RESET("\033[0m"), BOLD("\033[1m"), UNDER("\033[4m"), BLACK("\033[0;30m"), RED("\033[0;31m"),
		GREEN("\033[0;32m"), YELLOW("\033[0;33m"), BLUE("\033[0;34m"), MAGENTA("\033[0;35m"), CYAN("\033[0;36m"),
		WHITE("\033[0;37m");

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
		GenerateMap map = new GenerateMap(SIZE_X, SIZE_Y, 7, 7, 60, 800, 100, 8);
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

		loop();
	}

	public void loop() {
		Timer timer = new Timer();
		TimerTask task = new GameLoop(ttp);

		timer.schedule(task, 0, 30);
	}

	public static String convertToRN(int n) {
		int values[] = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		String roman[] = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
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