package gui;

import gui.sysadmin.SysadminController;
import gui.sysadmin.SysadminTab;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import util.*;

public class GUI extends JFrame implements GenomizerView {

    private static final long serialVersionUID = 6659839768426124853L;
    private JPanel mainPanel;
    private JPanel processPanel;
    private JTabbedPane tabbedPane;
    private SearchTab searchTab;
    // private LoginPanel loginPanel;
    private UserPanel userPanel;
    private UploadTab uploadTab;
    private AnalyzeTab analyzeTab;
    private WorkspaceTab workspaceTab;
    private LoginWindow loginWindow;
    private ProcessTab processTab;
    private SysadminTab sysadminTab;
    private QuerySearchTab querySearchTab;
    private DownloadWindow downloadWindow;

    public GUI() {

	setLookAndFeel();
	this.setTitle("Genomizer");
	setSize(800, 800);

	BorderLayout bl = new BorderLayout();
	mainPanel = new JPanel(bl);
	userPanel = new UserPanel();
	loginWindow = new LoginWindow(this);

	add(mainPanel);

	tabbedPane = new JTabbedPane();
	mainPanel.add(tabbedPane);
	mainPanel.add(tabbedPane);

	// mainPanel.add(new
	// UserPanel("kallekarlsson123",true),BorderLayout.NORTH);
	mainPanel.add(userPanel, BorderLayout.NORTH);

	setLookAndFeel();
	// tabbedPane.add("LOGIN", userPanel);
	setSize(1200, 1200);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLocationRelativeTo(null);
    }

    @Override
    public void addLoginListener(ActionListener listener) {
	loginWindow.addLoginListener(listener);
    }

    public void addUpdateSearchAnnotationsListener(ActionListener listener) {
	querySearchTab.addUpdateAnnotationsListener(listener);
    }

    @Override
    public void addProcessFileListener(ActionListener listener) {
	workspaceTab.addProcessFileListener(listener);
    }

    @Override
    public void addConvertFileListener(ActionListener listener) {
	processTab.addConvertFileListener(listener);
    }

    @Override
    public void addRawToProfileDataListener(ActionListener listener) {
	processTab.addRawToProfileDataListener(listener);
    }

    @Override
    public void addRawToRegionDataListener(ActionListener listener) {
	processTab.addRawToRegionDataListener(listener);
    }

    @Override
    public void addScheduleFileListener(ActionListener listener) {
	processTab.addScheduleFileListener(listener);
    }

    public void addSearchToWorkspaceListener(ActionListener listener) {
	querySearchTab.addAddToWorkspaceButtonListener(listener);
    }

    @Override
    public void addLogoutListener(ActionListener listener) {

	userPanel.addLogoutButtonListener(listener);
    }

    @Override
    public void addSearchListener(ActionListener listener) {
	// TODO Auto-generated method stub
    }

    @Override
    public void addQuerySearchListener(ActionListener listener) {
	querySearchTab.addSearchButtonListener(listener);
    }

    @Override
    public void addDownloadFileListener(ActionListener listener) {
	workspaceTab.addDownloadFileListener(listener);
    }

    @Override
    public void addAddToExistingExpButtonListener(ActionListener listener) {
	uploadTab.addAddToExistingExpButtonListener(listener);
    }

    public void addSelectFilesToUploadButtonListener(ActionListener listener) {
	uploadTab.getUploadToExistingExpPanel()
		.addSelectFilesToUploadButtonListener(listener);
    }

    @Override
    public void addUploadToExperimentButtonListener(ActionListener listener) {
	uploadTab.getUploadToExistingExpPanel()
		.addUploadToExperimentButtonListener(listener);
    }

    @Override
    public void addSearchResultsDownloadListener(ActionListener listener) {
	querySearchTab.addDownloadButtonListener(listener);
    }

    public void addToWorkspace(ExperimentData[] experiments) {
	workspaceTab.addExperimentsToTable(experiments);
    }

    @Override
    public FileData[] getSelectedFilesInSearch() {
	return querySearchTab.getSelectedFiles();
    }

    public ExperimentData[] getSelectedFilesWithExpsInSearch() {
	return querySearchTab.getSelectedFilesWithExperiments();
    }

    public ExperimentData[] getSelectedExperimentsInSearch() {
	return querySearchTab.getSelectedExperiments();
    }

    public UploadTab getUploadTab() {
	return uploadTab;
    }

    @Override
    public String getQuerySearchString() {
	return querySearchTab.getSearchString();
    }

    @Override
    public ArrayList<FileData> getAllMarkedFileData() {
	return processTab.getAllMarkedFileData();
    }

    @Override
    public ArrayList<String> getAllMarkedFiles() {

	return processTab.getAllMarkedFiles();

    }

    public JPanel getSearchPanel() {
	return searchTab;
    }

    @Override
    public String getPassword() {
	return loginWindow.getPasswordInput();
    }

    @Override
    public String getUsername() {
	return loginWindow.getUsernameInput();
    }

    @Override
    public String getIp() {
	return loginWindow.getIPInput();
    }

    @Override
    public JFrame getFrame() {
	return this;
    }

    @Override
    public void setDownloadWindow(DownloadWindow downloadWindow) {
        this.downloadWindow = downloadWindow;
    }

    @Override
    public void updateLoginAccepted(String username, String pwd, String name) {
	userPanel.setUserInfo(username, name, false);
	refreshGUI();
	this.setVisible(true);
	loginWindow.removeErrorMessage();
	loginWindow.setVisible(false);
	querySearchTab.clickUpdateAnnotations();
    }

    @Override
    public void updateLoginNeglected(String errorMessage) {
	loginWindow.updateLoginFailed(errorMessage);

    }

    @Override
    public void updateLogout() {
	this.setVisible(false);
	loginWindow.setVisible(true);
    }

    @Override
    public void updateQuerySearchResults(ExperimentData[] searchResults) {
	querySearchTab.updateSearchResults(searchResults);
    }

    public void setProcessTab(ProcessTab processTab) {
	this.processTab = processTab;
	tabbedPane.add("PROCESS", processTab);

    }

    private void setLookAndFeel() {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}
    }

    public void setSearchTab(SearchTab searchTab) {
	this.searchTab = searchTab;
	// tabbedPane.add("SEARCH", searchTab);
    }

    public void setUploadTab(UploadTab uploadTab) {
	this.uploadTab = uploadTab;
	tabbedPane.add("UPLOAD", uploadTab);
    }

    public void setWorkspaceTab(WorkspaceTab workspaceTab) {
	this.workspaceTab = workspaceTab;
	tabbedPane.add("WORKSPACE", workspaceTab);
    }

    public void setAnalyzeTab(AnalyzeTab analyzeTab) {
	this.analyzeTab = analyzeTab;
	tabbedPane.add("ANALYZE", analyzeTab);
    }

    public void setSysAdminTab(SysadminTab sat) {
	this.sysadminTab = sat;
	tabbedPane.add("ADMINISTRATION", sysadminTab);
    }

    public void setQuerySearchTab(QuerySearchTab qst) {
	this.querySearchTab = qst;
	tabbedPane.add("SEARCH", querySearchTab);
    }

    public void setSearchAnnotationTypes(AnnotationDataType[] annotationTypes) {
	querySearchTab.setAnnotationTypes(annotationTypes);
    }

    @Override
    public void setProccessFileList(ArrayList<FileData> allFileData) {

	ArrayList<FileData> fileArray = allFileData;

	// TODO
	// TESTING
	for (int i = 0; i < fileArray.size(); i++) {

	    System.out.println(fileArray.get(i).filename);

	}
	processTab.setFileInfo(allFileData);

    }

    @Override
    public void printToConvertText(String message) {
	processTab.printToConvertText(message);
    }

    @Override
    public ArrayList<FileData> getWorkspaceSelectedFiles() {

	return workspaceTab.getSelectedFiles();
    }

    public ArrayList<ExperimentData> getWorkspaceSelectedExperiments() {
        return workspaceTab.getSelectedExperiments();
    }

    public void refreshGUI() {
	mainPanel.repaint();
	mainPanel.revalidate();
    }

    public void showLoginWindow() {
	loginWindow.setVisible(true);
    }

    @Override
    public void addNewExpButtonListener(ActionListener listener) {
	uploadTab.addNewExpButtonListener(listener);
    }

    @Override
    public void addSelectButtonListener(ActionListener listener) {
	uploadTab.addSelectButtonListener(listener);
    }

    @Override
    public void addUploadButtonListener(ActionListener listener) {
	uploadTab.addUploadButtonListener(listener);
    }

    @Override
    public void createNewExp(AnnotationDataType[] annotations) {
	uploadTab.createNewExp(annotations);
    }
    @Override
    public void selectFilesToNewExp(String[] fileNames) {
	uploadTab.createUploadFileRow(fileNames);
    }

    public DownloadWindow getDownloadWindow() {
        return downloadWindow;
    }

	@Override
	public String[] getBowtieParameters() {
		return processTab.getBowtieParameters();
	}

	@Override
	public File[] getFilesToUpload() {
	    return uploadTab.getUploadFiles();
	}

	@Override
	public AnnotationDataValue[] getUploadAnnotations() {
	    return uploadTab.getUploadAnnotations();
	}

	@Override
	public void setBowtieParameters() {
		processTab.setBowtieParameters();
	}

	@Override
	public JList getfileList() {
		return processTab.getFileList();
	}

    @Override
    public void setSysadminController(SysadminController sysadminController) {
        sysadminTab.setController(sysadminController);

    }
}
