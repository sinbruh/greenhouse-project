package no.ntnu.controlpanel;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import no.ntnu.tools.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class extends the RealCommunicationChannel to add security and reliability features.
 * It uses SSL for secure communication and includes a retry mechanism for reliability.
 */
public class SecureCommunicationChannel extends RealCommunicationChannel {
    private SSLSocket secureSocket;

    /**
     * Constructor for SecureReliableCommunicationChannel.
     * @param logic The ControlPanelLogic instance to be used for communication.
     */
    public SecureCommunicationChannel(ControlPanelLogic logic) {
        super(logic);
    }

    /**
     * Initializes the streams for secure communication.
     * This method creates a secure socket using the SSLSocketFactory and enables all supported cipher suites.
     * It then sets the socketWriter with a PrintWriter that writes to the secure socket's output stream.
     * @param socket The socket to be used for communication.
     * @throws IOException If an I/O error occurs when creating the secure socket or getting its I/O streams.
     */
    @Override
    public void initializeStreams(Socket socket) {
    try {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        secureSocket = (SSLSocket) factory.createSocket(socket.getInetAddress(), socket.getPort());

        // Enable all the suites
        String[] supported = secureSocket.getSupportedCipherSuites();
        secureSocket.setEnabledCipherSuites(supported);

        setSocketWriter(new PrintWriter(secureSocket.getOutputStream(), true));
        // rest of the code
    } catch (IOException e) {
        Logger.info("could not initialize secure stream");
    }
    }

    /**
     * Closes the socket and the secure socket.
     * This method first calls the superclass's closeSocket method to close the regular socket.
     * It then checks if the secure socket is not null and not already closed, and if so, closes it.
     * @throws IOException If an I/O error occurs when closing the secure socket.
     */
    @Override
    public void closeSocket() {
        super.closeSocket();
        if (secureSocket != null && !secureSocket.isClosed()) {
            try {
                secureSocket.close();
            } catch (IOException e) {
                Logger.error("Could not close secure socket" + e.getMessage());
            }
        }
    }

    /**
     * Runs the communication channel with a retry mechanism.
     * This method attempts to run the communication channel up to MAX_RECONNECT_ATTEMPTS times.
     * If an exception occurs during the run, it increments the retry count and tries again.
     * If the run is successful, it breaks the loop and ends the retries.
     */
    @Override
    public void run() {
        int retryCount = 0;
        while (retryCount < MAX_RECONNECT_ATTEMPTS) {
            try {
                super.run();
                break; // If successful, break the loop
            } catch (Exception e) {
                retryCount++;
                Logger.error("Error occurred, retrying... Attempt: " + retryCount);
            }
        }
    }
}