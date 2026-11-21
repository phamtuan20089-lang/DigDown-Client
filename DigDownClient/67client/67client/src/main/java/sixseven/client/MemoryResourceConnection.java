package sixseven.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MemoryResourceConnection extends URLConnection {
  public final MemoryResourceHandler memoryResourceHandler;

  public MemoryResourceConnection(MemoryResourceHandler memoryResourceHandler2, URL url) {
    super(url);
    this.memoryResourceHandler = memoryResourceHandler2;
  }

  @Override
  public void connect() {}

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(this.memoryResourceHandler.byteValueArray);
  }
}
