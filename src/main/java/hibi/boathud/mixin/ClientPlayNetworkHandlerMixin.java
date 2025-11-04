package hibi.boathud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.boathud.Common;
import hibi.boathud.HudData;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	private ClientWorld world;

	@Inject(
		method = "onEntityPassengersSet(Lnet/minecraft/network/packet/s2c/play/EntityPassengersSetS2CPacket;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;setOverlayMessage(Lnet/minecraft/text/Text;Z)V",
			shift = At.Shift.AFTER
		)
	)
	private void checkBoatEntry(EntityPassengersSetS2CPacket packet, CallbackInfo info) {
		if(!(world.getEntityById(packet.getEntityId()) instanceof AbstractBoatEntity)) return;
		Common.ridingBoat = true;
		Common.hudData = new HudData();
	}
}
