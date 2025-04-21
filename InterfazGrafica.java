import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class InterfazGrafica {
    private static boolean juegoIniciado = false;


    public static void menuInicial() {
        JFrame ventana = new JFrame("Mago De Palabras");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(550, 700);
        ventana.setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelDeTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon imagenIcono = new ImageIcon("C:\\Users\\14321\\Downloads\\Practica-5-main\\PantallaInicial.gif");
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


}
