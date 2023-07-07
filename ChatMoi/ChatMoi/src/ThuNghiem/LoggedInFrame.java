package ThuNghiem;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class LoggedInFrame extends JFrame {

    private JPanel contentPane;

    public LoggedInFrame(String username) {
        setTitle("Đăng nhập thành công");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 300, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblLoggedInUser = new JLabel("Username: " + username);
        lblLoggedInUser.setBounds(50, 30, 200, 20);
        contentPane.add(lblLoggedInUser);
    }

}

