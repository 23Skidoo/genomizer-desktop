package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.AnnotationDataType;
import util.AnnotationDataValue;
import util.FileDrop;
import util.GenomeReleaseData;

import communication.HTTPURLUpload;

public class UploadToNewExpPanel extends JPanel implements ExperimentPanel {

    private HashMap<File, UploadFileRow> uploadFileRows;
    private HashMap<String, JComboBox> annotationBoxes;
    private HashMap<String, JTextField> annotationFields;
    private ArrayList<String> annotationHeaders;
    private JPanel /* uploadPanel, */uploadFilesPanel, newExpPanel,
            buttonsPanel, uploadBackground;
    private JButton uploadButton, uploadSelectedBtn, selectButton;
    private AnnotationDataType[] annotations;
    private JLabel expNameLabel, genomeLabel, boldTextLabel;
    private JTextField expID;
    private JComboBox<String> species;
    private ArrayList<String> genome;
    private CopyOnWriteArrayList<HTTPURLUpload> ongoingUploads;

    public UploadToNewExpPanel() {
        setLayout(new BorderLayout());
        uploadFileRows = new HashMap<File, UploadFileRow>();
        annotationBoxes = new HashMap<String, JComboBox>();
        annotationFields = new HashMap<String, JTextField>();
        annotationHeaders = new ArrayList<String>();
        uploadBackground = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel(new FlowLayout());
        uploadFilesPanel = new JPanel(new GridLayout(0, 1));
        selectButton = new JButton("Browse files");
        uploadSelectedBtn = new JButton("Create with selected files");
        uploadButton = new JButton("Create with all files");
        newExpPanel = new JPanel();
        expNameLabel = new JLabel();
        boldTextLabel = new JLabel(
                "<html><b>Bold text indicates a forced annotation.</b></html>");
        boldTextLabel.setOpaque(true);
        expNameLabel = new JLabel();
        expID = new JTextField();
        expID.setColumns(10);
        expID.getDocument().addDocumentListener(new FreetextListener());
        species = new JComboBox<String>();
        species.setPreferredSize(new Dimension(120, 31));
        genomeLabel = new JLabel();
        genome = new ArrayList<String>();
        enableUploadButton(false);
    }

    /**
     * TRY
     */
    public void build() {
        createNewExp();
        repaintSelectedFiles();
    }

    public void createNewExpPanel(AnnotationDataType[] annotations) {
        this.annotations = annotations;
        build();
    }

    @Override
    public void removeAll() {
        newExpPanel.removeAll();
        super.removeAll();
    }

    public HashMap<File, UploadFileRow> getFileRows() {
        return uploadFileRows;
    }

    public String getSelectedSpecies() {
        return species.getSelectedItem().toString();
    }

    public void setGenomeReleases(GenomeReleaseData[] grd) {
        if(genome.size() > 0) {
            genome.clear();
        }
        for(GenomeReleaseData g : grd) {
            genome.add(g.getVersion());
        }
    }

    public ArrayList<String> getGenomeReleases() {
        return genome;
    }
    public String getGenomeVersion() {
//        return genome.getSelectedItem().toString();
        return null;
    }

    /**
     * Method adding a listener to the "selectButton".
     *
     * @param listener
     *            The listener to select files.
     */
    public void addSelectButtonListener(ActionListener listener) {
        selectButton.addActionListener(listener);
    }

    /**
     * Method adding a listener to the "uploadButton".
     *
     * @param listener
     *            The listener to start uploading selected files.
     */
    public void addUploadButtonListener(ActionListener listener) {
        uploadButton.addActionListener(listener);
    }

    public void addUploadSelectedFilesListener(ActionListener listener) {
        uploadSelectedBtn.addActionListener(listener);

    }

    public void addSpeciesSelectedListener(ActionListener listener) {
        species.addActionListener(listener);
    }

    /**
     * A method creating a panel for creating a new experiment to upload files
     * to it.
     */
    public void createNewExp() {
        /*
         * setBorder(BorderFactory
         * .createTitledBorder("Create new experiment"));
         */
        try {
            GridBagLayout gbl_panel = new GridBagLayout();
            gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
            gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
            gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, Double.MIN_VALUE };
            gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0,
                    Double.MIN_VALUE };
            newExpPanel.setLayout(gbl_panel);
            add(newExpPanel, BorderLayout.NORTH);
            addAnnotationsForExp();
            repaintSelectedFiles();
            uploadBackground.add(uploadFilesPanel, BorderLayout.NORTH);
            add(uploadBackground, BorderLayout.CENTER);
            // Makes dragging & dropping of files into the panel possible
            new FileDrop(this, new FileDrop.Listener() {
                public void filesDropped(java.io.File[] files) {
                    createUploadFileRow(files);
                    enableUploadButton(true);
                }
            });
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this,
                    "Eggs are supposed to be green.", "Inane error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A method dynamically adding annotations from the server. In order to
     * customize the experiment, which the files should be uploaded to.
     *
     *
     * @throws NullPointerException
     *             if a annotation points at null value.
     */
    private void addAnnotationsForExp() throws NullPointerException {
        annotationBoxes = new HashMap<String, JComboBox>();
        annotationFields = new HashMap<String, JTextField>();
        annotationHeaders.clear();
        annotationBoxes = new HashMap<>();
        annotationFields = new HashMap<>();
        int x = 0;
        int y = 0;
        String[] annotationNames = new String[annotations.length];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 30);
        gbc.gridx = x;
        gbc.gridy = y;
        JPanel exp = new JPanel(new BorderLayout());
        expNameLabel.setText("<html><b>Experiment ID</b></html>");
        expNameLabel.setToolTipText("Bold indicates a forced annotation");
        exp.add(expNameLabel, BorderLayout.NORTH);
        exp.add(expID, BorderLayout.CENTER);
        newExpPanel.add(exp, gbc);
        x++;
        for (int i = 0; i < annotations.length; i++) {

            if (annotations[i].getValues().length > 0) {
                if (x > 6) {
                    x = 0;
                    y++;
                }
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 0, 5, 30);
                gbc.gridx = x;
                gbc.gridy = y;
                JPanel p = new JPanel(new BorderLayout());
                String label = null;
                JLabel annotationLabel = null;
                if (annotations[i].isForced()) {
                    annotationLabel = new JLabel("<html><b>"
                            + annotations[i].getName() + "</b></html>");
                    annotationLabel
                            .setToolTipText("Bold indicates a forced annotation");
                } else {
                    annotationLabel = new JLabel(annotations[i].getName());
                }
                annotationHeaders.add(annotations[i].getName());
                p.add(annotationLabel, BorderLayout.NORTH);
                if (annotations[i].getValues()[0].equals("freetext")) {
                    final JTextField textField = new JTextField();
                    textField.setColumns(10);

                    // Add listener for when the text in the textfield changes.
                    textField.getDocument().addDocumentListener(
                            new FreetextListener());

                    annotationFields.put(annotations[i].getName(), textField);
                    p.add(textField, BorderLayout.CENTER);
                    newExpPanel.add(p, gbc);

                } else {
                    if (annotations[i].getName().equalsIgnoreCase("species")) {
                        if(species.getItemCount() > 0) {
                            species.removeAllItems();
                        }
                        for (String s : annotations[i].getValues()) {
                            System.out.println(s);
                            species.addItem(s);
                        }
                        annotationBoxes.put(annotations[i].getName(), species);
                        p.add(species, BorderLayout.CENTER);
                        newExpPanel.add(p, gbc);
                    } else {
                        final JComboBox<String> comboBox = new JComboBox<String>(
                                annotations[i].getValues());
                        comboBox.setPreferredSize(new Dimension(120, 31));
                        /*
                         * Listener for when the user chooses something in the
                         * combobox.
                         */
                        comboBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                String text = (String) comboBox
                                        .getSelectedItem();
                                if (!text.equals("") && text != null) {
                                    enableUploadButton(true);
                                }
                            }
                        });
                        annotationBoxes.put(annotations[i].getName(), comboBox);
                        p.add(comboBox, BorderLayout.CENTER);
                        newExpPanel.add(p, gbc);
                    }
                }
                x++;
            }
        }
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.insets = new Insets(5, 0, 5, 30);
//        gbc.gridx = x;
//        gbc.gridy = y;
//        JPanel gr = new JPanel(new BorderLayout());
//        genomeLabel.setText("<html><b>Genome release</b></html>");
//        genomeLabel.setToolTipText("Bold indicates a forced annotation");
//        gr.add(genomeLabel, BorderLayout.NORTH);
//        gr.add(genome, BorderLayout.CENTER);
//        newExpPanel.add(gr, gbc);
    }

    /**
     * Creates an uploadFileRow from the provided files. Checks if the files are
     * already in an uploadFileRow so there won't be duplicates. Displays an
     * error message if it was selected and added previously.
     *
     * @param files
     *            The files to make an uploadFileRow out of.
     */
    public void createUploadFileRow(File[] files) {
        for (File f : files) {
            if (!uploadFileRows.containsKey(f)) {
                UploadFileRow fileRow = new UploadFileRow(f, this, true);
                uploadFileRows.put(f, fileRow);
            } else {
                JOptionPane.showMessageDialog(this, "File already selected: "
                        + f.getName() + "", "File error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        repaintSelectedFiles();
    }

    /**
     * Checks if there are any uploadfilerows. Disables the uploadbutton if
     * there aren't, and adds them to the panel if there are. After these
     * updates, it repaints the panel.
     */
    private void repaintSelectedFiles() {
        uploadFilesPanel.add(boldTextLabel);
        if (!uploadFileRows.isEmpty()) {
            for (File f : uploadFileRows.keySet()) {
                uploadFilesPanel.add(uploadFileRows.get(f));
            }
        } else {
            enableUploadButton(false);
        }
        buttonsPanel.add(selectButton);
        // buttonsPanel.add(Box.createHorizontalStrut(20));
        // (orsakar att knapparna flyttar mer och mer åt höger efter varje
        // repaint)
        buttonsPanel.add(uploadSelectedBtn);
        buttonsPanel.add(uploadButton);
        uploadFilesPanel.add(buttonsPanel);
        repaint();
        revalidate();
    }

    /**
     * Deletes an uploadFileRow and calls repaintSelectedFiles() to repaint. If
     * it fails to find the file, an error message is shown to the user.
     *
     * @param f
     *            This is used to identify which uploadFileRow to be deleted.
     */
    public void deleteFileRow(File f) {
        if (uploadFileRows.containsKey(f)) {
            uploadFileRows.remove(f);
            uploadFilesPanel.removeAll();
            buttonsPanel.removeAll();
            repaintSelectedFiles();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Can't delete file: " + f.getName() + "", "File error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method returning the ExpID for a new experiment.
     *
     * @return a String with the ID of a experiment.
     */
    public String getNewExpID() {
        return expID.getText();
    }

    public AnnotationDataValue[] getUploadAnnotations() {
        AnnotationDataValue[] annotations = new AnnotationDataValue[annotationHeaders
                .size()];
        for (int i = 0; i < annotationHeaders.size(); i++) {
            if (annotationBoxes.containsKey(annotationHeaders.get(i))) {
                annotations[i] = new AnnotationDataValue(Integer.toString(i),
                        annotationHeaders.get(i), annotationBoxes
                                .get(annotationHeaders.get(i))
                                .getSelectedItem().toString());
            } else if (annotationFields.containsKey(annotationHeaders.get(i))) {
                annotations[i] = new AnnotationDataValue(Integer.toString(i),
                        annotationHeaders.get(i), annotationFields.get(
                                annotationHeaders.get(i)).getText());
            }
        }
        return annotations;
    }

    /**
     * Method returning the files to be uploaded.
     *
     * @return a array with the files.
     *
     */
    public ArrayList<File> getUploadFiles() {
        ArrayList<File> files = new ArrayList<>();
        for (File f : uploadFileRows.keySet()) {
            files.add(f);
        }
        return files;
    }

    /**
     * Method returning the type of the files to be uploaded.
     *
     * @return a HashMap with the filenames and there types.
     *
     */
    public HashMap<String, String> getTypes() {
        HashMap<String, String> types = new HashMap<String, String>();
        for (File f : uploadFileRows.keySet()) {
            types.put(f.getName(), uploadFileRows.get(f).getType());
        }
        return types;
    }

    /**
     * @return true if all forced annotation fields (including expID) are
     *         filled. Otherwise returns false.
     */
    public boolean forcedAnnotationCheck() {

        String expIDName = expID.getText();
        if (expIDName == null || expIDName.equals("")) {
            return false;
        }

        boolean allForcedAnnotationsAreFilled = true;
        String annotationName;
        String text;
        JTextField annotationField;
        JComboBox<Object> annotationBox;

        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].isForced()) {
                annotationName = annotations[i].getName();
                if (annotationFields.containsKey(annotationName)) {
                    annotationField = annotationFields.get(annotationName);
                    text = annotationField.getText();
                    if (text == null || text.equals("")) {
                        allForcedAnnotationsAreFilled = false;
                    }
                    text = null;
                } else if (annotationBoxes.containsKey(annotationName)) {
                    annotationBox = annotationBoxes.get(annotationName);
                    text = (String) annotationBox.getSelectedItem();
                    if (text == null || text.equals("")) {
                        allForcedAnnotationsAreFilled = false;
                    }

                    text = null;
                }
            }
        }
        return allForcedAnnotationsAreFilled;
    }

    /**
     * Sets the experiment button to either be enabled or disabled. Only enables
     * it if there are selected files and all forced annotations fields are
     * filled.
     *
     * @param b
     *            Whether it should try to: enable the button (true) or disable
     *            it (false)
     */
    public void enableUploadButton(boolean b) {
        if (b) {
            if (!uploadFileRows.isEmpty() && forcedAnnotationCheck()) {
                uploadSelectedBtn.setEnabled(b);
                uploadButton.setEnabled(b);
            }
        } else {
            uploadSelectedBtn.setEnabled(b);
            uploadButton.setEnabled(b);
        }
    }

    /**
     * Method returning the files that are selected.
     *
     * @return an ArrayList with the selected files.
     */
    public ArrayList<File> getSelectedFilesToUpload() {
        ArrayList<File> files = new ArrayList<File>();
        for (File f : uploadFileRows.keySet()) {
            if (uploadFileRows.get(f).isSelected()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
     * Listener for when the text in a textfield changes.
     */
    private class FreetextListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            react();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            react();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            react();
        }

        public void react() {
            enableUploadButton(true);
        }
    }
}