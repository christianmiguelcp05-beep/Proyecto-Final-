[README.md](https://github.com/user-attachments/files/26733100/README.md)
# AlmacenITLA — Sistema de Gestión de Almacén

## Estructura del Proyecto

```
AlmacenITLA/
└── src/
    ├── Main.java                        ← Punto de entrada
    ├── modelo/
    │   ├── Usuario.java                 ← Entidad Usuario (Abstracción + Encapsulamiento)
    │   └── Producto.java               ← Entidad Producto (Abstracción + Encapsulamiento)
    ├── dao/
    │   ├── BaseDAO.java                 ← Clase abstracta genérica (Herencia + Polimorfismo)
    │   ├── UsuarioDAO.java              ← CRUD de usuarios
    │   └── ProductoDAO.java            ← CRUD de productos
    ├── vista/
    │   ├── LoginVista.java             ← Pantalla de login
    │   ├── RegistroVista.java          ← Pantalla de registro
    │   ├── PrincipalVista.java         ← Panel principal (menú)
    │   ├── GestionUsuariosVista.java   ← Listado de usuarios
    │   ├── FormularioUsuarioVista.java ← Crear/editar usuario
    │   ├── GestionProductosVista.java  ← Listado de productos
    │   └── FormularioProductoVista.java← Crear/editar/eliminar producto
    └── util/
        ├── ConexionDB.java             ← PATRON SINGLETON — Conexión a BD
        └── Estilos.java               ← Utilidades de UI (colores, fuentes, botones)
```

## Patrones de Diseño Aplicados

### 1. Singleton — `util/ConexionDB.java`
Garantiza que solo exista **una única instancia** de la conexión a la base de datos durante toda la ejecución del programa.

### 2. DAO (Data Access Object) — `dao/BaseDAO.java`
Separa la lógica de acceso a datos de la lógica de negocio. La clase abstracta `BaseDAO<T>` define el contrato, y `UsuarioDAO` / `ProductoDAO` lo implementan.

## Pilares OOP

| Pilar | Clase(s) | Descripción |
|---|---|---|
| **Abstracción** | `BaseDAO<T>` | Contrato genérico de CRUD sin revelar implementación |
| **Encapsulamiento** | `Usuario`, `Producto` | Atributos privados con getters/setters |
| **Herencia** | `UsuarioDAO`, `ProductoDAO` | Extienden `BaseDAO` y heredan la conexión |
| **Polimorfismo** | `UsuarioDAO`, `ProductoDAO` | Cada uno implementa `insertar()`, `actualizar()`, etc. con su propia lógica |

## Configuración en Eclipse

1. Crea un nuevo **Java Project** y copia la carpeta `src/`
2. Descarga el driver MySQL: `mysql-connector-j-8.x.x.jar`
3. Agregalo al **Build Path**: clic derecho en proyecto → Build Path → Add External JARs
4. Ejecuta `Main.java`

## Base de Datos Remota

La conexión ya está configurada en `util/ConexionDB.java`:
- **Host**: `almacenitla-db-itla-3837.e.aivencloud.com:25037`
- **BD**: `almacenitlafinal`
- **Tablas**: `usuarios`, `productos`
