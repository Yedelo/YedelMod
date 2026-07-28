package at.yedel.yedelmod.utils;



import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;



public class Requests {
	public static final Gson GSON = new Gson();

	public static URLConnection openURLConnection(URL url) throws IOException {
		return url.openConnection();
	}

	public static JsonObject getJsonObject(URL url) throws IOException {
		return GSON.fromJson(new InputStreamReader(openURLConnection(url).getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
	}
}
