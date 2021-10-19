package hibi.boathud;

import net.minecraft.entity.vehicle.BoatEntity;

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
		this.speed = boat.getVelocity().multiply(1, 0, 1).length() * 20;
	}
}