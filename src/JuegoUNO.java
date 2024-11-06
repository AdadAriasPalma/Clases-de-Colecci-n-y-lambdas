import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase principal del juego UNO. Controla el flujo de turnos, interfaz gráfica, y reglas del juego.
 */
public class JuegoUNO extends JFrame {
    private BarajaUNO baraja;
    private Jugador jugador1;
    private Jugador jugador2;
    private CartaUNO cartaActual; // Carta en la pila
    private String colorActual; // Color actual del juego (para gestionar comodines)
    private int turno; // Indica el turno: 0 para jugador1, 1 para jugador2
    private int acumuladoCartas; // Cartas acumuladas por jugadas de +2 o +4
    private JPanel panelMano;
    private JScrollPane scrollPanelMano;
    private JPanel panelCartaActual;
    private JLabel lblTurno;
    private JButton botonUNO;
    private Timer temporizador;
    private JLabel lblTemporizador;
    private int tiempoRestante; // Tiempo restante en el turno actual
    private CartaGrafica cartaGrafica;
    private JButton botonMazo;

    /**
     * Constructor de JuegoUNO. Configura el tablero e inicializa los jugadores y la baraja.
     */
    public JuegoUNO() {
        // Inicialización de la baraja y los jugadores
        baraja = new BarajaUNO();
        jugador1 = new Jugador("Jugador 1", baraja.repartirMano());
        jugador2 = new Jugador("Jugador 2", baraja.repartirMano());
        cartaActual = baraja.robarCarta();
        colorActual = cartaActual.getColor();
        acumuladoCartas = 0;
        turno = 0;

        // Configuración de la ventana de juego
        setTitle("Juego de UNO - Dos Jugadores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicialización de paneles y temporizador
        inicializarPaneles();
        iniciarTemporizador();
        actualizarPanel();
        setVisible(true);
    }

    /**
     * Configura los paneles de la interfaz gráfica, incluyendo el panel de cartas y los botones.
     */
    private void inicializarPaneles() {
        // Panel superior que muestra el turno y el temporizador
        JPanel panelSuperior = new JPanel(new BorderLayout());
        lblTurno = new JLabel("Turno de: " + jugador1.getNombre(), SwingConstants.CENTER);
        lblTemporizador = new JLabel("Tiempo restante: 15 segundos", SwingConstants.CENTER);
        panelSuperior.add(lblTurno, BorderLayout.NORTH);
        panelSuperior.add(lblTemporizador, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel que muestra la carta actual en la pila
        panelCartaActual = new JPanel();
        cartaGrafica = new CartaGrafica(cartaActual.getValor(), obtenerColorCarta(cartaActual.getColor()));
        panelCartaActual.add(cartaGrafica);
        add(panelCartaActual, BorderLayout.CENTER);

        // Panel para las cartas del jugador, con desplazamiento
        panelMano = new JPanel(new FlowLayout());
        scrollPanelMano = new JScrollPane(panelMano);
        scrollPanelMano.setPreferredSize(new Dimension(700, 200));
        add(scrollPanelMano, BorderLayout.SOUTH);

        // Botón "UNO" que se activa cuando un jugador tiene una carta
        botonUNO = new JButton("UNO");
        botonUNO.setVisible(false); // Visible solo cuando el jugador tiene una carta
        botonUNO.addActionListener(e -> botonUNO.setVisible(false)); // Ocultar al presionar
        add(botonUNO, BorderLayout.EAST);

        // Botón para robar del mazo
        botonMazo = new JButton("Robar del Mazo");
        botonMazo.addActionListener(e -> {
            robarCartaJugadorActual();
            reiniciarTemporizador();
        });
        add(botonMazo, BorderLayout.WEST);
    }

    /**
     * Actualiza el panel de cartas del jugador y la información del turno.
     * Muestra las cartas disponibles y el botón "UNO" si es necesario.
     */
    private void actualizarPanel() {
        panelMano.removeAll();
        Jugador jugadorActual = (turno == 0) ? jugador1 : jugador2;

        // Mostrar el botón UNO si el jugador tiene solo una carta
        botonUNO.setVisible(jugadorActual.getMano().size() == 1);

        // Añadir botones para cada carta en la mano del jugador actual
        for (CartaUNO carta : jugadorActual.getMano()) {
            JButton botonCarta = new JButton("<html><h2>" + carta.getValor() + "</h2></html>");
            botonCarta.setBackground(obtenerColorCarta(carta.getColor()));
            botonCarta.setForeground(Color.WHITE);
            botonCarta.setPreferredSize(new Dimension(100, 150));

            // Acción al jugar una carta
            botonCarta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (esJugadaValida(carta)) {
                        jugadorActual.jugarCarta(carta);
                        if (carta.esComodin()) {
                            usarComodin(carta);
                        } else {
                            cartaActual = carta;
                            colorActual = carta.getColor();
                        }
                        actualizarCartaActualVisual();
                        verificarAccionesEspeciales(carta);
                        cambiarTurno();
                    } else {
                        JOptionPane.showMessageDialog(null, "Jugada no válida. Debes jugar una carta del mismo color o valor.");
                    }
                }
            });

            panelMano.add(botonCarta);
        }

        lblTurno.setText("Turno de: " + jugadorActual.getNombre());
        panelMano.revalidate();
        panelMano.repaint();
        verificarGanador(jugadorActual);
    }

    /**
     * Actualiza visualmente la carta en la pila.
     */
    private void actualizarCartaActualVisual() {
        cartaGrafica.actualizarCarta(cartaActual.getValor(), obtenerColorCarta(colorActual));
        panelCartaActual.revalidate();
        panelCartaActual.repaint();
    }

    /**
     * Verifica si la carta jugada es válida, es decir, si coincide en color o valor con la carta actual o es un comodín.
     * @param carta Carta que se intenta jugar.
     * @return true si la jugada es válida, de lo contrario false.
     */
    private boolean esJugadaValida(CartaUNO carta) {
        return carta.esComodin() || carta.getColor().equals(colorActual) || carta.getValor().equals(cartaActual.getValor());
    }

    /**
     * Ejecuta la acción de un comodín, permitiendo cambiar el color actual si es necesario.
     * @param carta Carta comodín jugada.
     */
    private void usarComodin(CartaUNO carta) {
        if (carta.getValor().equals("Cambio Color")) {
            String[] opcionesColor = {"Rojo", "Azul", "Verde", "Amarillo"};
            colorActual = (String) JOptionPane.showInputDialog(this, "Selecciona un color", "Cambio de Color",
                    JOptionPane.QUESTION_MESSAGE, null, opcionesColor, opcionesColor[0]);
        }
        cartaActual = carta;
        baraja.descartarCarta(carta);
    }

    /**
     * Verifica si una carta especial (+2 o +4) acumula cartas adicionales o requiere que el siguiente jugador robe.
     * @param carta Carta especial jugada.
     */
    private void verificarAccionesEspeciales(CartaUNO carta) {
        if (carta.getValor().equals("+4")) {
            acumuladoCartas += 4;
            verificarJugadaMas4();
        } else if (carta.getValor().equals("+2")) {
            acumuladoCartas += 2;
            verificarJugadaMas2();
        }
    }

    /**
     * Cambia el turno al siguiente jugador y reinicia el temporizador.
     */
    private void cambiarTurno() {
        turno = (turno == 0) ? 1 : 0;
        reiniciarTemporizador();
        actualizarPanel();
    }

    /**
     * Verifica si el jugador actual ha ganado al quedarse sin cartas.
     * @param jugadorActual Jugador cuyo turno se acaba de completar.
     */
    private void verificarGanador(Jugador jugadorActual) {
        if (jugadorActual.getMano().size() == 0) {
            JOptionPane.showMessageDialog(null, jugadorActual.getNombre() + " ha ganado el juego!");
            System.exit(0);
        }
    }

    /**
     * Inicia el temporizador del turno, que cuenta 15 segundos por turno.
     * Si el tiempo se agota, el jugador actual debe robar 2 cartas.
     */
    private void iniciarTemporizador() {
        tiempoRestante = 15;
        if (temporizador != null) {
            temporizador.cancel();
        }
        temporizador = new Timer();
        temporizador.schedule(new TimerTask() {
            @Override
            public void run() {
                tiempoRestante--;
                SwingUtilities.invokeLater(() -> lblTemporizador.setText("Tiempo restante: " + tiempoRestante + " segundos"));
                if (tiempoRestante <= 0) {
                    temporizador.cancel();
                    robarCartas(2, turno == 0 ? jugador1 : jugador2);
                    cambiarTurno();
                }
            }
        }, 0, 1000);
    }

    /**
     * Reinicia el temporizador del turno actual.
     */
    private void reiniciarTemporizador() {
        tiempoRestante = 15;
        if (temporizador != null) {
            temporizador.cancel();
        }
        iniciarTemporizador();
    }

    /**
     * Permite que el jugador actual robe una carta del mazo.
     */
    private void robarCartaJugadorActual() {
        Jugador jugadorActual = (turno == 0) ? jugador1 : jugador2;
        jugadorActual.getMano().add(baraja.robarCarta());
        actualizarPanel();
    }

    /**
     * Verifica si el jugador siguiente tiene una carta +4 para evitar robar las cartas acumuladas.
     */
    private void verificarJugadaMas4() {
        Jugador jugadorSiguiente = jugadorSiguiente();
        if (jugadorSiguiente.getMano().stream().noneMatch(c -> c.getValor().equals("+4"))) {
            JOptionPane.showMessageDialog(null, jugadorSiguiente.getNombre() + " roba " + acumuladoCartas + " cartas.");
            robarCartas(acumuladoCartas, jugadorSiguiente);
            acumuladoCartas = 0;
        }
    }

    /**
     * Verifica si el jugador siguiente tiene una carta +2 para evitar robar las cartas acumuladas.
     */
    private void verificarJugadaMas2() {
        Jugador jugadorSiguiente = jugadorSiguiente();
        if (jugadorSiguiente.getMano().stream().noneMatch(c -> c.getValor().equals("+2"))) {
            JOptionPane.showMessageDialog(null, jugadorSiguiente.getNombre() + " roba " + acumuladoCartas + " cartas.");
            robarCartas(acumuladoCartas, jugadorSiguiente);
            acumuladoCartas = 0;
        }
    }

    /**
     * Devuelve el jugador que sigue en turno.
     * @return Jugador siguiente.
     */
    private Jugador jugadorSiguiente() {
        return (turno == 0) ? jugador2 : jugador1;
    }

    /**
     * Permite que un jugador robe una cantidad de cartas específica.
     * @param cantidad Número de cartas a robar.
     * @param jugador Jugador que debe robar las cartas.
     */
    private void robarCartas(int cantidad, Jugador jugador) {
        for (int i = 0; i < cantidad; i++) {
            jugador.getMano().add(baraja.robarCarta());
        }
        actualizarPanel();
    }

    /**
     * Convierte el color de la carta en un objeto Color de Java para su visualización.
     * @param color Color en texto (Rojo, Azul, Verde, Amarillo).
     * @return Color correspondiente en la librería de Java.
     */
    private Color obtenerColorCarta(String color) {
        switch (color) {
            case "Rojo":
                return Color.RED;
            case "Azul":
                return Color.BLUE;
            case "Verde":
                return Color.GREEN;
            case "Amarillo":
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }
}
