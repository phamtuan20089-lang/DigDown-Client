package sixseven.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ProtectedResourceKeys {

  public static void main(String[] textArray) throws Exception {

    DiagnosticTokenGenerator.ensureInitialized();

    byte[] bytes;
    try (InputStream resourceAsStream =
        ProtectedResourceKeys.class.getResourceAsStream("/inner.bin")) {
      if (resourceAsStream == null) {
        System.exit(1);
      }

      bytes = resourceAsStream.readAllBytes();
    }

    byte[] byteValueArray = HardwareIdentity.computeFingerprint();
    byte[] masterKey = NativeHardwareBridge.getMasterKey();
    byte[] bytes2 = derivePayloadKey(masterKey, byteValueArray);
    Arrays.fill(masterKey, (byte) 0);
    Arrays.fill(byteValueArray, (byte) 0);
    byte[] bytes3 = decryptPayload(bytes, bytes2);
    Arrays.fill(bytes2, (byte) 0);
    Arrays.fill(bytes, (byte) 0);
    CRC32 crC32 = new CRC32();
    crC32.update(bytes3);
    long value2 = crC32.getValue();
    long longValue2 = readExpectedChecksum();
    if (value2 != longValue2) {
      Object value3 = new Object();
      synchronized (value3) {
        value3.wait();
      }
    } else {
      URL url = createMemoryJarUrl(bytes3);
      URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {url}, null);
      String text = readMainClassName(bytes3);
      Class type = urlClassLoader.loadClass(text);
      type.getMethod("main", String[].class).invoke(null, new Object[] {textArray});
      ClientInitializer.ensureInitialized();
    }
  }

  public static byte[] derivePayloadKey(byte[] bytes, byte[] bytes2) throws Exception {
    MessageDigest instance = MessageDigest.getInstance("SHA-256");
    instance.update(bytes);
    instance.update(bytes2);
    long longValue = System.currentTimeMillis() / 86400000L;
    instance.update(ByteBuffer.allocate(8).putLong(longValue).array());
    return instance.digest();
  }

  public static byte[] decryptPayload(byte[] bytes, byte[] bytes2) throws Exception {
    try {

      byte[] bytes3 = Arrays.copyOfRange(bytes, 0, 12);
      byte[] bytes4 = Arrays.copyOfRange(bytes, 12, bytes.length);
      Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
      instance.init(2, new SecretKeySpec(bytes2, "AES"), new GCMParameterSpec(128, bytes3));
      return instance.doFinal(bytes4);

    } catch (Throwable originalFailure) {
      throw sixseven.client.runtime.ExceptionPropagation.rethrow(originalFailure);
    }
  }

  public static long readExpectedChecksum() throws Exception {
    DiagnosticTokenGenerator.ensureInitialized();

    try (InputStream resourceAsStream =
        ProtectedResourceKeys.class.getResourceAsStream("/inner.crc")) {
      if (resourceAsStream != null) {
        byte[] allBytes = resourceAsStream.readAllBytes();
        long value2 = ByteBuffer.wrap(allBytes).getLong();
        return value2 ^ 6804792302017257007L;
      }
    }

    return -1L;
  }

  public static URL createMemoryJarUrl(byte[] bytes) throws Exception {
    return new URL("jar", "", -1, "mem:/inner.jar!/", new MemoryResourceHandler(bytes));
  }

  public static String readMainClassName(byte[] bytes) throws Exception {
    DiagnosticTokenGenerator.ensureInitialized();

    ZipEntry zipEntry;
    try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        if ("META-INF/MANIFEST.MF".equals(zipEntry.getName())) {
          String text = new String(zipInputStream.readAllBytes(), StandardCharsets.UTF_8);

          for (String text2 : text.split("\\r?\\n")) {
            if (text2.startsWith("Main-Class:")) {
              return text2.substring("Main-Class:".length()).trim();
            }
          }
        }
      }
    }

    throw new RuntimeException(DiagnosticTokenGenerator.tokenFor(1201));
  }

  static {
    p78e5d1dbaa0b.pd4bc7b9eb14a.pc1800f0007cc.p9cd9d96d00a6.p989430483434.class.getName();
  }
}
