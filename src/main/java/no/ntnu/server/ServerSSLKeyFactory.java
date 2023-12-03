package no.ntnu.server;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The key factory for server SSL key.
 */
public class ServerSSLKeyFactory {

  private static final Logger LOGGER = Logger.getLogger(ServerSSLKeyFactory.class.getName());

  /**
   * Creates a SSL context from the keystore. This code is based on the example from this page.
   *
   * @param keyStorePath the key store path
   * @param keyStorePassword the key store password
   * @return ssl context
   */

  public static SSLContext createSSLContext(String keyStorePath, String keyStorePassword) {
    SSLContext ctx = null;
    try {
      KeyStore keyStore = KeyStore.getInstance("pkcs12");
      try (InputStream kstore = new FileInputStream(keyStorePath)) {
        keyStore.load(kstore, keyStorePassword.toCharArray());
      }
      KeyManagerFactory kmf =
              KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(keyStore, keyStorePassword.toCharArray());
      ctx = SSLContext.getInstance("TLS");
      ctx.init(kmf.getKeyManagers(), null, SecureRandom.getInstanceStrong());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Could not create SSL context", e);
    }
    return ctx;
  }

  /**
   * Checking if server key exists.
   *
   * @param path the path to the file.
   * @return true if the file exists. False if not.
   */
  public static boolean serverKeyCheck (String path) {
    boolean returnValue = false;
    if (path != null && !path.isBlank()) {
      File file = new File(path);
      returnValue = file.exists();
    }
    return returnValue;
  }
}
