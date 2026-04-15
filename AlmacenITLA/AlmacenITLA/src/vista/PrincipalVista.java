package vista;

import modelo.Usuario;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * VISTA — Pantalla Principal
 * -------------------------------------------------------
 * Muestra los dos módulos: Gestión de Usuarios y Gestión de Productos.
 * También incluye el botón de Cerrar Sesión.
 */
public class PrincipalVista extends JFrame {

    private Usuario usuarioActual;

    public PrincipalVista(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("AlmacénITLA — Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Panel principal ────────────────────────────────────────────────────
        JPanel fondo = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Estilos.GRIS_FONDO,
                                              0, getHeight(), new Color(215, 225, 245)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondo.setBorder(new EmptyBorder(24, 32, 24, 32));
        setContentPane(fondo);

        // ── Encabezado ─────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel bienvenida = new JLabel("Bienvenido, " + usuarioActual.getNombre() + " 👋");
        bienvenida.setFont(Estilos.TITULO);
        bienvenida.setForeground(Estilos.TEXTO_PRINCIPAL);

        JLabel sub = new JLabel("Selecciona un módulo para continuar");
        sub.setFont(Estilos.PEQUEÑO);
        sub.setForeground(Estilos.TEXTO_SECUNDARIO);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(bienvenida);
        textos.add(Box.createVerticalStrut(3));
        textos.add(sub);

        JButton btnCerrar = Estilos.crearBotonPeligro("Cerrar Sesión");
        btnCerrar.addActionListener(e -> cerrarSesion());

        header.add(textos, BorderLayout.CENTER);
        header.add(btnCerrar, BorderLayout.EAST);

        // ── Tarjetas de módulos ────────────────────────────────────────────────
        JPanel tarjetas = new JPanel(new GridLayout(1, 2, 24, 0));
        tarjetas.setOpaque(false);
        tarjetas.setBorder(new EmptyBorder(30, 0, 0, 0));

        tarjetas.add(crearTarjetaModulo("👥", "Gestión de Usuarios",
            "Administra los usuarios\nregistrados en el sistema.",
            () -> new GestionUsuariosVista(this).setVisible(true)));

        tarjetas.add(crearTarjetaModulo("📦", "Gestión de Productos",
            "Administra el inventario\nde productos del almacén.",
            () -> new GestionProductosVista(this).setVisible(true)));

        fondo.add(header,   BorderLayout.NORTH);
        fondo.add(tarjetas, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaModulo(String emoji, String titulo, String descripcion, Runnable accion) {
        JPanel tarjeta = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Estilos.BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Estilos.GRIS_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(30, 28, 30, 28));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(emoji, SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(Estilos.SUBTITULO);
        lbl.setForeground(Estilos.TEXTO_PRINCIPAL);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><center>" + descripcion.replace("\n", "<br>") + "</center></html>");
        desc.setFont(Estilos.PEQUEÑO);
        desc.setForeground(Estilos.TEXTO_SECUNDARIO);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = Estilos.crearBotonPrimario("Abrir módulo");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> accion.run());

        tarjeta.add(ico);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lbl);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(desc);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(btn);

        // Hover en la tarjeta también abre el módulo
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { accion.run(); }
        });

        return tarjeta;
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas cerrar sesión?",
            "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginVista().setVisible(true);
        }
    }
}
