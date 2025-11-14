package hibi.boathud;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractBoat;

public class CameraHandler {

    public static void tick(AbstractBoat boat, LocalPlayer player) {
        var boatYaw = boat.getYRot();
        var velocity = boat.getDeltaMovement();
        var velocityAngle = (float) Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 90f;
        if (Float.isNaN(velocityAngle)) velocityAngle = boatYaw;
        
        var lerpProg = (float) Math.min(velocity.multiply(1, 0, 1).length() / Config.cameraAggressiveness, 1);
        var newYRot = Mth.rotLerp(lerpProg, boatYaw, velocityAngle);

        newYRot = Mth.rotLerp(Config.cameraSmoothing, newYRot, player.getYRot());

        player.setYRot(newYRot);
    }
}
