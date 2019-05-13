package presentacion;

import logica.Carta;
import logica.CartasApiladas;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;

class UIPanelCarta extends JComponent implements MouseListener, MouseMotionListener, ChangeListener {

	private static final int NUMBER_OF_PILES = 8;

	private int GAP = 12;
	private int FOUNDATION_TOP = GAP;
	private int FOUNDATION_BOTTOM = FOUNDATION_TOP + Carta.altoCarta;

	private int FREE_CELL_TOP = GAP + 10;
	private int FREE_CELL_BOTTOM = FREE_CELL_TOP + Carta.altoCarta;

	private int TABLEAU_TOP = 2 * GAP + Math.max(FOUNDATION_BOTTOM, FREE_CELL_BOTTOM);
	private int TABLEAU_INCR_Y = 15;
	private int TABLEAU_START_X = GAP;
	private int TABLEAU_INCR_X = Carta.anchoCarta + GAP;

	private int DISPLAY_WIDTH = GAP + NUMBER_OF_PILES * TABLEAU_INCR_X;
	private int DISPLAY_HEIGHT = TABLEAU_TOP + 3 * Carta.altoCarta + GAP;

	private Color BACKGROUND_COLOR = new Color(255, 255, 255);


	private int _initX = 0;
	private int _initY = 0;

	private int _dragFromX = 0;
	private int _dragFromY = 0;

	private Carta _draggedCard = null;
	private CartasApiladas _draggedFromPile = null;

	private IdentityHashMap<CartasApiladas, Rectangle> _whereIs = new IdentityHashMap<CartasApiladas, Rectangle>();

	private boolean _autoComplete = false;

	private Modelo _model;

	UIPanelCarta(Modelo model) {

		_model = model;

		setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		setBackground(Color.blue);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		int x = TABLEAU_START_X;
		for (int pileNum = 0; pileNum < NUMBER_OF_PILES; pileNum++) {
			CartasApiladas p;
			if (pileNum < 4) {
				p = _model.getFreeCellPile(pileNum);
				_whereIs.put(p, new Rectangle(x, FREE_CELL_TOP, Carta.anchoCarta, Carta.altoCarta));
			} else {
				p = _model.getFoundationPile(pileNum - 4);
				_whereIs.put(p, new Rectangle(x, FOUNDATION_TOP, Carta.anchoCarta, Carta.altoCarta));
			}

			p = _model.getTableauPile(pileNum);
			_whereIs.put(p, new Rectangle(x, TABLEAU_TOP, Carta.anchoCarta, 3 * Carta.altoCarta));

			x += TABLEAU_INCR_X;
		}

		_model.addChangeListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, width, height);
		float[] hsv = new float[1];
		g.setColor(Color.WHITE);
		for (CartasApiladas pile : _model.getFreeCellPiles()) {
			_drawPile(g, pile, true);
		}
		for (CartasApiladas pile : _model.getFoundationPiles()) {
			_drawPile(g, pile, true);
		}
		for (CartasApiladas pile : _model.getTableauPiles()) {
			_drawPile(g, pile, false);
		}

		if (_draggedCard != null) {
			_draggedCard.draw(g);
		}
	}

	private void _drawPile(Graphics g, CartasApiladas pile, boolean topOnly) {
		Rectangle loc = _whereIs.get(pile);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(loc.x, loc.y, loc.width, loc.height);
		int y = loc.y;
		if (pile.size() > 0) {
			if (topOnly) {
				Carta card = pile.peekTop();
				if (card != _draggedCard) {
					card.setPosition(loc.x, y);
					card.draw(g);
				}
			} else {

				for (Carta card : pile) {
					if (card != _draggedCard) {

						card.setPosition(loc.x, y);
						card.draw(g);
						y += TABLEAU_INCR_Y;
					}
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		_draggedCard = null;
		for (CartasApiladas pile : _model) {
			if (pile.size() > 0) {
				Carta testCard = pile.peekTop();
				if (testCard.isInside(x, y)) {
					_dragFromX = x - testCard.getX();
					_dragFromY = y - testCard.getY();
					_draggedCard = testCard;
					_draggedFromPile = pile;
					break;
				}
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		_clearDrag();
		this.repaint();
	}

	void setAutoCompletion(boolean autoComplete) {
		_autoComplete = autoComplete;
	}

	public void mouseDragged(MouseEvent e) {
		if (_draggedCard == null) {
			return;
		}
		int newX;
		int newY;

		newX = e.getX() - _dragFromX;
		newY = e.getY() - _dragFromY;

		newX = Math.max(newX, 0);
		newX = Math.min(newX, getWidth() - Carta.anchoCarta);

		newY = Math.max(newY, 0);
		newY = Math.min(newY, getHeight() - Carta.altoCarta);

		_draggedCard.setPosition(newX, newY);

		this.repaint();
	}

	public void mouseReleased(MouseEvent e) {

		if (_draggedFromPile != null) {
			int x = e.getX();
			int y = e.getY();
			CartasApiladas targetPile = _findPileAt(x, y);
			if (targetPile != null) {

				_model.moveFromPileToPile(_draggedFromPile, targetPile);
				if (_autoComplete) {

					_model.makeAllPlays();
				}
			}
			_clearDrag();
			this.repaint();
		}
	}

	private void _clearDrag() {
		_draggedCard = null;
		_draggedFromPile = null;
	}

	private CartasApiladas _findPileAt(int x, int y) {
		for (CartasApiladas pile : _model) {
			Rectangle loc = _whereIs.get(pile);
			if (loc.contains(x, y)) {
				return pile;
			}
		}

		return null;
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		;
	}
}
