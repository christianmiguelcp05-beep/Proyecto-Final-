package dao;

import modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ORIENTACIÓN A OBJETOS — ProductoDAO
 * -------------------------------------------------------
 * HERENCIA    : Extiende BaseDAO<Producto>, heredando la conexión
 *               y el contrato CRUD definido en la clase padre.
 * POLIMORFISMO: Implementa los métodos abstractos con la lógica
 *               específica para la tabla 'productos'.
 */
public class ProductoDAO extends BaseDAO<Producto> {

    // Variable para guardar el último error ocurrido
    private String ultimoError = "";

    public ProductoDAO() { super(); }

    /** Retorna la descripción del último error ocurrido */
    public String getUltimoError() { return ultimoError; }

    // ── Polimorfismo: implementación específica para Producto ─────────────────

    @Override
    public boolean insertar(Producto p) {
        ultimoError = "";
        String sql = "INSERT INTO productos (NombreProducto, MarcaProducto, CategoriaProducto, PrecioProducto, StockProducto) VALUES (?,?,?,?,?)";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                ultimoError = "No hay conexión a la base de datos. Verifica que el driver MySQL esté en el classpath y que el servidor esté accesible.";
                System.err.println("Error insertando producto: " + ultimoError);
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNombreProducto());
                ps.setString(2, p.getMarcaProducto());
                ps.setString(3, p.getCategoriaProducto());
                ps.setInt(4,    p.getPrecioProducto());
                ps.setInt(5,    p.getStockProducto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error insertando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto p) {
        ultimoError = "";
        String sql = "UPDATE productos SET NombreProducto=?, MarcaProducto=?, CategoriaProducto=?, PrecioProducto=?, StockProducto=? WHERE idProducto=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                ultimoError = "No hay conexión a la base de datos.";
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNombreProducto());
                ps.setString(2, p.getMarcaProducto());
                ps.setString(3, p.getCategoriaProducto());
                ps.setInt(4,    p.getPrecioProducto());
                ps.setInt(5,    p.getStockProducto());
                ps.setInt(6,    p.getIdProducto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error actualizando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        ultimoError = "";
        String sql = "DELETE FROM productos WHERE idProducto=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                ultimoError = "No hay conexión a la base de datos.";
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error eliminando producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE idProducto=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando producto: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return lista;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando productos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // ── Método auxiliar: mapea ResultSet → Producto ───────────────────────────
    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("idProducto"),
            rs.getString("NombreProducto"),
            rs.getString("MarcaProducto"),
            rs.getString("CategoriaProducto"),
            rs.getInt("PrecioProducto"),
            rs.getInt("StockProducto")
        );
    }
}
