package gui.sysadmin.genomereleaseview;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import util.GenomeReleaseData;

public class GenomeReleaseViewCreator {
    
    GenomereleaseTableModel grTablemodel;
    
    public GenomeReleaseViewCreator() {
        
    }
    
    public JPanel buildGenomeReleaseView() {
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 250));
        mainPanel.add(buildGenomeReleasePanel(), BorderLayout.NORTH);
        return mainPanel;
    }
    
    public JPanel buildGenomeReleasePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = buildGenomeHeaderPanel();
        JPanel listPanel = buildGenomeFileList();
        JPanel addGenomePanel = buildAddGenomeFilePanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(addGenomePanel, BorderLayout.EAST);
        return mainPanel;
    }
    
    private JPanel buildGenomeHeaderPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JLabel label = new JLabel();
        /** TODO: set variable string! */
        label.setText("Genome release files");
        
        Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        label.setBorder(border);
        mainPanel.add(label, BorderLayout.WEST);
        return mainPanel;
    }
    
    private JPanel buildAddGenomeFilePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel containerPanel = new JPanel();
        
        GroupLayout layout = new GroupLayout(containerPanel);
        containerPanel.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 0);
        JLabel versionLabel = new JLabel();
        JLabel speciesLabel = new JLabel();
        JLabel fileLabel = new JLabel();
        
        versionLabel.setText("Genome release version:");
        speciesLabel.setText("Species:");
        fileLabel.setText("Genome file:");

        versionLabel.setBorder(border);
        speciesLabel.setBorder(border);
        fileLabel.setBorder(border);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton fileButton = new JButton("Select file");

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(flowLayout.LEADING);


        JPanel buttonPanel = new JPanel(flowLayout);
        JPanel buttonCeptionPanel = new JPanel(new BorderLayout());


        buttonPanel.add(fileButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        JTextField versionText = new JTextField(20);
        JTextField speciesText = new JTextField(20);
        buttonCeptionPanel.add(buttonPanel, BorderLayout.WEST);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(versionLabel).addComponent(versionText)
                        .addComponent(speciesLabel).addComponent(speciesText)
                        .addComponent(fileLabel).addComponent(buttonCeptionPanel)
        ));
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(versionLabel).addComponent(versionText)
                .addComponent(speciesLabel).addComponent(speciesText)
                .addComponent(fileLabel).addComponent(buttonCeptionPanel)
        );
        
        mainPanel.add(containerPanel, BorderLayout.NORTH);
        return mainPanel;
    }
    
    public JPanel buildGenomeFileList() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        /******************************* TEST *********************************/
        
        /** TODO Call the syscontroller here to get the data from server */

        GenomeReleaseData gr1 = new GenomeReleaseData("version1", "dolphin",
                "filename.txt");
        
        GenomeReleaseData gr2 = new GenomeReleaseData("version2", "pig",
                "bfilename.txt");
        
        GenomeReleaseData gr3 = new GenomeReleaseData("version3", "zebra",
                "afilename.txt");
        
        GenomeReleaseData[] grdarray = new GenomeReleaseData[3];
        grdarray[0] = gr1;
        grdarray[1] = gr2;
        grdarray[2] = gr3;
        
        /***********************************************************************/
        
        grTablemodel = new GenomereleaseTableModel();
        
        JTable grTable = new JTable(grTablemodel);
        grTablemodel.setGenomeReleases(grdarray);
        /** Wrong array */
        grTable.setShowGrid(false);
        
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(
                grTablemodel);
        
        grTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        grTable.setRowSorter(rowSorter);
        
        JScrollPane scrollPane = new JScrollPane(grTable);
        JTableHeader header2 = grTable.getTableHeader();
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JScrollPane buildfileList() {
        
        String[] header = new String[] { "From version", "To version",
                "File name", "Species" };
        
        Object[][] table = new Object[][] {
                { "Genome release 3.0", "Genome release 1.0",
                        "randomfilename.txt", "Human" },
                { "Genome release 4.0", "Genome release 3.0",
                        "randomfilename.txt", "Human" },
                { "Genome release 3.0", "Genome release 4.0",
                        "randomfilename.txt", "Human" },
                { "Genome release 4.0", "Genome release 5.0",
                        "randomfilename.txt", "Human" },
                { "Genome release 5.0", "Genome release 3.0",
                        "randomfilename.txt", "Human" } };
        
        JTable cfTable = new JTable(table, header);
        cfTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(cfTable);
        
        return scrollPane;
    }
    
    /** TODO: Anna, add your code here! */
    private JPanel buildDropDownFilter() {
        return new JPanel();
    }
    
    private JPanel buildChainFileList() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel filterPanel = buildDropDownFilter();
        JScrollPane tablePanel = buildfileList();
        
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel buildAddChainFilePanel() {
        JPanel mainPanel = new JPanel();
        
        return mainPanel;
    }
    
    public TableModel getTableModel() {
        // TODO Auto-generated method stub
        return grTablemodel;
    }
    
}
