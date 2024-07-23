package at.yedel.yedelmodtest;



import java.net.URI;

import at.yedel.yedelmod.update.UpdateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class YedelModTest {
	@Test
	public void testUrlsValid() {
		Assertions.assertDoesNotThrow(() -> {
			URI.create(UpdateManager.getGithubLink());
		});

		Assertions.assertDoesNotThrow(() -> {
			URI.create(UpdateManager.getModrinthLink());
		});
	}
}
