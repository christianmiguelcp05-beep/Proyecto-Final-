package dao;

import util.ConexionDB;
import java.sql.Connection;
import java.util.List;

/**
 * ORIENTACIÓN A OBJETOS — CLASE ABSTRACTA BaseDAO
 * -------------------------------------------------------
 * ABSTRACCIÓN : Define el contrato genérico de operaciones CRUD
 * sin revelar los detalles de implementación.
 * HERENCIA : UsuarioDAO y ProductoDAO heredan esta clase,
 * reutilizando la conexión y el contrato de métodos.
 * POLIMORFISMO: Cada subclase implementa los métodos abstractos
 * con su propia lógica específica.
 *
 * @param <T> Tipo del modelo (Usuario o Producto)
 */
public abstract class BaseDAO<T> {

    // ── Herencia: conexión compartida por todas las subclases ─────────────────
    protected Connection conexion;

    public BaseDAO() {
        // Singleton reutilizado aquí
        this.conexion = ConexionDB.getInstancia().getConexion();
    }

    /**
     * Obtiene la conexión activa, reconectando si es necesario.
     * Los DAOs deben usar este método en vez de acceder directamente a 'conexion'
     * para garantizar que nunca trabajen con una conexión cerrada o nula.
     */
    protected Connection obtenerConexion() {
        this.conexion = ConexionDB.getInstancia().getConexion();
        return this.conexion;
    }

    // ── Abstracción: contrato de métodos que cada DAO debe implementar ─────────
    public abstract boolean insertar(T objeto);

    public abstract boolean actualizar(T objeto);

    public abstract boolean eliminar(int id);

    public abstract T buscarPorId(int id);

    public abstract List<T> listarTodos();
}
