package hibi.boathud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.boathud.Common;
import hibi.boathud.Config;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(
		method = "render",
		at = @At("TAIL")
	)
	public void render(MatrixStack stack, float tickDelta, CallbackInfo info) {
		if(Config.enabled && Common.ridingBoat && !(Common.client.currentScreen instanceof ChatScreen)) {
			Common.hudRenderer.render(stack);
		}
	}
}
