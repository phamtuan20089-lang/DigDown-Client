package sixseven.client.module;

import sixseven.client.setting.BooleanSetting;
import sixseven.client.setting.ChoiceSetting;
import sixseven.client.setting.ColorSetting;

public class PlayerESPModule extends Module {
  public final ChoiceSetting styleSetting =
      (ChoiceSetting)
          this.registerSetting(
              new ChoiceSetting(
                  "Style", "Highlight style", "Outline", new String[] {"Box", "Outline", "Glow"}));
  public final ColorSetting colorSetting;
  public final BooleanSetting tracersSetting;
  public static final long timestamp = -6026829235098796388L;

  public PlayerESPModule() {
    super("PlayerESP", "Highlights players through walls", ModuleCategory.RENDER);
    this.colorSetting =
        (ColorSetting)
            this.registerSetting(
                new ColorSetting("Color", "Highlight color; follows the theme until edited", -49508)
                    .getColorSetting());
    this.tracersSetting =
        (BooleanSetting)
            this.registerSetting(
                new BooleanSetting(
                    "Tracers", "Thin lines from the crosshair to nearby players", false));
  }
}
