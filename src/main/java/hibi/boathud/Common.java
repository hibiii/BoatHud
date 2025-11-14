package hibi.boathud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.AbstractBoat;

public class Common implements ClientModInitializer {

	public static HudData hudData;
	public static Minecraft client = null;
	public static boolean ridingBoat = false;
	public static HudRenderer hudRenderer;

	@Override
	public void onInitializeClient() {
		client = Minecraft.getInstance();
		hudRenderer = new HudRenderer(client);
		Config.load();
		ClientTickEvents.END_WORLD_TICK.register(clientWorld -> {
			if(client.player == null) return;
			if(client.player.getVehicle() instanceof AbstractBoat boat && boat.getFirstPassenger() == client.player) {
				if (hudData == null) {
					hudData = new HudData();
				}
				hudData.update();
			}
			else {
				if (ridingBoat) {
					ridingBoat = false;
				}
			}
		});
	}
}
