import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Tablero extends JFrame {
    private static final int LONGITUD_PALABRA = 5;
    private static final int FILAS = 6;
    private JuegoInterface juego;
    private int intentoActual = 0;
    
    private JPanel tableroPanel;
    private JPanel tecladoPanel;
    private JTextField[][] tablero;
    private Map<String, JButton> tecladoBotones = new HashMap<>();
    private JButton botonEnviar;
    private JButton botonEliminar;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem instruccionesMenuItem, creditosMenuItem, agregarPalabraMenuItem;
    
    public Tablero(JuegoInterface juego) {
        this.juego = juego;
        setTitle("Juego Wordle");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Menú de opciones
        menuBar = new JMenuBar();
        menu = new JMenu("Menú");
        
        instruccionesMenuItem = new JMenuItem("Instrucciones");
        creditosMenuItem = new JMenuItem("Créditos");
        agregarPalabraMenuItem = new JMenuItem("Agregar palabra");
        
        instruccionesMenuItem.addActionListener(e -> mostrarInstrucciones());
        creditosMenuItem.addActionListener(e -> mostrarCreditos());
        agregarPalabraMenuItem.addActionListener(e -> agregarPalabra());

        menu.add(instruccionesMenuItem);
        menu.add(creditosMenuItem);
        menu.add(agregarPalabraMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Panel para la cuadrícula (tablero)
        tableroPanel = new JPanel();
        tableroPanel.setLayout(new GridLayout(FILAS, LONGITUD_PALABRA, 5, 5));

        // Crear la cuadrícula de texto
        tablero = new JTextField[FILAS][LONGITUD_PALABRA];
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < LONGITUD_PALABRA; j++) {
                tablero[i][j] = new JTextField();
                tablero[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                tablero[i][j].setHorizontalAlignment(JTextField.CENTER);
                tablero[i][j].setEditable(false);
                tablero[i][j].setBackground(Color.WHITE);
                tableroPanel.add(tablero[i][j]);
            }
        }

        // Panel para el teclado virtual
        tecladoPanel = new JPanel();
        tecladoPanel.setLayout(new GridLayout(3, 10, 5, 5));

        // Crear botones de las letras del teclado
        String[] letras = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
        for (String letra : letras) {
            JButton boton = new JButton(letra);
            boton.setFont(new Font("Arial", Font.BOLD, 16));
            boton.addActionListener(e -> agregarLetra(letra));
            tecladoBotones.put(letra, boton);
            tecladoPanel.add(boton);
        }

        // Botón de eliminar (<--) al final de la fila de letras
        botonEliminar = new JButton("<--");
        botonEliminar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEliminar.addActionListener(e -> eliminarLetra());
        tecladoPanel.add(botonEliminar); // Lo agregamos al final del teclado

        // Botón de enviar
        botonEnviar = new JButton("Enviar");
        botonEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEnviar.addActionListener(e -> manejarIntento());

        // Agregar paneles a la ventana
        add(tableroPanel, BorderLayout.CENTER);
        add(tecladoPanel, BorderLayout.SOUTH);
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonEnviar); // Botón de enviar
        add(panelBotones, BorderLayout.NORTH);
    }

    // Método para agregar letra a la fila actual
    private void agregarLetra(String letra) {
        for (int i = 0; i < LONGITUD_PALABRA; i++) {
            if (tablero[intentoActual][i].getText().isEmpty()) {
                tablero[intentoActual][i].setText(letra);
                break;
            }
        }
    }

    // Método para eliminar la última letra
    private void eliminarLetra() {
        for (int i = LONGITUD_PALABRA - 1; i >= 0; i--) {
            if (!tablero[intentoActual][i].getText().isEmpty()) {
                tablero[intentoActual][i].setText("");  // Eliminar la letra
                break;
            }
        }
    }

    // Método para manejar el intento
    private void manejarIntento() {
        StringBuilder intento = new StringBuilder();
        for (int i = 0; i < LONGITUD_PALABRA; i++) {
            intento.append(tablero[intentoActual][i].getText());
        }

        if (intento.length() == LONGITUD_PALABRA) {
            String resultado = juego.verificarIntento(intento.toString());
            actualizarColores(resultado, intento.toString());
            
            // Si el jugador adivina correctamente
            if (resultado.startsWith("¡Felicidades!")) {
                JOptionPane.showMessageDialog(this, resultado);
                reiniciarTablero();
            } 
            // Si se agotaron los intentos
            else if (juego.getIntentosRestantes() == 0) {
                JOptionPane.showMessageDialog(this, "Has perdido. La palabra era: " + juego.getPalabraObjetivo());
                reiniciarTablero();
            } 
            // Si aún hay intentos restantes
            else {
                intentoActual++;  // Siguiente intento
                if (intentoActual >= FILAS) {
                    botonEnviar.setEnabled(false);  // Deshabilitar botón de enviar si se agotaron los intentos
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Completa la palabra antes de enviar.");
        }
    }

    // Método para actualizar los colores en el tablero
    private void actualizarColores(String resultado, String intento) {
        for (int i = 0; i < LONGITUD_PALABRA; i++) {
            char resultadoLetra = resultado.charAt(i);
            Color color;
            if (resultadoLetra == 'G') {
                color = Color.GREEN;
            } else if (resultadoLetra == 'Y') {
                color = Color.YELLOW;
            } else {
                color = Color.GRAY;
            }
            tablero[intentoActual][i].setBackground(color);
            tecladoBotones.get(String.valueOf(intento.charAt(i))).setBackground(color);
        }
    }

    // Reiniciar tablero
    private void reiniciarTablero() {
        intentoActual = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < LONGITUD_PALABRA; j++) {
                tablero[i][j].setText("");
                tablero[i][j].setBackground(Color.WHITE);
            }
        }

        // Restaurar color del teclado
        for (JButton boton : tecladoBotones.values()) {
            boton.setBackground(null);
        }

        // Llamar a reiniciar el juego para cargar una nueva palabra
        juego.reiniciarJuego();
    }

    // Ver Insrtrucciones
    private void mostrarInstrucciones() {
        JOptionPane.showMessageDialog(this, "Instrucciones:\n" +
                "1. Adivina la palabra de 5 letras.\n" +
                "2. Tienes 6 intentos.\n" +
                "3. Las letras correctas se marcarán en verde.\n" +
                "4. Las letras que estén en la palabra pero en otra posición se marcarán en amarillo.\n" +
                "5. Las letras que estén en gris no existen en la palabra.\n" +
                "6. Ñ = N\n" +
                "\t¡Buena suerte! :)");
    }

    // Mostrar creditos
    private void mostrarCreditos() {
        JOptionPane.showMessageDialog(this, "\tCréditos:\n" +
                "Juego creado por Ana Chen Zhang.\n");
    }

    // Agregar palabra nueva
    private void agregarPalabra() {
        String nuevaPalabra = JOptionPane.showInputDialog(this, "Introduce una nueva palabra (5 letras):");
        if (nuevaPalabra != null && nuevaPalabra.length() == LONGITUD_PALABRA) {
            nuevaPalabra = nuevaPalabra.toUpperCase();  // Asegurarse de que la palabra sea mayúscula
            if (palabraYaExistente(nuevaPalabra)) {
                JOptionPane.showMessageDialog(this, "La palabra ya existe en el archivo.");
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Proyecto Final\\palabras.txt", true))) {
                    writer.write(nuevaPalabra + "\n");
                    JOptionPane.showMessageDialog(this, "Palabra añadida exitosamente.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al agregar la palabra.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "La palabra debe tener 5 letras.");
        }
    }

    // Verificar si existe o no
    private boolean palabraYaExistente(String nuevaPalabra) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Proyecto Final\\palabras.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().equalsIgnoreCase(nuevaPalabra)) { // Comparar sin distinguir mayúsculas y minúsculas
                    return true; // La palabra ya existe
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de palabras.");
        }
        return false; // La palabra no existe
    }

}



