package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.BasicStroke;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JPanel;

import gen.sPolygon;
import gen.GenerateMap;
import sim.Timeline;
import sim.Title;
import sim.Person;
import sim.War;

public class Display extends JPanel {

	final static float dash1[] = { 2.0f };
	public final static BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
			dash1, 0.0f);

	public final static BasicStroke dashed_light = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
			10.0f, dash1, 0.0f);
	public final static BasicStroke default_stroke = new BasicStroke();

	private final String months[] = { "Jan 1", "Jan 2", "Feb 1", "Feb 2", "Mar 1", "Mar 2", "Apr 1", "Apr 2", "May 1",
			"May 2", "Jun 1", "Jun 2", "Jul 1", "Jul 2", "Aug 1", "Aug 2", "Sep 1", "Sep 2", "Oct 1", "Oct 2", "Nov 1",
			"Nov 2", "Dec 1", "Dec 2" };

	private int select = 0;
	private int mode = 0;
	HashMap<Integer, sPolygon> Polygons = new HashMap<Integer, sPolygon>();
	HashMap<Integer, List<sPolygon>> Areas = new HashMap<Integer, List<sPolygon>>();
	List<Area> AreaShapes = new ArrayList<Area>();
	List<Area> PlateShapes = new ArrayList<Area>();
	GenerateMap map;
	Timeline timeline;
	sPolygon province;
	Title selected;
	Person p;
	public static Scanner sc;

	boolean stepping = false;
	boolean drawing = true;
	Font font = new Font("Iosevka SS05", Font.PLAIN, 10);

	public Display(GenerateMap map) {
		sc = new Scanner(System.in);
		setFocusable(true);
		requestFocusInWindow();
		this.map = map;
		this.Polygons = map.getPolygons();
		this.Areas = map.getAreas();
		this.AreaShapes = map.getAreaShapes();
		this.PlateShapes = map.getPlateShapes();

		// setBorder(BorderFactory.createLineBorder(Color.black));

		this.timeline = new Timeline(map);

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 49) {
					mode = 0;
				} else if (e.getKeyCode() == 50) {
					mode = 1;
				} else if (e.getKeyCode() == 51) {
					mode = 2;
				} else if (e.getKeyCode() == 52) {
					mode = 3;
				} else if (e.getKeyCode() == 53) {
					mode = 4;
				} else if (e.getKeyCode() == 54) {
					mode = 5;
				} else if (e.getKeyCode() == 55) {
					mode = 6;
				} else if (e.getKeyCode() == 56) {
					mode = 7;
				} else if (e.getKeyCode() == 57) {
					mode = 8;
				} else if (e.getKeyCode() == 80) {
					timeline.getPerson(null);
				} else if (e.getKeyCode() == 72) {
					timeline.getPerson(p);
				} else if (e.getKeyCode() == 68) {
					timeline.getDynasty(p);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("Pressed " + e.getKeyChar()); // 49 50 51
				if (e.getKeyChar() == ' ') {
					stepping = !stepping;
				} else if (e.getKeyChar() == 'q') {
					drawing = !drawing;
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				update(e.getX(), e.getY());
			}
		});

	}

	private void update(int x, int y) {
		int poly = map.getNearestPoly(x, y);
		province = Polygons.get(poly);
	}

	private void drawTerrain(Graphics2D g2) {
		for (int i = 0; i < AreaShapes.size(); i++) {
			g2.setPaint(Areas.get(i + 1).get(0).getColor());

			g2.fill(AreaShapes.get(i));

			if (select - 1 == i)
				g2.setPaint(Color.RED);

			g2.draw(AreaShapes.get(i));
		}

		if (select != 0) {
			g2.setPaint(Color.RED);
			g2.draw(AreaShapes.get(select - 1));
		}
	}

	private void drawPlates(Graphics2D g2) {
		for (int i = 0; i < PlateShapes.size(); i++) {
			g2.setPaint(Color.WHITE);
			g2.fill(PlateShapes.get(i));
			g2.setPaint(Color.BLACK);
			g2.draw(PlateShapes.get(i));
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setFont(font);
		g2.setPaint(sPolygon.WATER);
		g2.fillRect(0, 0, Main.SIZE_X, Main.SIZE_Y);

		switch (mode) {
			case 0:
				selected = null;
				p = null;
				if (province != null)
					select = province.location_id;
				drawTerrain(g2);
				break;
			case 1:
				select = 0;
				if (province != null) {
					if (timeline.FindCounty(province) != null)
						this.p = timeline.FindCounty(province).owner;
					else
						this.p = null;
				}
				selected = null;
				drawTerrain(g2);
				timeline.drawWaterways(g2);
				timeline.drawPeople(g2);
				break;
			case 2:
				select = 0;
				p = null;
				if (province != null)
					selected = timeline.FindCounty(province);
				timeline.drawWaterways(g2);
				timeline.drawCounties(g2);
				break;
			case 3:
				select = 0;
				p = null;
				if (province != null)
					selected = timeline.FindDuchy(province);
				timeline.drawWaterways(g2);
				timeline.drawDuchies(g2);
				break;
			case 4:
				select = 0;
				p = null;
				if (province != null)
					selected = timeline.FindKingdom(province);
				timeline.drawWaterways(g2);
				timeline.drawKingdoms(g2);
				break;
			case 5:
				select = 0;
				p = null;
				if (province != null)
					selected = timeline.FindEmpire(province);
				timeline.drawWaterways(g2);
				timeline.drawEmpires(g2);
				break;
			case 6:
				select = 0;
				p = null;
				selected = null;
				timeline.drawWaterways(g2);
				timeline.drawIslands(g2);
				break;
			case 7:
				select = 0;
				p = null;
				selected = null;
				timeline.drawWaterways(g2);
				timeline.drawLanguages(g2);
				break;
			case 8:
				select = 0;
				p = null;
				if (province != null)
					selected = timeline.FindCounty(province);
				timeline.drawWaterways(g2);
				timeline.drawDevelopment(g2);
				break;
		}

		g2.setColor(Color.WHITE);
		g2.drawString("People: " + timeline.person_manager.size(), 20, 20);
		g2.drawString("Dynasties: " + timeline.dynasty_count + ", " + timeline.original_dynasties, 20, 34);
		g2.drawString("RR: " + String.format("%.2f", timeline.replacementrate), 20, 48);

		if (selected != null) {
			for (int i = 0; i < selected.getNeighbors().size(); i++) {
				g2.setColor(Color.WHITE);
				g2.draw(selected.getNeighbors().get(i).getArea());
			}
			g2.setColor(Color.RED);
			g2.draw(selected.getArea());

			g2.setColor(Color.BLACK);
			g2.drawString(selected.toString() + (selected.level == 0 ? " (" + selected.development + ")" : ""),
					Main.SIZE_X + 20, 20);

			if (selected.owner != null)
				g2.drawString(selected.owner.getName(), Main.SIZE_X + 20, 40);

			g2.drawString("Titles Contained: (" + selected.getLowest().size() + ")", Main.SIZE_X + 20, 90);
			for (int i = 0; i < Math.min(selected.getLowest().size(), 45); i++) {
				g2.setColor(Color.BLUE);
				g2.drawString(selected.getLowest().get(i).toString(), Main.SIZE_X + 20, 120 + i * 20);
			}

			for (int i = 45; i < Math.min(selected.getLowest().size(), 90); i++) {
				g2.setColor(Color.BLUE);
				g2.drawString(selected.getLowest().get(i).toString(), Main.SIZE_X + 420, 120 + (i - 45) * 20);
			}
		} else if (this.p != null) {
			if (p.dynasty.original)
				g2.setColor(Color.MAGENTA);
			else
				g2.setColor(Color.BLACK);
			g2.drawString(p.getName() + " - " + p.lang.getName(), Main.SIZE_X + 20, 20);
			g2.drawString("Levy: " + p.levy + "/" + p.getMaxLevy(p, null), Main.SIZE_X + 20, 34);
			g2.setColor(Color.GRAY);
			g2.drawString("Domain: " + (p.demesne.size() + p.getAllSubDemesne().size()), Main.SIZE_X + 20, 48);
			g2.setColor(Color.BLACK);

			if (p.getLord() != null && p.getSpouse() != null)
				g2.drawString("Lord: " + p.getLord().getName() + ", Spouse: " + p.getSpouse().getName(),
						Main.SIZE_X + 20, 62);
			else if (p.getLord() != null && p.getSpouse() == null)
				g2.drawString("Lord: " + p.getLord().getName(), Main.SIZE_X + 20, 62);
			else if (p.getLord() == null && p.getSpouse() != null)
				g2.drawString("Spouse: " + p.getSpouse().getName(), Main.SIZE_X + 20, 62);

			g2.drawString("Titles Contained: ", Main.SIZE_X + 20, 90);

			List<Title> titles = new ArrayList<Title>();
			titles.addAll(p.empires);
			titles.addAll(p.kingdoms);
			titles.addAll(p.duchies);
			titles.addAll(p.demesne);

			for (int i = 0; i < titles.size(); i++) {
				g2.setColor(Color.BLUE);
				if (titles.get(i) == p.main)
					g2.setColor(Color.CYAN);
				if (titles.get(i) == p.capital)
					g2.setColor(Color.RED);

				g2.drawString(titles.get(i).toString(), Main.SIZE_X + 20, 104 + i * 14);
			}

			g2.setColor(Color.BLACK);
			g2.drawString("Vassals Contained: (" + p.getSubSize() + ")", Main.SIZE_X + 220, 90);

			List<Person> sub = p.getSubList();

			for (int i = 0; i < sub.size(); i++) {
				g2.setColor(Color.BLUE);
				g2.drawString(sub.get(i).getName(), Main.SIZE_X + 220, 104 + i * 14);
			}

			g2.setColor(Color.BLACK);
			g2.drawString("Claims: (" + p.getClaims().size() + ")", Main.SIZE_X + 420, 90);

			List<Title> claims = p.getClaims();

			for (int i = 0; i < claims.size(); i++) {
				if (claims.get(i).owner != null && !timeline.isVassalOf(p, timeline.getTopMost(p, claims.get(i).owner)))
					g2.setColor(Color.GREEN);
				else
					g2.setColor(Color.GRAY);

				g2.drawString(claims.get(i).toString(), Main.SIZE_X + 420, 104 + i * 14);
			}

			g2.setColor(Color.BLACK);
			g2.drawString("Wars: (" + p.wars.size() + ")", Main.SIZE_X + 420, 490);

			List<War> wars = p.wars;

			for (int i = 0; i < wars.size(); i++) {
				if (wars.get(i).getAttacker() == p) {
					g2.setColor(Color.RED);
					g2.drawString(wars.get(i).getDefender().getName() + " (" + wars.get(i).getWarScore() + ")",
							Main.SIZE_X + 420, 520 + i * 14);
				} else {
					g2.setColor(Color.ORANGE);
					g2.drawString(wars.get(i).getAttacker().getName() + " (" + wars.get(i).getWarScore() + ")",
							Main.SIZE_X + 420, 520 + i * 14);
				}
			}

			g2.setColor(Color.BLACK);
			g2.drawString("Children: (" + p.children.size() + ")", Main.SIZE_X + 20, 490);
			for (int i = 0; i < p.children.size(); i++) {
				g2.setColor(Color.BLUE);

				g2.drawString(p.children.get(i).getName(), Main.SIZE_X + 20, 504 + i * 14);
			}

			g2.drawString("Court: (" + p.getCourtSize() + ")", Main.SIZE_X + 220, 490);
			for (int i = 0; i < p.getCourtSize(); i++) {
				g2.setColor(Color.BLUE);

				g2.drawString(p.getCourtAt(i).getName(), Main.SIZE_X + 220, 504 + i * 14);
			}
			p.draw(g2, null, true);
		}

		g2.setColor(Color.WHITE);
		long year = timeline.semimonth / 24;
		int month = (int) (timeline.semimonth % 24);
		g2.drawString("Year: " + year + " - " + months[month], 10, Main.SIZE_Y - 30);
		g2.drawString("Seed: " + Main.SEED, 10, Main.SIZE_Y - 16);
	}
}