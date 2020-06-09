package ling;

import java.util.Arrays;
import java.util.List;

class Rule {
	String input, output;
	public Rule(String input, String output) {
		this.input = input;
		this.output = output;
	}
	public String apply(String s) {
		return s.replace(input, output);
	}
}

public class Orthography {
	List<Rule> rules;

	final static Orthography O1 = new Orthography(new Rule[] {
		new Rule("tsː", "tts"),
		new Rule("dzː", "ddz"),
		new Rule("j", "y"),
		new Rule("kː", "kk"),
		new Rule("tː", "tt"),
		new Rule("sː", "ss"),
		new Rule("nː", "nn"),
		new Rule("mː", "mm"),
		new Rule("pː", "pp"),
		new Rule("bː", "bb"),
		new Rule("hː", "hh"),
		new Rule("rː", "rr"),
		new Rule("zː", "zz"),
		new Rule("fː", "ff"),
		new Rule("vː", "vv"),
		new Rule("lː", "ll"),
		new Rule("dː", "dd"),
		new Rule("gː", "gg"),
		new Rule("dʒː", "jj"),
		new Rule("dʒ", "j"),
		new Rule("xː", "ħħ"),
		new Rule("x", "ħ"),
		new Rule("çː", "xx"),
		new Rule("ç", "x"),
		new Rule("tɬː", "łł"),
		new Rule("tɬ", "ł"),
		new Rule("ðː", "tth"),
		new Rule("ð", "th"),
		new Rule("tʃː", "ćć"),
		new Rule("tʃ", "ć"),
		new Rule("ʃː", "śś"),
		new Rule("ʃ", "ś"),
		new Rule("ʒː", "źź"),
		new Rule("ʒ", "ź"),
		new Rule("ŋ", "ng"),

		new Rule("ʔ", "q"),
		new Rule("ɲ", "ń"),
		new Rule("ʷ", "w"),
		new Rule("ʲ", "y"),
		new Rule("aː", "ā"),
		new Rule("eː", "ē"),
		new Rule("iː", "ī"),
		new Rule("oː", "ō"),
		new Rule("uː", "ū")
	});

	final static Orthography O2 = new Orthography(new Rule[] {
		new Rule("kː", "kk"),
		new Rule("tː", "tt"),
		new Rule("sː", "ss"),
		new Rule("ʃː", "ssh"),
		new Rule("dʒː", "jj"),
		new Rule("tsː", "tts"),
		new Rule("nː", "nn"),
		new Rule("mː", "mm"),
		new Rule("pː", "pp"),
		new Rule("hː", "h"),
		new Rule("rː", "rr"),
		new Rule("zː", "zz"),
		new Rule("fː", "ff"),
		new Rule("vː", "vv"),
		new Rule("lː", "ll"),
		new Rule("dː", "dd"),
		new Rule("hu", "fu"),

		new Rule("ɲ", "ny"),
		new Rule("j", "y"),
		new Rule("ʒ", "j"),
		new Rule("ʃ", "sh"),
		new Rule("dʒ", "j"),
		new Rule("tʃ", "ch"),
		new Rule("ð", "z"),
		new Rule("ʔ", "'"),
		new Rule("dʒe", "jae"),
		new Rule("tʃe", "chae"),
		new Rule("ti", "chi"),
		new Rule("x", "h"),
		new Rule("zi", "ji"),
		new Rule("tu", "tsu"),
		new Rule("ʃe", "shae"),
		new Rule("tsa", "tsua"),
		new Rule("tso", "tsuo"),
		new Rule("tse", "tsue"),
		new Rule("tsi", "tsui"),
		new Rule("ʷ", "w"),
		new Rule("ʲ", "y"),
		new Rule("aː", "ā"),
		new Rule("eː", "ē"),
		new Rule("iː", "ī"),
		new Rule("oː", "ō"),
		new Rule("uː", "ū"),
	});

	public Orthography(Rule[] rules) {
		this.rules = Arrays.asList(rules);
	}

	public String apply(String s) {
		for (int i = 0; i < rules.size(); i++)
			s = rules.get(i).apply(s);
		return s;
	}
}