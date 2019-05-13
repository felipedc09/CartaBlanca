package presentacion;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class UIEspacioLibre extends JFrame {
	private Modelo _model = new Modelo();

	private UIPanelCarta panel;

	public UIEspacioLibre() {
		panel = new UIPanelCarta(_model);

		JButton newGameBtn = new JButton("Nuevo Juego");
		newGameBtn.addActionListener(new nuevoJuego());

		JPanel controlPanel = new JPanel(new FlowLayout());
		controlPanel.add(newGameBtn);

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.add(controlPanel, BorderLayout.NORTH);
		content.add(panel, BorderLayout.CENTER);

		setContentPane(content);
		setTitle("CARTA BLANCA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	class nuevoJuego implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			_model.reset();
		}
	}

}
