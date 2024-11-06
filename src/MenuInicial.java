import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInicial extends JFrame {

    public MenuInicial() {
        setTitle("Menú UNO");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Botón para empezar el juego
        JButton botonJugar = new JButton("Jugar");
        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JuegoUNO(); // Llama a la clase JuegoUNO para iniciar el juego
                dispose(); // Cierra la ventana del menú
            }
        });

        // Botón para salir
        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cierra la aplicación
            }
        });

        // Añadir los botones a la ventana
        add(botonJugar);
        add(botonSalir);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuInicial(); // Inicia la aplicación mostrando el menú principal
    }
}
