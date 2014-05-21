package gui.sysadmin.annotationview;

import gui.sysadmin.strings.SysStrings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import util.AnnotationDataType;

public class EditAnnotationPopup2 extends JPanel {
    
    private JTable table;
    private AnnotationDataType annotation;
    private JButton activateNameChangeButton = new JButton(
            SysStrings.ANNOTATIONS_RENAME_FINAL);;
    private JButton renameButton;
    private JTextField nameField;
    private ArrayList<JButton> valueButtons = new ArrayList<JButton>();
    private ArrayList<JTextField> valueFields = new ArrayList<JTextField>();
    private JPanel centerpanel;
    
    public EditAnnotationPopup2(JTable table) {
        this.table = table;
        if (!setAnnotation()) {
            JOptionPane.showMessageDialog(null,
                    "Please select an annotation to edit");
            this.setEnabled(false);
            this.setLayout(new BorderLayout());
        } else {
            createAnnotationNamePanel();
            createValuesPanel();
            createForcedPanel();
        }
    }
    
    private void createForcedPanel() {
        
    }
    
    private void createValuesPanel() {
        
        BoxLayout layout;
        centerpanel = new JPanel();
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        layout = (BoxLayout) centerpanel.getLayout();
        for (String annotationValue : annotation.getValues()) {
            JPanel panel = createAnnotationValue(annotationValue);
            // layout.
            
            centerpanel.add(panel);
        }
        JPanel addValuePanel = createAddValuePanel();
        centerpanel.add(addValuePanel);
        JPanel scrollPanel = new JPanel();
        scrollPanel.add(centerpanel);
        JScrollPane scrollpane = new JScrollPane(scrollPanel);
        //scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(centerpanel, BorderLayout.CENTER);
    }
    
    private JPanel createAddValuePanel() {
        JPanel panel = new JPanel();
        JButton addValueButton = new JButton(
                SysStrings.ANNOTATIONS_MODIFY_ADD_VALUE);
        addValueButton.setName(SysStrings.ANNOTATIONS_MODIFY_ADD_VALUE);
        JTextField valueTextField = new JTextField("");
        valueTextField.setName("addedTextField");
        valueTextField.setPreferredSize(new Dimension(180, 30));
        panel.add(valueTextField, 0);
        panel.add(addValueButton);
        valueButtons.add(addValueButton);
        return panel;
    }
    
    private JPanel createAnnotationValue(String name) {
        JPanel panel = new JPanel();
        JTextField valueTextField = new JTextField(name);
        valueTextField.setName(name);
        valueTextField.setPreferredSize(new Dimension(180, 30));
        panel.add(valueTextField, 0);
        JButton modifyNameButton = new JButton(
                SysStrings.ANNOTATIONS_MODIFY_RENAME);
        JButton removeButton = new JButton(SysStrings.ANNOTATIONS_MODIFY_REMOVE);
        
        valueButtons.add(removeButton);
        valueButtons.add(modifyNameButton);
        valueFields.add(valueTextField);
        
        panel.add(modifyNameButton);
        panel.add(removeButton);
        return panel;
    }
    
    private void createAnnotationNamePanel() {
        JPanel annotationNamePanel = new JPanel();
        
        JLabel name = new JLabel("Name: ");
        annotationNamePanel.add(name);
        
        nameField = new JTextField(annotation.name);
        nameField.setPreferredSize(new Dimension(200, 30));
        annotationNamePanel.add(nameField);
        renameButton = new JButton(SysStrings.ANNOTATIONS_RENAME);
        renameButton.setMinimumSize(new Dimension(80, 10));
        annotationNamePanel.add(renameButton);
        
        // JButton forced = new JButton("set Required");
        // forced.setMinimumSize(new Dimension(80, 10));
        // annotationNamePanel.add(forced);
        this.add(annotationNamePanel, BorderLayout.NORTH);
    }
    
    /**
     * Sets the local value for the annotation variable
     */
    private boolean setAnnotation() {
        if (table.getSelectedRow() != -1) {
            int row = table.getSelectedRow();
            row = table.convertRowIndexToModel(row);
            int col = 3;
            annotation = (AnnotationDataType) table.getModel().getValueAt(row,
                    col);
            return true;
        } else {
            System.out.println("You must select an annotation to edit");
            return false;
        }
    }
    
    public AnnotationDataType getAnnotation() {
        return annotation;
    }
    
    public String getNewAnnotationName() {
        return nameField.getText();
    }
    
    public Boolean getNewAnnotationForcedValue() {
        return annotation.isForced();
    }
    
    public String[] getNewAnnotationCategories() {
        return annotation.getValues();
    }
    
    public void buildRenameAnnotationPanel() {
        JPanel renameAnnotationPanel = new JPanel();
        nameField = new JTextField();
        nameField.setText(annotation.name);
        nameField.setPreferredSize(new Dimension(200, 30));
        
        renameAnnotationPanel.add(nameField);
        renameAnnotationPanel.add(activateNameChangeButton);
        
        this.add(renameAnnotationPanel);
        revalidate();
        validate();
    }
    
    public void addEditAnnotationListener(ActionListener listener) {
        renameButton.addActionListener(listener);
        activateNameChangeButton.addActionListener(listener);
        
        for (JButton button : valueButtons) {
            button.addActionListener(listener);
        }
        
        for (JTextField field : valueFields) {
            field.getDocument().addDocumentListener(new DocumentListener() {
                
                @Override
                public void changedUpdate(DocumentEvent ev) {
                    
                }
                
                @Override
                public void insertUpdate(DocumentEvent ev) {
                    
                    try {
                        String newString = ev.getDocument().getText(0,
                                ev.getDocument().getLength());
                        
                        if (valueRenameIsValid(newString)) {
                            System.out.println("Name change is valid!");
                        }
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    
                }
                
                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    System.out.println("Removed something");
                    
                }
                
            });
        }
    }
    
    public void updateAnnotation(String name) {
        centerpanel.remove(centerpanel.getComponents().length - 1);
        centerpanel.add(createAnnotationValue(name));
        centerpanel.add(createAddValuePanel());
        centerpanel.updateUI();
        updateUI();
        this.repaint();
        
    }
    
    private boolean valueRenameIsValid(String newName) {
        if (annotation.name.equalsIgnoreCase(newName)) {
            return false;
        } else
            return false;
        
    }
    
}
