package vista;

import dao.ProductoDAO;
import modelo.Producto;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * VISTA — Gestión de Productos
 * -------------------------------------------------------
 * Lista todos los productos. Al hacer clic en uno, abre el formulario
 * de edición. El botón "Nuevo" abre el formulario en modo creación.
 */
public class GestionProductosVista extends JFrame {

    private JTable           tabla;
    private DefaultTableModel modeloTabla;
    private JButton          btnNuevo, btnVolver;
    private ProductoDAO      productoDAO;
    private PrincipalVista   principal;

    public GestionProductosVista(PrincipalVista principal) {
        this.principal   = principal;
        this.productoDAO = new ProductoDAO();
        inicializarUI();
        cargarProductos();
    }

    private void inicializarUI() {
        setTitle("AlmacénITLA — Gestión de Productos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 520);
        setLocationRelativeTo(null);

        JPanel fondo = new JPanel(new BorderLayout(0, 16));
        fondo.setBackground(Estilos.GRIS_FONDO);
        fondo.setBorder(new EmptyBorder(24, 28, 20, 28));
        setContentPane(fondo);

        // ── Encabezado ─────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titulo = Estilos.crearTitulo("📦 Productos de Almacén");
        btnVolver = Estilos.crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> { dispose(); principal.setVisible(true); });
        header.add(titulo,    BorderLayout.WEST);
        header.add(btnVolver, BorderLayout.EAST);

        // ── Tabla ──────────────────────────────────────────────────────────────
        String[] columnas = {"ID", "Nombre", "Marca", "Categoría", "Precio (RD$)", "Stock"};
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

        JLabel hint = new JLabel("💡 Haz clic en un producto para editarlo o eliminarlo");
        hint.setFont(Estilos.PEQUEÑO);
        hint.setForeground(Estilos.TEXTO_SECUNDARIO);

        // ── Botones inferiores ─────────────────────────────────────────────────
        JPanel botones = new JPanel(new BorderLayout());
        botones.setOpaque(false);
        btnNuevo = Estilos.crearBotonPrimario("+ Nuevo Producto");
        botones.add(btnNuevo, BorderLayout.WEST);
        botones.add(hint,     BorderLayout.EAST);

        fondo.add(header,  BorderLayout.NORTH);
        fondo.add(scroll,  BorderLayout.CENTER);
        fondo.add(botones, BorderLayout.SOUTH);

        // ── Eventos ────────────────────────────────────────────────────────────
        btnNuevo.addActionListener(e -> new FormularioProductoVista(this, null).setVisible(true));

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    new FormularioProductoVista(
                        GestionProductosVista.this,
                        productoDAO.buscarPorId(id)
                    ).setVisible(true);
                }
            }
        });
    }

    public void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.listarTodos();
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                p.getMarcaProducto(),
                p.getCategoriaProducto(),
                "RD$ " + p.getPrecioProducto(),
                p.getStockProducto()
            });
        }
    }
}
