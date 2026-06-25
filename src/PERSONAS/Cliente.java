package PERSONAS;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente del taller.
 * Extiende Persona y mantiene una lista de patentes de vehículos asociados.
 * Soporta baja lógica mediante el atributo activo.
 */
public class Cliente extends Persona {
    private final List<String> vehiculos = new ArrayList<>();
    private boolean activo;

    public Cliente(String nombre, String dni, String telefono, String email) {
        super(nombre, dni, telefono, email);
        this.activo = true;
    }

    @Override
    public String toString() {
        return "Cliente: " + nombre + " - DNI: " + dni + " - Telefono: " + telefono + " - Email: " + email;
    }

    /**
     * Agrega un vehiculo a la lista de vehiculos del cliente, pasamos patente por parametro
     */
    public void agregarVehiculo(String patente) { vehiculos.add(patente); }

    /*
     * Elimina el vehiculo de la lista de vehiculos del cliente, pasamos patente por parametro
    */
    //public void eliminarVehiculo(String patente) { vehiculos.remove(patente); }

    /// getters y setters
    public List<String> getVehiculos() {return vehiculos;}

    //public void setVehiculos(List<String> vehiculos) {this.vehiculos = vehiculos;}

    public boolean isActivo() {return activo;}

    public void setActivo(boolean activo) {this.activo = activo;}
}
