public class Main {
    public static void main(String[] args) {
        // En este ejemplo, se juega en modo gráfico. Para consola, podríamos usar otro modo.
        boolean modoGrafico = true;

        // Usamos la interfaz para el juego (JuegoInterface)
        JuegoInterface juego = (JuegoInterface) new Juego("Proyecto Final\\palabras.txt");
        
        if (modoGrafico) {
            // Si se juega en modo gráfico, creamos la interfaz gráfica
            javax.swing.SwingUtilities.invokeLater(() -> new Tablero(juego).setVisible(true));
        } else {
            // Si se quiere jugar en modo consola, se llamaría a otro método para manejar la entrada en consola.
        }
    }
}
