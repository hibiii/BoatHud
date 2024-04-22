package hibi.boathud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.vehicle.BoatEntity;

public class Common implements ClientModInitializer {

	public static HudData hudData;
	public static MinecraftClient client = null;
	public static boolean ridingBoat = false;
	public static HudRenderer hudRenderer;

	@Override
	public void onInitializeClient() {
		client = MinecraftClient.getInstance();
		hudRenderer = new HudRenderer(client);
		Config.load();
		ClientTickEvents.END_WORLD_TICK.register(clientWorld -> {
			if(client.player == null) return;
			if(client.player.getVehicle() instanceof BoatEntity boat && boat.getFirstPassenger() == client.player) {
				hudData.update();
			}
			else {
				ridingBoat = false;
			}
		});
	}
}
