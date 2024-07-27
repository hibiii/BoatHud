package hibi.boathud.mixin;

import hibi.boathud.Common;
import hibi.boathud.Config;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	//@Inject(
	//	method = "render",
	//	at = @At(
	//		value = "INVOKE",
	//		target = "net/minecraft/client/MinecraftClient.getProfiler()Lnet/minecraft/util/profiler/Profiler;",
	//		ordinal = 8
	//	)
	//)
	//public void render(DrawContext graphics, float tickDelta, CallbackInfo info) {
	//	if(Config.enabled && Common.ridingBoat && !(Common.client.currentScreen instanceof ChatScreen)) {
	//		Common.hudRenderer.render(graphics, tickDelta);
	//	}
	//}
	
	@Inject(method = "renderChat", at = @At(value = "TAIL"))
	public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if(Config.enabled && Common.ridingBoat && !(Common.client.currentScreen instanceof ChatScreen)) {
			Common.hudRenderer.render(context, tickCounter.getTickDelta(false));
		}
	}
}
