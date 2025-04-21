// Se importan las librerias necesarias para la clase Interfaz.
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

// Se declara la creacion de la clase Interfaz.
public class Interfaz {

    // Se inicializa un tipo de impresion en terminal.
    final String negrita = "\u001B[1m";

    // Metodo para imprimir el titulo del juego.
    public void imprimirTituloDelJuego() {
        final String reset = "\u001B[0m";
        final String MORADO = "\u001B[35m";
        final String AMARILLO = "\u001B[33m";

        String[] titulo = {
                "\n    ███╗   ███╗  █████╗   ██████╗   ██████╗      ██████╗  ███████╗",
                "    ████╗ ████║ ██╔══██╗ ██╔════╝  ██╔═══██╗     ██╔══██╗ ██╔════╝",
                "    ██╔████╔██║ ███████║ ██║  ███╗ ██║   ██║     ██║  ██║ █████╗  ",
                "    ██║╚██╔╝██║ ██╔══██║ ██║   ██║ ██║   ██║     ██║  ██║ ██╔══╝  ",
                "    ██║ ╚═╝ ██║ ██║  ██║ ╚██████╔╝ ╚██████╔╝     ██████╔╝ ███████╗",
                "    ╚═╝     ╚═╝ ╚═╝  ╚═╝  ╚═════╝   ╚═════╝      ╚═════╝  ╚══════╝",
                "                                                          ",
                "██████╗   █████╗  ██╗       █████╗  ██████╗  ██████╗   █████╗  ███████╗",
                "██╔══██╗ ██╔══██╗ ██║      ██╔══██╗ ██╔══██╗ ██╔══██╗ ██╔══██╗ ██╔════╝",
                "██████╔╝ ███████║ ██║      ███████║ ██████╔╝ ██████╔╝ ███████║ ███████╗",
                "██╔═══╝  ██╔══██║ ██║      ██╔══██║ ██╔══██╗ ██╔══██╗ ██╔══██║ ╚════██║",
                "██║      ██║  ██║ ███████╗ ██║  ██║ ██████╔╝ ██║  ██║ ██║  ██║ ███████║",
                "╚═╝      ╚═╝  ╚═╝ ╚══════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═╝  ╚═╝ ╚═╝  ╚═╝ ╚══════╝"
        };

        for (int i = 0; i < 6; i++) {
            System.out.println(MORADO + titulo[i] + reset);
        }

        System.out.println(titulo[6]);

        for (int i = 7; i < titulo.length; i++) {
            System.out.println(AMARILLO + titulo[i] + reset);
        }
    }

    // Metodo para solicitar la cantidad especifica de jugadores.
    public int solicitarJugadores(Scanner scanner) {
        int numeroDeJugadores;
        scanner = new Scanner(System.in);
        final String reset = "\u001B[0m";

        while (true) {
            System.out.print(negrita + "\n * Ingrese el numero de jugadores (2 - 4): " + reset);
            numeroDeJugadores = scanner.nextInt();
            if (numeroDeJugadores >= 2 && numeroDeJugadores <= 4) {
                return numeroDeJugadores;
            } else {
                System.out.println("El numero de jugadores debe de ser entre 2 y 4.");
            }
        }
    }

    // Metodo para solicitar y asignarle nombre a cada uno de los jugadores
    public String solicitarNombreDeJugador(Scanner scanner, int numDeJugador) {
        final String reset = "\u001B[0m";

        System.out.print(negrita + "\n * Ingrese el nombre del jugador " + numDeJugador + ": " + reset);
        String nombre = scanner.next();
        return nombre;
    }

    // Metodo en donde se presentan los modos de juego y permite seleccionar el modo deseado para jugar.
    public int solicitarModoDeJuego(Scanner scanner) {
        int opcModoDeJuego;
        final String verde = "\u001B[32m";
        final String rojo = "\u001B[31m";
        final String reset = "\u001B[0m";

        while (true) {
            System.out.println("\n      |=====| " + negrita + "MODOS DE JUEGO" + reset + " |=====| ");
            System.out.println(verde + "\n  1 -> NORMAL" + reset);
            System.out.println(rojo + "  2 -> EXPERTO" + reset + "\n");
            System.out.print(negrita + " * Ingrese el numero del modo de juego deseado: " + reset);
            opcModoDeJuego = scanner.nextInt();
            if (opcModoDeJuego == 1) {
                return 1;
            } else if (opcModoDeJuego == 2) {
                return 2;
            } else {
                System.out.println("La entrada esta fuera del rango, unicamente ingresa 1 o 2.");
            }
        }
    }

    // Metodo para "limpiar" pantalla.
    public void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // Metodo para indicar cada turno del jugador y a la vez el estado actual de la ronda.
    public void indicarTurnoDeJugador(Jugador jugador) {
        final String reset = "\u001B[0m";
        System.out.print(negrita + "\n" + jugador.getNombre() + " |" + jugador.getPuntuacionTotal() + " puntos| ---> " + reset);
    }

    // Metodo para imprimir el estado actual de la ronda.
    public void imprimirRonda (int ronda, HashSet<Character> letras) {
        final String reset = "\u001B[0m";
        System.out.println(negrita + "\n                       |=====| RONDA  " + ronda + " |=====|" + reset);
        System.out.println("\n               Forma palabras con las siguientes letras: \n\n                    " + letras);
        System.out.print(negrita + "\n * Escribe una palabra usando solo estas letras o presiona Enter para pasar tu turno.\n" + reset);
    }

    // Metodo para determinar el ganador y a la vez imprimir los resultados mediante una tabla ordenada de forma descendente.
    public void imprimirResultados (List<Jugador> jugadores) {
        final String negritaVerde = "\u001B[1;32m";
        final String reset = "\u001B[0m";

        System.out.println(negrita + "\n| ===== | RESULTADOS | ===== |" + reset);
        System.out.println("   Tabla de Puntuaciones  ");

        jugadores.sort((jugador1, jugador2) -> jugador2.getPuntuacionTotal() - jugador1.getPuntuacionTotal());
        jugadores.forEach(jugador -> System.out.println(" * " + jugador.getNombre() + "--> " + jugador.getPuntuacionTotal() + " Puntos Totales"));

        if (!jugadores.isEmpty()) {
            System.out.println(negritaVerde + "\nEl ganador del Mago De Palabras es: " + jugadores.get(0).getNombre() + "!!!" + reset);
        } else {
            System.out.println("No se puede determinar el ganador.");
        }

        System.out.println(negrita + "\nPalabras usadas por el ganador:" + reset);
        jugadores.get(0).getPalabrasUsadas().forEach(palabras -> System.out.println(" * " + palabras));
    }

    // Metodo para mostrar un aviso si la palabra formada es correcta.
    public void mostrarPalabraCorrecta(String aviso, int puntuacion, int puntosTotales) {
        final String negritaVerde = "\u001B[1;32m";
        final String reset = "\u001B[0m";
        System.out.println(negritaVerde + "\nLa palabra es correcta |" + aviso + "| ---> + " + puntuacion + " puntos (Puntos Totales: " + puntosTotales + ")" + reset);
    }

    // Metodo para mostrar un aviso si se infrijio alguna de las reglas del juego.
    public void mostrarPalabraIncorrecta(String aviso) {
        final String negritaRojo = "\u001B[1;31m";
        final String reset = "\u001B[0m";
        System.out.println(negritaRojo + "\n Error --> " + aviso + reset);
    }

    // Metodo para mostrar informacion acerca del modo de juego seleccionado.
    public void mostrarInformacionDelModo(int opcModoDeJuego) {
        final String reset = "\u001B[0m";
        final String negritaAmarillo = "\u001B[1;33m";


        if (opcModoDeJuego == 1) {
            System.out.println(negritaAmarillo + "\n       El modo normal consiste en que unicamente aparecen vocales \n     sin " +
                    "acentos y cada una de las 10 letras que aparecen disponibles\n                se" +
                    " pueden repetir para formar una palabra. " + reset);
        } else {
            System.out.println(negritaAmarillo + "\n       El modo experto consiste en que aparecen vocales con " +
                    "acentos \n           y cada una de las 10 letras que aparecen disponibles\n                 no" +
                    " pueden repetir para formar una palabra. " + reset);

        }
    }

}
