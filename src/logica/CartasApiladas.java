package logica;

import java.util.*;

public class CartasApiladas implements Iterable<Carta> {

	private ArrayList<Carta> cartas = new ArrayList<Carta>();

	public void pushIgnoreRules(Carta newCard) {
		cartas.add(newCard);
	}

	public Carta sacarIgnorarReglas() {
		int lastIndex = size() - 1;
		Carta crd = cartas.get(lastIndex);
		cartas.remove(lastIndex);
		return crd;
	}

	public boolean push(Carta newCard) {
		if (rulesAllowAddingThisCard(newCard)) {
			cartas.add(newCard);
			return true;
		} else {
			return false;
		}
	}

	public boolean rulesAllowAddingThisCard(Carta card) {
		return true;
	}

	public int size() {
		return cartas.size();
	}

	public Carta pop() {
		if (!isRemovable()) {
			throw new UnsupportedOperationException("No se puede remover");
		}
		return sacarIgnorarReglas();
	}

	public void shuffle() {
		Collections.shuffle(cartas);
	}

	public Carta peekTop() {
		return cartas.get(cartas.size() - 1);
	}

	public Iterator<Carta> iterator() {
		return cartas.iterator();
	}

	public ListIterator<Carta> reverseIterator() {
		return cartas.listIterator(cartas.size());
	}

	public void clear() {
		cartas.clear();
	}

	public boolean isRemovable() {
		return true;
	}
}