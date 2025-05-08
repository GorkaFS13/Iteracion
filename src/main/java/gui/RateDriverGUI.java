package gui;

import businessLogic.BLFacade;
import domain.Driver;
import domain.RideRequest;
import domain.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


public class RateDriverGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final User currentUser;
    private final RideRequest rideRequest;
    private final Driver driver;

    private JPanel starsPanel;
    private JTextArea commentTextArea;
    private JButton submitButton;
    private JButton cancelButton;
    private int selectedRating = 0;

    private final BLFacade facade = MainGUI.getBusinessLogic();

    
    public RateDriverGUI(User currentUser, RideRequest rideRequest) {
        this.currentUser = currentUser;
        this.rideRequest = rideRequest;
        this.driver = rideRequest.getDriver();

        setupUI();
    }

    
    private void setupUI() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

        setTitle(bundle.getString("RateDriverGUI.Title"));
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel driverInfoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel driverLabel = new JLabel(bundle.getString("RateDriverGUI.Driver") + " " + driver.getUsername());
        driverLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel rideLabel = new JLabel(bundle.getString("RateDriverGUI.Ride") + " " + rideRequest.getRide().getFrom() + " → " + rideRequest.getRide().getTo());

        driverInfoPanel.add(driverLabel);
        driverInfoPanel.add(rideLabel);

        
        starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel ratingLabel = new JLabel(bundle.getString("RateDriverGUI.RateExperience"));
        starsPanel.add(ratingLabel);

        
        for (int i = 1; i <= 5; i++) {
            final int rating = i;
            JButton starButton = createStarButton(rating);
            starsPanel.add(starButton);
        }

        
        JPanel commentPanel = new JPanel(new BorderLayout(5, 5));
        JLabel commentLabel = new JLabel(bundle.getString("RateDriverGUI.Comment"));
        commentTextArea = new JTextArea(4, 20);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentTextArea);

        commentPanel.add(commentLabel, BorderLayout.NORTH);
        commentPanel.add(scrollPane, BorderLayout.CENTER);

        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        submitButton = new JButton(bundle.getString("RateDriverGUI.Submit"));
        cancelButton = new JButton(bundle.getString("RateDriverGUI.Cancel"));

        submitButton.setEnabled(false); 

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonsPanel.add(submitButton);
        buttonsPanel.add(cancelButton);

        
        mainPanel.add(driverInfoPanel, BorderLayout.NORTH);
        mainPanel.add(starsPanel, BorderLayout.CENTER);
        mainPanel.add(commentPanel, BorderLayout.SOUTH);

        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    
    private JButton createStarButton(final int rating) {
        JButton starButton = new JButton("★");
        starButton.setFont(new Font("Arial", Font.PLAIN, 24));
        starButton.setForeground(Color.GRAY);
        starButton.setFocusPainted(false);
        starButton.setBorderPainted(false);
        starButton.setContentAreaFilled(false);

        starButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedRating = rating;
                updateStarButtons();
                submitButton.setEnabled(true);
            }
        });

        return starButton;
    }

    
    private void updateStarButtons() {
        for (int i = 0; i < starsPanel.getComponentCount(); i++) {
            Component component = starsPanel.getComponent(i);
            if (component instanceof JButton && "★".equals(((JButton) component).getText())) {
                int starIndex = i - 1; 
                if (starIndex >= 0) {
                    if (starIndex < selectedRating) {
                        ((JButton) component).setForeground(Color.ORANGE);
                    } else {
                        ((JButton) component).setForeground(Color.GRAY);
                    }
                }
            }
        }
    }

    
    private void submitRating() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

        if (selectedRating < 1 || selectedRating > 5) {
            JOptionPane.showMessageDialog(this,
                bundle.getString("RateDriverGUI.RatingRequired"),
                bundle.getString("RateDriverGUI.RatingRequired"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String comment = commentTextArea.getText().trim();

        boolean success = facade.rateDriver(driver, currentUser, rideRequest, selectedRating, comment);

        if (success) {
            JOptionPane.showMessageDialog(this,
                bundle.getString("RateDriverGUI.ThankYou"),
                bundle.getString("RateDriverGUI.RatingSubmitted"),
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                bundle.getString("RateDriverGUI.RatingError"),
                bundle.getString("RateDriverGUI.RatingError"),
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
