package dao;

import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ORIENTACIÓN A OBJETOS — UsuarioDAO
 * -------------------------------------------------------
 * HERENCIA    : Extiende BaseDAO<Usuario>, heredando la conexión
 *               y el contrato CRUD definido en la clase padre.
 * POLIMORFISMO: Implementa los métodos abstractos con la lógica
 *               específica para la tabla 'usuarios'.
 */
public class UsuarioDAO extends BaseDAO<Usuario> {

    // ── Herencia: llama al constructor del padre ──────────────────────────────
    public UsuarioDAO() { super(); }

    // ── Polimorfismo: implementación específica para Usuario ──────────────────

    @Override
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (UserName, Nombre, Apellido, Telefono, Email, Password) VALUES (?,?,?,?,?,?)";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                System.err.println("Error insertando usuario: conexión a la BD es null.");
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getUserName());
                ps.setString(2, u.getNombre());
                ps.setString(3, u.getApellido());
                ps.setString(4, u.getTelefono());
                ps.setString(5, u.getEmail());
                ps.setString(6, u.getPassword());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error insertando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET UserName=?, Nombre=?, Apellido=?, Telefono=?, Email=?, Password=? WHERE idUser=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                System.err.println("Error actualizando usuario: conexión a la BD es null.");
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getUserName());
                ps.setString(2, u.getNombre());
                ps.setString(3, u.getApellido());
                ps.setString(4, u.getTelefono());
                ps.setString(5, u.getEmail());
                ps.setString(6, u.getPassword());
                ps.setInt(7, u.getIdUser());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error actualizando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE idUser=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) {
                System.err.println("Error eliminando usuario: conexión a la BD es null.");
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error eliminando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE idUser=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Busca un usuario por username y contraseña (para login) */
    public Usuario buscarPorCredenciales(String userName, String password) {
        String sql = "SELECT * FROM usuarios WHERE UserName=? AND Password=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userName);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /** Verifica si un username ya existe en la BD */
    public boolean existeUsuario(String userName) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE UserName=?";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return false;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            Connection conn = obtenerConexion();
            if (conn == null) return lista;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // ── Método auxiliar: mapea ResultSet → Usuario ────────────────────────────
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("idUser"),
            rs.getString("UserName"),
            rs.getString("Nombre"),
            rs.getString("Apellido"),
            rs.getString("Telefono"),
            rs.getString("Email"),
            rs.getString("Password")
        );
    }
}
