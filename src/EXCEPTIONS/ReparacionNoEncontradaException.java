package EXCEPTIONS;

/**
 * excepcion lanzada cada vez que se quiere acceder a una reparacion que no existe
 */
public class ReparacionNoEncontradaException extends RuntimeException {
    public ReparacionNoEncontradaException(String mensaje) { super(mensaje); }
}
