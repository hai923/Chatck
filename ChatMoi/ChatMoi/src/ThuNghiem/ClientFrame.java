package ThuNghiem;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientFrame extends JFrame {

    private JPanel contentPane;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientFrame frame = new ClientFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientFrame() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblUsername.setBounds(50, 30, 70, 20);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblPassword.setBounds(50, 60, 70, 20);
        contentPane.add(lblPassword);

        tfUsername = new JTextField();
        tfUsername.setBounds(130, 30, 200, 20);
        contentPane.add(tfUsername);
        tfUsername.setColumns(10);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(130, 60, 200, 20);
        contentPane.add(tfPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = new String(tfPassword.getPassword());

                try {
                    socket = new Socket("localhost", 1234);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());

                    dos.writeUTF(username);
                    dos.writeUTF(password);

                    boolean isValidLogin = dis.readBoolean();
                    if (isValidLogin) {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                        // Hiển thị frame mới với thông tin người dùng đã đăng nhập thành công
                        LoggedInFrame frame = new LoggedInFrame(username);
                        frame.setVisible(true);
                        dispose(); // Đóng frame đăng nhập
                    } else {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thất bại!");
                    }

                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnLogin.setBounds(130, 100, 100, 25);
        contentPane.add(btnLogin);
    }

}
