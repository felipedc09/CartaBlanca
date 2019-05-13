package logica;

import java.awt.Color;

enum Palo {
	SPADES(Color.BLACK), HEARTS(Color.RED), CLUBS(Color.BLACK), DIAMONDS(Color.RED);
	private final Color _color;

	Palo(Color color) {
		_color = color;
	}

	public Color getColor() {
		return _color;
	}
}