package hibi.boathud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
	
	/** Format string for speed display on the HUD. Should not be modified directly, use setUnit(). */
	public static String speedFormat = "%03.0f km/h";

	/** Format string for the drift angle display on the HUD. */
	public static final  String angleFormat = "%03.0f °";

	/** Format string for the acceleration display on the HUD. */
	public static final String gFormat = "%+.1f g";

	/** Controls whether or not the HUD should be displayed. */
	public static boolean enabled = true;
	/** Controls whether or not to show all the available details on the HUD. */
	public static boolean extended = true;

	/** Conversion rate between speed unit and m/s. Should not be modified directly, use setUnit(). */
	public static double speedRate = 3.6d;
	/** Speed unit, used for tracking. Should not be modified directly, use setUnit(). */
	public static int configSpeedType = 1;

	// The speed bar type is one of three values:
	// 0: (Pack) Water and Packed Ice speeds (0 ~ 40 m/s)
	// 1: (Mix) Packed and Blue Ice speeds (10 ~ 70 m/s)
	// 2: (Blue) Blue Ice type speeds (40 ~ 70 m/s)
	/** Setting a value that's not between 0 and 2 *will* cause an IndexOutOfBounds */
	public static int barType = 0;

	private static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "boathud.properties");

	private Config() {}

	/**
	 * Load the config from disk and into memory. Ideally should be run only once. Wrong and missing settings are silently reset to defaults.
	 */
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
		// Sanity check
		if(barType > 2 || barType < 0) {
			barType = 0;
		}
	}

	/**
	 * Save the config from memory and onto disk. Ideally, should only be run when the settings are changed.
	 */
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

	/**
	 * Sets the speed unit.
	 * @param type 0 for m/s, 1 for km/h (default), 2 for mph, 3 for knots.
	 */
	public static void setUnit(int type) {
		switch(type) {
		case 0:
			Config.speedRate = 1d;
			Config.speedFormat = "%03.0f m/s";
			Config.configSpeedType = 0;
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
		case 1:
			default:
			Config.speedRate = 3.6d;
			Config.speedFormat = "%03.0f km/h";
			Config.configSpeedType = 1;
			break;
		}
	}
}
