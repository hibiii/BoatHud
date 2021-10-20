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

	public HudRenderer(MinecraftClient client) {
		this.client = client;
	}

	public void render(MatrixStack stack) {
		this.scaledWidth = this.client.getWindow().getScaledWidth();
		this.scaledHeight = this.client.getWindow().getScaledHeight();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		// RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		// int oldZOffset = this.getZOffset();
		// this.setZOffset(oldZOffset +1);

		int i = this.scaledWidth / 2;
		// Overlay texture and bar //
		this.drawTexture(stack, i - 91, this.scaledHeight - 82, 0, 67, 182, 30);
		this.renderBar(stack, i - 91, this.scaledHeight - 82);

		this.client.textRenderer.drawWithShadow(stack, Double.toString(Common.hudData.angleDiff), i - 90, this.scaledHeight - 76, 0xFFFFFF);
		RenderSystem.disableBlend();
		// this.setZOffset(oldZOffset);
	}

	private void renderBar(MatrixStack stack, int x, int y) {
		this.drawTexture(stack, x, y, 0, 0, 182, 5);
		int barType = 0;
		if(Common.hudData.speed < MIN_V[barType]) return;
		if(Common.hudData.speed > MAX_V[barType]) {
			if(this.client.world.getTime() % 2 == 0) return;
			this.drawTexture(stack, x, y, 0, 5, 182, 5);
			return;
		}
		this.drawTexture(stack, x, y, 0, 5, (int)((Common.hudData.speed - MIN_V[barType]) * SCALE_V[barType]), 5);
	}
}
