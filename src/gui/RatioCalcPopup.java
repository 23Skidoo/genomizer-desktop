package gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class RatioCalcPopup extends JFrame{

    private JLabel errorLabel;
    private JPanel ratioPanel;
    private final JTextField inputReads = new JTextField();
    private final JTextField chromosome = new JTextField();
    private final JTextField ratioWindowSize = new JTextField();
    private final JTextField ratioStepPosition = new JTextField();
    private final JCheckBox ratioPrintMean = new JCheckBox("Print mean");
    private final JCheckBox ratioPrintZeros = new JCheckBox("Print zeros");
    private final JComboBox<String> single = new JComboBox<String>();
    private final JComboBox<String> ratioSmoothType = new JComboBox<String>();

    public RatioCalcPopup(final GenomizerView parent){
        addWindowListener(new WindowAdapter() {
            @Override
           public void windowClosed(WindowEvent e) {
                parent.getFrame().dispose();
            }
        });
        setTitle("Ratio calculation parameters");
        setResizable(false);
        setSize(new Dimension(700,600));
        this.setLocationRelativeTo(parent.getFrame());
        placeComponents();
    }
    /**
     * Sets the layout and looks to the login window
     */
    private void placeComponents() {

        ratioPanel = new JPanel(new FlowLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel.setBorder(BorderFactory.createTitledBorder("Ratio calculation"));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Ratio smoothing"));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(""));

        ratioPanel.add(topPanel);
        ratioPanel.add(centerPanel);
        ratioPanel.add(bottomPanel);
        ratioPanel.add(buttonPanel);

        topPanel.add(single);
        topPanel.add(inputReads);
        topPanel.add(chromosome);
        centerPanel.add(ratioWindowSize);
        centerPanel.add(ratioSmoothType);
        centerPanel.add(ratioStepPosition);
        bottomPanel.add(ratioPrintZeros);
        bottomPanel.add(ratioPrintMean);

       single.setPreferredSize(new Dimension(150,60));
       inputReads.setBorder(BorderFactory.createTitledBorder("Input reads cut-off"));
       inputReads.setPreferredSize(new Dimension(160,60));
       chromosome.setBorder(BorderFactory.createTitledBorder("Chromosomes"));
       chromosome.setPreferredSize(new Dimension(120,60));

       ratioWindowSize.setBorder(BorderFactory.createTitledBorder("Window size"));
       ratioWindowSize.setPreferredSize(new Dimension(120,60));
       ratioSmoothType.setBorder(BorderFactory.createTitledBorder("Smooth type"));
       ratioSmoothType.setPreferredSize(new Dimension(120,60));
       ratioStepPosition.setBorder(BorderFactory.createTitledBorder("Step position"));
       ratioStepPosition.setPreferredSize(new Dimension(120,60));

       this.add(ratioPanel);

    }

    /**
     * Adds listener to the loginbutton
     * @param listener
     *            The listener to login to the server
     */
    public void addLoginListener(ActionListener listener) {
   //     loginButton.addActionListener(listener);
    }

    public void updateLoginFailed(String errorMessage) {
        paintErrorMessage(errorMessage);
    }

    public void paintErrorMessage(String message) {
        errorLabel = new JLabel("<html><b>" + message + "</b></html>");
        errorLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        errorLabel.setBounds(120, 100, 150, 45);
        ratioPanel.add(errorLabel);
        repaint();
    }

    public void removeErrorMessage() {
        if (errorLabel != null) {
            ratioPanel.remove(errorLabel);
            errorLabel = null;
            repaint();
        }
    }
}

