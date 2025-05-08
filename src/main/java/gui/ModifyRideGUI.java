package gui;

import businessLogic.BLFacade;
import com.toedter.calendar.JCalendar;
import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyRideGUI extends JFrame {
    private final Driver driver;
    private final JComboBox<String> ridesComboBox = new JComboBox<>();
    private final JTable requestsTable = new JTable();
    private List<Ride> rides = new ArrayList<>();
    private DefaultTableModel tableModel;

    private JLabel jLabelMsg = new JLabel();

    private JTextField fieldOrigin=new JTextField();
    private JTextField fieldDestination=new JTextField();

    private JLabel jLabelOrigin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.LeavingFrom"));
    private JLabel jLabelDestination = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.GoingTo"));
    private JLabel jLabelSeats = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.NumberOfSeats"));
    private JLabel jLabRideDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate"));
    private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Price"));




    private JCalendar jCalendar = new JCalendar();

    private JScrollPane scrollPaneEvents = new JScrollPane();


    private final JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private final JButton jButtonAccept = new JButton(ResourceBundle.getBundle("Etiquetas").getString("VisualizeRequestsGUI.AcceptRequest"));

    
    private String selectRideText = "Select a ride:";
    private String userColumnText = "User";
    private String windowTitle = "Driver Ride Requests";
    private String noRidesText = "No rides available";

    private final BLFacade facade = MainGUI.getBusinessLogic();

    public ModifyRideGUI(Driver driver) {
        this.driver = driver;
        loadResources();
        setupUI();
        loadRidesData();
    }

    private void loadResources() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");
            selectRideText = bundle.getString("VisualizeRequestsGUI.SelectRide");
            userColumnText = bundle.getString("VisualizeRequestsGUI.User");
            windowTitle = bundle.getString("VisualizeRequestsGUI");
            noRidesText = bundle.getString("VisualizeRequestsGUI.NoRides");
        } catch (Exception e) {
            System.out.println("Using default texts for missing resources");
        }
    }

    private void setupUI() {
        setTitle(windowTitle);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel(selectRideText));
        topPanel.add(ridesComboBox);

        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(jButtonAccept);
        bottomPanel.add(jButtonClose);

        JPanel midPannel = new JPanel();
        midPannel.setLayout(new BoxLayout(midPannel, BoxLayout.Y_AXIS));
        midPannel.setBorder(new EmptyBorder(20, 30, 20, 30));


        
        jButtonClose.addActionListener(e -> setVisible(false));
        jButtonAccept.addActionListener(this::jButtonAccept_actionPerformed);

        bottomPanel.add(jButtonClose, null);


        midPannel.add(jLabelOrigin, null);
        midPannel.add(fieldOrigin, null);
        midPannel.add(jLabelDestination, null);
        midPannel.add(fieldDestination, null);



        midPannel.add(jCalendar, null);

        midPannel.add(jLabelMsg, null);

        
        add(topPanel, BorderLayout.NORTH);
        add(midPannel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);



    }

    private void jButtonAccept_actionPerformed(ActionEvent e) {
        jLabelMsg.setText("");

        try {
            BLFacade facade = MainGUI.getBusinessLogic();


            Ride currentRide = (Ride) rides.get(ridesComboBox.getSelectedIndex());
            System.out.println(fieldOrigin.getText() + ", " + fieldDestination.getText() + ", " +  UtilDate.trim(jCalendar.getDate()));

            facade.updateRide(currentRide, fieldOrigin.getText(), fieldDestination.getText(), UtilDate.trim(jCalendar.getDate()));
            jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideUpdated"));

        } catch (RideMustBeLaterThanTodayException e1) {
            
            jLabelMsg.setText(e1.getMessage());
        } catch (RideAlreadyExistException e1) {
            
            jLabelMsg.setText(e1.getMessage());
        }

    }

    private void loadRidesData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                rides = facade.getRidesDriver(driver);
                System.out.println("Rides: " + rides);
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (rides == null || rides.isEmpty()) {
                        showMessage(noRidesText, JOptionPane.INFORMATION_MESSAGE);
                        ridesComboBox.setEnabled(false);
                        return;
                    }

                    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
                    for (Ride ride : rides) {
                        if (ride != null) {
                            String from = ride.getFrom() != null ? ride.getFrom() : "Unknown";
                            String to = ride.getTo() != null ? ride.getTo() : "Unknown";
                            String date = ride.getDate() != null ? ride.getDate().toString() : "No date";
                            comboModel.addElement(String.format("%s → %s (%s)", from, to, date));
                        }
                    }

                    if (comboModel.getSize() > 0) {
                        ridesComboBox.setModel(comboModel);
                        ridesComboBox.setSelectedIndex(0);
                    } else {
                        showMessage(noRidesText, JOptionPane.INFORMATION_MESSAGE);
                        ridesComboBox.setEnabled(false);
                    }
                } catch (Exception e) {
                    showMessage("Error loading rides: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }



    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, windowTitle, messageType);
    }
}
