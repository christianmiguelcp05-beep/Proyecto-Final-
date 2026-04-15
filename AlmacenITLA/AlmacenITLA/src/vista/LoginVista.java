package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VISTA — Pantalla de Login
 * -------------------------------------------------------
 * Permite al usuario autenticarse o navegar al registro.
 * Valida campos vacíos y credenciales incorrectas.
 */
public class LoginVista extends JFrame {

    private JTextField     txtUsuario;
    private JPasswordField txtContrasena;
    private JButton        btnEntrar;
    private JLabel         lblRegistrarse;
    private UsuarioDAO     usuarioDAO;

    public LoginVista() {
        usuarioDAO = new UsuarioDAO();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("AlmacénITLA — Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Panel principal con fondo degradado ───────────────────────────────
        JPanel fondo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 80, 160),
                                              0, getHeight(), new Color(10, 30, 80)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(fondo);

        // ── Tarjeta blanca central ─────────────────────────────────────────────
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Estilos.BLANCO);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 228, 245), 1, true),
            new EmptyBorder(35, 40, 35, 40)
        ));
        tarjeta.setMaximumSize(new Dimension(320, 400));

        // Icono / Logo
        JLabel icono = new JLabel("📦", SwingConstants.CENTER);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("AlmacénITLA", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Estilos.TEXTO_PRINCIPAL);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Inicia sesión para continuar", SwingConstants.CENTER);
        subtitulo.setFont(Estilos.PEQUEÑO);
        subtitulo.setForeground(Estilos.TEXTO_SECUNDARIO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        JLabel lblUser = new JLabel("Nombre de usuario");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(Estilos.TEXTO_SECUNDARIO);

        txtUsuario    = Estilos.crearCampo("usuario");
        txtContrasena = Estilos.crearCampoPassword("contraseña");

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(Estilos.TEXTO_SECUNDARIO);

        btnEntrar = Estilos.crearBotonPrimario("Entrar");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(Estilos.GRIS_BORDE);

        lblRegistrarse = new JLabel("¿No tienes cuenta? Regístrate", SwingConstants.CENTER);
        lblRegistrarse.setFont(Estilos.PEQUEÑO);
        lblRegistrarse.setForeground(Estilos.AZUL_PRIMARIO);
        lblRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ensamblar tarjeta
        tarjeta.add(icono);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(subtitulo);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(lblUser);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(txtUsuario);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblPass);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(txtContrasena);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(btnEntrar);
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(sep);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblRegistrarse);

        fondo.add(tarjeta);

        // ── Eventos ────────────────────────────────────────────────────────────
        btnEntrar.addActionListener(e -> iniciarSesion());
        txtContrasena.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) iniciarSesion();
            }
        });
        lblRegistrarse.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                abrirRegistro();
            }
            @Override public void mouseEntered(MouseEvent e) {
                lblRegistrarse.setForeground(Estilos.AZUL_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                lblRegistrarse.setForeground(Estilos.AZUL_PRIMARIO);
            }
        });
    }

    private void iniciarSesion() {
        String usuario    = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        // Validación de campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Debe ingresar su usuario y contraseña.\nSi no está registrado debe registrarse.");
            return;
        }

        Usuario u = usuarioDAO.buscarPorCredenciales(usuario, contrasena);
        if (u == null) {
            mostrarError("Usuario o contraseña incorrectos.");
        } else {
            dispose();
            new PrincipalVista(u).setVisible(true);
        }
    }

    private void abrirRegistro() {
        dispose();
        new RegistroVista(this).setVisible(true);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Llamado desde RegistroVista cuando el usuario cancela el registro */
    public void mostrarDeNuevo() {
        setVisible(true);
    }
}
