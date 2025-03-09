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

	@Test
	public void testRandomUuid() {
		Assertions.assertNotEquals(TextUtils.randomUuid(5), TextUtils.randomUuid(5));
	}
}
