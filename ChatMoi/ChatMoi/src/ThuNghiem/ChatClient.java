package ThuNghiem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends JFrame {

    private JPanel contentPane;
    private JTextField tfMessage;
    private JTextArea taChat;
    private JButton btnSend;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isChatting = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatClient frame = new ChatClient();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ChatClient() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        taChat = new JTextArea();
        taChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taChat);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        tfMessage = new JTextField();
        panel.add(tfMessage, BorderLayout.CENTER);
        tfMessage.setColumns(10);

        btnSend = new JButton("Gửi");
        panel.add(btnSend, BorderLayout.EAST);

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    try {
                        dos.writeUTF(message);
                        dos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                tfMessage.setText("");
            }
        });
    }

    public void connectToServer(String username) {
        try {
            socket = new Socket("localhost", 1234);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            isChatting = true;
            dos.writeUTF(username);
            dos.flush();

            receiveMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessages() {
        new Thread(new Runnable() {
            public void run() {
                while (isChatting) {
                    try {
                        String message = dis.readUTF();
                        taChat.append(message + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                        isChatting = false;
                    }
                }
            }
        }).start();
    }
}

