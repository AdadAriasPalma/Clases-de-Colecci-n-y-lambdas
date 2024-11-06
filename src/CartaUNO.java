/**
 * Representa una carta en el juego UNO, con atributos para el color, valor y un indicador si es comodín.
 */
public class CartaUNO {
    private String color;
    private String valor;
    private boolean esComodin;

    /**
     * Constructor de la clase CartaUNO.
     * @param color Color de la carta (rojo, azul, verde, amarillo o "Comodín").
     * @param valor Valor de la carta (número, "Salta", "Reversa", "+2", etc.).
     * @param esComodin Indica si la carta es un comodín.
     */
    public CartaUNO(String color, String valor, boolean esComodin) {
        this.color = color;
        this.valor = valor;
        this.esComodin = esComodin;
    }

    // Accesor para obtener el color de la carta
    public String getColor() {
        return color;
    }

    // Accesor para obtener el valor de la carta
    public String getValor() {
        return valor;
    }

    // Indica si la carta es un comodín
    public boolean esComodin() {
        return esComodin;
    }

    @Override
    public String toString() {
        return color + " " + valor;
    }
}
