package hibi.boathud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.phys.Vec3;

public class HudData {
	/** The current speed in m/s. */
	public double speed;
	/** The current acceleration in g. */
	public double g;
	/** The current drift angle in degrees. */
	public double driftAngle;
	/** The current ping of the player. -1 means unknown. */
	public int ping = -1;
	/** The name of the player. */
	public String name = "";
	/** Controls whether or not the player's inputs are displayed on the HUD. */
	public boolean isDriver;

    public HudData() {
		Minecraft client = Common.client;

		if (client.player != null) {
			this.name = client.player.getName().getString();
		}
	}

	/** Updates the data. Safe to call every tick. */
	public void update() {
		Minecraft client = Common.client;

		if (client == null || client.player == null) {
			return;
		}
		if (!(client.player.getVehicle() instanceof AbstractBoat boat)) {
			return;
		}

		this.name = client.player.getName().getString();

		Vec3 velocity = boat.getDeltaMovement().multiply(1, 0, 1);

        double oldSpeed = this.speed;
		this.speed = velocity.length() * 20d;

		Vec3 look = boat.getLookAngle().multiply(1, 0, 1);

		double velocityLength = velocity.length();
		double lookLength = look.length();

		if (velocityLength == 0 || lookLength == 0) {
			this.driftAngle = 0;
		} else {
			double cos = velocity.dot(look) / (velocityLength * lookLength);
			cos = Math.clamp(cos, -1.0d, 1.0d);
			this.driftAngle = Math.toDegrees(Math.acos(cos));
		}

		this.g = (this.speed - oldSpeed) * 2.040816327d;

		ClientPacketListener connection = client.getConnection();
		PlayerInfo playerInfo = connection == null
				? null
				: connection.getPlayerInfo(client.player.getUUID());

		this.ping = playerInfo == null ? -1 : playerInfo.getLatency();
		this.isDriver = boat.getControllingPassenger() == client.player;
	}
}
