package hibi.boathud;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.text.TranslatableText;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

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
					.setSaveConsumer(newVal -> {switch (newVal){
						case MS:
							Config.speedRate = 1d;
							Config.speedFormat = "%03.0f m/s";
							Config.configSpeedType = 0;
							break;
						case KMPH:
							Config.speedRate = 3.6d;
							Config.speedFormat = "%03.0f km/h";
							Config.configSpeedType = 1;
							break;
						case MPH:
							Config.speedRate = 2.236936d;
							Config.speedFormat = "%03.0f mph";
							Config.configSpeedType = 2;
							break;
						case KT:
							Config.speedRate = 1.943844d;
							Config.speedFormat = "%03.0f kt";
							Config.configSpeedType = 3;
							break;
						}})
					.setEnumNameProvider(value -> new TranslatableText("boathud.option.speed_format." + value.toString()))
					.build())
				.addEntry(entryBuilder.startEnumSelector(BAR_TYPE, BarType.class, BarType.values()[Config.barType])
					.setDefaultValue(BarType.PACKED)
					.setTooltip(TIP_BAR, TIP_BAR_PACKED, TIP_BAR_MIXED, TIP_BAR_BLUE)
					.setSaveConsumer(newVal -> Config.barType = newVal.ordinal())
					.setEnumNameProvider(value -> new TranslatableText("boathud.option.bar_type." + value.toString()))
					.build());
			return builder.build();
		};
	}

	public enum BarType {
		PACKED, MIXED, BLUE
	}
	public enum SpeedFormat {
		MS, KMPH, MPH, KT
	}

	private static final TranslatableText
		TITLE = new TranslatableText("boathud.config.title"),
		CAT = new TranslatableText("boathud.config.cat"),
		ENABLED = new TranslatableText("boathud.option.enabled"),
		EXTENDED = new TranslatableText("boathud.option.extended"),
		BAR_TYPE = new TranslatableText("boathud.option.bar_type"),
		SPEED_FORMAT = new TranslatableText("boathud.option.speed_format"),
		TIP_EXTENDED = new TranslatableText("boathud.tooltip.extended"),
		TIP_BAR = new TranslatableText("boathud.tooltip.bar_type"),
		TIP_BAR_PACKED = new TranslatableText("boathud.tooltip.bar_type.packed"),
		TIP_BAR_MIXED = new TranslatableText("boathud.tooltip.bar_type.mixed"),
		TIP_BAR_BLUE = new TranslatableText("boathud.tooltip.bar_type.blue");
}
