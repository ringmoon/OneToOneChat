
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyFrame extends JFrame implements ActionListener {

    private JTextField connectIP;
    private JTextField tfMyName;
    private JScrollBar chatWindowBar;
    private JButton connect;
    private JLabel myIP;
    private JLabel lbMyName;
    private JLabel targetIP;
    private JTextArea chatWindow;
    private JTextField chatInputWindow;
    private JButton send;
    private Server myServer;
    private Client myClient;
    private Socket serverSocket, clientSocket;
    private boolean serverIsConnected, clientIsConnected;
    private String name;
    private BufferedReader reader;
    private PrintStream writer;

    public MyFrame() throws UnknownHostException {
        //UI設計
        connectIP = new JTextField("127.0.0.1", 12);
        tfMyName = new JTextField("帥哥", 8);
        chatInputWindow = new JTextField(25);
        connect = new JButton("開始連線");
        send = new JButton("送出");
        send.setEnabled(false);
        myIP = new JLabel("我的IP : ");
        myIP.setFont(new Font("標楷體", Font.BOLD, 24));
        lbMyName = new JLabel("我的名字 : ");
        targetIP = new JLabel("連線IP : ");
        chatWindow = new JTextArea(15, 50);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.add(myIP);
        panel.add(lbMyName);
        panel.add(tfMyName);
        panel.add(targetIP);
        panel.add(connectIP);
        panel.add(connect);
        panel.setPreferredSize(new Dimension(0, 50));
        add(panel2, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
        chatWindow.setEditable(false);
        chatWindow.setLineWrap(true);
        chatWindow.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(chatWindow);
        // 取得scrollBar
        chatWindowBar = jsp.getVerticalScrollBar();
        JPanel panel3 = new JPanel();
        panel3.add(jsp);
        panel3.add(chatInputWindow);
        panel3.add(send);
        add(panel3, BorderLayout.CENTER);
        //////////////////////////////////
        //取得本機IP並顯示
        InetAddress myComputer = InetAddress.getLocalHost();
        myIP.setText(myIP.getText() + myComputer.getHostAddress());
        //設定連線按鈕功能
        connect.addActionListener(this);
        //設定訊息送出按鈕功能
        send.addActionListener(this);
        //run server
        myServer = new Server(8888);
        myServer.start();
        ////被動等待server被連線完成並做出對應(client連server)
        new Thread(new passiveConnect()).start();
    }

    private class passiveConnect implements Runnable {

        public void run() {
            while (true) {
                if (clientIsConnected && serverIsConnected) {
                    break;
                }
                if (myServer.getSocket() != null && myServer.getSocket().isConnected()) {
                    try {
                        JOptionPane.showMessageDialog(MyFrame.this, "有Client要連接此Server",
                                "請選擇", JOptionPane.INFORMATION_MESSAGE);
                        serverSocket = myServer.getSocket();
                        chatWindow.append("執行狀態：他機Client連本機Server連線成功\n");
                        chatWindow.append("-----------------------------"
                                + "------------------------------------"
                                + "------------------------------------"
                                + "------------------------------------\n");
                        reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), "UTF-8"));
                        serverIsConnected = true;
                        //聊天訊息更新執行緒
                        new Thread(new chatWindowUpdate()).start();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    if (!clientIsConnected) {
                        clientConnectServer(serverSocket.getInetAddress().toString().substring(1));
                    }
                }
                System.out.print("");
            }
        }
    }

    private class chatWindowUpdate implements Runnable {

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    chatWindow.append(message + "\n");
                    // 使jsp每次都自動滾動到最後一行
                    chatWindowBar.setValue(chatWindowBar.getMaximum());
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void clientConnectServer(String ip) {
        try {
            myClient = new Client(ip, 8888);
            clientIsConnected = true;
            clientSocket = myClient.getSocket();
            writer = new PrintStream(clientSocket.getOutputStream());
            name = tfMyName.getText();
            chatWindow.append("執行狀態：本機Client連他機Server連線成功\n");
            chatWindow.append("IP: " + clientSocket.getLocalAddress().toString()
                    + "連到IP: " + clientSocket.getInetAddress() + "\n");
            chatWindow.append("你使用的名字是 :" + name + "\n");
            chatWindow.append("-----------------------------"
                    + "------------------------------------"
                    + "------------------------------------"
                    + "------------------------------------\n");
            tfMyName.setEditable(!clientIsConnected);
            connectIP.setEditable(!clientIsConnected);
            connect.setEnabled(!clientIsConnected);
            send.setEnabled(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("開始連線")) {
            clientConnectServer(connectIP.getText());
        } else if (command.equals("送出")) {
            if(chatInputWindow.getText().equals("")){
                JOptionPane.showMessageDialog(MyFrame.this,"請輸入聊天內容", "錯誤訊息",
                        JOptionPane.ERROR_MESSAGE);
                return ;
            }
            chatWindow.append(name + ": " + chatInputWindow.getText() + "\n");
            writer.println(name + ": " + chatInputWindow.getText());
            writer.flush();
            chatInputWindow.setText("");
        }
    }
}
