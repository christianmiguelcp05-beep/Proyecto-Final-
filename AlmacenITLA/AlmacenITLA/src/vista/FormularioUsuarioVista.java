package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * VISTA — Formulario de Usuario (Crear / Editar)
 * -------------------------------------------------------
 * Reutilizable: si recibe un Usuario existente, pre-llena los campos (edición).
 * Si recibe null, opera en modo creación.
 */
public class FormularioUsuarioVista extends JDialog {

    private JTextField     txtNombre, txtApellido, txtTelefono, txtEmail, txtUsuario;
    private JPasswordField txtContrasena;
    private JButton        btnGuardar, btnCancelar;
    private UsuarioDAO     usuarioDAO;
    private Usuario        usuarioEditar;
    private GestionUsuariosVista gestionRef;

    public FormularioUsuarioVista(GestionUsuariosVista gestionRef, Usuario usuario) {
        super(gestionRef, true);
        this.gestionRef    = gestionRef;
        this.usuarioDAO    = new UsuarioDAO();
        this.usuarioEditar = usuario;
        inicializarUI();
        if (usuario != null) precargar(usuario);
    }

    private void inicializarUI() {
        boolean esEdicion = (usuarioEditar != null);
        setTitle(esEdicion ? "Editar Usuario" : "Nuevo Usuario");
        setSize(400, 480);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Estilos.BLANCO);
        panel.setBorder(new EmptyBorder(24, 30, 24, 30));
        setContentPane(panel);

        JLabel titulo = Estilos.crearTitulo(esEdicion ? "✏ Editar Usuario" : "➕ Nuevo Usuario");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        txtNombre    = Estilos.crearCampo("Nombre");
        txtApellido  = Estilos.crearCampo("Apellido");
        txtTelefono  = Estilos.crearCampo("Teléfono");
        txtEmail     = Estilos.crearCampo("Correo electrónico");
        txtUsuario   = Estilos.crearCampo("Nombre de usuario");
        txtContrasena = Estilos.crearCampoPassword("Contraseña");

        agregarCampo(panel, "Nombre",             txtNombre);
        agregarCampo(panel, "Apellido",            txtApellido);
        agregarCampo(panel, "Teléfono",            txtTelefono);
        agregarCampo(panel, "Correo Electrónico",  txtEmail);
        agregarCampo(panel, "Nombre de Usuario",   txtUsuario);
        agregarCampo(panel, "Contraseña",          txtContrasena);

        panel.add(Box.createVerticalStrut(16));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setOpaque(false);
        btnGuardar  = Estilos.crearBotonPrimario(esEdicion ? "Guardar Cambios" : "Crear Usuario");
        btnCancelar = Estilos.crearBotonSecundario("Cancelar");
        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        panel.add(btnPanel);

        // ── Eventos ────────────────────────────────────────────────────────────
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void agregarCampo(JPanel panel, String label, JComponent campo) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(Estilos.TEXTO_SECUNDARIO);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(3));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(10));
    }

    private void precargar(Usuario u) {
        txtNombre.setText(u.getNombre());
        txtApellido.setText(u.getApellido());
        txtTelefono.setText(u.getTelefono());
        txtEmail.setText(u.getEmail());
        txtUsuario.setText(u.getUserName());
        txtContrasena.setText(u.getPassword());
    }

    private void guardar() {
        String nombre    = txtNombre.getText().trim();
        String apellido  = txtApellido.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String usuario   = txtUsuario.getText().trim();
        String pass      = new String(txtContrasena.getPassword()).trim();

        if (nombre.isEmpty())   { error("El campo 'Nombre' es obligatorio.");           return; }
        if (apellido.isEmpty()) { error("El campo 'Apellido' es obligatorio.");         return; }
        if (telefono.isEmpty()) { error("El campo 'Teléfono' es obligatorio.");         return; }
        if (email.isEmpty())    { error("El campo 'Correo Electrónico' es obligatorio.");return; }
        if (usuario.isEmpty())  { error("El campo 'Nombre de Usuario' es obligatorio."); return; }
        if (pass.isEmpty())     { error("El campo 'Contraseña' es obligatorio.");        return; }

        boolean exito;
        if (usuarioEditar == null) {
            // Modo crear
            if (usuarioDAO.existeUsuario(usuario)) {
                error("El nombre de usuario '" + usuario + "' ya existe.");
                return;
            }
            exito = usuarioDAO.insertar(new Usuario(usuario, nombre, apellido, telefono, email, pass));
        } else {
            // Modo editar
            usuarioEditar.setNombre(nombre);
            usuarioEditar.setApellido(apellido);
            usuarioEditar.setTelefono(telefono);
            usuarioEditar.setEmail(email);
            usuarioEditar.setUserName(usuario);
            usuarioEditar.setPassword(pass);
            exito = usuarioDAO.actualizar(usuarioEditar);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Operación exitosa!");
            gestionRef.cargarUsuarios();
            dispose();
        } else {
            error("Error al guardar. Intenta de nuevo.");
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
