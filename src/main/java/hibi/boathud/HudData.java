package hibi.boathud;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HudData {
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

	public void update() {
		BoatEntity boat = (BoatEntity)Common.client.player.getVehicle();
		Vec3d velocity = boat.getVelocity().multiply(1, 0, 1);
		this.speed = velocity.length() * 20;
		this.angleDiff = Math.toDegrees(Math.acos(velocity.dotProduct(boat.getRotationVector()) / velocity.length() * boat.getRotationVector().length()));
		if(Double.isNaN(this.angleDiff)) this.angleDiff = 0;
	}
}