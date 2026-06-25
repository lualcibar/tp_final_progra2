package VEHICULOS;

/**
 * Clase Vehiculo
 * Mantiene atributos:
 * patente: identificador de cada vehiculo
 * marca: atributo caracteristico
 * modelo: atributo caracteritico
 * anio: tributo caracteristico
 * dniDueno: identificador del dueno del vehiculo
 */
public class Vehiculo {
    private final String patente;
    private String marca;
    private String modelo;
    private final int anio;
    private final String dniDueno;  // vincula con PERSONAS.Cliente

    public Vehiculo(String patente, String marca, String modelo, int anio, String dniDueno) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.dniDueno = dniDueno;
    }

    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public String getDniDueno() { return dniDueno; }

    @Override
    public String toString() {
        return patente + " - " + marca + " " + modelo + " (" + anio + ")";
    }

    //public void setPatente(String patente) {this.patente = patente;}

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * con estos metodos evitamos duplicados en las listas de vehiculos
     * @param obj   the reference object with which to compare.
     * //@return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vehiculo v)) return false;
        return patente.equalsIgnoreCase(v.patente);
    }

    @Override
    public int hashCode() {
        return patente.toUpperCase().hashCode();
    }
}

