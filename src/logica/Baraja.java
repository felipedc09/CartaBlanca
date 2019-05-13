package logica;

import java.util.*;

public class Baraja extends CartasApiladas {
	public Baraja() {
		for (Palo s : Palo.values()) {
			for (Numeracion f : Numeracion.values()) {
				Carta c = new Carta(f, s);
				c.mostrar();
				this.push(c);
			}
		}
		shuffle();
	}
}
