
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        try {
            while (socket==null) {
                socket = serverSocket.accept();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }
     public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
