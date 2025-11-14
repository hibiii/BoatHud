package hibi.boathud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.boathud.Common;
import hibi.boathud.Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

@Mixin(Gui.class)
public class InGameHudMixin {
	@Inject(
		method = "renderHotbarAndDecorations",
		at = @At("TAIL")
	)
	public void render(GuiGraphics graphics, DeltaTracker counter, CallbackInfo info) {
		if(Config.enabled && Common.ridingBoat && !(Common.client.screen instanceof ChatScreen)) {
			Common.hudRenderer.render(graphics, counter);
		}
	}
}
