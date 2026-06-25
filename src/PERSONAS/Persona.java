package PERSONAS;

/**
 * Clase padre de la que se extienden Cliente y Empleado.
 * Mantiene atributos: nombre, dni, telefono, email.
 */
public abstract class Persona {
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;

    public Persona(String nombre, String dni, String telefono, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    public abstract String toString();

    // getters y setters

    public String getNombre() {
        return nombre;
    }

    //public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
