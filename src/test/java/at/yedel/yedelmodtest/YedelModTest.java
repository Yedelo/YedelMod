package at.yedel.yedelmodtest;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class YedelModTest {
	@Test
	public void test() {
		// check that commafy works
		Assertions.assertEquals(
			TextUtils.commafy(123456789),
			"123,456,789"
		);
		Assertions.assertEquals(
			TextUtils.commafy(1),
			"1"
		);

		// check that removeFormatting works
		Assertions.assertEquals(
			TextUtils.removeFormatting("&cString with §asome formatting"),
			"String with some formatting"
		);

		// check that removeSection works
		Assertions.assertEquals(
			TextUtils.removeSection("§cString with &cmore formatting"),
			"String with &cmore formatting"
		);

		// check that replaceAmpersand works
		Assertions.assertEquals(
			TextUtils.replaceAmpersand("&cBad text in red &eDecent text in yellow &a&lReally good text in bold and green"),
			"§cBad text in red §eDecent text in yellow §a§lReally good text in bold and green"
		);
	}
}
