
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client  {

    private Socket socket;

    public Client(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        }catch(UnknownHostException e){
            e.printStackTrace();
           e.printStackTrace();
        }
    }
    public Socket getSocket(){
        return socket;
    }
}
