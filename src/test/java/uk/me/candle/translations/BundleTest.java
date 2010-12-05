package uk.me.candle.translations;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Wheat
 */
public class BundleTest {

	@Test
	public void testNoParamsEn() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("there are no parameters", b.noParams());
	}

	@Test
	public void testNoParamsFr() throws Exception {
		Locale locale = Locale.FRENCH;
		Properties trns = TranslationBundle.getProperties();
		trns.setProperty("noParams", "Rien de truc");
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("Rien de truc", b.noParams());
	}

	@Test(expected=MissingResourceException.class)
	public void testMissing() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = new Properties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO
				);
		assertEquals("there are no parameters", b.noParams());
	}

	@Test
	public void testMissingAllowed() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = new Properties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.YES,
				Bundle.LoadIgnoreExtra.YES,
				Bundle.LoadIgnoreParameterMisMatch.YES
				);
		assertEquals("noParams", b.noParams());
	}

	@Test(expected=MissingResourceException.class)
	public void testParamMisMatchA() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		trns.setProperty("noParams", "{0}, {1}");
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO
				);
		assertEquals("there are no parameters", b.noParams());
	}

	@Test(expected=MissingResourceException.class)
	public void testParamMisMatchB() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		trns.setProperty("oneParam", "no parameters");
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO
				);
		assertEquals("no parameters", b.oneParam("ss"));
	}

	@Test
	public void testParamMisMatchBAllowed() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		trns.setProperty("oneParam", "no parameters");
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("no parameters", b.oneParam("ss"));
	}

	@Test(expected=MissingResourceException.class)
	public void testExtra() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		trns.setProperty("this is extra", "more then enough");
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO
				);
		assertEquals("there are no parameters", b.noParams());
	}

	@Test
	public void testOneParam() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("one parameter, and it is aa", b.oneParam((Object) "aa"));
	}

	@Test
	public void testTwoParams() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("two more params: aa bb", b.twoParams("aa", "bb"));
	}

	@Test
	public void testLotsOfParams() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("Fifteen params: a b c d e f g h i j k l m n o", b.lotsOfParams("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"));
	}

	@Test
	public void testIntegerObject() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("There are no elements.", b.integerObject(Integer.valueOf(0)));
		assertEquals("There are 2 elements.", b.integerObject(Integer.valueOf(2)));
		assertEquals("There is one element.", b.integerObject(Integer.valueOf(1)));
		assertEquals("There are 54 elements.", b.integerObject(Integer.valueOf(54)));
	}

	@Test
	public void testNonObject() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("This isn't a strict java.lang.Object: aa", b.nonObject("aa"));
	}

	@Test
	public void testNonObjects() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("Nor are these: aa. Container{s=bb i=3}", b.nonObjects("aa", new Container("bb", 3)));
	}

	@Test
	public void testTypes() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("oo ztrue b4 cr s54 i1,111 l99,999,999,999 f3.2 d4.6"
				, b.types("o", true, (byte)4, 'r', (short)54, 1111, 99999999999L, 3.2F, 4.6D));
	}

	@Test
	public void testPrimitiveByte() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("byte: 4", b.primitiveByte((byte) 4));
	}

	@Test
	public void testPrimitiveShort() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("short: 5", b.primitiveShort((short) 5));
	}

	@Test
	public void testPrimitiveChar() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("char: a", b.primitiveChar('a'));
	}

	@Test
	public void testPrimitiveInt() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("int: 66", b.primitiveInt(66));
	}

	@Test
	public void testPrimitiveLong() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("long: 99,999,999,999,999", b.primitiveLong(99999999999999L));
	}

	@Test
	public void testPrimitiveFloat() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("float: 1.2", b.primitiveFloat(1.2f));
	}

	@Test
	public void testPrimitiveFloat2() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("float: 10,000.5", b.primitiveFloat(10000.5f));
	}

	@Test
	public void testPrimitiveFloatFr() throws Exception {
		Locale locale = Locale.FRENCH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("float: 1,2", b.primitiveFloat(1.2f));
	}

	@Test
	public void testPrimitiveFloatFr2() throws Exception {
		Locale locale = Locale.FRENCH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("float: 10\u00a0000,5", b.primitiveFloat(10000.5f));
	}

	@Test
	public void testPrimitiveDouble() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("double: 4.56", b.primitiveDouble(4.56d));
	}

	@Test
	public void testPrimitiveDoubleFr() throws Exception {
		Locale locale = Locale.FRENCH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("double: 4,56", b.primitiveDouble(4.56d));
	}

	@Test
	public void testPrimitiveDouble2() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("double: 7,654,321.099", b.primitiveDouble(7654321.0987d));
	}

	@Test
	public void testPrimitiveDoubleFr2() throws Exception {
		Locale locale = Locale.FRENCH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("double: 7\u00a0654\u00a0321,099", b.primitiveDouble(7654321.0987d));
	}

	@Test
	public void testOverloadObject() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("overloaded Container{s=aa i=4}", b.overload(new Container("aa", 4)));
	}

	@Test
	public void testOverloadString() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("overloaded ss", b.overload("ss"));
	}

	@Test
	public void testOverloadDouble() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("overloaded 5.5", b.overload(5.5));
	}

	@Test
	public void testOverloadInt() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("overloaded 5", b.overload(5));
	}

	@Test
	public void testSubParameter() throws Exception {
		Locale locale = Locale.ENGLISH;
		Properties trns = TranslationBundle.getProperties();
		TranslationBundle b = Bundle.load(TranslationBundle.class, locale, trns);
		assertEquals("name version", b.subPatternParameter("name", "version", 0, 0, null));
		assertEquals("name version - profile", b.subPatternParameter("name", "version", 0, 1, "profile"));
		assertEquals("name version flag", b.subPatternParameter("name", "version", 1, 0, null));
		assertEquals("name version flag - profile", b.subPatternParameter("name", "version", 1, 1, "profile"));
	}
	@Test
	public void testSubParameterFailBundle() throws Exception {
		//{0} {1}{2,choice,1# portable}{3,choice,1< - {4}}
		Locale locale = Locale.ENGLISH;
		Properties trns = SubPatternBundle.getProperties();
		SubPatternBundle b = Bundle.load(SubPatternBundle.class, locale, trns,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO);
		assertEquals("name version", b.subPatternParameter("name", "version", 0, 0, null));
		assertEquals("name version - profile", b.subPatternParameter("name", "version", 0, 1, "profile"));
		assertEquals("name version flag", b.subPatternParameter("name", "version", 1, 0, null));
		assertEquals("name version flag - profile", b.subPatternParameter("name", "version", 1, 1, "profile"));
	}

	// tests to see if it is picking up the default language

	Locale getLocale(String language, String country, String varient) {
		Locale bestMatch = null;
		for (Locale l : Locale.getAvailableLocales()) {
			if (l.getLanguage().equals(language)) {
				bestMatch = l;
				if (l.getCountry().equals(country)) {
					bestMatch = l;
					if (l.getVariant().equals(varient)) {
						return l;
					}
				}
			}
		}
		return bestMatch;
	}

	@Test
	public void testDefaultLanguageSimple1() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.KOREAN); // no translations
		assertEquals("simple", ssb.simple());
	}
	@Test
	public void testDefaultLanguageSimple2() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.KOREAN); // no translations
		assertEquals("simple int 1.", ssb.simpleOne(1));
	}
	@Test
	public void testDefaultLanguageSimple3() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("bg simple int 1.", ssb.simpleOne(1));
	}
	@Test
	public void testDefaultLanguageSimple4() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("de simple int 1.", ssb.simpleOne(1));
	}

	@Test
	public void testDefaultLanguageDefaultOnly() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.ENGLISH);
		assertEquals("default only", ssb.defaultOnly());
		SimpleSmallBundle ssbBG = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("default only", ssbBG.defaultOnly());
		SimpleSmallBundle ssbJA = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "", ""));
		assertEquals("default only", ssbJA.defaultOnly());
		SimpleSmallBundle ssbJAJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", ""));
		assertEquals("default only", ssbJAJP.defaultOnly());
		SimpleSmallBundle ssbJAJPJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", "JP"));
		assertEquals("default only", ssbJAJPJP.defaultOnly());
		SimpleSmallBundle ssbDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("de default only", ssbDE.defaultOnly());
		SimpleSmallBundle ssbDEDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "DE", ""));
		assertEquals("de_de default only", ssbDEDE.defaultOnly());
	}
	
	@Test
	public void testDefaultLanguageDefaultBg() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.ENGLISH);
		assertEquals("default and Bulgarian", ssb.defaultBg());
		SimpleSmallBundle ssbBG = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("Bulgarian", ssbBG.defaultBg());
		SimpleSmallBundle ssbJA = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "", ""));
		assertEquals("default and Bulgarian", ssbJA.defaultBg());
		SimpleSmallBundle ssbJAJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", ""));
		assertEquals("default and Bulgarian", ssbJAJP.defaultBg());
		SimpleSmallBundle ssbJAJPJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", "JP"));
		assertEquals("default and Bulgarian", ssbJAJPJP.defaultBg());
		SimpleSmallBundle ssbDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("de default and Bulgarian", ssbDE.defaultBg());
		SimpleSmallBundle ssbDEDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "DE", ""));
		assertEquals("de_de default and Bulgarian", ssbDEDE.defaultBg());
	}
	
	@Test
	public void testDefaultLanguageDefaultBgJa() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.ENGLISH);
		assertEquals("default, Bulgarian and Japanese", ssb.defaultBgJa());
		SimpleSmallBundle ssbBG = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("Bulgarian", ssbBG.defaultBgJa());
		SimpleSmallBundle ssbJA = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "", ""));
		assertEquals("Japanese", ssbJA.defaultBgJa());
		SimpleSmallBundle ssbJAJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", ""));
		assertEquals("Japanese", ssbJAJP.defaultBgJa());
		SimpleSmallBundle ssbJAJPJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", "JP"));
		assertEquals("Japanese", ssbJAJPJP.defaultBgJa());
		SimpleSmallBundle ssbDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("de default, Bulgarian and Japanese", ssbDE.defaultBgJa());
		SimpleSmallBundle ssbDEDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "DE", ""));
		assertEquals("de_de default, Bulgarian and Japanese", ssbDEDE.defaultBgJa());
	}

	@Test
	public void testDefaultLanguageDefaultBgJaJp() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.ENGLISH);
		assertEquals("default, Bulgarian and japanese variant", ssb.defaultBgJaJp());
		SimpleSmallBundle ssbBG = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("Bulgarian", ssbBG.defaultBgJaJp());
		SimpleSmallBundle ssbJA = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "", ""));
		assertEquals("Japanese", ssbJA.defaultBgJaJp());
		SimpleSmallBundle ssbJAJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", ""));
		assertEquals("Japanese varient", ssbJAJP.defaultBgJaJp());
		SimpleSmallBundle ssbJAJPJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", "JP"));
		assertEquals("Japanese varient", ssbJAJPJP.defaultBgJaJp());
		SimpleSmallBundle ssbDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("default, Bulgarian and japanese variant", ssbDE.defaultBgJaJp());
		SimpleSmallBundle ssbDEDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "DE", ""));
		assertEquals("de_de default, Bulgarian and japanese variant", ssbDEDE.defaultBgJaJp());
	}

	@Test
	public void testDefaultLanguageDefaultBgJaJpJp() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class, Locale.ENGLISH);
		assertEquals("default, Bulgarian and japanese sub-variant", ssb.defaultBgJaJpJp());
		SimpleSmallBundle ssbBG = Bundle.load(SimpleSmallBundle.class, getLocale("bg", "", ""));
		assertEquals("Bulgarian", ssbBG.defaultBgJaJpJp());
		SimpleSmallBundle ssbJA = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "", ""));
		assertEquals("Japanese", ssbJA.defaultBgJaJpJp());
		SimpleSmallBundle ssbJAJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", ""));
		assertEquals("Japanese varient", ssbJAJP.defaultBgJaJpJp());
		SimpleSmallBundle ssbJAJPJP = Bundle.load(SimpleSmallBundle.class, getLocale("ja", "JP", "JP"));
		assertEquals("Japanese sub-varient", ssbJAJPJP.defaultBgJaJpJp());
		SimpleSmallBundle ssbDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "", ""));
		assertEquals("default, Bulgarian and japanese sub-variant", ssbDE.defaultBgJaJpJp());
		SimpleSmallBundle ssbDEDE = Bundle.load(SimpleSmallBundle.class, getLocale("de", "DE", ""));
		assertEquals("default, Bulgarian and japanese sub-variant", ssbDEDE.defaultBgJaJpJp());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsNoBundle() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				Locale.FRENCH,
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleBg() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("bg", "", ""),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleJa() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("ja", "", ""),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleJaJp() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("ja", "JP", ""),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleJaJpJp() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("ja", "JP", "JP"),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleDe() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("de", "", ""),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}

	@Test(expected=MissingResourceException.class)
	public void testFailToLoadWithoutDefaultsPartialBundleDeDe() throws Exception {
		SimpleSmallBundle ssb = Bundle.load(SimpleSmallBundle.class,
				getLocale("de", "DE", ""),
				Bundle.LoadIgnoreMissing.NO,
				Bundle.LoadIgnoreExtra.NO,
				Bundle.LoadIgnoreParameterMisMatch.NO,
				Bundle.AllowDefaultLanguage.NO
				);
		System.out.println(ssb.defaultBg());
	}
}

