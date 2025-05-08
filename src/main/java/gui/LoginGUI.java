package gui;

import businessLogic.BLFacade;
import domain.User;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesntExistException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class LoginGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private String type = null;


	private JLabel jLabelUsername = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Username"));
	private JLabel jLabelPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Password"));



	private JTextField jTextFieldUsername = new JTextField();
	private JPasswordField jTextFieldPassword = new JPasswordField();


	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	private JPanel jContentPane = null;

	private List<Date> datesWithEventsCurrentMonth;

	public void setLoginDone(boolean loginDone) {
		this.loginDone = loginDone;
	}

	private boolean loginDone;


	private User currentUser = null;

	public LoginGUI() {
		loginDone = false;
		this.setSize(new Dimension(400, 300));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
		this.setLocationRelativeTo(null); 
		this.setResizable(false);
		this.getContentPane().setLayout(null);

		
		jLabelUsername.setBounds(new Rectangle(50, 70, 120, 20));
		jTextFieldUsername.setBounds(new Rectangle(180, 70, 150, 20));
		
		jLabelPassword.setBounds(new Rectangle(50, 110, 120, 20));
		jTextFieldPassword.setBounds(new Rectangle(180, 110, 150, 20));
		
		jButtonLogin.setBounds(new Rectangle(100, 180, 100, 30));
		jButtonClose.setBounds(new Rectangle(220, 180, 100, 30));
		
		jLabelMsg.setBounds(new Rectangle(50, 220, 300, 20));
		jLabelMsg.setForeground(Color.RED);
		jLabelMsg.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		this.getContentPane().add(jLabelUsername);
		this.getContentPane().add(jTextFieldUsername);
		this.getContentPane().add(jLabelPassword);
		this.getContentPane().add(jTextFieldPassword);
		this.getContentPane().add(jButtonLogin);
		this.getContentPane().add(jButtonClose);
		this.getContentPane().add(jLabelMsg);
		
		
		JLabel titleLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(new Rectangle(50, 20, 300, 30));
		this.getContentPane().add(titleLabel);
		
		
		jButtonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonLogin_actionPerformed(e);
			}
		});

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});
	}
	
	private void jButtonLogin_actionPerformed(ActionEvent e) {
		jLabelMsg.setText("");
		String error=field_Errors();
		if (error!=null)
			jLabelMsg.setText(error);
		else
			try {
				BLFacade facade = MainGUI.getBusinessLogic();
				String username = jTextFieldUsername.getText();
				String password = new String(jTextFieldPassword.getPassword());

				boolean userExists =facade.checkUser(username, password);
				if (userExists) {
					loginDone = true;
					currentUser = facade.getUser(username);
					System.out.println("loginDone: " + loginDone);
					jLabelMsg.setForeground(new Color(0, 128, 0)); 
					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginDone"));

					
					jTextFieldUsername.setText("");
					jTextFieldPassword.setText("");
				}
				else{
					jLabelMsg.setForeground(Color.RED);
					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginFailedPassword"));
				}

			} catch (UserDoesntExistException e1) {
				
				jLabelMsg.setText(e1.getMessage());
			}

    }

	public boolean getLoginDone(){
		return loginDone;
	}
	public User getCurrentUser(){
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}


	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	private String field_Errors() {
		
		return null;
	}
}
