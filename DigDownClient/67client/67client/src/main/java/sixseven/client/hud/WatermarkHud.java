package sixseven.client.hud;

import sixseven.client.ArgbColors;
import sixseven.client.ColorPalette;
import sixseven.client.HudElementContract;
import sixseven.client.ThemeManager;
import sixseven.client.render.FontManager;

public class WatermarkHud extends HudElement {
  public static final float amount = 30.0F;
  public static final float amount2 = 13.0F;
  public static final float amount3 = 18.0F;
  public static final float amount4 = 14.0F;
  public final ThemeManager themeManager;

  public WatermarkHud(ThemeManager themeManager2, HudElementContract hudElementContract) {
    super("watermark", "Watermark", "The 67Client mark", 0.006F, 0.01F, hudElementContract);
    this.themeManager = themeManager2;
  }

  @Override
  public float computeFloat(FontManager fontManager) {
    return 13.0F
        + fontManager.computeFloat("67", 18.0F)
        + 7.0F
        + fontManager.computeFloat("client", 14.0F)
        + 13.0F;
  }

  @Override
  public float computeFloat2(FontManager fontManager) {
    return 30.0F;
  }

  @Override
  public void render(
      FontManager fontManager,
      float floatValue,
      float floatValue2,
      float floatValue3,
      float floatValue4) {
    ColorPalette colorPalette = this.themeManager.getColorPalette();
    float floatValue5 = floatValue2 + floatValue4 / 2.0F;
    float floatValue6 = (float) (0.5 + 0.5 * Math.sin(System.nanoTime() / 8.0E8));
    float floatValue7 = (float) (0.5 + 0.5 * Math.sin(System.nanoTime() / 4.8E8));
    int value = ArgbColors.lerp(colorPalette.computeInt(), colorPalette.getCount(), floatValue7);
    int value2 = ArgbColors.lerp(colorPalette.getCount(), colorPalette.computeInt(), floatValue7);
    fontManager.render2(
        floatValue,
        floatValue2,
        floatValue3,
        floatValue4,
        floatValue4 / 2.0F,
        8.0F,
        ArgbColors.withOpacity(colorPalette.getCount(), 0.1F + 0.1F * floatValue6));
    fontManager.render(
        floatValue,
        floatValue2,
        floatValue3,
        floatValue4,
        floatValue4 / 2.0F,
        ArgbColors.withOpacity(-15264995, 0.88F),
        ArgbColors.withOpacity(-15856878, 0.88F),
        true);
    fontManager.render(
        floatValue,
        floatValue2,
        floatValue3,
        floatValue4,
        floatValue4 / 2.0F,
        1.0F,
        ArgbColors.withOpacity(
            ArgbColors.lerp(colorPalette.getCount(), colorPalette.computeInt(), floatValue6),
            0.55F));
    float floatValue8 = floatValue + 13.0F;
    fontManager.render(
        "67",
        floatValue8,
        floatValue5,
        18.0F,
        ArgbColors.withOpacity(colorPalette.getCount(), 0.45F + 0.3F * floatValue6));
    fontManager.computeFloat2("67", floatValue8, floatValue5, 18.0F, value, value2);
    floatValue8 += fontManager.computeFloat("67", 18.0F) + 7.0F;
    fontManager.render2(
        floatValue8 - 4.5F,
        floatValue5,
        1.4F,
        ArgbColors.withOpacity(colorPalette.computeInt18(), 0.8F));
    fontManager.computeFloat(
        "client", floatValue8, floatValue5, 14.0F, ArgbColors.withOpacity(-856073, 0.92F));
  }
}
