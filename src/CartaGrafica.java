import javax.swing.*;
import java.awt.*;

public class CartaGrafica extends JComponent {
    private String valor;
    private Color color;

    public CartaGrafica(String valor, Color color) {
        this.valor = valor;
        this.color = color;
        setPreferredSize(new Dimension(100, 150)); // Tamaño de la carta
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar la carta (rectángulo)
        g2.setColor(color);
        g2.fillRoundRect(10, 10, 80, 130, 20, 20); // Dibujar un rectángulo redondeado

        // Dibujar el número de la carta
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.drawString(valor, 35, 85); // Posición del número en la carta
    }

    // Método para actualizar el valor y el color de la carta
    public void actualizarCarta(String nuevoValor, Color nuevoColor) {
        this.valor = nuevoValor;
        this.color = nuevoColor;
        repaint(); // Redibuja la carta cuando se actualicen los valores
    }
}
