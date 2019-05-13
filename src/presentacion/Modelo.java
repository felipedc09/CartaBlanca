package presentacion;

import logica.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Modelo implements Iterable<CartasApiladas> {

	private VistaPrincipal ventana;

	private CartasApiladas[] celdasLibres;
	private CartasApiladas[] celdasDesordenadas;
	private CartasApiladas[] celdasAgrupadas;

	private ArrayList<CartasApiladas> Pilas;

	private ArrayList<ChangeListener> changeListeners;

	private ArrayDeque<CartasApiladas> _undoStack = new ArrayDeque<CartasApiladas>();

	public Modelo() {
		Pilas = new ArrayList<CartasApiladas>();

		celdasLibres = new CartasApiladas[4];
		celdasDesordenadas = new CartasApiladasTablero[8];
		celdasAgrupadas = new CartasApiladas[4];

		for (int pile = 0; pile < celdasAgrupadas.length; pile++) {
			celdasAgrupadas[pile] = new CartasApiladasBase();
			Pilas.add(celdasAgrupadas[pile]);
		}

		for (int pile = 0; pile < celdasLibres.length; pile++) {
			celdasLibres[pile] = new CartasApiladasCeldaLibre();
			Pilas.add(celdasLibres[pile]);
		}

		for (int pile = 0; pile < celdasDesordenadas.length; pile++) {
			celdasDesordenadas[pile] = new CartasApiladasTablero();
			Pilas.add(celdasDesordenadas[pile]);
		}

		changeListeners = new ArrayList<ChangeListener>();

		reset();
	}

	public void iniciar() {
		ventana = new VistaPrincipal(this);
	}

	public void reset() {
		Baraja deck = new Baraja();
		deck.shuffle();

		for (CartasApiladas p : Pilas) {
			p.clear();
		}

		int whichPile = 0;
		for (Carta crd : deck) {
			celdasDesordenadas[whichPile].pushIgnoreRules(crd);
			whichPile = (whichPile + 1) % celdasDesordenadas.length;
		}

		_notifyEveryoneOfChanges();
	}

	public Iterator<CartasApiladas> iterator() {
		return Pilas.iterator();
	}

	public CartasApiladas getTableauPile(int i) {
		return celdasDesordenadas[i];
	}

	public CartasApiladas[] getTableauPiles() {
		return celdasDesordenadas;
	}

	public CartasApiladas[] getFreeCellPiles() {
		return celdasLibres;
	}

	public CartasApiladas getFreeCellPile(int cellNum) {
		return celdasLibres[cellNum];
	}

	public CartasApiladas[] getFoundationPiles() {
		return celdasAgrupadas;
	}

	public CartasApiladas getFoundationPile(int cellNum) {
		return celdasAgrupadas[cellNum];
	}

	public boolean moveFromPileToPile(CartasApiladas source, CartasApiladas target) {
		boolean result = false;
		if (source.size() > 0) {
			Carta crd = source.peekTop();
			if (target.rulesAllowAddingThisCard(crd)) {
				target.push(crd);
				source.pop();
				_notifyEveryoneOfChanges();

				_undoStack.push(source);
				_undoStack.push(target);
				result = true;
			}
		}
		return result;
	}

	private void _forceMoveFromPileToPile(CartasApiladas source, CartasApiladas target) {
		if (source.size() > 0) {
			target.push(source.pop());
			_notifyEveryoneOfChanges();
		}
	}

	public void makeAllPlays() {

		boolean worthTrying;
		do {
			worthTrying = false;

			for (CartasApiladas freePile : celdasLibres) {
				for (CartasApiladas gravePile : celdasAgrupadas) {
					worthTrying |= moveFromPileToPile(freePile, gravePile);
				}
			}

			for (CartasApiladas cardPile : celdasDesordenadas) {
				for (CartasApiladas gravePile : celdasAgrupadas) {
					worthTrying |= moveFromPileToPile(cardPile, gravePile);
				}
			}

		} while (worthTrying);
	}

	public void addChangeListener(ChangeListener someoneWhoWantsToKnow) {
		changeListeners.add(someoneWhoWantsToKnow);
	}

	private void _notifyEveryoneOfChanges() {
		for (ChangeListener interestedParty : changeListeners) {
			interestedParty.stateChanged(new ChangeEvent("Game state changed."));
		}
	}
}