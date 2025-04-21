// Se importan las librerias necesarias para la clase.
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Se crea la clase MagoDePalabras.
public class MagoDePalabras {
    // Se declaran los atributos de la clase.
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

    //Metodo para generar letras aleatorias segun el modo experto.
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

    // Metodo para analizar la respuesta de acuerdo a las reglas del juego y segun el modo experto.
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

    // Metodo para formar las palabras ya formadas por los jugadores.
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

    private void pasarTurno() {
        pasesConsecutivos++;
        if (pasesConsecutivos >= jugadores.size()) {
            ventanaJuegoActual.dispose();
            rondaActiva = false;
            JOptionPane.showMessageDialog(ventanaJuegoActual, "\uD83D\uDCE2 Ronda terminada por pases consecutivos", "Pase de jugador", JOptionPane.INFORMATION_MESSAGE);
            turnoActual = (turnoActual - 1) % jugadores.size();
        }
    }

    private void pasarAlSiguienteJugador() {
        turnoActual = (turnoActual + 1) % jugadores.size();
        ventanaJuegoActual.dispose();
        mostrarTurnoActual();
    }

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

    private void registrarPalabra(Jugador jugador, String respuesta) {
        try (FileWriter escritor = new FileWriter("C:\\Users\\PC OSTRICH\\Practica-6\\palabras.txt", true)) {
            escritor.write(respuesta + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cargarArchivo("C:\\Users\\PC OSTRICH\\Practica-6\\palabras.txt");
        jugador.eliminarPalabra(respuesta,+5);
    }

    public void iniciarMagoDePalabras() {

    }



}
