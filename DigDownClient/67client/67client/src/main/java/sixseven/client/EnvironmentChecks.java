package sixseven.client;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class EnvironmentChecks {
  public static volatile boolean enabled = true;
  public static volatile boolean enabled2 = false;
  public static final ScheduledExecutorService scheduledExecutorService =
      Executors.newSingleThreadScheduledExecutor(
          runnable -> {
            Thread thread =
                new Thread(runnable, "guard-" + Long.toHexString(System.nanoTime() & 65535L));
            thread.setDaemon(true);
            thread.setPriority(1);
            return thread;
          });

  public static boolean test() {
    DiagnosticTokenGenerator.ensureInitialized();
    return enabled && enabled2;
  }

  public static void update() {
    DiagnosticTokenGenerator.ensureInitialized();

    try {
      if (test2()) {
        update2();
        return;
      }

      if (test4()) {
        update2();
        return;
      }

      if (test5()) {
        update2();
        return;
      }

      if (test6()) {
        update2();
        return;
      }

      if (test7()) {
        update2();
        return;
      }

      if (test8()) {
        update2();
        return;
      }

      enabled2 = test9();
    } catch (Throwable failure) {
      update2();
    }
  }

  public static void update2() {
    enabled = false;
  }

  public static boolean test2() {
    DiagnosticTokenGenerator.ensureInitialized();

    String[] textArray = new String[] {"/sys/class/dmi/id/product_name", "/proc/scsi/scsi"};

    for (String text : textArray) {
      try {
        String text2 = Files.readString(Path.of(text)).toLowerCase();
        if (text2.contains("virtualbox")
            || text2.contains("vmware")
            || text2.contains("qemu")
            || text2.contains("kvm")
            || text2.contains("xen")
            || text2.contains("hyper-v")
            || text2.contains("bochs")) {
          return true;
        }
      } catch (IOException failure) {
      }
    }

    try {
      String text3 = Files.readString(Path.of("/proc/cpuinfo")).toLowerCase();
      if (text3.contains("hypervisor") || text3.contains("qemu")) {
        return true;
      }
    } catch (IOException failure2) {
    }

    String text4 = System.getProperty("os.name", "").toLowerCase();
    return text4.contains("win") ? test3() : false;
  }

  public static boolean test3() {
    DiagnosticTokenGenerator.ensureInitialized();

    try {
      Process process = Runtime.getRuntime().exec("reg query HKLM\\SOFTWARE\\VMware,\\ Inc.");
      process.waitFor(500L, TimeUnit.MILLISECONDS);
      return process.exitValue() == 0;
    } catch (Exception failure) {
      return false;
    }
  }

  public static boolean test4() {
    DiagnosticTokenGenerator.ensureInitialized();

    String text = System.getenv("WINEPREFIX");
    if (text != null) {
      return true;
    }

    File file = new File(System.getProperty("user.home"), ".wine");
    if (file.exists()) {
      return true;
    }

    try {
      return NativeHardwareBridge.isWine();
    } catch (Throwable failure) {
      return false;
    }
  }

  public static boolean test5() {
    DiagnosticTokenGenerator.ensureInitialized();

    try {
      RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
      long uptime = runtimeMXBean.getUptime();
      if (uptime < 8000L) {
        return true;
      }

      try {
        String text = Files.readString(Path.of("/proc/uptime")).trim().split(" ")[0];
        double doubleValue = Double.parseDouble(text);
        if (doubleValue < 60.0) {
          return true;
        }
      } catch (IOException failure) {
      }

      return false;
    } catch (Exception failure2) {
      return false;
    }
  }

  public static boolean test6() {
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    DiagnosticTokenGenerator.ensureInitialized();

    for (String text : runtimeMXBean.getInputArguments()) {
      String text2 = text.toLowerCase();
      if (text2.contains("-javaagent")
          || text2.contains("-agentlib")
          || text2.contains("-agentpath")
          || text2.contains("jdwp")
          || text2.contains("frida")) {
        return true;
      }
    }

    try {
      Class.forName("java.lang.instrument.ClassFileTransformer");

      Class type = Class.forName("sun.instrument.InstrumentationImpl");
      Field declaredField = type.getDeclaredField("mTransformerManager");
      declaredField.setAccessible(true);
      return true;
    } catch (ClassNotFoundException failure) {
      return false;
    } catch (Exception failure2) {
      return false;
    }
  }

  public static boolean test7() {
    DiagnosticTokenGenerator.ensureInitialized();

    int[] intValues = new int[] {5005, 8000, 8787, 9009, 45002};

    for (int intValue : intValues) {
      try (ServerSocket serverSocket =
          new ServerSocket(intValue, 0, InetAddress.getLoopbackAddress())) {
        serverSocket.setReuseAddress(false);
      } catch (IOException failure) {
        return true;
      }
    }

    return System.getProperty("sun.java.command", "").contains("jdwp");
  }

  public static boolean test8() {
    DiagnosticTokenGenerator.ensureInitialized();

    long longValue = System.nanoTime();
    int intValue = 0;

    for (int index = 0; index < 50000; index++) {
      intValue ^= Integer.bitCount(index) * 7 + index;
    }

    long longValue2 = System.nanoTime() - longValue;
    if (intValue == 51966) {
      System.err.print("");
    }

    return longValue2 > 50000000L;
  }

  public static boolean test9() {
    try {
      Class type = Class.forName("net.minecraft.client.Minecraft");
      Method method = type.getMethod("getInstance");
      Object value = method.invoke(null);
      return value != null;
    } catch (ClassNotFoundException failure) {
      return false;
    } catch (Exception failure2) {
      return isMinecraftWindowVisible();
    }
  }

  public static boolean isMinecraftWindowVisible() {
    DiagnosticTokenGenerator.ensureInitialized();

    try {
      if (GraphicsEnvironment.isHeadless()) {
        return false;
      }

      ProcessHandle.allProcesses()
          .filter(
              processHandle -> {
                return processHandle
                    .info()
                    .command()
                    .map(
                        text -> {
                          return text.toLowerCase().contains("java");
                        })
                    .orElse(false);
              })
          .count();
      return Thread.currentThread()
              .getContextClassLoader()
              .getResource("net/minecraft/client/Minecraft.class")
          != null;
    } catch (Exception failure) {
      return false;
    }
  }

  static {
    scheduledExecutorService.scheduleAtFixedRate(
        EnvironmentChecks::update, 2L, 2L, TimeUnit.SECONDS);
  }
}
