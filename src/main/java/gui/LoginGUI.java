package gui;

import businessLogic.BLFacade;
import domain.User;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesntExistException;

import javax.swing.*;
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
	private JTextField jTextFieldPassword = new JTextField();


	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	private JPanel jContentPane = null;

	private List<Date> datesWithEventsCurrentMonth;
	private boolean loginDone;
	private User currentUser = null;

	public LoginGUI() {
		System.out.println("añsldkjfañlskdfjñlkajsdfñlkj");
		loginDone=false;
		User currentUser;
		this.getContentPane().setLayout(null);
		jContentPane = new JPanel();

		jContentPane.setLayout(new GridLayout(6, 1, 0, 0));

		this.setSize(new Dimension(604, 370));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));

		jLabelUsername.setBounds(new Rectangle(6, 159, 173, 20));
		jLabelPassword.setBounds(new Rectangle(6, 119, 173, 20));

		jTextFieldUsername.setBounds(new Rectangle(139, 159, 60, 20));
		jTextFieldPassword.setBounds(new Rectangle(139, 159, 60, 20));

		scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));

		jButtonLogin.setBounds(new Rectangle(100, 263, 130, 30));

		jButtonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonLogin_actionPerformed(e);
			}
		});
		jButtonClose.setBounds(new Rectangle(275, 263, 130, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});

		jLabelMsg.setBounds(new Rectangle(275, 214, 305, 20));
		jLabelMsg.setForeground(Color.red);

		jLabelError.setBounds(new Rectangle(6, 191, 320, 20));
		jLabelError.setForeground(Color.red);


		jContentPane.add(jLabelUsername, null);
		jContentPane.add(jTextFieldUsername, null);
		jContentPane.add(jLabelPassword, null);
		jContentPane.add(jTextFieldPassword, null);

		jContentPane.add(jButtonClose, null);
		jContentPane.add(jButtonLogin, null);

		jContentPane.add(jLabelMsg, null);
		jContentPane.add(jLabelError, null);
		this.setContentPane(jContentPane);



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
				String password = jTextFieldPassword.getText();

				boolean userExists =facade.checkUser(username, password);
				if (userExists) {
					loginDone = true;
					currentUser = facade.getUser(username);
					System.out.println("loginDone: " + loginDone);
					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginDone"));
				}
				else{
					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.LoginFailedPassword"));
				}

			} catch (UserDoesntExistException e1) {
				// TODO Auto-generated catch block
				jLabelMsg.setText(e1.getMessage());
			}

    }

	public boolean getLoginDone(){
		return loginDone;
	}
	public User getCurrentUser(){
		return currentUser;
	}

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	private String field_Errors() {
		/*
		try {
			if ((fieldOrigin.getText().length()==0) || (fieldDestination.getText().length()==0) || (jTextFieldSeats.getText().length()==0) || (jTextFieldPrice.getText().length()==0))
				return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
			else {

				// trigger an exception if the introduced string is not a number
				int inputSeats = Integer.parseInt(jTextFieldSeats.getText());

				if (inputSeats <= 0) {
					return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.SeatsMustBeGreaterThan0");
				}
				else {
					float price = Float.parseFloat(jTextFieldPrice.getText());
					if (price <= 0) 
						return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.PriceMustBeGreaterThan0");
					
					else 
						return null;
						
				}
			}
		} catch (NumberFormatException e1) {

			return  ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorNumber");		
		} catch (Exception e1) {

			e1.printStackTrace();
			return null;

		}
		*/
		return null;
	}
}
