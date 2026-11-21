package sixseven.client;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class PayloadCipher {
  public static final byte[][] encryptedStrings = new byte[0][];

  public static final ConcurrentHashMap decodedStringCache = new ConcurrentHashMap();

  public static String decodeString(int index) {
    try {

      DiagnosticTokenGenerator.ensureInitialized();

      char[] chars = (char[]) decodedStringCache.get(index);
      if (chars != null) {
        return new String(chars);
      }

      byte[] bytes = encryptedStrings[index];
      if (bytes != null && bytes.length >= 32) {
        int value2 = ByteBuffer.wrap(bytes, 0, 4).getInt();
        byte[] bytes2 = deriveStringKey(value2, index);

        try {
          byte[] bytes3 = Arrays.copyOfRange(bytes, 4, 16);

          byte[] bytes4 = Arrays.copyOfRange(bytes, 16, bytes.length);

          Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
          instance.init(2, new SecretKeySpec(bytes2, "AES"), new GCMParameterSpec(128, bytes3));
          byte[] bytes5 = instance.doFinal(bytes4);
          char[] chars2 = new String(bytes5, StandardCharsets.UTF_8).toCharArray();
          Arrays.fill(bytes5, (byte) 0);
          decodedStringCache.put(index, chars2);
          return new String(chars2);
        } catch (Exception failure) {
          throw new RuntimeException(failure);
        } finally {
          Arrays.fill(bytes2, (byte) 0);
        }
      } else {
        throw new IllegalStateException();
      }

    } catch (Throwable originalFailure) {
      throw sixseven.client.runtime.ExceptionPropagation.rethrow(originalFailure);
    }
  }

  public static byte[] deriveStringKey(int intValue, int intValue2) {
    DiagnosticTokenGenerator.ensureInitialized();

    ByteBuffer byteBuffer = ByteBuffer.allocate(40);
    byteBuffer.putLong(-4285594360607555531L);
    byteBuffer.putLong(-7202067040905241491L ^ (long) intValue << 32);
    byteBuffer.putLong(9119677142956588623L ^ intValue2);
    byteBuffer.putLong(6515438233605577259L);
    byteBuffer.putLong(-2401053089206453570L ^ intValue ^ intValue2);
    byte[] bytes = byteBuffer.array();

    for (int index = 0; index < 3; index++) {
      for (int index2 = 0; index2 < bytes.length; index2++) {
        bytes[index2] ^= (byte) (bytes[(index2 + 7) % bytes.length] * 107 + index * 31);
      }
    }

    return Arrays.copyOf(bytes, 32);
  }

  public static byte[] encryptString(String text, long longValue, int intValue) throws Exception {
    try {

      SecureRandom instanceStrong = SecureRandom.getInstanceStrong();
      int intValue2 = instanceStrong.nextInt();
      DiagnosticTokenGenerator.ensureInitialized();

      ByteBuffer byteBuffer = ByteBuffer.allocate(40);
      byteBuffer.putLong(1885667171979197067L ^ longValue);
      byteBuffer.putLong(-7202067040905241491L ^ (long) intValue2 << 32);
      byteBuffer.putLong(9119677142956588623L ^ intValue);
      byteBuffer.putLong(6515438233605577259L);
      byteBuffer.putLong(longValue ^ intValue2 ^ intValue);
      byte[] bytes = byteBuffer.array();

      for (int index = 0; index < 3; index++) {
        for (int index2 = 0; index2 < bytes.length; index2++) {
          bytes[index2] ^= (byte) (bytes[(index2 + 7) % bytes.length] * 107 + index * 31);
        }
      }

      bytes = Arrays.copyOf(bytes, 32);

      byte[] bytes2 = new byte[12];

      instanceStrong.nextBytes(bytes2);

      Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
      instance.init(1, new SecretKeySpec(bytes, "AES"), new GCMParameterSpec(128, bytes2));

      byte[] bytes3 = instance.doFinal(text.getBytes(StandardCharsets.UTF_8));

      ByteBuffer byteBuffer2 = ByteBuffer.allocate(16 + bytes3.length);
      byteBuffer2.putInt(intValue2);
      byteBuffer2.put(bytes2);
      byteBuffer2.put(bytes3);
      Arrays.fill(bytes, (byte) 0);
      return byteBuffer2.array();

    } catch (Throwable originalFailure) {
      throw sixseven.client.runtime.ExceptionPropagation.rethrow(originalFailure);
    }
  }
}
