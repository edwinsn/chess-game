import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

import partidasJugadas.partidasJugadas;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Color;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame ventanaPrincipal=new JFrame();
		ventanaPrincipal.setSize(800, 800);

		ventanaPrincipal.setIconImage(new ImageIcon("/imagenes/p.png").getImage());
		ventanaPrincipal.setLocationRelativeTo(null);
		
		JButton btnNewButton_2 = new JButton("Salir de Aquí");
		btnNewButton_2.setBackground(Color.WHITE);
		btnNewButton_2.setBounds(261, 336, 264, 105);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			System.exit(0);
			}
		});
		
		
		
		JButton btnNewButton = new JButton("Jugar");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(261, 235, 264, 105);
		btnNewButton.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent e) {
				try {
					tablero t=new tablero(true);//true para blancas false para negras
				} catch (MoveGeneratorException es) {
			}
			}
		});
		ventanaPrincipal.getContentPane().setLayout(null);
		ventanaPrincipal.getContentPane().add(btnNewButton);
		ventanaPrincipal.getContentPane().add(btnNewButton_2);
		
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(10, 62, 631, 544);
		lblNewLabel_2.setIcon(new ImageIcon(Main.class.getResource("/imagenes/Chess_tile_rl.svg (1).png")));
		ventanaPrincipal.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(Main.class.getResource("/imagenes/Chess_tile_rl.svg (1).png")));
		lblNewLabel_1.setBounds(21, 80, 784, 588);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(0, 0, 784, 749);
		lblNewLabel.setIcon(new ImageIcon(Main.class.getResource("/imagenes/fondo.png")));
		ventanaPrincipal.getContentPane().add(lblNewLabel);
		ventanaPrincipal.setVisible(true);
			
			
		}
	

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
