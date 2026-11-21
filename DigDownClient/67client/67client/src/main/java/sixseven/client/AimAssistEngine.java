package sixseven.client;

import sixseven.client.module.AimAssistModule;

public interface AimAssistEngine {
  double[] computeCorrection(
      AimAssistModule aimAssistModule,
      double doubleValue,
      double doubleValue2,
      double doubleValue3);

  void reset();
}
