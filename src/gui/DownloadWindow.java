package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DownloadWindow extends JFrame {

    private static final long serialVersionUID = -7647204230941649167L;
    private JPanel panel;
    private JTable table;
    private JButton downloadButton;
    private ImageIcon downloadIcon = new ImageIcon(
            "src/icons/DownloadButton.png");

    // Tar in ArrayList med de filer som valdes
    public DownloadWindow(ArrayList<String> files) {

        setUp(files);
    }

    public DownloadWindow() {
        ArrayList<String> data = new ArrayList<String>();

        data.add("Protein123_A5_2014.WIG");
        data.add("Protein123_A5_2014.RAW");
        data.add("Protein231_A5_2014.WIG");
        data.add("Protein211_A5_2014.WIG");
        data.add("Protein223_A5_2014.RAW");
        data.add("Protein112_A5_2014.WIG");

        setUp(data);
    }

    private void setUp(ArrayList<String> data) {

        panel = new JPanel(new BorderLayout(3, 3));
        add(panel);
        panel.add(new JLabel("test"), BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        TableColumn fileNameColumn, formatConversionColumn;
        fileNameColumn = new TableColumn();
        formatConversionColumn = new TableColumn();

        tableModel.addColumn(fileNameColumn);
        tableModel.addColumn(formatConversionColumn);

        fileNameColumn = table.getTableHeader().getColumnModel().getColumn(0);
        formatConversionColumn = table.getTableHeader().getColumnModel()
                .getColumn(1);

        fileNameColumn.setHeaderValue("File name");
        formatConversionColumn.setHeaderValue("Format conversion");

        for (int i = 0; i < data.size(); i++) {
            tableModel.addRow(new Object[]{data.get(i), "Click here to choose"});
        }

        JComboBox comboBox = new JComboBox();
        comboBox.addItem("RAW");
        comboBox.addItem("WIG");
        DefaultCellEditor cellEditor = new DefaultCellEditor(comboBox);
        formatConversionColumn.setCellEditor(cellEditor);

        table.setBackground(Color.cyan);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(table.getTableHeader(), BorderLayout.NORTH);

        downloadButton = new JButton();

        downloadButton.setBorderPainted(true);
        downloadButton.setContentAreaFilled(false);
        downloadButton.setIcon(downloadIcon);

        JPanel flowSouth = new JPanel();
        flowSouth.add(downloadButton);
        panel.add(flowSouth, BorderLayout.SOUTH);

        setTitle("DOWNLOAD FILES");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addDownloadFileListener(ActionListener listener) {
        downloadButton.addActionListener(listener);
    }
}
