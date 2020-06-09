package sim;

import java.util.ArrayList;
import java.util.List;

import main.Main;

public class Event {
	public List<String> people;
	public String message;
	public long time;
	Main.CCOLOR color;

	public Event(long time, Main.CCOLOR color, String message, Person p[]) {
		this.time = time;
		this.color = color;
		this.message = message;
		this.people = new ArrayList<String>();
		for (int i = 0; i < p.length; i++)
			this.people.add(p[i].getName());
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(color);
		int i = 0;
		for (char c : message.toCharArray()) {
			switch (c) {
				case '$':
					s.append(Main.CCOLOR.BOLD);
					s.append(people.get(i++));
					s.append(color);
					break;
				default:
					s.append(c);
					break;
			}
		}
		s.append(Main.CCOLOR.RESET);
		return s.toString();
	}
}