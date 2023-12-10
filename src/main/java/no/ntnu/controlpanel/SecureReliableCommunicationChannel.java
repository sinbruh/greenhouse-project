package no.ntnu.controlpanel;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import no.ntnu.tools.Logger;

import static no.ntnu.controlpanel.RealCommunicationChannel.MAX_RECONNECT_ATTEMPTS;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SecureReliableCommunicationChannel extends RealCommunicationChannel {
    private SSLSocket secureSocket;

    public SecureReliableCommunicationChannel(ControlPanelLogic logic) {
        super(logic);
    }

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