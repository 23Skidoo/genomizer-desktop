package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

public class WorkspaceTab extends JPanel {

	private static final long serialVersionUID = -7278768268151806081L;
	private JPanel buttonPanel, filePanel;
	private JButton deleteButton, removeButton, downloadButton;
	private JButton analyzeButton, browseButton;
	private ImageIcon analyseIcon = new ImageIcon("src/icons/AnalyzeSelectedButton.png");
	private ImageIcon browseIcon = new ImageIcon("src/icons/BrowseLocalFilesButton.png");
	private ImageIcon deleteIcon = new ImageIcon("src/icons/DeleteSelectedButton.png");
	private ImageIcon downloadSelectedIcon = new ImageIcon("src/icons/DownloadSelectedButton.png");
	private ImageIcon removeFromDBIcon = new ImageIcon("src/icons/RemoveFromDatabaseButton.png");
	private GridBagConstraints gbc;

	public WorkspaceTab() {
        	setLayout(new BorderLayout());
        	buttonPanel = new JPanel();
        	filePanel = new JPanel(new BorderLayout());

        	add(buttonPanel, BorderLayout.NORTH);
        	add(filePanel, BorderLayout.CENTER);

        	GridBagLayout layout = new GridBagLayout();
        	gbc = new GridBagConstraints();

        	buttonPanel.setLayout(layout);
        	gbc.ipadx = 10;
        	gbc.ipady = 10;
        	gbc.insets = new Insets(1, 1, 1, 1);
        	gbc.anchor = GridBagConstraints.NORTHWEST;

        	buttonPanel.setBackground(new Color(210,210,210));
        	filePanel.setBackground(Color.white);

        	createButtons();
        	addToButtonPanel();
        	buttonPanel.setVisible(true);

        	String[] columnNames = { "Name", "Date", "Uploaded by", "Data type", "Local" };
        	Object[][] data = {
        		{ "Protein123_A5_2014.WIG", "2014-04-29", "Per", "Profile" , new Boolean(false)},
        		{ "Protein123_A5_2014.RAW", "2014-04-29", "Per", "RAW" ,  new Boolean(false)},
        		{ "Protein123_A5_2014.WIG", "2014-04-29", "Per", "Region" , new Boolean(false)} };

        	JTable table = new JTable(data, columnNames);
        	table.setBackground(new Color(210,210,210));

        	filePanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        	filePanel.add(table, BorderLayout.CENTER);
        	setVisible(true);
	}

	private void createButtons() {
		removeButton = new JButton();
		downloadButton = new JButton();
		analyzeButton = new JButton();
		browseButton = new JButton();
		deleteButton = new JButton();
	}

	private void addToButtonPanel() {

		gbc.gridx = 0;
		gbc.gridy = 0;
		deleteIcon = new ImageIcon(deleteIcon.getImage().getScaledInstance(175, 30, BufferedImage.SCALE_SMOOTH));
		deleteButton.setBorderPainted(true);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setIcon(deleteIcon);
		buttonPanel.add(deleteButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		removeFromDBIcon = new ImageIcon(removeFromDBIcon.getImage().getScaledInstance(175, 30, BufferedImage.SCALE_SMOOTH));
		removeButton.setBorderPainted(true);
		removeButton.setContentAreaFilled(false);
		removeButton.setIcon(removeFromDBIcon);
		buttonPanel.add(removeButton, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		downloadSelectedIcon = new ImageIcon(downloadSelectedIcon.getImage().getScaledInstance(175, 30, BufferedImage.SCALE_SMOOTH));
		downloadButton.setBorderPainted(true);
		downloadButton.setContentAreaFilled(false);
		downloadButton.setIcon(downloadSelectedIcon);
		buttonPanel.add(downloadButton, gbc);

		gbc.gridx = 3;
		gbc.gridy = 0;
		analyseIcon = new ImageIcon(analyseIcon.getImage().getScaledInstance(175, 30, BufferedImage.SCALE_SMOOTH));
		analyzeButton.setBorderPainted(true);
		analyzeButton.setContentAreaFilled(false);
		analyzeButton.setIcon(analyseIcon);
		buttonPanel.add(analyzeButton, gbc);

		gbc.gridx = 4;
		gbc.gridy = 0;
		browseIcon = new ImageIcon(browseIcon.getImage().getScaledInstance(175, 30, BufferedImage.SCALE_SMOOTH));
		browseButton.setBorderPainted(true);
		browseButton.setContentAreaFilled(false);
		browseButton.setIcon(browseIcon);
		buttonPanel.add(browseButton, gbc);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	public void addDownloadFileListener(ActionListener listener) {
		downloadButton.addActionListener(listener);
	}
}
