package sixseven.client;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class MemoryResourceHandler extends URLStreamHandler {
  public final byte[] byteValueArray;

  public MemoryResourceHandler(byte[] byteValueArray2) {
    this.byteValueArray = byteValueArray2;
  }

  @Override
  public URLConnection openConnection(URL url) {
    return new MemoryResourceConnection(this, url);
  }
}
