package logica;

public class CartasApiladasCeldaLibre extends CartasApiladas {
	@Override
	public boolean rulesAllowAddingThisCard(Carta card) {
		return size() == 0;

	}
}
