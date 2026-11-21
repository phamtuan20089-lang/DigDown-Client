package sixseven.client.ui;

import sixseven.client.AnimationIntensity;
import sixseven.client.AnimationIntensitySupport;
import sixseven.client.ClickRippleEffects;
import sixseven.client.DampedSpring;

public final class ConfigsPageSupport {
  public final AnimationIntensitySupport animationIntensitySupport =
      new AnimationIntensitySupport();
  public final AnimationIntensitySupport[] animationIntensitySupportArray =
      new AnimationIntensitySupport[] {
        new AnimationIntensitySupport(AnimationIntensity.SUBTLE),
        new AnimationIntensitySupport(AnimationIntensity.SUBTLE),
        new AnimationIntensitySupport(AnimationIntensity.SUBTLE),
        new AnimationIntensitySupport(AnimationIntensity.SUBTLE)
      };
  public final ClickRippleEffects clickRippleEffects = new ClickRippleEffects();
  public final DampedSpring dampedSpring = DampedSpring.createQuick();
  public float amount;
  public float amount2;
}
