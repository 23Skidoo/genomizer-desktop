package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import util.ActivePanel;
import util.AnnotationDataType;
import util.ExperimentData;
import util.GenomeReleaseData;

import communication.HTTPURLUpload;

/**
 * A class representing a upload view in an application for genome research.
 * This class allows the user to upload files to the database of the
 * application.
 */
public class UploadTab extends JPanel {

    private static final long serialVersionUID = -2830290705724588252L;
    private JButton existingExpButton, newExpButton;
    private JPanel northPanel, expNamePanel, uploadPanel;
    private UploadToExistingExpPanel uploadToExistingExpPanel;
    private CopyOnWriteArrayList<HTTPURLUpload> ongoingUploads;
    private ActivePanel activePanel;
    private JLabel boldTextLabel;
    private JTextField experimentNameField;
    private JScrollPane uploadScroll;

    // Test purpose
    private ArrayList<UploadToNewExpPanel> newExps = new ArrayList<UploadToNewExpPanel>();
    private UploadToNewExpPanel uploadToNewExpPanel;

    /**
     * Constructor creating a upload tab.
     */
    public UploadTab() {
        activePanel = ActivePanel.NONE;
        setLayout(new BorderLayout());
        uploadToExistingExpPanel = new UploadToExistingExpPanel();
        uploadToNewExpPanel = new UploadToNewExpPanel();
        northPanel = new JPanel();
        expNamePanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);
        northPanel.add(new JLabel("Experiment name: "));
        experimentNameField = new JTextField();
        experimentNameField.setColumns(30);
        expNamePanel.add(experimentNameField);
        northPanel.add(expNamePanel);
        northPanel.setBorder(BorderFactory.createTitledBorder("Upload"));
        existingExpButton = new JButton("Search for existing experiment");
        newExpButton = new JButton("Create new experiment");
        experimentNameField.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent actionEvent) {
                existingExpButton.doClick();
            }
        });
        northPanel.add(existingExpButton, BorderLayout.EAST);
        northPanel.add(newExpButton, BorderLayout.EAST);
        uploadPanel = new JPanel(new BorderLayout());
        uploadScroll = new JScrollPane(uploadPanel);
        add(uploadScroll, BorderLayout.CENTER);
        boldTextLabel = new JLabel(
                "<html><b>Bold text indicates a forced annotation.</b></html>");
        boldTextLabel.setOpaque(true);
        updateProgress();
    }

    /**
     * Method adding a listener to the "addToExistingExpButton".
     *
     * @param listener
     *            The listener to add file to existing experiment.
     */
    public void addAddToExistingExpButtonListener(ActionListener listener) {
        existingExpButton.addActionListener(listener);
    }

    /**
     * Method adding a listener to the "newExpButton".
     *
     * @param listener
     *            The listener to create a experiment.
     */
    public void addNewExpButtonListener(ActionListener listener) {
        newExpButton.addActionListener(listener);
    }

    /**
     * Displays a panel for adding to an existing experiment.
     *
     * @param ed
     *            The experiment data for the existing experiment.
     */
    public void addExistingExpPanel(ExperimentData ed) {
        killContentsOfUploadPanel();
        activePanel = ActivePanel.EXISTING;
        uploadToExistingExpPanel.build();
        uploadToExistingExpPanel.addExistingExp(ed);
        uploadPanel.add(uploadToExistingExpPanel, BorderLayout.CENTER);
        repaint();
        revalidate();
    }

    /**
     * Displays a panel for creating a new experiment.
     *
     * @param annotations
     *            The available annotations at the server.
     *
     */
    public void addNewExpPanel(AnnotationDataType[] annotations) {
        killContentsOfUploadPanel();
        activePanel = ActivePanel.NEW;
        uploadToNewExpPanel.createNewExpPanel(annotations);
        uploadPanel.add(uploadToNewExpPanel, BorderLayout.CENTER);
        newExps.add(uploadToNewExpPanel);
        repaint();
        revalidate();
    }

    /**
     * Method returning a uploadToExistingExpPanel.
     *
     * @return a panel used when uploading file to a existing experiment.
     */
    public UploadToExistingExpPanel getExistExpPanel() {
        return uploadToExistingExpPanel;
    }

    public UploadToNewExpPanel getNewExpPanel() {
        return uploadToNewExpPanel;
    }

    public boolean newExpStarted() {
        return activePanel == ActivePanel.NEW;
    }

    /**
     * Method returning the text in the experiment name field.
     *
     * @return a String with the experiment name.
     */
    public String getSearchText() {
        return experimentNameField.getText();
    }

    public JButton getExistingExpButton() {
        return existingExpButton;
    }

    public JTextField getExperimentNameField() {
        return experimentNameField;
    }

    public String getGenomeVersion(File f) {
        if (activePanel == ActivePanel.EXISTING) {
            return uploadToExistingExpPanel.getGenomeVersion(f);
        } else {
            return uploadToNewExpPanel.getGenomeVersion(f);
        }
    }

    public void setGenomeReleases(GenomeReleaseData[] grd) {
        if (activePanel == ActivePanel.EXISTING) {
            uploadToExistingExpPanel.setGenomeReleases(grd);
        } else {
            uploadToNewExpPanel.setGenomeReleases(grd);
        }
    }

    /**
     * A method removing the components in the panels when one of them gets
     * chosen by the user, to make sure the new components won't overlap and end
     * up invisible. The method checks the Enum ActivePanel to check which panel
     * was the active one.
     */
    public void killContentsOfUploadPanel() {
        switch (activePanel) {
            case NONE:
                break;
            case EXISTING:
                uploadPanel.remove(uploadToExistingExpPanel);
                uploadToExistingExpPanel.removeAll();
                repaint();
                revalidate();
                activePanel = ActivePanel.NONE;
                break;
            case NEW:
                uploadPanel.remove(uploadToNewExpPanel);
                uploadToNewExpPanel.removeAll();
                repaint();
                revalidate();
                activePanel = ActivePanel.NONE;
                break;
        }
    }

    /**
     * Method setting the ongoing uploads.
     *
     * @param ongoingUploads
     *            The uploads currently ongoing.
     */
    public void setOngoingUploads(
            CopyOnWriteArrayList<HTTPURLUpload> ongoingUploads) {
        this.ongoingUploads = ongoingUploads;
    }

    /**
     * Method updating the progress of ongoing uploads.
     */
    private void updateProgress() {
        new Thread(new Runnable() {
            private boolean running;

            @Override
            public void run() {
                running = true;
                while (running) {
                    for (File key : uploadToNewExpPanel.getFileRows().keySet()) {
                        UploadFileRow row = uploadToNewExpPanel.getFileRows()
                                .get(key);
                        for (HTTPURLUpload upload : ongoingUploads) {
                            if (upload.getFileName().equals(row.getFileName())) {
                                row.updateProgressBar(upload
                                        .getCurrentProgress());
                            }
                        }
                    }
                    for (File key : uploadToExistingExpPanel.getFileRows()
                            .keySet()) {
                        UploadFileRow row = uploadToExistingExpPanel
                                .getFileRows().get(key);
                        for (HTTPURLUpload upload : ongoingUploads) {
                            if (upload.getFileName().equals(row.getFileName())) {
                                row.updateProgressBar(upload
                                        .getCurrentProgress());
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        running = false;
                    }
                }
            }
        }).start();
    }
}
