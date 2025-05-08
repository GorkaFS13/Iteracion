package gui;

import businessLogic.BLFacade;
import domain.User;
import domain.RideRequest;
import services.EmailService;
import exceptions.UserDoesntExistException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
    private final JButton jButtonRateDriver = new JButton("Rate Driver");

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

            
            try {
                jButtonRateDriver.setText(bundle.getString("RequestsStateGUI.RateDriver"));
            } catch (Exception ex) {
                jButtonRateDriver.setText("Rate Driver");
            }
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
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); 

        JLabel selectRideLabel = new JLabel(selectRideText);
        selectRideLabel.setFont(new Font("Arial", Font.BOLD, 12));

        topPanel.add(selectRideLabel);
        topPanel.add(ridesComboBox);

        tableModel = new DefaultTableModel(new Object[]{"Driver", "Ride", "Status", "Price (€)", "Request"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                
                if (columnIndex == 3) {
                    return Float.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                
                return false;
            }
        };

        requestsTable.setModel(tableModel);

        
        requestsTable.setRowHeight(30); 
        requestsTable.setIntercellSpacing(new Dimension(10, 5)); 
        requestsTable.setShowGrid(true); 
        requestsTable.setGridColor(new Color(230, 230, 230)); 
        requestsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12)); 
        requestsTable.setSelectionBackground(new Color(232, 242, 254)); 

        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
                }
                return c;
            }
        };

        
        requestsTable.getColumnModel().getColumn(0).setCellRenderer(defaultRenderer); 
        requestsTable.getColumnModel().getColumn(1).setCellRenderer(defaultRenderer); 
        requestsTable.getColumnModel().getColumn(2).setCellRenderer(defaultRenderer); 

        
        requestsTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                if (value instanceof Float) {
                    value = String.format("%.2f €", (Float) value);
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }

                return c;
            }
        });

        
        requestsTable.getColumnModel().getColumn(0).setPreferredWidth(100); 
        requestsTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        requestsTable.getColumnModel().getColumn(2).setPreferredWidth(120); 
        requestsTable.getColumnModel().getColumn(3).setPreferredWidth(100); 

        
        requestsTable.getColumnModel().removeColumn(requestsTable.getColumnModel().getColumn(4));

        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 

        
        Dimension buttonSize = new Dimension(120, 35);
        jButtonClose.setPreferredSize(buttonSize);
        jButtonDeleteRequest.setPreferredSize(buttonSize);
        jButtonPay.setPreferredSize(buttonSize);
        jButtonRateDriver.setPreferredSize(buttonSize);

        bottomPanel.add(jButtonClose);
        bottomPanel.add(jButtonDeleteRequest);
        bottomPanel.add(jButtonPay);
        bottomPanel.add(jButtonRateDriver);

        jButtonClose.addActionListener(e -> setVisible(false));
        jButtonPay.addActionListener(this::jButtonPay_actionPerformed);
        jButtonDeleteRequest.addActionListener(this::jButtonDelete_actionPerformed);
        jButtonRateDriver.addActionListener(this::jButtonRateDriver_actionPerformed);

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

        Object request = tableModel.getValueAt(selectedRow, 4);
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

        Object request = tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 2);
        boolean isAccepted = status.contains("Accepted");
        System.out.println("Is the request accepted: " + isAccepted);

        if (isAccepted) {
            if (request instanceof RideRequest) {
                RideRequest rideRequest = (RideRequest) request;

                
                float price = rideRequest.getRide().getPrice();

                
                if (price <= 0) {
                    System.out.println("Warning: Ride price is invalid: " + price + ". Setting to minimum price of 1.0");
                    price = 1.0f; 
                    rideRequest.getRide().setPrice(price);
                }

                
                rideRequest.getPayment().setPrice(price);

                System.out.println("Processing payment with price: €" + price);

                
                try {
                    User refreshedUser = facade.getUser(currentUser.getUsername());
                    if (refreshedUser != null) {
                        
                        currentUser.setWalletBalance(refreshedUser.getWalletBalance());
                        System.out.println("Retrieved latest wallet balance from database: €" + currentUser.getWalletBalance());
                    }
                } catch (Exception ex) {
                    System.out.println("Error refreshing user data before payment: " + ex.getMessage());
                }

                
                float currentBalance = currentUser.getWalletBalance();
                System.out.println("Checking if user has enough funds: Balance: €" + currentBalance + ", Price: €" + price);

                if (currentBalance < price) {
                    JOptionPane.showMessageDialog(this,
                        "Insufficient funds in your wallet. Current balance: €" + currentBalance +
                        ", Required: €" + price,
                        "Insufficient Funds",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                
                boolean withdrawSuccess = facade.withdrawMoneyFromWallet(currentUser.getUsername(), price);
                if (!withdrawSuccess) {
                    JOptionPane.showMessageDialog(this, "Failed to withdraw money from wallet",
                        "Payment Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                try {
                    
                    User refreshedUser = facade.getUser(currentUser.getUsername());
                    if (refreshedUser != null) {
                        currentUser.setWalletBalance(refreshedUser.getWalletBalance());
                        System.out.println("Updated local user wallet balance to: €" + currentUser.getWalletBalance());
                    }
                } catch (Exception ex) {
                    System.out.println("Error refreshing user data: " + ex.getMessage());
                }

                
                User driver = rideRequest.getDriver();
                boolean addSuccess = facade.addMoneyToWallet(driver.getUsername(), price);
                if (!addSuccess) {
                    System.out.println("Warning: Failed to add money to driver's wallet. Continuing with payment process.");
                }

                
                boolean paymentSuccessful = facade.payRequest(rideRequest);
                if (paymentSuccessful) {
                    
                    String emailSubject = "Ride Payment Confirmation";
                    String emailBody = "Dear " + currentUser.getUsername() + ",\n\n" +
                                      "Your payment of €" + price + " for the ride has been processed successfully.\n\n" +
                                      "Thank you for using our service!\n\n" +
                                      "Rides24 Team";

                    
                    String userEmail = currentUser.getEmail();
                    System.out.println("User email for confirmation: " + userEmail);

                    
                    boolean emailSent = EmailService.sendEmail(userEmail, emailSubject, emailBody);
                    System.out.println("Email sending result: " + (emailSent ? "Success" : "Failed"));

                    JOptionPane.showMessageDialog(this, "Request paid! €" + price + " has been deducted from your wallet.",
                        "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                System.out.println("ERROR: Something went wrong.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Still waiting for ride acceptance", "Error", JOptionPane.ERROR_MESSAGE);
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
                            
                            float ridePrice = request.getRide().getPrice();
                            if (ridePrice <= 0) {
                                System.out.println("Warning: Invalid ride price: " + ridePrice + ". Using minimum price of 1.0");
                                ridePrice = 1.0f;
                                
                                request.getRide().setPrice(ridePrice);
                            }

                            
                            float paymentPrice = request.getPayment().getPrice();
                            if (paymentPrice != ridePrice) {
                                System.out.println("Warning: Payment price (" + paymentPrice +
                                    ") doesn't match ride price (" + ridePrice + "). Updating payment price.");
                                request.getPayment().setPrice(ridePrice);
                            }

                            
                            String status = request.getState();
                            if ("Accepted".equals(status) && "Done".equals(request.getPayment().getStatus())) {
                                status = "Completed";
                            } else if ("Accepted".equals(status)) {
                                status = "Accepted (Unpaid)";
                            }

                            tableModel.addRow(new Object[]{
                                    request.getDriver().getUsername(),
                                    request.getRide().getFrom() + " → " + request.getRide().getTo() +
                                        " (" + request.getRide().getDate().toString().substring(0, 10) + ")",
                                    status,
                                    ridePrice, 
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

    
    private void jButtonRateDriver_actionPerformed(ActionEvent e) {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a ride to rate", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object request = tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 2);

        if (!"Completed".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "You can only rate rides that have been completed and paid for.",
                "Cannot Rate Yet",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (request instanceof RideRequest) {
            RideRequest rideRequest = (RideRequest) request;

            
            if (rideRequest.isRated()) {
                JOptionPane.showMessageDialog(this,
                    "You have already rated this ride.",
                    "Already Rated",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            
            RateDriverGUI rateDriverGUI = new RateDriverGUI(currentUser, rideRequest);
            rateDriverGUI.setVisible(true);

            
            rateDriverGUI.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    updateRequestsTable();
                }
            });
        }
    }
}
