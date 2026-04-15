package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * VISTA — Gestión de Usuarios
 * -------------------------------------------------------
 * Muestra listado de usuarios y permite crear, actualizar y eliminar.
 */
public class GestionUsuariosVista extends JFrame {

    private JTable         tabla;
    private DefaultTableModel modeloTabla;
    private JButton        btnNuevo, btnActualizar, btnEliminar, btnVolver;
    private UsuarioDAO     usuarioDAO;
    private PrincipalVista principal;

    public GestionUsuariosVista(PrincipalVista principal) {
        this.principal  = principal;
        this.usuarioDAO = new UsuarioDAO();
        inicializarUI();
        cargarUsuarios();
    }

    private void inicializarUI() {
        setTitle("AlmacénITLA — Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);

        JPanel fondo = new JPanel(new BorderLayout(0, 16));
        fondo.setBackground(Estilos.GRIS_FONDO);
        fondo.setBorder(new EmptyBorder(24, 28, 20, 28));
        setContentPane(fondo);

        // ── Encabezado ─────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titulo = Estilos.crearTitulo("👥 Clientes Registrados");
        btnVolver = Estilos.crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> { dispose(); principal.setVisible(true); });
        header.add(titulo,   BorderLayout.WEST);
        header.add(btnVolver, BorderLayout.EAST);

        // ── Tabla ──────────────────────────────────────────────────────────────
        String[] columnas = {"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setFont(Estilos.NORMAL);
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(Estilos.AZUL_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setGridColor(Estilos.GRIS_BORDE);
        tabla.setShowVerticalLines(false);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(Estilos.GRIS_BORDE, 1, true));
        scroll.getViewport().setBackground(Estilos.BLANCO);

        // ── Botones ────────────────────────────────────────────────────────────
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);

        btnNuevo      = Estilos.crearBotonPrimario("+ Nuevo Usuario");
        btnActualizar = Estilos.crearBotonPrimario("✏ Actualizar");
        btnEliminar   = Estilos.crearBotonPeligro("🗑 Eliminar");

        botones.add(btnNuevo);
        botones.add(btnActualizar);
        botones.add(btnEliminar);

        fondo.add(header,  BorderLayout.NORTH);
        fondo.add(scroll,  BorderLayout.CENTER);
        fondo.add(botones, BorderLayout.SOUTH);

        // ── Eventos ────────────────────────────────────────────────────────────
        btnNuevo.addActionListener(e -> abrirFormulario(null));

        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un usuario primero."); return; }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            abrirFormulario(usuarioDAO.buscarPorId(id));
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un usuario a eliminar."); return; }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            confirmarEliminar(id);
        });
    }

    public void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listarTodos();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                u.getIdUser(), u.getNombre(), u.getApellido(),
                u.getTelefono(), u.getEmail(), u.getUserName()
            });
        }
    }

    private void abrirFormulario(Usuario usuario) {
        new FormularioUsuarioVista(this, usuario).setVisible(true);
    }

    private void confirmarEliminar(int id) {
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Seguro que deseas eliminar este usuario?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
            }
        }
    }
}
