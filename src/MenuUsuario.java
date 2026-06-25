import EXCEPTIONS.VehiculoNoEncontradoException;
import TURNO.Turno;
import ENUMS.EstadoTurno;
import PERSONAS.Cliente;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Clase para administrar menu del usuario
 * numero de dni: 'cualquier dni cargado en json'
 * contrasenia: 'cualquier contrasenia, no hice la validacion aun'
 * a traves de este menu el usuario puede acceder a sus datos, a sus vehiculos,
 * pedir y ver sus turnos y ver reparaciones
 */
public class MenuUsuario {

    public static void mostrar(SistemaTaller taller, String dniUsuario) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU USUARIO =====");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Ver mis vehículos");
            System.out.println("3. Buscar vehiculo por patente");
            System.out.println("4. Pedir turno");
            System.out.println("5. Ver mis turnos");
            System.out.println("6. Ver mis reparaciones");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    Cliente c = taller.buscarCliente(dniUsuario);
                    System.out.println(c != null ? c : "Cliente no encontrado.");
                }
                case 2 -> taller.listarVehiculosPorCliente(dniUsuario);
                case 3 -> {
                    System.out.print("Patente a buscar: "); String patente = sc.nextLine();
                    try {
                        System.out.println(taller.buscarVehiculo(patente));
                    } catch (VehiculoNoEncontradoException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 4 -> {
                    /*
                     * ya no pedimos dni al cliente porque ya esta logeado, usamos ese dni
                     */
                    System.out.print("Patente vehículo: "); String pat = sc.nextLine();
                    System.out.print("DNI mecánico: "); String dni = sc.nextLine();
                    System.out.print("Fecha (AAAA-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    try {
                        taller.agregarTurnoValidado(dniUsuario, new Turno(0, pat, dni, fecha, EstadoTurno.PENDIENTE));
                    } catch (EXCEPTIONS.ClienteNoActivoException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 5 -> taller.listarTurnosPorCliente(dniUsuario);
                case 6 -> taller.listarReparacionesPorCliente(dniUsuario);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}