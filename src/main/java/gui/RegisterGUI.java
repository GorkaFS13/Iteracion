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



	private JTextField jTextFieldUsername = new JTextField();
	private JTextField jTextFieldPassword = new JTextField();


	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	private JPanel jContentPane = null;

	private List<Date> datesWithEventsCurrentMonth;

	private JComboBox<String> jComboType = null;

	public RegisterGUI() {
		this.getContentPane().setLayout(null);
		jContentPane = new JPanel();
		jComboType = new JComboBox<String>();
		jComboType.addItem("Driver");
		jComboType.addItem("Traveller");

		jContentPane.setLayout(new GridLayout(6, 1, 0, 0));

		this.setSize(new Dimension(604, 370));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));

		jLabelUsername.setBounds(new Rectangle(6, 159, 173, 20));
		jLabelPassword.setBounds(new Rectangle(6, 119, 173, 20));

		jTextFieldUsername.setBounds(new Rectangle(139, 159, 60, 20));
		jTextFieldPassword.setBounds(new Rectangle(139, 159, 60, 20));

		scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));

		jButtonRegister.setBounds(new Rectangle(100, 263, 130, 30));

		jButtonRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonRegister_actionPerformed(e);
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

		jLabelError.setBounds(new Rectangle(7, 191, 320, 20));
		jLabelError.setForeground(Color.red);


		jContentPane.add(jLabelUsername, null);
		jContentPane.add(jTextFieldUsername, null);
		jContentPane.add(jLabelPassword, null);
		jContentPane.add(jTextFieldPassword, null);

		jContentPane.add(jComboType, null);
		jContentPane.add(jButtonClose, null);
		jContentPane.add(jButtonRegister, null);

		jContentPane.add(jLabelMsg, null);
		jContentPane.add(jLabelError, null);
		jContentPane.setBorder(new EmptyBorder(20, 30, 20, 30));

		this.setContentPane(jContentPane);



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
				String password = jTextFieldPassword.getText();
				String type = jComboType.getSelectedItem().toString();

				User r=facade.createUser(username, password, type);
				jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Registered"));




			} catch (UserAlreadyExistException e1) {
				// TODO Auto-generated catch block
				jLabelMsg.setText(e1.getMessage());
			}

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
