package gui;

import businessLogic.BLFacade;
import domain.Driver;
import domain.User;
import exceptions.UserAlreadyExistException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class RegisterGUI extends JFrame {
	private static final long serialVersionUID = 1L;


	private JLabel jLabelUsername = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Username"));
	private JLabel jLabelPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
	private JLabel jLabelType = new JLabel("User Type:");


	private JTextField jTextFieldUsername = new JTextField();
	private JPasswordField jTextFieldPassword = new JPasswordField();


	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	private JPanel jContentPane = null;

	private List<Date> datesWithEventsCurrentMonth;

	private JComboBox<String> jComboType = null;

	public RegisterGUI() {
		this.setSize(new Dimension(400, 350));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		this.setLocationRelativeTo(null); 
		this.setResizable(false);
		this.getContentPane().setLayout(null);

		
		jComboType = new JComboBox<String>();
		jComboType.addItem("Driver");
		jComboType.addItem("Traveller");

		
		jLabelUsername.setBounds(new Rectangle(50, 70, 120, 20));
		jTextFieldUsername.setBounds(new Rectangle(180, 70, 150, 20));
		
		jLabelPassword.setBounds(new Rectangle(50, 110, 120, 20));
		jTextFieldPassword.setBounds(new Rectangle(180, 110, 150, 20));
		
		jLabelType.setBounds(new Rectangle(50, 150, 120, 20));
		jComboType.setBounds(new Rectangle(180, 150, 150, 20));
		
		jButtonRegister.setBounds(new Rectangle(100, 200, 120, 30));
		jButtonClose.setBounds(new Rectangle(230, 200, 100, 30));
		
		jLabelMsg.setBounds(new Rectangle(50, 250, 300, 20));
		jLabelMsg.setForeground(Color.RED);
		jLabelMsg.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		this.getContentPane().add(jLabelUsername);
		this.getContentPane().add(jTextFieldUsername);
		this.getContentPane().add(jLabelPassword);
		this.getContentPane().add(jTextFieldPassword);
		this.getContentPane().add(jLabelType);
		this.getContentPane().add(jComboType);
		this.getContentPane().add(jButtonRegister);
		this.getContentPane().add(jButtonClose);
		this.getContentPane().add(jLabelMsg);
		
		
		JLabel titleLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(new Rectangle(50, 20, 300, 30));
		this.getContentPane().add(titleLabel);
		
		
		jButtonRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonRegister_actionPerformed(e);
			}
		});

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});
	}
	
	private void jButtonRegister_actionPerformed(ActionEvent e) {
		jLabelMsg.setText("");
		String error=field_Errors();
		if (error!=null)
			jLabelMsg.setText(error);
		else
			try {
				BLFacade facade = MainGUI.getBusinessLogic();
				String username = jTextFieldUsername.getText();
				String password = new String(jTextFieldPassword.getPassword());
				String type = jComboType.getSelectedItem().toString();

				
				if (username.trim().isEmpty() || password.trim().isEmpty()) {
					jLabelMsg.setForeground(Color.RED);
					jLabelMsg.setText("Username and password cannot be empty");
					return;
				}

				User r = facade.createUser(username, password, type);
				jLabelMsg.setForeground(new Color(0, 128, 0)); 
				jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Registered"));

				
				jTextFieldUsername.setText("");
				jTextFieldPassword.setText("");




			} catch (UserAlreadyExistException e1) {
				jLabelMsg.setForeground(Color.RED);
				jLabelMsg.setText(e1.getMessage());
			} catch (Exception e1) {
				jLabelMsg.setForeground(Color.RED);
				jLabelMsg.setText("An error occurred during registration");
				e1.printStackTrace();
			}

		}


	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	private String field_Errors() {
		
		return null;
	}
}
