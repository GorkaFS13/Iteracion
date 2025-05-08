package gui;




import javax.swing.*;
import javax.swing.border.EmptyBorder;


import domain.Driver;
import domain.Traveller;
import domain.User;
import businessLogic.BLFacade;

import java.awt.*;
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
	private JButton jButtonWallet = null;
	private JButton jButtonLogout = null;

	
	private JPanel buttonPanel = null;
	private JPanel driverButtonPanel = null;
	private JPanel travelerButtonPanel = null;

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
	private LoginGUI login;
	private User currentUser = null;;
	private JButton jButtonSelectCar;
	
	public MainGUI() {
		super();
		login = new LoginGUI();

		
		currentUser = login.getCurrentUser();
		jLabelUserType = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeUndefined"));
		jLabelUserType.setFont(new Font("Tahoma", Font.BOLD, 14));

		
		this.setSize(600, 500);
		this.setLocationRelativeTo(null); 

		
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);

		
		JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		langPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LanguagePanel")));

		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton);

		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
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

		
		String currentLocale = Locale.getDefault().getLanguage();
		if (currentLocale.equals("en")) {
			rdbtnNewRadioButton.setSelected(true);
		} else if (currentLocale.equals("eus")) {
			rdbtnNewRadioButton_1.setSelected(true);
		} else if (currentLocale.equals("es")) {
			rdbtnNewRadioButton_2.setSelected(true);
		} else {
			rdbtnNewRadioButton.setSelected(true); 
		}

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(langPanel);

		langPanel.add(rdbtnNewRadioButton_1);
		langPanel.add(rdbtnNewRadioButton_2);
		langPanel.add(rdbtnNewRadioButton);

		
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
				if (currentUser.getType().equals("Driver")) {
					JFrame a = new VisualizeRequestsGUI(new Driver(currentUser));
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
				if (currentUser.getType().equals("Driver")) {
					JFrame a = new ModifyRideGUI(new Driver(currentUser));
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

		jButtonWallet = new JButton();
		jButtonWallet.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Wallet"));
		jButtonWallet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = login.getCurrentUser();
				if (currentUser != null) {
					JFrame a = new WalletGUI(currentUser);
					a.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Please login first", "Error", JOptionPane.ERROR_MESSAGE);
				}
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

		jButtonLogout = new JButton();
		jButtonLogout.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Logout"));
		jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				currentUser = null;
				login.setCurrentUser(null);
				login.setLoginDone(false);
				jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeUndefined"));

				
				Component[] components = buttonPanel.getComponents();
				for (Component comp : components) {
					
					if (comp instanceof JPanel) {
						JPanel panel = (JPanel) comp;
						Component[] innerComponents = panel.getComponents();
						for (Component innerComp : innerComponents) {
							if (innerComp == driverButtonPanel || innerComp == travelerButtonPanel) {
								buttonPanel.remove(panel);
								break;
							}
						}
					}
				}

				
				JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				welcomePanel.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.WelcomeMessage")));
				buttonPanel.add(welcomePanel, 0);

				
				jButtonCreateQuery.setVisible(false);
				jButtonModifyRide.setVisible(false);
				jButtonVisualizeRequests.setVisible(false);
				jButtonQueryQueries.setVisible(false);
				jButtonRequestsStates.setVisible(false);

				
				jButtonWallet.setVisible(false); 
				jButtonLogout.setVisible(false);
				jButtonLogin.setVisible(true);
				jButtonRegister.setVisible(true);

				
				buttonPanel.revalidate();
				buttonPanel.repaint();

				JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogoutMessage"), ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Logout"), JOptionPane.INFORMATION_MESSAGE);
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				System.out.println(login.getLoginDone());
				System.out.println("Current User" + currentUser);

				currentUser = login.getCurrentUser();

				
				Component[] components = buttonPanel.getComponents();
				for (Component comp : components) {
					
					if (comp instanceof JPanel) {
						JPanel panel = (JPanel) comp;
						Component[] innerComponents = panel.getComponents();
						for (Component innerComp : innerComponents) {
							if (innerComp == driverButtonPanel || innerComp == travelerButtonPanel) {
								buttonPanel.remove(panel);
								break;
							}
						}
					}
				}

				
					components = buttonPanel.getComponents();
				for (Component comp : components) {
					if (comp instanceof JPanel) {
						JPanel panel = (JPanel) comp;
						Component[] innerComponents = panel.getComponents();
						for (Component innerComp : innerComponents) {
							if (innerComp instanceof JLabel &&
								!(innerComp.equals(jLabelSelectOption) || innerComp.equals(jLabelUserType))) {
								buttonPanel.remove(panel);
								break;
							}
						}
					}
				}

				if (currentUser != null) {
					
					if(currentUser.getType().equals("Driver")) {
						jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeDriver"));

						
						
						JPanel driverWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
						driverWrapperPanel.add(driverButtonPanel);
						buttonPanel.add(driverWrapperPanel, 0);
						jButtonCreateQuery.setVisible(true);
						jButtonModifyRide.setVisible(true);
						jButtonVisualizeRequests.setVisible(true);
						jButtonSelectCar.setVisible(true);
						jButtonQueryQueries.setVisible(false);
						jButtonRequestsStates.setVisible(false);
					}
					else {
						jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeTraveller"));

						
						
						JPanel travelerWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
						travelerWrapperPanel.add(travelerButtonPanel);
						buttonPanel.add(travelerWrapperPanel, 0);
						jButtonCreateQuery.setVisible(false);
						jButtonModifyRide.setVisible(false);
						jButtonVisualizeRequests.setVisible(false);
						jButtonQueryQueries.setVisible(true);
						jButtonRequestsStates.setVisible(true);
					}

					
					jButtonLogout.setVisible(true);
					jButtonLogin.setVisible(false);
					jButtonRegister.setVisible(false);
				} else {
					
					jLabelUserType.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.UserTypeUndefined"));

					
					jButtonCreateQuery.setVisible(false);
					jButtonModifyRide.setVisible(false);
					jButtonVisualizeRequests.setVisible(false);
					jButtonQueryQueries.setVisible(false);
					jButtonRequestsStates.setVisible(false);
					jButtonSelectCar.setVisible(false);

					
					JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
					welcomePanel.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.WelcomeMessage")));
					buttonPanel.add(welcomePanel, 0);

					
					jButtonLogout.setVisible(false);
					jButtonLogin.setVisible(true);
					jButtonRegister.setVisible(true);
				}

				
				jButtonWallet.setVisible(currentUser != null);

				
				buttonPanel.revalidate();
				buttonPanel.repaint();
			}
		});

		
		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

		
		JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
		headerPanel.setBorder(new EmptyBorder(10, 10, 15, 10));

		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 18));
		titlePanel.add(jLabelSelectOption);

		
		JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jLabelUserType.setFont(new Font("Tahoma", Font.BOLD, 14));
		userTypePanel.add(jLabelUserType);

		
		headerPanel.add(titlePanel, BorderLayout.CENTER);
		headerPanel.add(userTypePanel, BorderLayout.EAST);

		
		JPanel authPanel = new JPanel();
		authPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
		authPanel.setBorder(new EmptyBorder(5, 0, 15, 0));

		
		Dimension authButtonSize = new Dimension(120, 35);
		jButtonLogin.setPreferredSize(authButtonSize);
		jButtonRegister.setPreferredSize(authButtonSize);
		jButtonLogout.setPreferredSize(authButtonSize);

		
		authPanel.add(jButtonLogin);
		authPanel.add(jButtonRegister);

		
		JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logoutPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
		logoutPanel.add(jButtonLogout);

		
		jButtonLogout.setVisible(false);

		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		
		driverButtonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		driverButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		
		jButtonSelectCar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectCar"));
		jButtonSelectCar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser != null && currentUser.getType().equals("Driver")) {
					Driver driver = new Driver(currentUser);
					SelectCarGUI selectCarGUI = new SelectCarGUI(driver);
					selectCarGUI.setVisible(true);
				}
			}
		});

		
		travelerButtonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		travelerButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		
		JPanel walletPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		walletPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

		
		Dimension buttonSize = new Dimension(180, 40);
		jButtonModifyRide.setPreferredSize(buttonSize);
		jButtonCreateQuery.setPreferredSize(buttonSize);
		jButtonVisualizeRequests.setPreferredSize(buttonSize);
		jButtonQueryQueries.setPreferredSize(buttonSize);
		jButtonRequestsStates.setPreferredSize(buttonSize);
		jButtonWallet.setPreferredSize(buttonSize);
		jButtonSelectCar.setPreferredSize(buttonSize);

		
		driverButtonPanel.add(jButtonCreateQuery);
		driverButtonPanel.add(jButtonModifyRide);
		driverButtonPanel.add(jButtonVisualizeRequests);
		driverButtonPanel.add(jButtonSelectCar);

		
		int totalButtons = 4; 
		int gridSize = 4;     
		for (int i = totalButtons; i < gridSize; i++) {
			driverButtonPanel.add(new JPanel()); 
		}

		
		travelerButtonPanel.add(jButtonQueryQueries);
		travelerButtonPanel.add(jButtonRequestsStates);

		
		walletPanel.add(jButtonWallet);

		
		buttonPanel.add(walletPanel);

		

		
		jButtonCreateQuery.setVisible(false);
		jButtonModifyRide.setVisible(false);
		jButtonVisualizeRequests.setVisible(false);
		jButtonQueryQueries.setVisible(false);
		jButtonRequestsStates.setVisible(false);
		jButtonSelectCar.setVisible(false);

		
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));

		
		jContentPane.add(headerPanel);
		jContentPane.add(authPanel);
		jContentPane.add(buttonPanel);
		jContentPane.add(logoutPanel);  
		jContentPane.add(panel);

		
		
		

		
		jButtonWallet.setVisible(false);

		
		jButtonLogin.setVisible(true);
		jButtonRegister.setVisible(true);
		jButtonLogout.setVisible(false);

		
		setContentPane(jContentPane);

		
		if (currentUser == null) {
			setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
		} else {
			String userType = currentUser.getType().equals("Driver") ? "Driver" : "Traveller";
			setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") +
				" - " + userType + ": " + currentUser.getUsername());
		}

		
		setLocationRelativeTo(null);

		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}

	
	private void paintAgain() {
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

		
		jLabelSelectOption.setText(bundle.getString("MainGUI.SelectOption"));
		jLabelUserType.setText(bundle.getString(currentUser == null ?
			"MainGUI.UserTypeUndefined" :
			(currentUser.getType().equals("Driver") ? "MainGUI.UserTypeDriver" : "MainGUI.UserTypeTraveller")));

		
		jButtonQueryQueries.setText(bundle.getString("MainGUI.QueryRides"));
		jButtonCreateQuery.setText(bundle.getString("MainGUI.CreateRide"));
		jButtonVisualizeRequests.setText(bundle.getString("MainGUI.VisualizeQueries"));
		jButtonRequestsStates.setText(bundle.getString("MainGUI.RequestsStates"));
		jButtonModifyRide.setText(bundle.getString("MainGUI.ModifyRide"));
		jButtonLogin.setText(bundle.getString("MainGUI.Login"));
		jButtonRegister.setText(bundle.getString("MainGUI.Register"));
		jButtonLogout.setText(bundle.getString("MainGUI.Logout"));
		jButtonWallet.setText(bundle.getString("MainGUI.Wallet"));
		jButtonSelectCar.setText(bundle.getString("MainGUI.SelectCar"));

		
		jButtonWallet.setVisible(currentUser != null);

		
		jButtonSelectCar.setVisible(currentUser != null && currentUser.getType().equals("Driver"));

		
		if (currentUser == null) {
			setTitle(bundle.getString("MainGUI.MainTitle"));
		} else {
			String userType = currentUser.getType().equals("Driver") ? "Driver" : "Traveller";
			setTitle(bundle.getString("MainGUI.MainTitle") + " - " + userType + ": " + currentUser.getUsername());
		}

		
		Component[] components = panel.getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel) {
				JPanel langPanel = (JPanel) comp;
				if (langPanel.getBorder() instanceof javax.swing.border.TitledBorder) {
					((javax.swing.border.TitledBorder) langPanel.getBorder()).setTitle(bundle.getString("MainGUI.LanguagePanel"));
				}
			}
		}

		
		components = buttonPanel.getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel) {
				JPanel innerPanel = (JPanel) comp;
				Component[] innerComponents = innerPanel.getComponents();
				for (Component innerComp : innerComponents) {
					if (innerComp instanceof JLabel) {
						JLabel label = (JLabel) innerComp;
						if (label.getText() != null && !label.equals(jLabelSelectOption) && !label.equals(jLabelUserType)) {
							label.setText(bundle.getString("MainGUI.WelcomeMessage"));
						}
					}
				}
			}
		}

		
		this.revalidate();
		this.repaint();
	}



} 

