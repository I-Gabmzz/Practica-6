// Se importa la libreria necesaria para HashSet.
import java.util.HashSet;

// Se crea la clase jugador.
public class Jugador {

    // Se declaran los atributos de la clase.
    private String nombre;
    private HashSet<String> palabrasUsadas;
    private int puntuacionTotal ;

    // Se declara el constructor de la clase recibiendo como parametro el nombre del jugador.
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.palabrasUsadas = new HashSet<>();
        this.puntuacionTotal = 0;
    }

    // Metodo para eliminar palabras al HashSet, recibe como parametro la palabra a agregar y la puntuacion que se le acumulara.
    public void eliminarPalabra(String palabra, int puntuacion) {
        this.palabrasUsadas.remove(palabra);
        this.puntuacionTotal += puntuacion;
    }

    // Metodo para agregar palabras al HashSet, recibe como parametro la palabra a agregar y la puntuacion que se le acumulara.
    public void agregarPalabra(String palabra, int puntuacion) {
        palabrasUsadas.add(palabra.toLowerCase());
        this.puntuacionTotal += puntuacion;
    }
    // Metodo getter para obtener la puntuacion total del jugador.
    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    // Metodo para verificar si el jugador ya uso una palabra, recibiendo como parametro la palabra que desea verificar.
    public boolean yaUsoEstaPalabra(String palabra) {
        return palabrasUsadas.contains(palabra.toLowerCase());
    }

    // Metodo para reiniciar el HashSet de las palabras usadas.
    public void reiniciarPalabrasUsadas() {
        palabrasUsadas.clear();
    }

    // Metodo para obtener el HashSet de las palabras usadas del jugador.
    public HashSet<String> getPalabrasUsadas() {
        return palabrasUsadas;
    }

    // Metodo para obtener el nombre asignado al jugador.
    public String getNombre() {
        return nombre;
    }

}