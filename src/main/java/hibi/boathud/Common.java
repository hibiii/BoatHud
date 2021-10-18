package hibi.boathud;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class Common implements ClientModInitializer {
	//public static final Logger LOGGER = LogManager.getLogger("modid");

	public static HudData data;
	public static MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(client.world.getTime() % 10 != 0) return;
		});
	}

	public static class HudData {
		public double speed;
		public double g;
		public double angleDiff;
		public boolean left;
		public boolean right;
		public boolean throttle;
		public boolean brake;
		public int ping;
		public final String name;
		
		public HudData(){
			this.name = Common.client.player.getEntityName();
		}
	}
}
