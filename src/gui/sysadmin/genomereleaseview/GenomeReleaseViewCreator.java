package gui.sysadmin.genomereleaseview;

import com.sun.org.apache.xpath.internal.SourceTree;
import gui.sysadmin.strings.SysStrings;
import util.FileData;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class GenomeReleaseViewCreator {

    private GenomereleaseTableModel grTablemodel;
    private ActionListener          buttonListener;

    private JTable grTable;

    private JTextField versionText;
    private JComboBox speciesText;
    private JTextField fileText;

    private JButton addButton;
    private JButton clearButton;
    private JButton deleteButton;
    private JButton fileButton;
    private GenomeTextFieldListener textListener;




    public GenomeReleaseViewCreator() {
    }

    public JPanel buildGenomeReleaseView(ActionListener buttonListener,
            GenomeTextFieldListener textListener) {
        this.buttonListener = buttonListener;
        this.textListener = textListener;
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 250));
        mainPanel.add(buildGenomeReleasePanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    public JPanel buildGenomeReleasePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel headerPanel = buildGenomeHeaderPanel();
        JPanel listPanel = buildGenomeFileList();
        JPanel rightSidePanel = buildSidePanel();


        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(rightSidePanel, BorderLayout.EAST);
        return mainPanel;
    }

    private JPanel buildSidePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(buildAddNewSpeciePanel(), BorderLayout.NORTH);
        mainPanel.add(buildAddGenomeFilePanel(), BorderLayout.CENTER);

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

    private JPanel buildFileInfoPanel(){
        JPanel mainPanel = new JPanel(new BorderLayout());

        deleteButton = new JButton(SysStrings.GENOME_BUTTON_DELETE);
        deleteButton.addActionListener(buttonListener);
        JButton closeButton = new JButton(SysStrings.GENOME_BUTTON_CLOSE);
        closeButton.addActionListener(buttonListener);

        JPanel buttonCeptionPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        buttonCeptionPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(buttonCeptionPanel, BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel buildAddNewSpeciePanel(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory
                .createTitledBorder("Add new specie"));

        JPanel containerPanel = new JPanel();

        GroupLayout layout = new GroupLayout(containerPanel);
        containerPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 0);

        JPanel textNButton = new JPanel(new BorderLayout());

        /** TODO fix this text and button so it works properly*/
        JLabel specieLabel = new JLabel();
        specieLabel.setBorder(border);
        specieLabel.setText("Specie");
        JTextField specie = new JTextField(20);

        JButton button = new JButton("Add");

        textNButton.add(specie, BorderLayout.CENTER);
        textNButton.add(button, BorderLayout.EAST);

        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(specieLabel).addComponent(textNButton)
        ));

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(specieLabel).addComponent(textNButton)
        );


        mainPanel.add(containerPanel);

        return mainPanel;
    }

    private JPanel buildAddGenomeFilePanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory
                .createTitledBorder("Add new genome release"));
        JPanel containerPanel = new JPanel();

        GroupLayout layout = new GroupLayout(containerPanel);
        containerPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);



        /* text labels*/
        JLabel versionLabel = new JLabel();
        JLabel speciesLabel = new JLabel();
        JLabel fileLabel = new JLabel();

        versionLabel.setText(SysStrings.GENOME_TEXT_GR_VERSION);
        speciesLabel.setText(SysStrings.GENOME_TEXT_SPECIES);
        fileLabel.setText(SysStrings.GENOME_TEXT_GFILE);

        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 0);
        versionLabel.setBorder(border);
        speciesLabel.setBorder(border);
        fileLabel.setBorder(border);


        /* text fields*/
        versionText = new JTextField(20);
        versionText.addKeyListener(textListener);
        speciesText = new JComboBox();
        fileText = new JTextField(20);
        fileText.addKeyListener(textListener);
        fileText.setEditable(false);
        fileText.setEnabled(false);


        /* upload status panel*/
        //TODO place shit here

        JLabel fileName = new JLabel("genomefile.fasta");
        JProgressBar fileUploadProgress = new JProgressBar(0, 100);


        /* buttons */
        addButton = new JButton(SysStrings.GENOME_BUTTON_ADD);
        addButton.addActionListener(buttonListener);
        addButton.setEnabled(false);

        clearButton = new JButton(SysStrings.GENOME_BUTTON_CLEAR);
        clearButton.addActionListener(buttonListener);
        clearButton.setEnabled(false);

        fileButton = new JButton(SysStrings.GENOME_BUTTON_FILE);
        fileButton.addActionListener(buttonListener);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(flowLayout.LEADING);


        JPanel buttonPanel = new JPanel(flowLayout);
        JPanel buttonCeptionPanel = new JPanel(new BorderLayout());

        buttonPanel.add(fileButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        buttonCeptionPanel.add(buttonPanel, BorderLayout.WEST);





        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(versionLabel).addComponent(versionText)
                        .addComponent(speciesLabel).addComponent(speciesText)
                        .addComponent(fileLabel)
                        .addComponent(buttonCeptionPanel)
        ));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(versionLabel).addComponent(versionText)
                .addComponent(speciesLabel).addComponent(speciesText)
                .addComponent(fileLabel).addComponent(
                                buttonCeptionPanel)
        );




        mainPanel.add(containerPanel, BorderLayout.NORTH);
        return mainPanel;
    }

    public JPanel buildGenomeFileList() {
        JPanel mainPanel = new JPanel(new BorderLayout());


        grTablemodel = new GenomereleaseTableModel();

        grTable = new JTable(grTablemodel);

        grTable.setShowGrid(false);

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(
                grTablemodel);

        grTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        grTable.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(grTable);


        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buildFileInfoPanel(), BorderLayout.SOUTH);
        return mainPanel;
    }






    public TableModel getTableModel() {
        // TODO Auto-generated method stub
        return grTablemodel;
    }

    public String getVersionText() {
        return versionText.getText();
    }

    public String getSpeciesText() {
        return speciesText.getName();
    }

    public String getFileText() {
        return fileText.getText();
    }

    public void clearTextFields(){
        versionText.setText("");
        fileText.setText("");
        enableClearButton(false);
        enableAddButton(false);
    }

    public boolean isTextFieldsEmpty(){
        boolean returnValue = true;

        if(!versionText.getText().equals("") || !fileText.getText().equals("")){
            returnValue = false;
        }

        return returnValue;
    }

    public boolean allTextFieldsContainInfo(){
        boolean returnValue = false;
        if(!versionText.getText().equals("") && !fileText.getText().equals("")){
            returnValue = true;
        }

        return returnValue;
    }

    public void enableClearButton(boolean status){
        this.clearButton.setEnabled(status);
    }

    public void enableAddButton(boolean status){
        this.addButton.setEnabled(status);
    }

    public String getSelectedVersion(){
        return (String)grTable.getValueAt(grTable.getSelectedRow(),
                grTable.getTableHeader().getColumnModel()
                        .getColumnIndex(SysStrings.GENOME_TABLE_VERSION));
    }

    public String getSelectedSpecie(){
        return (String)grTable.getValueAt(grTable.getSelectedRow(),
                grTable.getTableHeader().getColumnModel()
                        .getColumnIndex(SysStrings.GENOME_TABLE_SPECIES));
    }

    public void selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        int ret = fileChooser.showOpenDialog(new JPanel());

        String directoryName = "";

        File[] selectedFiles;
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                directoryName = fileChooser.getSelectedFile()
                        .getCanonicalPath();
                selectedFiles = fileChooser.getSelectedFiles();

            } catch (IOException e) {
                e.printStackTrace();
            }
            fileText.setText(directoryName);
            enableClearButton(true);
            if(allTextFieldsContainInfo())
                enableAddButton(true);
        } else {
            return;
        }
    }

    public boolean isGeneSelected() {
        if(this.grTable.getSelectedRow() < 0){
            return false;
        } else {
            return true;
        }
    }

    public void setSpeciesDDList(String[] listItems){
        speciesText.removeAllItems();
        for(String item : listItems){
            speciesText.addItem(item);
        }
        speciesText.repaint();
    }










    /* old crap will be needed next year maybe*/
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
}
