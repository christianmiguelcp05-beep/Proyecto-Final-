package modelo;

/**
 * ORIENTACIÓN A OBJETOS — MODELO Usuario
 * -------------------------------------------------------
 * ABSTRACCIÓN    : Representa la entidad "usuario" del mundo real,
 *                  exponiendo solo los atributos relevantes al sistema.
 * ENCAPSULAMIENTO: Todos los atributos son privados; el acceso se
 *                  realiza exclusivamente mediante getters y setters.
 */
public class Usuario {

    // ── Encapsulamiento: atributos privados ───────────────────────────────────
    private int    idUser;
    private String userName;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Usuario() {}

    public Usuario(String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.userName  = userName;
        this.nombre    = nombre;
        this.apellido  = apellido;
        this.telefono  = telefono;
        this.email     = email;
        this.password  = password;
    }

    public Usuario(int idUser, String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this(userName, nombre, apellido, telefono, email, password);
        this.idUser = idUser;
    }

    // ── Getters y Setters (Encapsulamiento) ────────────────────────────────────
    public int    getIdUser()             { return idUser;    }
    public void   setIdUser(int idUser)   { this.idUser = idUser; }

    public String getUserName()              { return userName;  }
    public void   setUserName(String u)      { this.userName = u; }

    public String getNombre()                { return nombre;    }
    public void   setNombre(String n)        { this.nombre = n;  }

    public String getApellido()              { return apellido;  }
    public void   setApellido(String a)      { this.apellido = a; }

    public String getTelefono()              { return telefono;  }
    public void   setTelefono(String t)      { this.telefono = t; }

    public String getEmail()                 { return email;     }
    public void   setEmail(String e)         { this.email = e;   }

    public String getPassword()              { return password;  }
    public void   setPassword(String p)      { this.password = p; }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (@" + userName + ")";
    }
}
