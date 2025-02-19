package at.yedel.yedelmodtest;



import at.yedel.yedelmod.utils.typeutils.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class TextUtilsTest {
	@Test
	public void testCommafy() {
		Assertions.assertEquals(TextUtils.commafy(123456789), "123,456,789");
		Assertions.assertEquals(TextUtils.commafy(123), "123");
	}

	@Test()
	public void testRemoveFormatting() {
		Assertions.assertEquals(TextUtils.removeFormatting("&aThis is my &ltest!"), "This is my test!");
		Assertions.assertEquals(TextUtils.removeFormatting("Tom & Jerry"), "Tom & Jerry");
		Assertions.assertEquals(TextUtils.removeFormatting("§aThis is my §ltest!"), "This is my test!");
		Assertions.assertEquals(TextUtils.removeFormatting("16 U.S.C. § 580p"), "16 U.S.C. § 580p");
		Assertions.assertEquals(TextUtils.removeFormatting("&aThis is my §ltest!"), "This is my test!");
	}

	@Test
	public void testRemoveSection() {
		Assertions.assertEquals(TextUtils.removeSection("§aThis is my §ltest!"), "This is my test!");
		Assertions.assertEquals(TextUtils.removeSection("16 U.S.C. § 580p"), "16 U.S.C. § 580p");
		Assertions.assertEquals(TextUtils.removeSection("&aThis is my §ltest!"), "&aThis is my test!");
	}

	@Test
	public void testRemoveAmpersand() {
		Assertions.assertEquals(TextUtils.removeAmpersand("&aThis is my &ltest!"), "This is my test!");
		Assertions.assertEquals(TextUtils.removeAmpersand("Tom & Jerry"), "Tom & Jerry");
		Assertions.assertEquals(TextUtils.removeAmpersand("&aThis is my §ltest!"), "This is my §ltest!");
	}

	@Test
	public void testReplaceAmpersand() {
		Assertions.assertEquals(TextUtils.replaceAmpersand("&aThis is my &ltest!"), "§aThis is my §ltest!");
		Assertions.assertEquals(TextUtils.replaceAmpersand("Tom & Jerry"), "Tom & Jerry");
		Assertions.assertEquals(TextUtils.replaceAmpersand("16 U.S.C. § 580p"), "16 U.S.C. § 580p");
		Assertions.assertEquals(TextUtils.replaceAmpersand("&aThis is my §ltest!"), "§aThis is my §ltest!");
	}

	@Test
	public void testRandomUuid() {
		Assertions.assertNotEquals(TextUtils.randomUuid(5), TextUtils.randomUuid(5));
	}
}
