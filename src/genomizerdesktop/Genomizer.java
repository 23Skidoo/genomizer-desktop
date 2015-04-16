package genomizerdesktop;

import gui.AnalyzeTab;
import gui.GUI;
import gui.ProcessTab;
import gui.QuerySearchTab;
import gui.UploadTab;
import gui.WorkspaceTab;
import gui.sysadmin.SysadminTab;

import javax.swing.SwingUtilities;

import model.Model;
import controller.Controller;


public class Genomizer {

    public static void main(String args[]) {

        // Create GUI
        final GUI gui = new GUI();

        // Create Tabs
        UploadTab ut = new UploadTab();
        WorkspaceTab wt = new WorkspaceTab();
        ProcessTab pt = new ProcessTab();
        AnalyzeTab at = new AnalyzeTab();
        SysadminTab sat = new SysadminTab();
        QuerySearchTab qst = new QuerySearchTab();

        // Set tabs in GUI
        gui.setQuerySearchTab(qst);
        gui.setUploadTab(ut);
        gui.setProcessTab(pt);
        gui.setWorkspaceTab(wt);
   //     gui.setAnalyzeTab(at);

        // Create model and controller
        Model model = new Model();
        Controller controller = new Controller(gui, model);

        // Start the GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui.showLoginWindow();
                gui.pack();
            }
        });


    }
}
