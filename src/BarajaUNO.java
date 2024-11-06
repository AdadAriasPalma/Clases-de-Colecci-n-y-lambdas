import java.util.ArrayList;
import java.util.Collections;

/**
 * Representa el mazo de cartas en el juego UNO, incluyendo el montón de descartes.
 * Permite robar cartas y rebarajar cuando el mazo se queda sin cartas.
 */
public class BarajaUNO {
    private ArrayList<CartaUNO> cartas;
    private ArrayList<CartaUNO> descarte;

    /**
     * Constructor de la clase BarajaUNO. Inicializa el mazo y lo mezcla.
     * Incluye cartas numéricas, de acción y comodines.
     */
    public BarajaUNO() {
        cartas = new ArrayList<>();
        descarte = new ArrayList<>();
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        String[] valores = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Salta", "Reversa", "+2"};

        // Añade las cartas de cada color y valor al mazo
        for (String color : colores) {
            cartas.add(new CartaUNO(color, "0", false));
            for (int i = 0; i < 2; i++) {
                for (String valor : valores) {
                    cartas.add(new CartaUNO(color, valor, false));
                }
            }
        }

        // Añade los comodines al mazo
        for (int i = 0; i < 4; i++) {
            cartas.add(new CartaUNO("Comodín", "Cambio Color", true));
            cartas.add(new CartaUNO("Comodín", "+4", true));
        }

        Collections.shuffle(cartas); // Mezcla el mazo al inicio del juego
    }

    /**
     * Permite robar una carta del mazo. Si el mazo está vacío, se recargan las cartas desde el montón de descartes.
     * @return Carta robada del mazo.
     */
    public CartaUNO robarCarta() {
        if (cartas.isEmpty()) {
            cartas.addAll(descarte);
            descarte.clear();
            Collections.shuffle(cartas);
        }
        return cartas.remove(0);
    }

    // Mueve una carta al montón de descartes
    public void descartarCarta(CartaUNO carta) {
        descarte.add(carta);
    }

    /**
     * Genera una mano inicial de 7 cartas.
     * @return Lista de cartas para la mano inicial.
     */
    public ArrayList<CartaUNO> repartirMano() {
        ArrayList<CartaUNO> mano = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mano.add(robarCarta());
        }
        return mano;
    }
}
