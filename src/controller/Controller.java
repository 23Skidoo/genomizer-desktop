package controller;

import gui.DownloadWindow;
import gui.GenomizerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import model.GenomizerModel;

public class Controller {

    private GenomizerView view;
    private GenomizerModel model;
    private final JFileChooser fileChooser = new JFileChooser();

    public Controller(GenomizerView view, GenomizerModel model) {
	this.view = view;
	this.model = model;
	view.addLoginListener(new LoginListener());
	view.addLogoutListener(new LogoutListener());
	view.addSearchListener(new QuerySearchListener());
	view.addUploadFileListener(new UploadListener());
	view.addBrowseListener(new BrowseListener());
	view.addConvertFileListener(new ConvertFileListener());
	view.addQuerySearchListener(new QuerySearchListener());
	view.addRawToProfileDataListener(new RawToProfileDataListener());
	view.addRawToRegionDataListener(new RawToRegionDataListener());
	view.addScheduleFileListener(new ScheduleFileListener());
	view.addDownloadFileListener(new DownloadListener());

    }

    class ConvertFileListener implements ActionListener, Runnable {
	@Override
	public void actionPerformed(ActionEvent e) {
	    new Thread(this).start();
	}

	@Override
	public void run() {

	    System.out.println("CONVERT");
	    System.out.println(view.getAllMarkedFiles());

	}
    }

    class RawToProfileDataListener implements ActionListener, Runnable {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    	    new Thread(this).start();
    	}

    	@Override
    	public void run() {

    	    System.out.println("RAW TO PROFILE");
    	    System.out.println(view.getAllMarkedFiles());

    	    System.out.println("Has converted RAW TO PROFILE: " + model.rawToProfile(view.getAllMarkedFiles()));

    	}
    }

    class RawToRegionDataListener implements ActionListener, Runnable {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    	    new Thread(this).start();
    	}

    	@Override
    	public void run() {

    	    System.out.println("RAW TO REGION");
    	    System.out.println(view.getAllMarkedFiles());

    	}
    }

    class ScheduleFileListener implements ActionListener, Runnable {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    	    new Thread(this).start();
    	}

    	@Override
    	public void run() {

    	    System.out.println("SCHEDULEING FILE");
    	    System.out.println(view.getAllMarkedFiles());

    	}
    }

    class LoginListener implements ActionListener, Runnable {
	@Override
	public void actionPerformed(ActionEvent e) {
	    new Thread(this).start();
	}

	@Override
	public void run() {
	    String username = view.getUsername();
	    String pwd = view.getPassword();
	    view.updateLoginAccepted(username, pwd, "Yuri Gagarin");
	    // if (model.loginUser(username, pwd)) {
	    // view.updateLoginAccepted(username, pwd, "Yuri Gagarin");
	    // } else {
	    // view.updateLoginNeglected("Login not accepted");
	    // }
	}
    }

    class QuerySearchListener implements ActionListener, Runnable {
	public void actionPerformed(ActionEvent e) {
	    new Thread(this).start();
	}

	@Override
	public void run() {
	    String pubmed = view.getQuerySearchString();
	    model.search(pubmed);
	}
    }

    class LogoutListener implements ActionListener, Runnable {
	public void actionPerformed(ActionEvent e) {
	    new Thread(this).start();
	}

	@Override
	public void run() {
	    view.updateLogout();
	    // if (model.logoutUser()) {
	    // view.updateLogout();
	    // } else {
	    // view.updateLogout();
	    // }

	}
    }

    class UploadListener implements ActionListener, Runnable {

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
	    new Thread(this).start();
	}

	@Override
	public void run() {

	    if (model.uploadFile()) {
		// update view?
	    }

	}
    }

    class BrowseListener implements ActionListener, Runnable {
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
	    new Thread(this).start();
	}

	@Override
	public void run() {
	    int returnVal = fileChooser.showOpenDialog(new JPanel());
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		String filePath = fileChooser.getSelectedFile()
			.getAbsolutePath();

		view.updateFileChosen(filePath);
	    }
	}
    }

    class DownloadListener implements ActionListener, Runnable {

	@Override
	public void actionPerformed(ActionEvent actionEvent) {

	    new Thread(this).start();
	}

	@Override
	public void run() {
		/*
		 * TODO N�r vi har faktiska filer som ska nedladdas: anv�nd den andra konstruktorn new DownloadWindow(ArrayList<String>) ist�llet
		 */
		new DownloadWindow();
	}
    }
}
