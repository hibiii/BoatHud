package hibi.boathud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
	
	public static boolean enabled = true;
	public static boolean extended = true;
	public static double speedRate = 3.6d;
	public static String speedFormat = "%03.0f km/h";
	public static final  String diffFormat = "%03.0f °";
	public static final String gFormat = "%+.1f g";
	public static int barType = 0;
	public static int configSpeedType = 1;

	private static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "boathud.properties");

	private Config() {}

	public static void load() {
		try {
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				do {
					if(line.startsWith("enabled "))
						enabled = Boolean.parseBoolean(line.substring(8));
					if(line.startsWith("extended "))
						extended = Boolean.parseBoolean(line.substring(9));
					if(line.startsWith("barType "))
						barType = Integer.parseInt(line.substring(8));
					if(line.startsWith("speedUnit "))
						setUnit(Integer.parseInt(line.substring(10)));
					line = br.readLine();
				} while (line != null);
				br.close();
			}
		}
		catch (Exception e) {
		}
	}

	public static void save() {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write("enabled " + Boolean.toString(enabled) + "\n");
			writer.write("extended " + Boolean.toString(extended) + "\n");
			writer.write("barType " + Integer.toString(barType) + "\n");
			writer.write("speedUnit " + Integer.toString(configSpeedType) + "\n");
			writer.close();
		}
		catch (Exception e) {
		}
	}

	public static void setUnit(int type) {
		switch(type) {
		case 1:
			Config.speedRate = 3.6d;
			Config.speedFormat = "%03.0f km/h";
			Config.configSpeedType = 1;
			break;
		case 2:
			Config.speedRate = 2.236936d;
			Config.speedFormat = "%03.0f mph";
			Config.configSpeedType = 2;
			break;
		case 3:
			Config.speedRate = 1.943844d;
			Config.speedFormat = "%03.0f kt";
			Config.configSpeedType = 3;
			break;
		case 0:
			default:
			Config.speedRate = 1d;
			Config.speedFormat = "%03.0f m/s";
			Config.configSpeedType = 0;
			break;
		}
	}
}
