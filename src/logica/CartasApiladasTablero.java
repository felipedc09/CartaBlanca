package logica;

public class CartasApiladasTablero extends CartasApiladas {
	@Override
	public boolean rulesAllowAddingThisCard(Carta card) {
		if ((this.size() == 0) || (this.peekTop().getCara().ordinal() - 1 == card.getCara().ordinal()
				&& this.peekTop().getPinta().getColor() != card.getPinta().getColor())) {
			return true;
		}
		return false;
	}
}
