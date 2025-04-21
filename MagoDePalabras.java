// Se importan las librerias necesarias para la clase.
import java.io.*;
import java.util.*;

// Se crea la clase MagoDePalabras.
public class MagoDePalabras {
    // Se declaran los atributos de la clase.
    private Random random;
    private HashMap<String, Integer> diccionario;
    private HashMap<String, Integer> puntuaciones;
    private HashSet<String> palabrasUsadas;
    private HashSet<Character> letrasEnJuego;

    // Se declara el constructor de la clase.
    public MagoDePalabras() {
        random = new Random();
        diccionario = new HashMap<>();
        puntuaciones = new HashMap<>();
        palabrasUsadas = new HashSet<>();
        letrasEnJuego = new HashSet<>();
        cargarArchivo("C:\\Users\\PC OSTRICH\\Practica-5\\palabras.txt");
       // cargarArchivo("C:\\Users\\14321\\IdeaProjects\\Practica-5\\palabras.txt");
    }

    // Metodo para cargar el archivo de las palabras.
    public void cargarArchivo(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String palabra = linea.toLowerCase();
                diccionario.put(palabra, calcularPuntos(palabra));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Metodo para calcular los puntos de la palabra.
    public int calcularPuntos(String palabra) {
        int puntos = 0;
        String vocales = "aeiouáéíóú";
        for(int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if(vocales.indexOf(letra) != -1) {
                puntos += 5;
            }else{
                puntos += 3;
            }
        }
        return puntos;
    }

    // Metodo para generar letras aleatorias segun el modo normal.
    public HashSet<Character> generarLetrasAleatoriasNormal() {
        HashSet<Character> letras = new HashSet<>();
        String vocales = "aeiou";
        String consonantes = "bcdfghjklmnpqrstvwxyz";

        while (letras.size() < 3) {
            char vocal = vocales.charAt(random.nextInt(vocales.length()));
            letras.add(vocal);
        }

        while (letras.size() < 10) {
            char consonante = consonantes.charAt(random.nextInt(consonantes.length()));
            letras.add(consonante);
        }
        return letras;
    }

    //Metodo para generar letras aleatorias segun el modo experto.
    public HashSet<Character> generarLetrasAleatoriasExperto() {
        HashSet<Character> letras = new HashSet<>();
        String vocales = "aeiouáéíóú";
        String consonantes = "bcdfghjklmnpqrstvwxyz";

        while (letras.size() < 3) {
            char vocal = vocales.charAt(random.nextInt(vocales.length()));
            letras.add(vocal);
        }

        while (letras.size() < 10) {
            char consonante = consonantes.charAt(random.nextInt(consonantes.length()));
            letras.add(consonante);
        }
        return letras;
    }

    // Metodo para agregar letras en el juego.
    public void agregarLetrasEnJuego(HashSet<Character> letrasRonda) {
        letrasEnJuego.clear();
        letrasEnJuego.addAll(letrasRonda);
    }

    // Metodo para saber si ya se uso una palabra.
    public boolean yaSeUsoEsaPalabra(String palabra) {
        return palabrasUsadas.contains(palabra.toUpperCase());
    }

    // Metodo para saber si la palabra ingresada tiene letras validas en el modo normal.
    public boolean tieneLetrasValidasNormal(String palabra) {
        Set<Character> letrasDisponibles = new HashSet<>(letrasEnJuego);
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (!letrasDisponibles.contains(letra)) {
                return false;
            }
        }
        return true;
    }

    //Metodo para saber si la palabra ingresada tiene letras validas en el modo experto.
    public boolean tieneLetrasValidasExperto(String palabra) {
        if (palabra.length() > letrasEnJuego.size()) {
            return false;
        }
        Map<Character, Integer> frecuencias = new HashMap<>();
        for (Character letra : letrasEnJuego) {
            frecuencias.put(letra, frecuencias.getOrDefault(letra, 0) + 1);
        }
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            Integer count = frecuencias.get(letra);

            if (count == null || count <= 0) {
                return false;
            }
            frecuencias.put(letra, count - 1);
        }
        return true;
    }

    // Metodo para agregar la palabra a las palabras usadas
    public void agregarPalabrasUsadas(String palabra) {
        palabrasUsadas.add(palabra.toLowerCase());
    }

    // Metodo para validar la entrada segun el modo normal.
    public boolean validarEntrada(Jugador jugador, String respuesta) {
        if (jugador.yaUsoEstaPalabra(respuesta)) {
            return false;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            return false;
        }
        if (!tieneLetrasValidasNormal(respuesta)) {
            return false;
        }
        if (!diccionario.containsKey(respuesta)) {
            return false;
        }
        return true;
    }

    // Metodo para validar la entrada segun el modo experto.
    public boolean validarEntradaExperto(Jugador jugador, String respuesta) {
        if (jugador.yaUsoEstaPalabra(respuesta)) {
            return false;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            return false;
        }
        if (!tieneLetrasValidasExperto(respuesta)) {
            return false;
        }
        if (!diccionario.containsKey(respuesta)) {
            return false;
        }
        return true;
    }

    // Metodo para analizar la respuesta de acuerdo a las reglas del juego y segun el modo normal.
    public void analisisDeRespuestaNormal(Jugador jugador, String respuesta) {
        Interfaz interfaz = new Interfaz();

        if (jugador.yaUsoEstaPalabra(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Ya usaste esa palabra en esta ronda");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Esa palabra no esta disponible porque ya fue usada por otro jugador!");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }
        if (!tieneLetrasValidasNormal(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Solo puedes usar las letras disponibles");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }

        if (diccionario.containsKey(respuesta)) {
            int puntos = diccionario.get(respuesta);

            jugador.agregarPalabra(respuesta, puntos);
            agregarPalabrasUsadas(respuesta);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraCorrecta(respuesta, puntos, jugador.getPuntuacionTotal());
        } else {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
        }
    }

    // Metodo para analizar la respuesta de acuerdo a las reglas del juego y segun el modo experto.
    public void analisisDeRespuestaExperto(Jugador jugador, String respuesta) {
        Interfaz interfaz = new Interfaz();

        if (jugador.yaUsoEstaPalabra(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Ya usaste esa palabra en esta ronda");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Esa palabra no esta disponible porque fue usada por otro jugador.");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }
        if (!tieneLetrasValidasExperto(respuesta)) {
            interfaz.mostrarPalabraIncorrecta("Solo puedes usar las letras disponibles.");
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
            return;
        }

        if (diccionario.containsKey(respuesta)) {
            int puntos = diccionario.get(respuesta);

            jugador.agregarPalabra(respuesta, puntos);
            agregarPalabrasUsadas(respuesta);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraCorrecta(respuesta, puntos, jugador.getPuntuacionTotal());
        } else {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            interfaz.mostrarPalabraIncorrecta("La palabra no es válida, tienes -5 puntos.");
        }
    }

    // Metodo para formar las palabras ya formadas por los jugadores.
    public void mostrarPalabrasUsadas() {
        final String negritaMorado = "\u001B[1;35m";
        final String reset = "\u001B[0m";

        if (palabrasUsadas.isEmpty()) {
            System.out.println(negritaMorado + "\n\nNo hay palabras usadas" + reset);
            return;
        }

        System.out.println(negritaMorado + "\n\nPalabras usadas y sus puntuaciones:" + reset);

        palabrasUsadas.stream()
                .sorted()
                .forEach(palabra -> {
                    Integer puntuacion = diccionario.getOrDefault(palabra, null);
                    if (puntuacion != null) {
                        System.out.println(palabra + " ---> " + puntuacion);
                    } else {
                        System.out.println(palabra + " -> Sin puntuación");
                    }
                });
    }


    // Metodo para jugar una ronda del juego del Mago De Las Palabras.
    public void jugarRondaDePalabras(Scanner scanner, List <Jugador> jugadores, int modoDeJuego,int NumeroDeRonda) {
        Interfaz interfaz = new Interfaz();
        boolean rondaActiva = true;

        palabrasUsadas.clear();
        jugadores.forEach(jugador -> jugador.reiniciarPalabrasUsadas());

        HashSet<Character> letras;

        if (modoDeJuego == 1) {
             letras = generarLetrasAleatoriasNormal();
        } else {
             letras = generarLetrasAleatoriasExperto();
        }

        agregarLetrasEnJuego(letras);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        interfaz.mostrarInformacionDelModo(modoDeJuego);

        if (modoDeJuego == 1) {
            int pasesConsecutivos = 0;
            while (rondaActiva) {
                rondaActiva = false;

                Iterator<Jugador> iterator = jugadores.iterator();
                while (iterator.hasNext()) {
                    Jugador jugador = iterator.next();
                    interfaz.imprimirRonda(NumeroDeRonda, letras);
                    mostrarPalabrasUsadas();
                    boolean noContesto = true;

                    while (noContesto) {
                        interfaz.indicarTurnoDeJugador(jugador);
                        String respuesta = scanner.nextLine().trim().toLowerCase();

                        if (!respuesta.isEmpty()) {
                            if (validarEntrada(jugador, respuesta)) {
                                noContesto = false;
                                pasesConsecutivos = 0;
                                rondaActiva = true;
                            }
                            analisisDeRespuestaNormal(jugador, respuesta);
                        } else {
                            noContesto = false;
                            pasesConsecutivos++;
                            if (pasesConsecutivos >= jugadores.size()) {
                                return;
                            }
                        }
                    }
                    if (!rondaActiva) {
                        break;
                    }
                }
            }
        } else {
            while (rondaActiva) {
                int pasesConsecutivos = 0;
                rondaActiva = false;

                Iterator<Jugador> iterator = jugadores.iterator();
                while (iterator.hasNext()) {
                    Jugador jugador = iterator.next();
                    interfaz.imprimirRonda(NumeroDeRonda, letras);
                    mostrarPalabrasUsadas();
                    boolean noContesto = true;

                    while (noContesto) {
                        interfaz.indicarTurnoDeJugador(jugador);
                        String respuesta = scanner.nextLine().trim().toLowerCase();

                        if (!respuesta.isEmpty()) {
                            if (validarEntradaExperto(jugador, respuesta)) {
                                noContesto = false;
                                pasesConsecutivos = 0;
                                rondaActiva = true;
                            }
                            analisisDeRespuestaExperto(jugador, respuesta);
                        } else {
                            noContesto = false;
                            pasesConsecutivos++;
                            if (pasesConsecutivos >= jugadores.size()) {
                                return;
                            }
                        }
                    }

                    if (!rondaActiva) {
                        break;
                    }
                }
            }
        }
    }

    // Metodo para jugar al Mago de Las palabras.
    public void iniciarMagoDePalabras() {
        Scanner scanner = new Scanner(System.in);
        Interfaz interfaz = new Interfaz();

        interfaz.imprimirTituloDelJuego();

        int numeroDeJugadores = interfaz.solicitarJugadores(scanner);
        List <Jugador> jugadores = new ArrayList <>();

        for (int i = 0; i < numeroDeJugadores; i++) {
            String nombreDeJugador = interfaz.solicitarNombreDeJugador(scanner, i + 1);
            Jugador jugador = new Jugador(nombreDeJugador);
            jugadores.add(jugador);
            puntuaciones.put(nombreDeJugador, 0);
        }

        int modoDeJuego = interfaz.solicitarModoDeJuego(scanner);
        interfaz.limpiarPantalla();

        for (int NumeroDeRonda = 1; NumeroDeRonda <= 3; NumeroDeRonda++) {
            jugarRondaDePalabras(scanner, jugadores, modoDeJuego, NumeroDeRonda);
        }

        interfaz.limpiarPantalla();
        interfaz.imprimirResultados(jugadores);
    }



}
