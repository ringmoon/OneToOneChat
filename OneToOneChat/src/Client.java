
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client  {

    private Socket socket;

    public Client(String ip, int port) throws IOException,UnknownHostException {
       socket = new Socket(ip, port);
        
    }
    public Socket getSocket(){
        return socket;
    }
}
