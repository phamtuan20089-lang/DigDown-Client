package sixseven.client;

public final class ProtectionBootstrap {
  public static volatile boolean enabled = false;

  public static void init() {
    DiagnosticTokenGenerator.ensureInitialized();
    if (!enabled) {
      synchronized (ProtectionBootstrap.class) {
        if (!enabled) {
          try {
            System.loadLibrary("67protect");
          } catch (UnsatisfiedLinkError failure) {
          }

          EnvironmentChecks.test();
          Thread thread =
              new Thread(
                  () -> {
                    try {
                      HardwareIdentity.computeFingerprint();
                    } catch (Throwable failure2) {
                    }
                  },
                  "hwid-init");
          thread.setDaemon(true);
          thread.start();
          enabled = true;
        }
      }
    }
  }

  public static boolean test() {
    return EnvironmentChecks.test();
  }
}
