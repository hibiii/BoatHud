package hibi.boathud;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class HudRenderer
extends DrawableHelper {

	private static final Identifier WIDGETS_TEXTURE = new Identifier("boathud","textures/widgets.png");
	private final MinecraftClient client;
	private int scaledWidth;
	private int scaledHeight;

	// The index to be used in these scales is the bar type (stored internally as an integer, defined in Config)
	//                                       Pack  Mix Blue
	private static final double[] MIN_V =   {  0d, 10d, 40d}; // Minimum display speed (m/s)
	private static final double[] MAX_V =   { 40d, 70d, 70d}; // Maximum display speed (m/s)
	private static final double[] SCALE_V = {4.5d,  3d,  6d}; // Pixels for 1 unit of speed (px*s/m) (BarWidth / (VMax - VMin))
	// V coordinates for each bar type in the texture file
	//                                    Pk Mix Blu
	private static final int[] BAR_OFF = { 0, 10, 20};
	private static final int[] BAR_ON =  { 5, 15, 25};

	// Used for lerping
	private double displayedSpeed = 0.0d;

	public HudRenderer(MinecraftClient client) {
		this.client = client;
	}

	public void render(MatrixStack stack, float tickDelta) {
		this.scaledWidth = this.client.getWindow().getScaledWidth();
		this.scaledHeight = this.client.getWindow().getScaledHeight();
		int i = this.scaledWidth / 2;
		int nameLen = this.client.textRenderer.getWidth(Common.hudData.name);

		// Render boilerplate
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		// Lerping the displayed speed with the actual speed against how far we are into the tick not only is mostly accurate,
		// but gives the impression that it's being updated faster than 20 hz (which it isn't)
		this.displayedSpeed = MathHelper.lerp(tickDelta, this.displayedSpeed, Common.hudData.speed);

		if(Config.extended) {
			// Overlay texture and bar
			this.drawTexture(stack, i - 91, this.scaledHeight - 83, 0, 70, 182, 33);
			this.renderBar(stack, i - 91, this.scaledHeight - 83);

			// Sprites
			// Left-right
			this.drawTexture(stack, i - 86, this.scaledHeight - 65, 61, this.client.options.leftKey.isPressed() ? 38 : 30, 17, 8);
			this.drawTexture(stack, i - 63, this.scaledHeight - 65, 79, this.client.options.rightKey.isPressed() ? 38 : 30, 17, 8);
			// Ping
			this.renderPing(stack, i + 75 - nameLen, this.scaledHeight - 65);
			// Brake-throttle bar
			this.drawTexture(stack, i, this.scaledHeight - 55, 0, this.client.options.forwardKey.isPressed() ? 45 : 40, 61, 5);
			this.drawTexture(stack, i - 61, this.scaledHeight - 55, 0, this.client.options.backKey.isPressed() ? 35 : 30, 61, 5);
			
			// Text
			// First Row
			this.typeCentered(stack, String.format(Config.speedFormat, this.displayedSpeed * Config.speedRate), i - 58, this.scaledHeight - 76, 0xFFFFFF);
			this.typeCentered(stack, String.format(Config.angleFormat, Common.hudData.driftAngle), i, this.scaledHeight - 76, 0xFFFFFF);
			this.typeCentered(stack, String.format(Config.gFormat, Common.hudData.g), i + 58, this.scaledHeight - 76, 0xFFFFFF);
			// Second Row
			this.client.textRenderer.drawWithShadow(stack, Common.hudData.name, i + 88 - nameLen, this.scaledHeight - 65, 0xFFFFFF);

		} else { // Compact mode
			// Overlay texture and bar
			this.drawTexture(stack, i - 91, this.scaledHeight - 83, 0, 50, 182, 20);
			this.renderBar(stack, i - 91, this.scaledHeight - 83);
			// Speed and drift angle
			this.typeCentered(stack, String.format(Config.speedFormat, this.displayedSpeed * Config.speedRate), i - 58, this.scaledHeight - 76, 0xFFFFFF);
			this.typeCentered(stack, String.format(Config.angleFormat, Common.hudData.driftAngle), i + 58, this.scaledHeight - 76, 0xFFFFFF);
		}
		RenderSystem.disableBlend();
	}

	/** Renders the speed bar atop the HUD, uses displayedSpeed to, well, diisplay the speed. */
	private void renderBar(MatrixStack stack, int x, int y) {
		this.drawTexture(stack, x, y, 0, BAR_OFF[Config.barType], 182, 5);
		if(Common.hudData.speed < MIN_V[Config.barType]) return;
		if(Common.hudData.speed > MAX_V[Config.barType]) {
			if(this.client.world.getTime() % 2 == 0) return;
			this.drawTexture(stack, x, y, 0, BAR_ON[Config.barType], 182, 5);
			return;
		}
		this.drawTexture(stack, x, y, 0, BAR_ON[Config.barType], (int)((this.displayedSpeed - MIN_V[Config.barType]) * SCALE_V[Config.barType]), 5);
	}

	/** Implementation is cloned from the notchian ping display in the tab player list.	 */
	private void renderPing(MatrixStack stack, int x, int y) {
		int offset = 0;
		if(Common.hudData.ping < 0) {
			offset = 40;
		}
		else if(Common.hudData.ping < 150) {
			offset = 0;
		}
		else if(Common.hudData.ping < 300) {
			offset = 8;
		}
		else if(Common.hudData.ping < 600) {
			offset = 16;
		}
		else if(Common.hudData.ping < 1000) {
			offset = 24;
		}
		else {
			offset = 32;
		}
		this.drawTexture(stack, x, y, 246, offset, 10, 8);
	}

	/** Renders a piece of text centered horizontally on an X coordinate. */
	private void typeCentered(MatrixStack stack, String text, int centerX, int y, int color) {
		this.client.textRenderer.drawWithShadow(stack, text, centerX - this.client.textRenderer.getWidth(text) / 2, y, color);
	}
}
