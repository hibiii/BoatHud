package hibi.boathud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.boathud.Common;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(
		method = "render",
		at = @At("TAIL")
	)
	public void render(MatrixStack stack, float tickDelta, CallbackInfo info) {
		if(Common.ridingBoat) {
			stack.push();
			Common.client.textRenderer.drawWithShadow(stack, Double.toString(Common.hudData.speed), 1, 1, 0xFFFFFF);
			Common.client.textRenderer.drawWithShadow(stack, Common.hudData.name, 1, 18, 0xFFFFFF);
			stack.pop();
		}
	}
}
