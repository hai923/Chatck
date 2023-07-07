package ThuNghiem;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {
    private JTextArea taMessages;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
      
                    Server serverUI = new Server();
                    serverUI.setVisible(true);
                    serverUI.startServer();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Server() {
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        getContentPane().setLayout(new BorderLayout(0, 0));

        taMessages = new JTextArea();
        taMessages.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taMessages);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            appendMessage("Server đã khởi động và đang chờ kết nối...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                appendMessage("Đã kết nối với Client: " + clientSocket);

                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                String username = dis.readUTF();
                String password = dis.readUTF();

                boolean isValidLogin = checkLogin(username, password);

                if (isValidLogin) {
                    dos.writeBoolean(true);
                    appendMessage("Đăng nhập thành công: " + username);
                } else {
                    dos.writeBoolean(false);
                    appendMessage("Đăng nhập thất bại: " + username);
                }

                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLogin(String username, String password) {
        // Đây là nơi bạn thực hiện kiểm tra thông tin đăng nhập
        // Trong ví dụ này, mình sẽ giả sử username và password là "admin"
        return username.equals("admin") && password.equals("admin");
    }

    private void appendMessage(String message) {
        taMessages.append(message + "\n");
    }
}
