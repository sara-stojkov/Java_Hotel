package main;

import utils.AppSettings;
import view.*;
import java.io.IOException;

import managers.ManagerFactory;

public class ISHotela {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Aplikacija se pokreće ...");
		AppSettings appSettings = new AppSettings("./data/korisnici.csv", "./data/sobe.csv", "./data/tipoviSoba.csv","./data/rezervacije.csv", "./data/dodatneUsluge.csv", "./data/cenovnici.csv", "./data/ciscenjeLog.csv", "./data/danasnjipodaci.csv");
		ManagerFactory controlers = new ManagerFactory(appSettings);
		controlers.ucitavanje();
		
		System.out.println("Pokretanje glavnog prozora");
		
		StartScreen main = new StartScreen(controlers);
		
		main.setVisible(true);
		
	}

}
