package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN DE DISEÑO: SINGLETON
 * -------------------------------------------------------
 * Esta clase implementa el patrón Singleton para garantizar
 * que exista una única instancia de la conexión a la base de datos
 * durante toda la ejecución del programa.
 * 
 * El patrón Singleton se aplica en:
 *   - El atributo estático 'instancia' (única instancia)
 *   - El constructor privado (impide instanciación externa)
 *   - El método estático 'getInstancia()' (punto de acceso global)
 */
public class ConexionDB {

    // ── Singleton: única instancia de esta clase ──────────────────────────────
    private static ConexionDB instancia;

    // ── Configuración de la base de datos (Aiven Cloud MySQL) ─────────────────
    private static final String HOST     = "almacenitla-db-itla-3837.e.aivencloud.com";
    private static final int    PUERTO   = 25037;
    private static final String BD       = "almacenitlafinal";
    private static final String USUARIO  = "avnadmin";
    private static final String CONTRASENA = "AVNS_pPa2xcIg1UbjOzcsoMg";

    // URL con parámetros SSL correctos para Aiven Cloud
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BD
            + "?useSSL=true"
            + "&requireSSL=true"
            + "&verifyServerCertificate=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8";

    private Connection conexion;

    // ── Constructor privado: impide crear instancias desde afuera ─────────────
    private ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("✅ Conexión exitosa a la base de datos: " + BD);

            // Diagnóstico: mostrar columnas reales de las tablas
            try (java.sql.Statement st = conexion.createStatement()) {
                System.out.println("\n📋 Columnas de la tabla 'productos':");
                java.sql.ResultSet rs = st.executeQuery("SHOW COLUMNS FROM productos");
                while (rs.next()) {
                    System.out.println("   → " + rs.getString("Field") + " (" + rs.getString("Type") + ")");
                }
                rs.close();

                System.out.println("\n📋 Columnas de la tabla 'usuarios':");
                rs = st.executeQuery("SHOW COLUMNS FROM usuarios");
                while (rs.next()) {
                    System.out.println("   → " + rs.getString("Field") + " (" + rs.getString("Type") + ")");
                }
                rs.close();
                System.out.println();
            } catch (java.sql.SQLException ex) {
                System.err.println("⚠ No se pudieron leer las columnas: " + ex.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL no encontrado: " + e.getMessage());
            System.err.println("   Asegúrate de tener mysql-connector-j en el classpath.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            System.err.println("   URL: " + URL);
        }
    }

    // ── Método de acceso global al Singleton ──────────────────────────────────
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Obtiene la conexión activa. Si fue cerrada o es null,
     * recrea la instancia del Singleton automáticamente.
     */
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                System.out.println("🔄 Reconectando a la base de datos...");
                instancia = new ConexionDB();
                return instancia.conexion;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error verificando conexión: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos.
     * Útil al cerrar la aplicación.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error cerrando la conexión: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está activa.
     * @return true si la conexión existe y no está cerrada.
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed() && conexion.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }
}
