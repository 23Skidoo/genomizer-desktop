package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import util.ActiveSearchPanel;
import util.AnnotationDataType;
import util.ExperimentData;
import util.IconFactory;
import util.TreeTable;

/**
 * Class representing the search tab of the gui
 */
public class QuerySearchTab extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -5171748087481537247L;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel rowsPanel;
    private JPanel searchPanel;
    private JPanel resultsHeaderPanel;
    private JButton backButton;
    private JButton updateAnnotationsButton;
    private JButton addToWorkspaceButton;
    private JButton searchButton;
    private JButton downloadButton;
    private JTextArea searchArea;
    private CopyOnWriteArrayList<QueryBuilderRow> rowList;
    private TreeTable resultsTable;
    private AnnotationDataType[] annotationTypes;
    private ActiveSearchPanel activePanel;
    
    /**
     * Create a query search tab
     */
    public QuerySearchTab() {
        setUpQuerySearchTab();
        setUpSearchHeader();
        setUpRowsPanel();
        setUpResultsTable();
        setUpResultsHeaderPanel();
        showSearchView();
        clearSearchFields();
        activePanel = ActiveSearchPanel.SEARCH;
    }
    
    /**
     * Show the search view of the tab
     */
    private void showSearchView() {
        removeAll();
        JScrollPane bottomScroll = new JScrollPane(bottomPanel);
        bottomScroll.setBorder(BorderFactory.createEmptyBorder());
        add(topPanel, BorderLayout.NORTH);
        add(bottomScroll, BorderLayout.CENTER);
        topPanel.removeAll();
        bottomPanel.removeAll();
        topPanel.add(searchPanel);
        bottomPanel.add(rowsPanel, BorderLayout.NORTH);
        activePanel = ActiveSearchPanel.SEARCH;
        repaint();
        revalidate();
    }
    
    /**
     * Show the results view of the tab
     */
    private void showResultsView() {
        removeAll();
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
        topPanel.removeAll();
        bottomPanel.removeAll();
        topPanel.add(resultsHeaderPanel);
        bottomPanel.add(resultsTable, BorderLayout.CENTER);
        activePanel = ActiveSearchPanel.TABLE;
        repaint();
        revalidate();
    }
    
    /**
     * Set up the query search tab foundation
     */
    private void setUpQuerySearchTab() {
        updateAnnotationsButton = new JButton();
        annotationTypes = new AnnotationDataType[0];
        rowList = new CopyOnWriteArrayList<>();
        this.setLayout(new BorderLayout());
        bottomPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory
                .createTitledBorder("Genomizer Advanced Search Builder"));
    }
    
    /**
     * Set up the results tree table
     */
    private void setUpResultsTable() {
        resultsTable = new TreeTable();
    }
    
    /**
     * Set up the search view header
     */
    private void setUpSearchHeader() {
        searchPanel = new JPanel(new FlowLayout());
        
        // searchButton = new JButton("Search");
        searchButton = CustomButtonFactory.makeCustomButton(
                IconFactory.getSearchIcon(30, 30),
                IconFactory.getSearchIcon(32, 32), 32, 32, "Search for data");
        
        JButton clearButton = CustomButtonFactory
                .makeCustomButton(IconFactory.getClearIcon(30, 30),
                        IconFactory.getClearIcon(32, 32), 32, 32,
                        "Clear search fields");
        // JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                clearSearchFields();
            }
        });
        searchArea = new JTextArea("");
        searchArea.setLineWrap(true);
        searchArea.setSize(850, 20);
        searchArea.setEditable(false);
        
        /*
         * Click the search button if the enter key is pressed while the text
         * field has focus. (only if it's not editable)
         */
        searchArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == event.VK_ENTER
                        && !searchArea.isEditable()) {
                    searchButton.doClick();
                }
            }
        });
        
        JScrollPane searchScroll = new JScrollPane(searchArea);
        searchScroll.setPreferredSize(new Dimension(800, 35));
        JRadioButton queryBuilderButton = new JRadioButton("Query Builder");
        JRadioButton manualEditButton = new JRadioButton("Manual edit");
        ButtonGroup buttonGroup = new ButtonGroup();
        manualEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchArea.setEditable(true);
                
                for (QueryBuilderRow row : rowList) {
                    row.setEnabled(false);
                }
            }
        });
        queryBuilderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchArea.setEditable(false);
                
                for (QueryBuilderRow row : rowList) {
                    row.setEnabled(true);
                }
            }
        });
        buttonGroup.add(queryBuilderButton);
        buttonGroup.add(manualEditButton);
        buttonGroup.setSelected(queryBuilderButton.getModel(), true);
        searchPanel.add(queryBuilderButton);
        searchPanel.add(manualEditButton);
        searchPanel.add(searchScroll);
        searchPanel.add(searchButton);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(clearButton);
        searchPanel.add(Box.createHorizontalStrut(50));
    }
    
    /**
     * Set up the results view header
     */
    private void setUpResultsHeaderPanel() {
        resultsHeaderPanel = new JPanel(new BorderLayout());
        backButton = CustomButtonFactory.makeCustomButton(
                IconFactory.getBackIcon(25, 25),
                IconFactory.getBackIcon(27, 27), 25, 25, "Back to search view");
        // backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchView();
            }
        });
        
        // addToWorkspaceButton = CustomButtonFactory.makeCustomButton(
        // IconFactory.getAddToWorkspaceIcon(50, 34),
        // IconFactory.getAddToWorkspaceHoverIcon(52, 36), 52, 36,
        // "Add selected to workspace");
        addToWorkspaceButton = new JButton("Add to workspace");
        downloadButton = new JButton("Download Selected Files");
        // resultsHeaderPanel.add(downloadButton, BorderLayout.EAST);
        resultsHeaderPanel.add(addToWorkspaceButton, BorderLayout.EAST);
        resultsHeaderPanel.add(backButton, BorderLayout.WEST);
    }
    
    public void refresh() {
        if (activePanel == ActiveSearchPanel.TABLE) {
            searchButton.doClick();
        }
    }
    
    /**
     * Set up the rows panel (containing query builder rows
     */
    private void setUpRowsPanel() {
        rowsPanel = new JPanel(new GridLayout(0, 1));
    }
    
    /**
     * Update the search results and switch to results view
     * 
     * @param searchResults
     */
    public void updateSearchResults(
            final ArrayList<ExperimentData> searchResults) {
        // updateAnnotationsButton.doClick();
        // updateRows();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                paintRows();
                resultsTable = new TreeTable(searchResults);
                showResultsView();
            }
        });
        
    }
    
    /**
     * Clear the search fields of the tab (including all query builder rows and
     * the search text area
     */
    public synchronized void clearSearchFields() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateAnnotationsButton.doClick();
                rowList = new CopyOnWriteArrayList<QueryBuilderRow>();
                addRow();
                searchArea.setText("");
            }
        });
    }
    
    /**
     * Add a new row to the query builder
     */
    public void addRow() {
        rowList.add(new QueryBuilderRow(this, annotationTypes));
        paintRows();
    }
    
    /**
     * Remove a row from the query builder
     * 
     * @param row
     */
    public void removeRow(QueryBuilderRow row) {
        if (rowList.contains(row)) {
            rowList.remove(row);
        }
        paintRows();
    }
    
    /**
     * Paint the query builder rows in the rows panel
     */
    private synchronized void paintRows() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                rowsPanel.removeAll();
                
                for (int i = 0; i < rowList.size(); i++) {
                    QueryBuilderRow row = rowList.get(i);
                    if (i == 0 && i == (rowList.size() - 1)) {
                        row.setAs(true, true);
                    } else if (i == 0 && i != (rowList.size() - 1)) {
                        row.setAs(true, false);
                    } else if (i != 0 && i == (rowList.size() - 1)) {
                        row.setAs(false, true);
                    } else {
                        row.setAs(false, false);
                    }
                    rowsPanel.add(row);
                }
                updateSearchArea();
                rowsPanel.revalidate();
                rowsPanel.repaint();
            }
        });
        
    }
    
    /**
     * Update all query builder rows with annotation information
     */
    private void updateRows() {
        for (int i = 0; i < rowList.size(); i++) {
            rowList.get(i).setAnnotationBox(annotationTypes);
        }
    }
    
    public synchronized void updateSearchArea() {
        String searchString = "";
        int i = 0;
        for (QueryBuilderRow row : rowList) {
            if (!row.getText().isEmpty()) {
                String logic;
                String endParenthesis = "";
                if (i == 0) {
                    logic = "";
                } else {
                    logic = row.getLogic() + " ";
                    searchString = "(" + searchString;
                    endParenthesis = ") ";
                }
                String text = row.getText();
                String annotation = row.getAnnotation();
                searchString = searchString + endParenthesis + logic + text
                        + "[" + annotation + "]";
                i++;
            }
        }
        if (searchString.isEmpty()) {
            searchArea.setText("");
        } else {
            searchArea.setText(searchString);
        }
    }
    
    public void addSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    public void addAddToWorkspaceButtonListener(ActionListener listener) {
        addToWorkspaceButton.addActionListener(listener);
    }
    
    public void addDownloadButtonListener(ActionListener listener) {
        downloadButton.addActionListener(listener);
    }
    
    public void addUpdateAnnotationsListener(ActionListener listener) {
        updateAnnotationsButton.addActionListener(listener);
    }
    
    public void clickUpdateAnnotations() {
        updateAnnotationsButton.doClick();
    }
    
    public void setAnnotationTypes(AnnotationDataType[] annotationTypes) {
        this.annotationTypes = annotationTypes;
        updateRows();
        paintRows();
    }
    
    public ArrayList<ExperimentData> getSelectedData() {
        return resultsTable.getSelectedData();
        
    }
    
    public String getSearchString() {
        return searchArea.getText();
    }
    
    public ActiveSearchPanel getActivePanel() {
        return activePanel;
    }
    
    public JButton getBackButton() {
        return backButton;
    }
    
    public JButton getSearchButton() {
        return searchButton;
    }
    
    public void clearSearchSelection() {
        resultsTable.deselectTreeTable();
    }
    
    public CopyOnWriteArrayList<QueryBuilderRow> getRowList() {
        return rowList;
        
    }
    
    /**
     * Get the QueryBuilderRow's next position in QueryBuilderRow list.
     */
    public int getNextQueryPosition(QueryBuilderRow queryRow) {
        
        int thisIndex = rowList.indexOf(queryRow);
        int nextIndex = thisIndex + 1;
        
        return nextIndex;
    }
    
    protected QueryBuilderRow getNextQuery(QueryBuilderRow queryRow) {
        
        int next = getNextQueryPosition(queryRow);
        
        return rowList.get(next);
    }
    
    public boolean isLastQueryIndex(QueryBuilderRow queryRow) {
        
        int size = rowList.size();
        
        return (rowList.indexOf(queryRow) == (size - 1));
    }
    
}
