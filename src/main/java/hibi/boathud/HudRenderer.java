package hibi.boathud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HudRenderer {

	private final Minecraft client;
	private int scaledWidth;
	private int scaledHeight;

	// The index to be used in these scales is the bar type (stored internally as an integer, defined in Config)
	//                                        Pack     Mixed      Blue
	private static final double[] MIN_V =   {   0d,       8d,      40d}; // Minimum display speed (m/s)
	private static final double[] MAX_V =   {  40d,      70d,      70d}; // Maximum display speed (m/s)
	private static final double[] SCALE_V = {4.55d, 182d/62d, 182d/30d}; // Pixels for 1 unit of speed (px*s/m) (BarWidth / (VMax - VMin))

	// Used for lerping
	private double displayedSpeed = 0.0d;

	public HudRenderer(Minecraft client) {
		this.client = client;
	}

	public void render(GuiGraphics graphics, DeltaTracker counter) {
		this.scaledWidth = graphics.guiWidth();
		this.scaledHeight = graphics.guiHeight();
		int i = this.scaledWidth / 2;
		int nameLen = this.client.font.width(Common.hudData.name);

		// Lerping the displayed speed with the actual speed against how far we are into the tick not only is mostly accurate,
		// but gives the impression that it's being updated faster than 20 hz (which it isn't)
		this.displayedSpeed = Mth.lerp(counter.getGameTimeDeltaPartialTick(true), this.displayedSpeed, Common.hudData.speed);

		if(Config.extended) {
			// Overlay texture and bar
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_EXTENDED, i - 91, this.scaledHeight - 83, 182, 33);
			this.renderBar(graphics, i - 91, this.scaledHeight - 83);

			// Sprites
			if(Common.hudData.isDriver) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.client.options.keyLeft.isDown()? LEFT_LIT : LEFT_UNLIT, i - 86, this.scaledHeight - 65, 17, 8);
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.client.options.keyRight.isDown()? RIGHT_LIT : RIGHT_UNLIT, i - 63, this.scaledHeight - 65, 17, 8);
				// Brake-throttle bar
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.client.options.keyUp.isDown()? FORWARD_LIT : FORWARD_UNLIT, i, this.scaledHeight - 55, 61, 5);
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.client.options.keyDown.isDown()? BACKWARD_LIT : BACKWARD_UNLIT, i - 61, this.scaledHeight - 55, 61, 5);
			}

			// Ping
			this.renderPing(graphics, i + 75 - nameLen, this.scaledHeight - 65);
			
			// Text
			// First Row
			this.typeCentered(graphics, String.format(Config.speedFormat, this.displayedSpeed * Config.speedRate), i - 58, this.scaledHeight - 76, 0xFFFFFFFF);
			this.typeCentered(graphics, String.format(Config.angleFormat, Common.hudData.driftAngle), i, this.scaledHeight - 76, 0xFFFFFFFF);
			this.typeCentered(graphics, String.format(Config.gFormat, Common.hudData.g), i + 58, this.scaledHeight - 76, 0xFFFFFFFF);
			// Second Row
			graphics.drawString(this.client.font, Common.hudData.name, i + 88 - nameLen, this.scaledHeight - 65, 0xFFFFFFFF);

		} else { // Compact mode
			// Overlay texture and bar
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_COMPACT, i - 91, this.scaledHeight - 83, 182, 20);
			this.renderBar(graphics, i - 91, this.scaledHeight - 83);
			// Speed and drift angle
			this.typeCentered(graphics, String.format(Config.speedFormat, this.displayedSpeed * Config.speedRate), i - 58, this.scaledHeight - 76, 0xFFFFFFFF);
			this.typeCentered(graphics, String.format(Config.angleFormat, Common.hudData.driftAngle), i + 58, this.scaledHeight - 76, 0xFFFFFFFF);
		}
	}

	/** Renders the speed bar atop the HUD, uses displayedSpeed to, well, diisplay the speed. */
	private void renderBar(GuiGraphics graphics, int x, int y) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_OFF[Config.barType], x, y, 182, 5);
		if(Common.hudData.speed < MIN_V[Config.barType]) return;
		if(Common.hudData.speed > MAX_V[Config.barType]) {
			if(this.client.level.getGameTime() % 2 == 0) return;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_ON[Config.barType], x, y, 182, 5);
			return;
		}
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_ON[Config.barType], 182, 5, 0, 0, x, y, (int)((this.displayedSpeed - MIN_V[Config.barType]) * SCALE_V[Config.barType]), 5);
	}

	/** Implementation is cloned from the notchian ping display in the tab player list.	 */
	private void renderPing(GuiGraphics graphics, int x, int y) {
		ResourceLocation bar = PING_5;
		if(Common.hudData.ping < 0) {
			bar = PING_UNKNOWN;
		}
		else if(Common.hudData.ping < 150) {
			bar = PING_5;
		}
		else if(Common.hudData.ping < 300) {
			bar = PING_4;
		}
		else if(Common.hudData.ping < 600) {
			bar = PING_3;
		}
		else if(Common.hudData.ping < 1000) {
			bar = PING_2;
		}
		else {
			bar = PING_1;
		}
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, bar, x, y, 10, 8);
	}

	/** Renders a piece of text centered horizontally on an X coordinate. */
	private void typeCentered(GuiGraphics graphics, String text, int centerX, int y, int color) {
		graphics.drawString(this.client.font, text, centerX - this.client.font.width(text) / 2, y, color);
	}

	private static final ResourceLocation
		BACKGROUND_EXTENDED = ResourceLocation.fromNamespaceAndPath("boathud", "background_extended"),
		BACKGROUND_COMPACT = ResourceLocation.fromNamespaceAndPath("boathud", "background_compact"),
		LEFT_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "left_unlit"),
		LEFT_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "left_lit"),
		RIGHT_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "right_unlit"),
		RIGHT_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "right_lit"),
		FORWARD_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "forward_unlit"),
		FORWARD_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "forward_lit"),
		BACKWARD_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "backward_unlit"),
		BACKWARD_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "backward_lit"),
		BAR_1_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_1_unlit"),
		BAR_1_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_1_lit"),
		BAR_2_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_2_unlit"),
		BAR_2_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_2_lit"),
		BAR_3_UNLIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_3_unlit"),
		BAR_3_LIT = ResourceLocation.fromNamespaceAndPath("boathud", "bar_3_lit"),
		PING_5 = ResourceLocation.fromNamespaceAndPath("boathud", "ping_5"),
		PING_4 = ResourceLocation.fromNamespaceAndPath("boathud", "ping_4"),
		PING_3 = ResourceLocation.fromNamespaceAndPath("boathud", "ping_3"),
		PING_2 = ResourceLocation.fromNamespaceAndPath("boathud", "ping_2"),
		PING_1 = ResourceLocation.fromNamespaceAndPath("boathud", "ping_1"),
		PING_UNKNOWN = ResourceLocation.fromNamespaceAndPath("boathud", "ping_unknown")
	;
	private static final ResourceLocation[] BAR_OFF = {BAR_1_UNLIT, BAR_2_UNLIT, BAR_3_UNLIT};
	private static final ResourceLocation[] BAR_ON = {BAR_1_LIT, BAR_2_LIT, BAR_3_LIT};
}
