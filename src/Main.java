/*import REPARACION.Reparacion;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*SistemaTaller taller = new SistemaTaller();


        try {
            taller.cargarTodo();
        } catch (IOException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }

        //  Mostrar lo que hay
        taller.listarClientes();
        taller.listarVehiculos();



        ///YA FUERON AGREGADOS///
        /*
        //  Agregar un vehículo nuevo
        VEHICULOS.Vehiculo nuevo = new VEHICULOS.Vehiculo("ZZZ999", "Ford", "Fiesta", 2016, "30111222");
        taller.agregarVehiculo(nuevo);


        VEHICULOS.Vehiculo nuevo1 = new VEHICULOS.Vehiculo("AB786RG", "Ford", "Palio", 2014, "21931131");
        taller.agregarVehiculo(nuevo1);

        VEHICULOS.Vehiculo nuevo2 = new VEHICULOS.Vehiculo("AB786RG", "Ford", "Palio", 2014, "21931131");
        taller.agregarVehiculo(nuevo2);

        ///con esto chequeamos que funciona bien el hashCode

        PERSONAS.Cliente cliente1 = new PERSONAS.Cliente("Felipe", "21931131", "2235425164", "felipe.4@gmail.com");
        taller.agregarCliente(cliente1);

    ///chequeados todos los to string
        //PERSONAS.Empleado empleado1 =new PERSONAS.Empleado("Lucia", "47832917", "2235425164", "lucia.alcibar0@gmail.com", ENUMS.Cargo.MECANICO, 150.00);
        //TURNO.Turno turno1 = new TURNO.Turno(1, "AB786RG", "47832917", LocalDate.now(), ENUMS.EstadoTurno.PENDIENTE);
        Reparacion reparacion1 = new Reparacion(1, "AB786RG", "47832917", "Roto el embrague", 200.00);
        reparacion1.iniciarReparacion();
        reparacion1.estaActiva();
        reparacion1.iniciarReparacion();
        reparacion1.finalizarReparacion();
        reparacion1.estaActiva();

        //  Guardar cambios
        try {
            taller.guardarTodo();
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }


    }
*/// VIEJO MAIN
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaTaller taller = new SistemaTaller();

        try {
            taller.cargarTodo();
        } catch (IOException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("===== SISTEMA TALLER =====");
        System.out.print("DNI del usuario: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        if (user.equals("admin") && pass.equals("admin123")) {
            MenuAdmin.mostrar(taller);
        } else {
            // buscamos si el usuario es un cliente (usa su DNI como usuario)
            if (taller.buscarCliente(user) != null) {
                MenuUsuario.mostrar(taller, user);
            } else {
                System.out.println("Credenciales incorrectas.");
            }
        }
    }
}