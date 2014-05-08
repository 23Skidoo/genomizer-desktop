package util;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class TreeTable extends JPanel {

    private String[] headings;
    private String[][] data;
    private JXTreeTable table;
    private ExperimentData[] experiments;
    private ArrayList<Boolean> sortingOrders;
    private HashMap<String, FileData[]> experimentToFilesMap;
    private HashMap<String, ExperimentData> fileToExperimentMap;
    private HashMap<String, FileData> fileIdToFileDataMap;
    private int nrOfColumns;

    public TreeTable() {
	this.setLayout(new BorderLayout());
	initiateJXTreeTable();
    }

    public TreeTable(ExperimentData[] experimentData) {
	this.setLayout(new BorderLayout());
	initiateJXTreeTable();
	setContent(experimentData);
    }

    private void initiateJXTreeTable() {
	table = new JXTreeTable();
	table.setShowGrid(true, true);
	table.getTableHeader().setReorderingAllowed(false);
	table.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && experiments != null) {
		    TableColumnModel cModel = table.getColumnModel();
		    int column = cModel.getColumnIndexAtX(e.getX());
		    sortData(column);
		    createTreeStructure();
		    repaint();
		    revalidate();
		}
	    }
	});
	add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setContent(ExperimentData[] experimentData) {
	if (experimentData != null && experimentData.length > 0) {
	    experimentToFilesMap = new HashMap<String, FileData[]>();
	    fileToExperimentMap = new HashMap<String, ExperimentData>();
	    fileIdToFileDataMap = new HashMap<String, FileData>();
	    experiments = experimentData;
	    /* Set new content to the tree table */
	    // headings
	    nrOfColumns = 2 + experimentData[0].annotations.length;
	    headings = new String[nrOfColumns];
	    headings[0] = "<html><b>Experiment Name</html></b>";
	    headings[1] = "<html><b>Experiment Created By</html></b>";
	    for (int i = 0; i < nrOfColumns - 2; i++) {
		headings[2 + i] = "<html><b>"
			+ experiments[0].annotations[i].name + "</html></b>";
	    }
	    /* Create the data matrix from the experiment data */
	    data = new String[experiments.length][headings.length];
	    for (int i = 0; i < experiments.length; i++) {
		experimentToFilesMap.put(experiments[i].name,
			experiments[i].files);
		for (int j = 0; j < experiments[i].files.length; j++) {
		    fileIdToFileDataMap.put(experiments[i].files[j].id,
			    experiments[i].files[j]);
		    fileToExperimentMap.put(experiments[i].files[j].id,
			    experiments[i]);
		}
		data[i] = experiments[i].getAnnotationValueList();
	    }
	    /* Initate the sorting orders as descending */
	    sortingOrders = new ArrayList<Boolean>();
	    for (int i = 0; i < this.headings.length; i++) {
		sortingOrders.add(i, true);
	    }
	    createTreeStructure();
	}
    }

    private void sortData(final int sortByColumn) {
	// data = getDataFromTreeTable();
	// headings = getHeadingsFromTreeTable();
	/* update the sorting orders for the columns */
	for (int i = 0; i < sortingOrders.size(); i++) {
	    if (i == sortByColumn) {
		sortingOrders.set(i, !sortingOrders.get(i));
	    } else {
		sortingOrders.set(i, true);
	    }
	}
	/* Sorting method, can sort strings with numbers correctly */
	Arrays.sort(data, new Comparator<String[]>() {
	    private final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

	    @Override
	    public int compare(final String[] entry1, final String[] entry2) {
		Matcher m1 = PATTERN.matcher(entry1[sortByColumn]);
		Matcher m2 = PATTERN.matcher(entry2[sortByColumn]);
		/* The only way find() could fail is at the end of a string */
		while (m1.find() && m2.find()) {
		    /*
		     * matcher.group(1) fetches any non-digits captured by the
		     * first parentheses in PATTERN.
		     */
		    int nonDigitCompare;
		    if (sortingOrders.get(sortByColumn)) {
			nonDigitCompare = m2.group(1).compareTo(m1.group(1));
		    } else {
			nonDigitCompare = m1.group(1).compareTo(m2.group(1));
		    }
		    if (0 != nonDigitCompare) {
			return nonDigitCompare;
		    }

		    /*
		     * matcher.group(2) fetches any digits captured by the
		     * second parentheses in PATTERN.
		     */
		    if (m1.group(2).isEmpty()) {
			return m2.group(2).isEmpty() ? 0 : -1;
		    } else if (m2.group(2).isEmpty()) {
			return +1;
		    }

		    BigInteger n1 = new BigInteger(m1.group(2));
		    BigInteger n2 = new BigInteger(m2.group(2));
		    int numberCompare;
		    if (sortingOrders.get(sortByColumn)) {
			numberCompare = n2.compareTo(n1);
		    } else {
			numberCompare = n1.compareTo(n2);
		    }
		    if (0 != numberCompare) {
			return numberCompare;
		    }
		}

		/*
		 * Handle if one string is a prefix of the other. Nothing comes
		 * before something.
		 */
		return m1.hitEnd() && m2.hitEnd() ? 0 : m1.hitEnd() ? -1 : +1;
	    }
	});
    }

    public ExperimentData[] getContent() {
	return experiments;
    }

    public ArrayList<FileData> getSelectedFiles() {
	/* Get the files that are selected in the table */
	int[] rows = table.getSelectedRows();
	ArrayList<FileData> files = new ArrayList<FileData>();
	for (int i = 0; i < rows.length; i++) {
	    if (fileToExperimentMap.containsKey(table.getValueAt(rows[i], 0))) {
		FileData file = fileIdToFileDataMap.get(table.getValueAt(
			rows[i], 0));
		files.add(file);
	    }

	}
	return files;
    }

    public ArrayList<ExperimentData> getSelectedFilesWithExperiments() {
	return null;
    }

    private void createTreeStructure() {
	/* Create the tree root */
	Node root = new Node(new Object[] { "Root" });

	for (int i = 0; i < experiments.length; i++) {
	    /* Create experiment node and add to root */
	    Node child = new Node(data[i]);
	    root.add(child);
	    /* Create raw files node */
	    Node rawFiles = new Node(new String[] { "Raw Files" });
	    /* Create profile files node */
	    Node profileFiles = new Node(new String[] { "Profile Files" });
	    /* Create region files node */
	    Node regionFiles = new Node(new String[] { "Region Files" });
	    String[] fileHeaders = new String[] {
		    "<html><b>File ID</html></b>",
		    "<html><b>File Name</html></b>",
		    "<html><b>File Size</html></b>",
		    "<html><b>Date Added</html></b>",
		    "<html><b>Uploaded By</html></b>",
		    "<html><b>URL</html></b>" };
	    rawFiles.add(new Node(fileHeaders));
	    profileFiles.add(new Node(fileHeaders));
	    regionFiles.add(new Node(fileHeaders));
	    // ExperimentData currentExperiment =
	    // getExperimentFromData(data[i]);
	    FileData[] fileData = experimentToFilesMap.get(data[i][0]);
	    /*
	     * Loop through all files in the current experiment and create nodes
	     * for them
	     */
	    for (int j = 0; j < fileData.length; j++) {
		FileData currentFile = fileData[j];
		Object[] rowContent = { currentFile.id, currentFile.name,
			currentFile.size, currentFile.date,
			currentFile.uploadedBy, currentFile.URL };
		if (currentFile.type.equals("raw")) {
		    rawFiles.add(new Node(rowContent));
		} else if (currentFile.type.equals("region")) {
		    regionFiles.add(new Node(rowContent));
		} else if (currentFile.type.equals("profile")) {
		    profileFiles.add(new Node(rowContent));
		}
	    }
	    /* add the files nodes */
	    if (rawFiles.getChildCount() > 1) {
		child.add(rawFiles);
	    }
	    if (regionFiles.getChildCount() > 1) {
		child.add(regionFiles);
	    }
	    if (profileFiles.getChildCount() > 1) {
		child.add(profileFiles);
	    }
	}
	/* Create the model and add it to the table */
	DefaultTreeTableModel model = new DefaultTreeTableModel(root,
		Arrays.asList(headings));
	table.setTreeTableModel(model);
	table.packAll();
    }
}