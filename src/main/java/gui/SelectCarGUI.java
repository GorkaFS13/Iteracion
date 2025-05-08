package gui;

import businessLogic.BLFacade;
import domain.CarType;
import domain.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


public class SelectCarGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final Driver driver;
    private final BLFacade facade;
    private final JPanel carSelectionPanel;
    private CarType selectedCarType;

    
    private final JToggleButton[] carButtons = new JToggleButton[CarType.values().length];
    private final ButtonGroup buttonGroup = new ButtonGroup();

    
    private final JLabel titleLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Title"));
    private final JButton saveButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Save"));
    private final JButton cancelButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Cancel"));

    
    public SelectCarGUI(Driver driver) {
        this.driver = driver;
        this.facade = MainGUI.getBusinessLogic();
        this.selectedCarType = driver.getCarType();

        
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Title"));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        
        JPanel carSelectionWrapper = new JPanel(new BorderLayout());
        carSelectionWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        carSelectionPanel = new JPanel(new GridLayout(1, CarType.values().length, 20, 10));
        createCarButtons();

        carSelectionWrapper.add(carSelectionPanel, BorderLayout.CENTER);
        mainPanel.add(carSelectionWrapper, BorderLayout.CENTER);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        saveButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setPreferredSize(new Dimension(120, 40));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCarSelection();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        setContentPane(mainPanel);
    }

    
    private void createCarButtons() {
        CarType[] carTypes = CarType.values();

        for (int i = 0; i < carTypes.length; i++) {
            CarType carType = carTypes[i];

            
            JPanel carPanel = new JPanel();
            carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
            carPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            carPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

            
            carButtons[i] = new JToggleButton();
            carButtons[i].setActionCommand(carType.name());
            buttonGroup.add(carButtons[i]);

            
            ImageIcon carIcon = carType.getScaledImageIcon(120, 80);
            if (carIcon != null) {
                carButtons[i].setIcon(carIcon);
            } else {
                carButtons[i].setText(carType.getDisplayName());
            }

            carButtons[i].setPreferredSize(new Dimension(200, 120));
            carButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            carButtons[i].setVerticalAlignment(SwingConstants.CENTER);

            
            if (carType == driver.getCarType()) {
                carButtons[i].setSelected(true);
            }

            
            final CarType finalCarType = carType;
            carButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCarType = finalCarType;
                }
            });

            
            JLabel carLabel = new JLabel(carType.getDisplayName());
            carLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            carLabel.setFont(new Font("Arial", Font.BOLD, 14));

            
            carPanel.add(Box.createVerticalGlue()); 
            carPanel.add(carButtons[i]);
            carPanel.add(Box.createVerticalStrut(10));
            carPanel.add(carLabel);
            carPanel.add(Box.createVerticalGlue()); 

            
            carSelectionPanel.add(carPanel);
        }
    }

    
    private void saveCarSelection() {
        if (selectedCarType != null) {
            boolean success = facade.updateDriverCarType(driver, selectedCarType);

            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.SaveSuccess"),
                    ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Success"),
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.SaveError"),
                    ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Error"),
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.SelectPrompt"),
                ResourceBundle.getBundle("Etiquetas").getString("SelectCarGUI.Warning"),
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
