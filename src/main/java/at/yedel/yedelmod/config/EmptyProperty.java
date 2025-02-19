package at.yedel.yedelmod.config;



import gg.essential.vigilance.data.PropertyInfo;
import gg.essential.vigilance.gui.settings.SettingComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class EmptyProperty extends PropertyInfo {
	@Override
	public @NotNull SettingComponent createSettingComponent(@Nullable Object object) {
		return new EmptySettingComponent();
	}

	private static class EmptySettingComponent extends SettingComponent {}
}
