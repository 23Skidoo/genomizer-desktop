package genomizerdesktop;

import requests.*;

import com.google.gson.Gson;

public class Genomizer {

    public static void main(String args[]) {
	System.out.println("Starting Genomizerk");
	/* Your scrum master was here */
	// Lester

	System.out.println("adam testar");
	System.out.println("HUR G�R DET D� ADAM /CAPS?");
	/* Gson exempel */
	Gson gson = new Gson();
	Request login = new LoginRequest("kalle", "123");
	String json = gson.toJson(login);
	System.out.println(json);
	LoginRequest login2 = gson.fromJson(json, LoginRequest.class);
	System.out.println(login2.username + " " + login2.password);
	/* Gson exempel */
	GUI gui = new GUI();
	Connection con = new Connection("127.0.0.1", 25652);
	con.sendRequest(login);
	Thread threadConnection = new Thread(con);
	threadConnection.start();

    }
}
