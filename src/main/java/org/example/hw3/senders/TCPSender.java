package org.example.hw3.senders;

import org.example.hw2.basis.Sender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPSender implements Sender {
    /*
        Sorry, that might be absolutely irrelevant to pass
        Socket as a parameter to TCPSender's constructor like that.
        It is done so that we can write to the output stream of the already
        open socket (without creating a new one as if a sender is another client).
        Unfortunately, I haven't found an alternative solution that meets the
        provided Architecture Diagram in HW2. That seems to be rather inflexible.
    */
    public TCPSender(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void sendMessage(byte[] message, InetAddress address) {
        try {
            var ostream = socket.getOutputStream();
            ostream.write(message);
            ostream.flush();
            assert(socket.getInetAddress().equals(address));
            ostream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final Socket socket;
}
