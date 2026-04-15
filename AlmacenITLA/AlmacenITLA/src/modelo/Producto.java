package modelo;

/**
 * ORIENTACIÓN A OBJETOS — MODELO Producto
 * -------------------------------------------------------
 * ABSTRACCIÓN    : Representa la entidad "producto de almacén",
 *                  con los campos definidos en la base de datos remota.
 * ENCAPSULAMIENTO: Atributos privados con acceso controlado por
 *                  getters/setters públicos.
 */
public class Producto {

    // ── Encapsulamiento: atributos privados ───────────────────────────────────
    private int    idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String categoriaProducto;
    private int    precioProducto;
    private int    stockProducto;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Producto() {}

    public Producto(String nombre, String marca, String categoria,
                    int precio, int stock) {
        this.nombreProducto    = nombre;
        this.marcaProducto     = marca;
        this.categoriaProducto = categoria;
        this.precioProducto    = precio;
        this.stockProducto     = stock;
    }

    public Producto(int id, String nombre, String marca, String categoria,
                    int precio, int stock) {
        this(nombre, marca, categoria, precio, stock);
        this.idProducto = id;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────
    public int    getIdProducto()               { return idProducto;        }
    public void   setIdProducto(int id)         { this.idProducto = id;     }

    public String getNombreProducto()           { return nombreProducto;    }
    public void   setNombreProducto(String n)   { this.nombreProducto = n;  }

    public String getMarcaProducto()            { return marcaProducto;     }
    public void   setMarcaProducto(String m)    { this.marcaProducto = m;   }

    public String getCategoriaProducto()        { return categoriaProducto; }
    public void   setCategoriaProducto(String c){ this.categoriaProducto = c; }

    public int    getPrecioProducto()           { return precioProducto;    }
    public void   setPrecioProducto(int p)      { this.precioProducto = p;  }

    public int    getStockProducto()            { return stockProducto;     }
    public void   setStockProducto(int s)       { this.stockProducto = s;   }

    @Override
    public String toString() {
        return nombreProducto + " — " + marcaProducto;
    }
}
