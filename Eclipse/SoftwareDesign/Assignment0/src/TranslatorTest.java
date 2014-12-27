import static org.junit.Assert.*;
import org.junit.Test;

public class TranslatorTest {

	private final Translator translator = new Translator();

	@Test
	public void test0() {
		assertEquals(translator.translate("cat"), "atcay");
	}

	@Test
	public void test1() {
		assertEquals(translator.translate("dog"), "ogday");
	}

	@Test
	public void test2() {
		assertEquals(translator.translate("simply"), "implysay");
	}

	@Test
	public void test3() {
		assertEquals(translator.translate("noise"), "oisenay");
	}

	@Test
	public void test4() {
		assertEquals(translator.translate("scratch"), "atchscray");
	}

	@Test
	public void test5() {
		assertEquals(translator.translate("thick"), "ickthay");
	}

	@Test
	public void test6() {
		assertEquals(translator.translate("flight"), "ightflay");
	}

	@Test
	public void test7() {
		assertEquals(translator.translate("grime"), "imegray");
	}

	@Test
	public void test8() {
		assertEquals(translator.translate("is"), "isyay");
	}

	@Test
	public void test9() {
		assertEquals(translator.translate("apple"), "appleyay");
	}

	@Test
	public void test10() {
		assertEquals(translator.translate("under"), "underyay");
	}

	@Test
	public void test11() {
		assertEquals(translator.translate("octopus"), "octopusyay");
	}

	@Test
	public void test12() {
		assertEquals(translator.translate("y'all"), "'allyay");
	}

	@Test
	public void test13() {
		assertEquals(translator.translate("don't"), "on'tday");
	}

	@Test
	public void test14() {
		assertEquals(translator.translate("897"), "897");
	}

	@Test
	public void test15() {
		assertEquals(translator.translate("kitten"), "ittenkay");
	}

	@Test
	public void test16() {
		assertEquals(translator.translate("aardvark"), "aardvarkyay");
	}

	@Test
	public void test17() {
		assertEquals(translator.translate("yolk"), "olkyay");
	}

	@Test
	public void test18() {
		assertEquals(translator.translate("kenny"), "ennykay");
	}

	@Test
	public void test19() {
		assertEquals(translator.translate("turner"), "urnertay");
	}

	@Test
	public void test20() {
		assertEquals(translator.translate("nydus"), "ydusnay");
	}

	@Test
	public void test21() {
		assertEquals(translator.translate("scotch-tape"), "otchscay-apetay");
	}

	@Test
	public void test22() {
		assertEquals(translator.translate("HeLlo FrIeNdS"), "eLloHay IeNdSFray");
	}

	@Test
	public void test23() {
		assertEquals(translator.translate("   Space Test Basic"),
				"   aceSpay estTay asicBay");
	}

	@Test
	public void test24() {
		assertEquals(translator.translate("y'all"), "'allyay");
	}

	@Test
	public void test25() {
		assertEquals(translator.translate("doesn't"), "oesn'tday");
	}

	@Test 
	public void test26() {
		assertEquals(translator.translate("hers'"), "ers'hay");
	}

	@Test
	public void test27() {
		assertEquals(translator.translate("Hello:?!My.Name-is"),
				"elloHay:?!yMay.ameNay-isyay");
	}

	@Test
	public void test28() {
		assertEquals(translator.translate("aa aaa yay aaay"),
				"aayay aaayay ayyay aaayyay");
	}

	@Test
	public void test29() {
		assertEquals(translator.translate("asd8fasd"), "asd8fasd");
	}

	@Test
	public void test30() {
		assertEquals(translator.translate("asdf0"), "asdf0");
	}

	@Test
	public void test31() {
		assertEquals(translator.translate("---aaa---aaa---"),
				"---aaayay---aaayay---");
	}

	@Test
	public void test32() {
		assertEquals(translator.translate("\\/hello"), "\\/hello");
	}

	@Test
	public void test33() {
		assertEquals(translator.translate("hello?goodbye?"),
				"ellohay?oodbyegay?");
	}

	@Test
	public void test34() {
		assertEquals(translator.translate(": 897"), ": 897");
	}

	@Test
	public void test35() {
		assertEquals(translator.translate("?   f'f   ?"), "?   'ffay   ?");
	}

	@Test
	public void test36() {
		assertEquals(translator.translate("a-b-c ef-gh-ij"),
				"ayay-bay-cay efyay-ghay-ijyay");
	}

	@Test
	public void test37() {
		assertEquals(translator.translate("Don't tell mommy."),
				"on'tDay elltay ommymay.");
	}

	@Test
	public void test38() {
		assertEquals(translator.translate("Budweiser:    the King of Beers"),
				"udweiserBay:    ethay ingKay ofyay eersBay");
	}
}