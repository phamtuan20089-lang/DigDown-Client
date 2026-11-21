package p78e5d1dbaa0b.pd4bc7b9eb14a.pc1800f0007cc.p9cd9d96d00a6;

public final class p989430483434 {
  public static native String getCpuId();

  public static native String getVolumeSerial();

  public static native byte[] getMasterKey();

  public static native boolean isWine();

  static {
    System.loadLibrary("drdonut_native");
  }
}
