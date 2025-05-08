package gui;

import businessLogic.BLFacade;
import domain.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import exceptions.UserDoesntExistException;

public class WalletGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel jContentPane;
    private JLabel jLabelBalance;
    private JLabel jLabelAmount;
    private JTextField jTextFieldAmount;
    private JButton jButtonAddMoney;
    private JButton jButtonWithdraw;
    private JButton jButtonClose;
    private JLabel jLabelMsg;
    private User currentUser;

    public WalletGUI(User user) {
        this.currentUser = user;
        initialize();
        
        refreshUserData();
    }

    private void initialize() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

        this.setSize(new Dimension(500, 300));
        this.setTitle(bundle.getString("WalletGUI.Title"));

        jContentPane = new JPanel();
        jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
        jContentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jLabelBalance = new JLabel(bundle.getString("WalletGUI.CurrentBalance") + currentUser.getWalletBalance());
        jLabelBalance.setFont(new Font("Tahoma", Font.BOLD, 16));
        balancePanel.add(jLabelBalance);

        
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jLabelAmount = new JLabel(bundle.getString("WalletGUI.Amount") + " ");
        jTextFieldAmount = new JTextField(10);
        amountPanel.add(jLabelAmount);
        amountPanel.add(jTextFieldAmount);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jButtonAddMoney = new JButton(bundle.getString("WalletGUI.AddMoney"));
        jButtonWithdraw = new JButton(bundle.getString("WalletGUI.Withdraw"));
        jButtonClose = new JButton(bundle.getString("Close"));

        jButtonAddMoney.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMoney();
            }
        });

        jButtonWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdrawMoney();
            }
        });

        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(jButtonAddMoney);
        buttonPanel.add(jButtonWithdraw);
        buttonPanel.add(jButtonClose);

        
        JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jLabelMsg = new JLabel("");
        jLabelMsg.setForeground(Color.RED);
        msgPanel.add(jLabelMsg);

        
        jContentPane.add(balancePanel);
        jContentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        jContentPane.add(amountPanel);
        jContentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        jContentPane.add(buttonPanel);
        jContentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        jContentPane.add(msgPanel);

        this.setContentPane(jContentPane);
    }

    private void addMoney() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");
        try {
            float amount = Float.parseFloat(jTextFieldAmount.getText());
            if (amount <= 0) {
                jLabelMsg.setText(bundle.getString("WalletGUI.EnterValidNumber"));
                return;
            }

            
            boolean localSuccess = currentUser.addMoneyToWallet(amount);
            if (!localSuccess) {
                jLabelMsg.setText(bundle.getString("WalletGUI.FailedToAdd"));
                jLabelMsg.setForeground(Color.RED);
                return;
            }

            
            BLFacade facade = MainGUI.getBusinessLogic();
            boolean dbSuccess = facade.addMoneyToWallet(currentUser.getUsername(), amount);

            if (dbSuccess) {
                updateBalanceLabel();
                jLabelMsg.setText(bundle.getString("WalletGUI.SuccessfullyAdded") + amount);
                jLabelMsg.setForeground(new Color(0, 128, 0)); 

                
                refreshUserData();
            } else {
                
                currentUser.setWalletBalance(currentUser.getWalletBalance() - amount);
                jLabelMsg.setText(bundle.getString("WalletGUI.FailedToAdd"));
                jLabelMsg.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            jLabelMsg.setText(bundle.getString("WalletGUI.EnterValidNumber"));
            jLabelMsg.setForeground(Color.RED);
        }
    }

    private void withdrawMoney() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");
        try {
            float amount = Float.parseFloat(jTextFieldAmount.getText());
            if (amount <= 0) {
                jLabelMsg.setText(bundle.getString("WalletGUI.EnterValidNumber"));
                return;
            }

            
            float initialBalance = currentUser.getWalletBalance();
            System.out.println("Withdrawal attempt: Current balance before withdrawal: €" + initialBalance);
            System.out.println("Withdrawal amount requested: €" + amount);

            
            if (initialBalance < amount) {
                jLabelMsg.setText(bundle.getString("WalletGUI.InsufficientFunds") + ": €" + initialBalance + " < €" + amount);
                jLabelMsg.setForeground(Color.RED);
                return;
            }

            
            boolean localSuccess = currentUser.withdrawMoneyFromWallet(amount);
            if (!localSuccess) {
                jLabelMsg.setText(bundle.getString("WalletGUI.InsufficientFunds"));
                jLabelMsg.setForeground(Color.RED);
                System.out.println("Local withdrawal failed despite balance check");
                return;
            }

            System.out.println("Local withdrawal successful. New local balance: €" + currentUser.getWalletBalance());

            
            BLFacade facade = MainGUI.getBusinessLogic();
            boolean dbSuccess = facade.withdrawMoneyFromWallet(currentUser.getUsername(), amount);
            System.out.println("Database withdrawal result: " + (dbSuccess ? "Success" : "Failed"));

            if (dbSuccess) {
                updateBalanceLabel();
                jLabelMsg.setText(bundle.getString("WalletGUI.SuccessfullyWithdrawn") + amount);
                jLabelMsg.setForeground(new Color(0, 128, 0)); 

                
                refreshUserData();
                System.out.println("After refresh: Current balance: €" + currentUser.getWalletBalance());
            } else {
                
                currentUser.setWalletBalance(initialBalance);
                updateBalanceLabel();
                jLabelMsg.setText(bundle.getString("WalletGUI.FailedToWithdraw"));
                jLabelMsg.setForeground(Color.RED);
                System.out.println("Reverted local balance to: €" + currentUser.getWalletBalance());
            }
        } catch (NumberFormatException e) {
            jLabelMsg.setText(bundle.getString("WalletGUI.EnterValidNumber"));
            jLabelMsg.setForeground(Color.RED);
        } catch (Exception e) {
            jLabelMsg.setText(bundle.getString("WalletGUI.ErrorDuringWithdrawal") + " " + e.getMessage());
            jLabelMsg.setForeground(Color.RED);
            e.printStackTrace();
        }
    }

    private void updateBalanceLabel() {
        ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");
        jLabelBalance.setText(bundle.getString("WalletGUI.CurrentBalance") + currentUser.getWalletBalance());
    }

    
    private void refreshUserData() {
        System.out.println("WalletGUI.refreshUserData: Refreshing user data for " + currentUser.getUsername());
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            User refreshedUser = facade.getUser(currentUser.getUsername());

            if (refreshedUser != null) {
                float oldBalance = currentUser.getWalletBalance();
                float newBalance = refreshedUser.getWalletBalance();

                
                currentUser.setWalletBalance(newBalance);

                
                System.out.println("WalletGUI.refreshUserData: Updated wallet balance from €" +
                    oldBalance + " to €" + newBalance);

                
                updateBalanceLabel();
            } else {
                System.err.println("WalletGUI.refreshUserData: Refreshed user is null");
            }
        } catch (UserDoesntExistException e) {
            System.err.println("WalletGUI.refreshUserData: Error refreshing user data: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error refreshing user data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("WalletGUI.refreshUserData: Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
