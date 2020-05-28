package ling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class NewLanguage {
	static Random rand;

	public static enum BACKNESS {
		FRONT, CENTRAL, BACK
	}

	public static enum CLOSENESS {
		CLOSE, NEAR_CLOSE, CLOSE_MID, MID, OPEN_MID, NEAR_OPEN, OPEN
	}

	public static enum PLACE {
		BILABIAL, LABIODENTAL, LINGUOLABIAL, DENTAL, ALVEOLAR, POSTALVEOLAR, RETROFLEX, PALATAL, VELAR, UVULAR,
		EPIGLOTTAL, GLOTTAL
	}

	public static enum MANNER {
		NASAL, STOP, S_AFFRICATE, N_AFFRICATE, S_FRICATIVE, N_FRICATIVE, APPROXIMANT, TAP, TRILL, L_AFFRICATE,
		L_FRICATIVE, L_APPROXIMANT, L_TAP
	}

	public static ArrayList<Consonant> BILABIALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> LABIODENTALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> LINGUOLABIALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> DENTALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> ALVEOLARS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> POSTALVEOLARS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> RETROFLEXES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> PALATALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> VELARS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> UVULARS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> EPIGLOTTALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> GLOTTALS = new ArrayList<Consonant>();

	public static ArrayList<Consonant> NASALS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> STOPS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> S_AFFRICATES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> N_AFFRICATES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> S_FRICATIVES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> N_FRICATIVES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> APPROXIMANTS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> TAPS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> TRILLS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> L_AFFRICATES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> L_FRICATIVES = new ArrayList<Consonant>();
	public static ArrayList<Consonant> L_APPROXIMANTS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> L_TAPS = new ArrayList<Consonant>();

	public static ArrayList<Consonant> VOICELESS = new ArrayList<Consonant>();
	public static ArrayList<Consonant> VOICED = new ArrayList<Consonant>();

	public static ArrayList<Vowel> FRONTS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> CENTRALS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> BACKS = new ArrayList<Vowel>();

	public static ArrayList<Vowel> CLOSES = new ArrayList<Vowel>();
	public static ArrayList<Vowel> NEAR_CLOSES = new ArrayList<Vowel>();
	public static ArrayList<Vowel> CLOSE_MIDS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> MIDS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> OPEN_MIDS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> NEAR_OPENS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> OPENS = new ArrayList<Vowel>();

	public static ArrayList<Vowel> UNROUNDEDS = new ArrayList<Vowel>();
	public static ArrayList<Vowel> ROUNDEDS = new ArrayList<Vowel>();

	public static final Consonant CONS[] = {
			// BILABIALS
			new Consonant(PLACE.BILABIAL, MANNER.NASAL, false, "m̥", new String[] { "mh", "hm", "ḿ", "m" }),
			new Consonant(PLACE.BILABIAL, MANNER.NASAL, true, "m", new String[] { "m" }),
			new Consonant(PLACE.BILABIAL, MANNER.STOP, false, "p", new String[] { "p" }),
			new Consonant(PLACE.BILABIAL, MANNER.STOP, true, "b", new String[] { "b" }),
			/*
			 * new Consonant(PLACE.BILABIAL, MANNER.N_AFFRICATE, false, "pɸ", new String[] {
			 * "pf", "ṕ", "ph" }), new Consonant(PLACE.BILABIAL, MANNER.N_AFFRICATE, true,
			 * "bβ", new String[] { "bv", "b́", "bh" }),
			 */ // DOESN'T EXIST PHONEMICALLY IN ANY LANGUAGE smh
			new Consonant(PLACE.BILABIAL, MANNER.N_FRICATIVE, false, "ɸ", new String[] { "fh", "f́", "f" }),
			new Consonant(PLACE.BILABIAL, MANNER.N_FRICATIVE, true, "β", new String[] { "bh", "v̈", "b" }),
			new Consonant(PLACE.BILABIAL, MANNER.APPROXIMANT, false, "ɸ", new String[] { "fh", "f́", "f" }),
			new Consonant(PLACE.BILABIAL, MANNER.APPROXIMANT, true, "β", new String[] { "bh", "v̈", "b", }),
			/*
			 * new Consonant(PLACE.BILABIAL, MANNER.TAP, true, "ⱱ̟", new String[] { "vh",
			 * "v", "w" }),
			 */ // DOES THIS PHONEME EVEN EXIST?
			/*
			 * new Consonant(PLACE.BILABIAL, MANNER.TRILL, false, "ʙ̥", new String[] { "bs",
			 * "b" }),
			 */ // EXTREMELY RARE
			new Consonant(PLACE.BILABIAL, MANNER.TRILL, true, "ʙ", new String[] { "mb", "b" }),
			// LABIODENTALS
			new Consonant(PLACE.LABIODENTAL, MANNER.NASAL, true, "ɱ", new String[] { "m", "n", "mf" }),
			/*
			 * new Consonant(PLACE.LABIODENTAL, MANNER.STOP, false, "p̪", new String[] {
			 * "pn", "p" }), new Consonant(PLACE.LABIODENTAL, MANNER.STOP, true, "b̪", new
			 * String[] { "bn", "b" }), new Consonant(PLACE.LABIODENTAL, MANNER.N_AFFRICATE,
			 * false, "p̪f", new String[] { "pf", "f" }), new Consonant(PLACE.LABIODENTAL,
			 * MANNER.N_AFFRICATE, true, "b̪v", new String[] { "bv", "f", "v" }),
			 */ // A little too rare
			new Consonant(PLACE.LABIODENTAL, MANNER.N_FRICATIVE, false, "f", new String[] { "f" }),
			new Consonant(PLACE.LABIODENTAL, MANNER.N_FRICATIVE, true, "v", new String[] { "v" }),
			/*
			 * new Consonant(PLACE.LABIODENTAL, MANNER.APPROXIMANT, false, "ʋ̥", new
			 * String[] { "v́", "f" }),
			 */ // not common enough to be included
			//new Consonant(PLACE.LABIODENTAL, MANNER.APPROXIMANT, true, "ʋ", new String[] { "w", "v" }),
			//new Consonant(PLACE.LABIODENTAL, MANNER.TAP, true, "ⱱ", new String[] { "vw", "v̇" }),
			// LINGUOLABIAL
			/*
			 * new Consonant(PLACE.LINGUOLABIAL, MANNER.NASAL, true, "n̼", new String[] {
			 * "nh", "m" }), new Consonant(PLACE.LINGUOLABIAL, MANNER.STOP, false, "t̼", new
			 * String[] { "pt", "t" }), new Consonant(PLACE.LINGUOLABIAL, MANNER.STOP, true,
			 * "d̼", new String[] { "bd", "d" }), new Consonant(PLACE.LINGUOLABIAL,
			 * MANNER.N_FRICATIVE, false, "θ̼", new String[] { "th", "þ" }), new
			 * Consonant(PLACE.LINGUOLABIAL, MANNER.N_FRICATIVE, true, "ð̼", new String[] {
			 * "th", "dh", "ð" }), new Consonant(PLACE.LINGUOLABIAL, MANNER.APPROXIMANT,
			 * true, "ð̼", new String[] { "th", "dh", "ð" }), new
			 * Consonant(PLACE.LINGUOLABIAL, MANNER.TAP, true, "ɾ̼", new String[] { "ŕ",
			 * "rh" }),
			 */ // LINGUOLABIALS AREN'T REAL
			// DENTAL
			/*
			 * new Consonant(PLACE.DENTAL, MANNER.NASAL, false, "n̥", new String[] { "hn",
			 * "n" }),
			 */ // RARE
			new Consonant(PLACE.DENTAL, MANNER.NASAL, true, "n", new String[] { "n" }),
			new Consonant(PLACE.DENTAL, MANNER.STOP, false, "t", new String[] { "t" }),
			new Consonant(PLACE.DENTAL, MANNER.STOP, true, "d", new String[] { "d" }),
			/*
			 * new Consonant(PLACE.DENTAL, MANNER.N_AFFRICATE, false, "t̪θ", new String[] {
			 * "thn" }), new Consonant(PLACE.DENTAL, MANNER.N_AFFRICATE, true, "d̪ð", new
			 * String[] { "dhn" }),
			 */ // VERY RARE
			new Consonant(PLACE.DENTAL, MANNER.N_FRICATIVE, false, "θ", new String[] { "th", "þ" }),
			new Consonant(PLACE.DENTAL, MANNER.N_FRICATIVE, true, "ð", new String[] { "dh", "ð" }),
			// ALVEOLAR
			/*
			 * new Consonant(PLACE.ALVEOLAR, MANNER.NASAL, false, "n̥", new String[] { "hn",
			 * "n" }),
			 */
			new Consonant(PLACE.ALVEOLAR, MANNER.NASAL, true, "n", new String[] { "n" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.STOP, false, "t", new String[] { "t" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.STOP, true, "d", new String[] { "d" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.S_AFFRICATE, false, "ts", new String[] { "ts" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.S_AFFRICATE, true, "dz", new String[] { "dz" }),
			/*
			 * new Consonant(PLACE.ALVEOLAR, MANNER.N_AFFRICATE, false, "tɹ̝̊", new String[]
			 * { "trh" }), new Consonant(PLACE.ALVEOLAR, MANNER.N_AFFRICATE, true, "dɹ̝",
			 * new String[] { "drh" }),
			 */ // BOTH RARE
			new Consonant(PLACE.ALVEOLAR, MANNER.S_FRICATIVE, false, "s", new String[] { "s" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.S_FRICATIVE, true, "z", new String[] { "z" }),
			/*
			 * new Consonant(PLACE.ALVEOLAR, MANNER.N_FRICATIVE, false, "θ̠", new String[] {
			 * "th", "þ", "thn", "þn" }), // maybe // remove? new Consonant(PLACE.ALVEOLAR,
			 * MANNER.N_FRICATIVE, true, "ð̠", new String[] { "th", "dh", "ð", "thn", "dhn",
			 * "ðn" }), // maybe remove?s /* new Consonant(PLACE.ALVEOLAR,
			 * MANNER.APPROXIMANT, false, "ɹ̥", new String[] { "rh", "r", "yh" }),
			 */ // EXTREMELY RARE
			new Consonant(PLACE.ALVEOLAR, MANNER.APPROXIMANT, true, "ɹ", new String[] { "r", "l" }),
			/*
			 * new Consonant(PLACE.ALVEOLAR, MANNER.TAP, false, "ɾ̥", new String[] { "rh",
			 * "r" }),
			 */ // EXTREMELY RARE
			new Consonant(PLACE.ALVEOLAR, MANNER.TAP, true, "ɾ", new String[] { "r" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.TRILL, false, "r̥", new String[] { "rh", "sr" }), // honestly don't know
																									// if should keep
			new Consonant(PLACE.ALVEOLAR, MANNER.TRILL, true, "r", new String[] { "r" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_AFFRICATE, false, "tɬ", new String[] { "tl" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_AFFRICATE, true, "dɮ", new String[] { "dl" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_FRICATIVE, false, "ɬ", new String[] { "ł" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_FRICATIVE, true, "ɮ", new String[] { "dł" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_APPROXIMANT, false, "l̥", new String[] { "lh", "l" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_APPROXIMANT, true, "l", new String[] { "l" }),
			new Consonant(PLACE.ALVEOLAR, MANNER.L_TAP, true, "ɺ", new String[] { "r", "l" }),
			// POSTALVEOLAR
			/*
			 * new Consonant(PLACE.POSTALVEOLAR, MANNER.NASAL, false, "n̥", new String[] {
			 * "hn", "n" }),
			 */ // NOT COMMON
			new Consonant(PLACE.POSTALVEOLAR, MANNER.NASAL, true, "n", new String[] { "n" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.STOP, false, "t", new String[] { "t" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.STOP, true, "d", new String[] { "d" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.S_AFFRICATE, false, "tʃ",
					new String[] { "ć" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.S_AFFRICATE, true, "dʒ", new String[] { "j" }),
			/*
			 * new Consonant(PLACE.POSTALVEOLAR, MANNER.N_AFFRICATE, false, "t̠ɹ̠̊˔", new
			 * String[] { "trh" }), new Consonant(PLACE.POSTALVEOLAR, MANNER.N_AFFRICATE,
			 * true, "d̠ɹ̠˔", new String[] { "drh" }),
			 */ // not even real smh
			new Consonant(PLACE.POSTALVEOLAR, MANNER.S_FRICATIVE, false, "ʃ", new String[] { "ś" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.S_FRICATIVE, true, "ʒ", new String[] { "zh", "j", "s" }),
			/*
			 * new Consonant(PLACE.POSTALVEOLAR, MANNER.N_FRICATIVE, false, "ɹ̠̊˔", new
			 * String[] { "rh", "lh", "r" }), new Consonant(PLACE.POSTALVEOLAR,
			 * MANNER.N_FRICATIVE, true, "ɹ̠˔", new String[] { "r", "l", "yh" }), new
			 * Consonant(PLACE.POSTALVEOLAR, MANNER.APPROXIMANT, false, "ɹ̥", new String[] {
			 * "r", "l" }),
			 */ // what the fuck
			new Consonant(PLACE.POSTALVEOLAR, MANNER.APPROXIMANT, true, "ɹ", new String[] { "r", "l" }),
			/*
			 * new Consonant(PLACE.POSTALVEOLAR, MANNER.TAP, false, "ɾ̥", new String[] {
			 * "rh", "r" }),
			 */
			new Consonant(PLACE.POSTALVEOLAR, MANNER.TAP, true, "ɾ", new String[] { "r" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.TRILL, false, "r̥", new String[] { "rh", "sr" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.TRILL, true, "r", new String[] { "r" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_AFFRICATE, false, "tɬ", new String[] { "tl" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_AFFRICATE, true, "dɮ", new String[] { "dl" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_FRICATIVE, false, "ɬ", new String[] { "ł" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_FRICATIVE, true, "ɮ", new String[] { "dł" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_APPROXIMANT, false, "l̥", new String[] { "lh", "l" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_APPROXIMANT, true, "l", new String[] { "l" }),
			new Consonant(PLACE.POSTALVEOLAR, MANNER.L_TAP, true, "ɺ", new String[] { "r", "l" }),
			// RETROFLEX
			/*
			 * new Consonant(PLACE.RETROFLEX, MANNER.NASAL, false, "ɳ̊", new String[] {
			 * "hñ", "gnh", "hny" }),
			 */
			new Consonant(PLACE.RETROFLEX, MANNER.NASAL, true, "ɳ", new String[] { "ń" }),
			new Consonant(PLACE.RETROFLEX, MANNER.STOP, false, "ʈ", new String[] { "ţ" }),
			new Consonant(PLACE.RETROFLEX, MANNER.STOP, true, "ɖ", new String[] { "ḑ" }),
			new Consonant(PLACE.RETROFLEX, MANNER.S_AFFRICATE, false, "ʈʂ",
					new String[] { "tš", "č"}),
			new Consonant(PLACE.RETROFLEX, MANNER.S_AFFRICATE, true, "ɖʐ", new String[] { "dž" }),
			new Consonant(PLACE.RETROFLEX, MANNER.S_FRICATIVE, false, "ʂ", new String[] { "š" }),
			new Consonant(PLACE.RETROFLEX, MANNER.S_FRICATIVE, true, "ʐ", new String[] { "ž" }),
			/*
			 * new Consonant(PLACE.RETROFLEX, MANNER.N_FRICATIVE, true, "ɻ˔", new String[] {
			 * "r", "zr" }), new Consonant(PLACE.RETROFLEX, MANNER.APPROXIMANT, false, "ɻ̊",
			 * new String[] { "rh", "r" }),
			 */
			new Consonant(PLACE.RETROFLEX, MANNER.APPROXIMANT, true, "ɻ", new String[] { "rh", "r", "l" }),
			/*
			 * new Consonant(PLACE.RETROFLEX, MANNER.TAP, false, "ɽ̊", new String[] { "r",
			 * "sz" }), new Consonant(PLACE.RETROFLEX, MANNER.TAP, true, "ɽ", new String[] {
			 * "r", "l" }),
			 */
			/*
			 * new Consonant(PLACE.RETROFLEX, MANNER.TRILL, false, "ɽ̊r̥"), new
			 * Consonant(PLACE.RETROFLEX, MANNER.TRILL, true, "ɽr"), new
			 * Consonant(PLACE.RETROFLEX, MANNER.L_AFFRICATE, false, "ʈɭ̊˔"), new
			 * Consonant(PLACE.RETROFLEX, MANNER.L_FRICATIVE, false, "ɭ̊˔"), new
			 * Consonant(PLACE.RETROFLEX, MANNER.L_FRICATIVE, true, "ɭ˔"), new
			 * Consonant(PLACE.RETROFLEX, MANNER.L_APPROXIMANT, false, "ɭ̊"),
			 */ // Fake sounds made to confuse linguists
			new Consonant(PLACE.RETROFLEX, MANNER.L_APPROXIMANT, true, "ɭ", new String[] { "ļ" }),
			// new Consonant(PLACE.RETROFLEX, MANNER.L_TAP, true, "ɭ̆"),
			// PALATAL
			// new Consonant(PLACE.PALATAL, MANNER.NASAL, false, "ɲ̊"),
			new Consonant(PLACE.PALATAL, MANNER.NASAL, true, "ɲ", new String[] { "ñ", "gn", "nh", "ny" }),
			new Consonant(PLACE.PALATAL, MANNER.STOP, false, "c", new String[] { "q" }),
			// new Consonant(PLACE.PALATAL, MANNER.STOP, true, "ɟ"),
			new Consonant(PLACE.PALATAL, MANNER.S_AFFRICATE, false, "tɕ", new String[] { "tj" }),
			new Consonant(PLACE.PALATAL, MANNER.S_AFFRICATE, true, "dʑ", new String[] { "dj" }),
			// new Consonant(PLACE.PALATAL, MANNER.N_AFFRICATE, false, "cç"),
			// new Consonant(PLACE.PALATAL, MANNER.N_AFFRICATE, true, "ɟʝ"),
			new Consonant(PLACE.PALATAL, MANNER.S_FRICATIVE, false, "ɕ", new String[] { "ç", "ś", "sj" }),
			new Consonant(PLACE.PALATAL, MANNER.S_FRICATIVE, true, "ʑ", new String[] { "ź", "zj" }),
			new Consonant(PLACE.PALATAL, MANNER.N_FRICATIVE, false, "ç", new String[] { "ḩ", "hj", "h" }),
			// new Consonant(PLACE.PALATAL, MANNER.N_FRICATIVE, true, "ʝ"),
			// new Consonant(PLACE.PALATAL, MANNER.APPROXIMANT, false, "j̊"),
			new Consonant(PLACE.PALATAL, MANNER.APPROXIMANT, true, "j", new String[] { "y" }),
			// new Consonant(PLACE.PALATAL, MANNER.L_AFFRICATE, false, "cʎ̝̊"),
			// new Consonant(PLACE.PALATAL, MANNER.L_FRICATIVE, false, "ʎ̝̊"),
			// new Consonant(PLACE.PALATAL, MANNER.L_FRICATIVE, true, "ʎ̝"),
			// new Consonant(PLACE.PALATAL, MANNER.L_APPROXIMANT, false, "ʎ̥"),
			new Consonant(PLACE.PALATAL, MANNER.L_APPROXIMANT, true, "ʎ", new String[] { "lj", "lh", "gl" }),
			// new Consonant(PLACE.PALATAL, MANNER.L_TAP, true, "ʎ̆"),
			// VELAR
			// new Consonant(PLACE.VELAR, MANNER.NASAL, false, "ŋ̊"),
			new Consonant(PLACE.VELAR, MANNER.NASAL, true, "ŋ", new String[] { "ng" }),
			new Consonant(PLACE.VELAR, MANNER.STOP, false, "k", new String[] { "k" }),
			new Consonant(PLACE.VELAR, MANNER.STOP, true, "g", new String[] { "g" }),
			// new Consonant(PLACE.VELAR, MANNER.N_AFFRICATE, false, "kx"),
			// new Consonant(PLACE.VELAR, MANNER.N_AFFRICATE, true, "gɣ"),
			new Consonant(PLACE.VELAR, MANNER.N_FRICATIVE, false, "x", new String[] { "x", "kh" }),
			new Consonant(PLACE.VELAR, MANNER.N_FRICATIVE, true, "ɣ", new String[] { "ÿ", "gh" }),
			// new Consonant(PLACE.VELAR, MANNER.APPROXIMANT, false, "ɰ̊"),
			new Consonant(PLACE.VELAR, MANNER.APPROXIMANT, true, "ɰ", new String[] { "w" }),
			// new Consonant(PLACE.VELAR, MANNER.L_AFFRICATE, false, "kʟ̝̊"),
			// new Consonant(PLACE.VELAR, MANNER.L_AFFRICATE, true, "gʟ̝ "),
			// new Consonant(PLACE.VELAR, MANNER.L_FRICATIVE, false, "ʟ̝̊"),
			// new Consonant(PLACE.VELAR, MANNER.L_FRICATIVE, true, "ʟ̝"),
			// new Consonant(PLACE.VELAR, MANNER.L_APPROXIMANT, false, "ʟ̥"),
			// new Consonant(PLACE.VELAR, MANNER.L_APPROXIMANT, true, "ʟ"),
			// new Consonant(PLACE.VELAR, MANNER.L_TAP, true, "ʟ̆"),
			// UVULAR
			// new Consonant(PLACE.UVULAR, MANNER.NASAL, true, "ɴ"),
			new Consonant(PLACE.UVULAR, MANNER.STOP, false, "q", new String[] { "c", "q", "kx", "qh" }),
			// new Consonant(PLACE.UVULAR, MANNER.STOP, true, "ɢ"),
			// new Consonant(PLACE.UVULAR, MANNER.N_AFFRICATE, false, "qχ"),
			// new Consonant(PLACE.UVULAR, MANNER.N_AFFRICATE, true, "ɢʁ"),
			new Consonant(PLACE.UVULAR, MANNER.N_FRICATIVE, false, "χ", new String[] { "h", "kh", "ch" }),
			new Consonant(PLACE.UVULAR, MANNER.N_FRICATIVE, true, "ʁ", new String[] { "ǵ" }),
			new Consonant(PLACE.UVULAR, MANNER.APPROXIMANT, true, "ʁ", new String[] { "ǵ" }),
			// new Consonant(PLACE.UVULAR, MANNER.TAP, true, "ɢ̆"), new
			// Consonant(PLACE.UVULAR, MANNER.TRILL, false, "ʀ̥"),
			new Consonant(PLACE.UVULAR, MANNER.TRILL, true, "ʀ", new String[] { "r" }),
			// new Consonant(PLACE.UVULAR, MANNER.L_APPROXIMANT, true, "ʟ̠"),
			// EPIGLOTTAL
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.STOP, false, "ʡ"),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.N_AFFRICATE, true, "ʡʢ"),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.N_FRICATIVE, false, "ħ"),
			new Consonant(PLACE.EPIGLOTTAL, MANNER.N_FRICATIVE, true, "ʕ", new String[] { "c", "cj" }),
			new Consonant(PLACE.EPIGLOTTAL, MANNER.APPROXIMANT, true, "ʕ", new String[] { "c", "cj" }),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.TAP, true, "ʡ̆"),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.TRILL, false, "ʜ"),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.TRILL, true, "ʢ"),
			// GLOTTAL
			new Consonant(PLACE.EPIGLOTTAL, MANNER.STOP, false, "ʔ", new String[] { "ʻ" }),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.N_AFFRICATE, false, "ʔh"),
			new Consonant(PLACE.EPIGLOTTAL, MANNER.N_FRICATIVE, false, "h", new String[] { "h" }),
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.N_FRICATIVE, true, "ɦ"),
			new Consonant(PLACE.EPIGLOTTAL, MANNER.APPROXIMANT, false, "h", new String[] { "h" })
			// new Consonant(PLACE.EPIGLOTTAL, MANNER.APPROXIMANT, true, "ʔ̞")
	};

	public static final Vowel VOW[] = {
			// FRONT
			new Vowel(BACKNESS.FRONT, CLOSENESS.CLOSE, false, "i", new String[] { "i" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.CLOSE, true, "y", new String[] { "i", "ü" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.NEAR_CLOSE, false, "ɪ", new String[] { "i", "e" }),
			// new Vowel(BACKNESS.FRONT, CLOSENESS.NEAR_CLOSE, true, "ʏ"),
			new Vowel(BACKNESS.FRONT, CLOSENESS.CLOSE_MID, false, "e", new String[] { "e", "é", "ei" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.CLOSE_MID, true, "ø", new String[] { "eu" }),
			// new Vowel(BACKNESS.FRONT, CLOSENESS.MID, true, "ø̞"),
			new Vowel(BACKNESS.FRONT, CLOSENESS.OPEN_MID, false, "ɛ", new String[] { "e" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.OPEN_MID, true, "œ", new String[] { "ui", "oa" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.NEAR_OPEN, false, "æ", new String[] { "a", "á" }),
			new Vowel(BACKNESS.FRONT, CLOSENESS.OPEN, false, "a", new String[] { "a" }),
			// new Vowel(BACKNESS.FRONT, CLOSENESS.OPEN, true, "ɶ"),
			// CENTRAL
			new Vowel(BACKNESS.CENTRAL, CLOSENESS.CLOSE, false, "ɨ", new String[] { "ï", "ou" }),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.CLOSE, true, "ʉ"),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.CLOSE_MID, false, "ɘ"),
			new Vowel(BACKNESS.CENTRAL, CLOSENESS.CLOSE_MID, true, "ɵ", new String[] { "ö", "uo" }),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.MID, false, "ə"),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.MID, true, "ə"),
			new Vowel(BACKNESS.CENTRAL, CLOSENESS.OPEN_MID, false, "ɜ", new String[] { "ë", "a" }),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.OPEN_MID, true, "ɞ"),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.NEAR_OPEN, false, "ɐ"),
			// new Vowel(BACKNESS.CENTRAL, CLOSENESS.NEAR_OPEN, true, "ɐ"),
			new Vowel(BACKNESS.CENTRAL, CLOSENESS.OPEN, false, "ä", new String[] { "a", "ä" }),
			// BACK
			new Vowel(BACKNESS.BACK, CLOSENESS.CLOSE, false, "ɯ", new String[] { "u" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.CLOSE, true, "u", new String[] { "u" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.NEAR_CLOSE, true, "ʊ", new String[] { "o", "u", "ó" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.CLOSE_MID, false, "ɤ", new String[] { "eo", "oi", "oe" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.CLOSE_MID, true, "o", new String[] { "o" }),
			// new Vowel(BACKNESS.BACK, CLOSENESS.MID, true, "o̞"),
			new Vowel(BACKNESS.BACK, CLOSENESS.OPEN_MID, false, "ʌ", new String[] { "å", "u", "a", "o" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.OPEN_MID, true, "ɔ", new String[] { "o", "ô", "â" }),
			new Vowel(BACKNESS.BACK, CLOSENESS.OPEN, false, "ɑ", new String[] { "a" })
			// new Vowel(BACKNESS.BACK, CLOSENESS.OPEN, true, "ɒ", new String[] {})
	};

	private ArrayList<Consonant> consonants = new ArrayList<Consonant>();
	private ArrayList<Vowel> vowels = new ArrayList<Vowel>();

	private ArrayList<Consonant> initials = new ArrayList<Consonant>();
	private ArrayList<Consonant> finals = new ArrayList<Consonant>();

	private static enum RULESET {
		MEDIAL_VOICING, FINAL_DEVOICING, INITIAL_VOICING, INITIAL_DEVOICING
	}

	private static final String SYL[] = { "V", "CV", "VF", "CVF" };

	public int min;
	public int max;

	public NewLanguage(Random rand) {
		this.rand = rand;

		for (Consonant c : CONS) {
			consonants.add((Consonant) c.clone());
		}

		for (Vowel v : VOW) {
			vowels.add((Vowel) v.clone());
		}

		setConsonants();
		setVowels();

		for (Consonant c : consonants) {
			int r = rand.nextInt(3);
			switch (r) {
				case 0:
					initials.add(c);
					break;
				case 1:
					finals.add(c);
					break;
				case 2:
					initials.add(c);
					finals.add(c);
					break;
			}
		}

		min = rand.nextInt(2) + 1;
		max = rand.nextInt(4) + min;
	}

	public ArrayList<ArrayList<Phone>> genRoot() {
		ArrayList<ArrayList<Phone>> word = new ArrayList<ArrayList<Phone>>();
		for (int i = 0; i < min + (min == max ? 0 : rand.nextInt(max - min)); i++) {
			word.add(genSyllable());
		}
		return word;
	}

	public ArrayList<Phone> genSyllable() {
		ArrayList<Phone> syl = new ArrayList<Phone>();
		String temp = SYL[rand.nextInt(SYL.length)];
		for (char c : temp.toCharArray()) {
			switch (c) {
				case 'C':
					syl.add(getRandInitial());
					break;
				case 'V':
					syl.add(getRandVowel());
					break;
				case 'F':
					syl.add(getRandFinal());
					break;
			}
		}
		return syl;
	}

	public Consonant getRandInitial() {
		return initials.get(rand.nextInt(initials.size()));
	}

	public Consonant getRandFinal() {
		return finals.get(rand.nextInt(finals.size()));
	}

	public Vowel getRandVowel() {
		return vowels.get(rand.nextInt(vowels.size()));
	}

	public void setConsonants() {
		if (rand.nextInt(4) == 0) {
			consonants = remove(null, null, false, consonants);
		} else if (rand.nextInt(2) == 0) {
			consonants = remove(null, null, true, consonants);
		}

		if (rand.nextInt(2) == 0)
			consonants = remove(MANNER.L_TAP, null, null, consonants);
		if (rand.nextInt(4) == 0)
			consonants = remove(MANNER.L_APPROXIMANT, null, null, consonants);
		if (rand.nextInt(2) == 0)
			consonants = remove(MANNER.L_FRICATIVE, null, null, consonants);
		if (rand.nextInt(3) == 0)
			consonants = remove(MANNER.L_AFFRICATE, null, null, consonants);
		if (rand.nextInt(6) == 0)
			consonants = remove(MANNER.TRILL, null, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(MANNER.TAP, null, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(MANNER.APPROXIMANT, null, null, consonants);
		if (rand.nextInt(6) == 0)
			consonants = remove(MANNER.N_FRICATIVE, null, null, consonants);
		if (rand.nextInt(10) == 0)
			consonants = remove(MANNER.S_FRICATIVE, null, null, consonants);
		if (rand.nextInt(3) == 0)
			consonants = remove(MANNER.N_AFFRICATE, null, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(MANNER.S_AFFRICATE, null, null, consonants);
		if (rand.nextInt(12) == 0)
			consonants = remove(MANNER.STOP, null, null, consonants);
		if (rand.nextInt(14) == 0)
			consonants = remove(MANNER.NASAL, null, null, consonants);

		if (rand.nextInt(10) == 0)
			consonants = remove(null, PLACE.BILABIAL, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(null, PLACE.LABIODENTAL, null, consonants);
		if (rand.nextInt(4) == 0)
			consonants = remove(null, PLACE.LINGUOLABIAL, null, consonants);
		if (rand.nextInt(6) == 0)
			consonants = remove(null, PLACE.DENTAL, null, consonants);
		if (rand.nextInt(12) == 0)
			consonants = remove(null, PLACE.ALVEOLAR, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(null, PLACE.POSTALVEOLAR, null, consonants);
		if (rand.nextInt(2) == 0)
			consonants = remove(null, PLACE.RETROFLEX, null, consonants);
		if (rand.nextInt(6) == 0)
			consonants = remove(null, PLACE.PALATAL, null, consonants);
		if (rand.nextInt(8) == 0)
			consonants = remove(null, PLACE.VELAR, null, consonants);
		if (rand.nextInt(2) == 0)
			consonants = remove(null, PLACE.UVULAR, null, consonants);
		if (rand.nextInt(2) == 0)
			consonants = remove(null, PLACE.EPIGLOTTAL, null, consonants);
		if (rand.nextInt(3) == 0)
			consonants = remove(null, PLACE.GLOTTAL, null, consonants);

		if (consonants.size() > 10) {
			int cons_r = rand.nextInt(consonants.size() - 10);
			ArrayList<MANNER> manners = new ArrayList<MANNER>();
			manners.addAll(Arrays.asList(MANNER.values()));
			ArrayList<PLACE> places = new ArrayList<PLACE>();
			places.addAll(Arrays.asList(PLACE.values()));

			for (int i = 0; i < cons_r; i++) {
				MANNER r_manner = manners.get(rand.nextInt(manners.size()));
				PLACE r_place = places.get(rand.nextInt(places.size()));
				consonants = remove(r_manner, r_place, null, consonants);
			}
		}
	}

	public void setVowels() {
		if (rand.nextInt(6) == 0) {
			vowels = remove(null, null, false, vowels);
		} else if (rand.nextInt(4) == 0) {
			vowels = remove(null, null, true, vowels);
		}

		// if (rand.nextInt(10) == 0)
		// vowels = remove(BACKNESS.FRONT, null, null, vowels);
		if (rand.nextInt(10) == 0)
			vowels = remove(BACKNESS.CENTRAL, null, null, vowels);
		// if (rand.nextInt(10) == 0)
		// vowels = remove(BACKNESS.BACK, null, null, vowels);

		if (rand.nextInt(10) == 0)
			vowels = remove(null, CLOSENESS.CLOSE, null, vowels);
		if (rand.nextInt(4) == 0)
			vowels = remove(null, CLOSENESS.NEAR_CLOSE, null, vowels);
		if (rand.nextInt(10) == 0)
			vowels = remove(null, CLOSENESS.CLOSE_MID, null, vowels);
		if (rand.nextInt(4) == 0)
			vowels = remove(null, CLOSENESS.MID, null, vowels);
		if (rand.nextInt(10) == 0)
			vowels = remove(null, CLOSENESS.OPEN_MID, null, vowels);
		if (rand.nextInt(4) == 0)
			vowels = remove(null, CLOSENESS.NEAR_OPEN, null, vowels);
		if (rand.nextInt(10) == 0)
			vowels = remove(null, CLOSENESS.OPEN, null, vowels);

		if (vowels.size() > 5) {
			int vow_r = rand.nextInt(vowels.size() - 5);
			ArrayList<BACKNESS> backnesses = new ArrayList<BACKNESS>();
			backnesses.addAll(Arrays.asList(BACKNESS.values()));
			ArrayList<CLOSENESS> closenesses = new ArrayList<CLOSENESS>();
			closenesses.addAll(Arrays.asList(CLOSENESS.values()));

			for (int i = 0; i < vow_r; i++) {
				BACKNESS r_backness = backnesses.get(rand.nextInt(backnesses.size()));
				CLOSENESS r_closenesses = closenesses.get(rand.nextInt(closenesses.size()));
				vowels = remove(r_backness, r_closenesses, null, vowels);
			}
		}
	}

	public static ArrayList<Consonant> remove(MANNER m, PLACE p, Boolean v, ArrayList<Consonant> oldc) {
		ArrayList<Consonant> newc = new ArrayList<Consonant>();
		for (Consonant c : oldc) {
			if ((m == null || (m != null && c.manner == m)) && (p == null || (p != null && c.place == p))
					&& (v == null || (v != null && c.voiced == v)))
				continue;
			newc.add(c);
		}
		return newc;
	}

	public static ArrayList<Vowel> remove(BACKNESS b, CLOSENESS c, Boolean r, ArrayList<Vowel> oldv) {
		ArrayList<Vowel> newv = new ArrayList<Vowel>();
		for (Vowel v : oldv) {
			if ((b == null || (b != null && v.backness == b)) && (c == null || (c != null && v.closeness == c))
					&& (r == null || (r != null && v.rounding == r)))
				continue;
			newv.add(v);
		}
		return newv;
	}

	public void printChart() {
		Object[][] cons = new String[13][12];
		int i = 0;
		for (MANNER m : MANNER.values()) {
			int j = 0;
			for (PLACE p : PLACE.values()) {
				Consonant unvoiced = getConsonant(p, m, false);
				Consonant voiced = getConsonant(p, m, true);
				cons[i][j] = (unvoiced == null ? "_" : unvoiced) + " " + (voiced == null ? "_" : voiced) + "|";
				j++;
			}
			i++;
		}
		System.out.println("CONSONANTS");
		for (final Object[] row : cons) {
			System.out.format("%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s\n", row);
		}

		Object[][] vows = new String[7][3];
		i = 0;
		for (CLOSENESS c : CLOSENESS.values()) {
			int j = 0;
			for (BACKNESS b : BACKNESS.values()) {
				Vowel unrounded = getVowel(b, c, false);
				Vowel rounded = getVowel(b, c, true);
				vows[i][j] = (unrounded == null ? "_" : unrounded) + " " + (rounded == null ? "_" : rounded) + "|";
				j++;
			}
			i++;
		}
		System.out.println("VOWELS");
		for (final Object[] row : vows) {
			System.out.format("%10s%10s%10s\n", row);
		}
	}

	private Consonant getConsonant(PLACE p, MANNER m, boolean voiced) {
		for (Consonant c : consonants) {
			if (c.equals(new Consonant(p, m, voiced, "", null)))
				return c;
		}
		return null;
	}

	private Vowel getVowel(BACKNESS b, CLOSENESS c, boolean rounded) {
		for (Vowel v : vowels) {
			if (v.equals(new Vowel(b, c, rounded, "", null)))
				return v;
		}
		return null;
	}
}

abstract class Phone {
	String value;
	protected String reps[];
	private String chosen_rep;

	public Phone(String value, String[] reps) {
		this.value = value;
		this.reps = reps;
		if (reps != null && reps.length > 0 && NewLanguage.rand != null)
			this.chosen_rep = reps[NewLanguage.rand.nextInt(reps.length)];
	}

	public String toString() {
		return value;
	}

	public String transcribe() {
		return chosen_rep;
	}

	public abstract Phone clone();

	public abstract boolean equals(Phone p);
}

class Consonant extends Phone {
	NewLanguage.PLACE place;
	NewLanguage.MANNER manner;
	boolean voiced;

	public Consonant(NewLanguage.PLACE place, NewLanguage.MANNER manner, boolean voiced, String value, String[] reps) {
		super(value, reps);
		this.place = place;
		this.manner = manner;
		this.voiced = voiced;

		switch (place) {
			case BILABIAL:
				NewLanguage.BILABIALS.add(this);
				break;
			case LABIODENTAL:
				NewLanguage.LABIODENTALS.add(this);
				break;
			case LINGUOLABIAL:
				NewLanguage.LINGUOLABIALS.add(this);
				break;
			case DENTAL:
				NewLanguage.DENTALS.add(this);
				break;
			case ALVEOLAR:
				NewLanguage.ALVEOLARS.add(this);
				break;
			case POSTALVEOLAR:
				NewLanguage.POSTALVEOLARS.add(this);
				break;
			case RETROFLEX:
				NewLanguage.RETROFLEXES.add(this);
				break;
			case PALATAL:
				NewLanguage.PALATALS.add(this);
				break;
			case VELAR:
				NewLanguage.VELARS.add(this);
				break;
			case UVULAR:
				NewLanguage.UVULARS.add(this);
				break;
			case EPIGLOTTAL:
				NewLanguage.EPIGLOTTALS.add(this);
				break;
			case GLOTTAL:
				NewLanguage.GLOTTALS.add(this);
				break;
		}

		switch (manner) {
			case APPROXIMANT:
				NewLanguage.APPROXIMANTS.add(this);
				break;
			case L_AFFRICATE:
				NewLanguage.L_AFFRICATES.add(this);
				break;
			case L_APPROXIMANT:
				NewLanguage.L_APPROXIMANTS.add(this);
				break;
			case L_FRICATIVE:
				NewLanguage.L_FRICATIVES.add(this);
				break;
			case L_TAP:
				NewLanguage.L_TAPS.add(this);
				break;
			case NASAL:
				NewLanguage.NASALS.add(this);
				break;
			case N_AFFRICATE:
				NewLanguage.N_AFFRICATES.add(this);
				break;
			case N_FRICATIVE:
				NewLanguage.N_FRICATIVES.add(this);
				break;
			case STOP:
				NewLanguage.STOPS.add(this);
				break;
			case S_AFFRICATE:
				NewLanguage.S_AFFRICATES.add(this);
				break;
			case S_FRICATIVE:
				NewLanguage.S_FRICATIVES.add(this);
				break;
			case TAP:
				NewLanguage.TAPS.add(this);
				break;
			case TRILL:
				NewLanguage.TRILLS.add(this);
				break;
		}

		if (voiced)
			NewLanguage.VOICED.add(this);
		else
			NewLanguage.VOICELESS.add(this);
	}

	@Override
	public Phone clone() {
		return new Consonant(place, manner, voiced, value, reps);
	}

	@Override
	public boolean equals(Phone p) {
		if (p.getClass() != getClass())
			return false;
		Consonant c = (Consonant) p;
		return (c.place == place && c.manner == manner && c.voiced == voiced);
	}
}

class Vowel extends Phone {
	NewLanguage.BACKNESS backness;
	NewLanguage.CLOSENESS closeness;
	boolean rounding;

	public Vowel(NewLanguage.BACKNESS backness, NewLanguage.CLOSENESS closeness, boolean rounding, String value,
			String[] reps) {
		super(value, reps);
		this.backness = backness;
		this.closeness = closeness;
		this.rounding = rounding;

		switch (backness) {
			case BACK:
				NewLanguage.BACKS.add(this);
				break;
			case CENTRAL:
				NewLanguage.CENTRALS.add(this);
				break;
			case FRONT:
				NewLanguage.FRONTS.add(this);
				break;
		}

		switch (closeness) {
			case CLOSE:
				NewLanguage.CLOSES.add(this);
				break;
			case CLOSE_MID:
				NewLanguage.CLOSE_MIDS.add(this);
				break;
			case MID:
				NewLanguage.MIDS.add(this);
				break;
			case NEAR_CLOSE:
				NewLanguage.NEAR_CLOSES.add(this);
				break;
			case NEAR_OPEN:
				NewLanguage.NEAR_OPENS.add(this);
				break;
			case OPEN:
				NewLanguage.OPENS.add(this);
				break;
			case OPEN_MID:
				NewLanguage.OPEN_MIDS.add(this);
				break;
		}

		if (rounding)
			NewLanguage.ROUNDEDS.add(this);
		else
			NewLanguage.UNROUNDEDS.add(this);
	}

	@Override
	public Phone clone() {
		return new Vowel(backness, closeness, rounding, value, reps);
	}

	@Override
	public boolean equals(Phone p) {
		if (p.getClass() != getClass())
			return false;
		Vowel v = (Vowel) p;
		return (v.backness == backness && v.closeness == closeness && v.rounding == rounding);
	}
}