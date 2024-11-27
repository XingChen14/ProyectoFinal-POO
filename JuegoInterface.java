public interface JuegoInterface {
    String verificarIntento(String intento);
    String getPalabraObjetivo();
    int getIntentosRestantes();
    void reiniciarJuego();
}
