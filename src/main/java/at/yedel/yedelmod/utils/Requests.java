package at.yedel.yedelmod.utils;



import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static at.yedel.yedelmod.launch.YedelModConstants.yedelog;



// Credit to nea89 (https://moddev.nea.moe/https/#bringing-your-own-certificates)
public class Requests {
	public static Gson gson = new Gson();
	static SSLContext context;

	static {
		try {
			KeyStore myKeyStore = KeyStore.getInstance("JKS");
			myKeyStore.load(Requests.class.getResourceAsStream("/yedelmod_keystore.jks"), "changeit".toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			kmf.init(myKeyStore, null);
			tmf.init(myKeyStore);
			context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		}
		catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException |
			   IOException | CertificateException e) {
			yedelog.error("Failed to load keystore! Update checking and my messages won't work on older java versions.", e);
			yedelog.info("Current java version: {}", System.getProperty("java.version"));
			context = null;
		}
	}

	public static URLConnection openURLConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		if (connection instanceof HttpsURLConnection && context != null) {
			((HttpsURLConnection) connection).setSSLSocketFactory(context.getSocketFactory());
		}
		return connection;
	}

	public static JsonObject getJsonObject(URL url) throws IOException {
		return gson.fromJson(new InputStreamReader(openURLConnection(url).getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
	}
}
