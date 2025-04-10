package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;
import javax.swing.border.EmptyBorder;

//import com.sun.net.httpserver.Request;
import domain.Driver;
import domain.Traveller;
import domain.User;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainGUI extends JFrame {
	
    private Driver driver;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonRegister = null;
	private JButton jButtonLogin = null;
	private JButton jButtonQueryQueries = null;
	private JButton jButtonRequestsStates = null;
	private JButton jButtonModifyRide = null;


	private JButton jButtonVisualizeRequests = null;

	private  JButton jButtonRequestRide = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	private JLabel jLabelUserType;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	LoginGUI login;
	private User currentUser = null;
	/**
	 * This is the default constructor
	 */
	public MainGUI() {
		super();
		login = new LoginGUI();


		//driver=d;
		
		// this.setSize(271, 295);
		currentUser = login.getCurrentUser();
		jLabelUserType = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeUndefined"));;

		this.setSize(495, 290);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);

		//a.setVisible(true);
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();
				System.out.println("Current user: " + currentUser);
				JFrame a = new CreateRideGUI(new Driver(currentUser));
				a.setVisible(true);
			}
		});

		jButtonVisualizeRequests = new JButton();
		jButtonVisualizeRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VisualizeQueries"));
		jButtonVisualizeRequests.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();
				System.out.println("Current user: " + currentUser);
				if (currentUser instanceof Driver) {
					JFrame a = new VisualizeRequestsGUI((Driver) currentUser);
					a.setVisible(true);
				} else {
					System.out.println("El usuario actual no es un conductor.");
				}
			}
		});

		jButtonModifyRide = new JButton();
		jButtonModifyRide.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ModifyRide"));
		jButtonModifyRide.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();
				System.out.println("Current user: " + currentUser);
				if (currentUser instanceof Driver) {
					JFrame a = new ModifyRideGUI((Driver) currentUser);
					a.setVisible(true);
				} else {
					System.out.println("El usuario actual no es un conductor.");
				}
			}
		});


		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();

				JFrame a = new FindRidesGUI(currentUser);

				a.setVisible(true);
			}
		});

		jButtonRequestsStates = new JButton();
		jButtonRequestsStates.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.RequestsStates"));
		jButtonRequestsStates.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();
				System.out.println("Current user: " + currentUser);
					JFrame a = new RequestsStateGUI( currentUser);
					a.setVisible(true);

			}
		});



		jButtonLogin = new JButton();
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				login.setVisible(true);
			}
		});

		jButtonRegister = new JButton();
		jButtonRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new RegisterGUI();
				a.setVisible(true);
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				System.out.println(login.getLoginDone());
				System.out.println("Current User" + currentUser);


				currentUser = login.getCurrentUser();
				if (currentUser != null){
					if(currentUser.getType().equals("Driver")) {
						jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeDriver"));
						showRides(true);
						showQuery(false);
					}
					else {
						jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeTraveller"));
						showRides(false);
						showQuery(true);
					}
				}
			}
		});

		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.setBorder(new EmptyBorder(20, 30, 20, 30));


		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(new EmptyBorder(20, 30, 20, 30));
		jButtonLogin.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonRegister.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonModifyRide.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jLabelSelectOption.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonCreateQuery.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonVisualizeRequests.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonQueryQueries.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jButtonRequestsStates.setAlignmentX(panel2.CENTER_ALIGNMENT);
		jLabelUserType.setAlignmentX(panel2.CENTER_ALIGNMENT);

		panel2.add(jLabelSelectOption);

		panel2.add(jButtonLogin);
		panel2.add(jButtonRegister);

		panel2.add(jLabelUserType);

		panel2.add(jButtonModifyRide);

		panel2.add(jButtonCreateQuery);
		panel2.add(jButtonVisualizeRequests);
		panel2.add(jButtonQueryQueries);
		panel2.add(jButtonRequestsStates);

		jContentPane.add(panel2);

		jContentPane.add(panel);

		showRides(false);
		showQuery(false);

		setContentPane(jContentPane);
		if (currentUser == null)
			setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
		else
			setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - driver :"+driver.getUsername());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}

	private void showRides(boolean visibility) {
		jButtonCreateQuery.setVisible(visibility);
		jButtonModifyRide.setVisible(visibility);
		jButtonVisualizeRequests.setVisible(visibility);


	}
	private void showQuery(boolean visibility) {
		jButtonQueryQueries.setVisible(visibility);
		jButtonRequestsStates.setVisible(visibility);
	}
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonVisualizeRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.VisualizeQueries"));
		jButtonRequestsStates.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.RequestsStates"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ " - driver :"+driver.getUsername());
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

