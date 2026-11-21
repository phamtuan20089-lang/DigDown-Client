package sixseven.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class HardwareIdentity {
  public static final long timestamp = 2973141409294450720L;

  public static byte[] computeFingerprint() {
    try {
      String text = getCpuId();
      String text2 = getVolumeId();
      String text3 = getFirstMac();
      MessageDigest instance = MessageDigest.getInstance("SHA-256");
      instance.update((text + text2 + text3).getBytes(StandardCharsets.UTF_8));
      return instance.digest();
    } catch (Exception failure) {
      return new byte[32];
    }
  }

  public static String getCpuId() throws Exception {
    DiagnosticTokenGenerator.ensureInitialized();

    String text = System.getProperty("os.name", "").toLowerCase();
    if (text.contains("win")) {
      return NativeHardwareBridge.getCpuId();
    }

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
      return bufferedReader
          .lines()
          .filter(
              text2 -> {
                return text2.startsWith("model name");
              })
          .findFirst()
          .orElse("unknown")
          .split(":")[1]
          .trim();
    }
  }

  public static String getVolumeId() throws Exception {
    DiagnosticTokenGenerator.ensureInitialized();

    String text = System.getProperty("os.name", "").toLowerCase();
    if (text.contains("win")) {
      return NativeHardwareBridge.getVolumeSerial();
    }

    File file = new File("/dev/disk/by-id");
    if (file.exists()) {
      String[] textArray = file.list();
      if (textArray != null && textArray.length > 0) {
        return textArray[0];
      }
    }

    return "unknown";
  }

  public static String getFirstMac() throws Exception {
    return NetworkInterface.networkInterfaces()
        .filter(
            networkInterface -> {
              DiagnosticTokenGenerator.ensureInitialized();

              try {
                return !networkInterface.isLoopback()
                    && networkInterface.getHardwareAddress() != null;
              } catch (Exception failure) {
                return false;
              }
            })
        .findFirst()
        .map(
            networkInterface2 -> {
              DiagnosticTokenGenerator.ensureInitialized();

              try {
                byte[] hardwareAddress = networkInterface2.getHardwareAddress();
                StringBuilder builder = new StringBuilder();

                for (byte byteValue : hardwareAddress) {
                  builder.append(String.format("%02x", byteValue));
                }

                return builder.toString();
              } catch (Exception failure2) {
                return "unknown";
              }
            })
        .orElse("unknown");
  }
}
