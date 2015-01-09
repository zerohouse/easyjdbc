package easyjdbc.setting;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Setting {

	public static final String PASSWORD = "password";

	public static final String ID = "id";

	public static final String URL = "url";

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
		case URL:
			return setting.url;
		case ID:
			return setting.id;
		case PASSWORD:
			return setting.password;
		default:
			return null;
		}

	}

	private void readDBSettings(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String dbn = reader.nextName();
			if (dbn.equals(URL)) {
				url = reader.nextString();
			} else if (dbn.equals(ID)) {
				id = reader.nextString();
			} else if (dbn.equals(PASSWORD)) {
				password = reader.nextString();
			}
		}
		reader.endObject();
	}

}
