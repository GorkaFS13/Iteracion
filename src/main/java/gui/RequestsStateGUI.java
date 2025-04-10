package gui;

import businessLogic.BLFacade;
import domain.User;
import domain.RideRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RequestsStateGUI extends JFrame {
    private final User currentUser;
    private final JComboBox<String> ridesComboBox = new JComboBox<>();
    private final JTable requestsTable = new JTable();
    private List<RideRequest> requests = new ArrayList<>();
    private DefaultTableModel tableModel;

    private final JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private final JButton jButtonPay = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RequestsStateGUI.Pay"));

    private final JButton jButtonDeleteRequest = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RequestsStateGUI.Delete"));

    private String selectRideText = "Select a ride:";
    private String userColumnText = "User";
    private String windowTitle = "Traveller Requests State";
    private String noRidesText = "No rides available";

    private final BLFacade facade = MainGUI.getBusinessLogic();

    public RequestsStateGUI(User currentUser) {
        this.currentUser = currentUser;
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

            jButtonClose.setText(bundle.getString("Close"));
            jButtonPay.setText(bundle.getString("RequestsStateGUI.Pay"));
            jButtonDeleteRequest.setText(bundle.getString("RequestsStateGUI.Delete"));
        } catch (Exception e) {
            System.out.println("Using default texts for missing resources");
        }
    }

    private void setupUI() {
        setTitle(windowTitle);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel(selectRideText));
        topPanel.add(ridesComboBox);

        tableModel = new DefaultTableModel(new Object[]{"Driver", "Ride", "Request Status", "Payment status", "Payment price", "Request"}, 0);
        requestsTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(jButtonClose);
        bottomPanel.add(jButtonDeleteRequest);
        bottomPanel.add(jButtonPay);

        jButtonClose.addActionListener(e -> setVisible(false));
        jButtonPay.addActionListener(this::jButtonPay_actionPerformed);
        jButtonDeleteRequest.addActionListener(this::jButtonDelete_actionPerformed);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void jButtonDelete_actionPerformed(ActionEvent actionEvent) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a request", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object request = tableModel.getValueAt(selectedRow, 5);
        if (request instanceof RideRequest) {
            if (facade.removeRequest((RideRequest) request)) {
                JOptionPane.showMessageDialog(this, "Request deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                tableModel.removeRow(selectedRow);
            }
        } else {
            System.out.println("ERROR: Something went wrong");
        }
    }

    private void jButtonPay_actionPerformed(ActionEvent e) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a ride", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object request = tableModel.getValueAt(selectedRow, 5);


        boolean isAccepted = tableModel.getValueAt(selectedRow, 2).equals("Accepted");
        System.out.println("Is the request accepted: " + isAccepted);

        if (isAccepted) {
            if (request instanceof RideRequest) {
                boolean paymentSuccessful = facade.payRequest((RideRequest) request);
                if (paymentSuccessful) {
                    JOptionPane.showMessageDialog(this, "Request paid!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                System.out.println("ERROR: Something went wrong.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Still waiting", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRidesData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                requests = facade.getRequestsUser(currentUser);
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (requests == null || requests.isEmpty()) {
                            showMessage(noRidesText, JOptionPane.INFORMATION_MESSAGE);
                            ridesComboBox.setEnabled(false);
                        } else {
                            updateRequestsTable();
                        }
                    } catch (Exception e) {
                        showMessage("Error loading requests: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        };
        worker.execute();
    }

    private void updateRequestsTable() {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<RideRequest> requests = facade.getRequestsUser(this.currentUser);

                if (requests != null && !requests.isEmpty()) {
                    for (RideRequest request : requests) {
                            tableModel.addRow(new Object[]{
                                    request.getRide(),
                                    request.getDriver().getEmail(),
                                    request.getState(),
                                    request.getPayment().getStatus(),
                                    request.getPayment().getPrice(),
                                    request
                            });
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
