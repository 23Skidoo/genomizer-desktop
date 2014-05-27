package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.ExperimentData;
import util.TreeTable;

public class WorkspaceTab extends JPanel {
    
    private static final long serialVersionUID = -7278768268151806081L;
    private TreeTable table;
    private JPanel buttonPanel;
    private JButton deleteButton, removeButton, downloadButton;
    private JButton uploadToButton, processButton;
    
    public WorkspaceTab() {
        setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Workspace"));
        JPanel filePanel = new JPanel(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(filePanel, BorderLayout.CENTER);
        
        buttonPanel.setLayout(new FlowLayout());
        
        // buttonPanel.setBackground(new Color(210, 210, 210));
        filePanel.setBackground(Color.white);
        
        createButtons();
        addToButtonPanel();
        buttonPanel.setVisible(true);
        table = new TreeTable();
        // table.setContent(ExperimentData.getExample());
        filePanel.add(table, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void createButtons() {
        // removeButton = CustomButtonFactory.makeCustomButton(
        // IconFactory.getClearIcon(50, 50),
        // IconFactory.getClearHoverIcon(52, 52), 52, 52,
        // "Remove selected data from workspace");
        deleteButton = new JButton("Delete from database");
        deleteButton.setPreferredSize(new Dimension(190, 40));
        removeButton = new JButton("Remove from workspace");
        removeButton.setPreferredSize(new Dimension(190, 40));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedData();
            }
        });
        downloadButton = new JButton("Download");
        downloadButton.setPreferredSize(new Dimension(150, 40));
        uploadToButton = new JButton("Upload to");
        uploadToButton.setPreferredSize(new Dimension(150, 40));
        processButton = new JButton("Process");
        processButton.setPreferredSize(new Dimension(150, 40));
        // downloadButton = CustomButtonFactory.makeCustomButton(
        // IconFactory.getDownloadIcon(45, 45),
        // IconFactory.getDownloadHoverIcon(47, 47), 47, 47,
        // "Download selected data");
        //
        // analyzeButton = CustomButtonFactory.makeCustomButton(
        // IconFactory.getAnalyzeIcon(50, 50),
        // IconFactory.getAnalyzeHoverIcon(52, 52), 52, 52,
        // "Analyze selected data");
        // analyzeButton.setEnabled(false);
        //
        // processButton = CustomButtonFactory.makeCustomButton(
        // IconFactory.getProcessIcon(50, 50),
        // IconFactory.getProcessHoverIcon(52, 52), 52, 52,
        // "Process selected data");
        // processButton.setEnabled(false);
    }
    
    private void addToButtonPanel() {
        buttonPanel.add(deleteButton);
        
        buttonPanel.add(Box.createHorizontalStrut(50));
        
        buttonPanel.add(removeButton);
        
        buttonPanel.add(Box.createHorizontalStrut(50));
        
        buttonPanel.add(downloadButton);
        
        buttonPanel.add(Box.createHorizontalStrut(50));
        
        buttonPanel.add(uploadToButton);
        
        buttonPanel.add(Box.createHorizontalStrut(50));
        
        buttonPanel.add(processButton);
        
    }
    
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
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
    
    public void addProcessFileListener(ActionListener listener) {
        processButton.addActionListener(listener);
    }
    
    public void addUploadToListener(ActionListener listener) {
        uploadToButton.addActionListener(listener);
    }
    
    public void addDeleteSelectedListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    private String[] concatArrays(String[] first, String[] second) {
        ArrayList<String> both = new ArrayList<>(first.length + second.length);
        Collections.addAll(both, first);
        Collections.addAll(both, second);
        return both.toArray(new String[both.size()]);
    }
    
    public void addExperimentsToTable(ArrayList<ExperimentData> newExperiments) {
        ArrayList<ExperimentData> expList = new ArrayList<>();
        if (table.getContent() != null) {
            expList.addAll(table.getContent());
        }
        for (ExperimentData newExperiment : newExperiments) {
            boolean alreadyInTable = false;
            for (ExperimentData existingExperiment : expList) {
                if (newExperiment.name.equals(existingExperiment.name)) {
                    alreadyInTable = true;
                    existingExperiment.addFiles(newExperiment.files);
                    break;
                }
            }
            if (!alreadyInTable) {
                expList.add(newExperiment);
            }
        }
        
        table.setContent(expList);
    }
    
    public ArrayList<ExperimentData> getSelectedData() {
        return table.getSelectedData();
    }
    
    public ArrayList<ExperimentData> getSelectedExperiments() {
        return table.getSelectedExperiments();
    }
    
    public synchronized void removeSelectedData() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                table.removeSelectedData();
            }
        });
    }
}
