import EXCEPTIONS.ClienteNoActivoException;
import EXCEPTIONS.ReparacionNoEncontradaException;
import EXCEPTIONS.VehiculoNoEncontradoException;
import PERSONAS.Cliente;
import PERSONAS.Empleado;
import VEHICULOS.Vehiculo;
import java.io.*;
import java.util.*;

import ENUMS.Cargo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import REPARACION.Reparacion;
import TURNO.Turno;
import ENUMS.EstadoReparacion;
import ENUMS.EstadoTurno;
import java.time.LocalDate;

/**
 * Clase encargada de todo el mantenimiento y ejecucion del proyecto
 * mantine map de clientes, map de empleado, set de vehiculos, lista de reparaciones y lista de turnos
 */
public class SistemaTaller {
    private Map<String, Cliente> clientes = new HashMap<>(); // clave = DNI
    private Map<String, Empleado> empleados = new HashMap<>(); // clave = DNI
    ///Usamos Map<String, Cliente> porque el DNI es único por definición, lo que nos permite búsqueda O(1)
    ///en lugar de recorrer toda la lista.
    /// Esto es crítico porque turnos y reparaciones referencian clientes por DNI constantemente
    private Set<Vehiculo> vehiculos = new HashSet<>();
    private List<Reparacion> reparaciones = new ArrayList<>();
    private List<Turno> turnos = new ArrayList<>();
    private int contadorTurnos = 1;
    private int contadorReparaciones = 1;

    // ---------- CLIENTES ----------

    /**
     * metodo que carga en el .json de clientes un nuevo cliente,
     * //@throws IOException
     */
    public void cargarClientes() throws IOException {
        File archivo = new File("data/clientes.json");
        if (!archivo.exists()) {
            clientes = new HashMap<>();
            return;
        }
        FileReader reader = new FileReader(archivo);
        JSONArray array = new JSONArray(new JSONTokener(reader));
        reader.close();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Cliente c = new Cliente(
                    obj.getString("nombre"),
                    obj.getString("dni"),
                    obj.getString("telefono"),
                    obj.getString("email")
            );
            c.setActivo(obj.getBoolean("activo"));
            JSONArray veh = obj.getJSONArray("vehiculos");
            for (int j = 0; j < veh.length(); j++) {
                c.agregarVehiculo(veh.getString(j));
            }
            clientes.put(c.getDni(), c);
        }
    }

    /**
     * Agrega un cliente al sistema si no existe uno con el mismo DNI.
     * @param c Cliente a agregar
     */
    public void agregarCliente(Cliente c) {
        if (clientes.containsKey(c.getDni())) {
            System.out.println("Ya existe un cliente con DNI: " + c.getDni());
            return;
        }
        clientes.put(c.getDni(), c);
        System.out.println("Cliente agregado: " + c);
    }
    /**
     * Lista los clientes cargados
     */
    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes cargados.");
            return;
        }
        for (Cliente c : clientes.values()) {
            System.out.println(" - " + c);
        }
    }
    /**
     * busca clientes por dni y lo retorna
     * //@param dni
     * //@return
     */
    public Cliente buscarCliente(String dni) {
        return clientes.get(dni); // null si no existe
    }

    /**
     * guarda los clientes en el json, si no puede
     * //@throws IOException
     */
    public void guardarClientes() throws IOException {
        JSONArray array = new JSONArray();
        for (Cliente c : clientes.values()) {
            JSONObject obj = new JSONObject();
            obj.put("nombre", c.getNombre());
            obj.put("dni", c.getDni());
            obj.put("telefono", c.getTelefono());
            obj.put("email", c.getEmail());
            obj.put("activo", c.isActivo());
            obj.put("vehiculos", new JSONArray(c.getVehiculos()));
            array.put(obj);
        }
        FileWriter writer = new FileWriter("data/clientes.json");
        writer.write(array.toString(2)); // el 2 es indentación, queda legible
        writer.close();
    }
    ///Cliente tendra baja logica (activo = false) porque quiero conservar su historial de vehiculos y reparaciones.
    public void darDeBajaCliente(String dni) {
        Cliente c = clientes.get(dni);
        if (c == null) {
            System.out.println("Cliente no encontrado: " + dni);
            return;
        }
        c.setActivo(false);
        System.out.println("Cliente dado de baja: " + c.getNombre());
    }

    /**
     * modifica el cliente si existe,
     * //@param dni
     * //@param nuevoTelefono
     * //@param nuevoEmail
     */
    public void modificarCliente(String dni, String nuevoTelefono, String nuevoEmail) {
        Cliente c = clientes.get(dni);
        if (c == null) {
            System.out.println("Cliente no encontrado: " + dni);
            return;
        }
        c.setTelefono(nuevoTelefono);
        c.setEmail(nuevoEmail);
        System.out.println("Cliente actualizado: " + c.getNombre());
    }

    // ---------- VEHÍCULOS ----------
    /**
     * carga el vehiculo, si no puede
     * //@throws IOException
     */
    public void cargarVehiculos() throws IOException {
        File archivo = new File("data/vehiculos.json");
        if (!archivo.exists()) {
            vehiculos = new HashSet<>();
            return;
        }
        FileReader reader = new FileReader(archivo);
        JSONArray array = new JSONArray(new JSONTokener(reader));
        reader.close();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Vehiculo v = new Vehiculo(
                    obj.getString("patente"),
                    obj.getString("marca"),
                    obj.getString("modelo"),
                    obj.getInt("anio"),
                    obj.getString("dniDueno")
            );
            vehiculos.add(v);
        }
    }
    /**
     * agrega el vehiculo a la lista pasado por parametro
     * //@param v
     */
    public void agregarVehiculo(Vehiculo v) {
        if (vehiculos.add(v)) {
            System.out.println("Vehículo agregado: " + v);
        } else {
            System.out.println(" Ya existe un vehículo con esa patente.");
        }
    }
    /**
     * busca un vehiculo por patente y lo retorna
     * si no lo encuentra lanza una excepcion personalizada
     * //@param patente
     * //@return
     * //@throws VehiculoNoEncontradoException
     */
    public Vehiculo buscarVehiculo(String patente) throws VehiculoNoEncontradoException {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) return v;
        }
        throw new VehiculoNoEncontradoException(patente);
    }
    /**
     * lista los vehiculos cargados
     */
    public void listarVehiculos() {
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos cargados.");
            return;
        }
        System.out.println(" Lista de vehículos:");
        for (Vehiculo v : vehiculos) {
            System.out.println(" - " + v);
        }
    }

    /**
     * guarda los vehiculos en el .json, si no puede lanza IOException
     * //@throws IOException
     */
    public void guardarVehiculos() throws IOException {
        JSONArray array = new JSONArray();
        for (Vehiculo v : vehiculos) {
            JSONObject obj = new JSONObject();
            obj.put("patente", v.getPatente());
            obj.put("marca", v.getMarca());
            obj.put("modelo", v.getModelo());
            obj.put("anio", v.getAnio());
            obj.put("dniDueno", v.getDniDueno());
            array.put(obj);
        }
        FileWriter writer = new FileWriter("data/vehiculos.json");
        writer.write(array.toString(2));
        writer.close();
    }

    /**
     * da de baja el vehiculo de la patente pasada por parametro,
     * si no lo encuentra lanza excepcion personalizada
     * //@param patente
     * //@throws VehiculoNoEncontradoException
     */
    public void darDeBajaVehiculo(String patente) throws VehiculoNoEncontradoException {
        Vehiculo encontrado = null;
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) { encontrado = v; break; }
        }
        if (encontrado == null) throw new VehiculoNoEncontradoException(patente);
        vehiculos.remove(encontrado);
        System.out.println("Vehículo eliminado: " + patente);
    }

    /**
     * modifica el vehiculo con nueos datos pasados por parametro,
     * si no puede lanza excepcion personalizada
     * //@param patente
     * //@param nuevaMarca
     * //@param nuevoModelo
     * //@throws VehiculoNoEncontradoException
     */
    public void modificarVehiculo(String patente, String nuevaMarca, String nuevoModelo) throws VehiculoNoEncontradoException {
        for (Vehiculo v : vehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) {
                v.setMarca(nuevaMarca);
                v.setModelo(nuevoModelo);
                System.out.println("Vehículo actualizado: " + v);
                return;
            }
        }
        throw new VehiculoNoEncontradoException(patente);
    }

    // ---------- EMPLEADOS ----------

    /**
     * carga empleados en el .json,
     * si no puede lanza IOException
     * //@throws IOException
     */
    public void cargarEmpleados() throws IOException {
        File archivo = new File("data/empleados.json");
        if (!archivo.exists()) {
            empleados = new HashMap<>();
            return;
        }
        FileReader reader = new FileReader(archivo);
        JSONArray array = new JSONArray(new JSONTokener(reader));
        reader.close();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Empleado e = new Empleado(
                    obj.getString("nombre"),
                    obj.getString("dni"),
                    obj.getString("telefono"),
                    obj.getString("email"),
                    Cargo.valueOf(obj.getString("cargo")),
                    obj.getDouble("salario")
            );
            empleados.put(e.getDni(), e);
        }
    }

    /**
     * agrega empleado pasado por parametro al map,
     * se hace una validacion para que no haya duplicados en dni
     * //@param e
     */
    public void agregarEmpleado(Empleado e) {
        if (empleados.containsKey(e.getDni())) {
            System.out.println("Ya existe un empleado con DNI: " + e.getDni());
            return;
        }
        empleados.put(e.getDni(), e);
        System.out.println("Empleado agregado: " + e);
    }

    /**
     * lista empleados cargados
     */
    public void listarEmpleados() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados cargados.");
            return;
        }
        for (Empleado e : empleados.values()) {
            System.out.println(" - " + e);
        }
    }

    /**
     * busca empleado por dni y lo retorna,
     * sino retorna null
     * //@param dni
     * //@return
     */
    public Empleado buscarEmpleado(String dni) {
        return empleados.get(dni); // null si no existe
    }

    /**
     * guarda empleados en .json,
     * si no puede lanza IOException
     * //@throws IOException
     */
    public void guardarEmpleados() throws IOException {
        JSONArray array = new JSONArray();
        for (Empleado e : empleados.values()) {
            JSONObject obj = new JSONObject();
            obj.put("nombre", e.getNombre());
            obj.put("dni", e.getDni());
            obj.put("telefono", e.getTelefono());
            obj.put("email", e.getEmail());
            obj.put("cargo", e.getCargo().name());
            obj.put("salario", e.getSalario());
            array.put(obj);
        }
        FileWriter writer = new FileWriter("data/empleados.json");
        writer.write(array.toString(2));
        writer.close();
    }

    /**
     * se da un empleado de bajo pasando el dni por parametro
     * //Empleado se elimina fisicamente porque no hay razon de negocio para conservar un ex-empleado.
     * //@param dni
     */
    public void darDeBajaEmpleado(String dni) {
        Empleado e = empleados.get(dni);
        if (e == null) {
            System.out.println("Empleado no encontrado: " + dni);
            return;
        }
        empleados.remove(dni); // empleados se eliminan físicamente, no lógicamente
        System.out.println("Empleado eliminado: " + e.getNombre());
    }

    /**
     * se modifica un empleado con nuevos datos pasados por parametro si es que existe
     * //@param dni
     * //@param nuevoSalario
     */
    public void modificarEmpleado(String dni, double nuevoSalario, Cargo nuevoCargo) {
        Empleado e = empleados.get(dni);
        if (e == null) { System.out.println("Empleado no encontrado: " + dni); return; }
        e.setSalario(nuevoSalario);
        e.setCargo(nuevoCargo);
        System.out.println("Empleado actualizado: " + e.getNombre());
    }

    // ---------- REPARACIONES ----------

    /**
     * guarda las reparaciones pasadas en lista como parametro,
     * si no puede lanza IOException
     * //@param reparaciones
     * //@throws IOException
     */
    public void guardarReparaciones(List<Reparacion> reparaciones) throws IOException {
        JSONArray array = new JSONArray();
        for (Reparacion r : reparaciones) {
            JSONObject obj = new JSONObject();
            obj.put("id", r.getId());
            obj.put("patenteVehiculo", r.getPatenteVehiculo());
            obj.put("dniMecanico", r.getDniMecanico());
            obj.put("descripcion", r.getDescripcion());
            obj.put("costo", r.getCosto());
            obj.put("estado", r.getEstado().name());
            // LocalDate → String
            obj.put("fechaInicio", r.getFechaInicio() != null ? r.getFechaInicio().toString() : "");
            obj.put("fechaFin", r.getFechaFin() != null ? r.getFechaFin().toString() : "");
            array.put(obj);
        }
        FileWriter writer = new FileWriter("data/reparaciones.json");
        writer.write(array.toString(2));
        writer.close();
    }

    /**
     * carga las reparaciones y retorna la lista,
     * si no puede lanza IOexception
     * //@return
     * //@throws IOException
     */
    public List<Reparacion> cargarReparaciones() throws IOException {
        List<Reparacion> reparaciones = new ArrayList<>();
        File archivo = new File("data/reparaciones.json");
        if (!archivo.exists()) return reparaciones;

        FileReader reader = new FileReader(archivo);
        JSONArray array = new JSONArray(new JSONTokener(reader));
        reader.close();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Reparacion r = new Reparacion(
                    obj.getInt("id"),
                    obj.getString("patenteVehiculo"),
                    obj.getString("dniMecanico"),
                    obj.getString("descripcion"),
                    obj.getDouble("costo")
            );
            r.setEstado(EstadoReparacion.valueOf(obj.getString("estado")));
            // String → LocalDate
            String ini = obj.getString("fechaInicio");
            String fin = obj.getString("fechaFin");
            if (!ini.isEmpty()) r.setFechaInicio(LocalDate.parse(ini));
            if (!fin.isEmpty()) r.setFechaFin(LocalDate.parse(fin));
            reparaciones.add(r);
        }
        return reparaciones;
    }

    /**
     * agrega una reparacion pasada por parametro a la lista
     * //@param r
     */
    public void agregarReparacion(Reparacion r) {
        r.setId(contadorReparaciones++);
        reparaciones.add(r);
        System.out.println("Reparación agregada con ID: " + r.getId());
    }

    /**
     * lista las reparaciones si es que existen
     */
    public void listarReparaciones() {
        if (reparaciones.isEmpty()) { System.out.println("No hay reparaciones."); return; }
        for (Reparacion r : reparaciones)
            System.out.println(" - ID:" + r.getId() + " | " + r.getPatenteVehiculo() + " | " + r.getEstado());
    }

    /**
     * Lanza ReparacionNoEncontradaException si el ID no existe.
     * @param id ID de la reparación a iniciar
     * @throws ReparacionNoEncontradaException si no se encuentra la reparación
     */
    public void iniciarReparacion(int id) throws ReparacionNoEncontradaException {
        for (Reparacion r : reparaciones) {
            if (r.getId() == id) { r.iniciarReparacion(); System.out.println("Reparación iniciada."); return; }
        }
        throw new ReparacionNoEncontradaException("Reparación no encontrada: " + id);
    }
    /**
     * Lanza ReparacionNoEncontradaException si el ID no existe.
     * @param id ID de la reparación a finalizar
     * @throws ReparacionNoEncontradaException si no se encuentra la reparación
     */
    public void finalizarReparacion(int id) throws ReparacionNoEncontradaException {
        for (Reparacion r : reparaciones) {
            if (r.getId() == id) { r.finalizarReparacion(); System.out.println("Reparación finalizada."); return; }
        }
        throw new ReparacionNoEncontradaException("Reparación no encontrada: " + id);
    }

    // ---------- TURNOS ----------

    /**
     * guarda la lista de turnos pasada por parametro,
     * si no puede lanza IOException
     * //@param turnos
     * //@throws IOException
     */
    public void guardarTurnos(List<Turno> turnos) throws IOException {
        JSONArray array = new JSONArray();
        for (Turno t : turnos) {
            JSONObject obj = new JSONObject();
            obj.put("id", t.getId());
            obj.put("patenteVehiculo", t.getPatenteVehiculo());
            obj.put("dniMecanico", t.getDniMecanico());
            obj.put("fecha", t.getFecha().toString());
            obj.put("estado", t.getEstado().name());
            array.put(obj);
        }
        FileWriter writer = new FileWriter("data/turnos.json");
        writer.write(array.toString(2));
        writer.close();
    }

    /**
     * carga los turnos en el .json si es que existe,
     * retorna la lista
     * si no puede lanza IOException
     * //@return
     * //@throws IOException
     */
    public List<Turno> cargarTurnos() throws IOException {
        List<Turno> turnos = new ArrayList<>();
        File archivo = new File("data/turnos.json");
        if (!archivo.exists()) return turnos;

        FileReader reader = new FileReader(archivo);
        JSONArray array = new JSONArray(new JSONTokener(reader));
        reader.close();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Turno t = new Turno(
                    obj.getInt("id"),
                    obj.getString("patenteVehiculo"),
                    obj.getString("dniMecanico"),
                    LocalDate.parse(obj.getString("fecha")),
                    EstadoTurno.valueOf(obj.getString("estado"))
            );
            turnos.add(t);
        }
        return turnos;
    }

    /*
     * aprega a la lista el turno
     * //@param t

    public void agregarTurno(Turno t) {
        t.setId(contadorTurnos++);
        turnos.add(t);
        System.out.println("Turno agregado con ID: " + t.getId());
    }
    */

    /**
     * lista los turnos si es que existen cargados
     */
    public void listarTurnos() {
        if (turnos.isEmpty()) { System.out.println("No hay turnos."); return; }
        for (Turno t : turnos)
            System.out.println(" - ID:" + t.getId() + " | " + t.getPatenteVehiculo() + " | " + t.getFecha() + " | " + t.getEstado());
    }

    /**
     * agrega el turno validado del cliente con dni pasado por parametro,
     * si no puede lanza excepcion personalizada
     * //@param dniCliente
     * //@param t
     * /@throws ClienteNoActivoException
     */
    public void agregarTurnoValidado(String dniCliente, Turno t) throws ClienteNoActivoException {
        Cliente c = clientes.get(dniCliente);
        if (c == null || !c.isActivo()) throw new ClienteNoActivoException(dniCliente);
        t.setId(contadorTurnos++);
        turnos.add(t);
        System.out.println("Turno agregado con ID: " + t.getId());
    }

    /**
     * cancela turno con id pasado por parametro
     * //@param id
     */
    public void cancelarTurno(int id) {
        for (Turno t : turnos) {
            if (t.getId() == id) {
                t.setEstado(EstadoTurno.CANCELADO);
                System.out.println("Turno cancelado: " + id);
                return;
            }
        }
        System.out.println("Turno no encontrado: " + id);
    }

    // ---------- GUARDAR_TODO ----------

    /**
     * guarda todo, desde los clientes, empleados, vehiculos, reparaciones y turnos,
     * si algo falla lanza IOException
     * /@throws IOException
     */
    public void guardarTodo() throws IOException {
        guardarClientes();
        guardarEmpleados();
        guardarVehiculos();
        guardarReparaciones(reparaciones);
        guardarTurnos(turnos);
    }

    /**
     * carga todo, desde los clientes, empleados, vehiculos, reparaciones y turnos,
     * si algo falla lanza IOException
     * /@throws IOException
     */
    public void cargarTodo() throws IOException {
        cargarClientes();
        cargarEmpleados();
        cargarVehiculos();
        reparaciones = cargarReparaciones();
        turnos = cargarTurnos();
        contadorTurnos = turnos.stream().mapToInt(Turno::getId).max().orElse(0) + 1;
        contadorReparaciones = reparaciones.stream().mapToInt(Reparacion::getId).max().orElse(0) + 1;

    }
    // ------------ METODOS ----------------

    /**
     * lista lso vehiculos por cada cliente pasando su dni por parametro
     * /@param dniCliente
     */
    public void listarVehiculosPorCliente(String dniCliente) {
        Cliente c = clientes.get(dniCliente);
        if (c == null) { System.out.println("Cliente no encontrado."); return; }
        if (c.getVehiculos().isEmpty()) { System.out.println("No tenés vehículos registrados."); return; }
        for (String patente : c.getVehiculos()) {
            for (Vehiculo v : vehiculos) {
                if (v.getPatente().equalsIgnoreCase(patente))
                    System.out.println(" - " + v);
            }
        }
    }

    /**
     * lista los tuenos por cada cliente pasando su dni por parametro
     * /@param dniCliente
     */
    public void listarTurnosPorCliente(String dniCliente) {
        Cliente c = clientes.get(dniCliente);
        if (c == null) { System.out.println("Cliente no encontrado."); return; }
        boolean encontrado = false;
        for (Turno t : turnos) {
            if (c.getVehiculos().contains(t.getPatenteVehiculo())) {
                System.out.println(" - ID:" + t.getId() + " | " + t.getPatenteVehiculo() + " | " + t.getFecha() + " | " + t.getEstado());
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("No tenés turnos registrados.");
    }

    /**
     * lista las reparaciones por cada cliente pasando su dni por parametro
     * /@param dniCliente
     */
    public void listarReparacionesPorCliente(String dniCliente) {
        Cliente c = clientes.get(dniCliente);
        if (c == null) { System.out.println("Cliente no encontrado."); return; }
        boolean encontrado = false;
        for (Reparacion r : reparaciones) {
            if (c.getVehiculos().contains(r.getPatenteVehiculo())) {
                System.out.println(" - ID:" + r.getId() + " | " + r.getPatenteVehiculo() + " | " + r.getEstado());
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("No tenés reparaciones registradas.");
    }

    public void listarReparacionesActivas() {
        boolean encontrado = false;
        for (Reparacion r : reparaciones) {
            if (r.estaActiva()) {
                System.out.println(" - ID:" + r.getId() + " | " + r.getPatenteVehiculo() + " | " + r.getEstado());
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("No hay reparaciones activas.");
    }

    public static String[] leerDatosPersona(Scanner sc) {
        System.out.print("Nombre: "); String nom = sc.nextLine();
        System.out.print("DNI: "); String dni = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        return new String[]{nom, dni, tel, email};
    }
}


