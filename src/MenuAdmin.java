import EXCEPTIONS.ClienteNoActivoException;
import EXCEPTIONS.ReparacionNoEncontradaException;
import EXCEPTIONS.VehiculoNoEncontradoException;
import PERSONAS.Cliente;
import PERSONAS.Empleado;
import REPARACION.Reparacion;
import VEHICULOS.Vehiculo;
import TURNO.Turno;
import ENUMS.Cargo;
import ENUMS.EstadoTurno;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Clase para administrar menu del administrador
 * nombreUsuario: admin
 * contrasenia: admin123
 */
public class MenuAdmin {

    public static void mostrar(SistemaTaller taller) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Empleados");
            System.out.println("3. Gestionar Vehículos");
            System.out.println("4. Gestionar Turnos");
            System.out.println("5. Gestionar Reparaciones");
            System.out.println("6. Guardar todo");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1 -> menuClientes(taller, sc);
                case 2 -> menuEmpleados(taller, sc);
                case 3 -> menuVehiculos(taller, sc);
                case 4 -> menuTurnos(taller, sc);
                case 5 -> menuReparaciones(taller, sc);
                case 6 -> {
                    try { taller.guardarTodo(); }
                    catch (IOException e) { System.out.println("Error al guardar: " + e.getMessage()); }
                }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ---------- CLIENTES ----------
    /**
     * Sub-Menu de Clientes cn opcion de:
     * listar clientes, agregar clientes, buscar cliente, modificar cliente o dar de baja un cliente
     * //@param taller
     * @param sc
     * (aclaracion: listado de clientes no tiene orden ya que hash no ordena con logica alfabetica y demas)
     */
    private static void menuClientes(SistemaTaller taller, Scanner sc) {
        System.out.println("\n-- Clientes --");
        System.out.println("1. Listar  2. Agregar  3. Buscar  4. Modificar  5. Dar de baja");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> taller.listarClientes();
            case 2 -> {
                String[] datos = SistemaTaller.leerDatosPersona(sc);
                taller.agregarCliente(new Cliente(datos[0], datos[1], datos[2], datos[3]));
            }
            case 3 -> {
                System.out.print("DNI a buscar: "); String dni = sc.nextLine();
                Cliente c = taller.buscarCliente(dni);
                System.out.println(c != null ? c : "No encontrado.");
            }
            case 4 -> {
                System.out.print("DNI a modificar: "); String dni = sc.nextLine();
                System.out.print("Nuevo teléfono: "); String tel = sc.nextLine();
                System.out.print("Nuevo email: "); String email = sc.nextLine();
                taller.modificarCliente(dni, tel, email);
            }
            case 5 -> {
                System.out.print("DNI a dar de baja: "); String dni = sc.nextLine();
                taller.darDeBajaCliente(dni);
            }
        }
    }

    // ---------- EMPLEADOS ----------
    /**
     * Sub-Menu de Empleados con opcion de:
     * listar empleados, agregar empleado, buscar empleado, modificar salario o eliminar un empleado
     * //@param taller
     * @param sc
     * (aclaracion: listado de empleados no tiene orden ya que hash no ordena con logica alfabetica y demas)
     */
    private static void menuEmpleados(SistemaTaller taller, Scanner sc) {
        System.out.println("\n-- Empleados --");
        System.out.println("1. Listar  2. Agregar  3. Buscar  4. Modificar empleado  5. Eliminar");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> taller.listarEmpleados();
            case 2 -> {
                String[] datos = SistemaTaller.leerDatosPersona(sc);
                System.out.print("Cargo (ADMINISTRADOR/MECANICO/RECEPCIONISTA): ");
                Cargo cargo = Cargo.valueOf(sc.nextLine().toUpperCase());
                System.out.print("Salario: "); double sal = sc.nextDouble(); sc.nextLine();
                taller.agregarEmpleado(new Empleado(datos[0], datos[1], datos[2], datos[3], cargo, sal));
            }
            case 3 -> {
                System.out.print("DNI a buscar: "); String dni = sc.nextLine();
                Empleado e = taller.buscarEmpleado(dni);
                System.out.println(e != null ? e : "No encontrado.");
            }
            case 4 -> {
                System.out.print("DNI: "); String dni = sc.nextLine();
                System.out.print("Nuevo salario: "); double sal = sc.nextDouble(); sc.nextLine();
                System.out.print("Nuevo cargo (ADMINISTRADOR/MECANICO/RECEPCIONISTA): ");
                Cargo cargo = Cargo.valueOf(sc.nextLine().toUpperCase());
                taller.modificarEmpleado(dni, sal, cargo);
            }
            case 5 -> {
                System.out.print("DNI a eliminar: "); String dni = sc.nextLine();
                taller.darDeBajaEmpleado(dni);
            }
        }
    }

    /**
     * Sub-Menu de Vehiculos con opcion de:
     * listar vehiculos, agregar vehiculos, modificar un vehiculo o eliminar un vehiculo
     * //@param taller
     * @param sc
     * (aclaracion: listado de vehiculos no tiene orden ya que hash no ordena con logica alfabetica y demas)
     */
    // ---------- VEHÍCULOS ----------
    private static void menuVehiculos(SistemaTaller taller, Scanner sc) {
        System.out.println("\n-- Vehículos --");
        System.out.println("1. Listar  2. Agregar  3. Modificar  4. Eliminar");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> taller.listarVehiculos();
            case 2 -> {
                System.out.print("Patente: "); String pat = sc.nextLine();
                System.out.print("Marca: "); String marca = sc.nextLine();
                System.out.print("Modelo: "); String modelo = sc.nextLine();
                System.out.print("Año: "); int anio = sc.nextInt(); sc.nextLine();
                System.out.print("DNI dueño: "); String dni = sc.nextLine();
                taller.agregarVehiculo(new Vehiculo(pat, marca, modelo, anio, dni));
            }
            case 3 -> {
                System.out.print("Patente a modificar: "); String pat = sc.nextLine();
                System.out.print("Nueva marca: "); String marca = sc.nextLine();
                System.out.print("Nuevo modelo: "); String modelo = sc.nextLine();
                try {
                    taller.modificarVehiculo(pat, marca, modelo);
                } catch (VehiculoNoEncontradoException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            case 4 -> {
                System.out.print("Patente a eliminar: "); String pat = sc.nextLine();
                try {
                    taller.darDeBajaVehiculo(pat);
                } catch (VehiculoNoEncontradoException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
    /**
     * Sub-Menu de Turnos con opcion de:
     * listar turnos, agregar tuenos o modificar turnos.
     * //@param taller
     * @param sc
     * (aclaracion: listado de turnos no tiene orden ya que hash no ordena con logica alfabetica y demas)
     */
    // ---------- TURNOS ----------
    private static void menuTurnos(SistemaTaller taller, Scanner sc) {
        System.out.println("\n-- Turnos --");
        System.out.println("1. Listar  2. Agregar  3. Cancelar");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> taller.listarTurnos();
            case 2 -> {
                System.out.print("Patente vehículo: "); String pat = sc.nextLine();
                System.out.print("DNI mecánico: "); String dni = sc.nextLine();
                System.out.print("Fecha (AAAA-MM-DD): "); LocalDate fecha = LocalDate.parse(sc.nextLine());
                System.out.print("DNI cliente: "); String dniCliente = sc.nextLine();
                try {
                    taller.agregarTurnoValidado(dniCliente, new Turno(0, pat, dni, fecha, EstadoTurno.PENDIENTE));
                } catch (ClienteNoActivoException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            case 3 -> {
                System.out.print("ID turno a cancelar: "); int id = sc.nextInt(); sc.nextLine();
                taller.cancelarTurno(id);
            }
        }
    }

    /**
     * Sub-Menu de Reparaciones con opcion de:
     * listar reparaciones, agregar reparaciones, iniciar o finalizar una reparacion
     * //@param taller
     * @param sc
     * (aclaracion: listado de reparaciones no tiene orden ya que hash no ordena con logica alfabetica y demas)
     */
    // ---------- REPARACIONES ----------
    private static void menuReparaciones(SistemaTaller taller, Scanner sc) {
        System.out.println("\n-- Reparaciones --");
        System.out.println("1. Listar  2. Agregar  3. Iniciar  4. Finalizar  5. Listar activas");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> taller.listarReparaciones();
            case 2 -> {
                System.out.print("Patente vehículo: "); String pat = sc.nextLine();
                System.out.print("DNI mecánico: "); String dni = sc.nextLine();
                System.out.print("Descripción: "); String desc = sc.nextLine();
                System.out.print("Costo: "); double costo = sc.nextDouble(); sc.nextLine();
                taller.agregarReparacion(new Reparacion(0, pat, dni, desc, costo));
            }
            case 3 -> {
                System.out.print("ID de reparación a iniciar: "); int id = sc.nextInt(); sc.nextLine();
                try {
                    taller.iniciarReparacion(id);
                } catch (ReparacionNoEncontradaException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            case 4 -> {
                System.out.print("ID de reparación a finalizar: "); int id = sc.nextInt(); sc.nextLine();
                try {
                    taller.finalizarReparacion(id);
                } catch (ReparacionNoEncontradaException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            case 5 -> taller.listarReparacionesActivas();
        }
    }
}