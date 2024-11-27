import java.io.*;
import java.util.*;

public class Juego implements JuegoInterface {
    private String palabraObjetivo;
    private int intentosRestantes;
    private static final int MAX_INTENTOS = 6;

    // Constructor que recibe el archivo de palabras
    public Juego(String archivoPalabras) {
        this.intentosRestantes = MAX_INTENTOS;
        this.palabraObjetivo = cargarPalabraDesdeArchivo(archivoPalabras);
    }

    // Cargar una palabra al azar desde el archivo
    private String cargarPalabraDesdeArchivo(String archivo) {
        List<String> palabras = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                palabras.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random rand = new Random();
        return palabras.get(rand.nextInt(palabras.size())); // Elegir una palabra aleatoria
    }

    @Override
    public String verificarIntento(String intento) {
        if (intento.length() != palabraObjetivo.length()) {
            return "La palabra debe tener " + palabraObjetivo.length() + " letras.";
        }

        StringBuilder resultado = new StringBuilder();

        // Verificar cada letra
        for (int i = 0; i < palabraObjetivo.length(); i++) {
            char letra = intento.charAt(i);
            if (letra == palabraObjetivo.charAt(i)) {
                resultado.append("G"); // Verde
            } else if (palabraObjetivo.indexOf(letra) >= 0) {
                resultado.append("Y"); // Amarillo
            } else {
                resultado.append("X"); // Gris
            }
        }

        // Decrementar los intentos restantes
        intentosRestantes--;
        
        if (intento.equals(palabraObjetivo)) {
            return "¡Felicidades! Has adivinado la palabra.";
        }

        return resultado.toString(); // G, Y, X
    }

    @Override
    public String getPalabraObjetivo() {
        return palabraObjetivo;
    }

    @Override
    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    @Override
    public void reiniciarJuego() {
        this.intentosRestantes = MAX_INTENTOS;
        this.palabraObjetivo = cargarPalabraDesdeArchivo("Proyecto Final\\palabras.txt");
    }
}
