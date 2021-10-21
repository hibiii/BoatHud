package hibi.boathud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HudRenderer
extends DrawableHelper {

	private static final Identifier WIDGETS_TEXTURE = new Identifier("boathud","textures/widgets.png");
	private final MinecraftClient client;
	private int scaledWidth;
	private int scaledHeight;
	private static final double[] MIN_V =   {  0d, 10d, 40d};
	private static final double[] MAX_V =   { 40d, 70d, 70d};
	private static final double[] SCALE_V = {4.5d,  3d,  6d};
	private static final int[] BAR_OFF = { 0, 10, 20};
	private static final int[] BAR_ON =  { 5, 15, 25};

	public HudRenderer(MinecraftClient client) {
		this.client = client;
	}

	public void render(MatrixStack stack) {
		this.scaledWidth = this.client.getWindow().getScaledWidth();
		this.scaledHeight = this.client.getWindow().getScaledHeight();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		int i = this.scaledWidth / 2;
		// Overlay texture and bar //
		this.drawTexture(stack, i - 91, this.scaledHeight - 82, 0, 67, 182, 30);
		this.renderBar(stack, i - 91, this.scaledHeight - 82);

		this.typeCentered(stack, String.format(Config.speedFormat, Common.hudData.speed * Config.speedRate), i - 58, this.scaledHeight - 76, 0xFFFFFF);
		this.typeCentered(stack, String.format(Config.diffFormat, Common.hudData.angleDiff), i, this.scaledHeight - 76, 0xFFFFFF);
		this.typeCentered(stack, String.format(Config.gFormat, Common.hudData.g), i + 58, this.scaledHeight - 76, 0xFFFFFF);
		this.client.textRenderer.drawWithShadow(stack, Common.hudData.name, i + 89 - this.client.textRenderer.getWidth(Common.hudData.name), this.scaledHeight - 65, 0xFFFFFF);
		RenderSystem.disableBlend();
	}

	private void renderBar(MatrixStack stack, int x, int y) {
		this.drawTexture(stack, x, y, 0, BAR_OFF[Config.barType], 182, 5);
		if(Common.hudData.speed < MIN_V[Config.barType]) return;
		if(Common.hudData.speed > MAX_V[Config.barType]) {
			if(this.client.world.getTime() % 2 == 0) return;
			this.drawTexture(stack, x, y, 0, BAR_ON[Config.barType], 182, 5);
			return;
		}
		this.drawTexture(stack, x, y, 0, BAR_ON[Config.barType], (int)((Common.hudData.speed - MIN_V[Config.barType]) * SCALE_V[Config.barType]), 5);
	}

	private void typeCentered(MatrixStack stack, String text, int centerX, int y, int color) {
		this.client.textRenderer.drawWithShadow(stack, text, centerX - this.client.textRenderer.getWidth(text) / 2, y, color);
	}
}
