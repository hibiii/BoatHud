package hibi.boathud.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.text.Text;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	private @Final MinecraftClient client;

	@Inject(
		method = "onEntityPassengersSet(Lnet/minecraft/network/packet/s2c/play/EntityPassengersSetS2CPacket;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;setOverlayMessage(Lnet/minecraft/text/Text;Z)V",
			shift = At.Shift.AFTER
		)
	)
	private void checkBoatEntry(EntityPassengersSetS2CPacket packet, CallbackInfo info) {
		this.client.inGameHud.addChatMessage(MessageType.SYSTEM, Text.of(this.client.player.getEntityName() + " entered " + this.client.world.getEntityById(packet.getId()).getEntityName()), this.client.player.getUuid());
	}
}
