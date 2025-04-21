//Se importan las librerias necesarias para la clase
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class InterfazGrafica {
    private static boolean juegoIniciado = false;

    //Metodo para mostrar el menu inicial del juego, en el cual se muestra el titulo del juego, una gift animado y distintos botones para que el usuario
    //pueda interactuar
    public static void menuInicial() {
        JFrame ventana = new JFrame("Mago De Palabras");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(550, 700);
        ventana.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelDeTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon imagenIcono = new ImageIcon("C:\\Users\\PC OSTRICH\\Practica-6\\PantallaInicial.gif");
       // ImageIcon imagenIcono = new ImageIcon("C:\\Users\\14321\\Downloads\\Practica-5-main\\PantallaInicial.gif");
        JLabel labelImagen = new JLabel(imagenIcono);
        panelDeTitulo.add(labelImagen);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelBienvenida = new JLabel("Bienvenido a Mago De Palabras");
        labelBienvenida.setFont(new Font("Noto Sans", Font.BOLD, 24));
        labelBienvenida.setForeground(new Color(0, 102, 204));
        panelCentro.add(labelBienvenida);

        JPanel panelDeAcciones = new JPanel(new GridLayout(1, 3, 15, 15));
        JButton botonJugar = new JButton(" ▶ Jugar");
        JButton botonCreditos = new JButton(" \uD83D\uDC64 Créditos");
        JButton botonSalir = new JButton(" \uD83D\uDEAA Salir");

        Font fuenteBotones = new Font("Noto Sans", Font.BOLD, 18);
        Color colorBoton = new Color(220, 220, 220);

        Stream.of(botonJugar, botonCreditos, botonSalir)
                .forEach(boton -> {
                    boton.setFont(fuenteBotones);
                    boton.setBackground(colorBoton);
                    boton.setFocusPainted(false);
                });

        panelDeAcciones.add(botonJugar);
        panelDeAcciones.add(botonCreditos);
        panelDeAcciones.add(botonSalir);

        panelPrincipal.add(panelDeTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelDeAcciones, BorderLayout.SOUTH);

        ventana.add(panelPrincipal);
        ventana.setVisible(true);

        botonJugar.addActionListener(e -> {
            juegoIniciado = true;
            ventana.dispose();
        });

        botonCreditos.addActionListener(e -> {
            mostrarCreditos();
        });

        ventana.getRootPane().setDefaultButton(botonJugar);

        botonSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Estás seguro que quieres salir del juego?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        ventana.setVisible(true);
        while (!juegoIniciado) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Metodo para mostrar los creditos, es decir quien desarrollo el programa, se muestra una pequeña ventana con la informacion correspondiente
    public static void mostrarCreditos() {
        JPanel panelCreditos = new JPanel(new BorderLayout(10, 10));
        panelCreditos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Créditos", SwingConstants.CENTER);
        titulo.setFont(new Font("Noto Sans", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 224));
        panelCreditos.add(titulo, BorderLayout.NORTH);

        JTextArea contenido = new JTextArea(
                "Desarrollado por:\n" +
                        "• Diego Erik Alfonso Montoya (1198520)\n" +
                        "• Angel Gabriel Manjarrez Moreno (1197503)\n\n" +
                        "Versión: 21/04/2025\n" +
                        "© Todos los derechos reservados"
        );
        contenido.setFont(new Font("Noto Sans", Font.PLAIN, 14));
        contenido.setEditable(false);
        contenido.setBackground(new Color(240, 240, 240));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCreditos.add(new JScrollPane(contenido), BorderLayout.CENTER);

        Font fuenteBotones = new Font("Noto Sans", Font.BOLD, 14);
        Color colorBoton = new Color(220, 220, 220);

        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> ((Window) SwingUtilities.getWindowAncestor((Component) e.getSource())).dispose());
        cerrar.setFont(fuenteBotones);
        cerrar.setBackground(colorBoton);
        cerrar.setFocusPainted(false);

        JPanel panelBoton = new JPanel();
        panelBoton.add(cerrar);
        panelCreditos.add(panelBoton, BorderLayout.SOUTH);

        JDialog creditos = new JDialog();
        creditos.setTitle("Créditos");
        creditos.setModal(true);
        creditos.setResizable(true);
        creditos.setContentPane(panelCreditos);
        creditos.pack();
        creditos.setLocationRelativeTo(null);
        creditos.setVisible(true);
    }

    //Metodo para solitar cuantos jugadores jugaran, en el cual se muestran 3 botones para la cantidad de jugadores que se desee
    public static int solicitarJugadores() {
        AtomicInteger numeroDeJugadores = new AtomicInteger();

        JDialog jugadores = new JDialog();
        jugadores.setTitle("Número de Jugadores");
        jugadores.setSize(500, 150);
        jugadores.setLocationRelativeTo(null);
        jugadores.setModal(true);
        jugadores.setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Seleccione el número de jugadores \uD83D\uDC65", SwingConstants.CENTER);
        titulo.setFont(new Font("Noto Sans", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 204));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 15, 15));

        JButton boton2 = new JButton(" 2 ");
        JButton boton3 = new JButton(" 3 ");
        JButton boton4 = new JButton(" 4 ");

        Font fuenteBotones = new Font("Noto Sans", Font.BOLD, 18);
        Color colorBoton = new Color(220, 220, 220);

        Stream.of(boton2, boton3, boton4).forEach(boton -> {
            boton.setFont(fuenteBotones);
            boton.setBackground(colorBoton);
            boton.setFocusPainted(false);
        });

        boton2.addActionListener(e -> {
            numeroDeJugadores.set(2);
            jugadores.dispose();
        });

        boton3.addActionListener(e -> {
            numeroDeJugadores.set(3);
            jugadores.dispose();
        });

        boton4.addActionListener(e -> {
            numeroDeJugadores.set(4);
            jugadores.dispose();
        });

        panelBotones.add(boton2);
        panelBotones.add(boton3);
        panelBotones.add(boton4);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        jugadores.add(panelPrincipal);
        jugadores.setVisible(true);

        return numeroDeJugadores.get();
    }

    //Metodo para solicitar el nombre de cada jugador dependidendo de la cantidad de jugadores que se hayan ingresado
    public static String solicitarNombreDeJugador(int numDeJugador) {
        JDialog nombreJugador = new JDialog();
        nombreJugador.setTitle("Nombre del Jugador");
        nombreJugador.setSize(500, 200);
        nombreJugador.setLocationRelativeTo(null);
        nombreJugador.setModal(true);
        nombreJugador.setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Ingrese el nombre del jugador " + numDeJugador + "\uD83D\uDC64", SwingConstants.CENTER);
        titulo.setFont(new Font("Noto Sans", Font.BOLD, 18));
        titulo.setForeground(new Color(0, 102, 204));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JTextField campoNombre = new JTextField();
        campoNombre.setFont(new Font("Noto Sans", Font.PLAIN, 16));
        campoNombre.setHorizontalAlignment(JTextField.CENTER);
        panelPrincipal.add(campoNombre, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        JButton botonAceptar = new JButton("Aceptar");

        botonAceptar.setFont(new Font("Noto Sans", Font.BOLD, 18));
        botonAceptar.setBackground(new Color(220, 220, 220));
        botonAceptar.setFocusPainted(false);

        AtomicReference<String> nombre = new AtomicReference<>("");

        botonAceptar.addActionListener(e -> {
            if (!campoNombre.getText().trim().isEmpty()) {
                nombre.set(campoNombre.getText().trim());
                nombreJugador.dispose();
            } else {
                JOptionPane.showMessageDialog(nombreJugador, "Debe ingresar un nombre válido", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        campoNombre.addActionListener(e -> botonAceptar.doClick());

        panelBoton.add(botonAceptar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        nombreJugador.add(panelPrincipal);
        nombreJugador.setVisible(true);

        return nombre.get();
    }

    //Metodo para solicitar el modo de juego, el usuario puede interactuar con 2 botones segun el modo de juego que desee
    //tambien se muestra una pequeña descripcion de cada modo de juego
    public static int solicitarModoDeJuego() {
        AtomicInteger modoSeleccionado = new AtomicInteger(0);

        JDialog modo = new JDialog();
        modo.setTitle("Modo de Juego");
        modo.setSize(450, 250);
        modo.setLocationRelativeTo(null);
        modo.setModal(true);
        modo.setResizable(true);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("MODOS DE JUEGO", SwingConstants.CENTER);
        titulo.setFont(new Font("Noto Sans", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 204));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 15, 15));

        JButton botonNormal = new JButton(" Normal ");
        JButton botonExperto = new JButton(" Experto ");

        Font fuenteBotones = new Font("Noto Sans", Font.BOLD, 18);
        Color colorBoton = new Color(220, 220, 220);

        Stream.of(botonNormal, botonExperto).forEach(boton -> {
            boton.setFont(fuenteBotones);
            boton.setBackground(colorBoton);
            boton.setFocusPainted(false);
        });

        UIManager.put("ToolTip.background", new Color(255, 255, 205));
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.font", new Font("Noto Sans", Font.PLAIN, 14));

        botonNormal.setToolTipText("<html>Reglas del modo normal:<br> • Vocales sin acentos<br> • Letras repetibles</html>");
        botonExperto.setToolTipText("<html>Reglas del modo experto:<br> • Vocales con acentos<br> • Letras no repetibles</html>");

        botonNormal.addActionListener(e -> {
            modoSeleccionado.set(1);
            modo.dispose();
        });

        botonExperto.addActionListener(e -> {
            modoSeleccionado.set(2);
            modo.dispose();
        });

        panelBotones.add(botonNormal);
        panelBotones.add(botonExperto);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        modo.add(panelPrincipal);
        modo.setVisible(true);

        return modoSeleccionado.get();
    }

    // Metodo para mostrar un aviso acerca de que la palabra escrita es correcta
    public static void mostrarPalabraCorrecta(String aviso, int puntuacion, int puntosTotales) {
        String mensaje = "La palabra es correcta | " + aviso + " | -----> + " + puntuacion
                + " puntos \u2705 | (Puntos Totales: " + puntosTotales + ")";
        JOptionPane.showMessageDialog(null, mensaje, "Correcto", JOptionPane.INFORMATION_MESSAGE);
    }

    // Metodo para mostrar un aviso acerca de que la palabra escrita es incorrecta
    public static void mostrarPalabraIncorrecta(String aviso) {
        JOptionPane.showMessageDialog(null, "\u274C " + aviso, "Incorrecto", JOptionPane.ERROR_MESSAGE);
    }

    // Metodo para avisar que un nombre se duplico y no es valido.
    public static void mostrarMensajeError(){
        JDialog nombreIncorrecto = new JDialog();
        JOptionPane.showMessageDialog(nombreIncorrecto, "No puede ingresar nombres duplicados", "Error", JOptionPane.WARNING_MESSAGE);
    }


}
