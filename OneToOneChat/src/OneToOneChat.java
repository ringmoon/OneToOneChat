
import java.net.UnknownHostException;
import javax.swing.JFrame;


public class OneToOneChat {

    public static void main(String[] args) throws UnknownHostException {
        MyFrame myFrame =new MyFrame();
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        myFrame.setSize(600,450);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
