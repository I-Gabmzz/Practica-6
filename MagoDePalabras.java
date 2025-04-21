//Se importan las librerias necesarias para la clase
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

//Se declara la clase mago de palabras
public class MagoDePalabras {
    //Se declaran los atributos de la clase que se usaran
    private Random random;
    private HashMap<String, Integer> diccionario;
    private HashMap<String, Integer> puntuaciones;
    private HashSet<String> palabrasUsadas;
    private HashSet<Character> letrasEnJuego;
    private List<Jugador> jugadores;
    private int pasesConsecutivos;
    private int modoDeJuego;
    private int rondaActual;
    private int turnoActual;
    private boolean rondaActiva;
    private JFrame ventanaJuegoActual;
    private JTextArea areaPalabrasUsadas;
    private JTextArea areaPuntuacion;

    //Se crea el constructor de la clase en la que se inicializan todos los atributos y se carga el archivo de palabras
    public MagoDePalabras() {
        random = new Random();
        diccionario = new HashMap<>();
        puntuaciones = new HashMap<>();
        palabrasUsadas = new HashSet<>();
        letrasEnJuego = new HashSet<>();
        cargarArchivo("C:\\Users\\PC OSTRICH\\Practica-5\\palabras.txt");
       // cargarArchivo("C:\\Users\\14321\\IdeaProjects\\Practica-5\\palabras.txt");
    }

    //Metodo para cargar el archivo de palabras y almacenarlas en un HashMap
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

    //Metodo para calcular los puntos, teniendo como parametro la palabra que se desea puntuar
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

    //Metodo que genera las letras de manera aleatoria para el modo de juego normal en el cual no se incluyen letras con acentos
    //en este metodo se reparten letras hasta que toque un set de letras con la cual se puedan formar como minimo dos palabras
    private HashSet<Character> generarLetrasAleatoriasNormal() {
        HashSet<Character> letras = new HashSet<>();

        do {
            letras.clear();
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

        } while (!sePuedenFormarPalabras(letras));

        return letras;
    }

    //Metodo que genera las letras de manera aleatoria para el modo de juego experto en el cual se incluyen letras con acentos
    //en este metodo se reparten letras hasta que toque un set de letras con la cual se puedan formar como minimo dos palabras
    public HashSet<Character> generarLetrasAleatoriasExperto() {
        HashSet<Character> letras = new HashSet<>();

        do {
            letras.clear();
            String vocales = "aeiouáéíóú";
            String consonantes = "bcdfghjklmnpqrstvwxyz";

            while (letras.size() < 4) {
                char vocal = vocales.charAt(random.nextInt(vocales.length()));
                letras.add(vocal);
            }
            while (letras.size() < 10) {
                char consonante = consonantes.charAt(random.nextInt(consonantes.length()));
                letras.add(consonante);
            }

        } while (!sePuedenFormarPalabras(letras));

        return letras;
    }

    //Metodo para determinar si se pueden formar dos palabras en el cual se itera por cada palabra que haya en el diccionario de palabras
    //comprobando que se pueda formar una palabra con las letras disponibles del set, despues se evalua que se pueda formar una palabra distinta a la primera
    //para poder verificar que se puedan formar como minimo dos palabras con el set de letras dadas
    private boolean sePuedenFormarPalabras(HashSet<Character> letras) {
        List<Character> letrasDisponibles = new ArrayList<>(letras);
        String primeraPalabra = null;

        for (String palabra : diccionario.keySet()) {
            if (palabraValidaConLetras(palabra, letrasDisponibles)) {
                primeraPalabra = palabra;
                break;
            }
        }

        if (primeraPalabra == null) {
            return false;
        }

        List<Character> letrasRestantes = new ArrayList<>(letras);
        for (char c : primeraPalabra.toCharArray()) {
            letrasRestantes.remove((Character) c);
        }

        for (String palabra : diccionario.keySet()) {
            if (palabraValidaConLetras(palabra, letrasRestantes)) {
                return true;
            }
        }

        return false;
    }
    //Metodo para validar si una palabra es valida con las letras del set que se repartieron
    private boolean palabraValidaConLetras(String palabra, List<Character> letrasDisponibles) {
        List<Character> letrasTemp = new ArrayList<>(letrasDisponibles);

        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (!letrasTemp.remove((Character) letra)) {
                return false;
            }
        }
        return true;
    }

    //Metodo para agregar las letras que se repartieron al HashSet de letras que se jugara en la ronda
    public void agregarLetrasEnJuego(HashSet<Character> letrasRonda) {
        letrasEnJuego.clear();
        letrasEnJuego.addAll(letrasRonda);
    }

    //Metodo para verificar que una palabra ya se uso por un jugador
    public boolean yaSeUsoEsaPalabra(String palabra) {
        return palabrasUsadas.contains(palabra.toUpperCase());
    }

    //Metodo para verificar si la palabra tiene letras validas en el modo de juego normal
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

    //Metodo para verificar si la palabra tiene letras validas en el modo de juego experto
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

    //Metodo para agregar la palabra al HashSet de palabras usadas
    public void agregarPalabrasUsadas(String palabra) {
        palabrasUsadas.add(palabra.toLowerCase());
    }

    //Metodo para validar que la respuesta del jugador sea correcta en el modo normal
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

    //Metodo para validar que la respuesta del jugador sea correcta en el modo experto
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

    //Metodo para analizar la respuesta del jugador en el modo normal, se verifica que el jugador no use una palabra que ya haya sido usada,
    // se evalua que la respuesta tenga letras validas y que se encuentre en el diccionario realizando las puntuaciones correspondientes.
    public void analisisDeRespuestaNormal(Jugador jugador, String respuesta) {
        if (jugador.yaUsoEstaPalabra(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Ya usaste esa palabra en esta ronda | -5 puntos.");
            return;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Esa palabra ya fue usada por otro jugador | -5 puntos.");
            return;
        }
        if (!tieneLetrasValidasNormal(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Solo puedes usar las letras disponibles | -5 puntos.");
            return;
        }

        if (diccionario.containsKey(respuesta.toLowerCase())) {
            int puntos = diccionario.get(respuesta.toLowerCase());

            jugador.agregarPalabra(respuesta, puntos);
            agregarPalabrasUsadas(respuesta);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraCorrecta(respuesta, puntos, jugador.getPuntuacionTotal());
        } else {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("La palabra no está en el diccionario | -5 puntos.");
        }
    }

    //Metodo para analizar la respuesta del jugador en el modo experto, se verifica que el jugador no use una palabra que ya haya sido usada,
    // se evalua que la respuesta tenga letras validas y que se encuentre en el diccionario realizando las puntuaciones correspondientes.
    public void analisisDeRespuestaExperto(Jugador jugador, String respuesta) {
        if (jugador.yaUsoEstaPalabra(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Ya usaste esa palabra en esta ronda | -5 puntos.");
            return;
        }
        if (yaSeUsoEsaPalabra(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Esa palabra ya fue usada por otro jugador | -5 puntos.");
            return;
        }
        if (!tieneLetrasValidasExperto(respuesta)) {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("Solo puedes usar las letras disponibles | -5 puntos.");
            return;
        }

        if (diccionario.containsKey(respuesta.toLowerCase())) {
            int puntos = diccionario.get(respuesta.toLowerCase());

            jugador.agregarPalabra(respuesta, puntos);
            agregarPalabrasUsadas(respuesta);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraCorrecta(respuesta, puntos, jugador.getPuntuacionTotal());
        } else {
            jugador.agregarPalabra(respuesta, -5);
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacionTotal());
            InterfazGrafica.mostrarPalabraIncorrecta("La palabra no está en el diccionario. -5 puntos.");
        }
    }

    //Metodo para mostrar las palabras que se han usado hasta el momento en orden alfabetico con su respectivo puntaje
    public void mostrarPalabrasUsadas(JTextArea areaPalabrasUsadas) {
        if (areaPalabrasUsadas != null) {
            areaPalabrasUsadas.setText("");
            List<String> palabrasOrdenadas = palabrasUsadas.stream()
                    .sorted()
                    .collect(Collectors.toList());
            for (String palabra : palabrasOrdenadas) {
                Integer puntuacion = diccionario.getOrDefault(palabra, null);
                if (puntuacion != null) {
                    areaPalabrasUsadas.append("\u2726  " + palabra + "                          | " + puntuacion + "\n");
                } else {
                    areaPalabrasUsadas.append("\u2726  " + palabra + "                          |  Sin puntuación\n");
                }
            }
        }
    }
    //Metodo para mostrar el turno actual del juego mostrando los aspectos fundamentales del juego en la interfaz para que el jugador pueda jugar de manera correcta
    //en este metodo el jugador interactua con los botones, espacios de texto para escribir la palabra, ver la puntuacion de los jugadores y las palabras que se han
    //usado
    private void mostrarTurnoActual() {
        Jugador jugadorActual = jugadores.get(turnoActual);
        ventanaJuegoActual = new JFrame("\uD83C\uDFAE Mago de Palabras - Ronda " + rondaActual + " - Turno de: " + jugadorActual.getNombre());
        ventanaJuegoActual.setSize(800, 600);
        ventanaJuegoActual.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaJuegoActual.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelBanner = new JPanel();
        panelBanner.setLayout(new BoxLayout(panelBanner, BoxLayout.Y_AXIS));
        panelBanner.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel banner = new JLabel(new ImageIcon("banner.gif"));
        panelBanner.add(banner);
        banner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel InformacionDeTurno = new JLabel("Ronda " + rondaActual + "                    Turno de: " + jugadorActual.getNombre());
        InformacionDeTurno.setFont(new Font("N", Font.BOLD, 24));
        InformacionDeTurno.setForeground(new Color(0, 102, 204));
        InformacionDeTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBanner.add(InformacionDeTurno);
        panelPrincipal.add(panelBanner, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel panelPalabrasUsadas = new JPanel(new BorderLayout());
        panelPalabrasUsadas.setPreferredSize(new Dimension(150, 300));

        JLabel tituloPalabrasUsadas = new JLabel("\uD83C\uDFAF Palabras Usadas");
        tituloPalabrasUsadas.setForeground(new Color(0, 102, 204));
        tituloPalabrasUsadas.setFont(new Font("Noto Sans", Font.BOLD, 14));
        tituloPalabrasUsadas.setHorizontalAlignment(JLabel.CENTER);
        panelPalabrasUsadas.add(tituloPalabrasUsadas, BorderLayout.NORTH);

        areaPalabrasUsadas = new JTextArea();
        areaPalabrasUsadas.setEditable(false);
        panelPalabrasUsadas.add(new JScrollPane(areaPalabrasUsadas), BorderLayout.CENTER);

        JPanel panelLetras = new JPanel(new BorderLayout());
        panelLetras.setPreferredSize(new Dimension(400, 300));

        JLabel tituloLetras = new JLabel("Letras Disponibles");
        tituloLetras.setForeground(new Color(0, 102, 204));
        tituloLetras.setFont(new Font("Noto Sans", Font.BOLD, 14));
        tituloLetras.setHorizontalAlignment(JLabel.CENTER);
        panelLetras.add(tituloLetras, BorderLayout.NORTH);

        JLabel labelLetras = new JLabel(String.valueOf(letrasEnJuego), JLabel.CENTER);        labelLetras.setFont(new Font("Noto Sans", Font.BOLD, 24));
        panelLetras.add(labelLetras, BorderLayout.CENTER);

        JPanel panelPuntuacion = new JPanel(new BorderLayout());
        panelPuntuacion.setPreferredSize(new Dimension(150, 300));

        JLabel tituloPuntuacion = new JLabel("\uD83C\uDFC6 Puntuaciones");
        tituloPuntuacion.setForeground(new Color(0, 102, 204));
        tituloPuntuacion.setFont(new Font("Noto Sans", Font.BOLD, 14));
        tituloPuntuacion.setHorizontalAlignment(JLabel.CENTER);
        panelPuntuacion.add(tituloPuntuacion, BorderLayout.NORTH);

        areaPuntuacion = new JTextArea();
        areaPuntuacion.setEditable(false);
        panelPuntuacion.add(new JScrollPane(areaPuntuacion), BorderLayout.CENTER);

        panelSuperior.add(panelPalabrasUsadas);
        panelSuperior.add(Box.createRigidArea(new Dimension(15, 0)));
        panelSuperior.add(panelLetras);
        panelSuperior.add(Box.createRigidArea(new Dimension(15, 0)));
        panelSuperior.add(panelPuntuacion);

        JPanel panelEscritura = new JPanel(new BorderLayout());
        panelEscritura.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel tituloEscritura = new JLabel("Ingresa tu palabra \u270D\uFE0F");
        tituloEscritura.setForeground(new Color(0, 102, 204));
        tituloEscritura.setFont(new Font("Noto Sans", Font.BOLD, 14));
        tituloEscritura.setHorizontalAlignment(JLabel.CENTER);
        panelEscritura.add(tituloEscritura, BorderLayout.NORTH);
        panelEscritura.add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.CENTER);

        JTextField campoEscritura = new JTextField();
        campoEscritura.setFont(new Font("Noto Sans", Font.PLAIN, 18));
        panelEscritura.add(campoEscritura, BorderLayout.SOUTH);

        panelCentral.add(panelSuperior, BorderLayout.CENTER);
        panelCentral.add(panelEscritura, BorderLayout.SOUTH);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton botonPuntuar = new JButton("Puntuar Palabra");
        JButton botonPasarTurno = new JButton("Pasar Turno");
        JButton botonRegistrar = new JButton("Registrar Palabra");

        Font fuenteBotones = new Font("Noto Sans", Font.BOLD, 16);
        botonPuntuar.setFont(fuenteBotones);
        botonPuntuar.setBackground(new Color(220, 220, 220));
        botonPuntuar.setBorder(BorderFactory.createEtchedBorder());

        botonPasarTurno.setFont(fuenteBotones);
        botonPasarTurno.setBackground(new Color(220, 220, 220));
        botonPasarTurno.setBorder(BorderFactory.createEtchedBorder());

        botonRegistrar.setFont(fuenteBotones);
        botonRegistrar.setBackground(new Color(220, 220, 220));
        botonRegistrar.setBorder(BorderFactory.createEtchedBorder());

        panelBotones.add(botonPuntuar);
        panelBotones.add(botonPasarTurno);
        panelBotones.add(botonRegistrar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        botonPuntuar.addActionListener(e -> {
            String respuesta = campoEscritura.getText().trim().toLowerCase();
            if (!respuesta.isEmpty()) {
                procesarRespuesta(jugadorActual, respuesta);
                campoEscritura.setText("");
            } else {
                JOptionPane.showMessageDialog(ventanaJuegoActual, "Debe ingresar una palabra", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        campoEscritura.addActionListener(e -> botonPuntuar.doClick());


        botonPasarTurno.addActionListener(e -> {
            pasarTurno();
            if (rondaActiva) {
                pasarAlSiguienteJugador();
            }
        });

        botonRegistrar.addActionListener(e -> {
            String respuesta = campoEscritura.getText().trim().toLowerCase();
            if (!respuesta.isEmpty()) {
                registrarPalabra(jugadorActual, respuesta);
                procesarRespuesta(jugadorActual, respuesta);
                campoEscritura.setText("");
            } else {
                JOptionPane.showMessageDialog(ventanaJuegoActual, "Debe ingresar una palabra", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        actualizarInterfaz();
        ventanaJuegoActual.add(panelPrincipal);
        ventanaJuegoActual.setVisible(true);
    }
    //Metodo para pasar turno, si el pase de turnos es consecutivo en todos los jugadores, se termina la ronda y comienza una nueva
    private void pasarTurno() {
        pasesConsecutivos++;
        if (pasesConsecutivos >= jugadores.size()) {
            ventanaJuegoActual.dispose();
            rondaActiva = false;
            JOptionPane.showMessageDialog(ventanaJuegoActual, "\uD83D\uDCE2 Ronda terminada por pases consecutivos", "Pase de jugador", JOptionPane.INFORMATION_MESSAGE);
            turnoActual = (turnoActual - 1) % jugadores.size();
        }
    }
    //Metodo para que se cambie de turno
    private void pasarAlSiguienteJugador() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        ventanaJuegoActual.dispose();
        mostrarTurnoActual();
    }
    //Metodo para actualizar la interfaz
    private void actualizarInterfaz() {
        mostrarPalabrasUsadas(areaPalabrasUsadas);

        StringBuilder sb = new StringBuilder();
        for (Jugador jugador : jugadores) {
            sb.append("\u27A4  " + jugador.getNombre()).append(":   ").append(jugador.getPuntuacionTotal()).append("   Puntos \n");
        }
        if (areaPuntuacion != null) {
            areaPuntuacion.setText(sb.toString());
        }
    }
    //Metodo que procesa la respuesta del jugador dependiendo el modo de juego en el que se encuentre, en el cual se valida la respuesta dada.
    private void procesarRespuesta(Jugador jugador, String respuesta) {
        if (modoDeJuego == 1) {
            analisisDeRespuestaNormal(jugador, respuesta);
        } else {
            analisisDeRespuestaExperto(jugador, respuesta);
        }
        pasesConsecutivos = 0;
        jugador.eliminarPalabra(respuesta, 0);
        palabrasUsadas.remove(respuesta);
        if (validarEntrada(jugador, respuesta)) {
            jugador.agregarPalabra(respuesta, 0);
            palabrasUsadas.add(respuesta);
            pasarAlSiguienteJugador();
        } else if (validarEntradaExperto(jugador, respuesta)) {
            jugador.agregarPalabra(respuesta, 0);
            palabrasUsadas.add(respuesta);
            pasarAlSiguienteJugador();
        }
        actualizarInterfaz();
    }
    //Metodo para registrar una nueva palabra en el diccionario de palabras si es que no se encontraba anteriormente
    private void registrarPalabra(Jugador jugador, String respuesta) {
        try (FileWriter escritor = new FileWriter("C:\\Users\\PC OSTRICH\\Practica-6\\palabras.txt", true)) {
            escritor.write(respuesta + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cargarArchivo("C:\\Users\\PC OSTRICH\\Practica-6\\palabras.txt");
        jugador.eliminarPalabra(respuesta,+5);
    }
    //Metodo para jugar una ronda del juego en el cual se inicializan los jugadores,
    //se juega dependiendo el modo de juego y el numero de ronda en el que se encuentre el juego
    public void jugarRondaDePalabras(List<Jugador> jugadores, int modoDeJuego, int numeroDeRonda) {
        this.jugadores = jugadores;
        this.modoDeJuego = modoDeJuego;
        this.rondaActual = numeroDeRonda;
        this.turnoActual = 0;
        this.pasesConsecutivos = 0;
        this.rondaActiva = true;

        palabrasUsadas.clear();
        jugadores.forEach(jugador -> jugador.reiniciarPalabrasUsadas());

        HashSet<Character> letras = (modoDeJuego == 1) ?
                generarLetrasAleatoriasNormal() : generarLetrasAleatoriasExperto();
        agregarLetrasEnJuego(letras);

        mostrarTurnoActual();
    }

    //Metodo en donde se incia el juego, preguntando el numero de jugadores, nombre y modo, para despues jugar el juego
    public void iniciarMagoDePalabras() {
        InterfazGrafica.menuInicial();
        jugadores = new ArrayList<>();

        int numeroDeJugadores = InterfazGrafica.solicitarJugadores();

        for (int i = 0; i < numeroDeJugadores; i++) {
            String nombreDeJugador;
            boolean nombreValido = false;

            while (!nombreValido) {
                nombreDeJugador = InterfazGrafica.solicitarNombreDeJugador(i + 1);
                nombreValido = true;

                for (Jugador jugador : jugadores) {
                    if (jugador.getNombre().equalsIgnoreCase(nombreDeJugador)) {
                        InterfazGrafica.mostrarMensajeError();
                        nombreValido = false;
                        break;
                    }
                }
                if (nombreValido && !nombreDeJugador.trim().isEmpty()) {
                    Jugador jugador = new Jugador(nombreDeJugador);
                    jugadores.add(jugador);
                    puntuaciones.put(nombreDeJugador, 0);
                }
            }
        }

        modoDeJuego = InterfazGrafica.solicitarModoDeJuego();
        for (rondaActual = 1; rondaActual <= 3; rondaActual++) {
            jugarRondaDePalabras(jugadores, modoDeJuego, rondaActual);

            while (rondaActiva) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mostrarResultadosFinales();
    }

    //Metodo para mostrar el ganador del juego, mostrar puntuaciones finales y un mensaje de despedida
    private void mostrarResultadosFinales() {
        ventanaJuegoActual.dispose();

        JFrame frameFinal = new JFrame("\uD83C\uDFC6 Resultados Finales - Mago de Palabras");
        frameFinal.setSize(400, 600);
        frameFinal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameFinal.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel banner = new JLabel(new ImageIcon("C:\\Users\\14321\\Downloads\\Practica-5-main\\bannerFinal.gif"));
        banner.setHorizontalAlignment(JLabel.CENTER);
        panel.add(banner, BorderLayout.NORTH);

        jugadores.sort((j1, j2) -> Integer.compare(j2.getPuntuacionTotal(), j1.getPuntuacionTotal()));

        JPanel panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titulo = new JLabel("\uD83C\uDFC6 Puntuaciones Finales");
        titulo.setFont(new Font("Noto Sans", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 204));
        panelTitulo.add(titulo);
        panelResultados.add(panelTitulo);
        panelResultados.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            JPanel panelJugador = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel labelJugador = new JLabel(
                    (i == 0 ? "\uD83E\uDD47 " : i == 1 ? "\uD83E\uDD48 " : i == 2 ? "\uD83E\uDD49 " : "▸ ") +
                            jugador.getNombre() + ": " + jugador.getPuntuacionTotal() + " puntos"
            );
            labelJugador.setFont(new Font("Noto Sans", Font.PLAIN, 16));
            panelJugador.add(labelJugador);
            panelResultados.add(panelJugador);
            panelResultados.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panel.add(panelResultados, BorderLayout.CENTER);

        JButton botonSalir = new JButton("Salir del Juego");

        botonSalir.setFont(new Font("Noto Sans", Font.BOLD, 16));
        botonSalir.setFocusPainted(false);
        botonSalir.setBackground(new Color(220, 220, 220));
        botonSalir.setBorder(BorderFactory.createEtchedBorder());
        botonSalir.addActionListener(e -> System.exit(0));

        frameFinal.getRootPane().setDefaultButton(botonSalir);

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonSalir);
        panel.add(panelBoton, BorderLayout.SOUTH);

        frameFinal.add(panel);
        frameFinal.setVisible(true);
        ventanaJuegoActual = frameFinal;
    }

}
