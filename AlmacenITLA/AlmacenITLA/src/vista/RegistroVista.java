package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * VISTA — Pantalla de Registro de Usuarios
 * -------------------------------------------------------
 * Todos los campos son obligatorios. Valida que las contraseñas
 * coincidan y que el username no esté ya registrado.
 */
public class RegistroVista extends JFrame {

    private JTextField     txtNombre, txtApellido, txtTelefono, txtEmail, txtUsuario;
    private JPasswordField txtContrasena, txtConfirmar;
    private JButton        btnRegistrar, btnVolver;
    private UsuarioDAO     usuarioDAO;
    private LoginVista     loginRef;

    public RegistroVista(LoginVista loginRef) {
        this.loginRef  = loginRef;
        this.usuarioDAO = new UsuarioDAO();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("AlmacénITLA — Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Fondo degradado ────────────────────────────────────────────────────
        JPanel fondo = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(30, 80, 160),
                                              0, getHeight(), new Color(10, 30, 80)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(fondo);

        // ── Tarjeta blanca ─────────────────────────────────────────────────────
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Estilos.BLANCO);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 228, 245), 1, true),
            new EmptyBorder(30, 36, 30, 36)
        ));

        JLabel titulo = new JLabel("Crear Cuenta");
        titulo.setFont(Estilos.TITULO);
        titulo.setForeground(Estilos.TEXTO_PRINCIPAL);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Todos los campos son obligatorios");
        sub.setFont(Estilos.PEQUEÑO);
        sub.setForeground(Estilos.TEXTO_SECUNDARIO);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre    = Estilos.crearCampo("Ej: Juan");
        txtApellido  = Estilos.crearCampo("Ej: Pérez");
        txtTelefono  = Estilos.crearCampo("Ej: 809-555-1234");
        txtEmail     = Estilos.crearCampo("Ej: juan@email.com");
        txtUsuario   = Estilos.crearCampo("Ej: juanperez");
        txtContrasena = Estilos.crearCampoPassword("Mínimo 6 caracteres");
        txtConfirmar  = Estilos.crearCampoPassword("Repite la contraseña");

        btnRegistrar = Estilos.crearBotonPrimario("Crear Cuenta");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        btnVolver = Estilos.crearBotonSecundario("← Volver al Login");
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Ensamblar
        agregarFila(tarjeta, "Nombre",              txtNombre);
        agregarFila(tarjeta, "Apellido",            txtApellido);
        agregarFila(tarjeta, "Teléfono",            txtTelefono);
        agregarFila(tarjeta, "Correo Electrónico",  txtEmail);
        agregarFila(tarjeta, "Nombre de Usuario",   txtUsuario);
        agregarFila(tarjeta, "Contraseña",          txtContrasena);
        agregarFila(tarjeta, "Confirmar Contraseña",txtConfirmar);

        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(sub);
        tarjeta.add(Box.createVerticalStrut(20));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setBackground(Estilos.BLANCO);
        agregarFilaA(campos, "Nombre",              txtNombre);
        agregarFilaA(campos, "Apellido",            txtApellido);
        agregarFilaA(campos, "Teléfono",            txtTelefono);
        agregarFilaA(campos, "Correo Electrónico",  txtEmail);
        agregarFilaA(campos, "Nombre de Usuario",   txtUsuario);
        agregarFilaA(campos, "Contraseña",          txtContrasena);
        agregarFilaA(campos, "Confirmar Contraseña",txtConfirmar);

        tarjeta.add(campos);
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(btnRegistrar);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnVolver);

        fondo.add(tarjeta);

        // ── Eventos ────────────────────────────────────────────────────────────
        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> volver());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { volver(); }
        });
    }

    private void agregarFila(JPanel panel, String label, JComponent campo) { /* sin uso */ }

    private void agregarFilaA(JPanel panel, String label, JComponent campo) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(Estilos.TEXTO_SECUNDARIO);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(3));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(10));
    }

    private void registrar() {
        String nombre    = txtNombre.getText().trim();
        String apellido  = txtApellido.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String usuario   = txtUsuario.getText().trim();
        String pass      = new String(txtContrasena.getPassword()).trim();
        String confirmar = new String(txtConfirmar.getPassword()).trim();

        // Validar campos obligatorios
        if (nombre.isEmpty())    { error("El campo 'Nombre' es obligatorio.");             return; }
        if (apellido.isEmpty())  { error("El campo 'Apellido' es obligatorio.");           return; }
        if (telefono.isEmpty())  { error("El campo 'Teléfono' es obligatorio.");           return; }
        if (email.isEmpty())     { error("El campo 'Correo Electrónico' es obligatorio."); return; }
        if (usuario.isEmpty())   { error("El campo 'Nombre de Usuario' es obligatorio.");  return; }
        if (pass.isEmpty())      { error("El campo 'Contraseña' es obligatorio.");         return; }
        if (confirmar.isEmpty()) { error("Debes confirmar tu contraseña.");                return; }

        // Validar coincidencia de contraseñas
        if (!pass.equals(confirmar)) {
            error("La contraseña y la confirmación no coinciden.");
            return;
        }

        // Validar que el username no exista
        if (usuarioDAO.existeUsuario(usuario)) {
            error("El nombre de usuario '" + usuario + "' ya está registrado.");
            return;
        }

        Usuario u = new Usuario(usuario, nombre, apellido, telefono, email, pass);
        if (usuarioDAO.insertar(u)) {
            JOptionPane.showMessageDialog(this,
                "¡Cuenta creada exitosamente!\nYa puedes iniciar sesión.",
                "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            volver();
        } else {
            error("Error al registrar. Intenta de nuevo.");
        }
    }

    private void volver() {
        dispose();
        loginRef.mostrarDeNuevo();
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
}
