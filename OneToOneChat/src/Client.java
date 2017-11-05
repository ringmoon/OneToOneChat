
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client  {

    private Socket socket;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    public Socket getSocket(){
        return socket;
    }
}
