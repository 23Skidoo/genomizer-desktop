package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserPanel extends JPanel {
    
    private static final long serialVersionUID = -5837140311329422316L;
    private String name, username;
    private boolean admin;
    JLabel usernameLabel;
    JButton logoutButton;
    
    public UserPanel() {
        setLayout(new BorderLayout());
        setLabels();
        setLogoutButton();
        logoutButton.setFocusable(false);
    }
    
    private void setLogoutButton() {
        logoutButton = new JButton("Logout");
        add(logoutButton, BorderLayout.EAST);
    }
    
    private void setLabels() {
        usernameLabel = new JLabel();
        add(usernameLabel, BorderLayout.CENTER);
    }
    
    public void setUserInfo(String username, String name, boolean admin) {
        this.name = name;
        this.username = username;
        this.admin = admin;
        usernameLabel.setText("<html><b> " + username + "</b></html>");
        this.repaint();
        this.revalidate();
    }
    
    public void addLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}
