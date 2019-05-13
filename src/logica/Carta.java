package logica;

import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Carta {

	public static int anchoCarta;

	public static int altoCarta;

	private static String pathImagenes = "/cartas/";
	private static ImageIcon imagenTrasera;

	private static Class CLASS = Carta.class;
	private static String nombrePaquete;
	private static ClassLoader CLSLDR;

	private Numeracion cara;
	private Palo pinta;
	private ImageIcon imagen;
	private int _x;
	private int _y;
	private boolean cartaVisible = true;

	public Carta(Numeracion face, Palo suit) {
		nombrePaquete = CLASS.getPackage().getName();
		CLSLDR = CLASS.getClassLoader();
		String urlPath = nombrePaquete + pathImagenes + "b.gif";
		java.net.URL imageURL = CLSLDR.getResource(urlPath);
		imagenTrasera = new ImageIcon(imageURL);

		anchoCarta = imagenTrasera.getIconWidth();
		altoCarta = imagenTrasera.getIconHeight();

		cara = face;
		pinta = suit;

		_x = 0;
		_y = 0;

		cartaVisible = false;

		char faceChar = "a23456789tjqk".charAt(cara.ordinal());
		char suitChar = "shcd".charAt(pinta.ordinal());
		String cardFilename = "" + faceChar + suitChar + ".gif";

		String path = nombrePaquete + pathImagenes + cardFilename;
		imageURL = CLSLDR.getResource(path);

		imagen = new ImageIcon(imageURL);
	}

	public Numeracion getCara() {
		return cara;
	}

	public Palo getPinta() {
		return pinta;
	}

	public void setPosition(int x, int y) {
		_x = x;
		_y = y;
	}

	public void draw(Graphics g) {
		if (cartaVisible) {
			imagen.paintIcon(null, g, _x, _y);
		} else {
			imagenTrasera.paintIcon(null, g, _x, _y);
		}
	}

	public boolean isInside(int x, int y) {
		return (x >= _x && x < _x + anchoCarta) && (y >= _y && y < _y + altoCarta);
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	public void setX(int x) {
		_x = x;
	}

	public void setY(int y) {
		_y = y;
	}

	public String toString() {
		return "" + cara + " de " + pinta;
	}

	public void mostrar() {
		cartaVisible = true;
	}

	public void noMostrar() {
		cartaVisible = false;
	}
}