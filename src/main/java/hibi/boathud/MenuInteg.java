package hibi.boathud;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MenuInteg implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(TITLE);
			ConfigEntryBuilder entryBuilder = builder.entryBuilder();
			builder.getOrCreateCategory(CAT)

				.addEntry(entryBuilder.startBooleanToggle(ENABLED, Config.enabled)
					.setDefaultValue(true)
					.setSaveConsumer(newVal -> Config.enabled = newVal)
					.build())

				.addEntry(entryBuilder.startBooleanToggle(EXTENDED, Config.extended)
					.setDefaultValue(true)
					.setTooltip(TIP_EXTENDED)
					.setSaveConsumer(newVal -> Config.extended = newVal)
					.build())

				.addEntry(entryBuilder.startEnumSelector(SPEED_FORMAT, SpeedFormat.class, SpeedFormat.values()[Config.configSpeedType])
					.setDefaultValue(SpeedFormat.KMPH)
					.setSaveConsumer(newVal -> Config.setUnit(newVal.ordinal()))
					.setEnumNameProvider(value -> Component.translatable("boathud.option.speed_format." + value.toString()))
					.build())

				.addEntry(entryBuilder.startEnumSelector(BAR_TYPE, BarType.class, BarType.values()[Config.barType])
					.setDefaultValue(BarType.PACKED)
					.setTooltip(TIP_BAR, TIP_BAR_PACKED, TIP_BAR_MIXED, TIP_BAR_BLUE)
					.setSaveConsumer(newVal -> Config.barType = newVal.ordinal())
					.setEnumNameProvider(value -> Component.translatable("boathud.option.bar_type." + value.toString()))
					.build())

				.addEntry(entryBuilder.startBooleanToggle(CAMERA_CONTROL, Config.cameraControl)
					.setDefaultValue(true)
					.setTooltip(TIP_CAMERA_CONTROL)
					.setSaveConsumer(newVal -> Config.cameraControl = newVal)
					.build())
				
				.addEntry(entryBuilder.startFloatField(CAMERA_AGGRESSIVENESS, Config.cameraAggressiveness * 20)
					.setDefaultValue(60)
					.setTooltip(TIP_CAMERA_AGGRESSIVENESS)
					.setMin(4).setMax(70)
					.setSaveConsumer(newVal -> Config.cameraAggressiveness = newVal / 20)
					.build())
				
				.addEntry(entryBuilder.startFloatField(CAMERA_SMOOTHING, Config.cameraSmoothing / 0.009f)
					.setDefaultValue(50)
					.setTooltip(TIP_CAMERA_SMOOTHING)
					.setMin(0).setMax(100)
					.setSaveConsumer(newVal -> Config.cameraSmoothing = newVal * 0.009f)
					.build());

			builder.setSavingRunnable(() -> Config.save());
			return builder.build();
		};
	}

	public enum BarType {
		PACKED, MIXED, BLUE
	}
	public enum SpeedFormat {
		MS, KMPH, MPH, KT
	}

	private static final MutableComponent
		TITLE = Component.translatable("boathud.config.title"),
		CAT = Component.translatable("boathud.config.cat"),
		ENABLED = Component.translatable("boathud.option.enabled"),
		EXTENDED = Component.translatable("boathud.option.extended"),
		CAMERA_CONTROL = Component.translatable("boathud.option.camera_control"),
		CAMERA_AGGRESSIVENESS = Component.translatable("boathud.option.camera_aggressiveness"),
		CAMERA_SMOOTHING = Component.translatable("boathud.option.camera_smoothing"),
		BAR_TYPE = Component.translatable("boathud.option.bar_type"),
		SPEED_FORMAT = Component.translatable("boathud.option.speed_format"),
		TIP_EXTENDED = Component.translatable("boathud.tooltip.extended"),
		TIP_CAMERA_CONTROL = Component.translatable("boathud.tooltip.camera_control"),
		TIP_CAMERA_AGGRESSIVENESS = Component.translatable("boathud.tooltip.camera_aggressiveness"),
		TIP_CAMERA_SMOOTHING = Component.translatable("boathud.tooltip.camera_smoothing"),
		TIP_BAR = Component.translatable("boathud.tooltip.bar_type"),
		TIP_BAR_PACKED = Component.translatable("boathud.tooltip.bar_type.packed"),
		TIP_BAR_MIXED = Component.translatable("boathud.tooltip.bar_type.mixed"),
		TIP_BAR_BLUE = Component.translatable("boathud.tooltip.bar_type.blue");
}
