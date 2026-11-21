package sixseven.client;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptedClassLoader extends ClassLoader {

  public final byte[] classEncryptionKey;
  public final Map decryptedClasses;
  public volatile boolean closed;

  public EncryptedClassLoader(ClassLoader classLoader, byte[] bytes, byte[] bytes2) {
    super(classLoader);

    DiagnosticTokenGenerator.ensureInitialized();

    this.decryptedClasses = new ConcurrentHashMap();
    this.closed = false;
    if (bytes == null || bytes.length != 32) {
      throw new IllegalArgumentException();
    }

    if (bytes2 != null && bytes2.length == 32) {
      this.classEncryptionKey = deriveClassKey(bytes, bytes2);
      Arrays.fill(bytes, (byte) 0);
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static byte[] deriveClassKey(byte[] bytes, byte[] bytes2) {
    long longValue = System.currentTimeMillis() / 86400000L;

    try {
      MessageDigest instance = MessageDigest.getInstance("SHA-256");
      instance.update(bytes);
      instance.update(bytes2);
      ByteBuffer byteBuffer = ByteBuffer.allocate(8).putLong(longValue);
      instance.update(byteBuffer.array());
      byte[] bytes3 = instance.digest();
      instance.reset();
      instance.update(bytes3);
      instance.update(bytes);
      return instance.digest();
    } catch (NoSuchAlgorithmException failure) {
      throw new RuntimeException(failure);
    }
  }

  @Override
  public Class findClass(String text) throws ClassNotFoundException {
    DiagnosticTokenGenerator.ensureInitialized();
    if (this.closed) {
      throw new ClassNotFoundException(DiagnosticTokenGenerator.tokenFor(18450));
    }

    byte[] bytes = (byte[]) this.decryptedClasses.get(text);
    if (bytes != null) {
      return this.defineClass(text, bytes, 0, bytes.length);
    }

    String text2 = "enc/" + text.replace('.', '/') + ".bin";

    try (InputStream resourceAsStream = this.getParent().getResourceAsStream(text2)) {
      if (resourceAsStream == null) {
        throw new ClassNotFoundException(text);
      }

      byte[] allBytes = resourceAsStream.readAllBytes();
      byte[] byteValueArray2 = this.decryptClass(allBytes, text);
      this.decryptedClasses.put(text, (byte[]) byteValueArray2.clone());
      Class type = this.defineClass(text, byteValueArray2, 0, byteValueArray2.length);
      Arrays.fill(byteValueArray2, (byte) 0);
      return type;
    } catch (Exception failure) {
      throw new ClassNotFoundException(text);
    }
  }

  public byte[] decryptClass(byte[] bytes, String text) throws Exception {
    try {

      DiagnosticTokenGenerator.ensureInitialized();

      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      int value2 = byteBuffer.getInt();
      if (value2 != -574511792) {
        throw new SecurityException(DiagnosticTokenGenerator.tokenFor(1202));
      } else {
        long value3 = byteBuffer.getLong();

        byte[] bytes2 = new byte[12];

        byteBuffer.get(bytes2);
        byte[] bytes3 = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes3);
        Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
        instance.init(
            2,
            new SecretKeySpec(this.classEncryptionKey, "AES"),
            new GCMParameterSpec(128, bytes2));
        instance.updateAAD(text.getBytes(StandardCharsets.UTF_8));
        byte[] bytes4 = instance.doFinal(bytes3);
        if (bytes4.length != value3) {
          throw new SecurityException(DiagnosticTokenGenerator.tokenFor(1203));
        } else {
          return bytes4;
        }
      }

    } catch (Throwable originalFailure) {
      throw sixseven.client.runtime.ExceptionPropagation.rethrow(originalFailure);
    }
  }

  public void close() {
    this.closed = true;
    Arrays.fill(this.classEncryptionKey, (byte) 0);
    this.decryptedClasses.clear();
  }

  public static byte[] encryptClass(byte[] bytes, String text, byte[] bytes2, byte[] bytes3)
      throws Exception {
    try {

      byte[] bytes4 = deriveClassKey(bytes2, bytes3);
      SecureRandom instanceStrong = SecureRandom.getInstanceStrong();
      byte[] bytes5 = new byte[12];
      instanceStrong.nextBytes(bytes5);
      Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
      instance.init(1, new SecretKeySpec(bytes4, "AES"), new GCMParameterSpec(128, bytes5));
      instance.updateAAD(text.getBytes(StandardCharsets.UTF_8));
      byte[] bytes6 = instance.doFinal(bytes);
      ByteBuffer byteBuffer = ByteBuffer.allocate(24 + bytes6.length);
      byteBuffer.putInt(-574511792);
      byteBuffer.putLong(bytes.length);
      byteBuffer.put(bytes5);
      byteBuffer.put(bytes6);
      Arrays.fill(bytes4, (byte) 0);
      return byteBuffer.array();

    } catch (Throwable originalFailure) {
      throw sixseven.client.runtime.ExceptionPropagation.rethrow(originalFailure);
    }
  }
}
