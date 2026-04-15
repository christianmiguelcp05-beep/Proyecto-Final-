import util.Estilos;
import vista.LoginVista;

import javax.swing.*;

/**
 * =====================================================
 * PROYECTO FINAL — SISTEMA DE GESTIÓN DE ALMACÉN
 * =====================================================
 *
 * PATRONES DE DISEÑO APLICADOS:
 * ┌─────────────────────────────────────────────────────┐
 * │ 1. SINGLETON → util/ConexionDB.java │
 * │ Garantiza una única instancia de la conexión BD │
 * │ │
 * │ 2. DAO (Data Access Object) → dao/BaseDAO.java │
 * │ Separa la lógica de negocio del acceso a datos │
 * │ Subclases: UsuarioDAO, ProductoDAO │
 * └─────────────────────────────────────────────────────┘
 *
 * PILARES OOP APLICADOS:
 * ┌─────────────────────────────────────────────────────┐
 * │ • Abstracción → BaseDAO<T> (clase abstracta) │
 * │ • Encapsulamiento → modelo/Usuario, modelo/Producto │
 * │ • Herencia → UsuarioDAO, ProductoDAO │
 * │ extienden BaseDAO │
 * │ • Polimorfismo → Cada DAO implementa sus propios │
 * │ métodos insertar/actualizar/... │
 * └─────────────────────────────────────────────────────┘
 *
 * TECNOLOGÍAS: Java · Swing · AWT · MySQL (Aiven Cloud)
 * =====================================================
 */
public class Main {
    public static void main(String[] args) {
        // Configurar Look & Feel antes de mostrar cualquier ventana
        Estilos.configurarLookAndFeel();

        // Lanzar en el hilo de la interfaz gráfica (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginVista login = new LoginVista();
            login.setVisible(true);
        });
    }
}
