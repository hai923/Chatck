package phase1;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class LoginClient extends JFrame{
	
	private JFrame frame;
	private JTextField tfUser;
	private int port = 8818;
	private JTextField tfPass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { // main function which will make UI visible
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClient window = new LoginClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginClient() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() { // it will initialize the components of UI
		frame = new JFrame();
		frame.setBounds(100, 100, 619, 342);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Client Register");

		tfUser = new JTextField();
		tfUser.setBounds(207, 50, 276, 61);
		frame.getContentPane().add(tfUser);
		tfUser.setColumns(10);

		JButton clientLoginBtn = new JButton("Connect");
		clientLoginBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String username = tfUser.getText();
		        String password = tfPass.getText();
		        
		        try {
		            // Kết nối đến cơ sở dữ liệu
		            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/java-login-register" , "root", "");
		            
		            // Thực hiện truy vấn để kiểm tra thông tin người dùng
		            String query = "SELECT * FROM `the-app-user` WHERE `u_uname` = ? AND `u_pass` = ?";
		            PreparedStatement stmt = conn.prepareStatement(query);
		            stmt.setString(1, username);
		            stmt.setString(2, password);
		            ResultSet rs = stmt.executeQuery();
		            
		            
		            if (rs.next()) {
		                Socket socket = new Socket("localhost", port);
		                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
		                outStream.writeUTF(username);
		                
		                String msgFromServer = new DataInputStream(socket.getInputStream()).readUTF();
		                if (msgFromServer.equals("Username already taken")) {
		                    JOptionPane.showMessageDialog(frame, "Username already taken\n");
		                } else {
		                    new ClientView(username, socket);
		                    frame.dispose();
		                }
		            } else {
		                JOptionPane.showMessageDialog(frame, "Invalid username or password\n");
		            }
		            
		            rs.close();
		            stmt.close();
		            conn.close();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});

		
		clientLoginBtn.setFont(new Font("Tahoma", Font.PLAIN, 17));
		clientLoginBtn.setBounds(207, 234, 132, 61);
		frame.getContentPane().add(clientLoginBtn);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(44, 55, 132, 47);
		frame.getContentPane().add(lblNewLabel);
		
		tfPass = new JTextField();
		tfPass.setColumns(10);
		tfPass.setBounds(207, 135, 276, 61);
		frame.getContentPane().add(tfPass);
		
		JLabel lblPass = new JLabel("Pass");
		lblPass.setHorizontalAlignment(SwingConstants.CENTER);
		lblPass.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPass.setBounds(44, 135, 132, 47);
		frame.getContentPane().add(lblPass);
	}
}