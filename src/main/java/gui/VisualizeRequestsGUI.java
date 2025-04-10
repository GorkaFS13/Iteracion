package gui;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Ride;
import domain.RideRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisualizeRequestsGUI extends JFrame {
    private final Driver driver;
    private final JComboBox<String> ridesComboBox = new JComboBox<>();
    private final JTable requestsTable = new JTable();
    private List<Ride> rides = new ArrayList<>();
    private DefaultTableModel tableModel;

    private final JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private final JButton jButtonAccept = new JButton(ResourceBundle.getBundle("Etiquetas").getString("VisualizeRequestsGUI.AcceptRequest"));

    // Textos internacionalizados
    private String selectRideText = "Select a ride:";
    private String userColumnText = "User";
    private String windowTitle = "Driver Ride Requests";
    private String noRidesText = "No rides available";

    private final BLFacade facade = MainGUI.getBusinessLogic();

    public VisualizeRequestsGUI(Driver driver) {
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

        // 🔹 Panel superior con ComboBox
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel(selectRideText));
        topPanel.add(ridesComboBox);

        // 🔹 Configurar la tabla con un JScrollPane
        tableModel = new DefaultTableModel(new Object[]{userColumnText, "Ride (String)", "Ride (Object)", "Request(Object)"},0);
        requestsTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));

        // 🔹 Panel inferior con botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(jButtonAccept);
        bottomPanel.add(jButtonClose);

        // 🔹 Agregar eventos de los botones
        jButtonClose.addActionListener(e -> setVisible(false));
        jButtonAccept.addActionListener(this::jButtonAccept_actionPerformed);




        // 🔹 Agregar componentes al JFrame
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void jButtonAccept_actionPerformed(ActionEvent e) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a ride", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object rideObject = tableModel.getValueAt(selectedRow, 2);
        if (!(rideObject instanceof Ride)) {
            JOptionPane.showMessageDialog(this, "Error: Invalid ride selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ride selectedRide = (Ride) rideObject;
        boolean placesUpdated = facade.UpdatePlaces(selectedRide);
        Object request = tableModel.getValueAt(selectedRow, 3);
        if (placesUpdated) {

            if (request instanceof RideRequest) {
                boolean updateRequest = facade.updateRequest((RideRequest)request);
                if (updateRequest){
                    JOptionPane.showMessageDialog(this, "Request accepted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.removeRow(selectedRow); // 🔹 Elimina la fila correctamente
                }
            }

        } else {
            boolean removeRequest = facade.removeRequest((RideRequest)request);
            JOptionPane.showMessageDialog(this, "No places left", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRidesData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                rides = facade.getRidesDriver(driver);
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
                        ridesComboBox.addItemListener(VisualizeRequestsGUI.this::handleRideSelection);
                        ridesComboBox.setSelectedIndex(0);
                        updateRequestsTable(rides.get(0));
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

    private void handleRideSelection(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            int index = ridesComboBox.getSelectedIndex();
            if (index >= 0 && index < rides.size()) {
                updateRequestsTable(rides.get(index));
            }
        }
    }

    private void updateRequestsTable(Ride selectedRide) {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<RideRequest> requests = facade.getRequestsRide(selectedRide, driver);
                if (requests != null && !requests.isEmpty()) {
                    for (RideRequest request : requests) {
                        if (request.getState().equals("Waiting")) {
                            tableModel.addRow(new Object[]{
                                    request.getUser(),
                                    selectedRide.toString(),
                                    selectedRide,
                                    request
                            });
                        }
                    }
                }
            } catch (Exception e) {
                showMessage("Error loading requests: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, windowTitle, messageType);
    }
}
