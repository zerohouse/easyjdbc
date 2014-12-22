package setting;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.stream.JsonReader;

public class Setting {

	private static Setting setting = new Setting();

	private String url;
	private String id;
	private String password;

	private Setting() {
		String path = Setting.class.getResource("/").getPath();

		try {
			JsonReader reader = new JsonReader(new FileReader(path
					+ "../database.setting"));
			readDBSettings(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
			System.err.println("Current Path: "
					+ System.getProperty("user.dir"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String get(String type) {
		switch (type) {
		case "url":
			return setting.url;
		case "id":
			return setting.id;
		case "password":
			return setting.password;
		default:
			return null;
		}

	}

	private void readDBSettings(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String dbn = reader.nextName();
			if (dbn.equals("url")) {
				url = reader.nextString();
			} else if (dbn.equals("id")) {
				id = reader.nextString();
			} else if (dbn.equals("password")) {
				password = reader.nextString();
			}
		}
		reader.endObject();
	}

}
