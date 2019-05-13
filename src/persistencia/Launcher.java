package persistencia;

import presentacion.Modelo;

public class Launcher {

	private Modelo miApp;

	public Launcher() {
		miApp = new Modelo();
		miApp.iniciar();
	}

	public static void main(String[] args) {
		new Launcher();
	}

}