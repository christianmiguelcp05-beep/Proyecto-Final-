package vista;

import dao.ProductoDAO;
import modelo.Producto;
import util.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * VISTA — Formulario de Producto (Crear / Editar / Eliminar)
 * -------------------------------------------------------
 * Modo creación  : botón Guardar solamente.
 * Modo edición   : botones Guardar y Eliminar.
 */
public class FormularioProductoVista extends JDialog {

    private JTextField  txtNombre, txtMarca, txtCategoria, txtPrecio, txtStock;
    private JButton     btnGuardar, btnEliminar, btnCancelar;
    private ProductoDAO productoDAO;
    private Producto    productoEditar;
    private GestionProductosVista gestionRef;

    public FormularioProductoVista(GestionProductosVista gestionRef, Producto producto) {
        super(gestionRef, true);
        this.gestionRef     = gestionRef;
        this.productoDAO    = new ProductoDAO();
        this.productoEditar = producto;
        inicializarUI();
        if (producto != null) precargar(producto);
    }

    private void inicializarUI() {
        boolean esEdicion = (productoEditar != null);
        setTitle(esEdicion ? "Editar Producto" : "Nuevo Producto");
        setSize(380, 560);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Estilos.BLANCO);
        panel.setBorder(new EmptyBorder(24, 30, 24, 30));
        setContentPane(panel);

        JLabel titulo = Estilos.crearTitulo(esEdicion ? "✏ Editar Producto" : "➕ Nuevo Producto");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        txtNombre    = Estilos.crearCampo("Ej: Laptop HP");
        txtMarca     = Estilos.crearCampo("Ej: HP");
        txtCategoria = Estilos.crearCampo("Ej: Electrónica");
        txtPrecio    = Estilos.crearCampo("Ej: 45000");
        txtStock     = Estilos.crearCampo("Ej: 10");

        agregarCampo(panel, "Nombre del Producto", txtNombre);
        agregarCampo(panel, "Marca",               txtMarca);
        agregarCampo(panel, "Categoría",           txtCategoria);
        agregarCampo(panel, "Precio (RD$)",         txtPrecio);
        agregarCampo(panel, "Cantidad Disponible",  txtStock);

        panel.add(Box.createVerticalStrut(16));

        if (esEdicion) {
            // Dos botones: Guardar + Eliminar
            JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            btnPanel.setOpaque(false);
            btnGuardar  = Estilos.crearBotonPrimario("💾 Guardar");
            btnEliminar = Estilos.crearBotonPeligro("🗑 Eliminar");
            btnCancelar = Estilos.crearBotonSecundario("Cancelar");
            btnPanel.add(btnGuardar);
            btnPanel.add(btnEliminar);
            btnPanel.add(btnCancelar);
            panel.add(btnPanel);

            btnEliminar.addActionListener(e -> eliminar());
        } else {
            // Solo Guardar
            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
            btnPanel.setOpaque(false);
            btnGuardar  = Estilos.crearBotonPrimario("Crear Producto");
            btnCancelar = Estilos.crearBotonSecundario("Cancelar");
            btnPanel.add(btnGuardar);
            btnPanel.add(btnCancelar);
            panel.add(btnPanel);
        }

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void agregarCampo(JPanel panel, String label, JTextField campo) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(Estilos.TEXTO_SECUNDARIO);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(3));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(10));
    }

    private void precargar(Producto p) {
        txtNombre.setText(p.getNombreProducto());
        txtMarca.setText(p.getMarcaProducto());
        txtCategoria.setText(p.getCategoriaProducto());
        txtPrecio.setText(String.valueOf(p.getPrecioProducto()));
        txtStock.setText(String.valueOf(p.getStockProducto()));
    }

    private void guardar() {
        String nombre    = txtNombre.getText().trim();
        String marca     = txtMarca.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String stockStr  = txtStock.getText().trim();

        if (nombre.isEmpty())    { error("El campo 'Nombre del Producto' es obligatorio."); return; }
        if (marca.isEmpty())     { error("El campo 'Marca' es obligatorio.");               return; }
        if (categoria.isEmpty()) { error("El campo 'Categoría' es obligatorio.");           return; }
        if (precioStr.isEmpty()) { error("El campo 'Precio' es obligatorio.");              return; }
        if (stockStr.isEmpty())  { error("El campo 'Cantidad Disponible' es obligatorio."); return; }

        int precio, stock;
        try { precio = Integer.parseInt(precioStr); } catch (NumberFormatException e) {
            error("El 'Precio' debe ser un número entero."); return;
        }
        try { stock = Integer.parseInt(stockStr); } catch (NumberFormatException e) {
            error("La 'Cantidad Disponible' debe ser un número entero."); return;
        }

        boolean exito;
        if (productoEditar == null) {
            exito = productoDAO.insertar(new Producto(nombre, marca, categoria, precio, stock));
        } else {
            productoEditar.setNombreProducto(nombre);
            productoEditar.setMarcaProducto(marca);
            productoEditar.setCategoriaProducto(categoria);
            productoEditar.setPrecioProducto(precio);
            productoEditar.setStockProducto(stock);
            exito = productoDAO.actualizar(productoEditar);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Operación exitosa!");
            gestionRef.cargarProductos();
            dispose();
        } else {
            String detalleError = productoDAO.getUltimoError();
            if (detalleError != null && !detalleError.isEmpty()) {
                error("Error al guardar:\n" + detalleError);
            } else {
                error("Error al guardar. Intenta de nuevo.");
            }
        }
    }

    private void eliminar() {
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Seguro que deseas eliminar '" + productoEditar.getNombreProducto() + "'?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(productoEditar.getIdProducto())) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                gestionRef.cargarProductos();
                dispose();
            } else {
                error("Error al eliminar el producto.");
            }
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
