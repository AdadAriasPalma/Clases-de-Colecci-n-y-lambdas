import java.util.ArrayList;

/**
 * Representa un jugador en el juego UNO, con un nombre y una mano de cartas.
 */
public class Jugador {
    private String nombre;
    private ArrayList<CartaUNO> mano;

    /**
     * Constructor de la clase Jugador.
     * @param nombre Nombre del jugador.
     * @param mano Mano inicial de cartas del jugador.
     */
    public Jugador(String nombre, ArrayList<CartaUNO> mano) {
        this.nombre = nombre;
        this.mano = mano;
    }

    // Accesor para obtener el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Accesor para obtener la mano del jugador
    public ArrayList<CartaUNO> getMano() {
        return mano;
    }

    /**
     * Permite al jugador jugar una carta, eliminándola de su mano.
     * @param carta Carta a jugar.
     */
    public void jugarCarta(CartaUNO carta) {
        mano.remove(carta);
    }

    @Override
    public String toString() {
        return nombre + ": " + mano.toString();
    }
}
