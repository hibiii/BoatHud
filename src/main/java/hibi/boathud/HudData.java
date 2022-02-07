package hibi.boathud;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class HudData {
	/** The current speed in m/s. */
	public double speed;
	/** The current acceleration in g. */
	public double g;
	/** The current drift angle in degrees, the angle difference between the velocity and where the boat is facing. */
	public double driftAngle;

	/** The curerent ping of the player, just for bookkeeping. */
	public int ping;
	/** The name of the player. This is incompatible with mods that change which account you're logged in as. */
	public final String name;

	private double oldSpeed;
	private final PlayerListEntry listEntry;

	public HudData(){
		this.name = Common.client.player.getEntityName();
		this.listEntry = Common.client.getNetworkHandler().getPlayerListEntry(Common.client.player.getUuid());
	}

	/** Updates the data. Assumes player is in a boat. Do not call unless you are absolutely sure the player is in a boat. */
	public void update() {
		BoatEntity boat = (BoatEntity)Common.client.player.getVehicle();
		// Ignore vertical speed
		Vec3d velocity = boat.getVelocity().multiply(1, 0, 1);
		this.oldSpeed = this.speed;
		this.speed = velocity.length() * 20d; // Speed in Minecraft's engine is in meters/tick.

		// a̅•b̅ = |a̅||b̅|cos ϑ
		// ϑ = acos [(a̅•b̅) / (|a̅||b̅|)]
		this.driftAngle = Math.toDegrees(Math.acos(velocity.dotProduct(boat.getRotationVector()) / velocity.length() * boat.getRotationVector().length()));
		if(Double.isNaN(this.driftAngle)) this.driftAngle = 0; // Div by 0

		// Trivial miscellanea
		this.g = (this.speed - this.oldSpeed) * 2.040816327d; // 20 tps / 9.8 m/s²
		this.ping = this.listEntry.getLatency();
	}
}