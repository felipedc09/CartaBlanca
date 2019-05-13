package logica;

public class CartasApiladasBase extends CartasApiladas {
	@Override
	public boolean rulesAllowAddingThisCard(Carta card) {

		if ((this.size() == 0) && (card.getCara() == Numeracion.AS)) {
			return true;
		}

		if (size() > 0) {
			Carta top = peekTop();
			if ((top.getPinta() == card.getPinta() && (top.getCara().ordinal() + 1 == card.getCara().ordinal()))) {
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean isRemovable() {
		return false;
	}
}
