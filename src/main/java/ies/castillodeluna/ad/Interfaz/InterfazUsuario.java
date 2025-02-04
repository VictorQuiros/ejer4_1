package ies.castillodeluna.ad.Interfaz;

import edu.acceso.sqlutils.errors.DataAccessException;
import ies.castillodeluna.ad.modelo.Cliente;
import ies.castillodeluna.ad.modelo.Pedido;
import ies.castillodeluna.ad.modelo.ZonaEnvio;
import ies.castillodeluna.ad.backend.BackFactory;
import ies.castillodeluna.ad.backend.Connect;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InterfazUsuario {
    private final Connect connection;
    private final Scanner scanner = new Scanner(System.in);

    public InterfazUsuario(Map<String, Object> opciones) throws DataAccessException {
        this.connection = BackFactory.crearConexion(opciones);
    }

    public void menu() throws DataAccessException {
        boolean seguir = true;

        while (seguir) {
            System.out.println("\nMENU DE OPCIONES");
            System.out.println("1. Listar clientes");
            System.out.println("2. Listar zonas de envio");
            System.out.println("3. Ver datos cliente");
            System.out.println("4. Añadir cliente");
            System.out.println("5. Editar cliente");
            System.out.println("6. Borrar cliente");
            System.out.println("7. Agregar pedido");
            System.out.println("8. Borrar pedido");
            System.out.println("9. Editar pedido");
            System.out.println("10. Salir del menú");
            System.out.print("Elije una opción: ");

            int respuesta = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (respuesta) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    listarZonasEnvio();
                    break;
                case 3:
                    verDatosCliente();
                    break;
                case 4:
                    nuevoCliente();
                    break;
                case 5:
                    modificarCliente();
                    break;
                case 6:
                    eliminarCliente();
                    break;
                case 7:
                    nuevoPedido();
                    break;
                case 8:
                    eliminarPedido();
                    break;
                case 9:
                    modificarPedido();
                    break;
                case 10:
                    seguir = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor selecciona una entre las disponibles");
                    break;
            }
        }

        scanner.close();
    }

    public void listarZonasEnvio() throws DataAccessException {
        for (ZonaEnvio zona : connection.getZonaEnvio().get().toList()) {
            System.out.println("ID: " + zona.getId() + " Nombre: " + zona.getNombre() + " Tarifa envio: " + zona.getTarifa());
        }
    }

    public void listarClientes() throws DataAccessException {
        for (Cliente cliente : connection.getCliente().get().toList()) {
            System.out.println("ID: " + cliente.getId() + ", Nombre: " + cliente.getNombre() + ", Email: " + cliente.getEmail() + ", Teléfono: " + cliente.getTelefono() + ", Zona de envío: ID: " + cliente.getId_zona().getId() + " Nombre: " + cliente.getId_zona().getNombre() + " Tarifa envio: " + cliente.getId_zona().getTarifa());
        }
    }

    public void verDatosCliente() throws DataAccessException {
        try {
            System.out.print("Ingrese el ID del cliente (número positivo): ");
            int id = Integer.parseInt(scanner.nextLine());

            if (id <= 0) {
                throw new DataAccessException("El ID debe ser positivo");
            }

            Cliente cliente = connection.getCliente().get(id)
                    .orElseThrow(() -> new DataAccessException("No se ha podido encontrar el cliente"));

            System.out.println("Pedidos del cliente " + cliente.getNombre() + ":");

            List<Pedido> pedidos = connection.getPedido().get()
                    .filter(pedido -> pedido.getId_Cliente().getId() == id)
                    .collect(Collectors.toList());

            double total = 0.0;
            if (pedidos.isEmpty()) {
                System.out.println("El cliente no tiene pedidos registrados");
            } else {
                for (Pedido pedido : pedidos) {
                    System.out.println("ID: " + pedido.getId() +
                            ", Fecha: " + pedido.getFecha() +
                            ", Importe Total: " + pedido.getImporte_total() + " EUR" +
                            " , Cliente: " + pedido.getId_Cliente().getNombre());
                    total += pedido.getImporte_total();
                }
            }

            System.out.println("Total gastado: " + total + " EUR");
        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un número entero válido");
        }
    }

    public void nuevoCliente() throws DataAccessException {
        System.out.print("Escribe un ID para el cliente: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id <= 0) {
            throw new DataAccessException("El ID debe ser positivo");
        }

        System.out.print("Escribe un nombre para el cliente: ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) {
            throw new DataAccessException("Debes escribir un nombre para el cliente");
        }

        System.out.print("Escribe un email para el cliente: ");
        String email = scanner.nextLine();
        if (!email.contains("@")) {
            throw new DataAccessException("Para que el email sea válido debe contener un @");
        }

        System.out.print("Escribe un teléfono para el cliente: ");
        String telefono = scanner.nextLine();
        if (telefono.isEmpty()) {
            throw new DataAccessException("Debes escribir un telefono para el cliente");
        }

        System.out.print("Escribe el ID de la zona para el envío del pedido: ");
        int id_zona = Integer.parseInt(scanner.nextLine());

        if (id_zona <= 0) {
            throw new DataAccessException("El ID de la zona debe ser positivo");
        }

        ZonaEnvio zonaEnvio = connection.getZonaEnvio().get(id_zona)
                .orElseThrow(() -> new DataAccessException("No se ha podido encontrar la zona de envío con ese ID"));

        Cliente cliente = new Cliente(id, nombre, email, telefono, zonaEnvio);

        connection.getCliente().insert(cliente);
        System.out.println("El cliente ha sido añadido");
    }

    public void modificarCliente() throws DataAccessException {
        System.out.print("Escribe el ID del cliente que se quiere modificar: ");
        int id_cliente = Integer.parseInt(scanner.nextLine());

        if (id_cliente <= 0) {
            throw new DataAccessException("El ID debe ser positivo");
        }

        Cliente cliente = connection.getCliente().get(id_cliente)
                .orElseThrow(() -> new DataAccessException("No se encontró el cliente con ese ID"));

        System.out.print("Escribe un nuevo nombre para el cliente: ");
        String nombre = scanner.nextLine();

        if (nombre.isEmpty()) {
            throw new DataAccessException("Nombre inválido");
        } else {
            cliente.setNombre(nombre);
        }

        System.out.print("Escribe un nuevo email para el cliente: ");
        String email = scanner.nextLine();

        if (!email.contains("@")) {
            throw new DataAccessException("Para que el email sea válido debe contener un @");
        }
        cliente.setEmail(email);

        System.out.print("Escribe un nuevo teléfono para el cliente: ");
        String telefono = scanner.nextLine();
        if (telefono.isEmpty()) {
            throw new DataAccessException("Debes escribir un telefono para el cliente");
        }
        cliente.setTelefono(telefono);

        System.out.print("Escribe un nuevo ID de la zona para el envío del pedido: ");
        int id_zona = Integer.parseInt(scanner.nextLine());

        if (id_zona <= 0) {
            throw new DataAccessException("El ID de la zona debe ser positivo");
        }

        ZonaEnvio zonaEnvio = connection.getZonaEnvio().get(id_zona)
                .orElseThrow(() -> new DataAccessException("No se ha podido encontrar la zona de envío con ese ID"));
        cliente.setId_zona(zonaEnvio);

        if (!connection.getCliente().update(cliente)) {
            throw new DataAccessException("No se han podido modificar los datos del cliente");
        }

        System.out.println("El cliente ha sido modificado");
    }

    public void eliminarCliente() throws DataAccessException {
        System.out.print("Escribe el ID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id <= 0) {
            throw new DataAccessException("El ID del cliente debe ser mayor que 0");
        }

        if (connection.getCliente().delete(id)) {
            System.out.println("El cliente ha sido eliminado");
        } else {
            System.out.println("No se ha podido encontrar un cliente con ese ID");
        }
    }

    public void nuevoPedido() throws DataAccessException {
        System.out.print("Escribe un ID para el pedido: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id <= 0) {
            throw new DataAccessException("El ID debe ser positivo");
        }

        System.out.print("Escribe la fecha del pedido (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        System.out.print("Escribe el importe total del pedido: ");
        float importe_total = Float.parseFloat(scanner.nextLine());

        System.out.print("Escribe el ID del cliente para el pedido: ");
        int id_cliente = Integer.parseInt(scanner.nextLine());

        if (id_cliente <= 0) {
            throw new DataAccessException("El ID del cliente debe ser positivo");
        }

        Cliente cliente = connection.getCliente().get(id_cliente)
                .orElseThrow(() -> new DataAccessException("No se ha podido encontrar el cliente con ese ID"));

        Pedido pedido = new Pedido(id, fecha, importe_total, cliente);

        connection.getPedido().insert(pedido);
        System.out.println("El pedido ha sido añadido");
    }

    public void modificarPedido() throws DataAccessException {
        System.out.print("Escribe el ID del pedido que se quiere modificar: ");
        int id_pedido = Integer.parseInt(scanner.nextLine());

        if (id_pedido <= 0) {
            throw new DataAccessException("El ID debe ser positivo");
        }

        Pedido pedido = connection.getPedido().get(id_pedido)
                .orElseThrow(() -> new DataAccessException("No se encontró el pedido con ese ID"));

        System.out.print("Escribe una nueva fecha para el pedido (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());
        pedido.setFecha(fecha);

        System.out.print("Escribe un nuevo importe total para el pedido: ");
        float importe_total = Float.parseFloat(scanner.nextLine());
        pedido.setImporte_total(importe_total);

        System.out.print("Escribe un nuevo ID del cliente para el pedido: ");
        int id_cliente = Integer.parseInt(scanner.nextLine());

        if (id_cliente <= 0) {
            throw new DataAccessException("El ID del cliente debe ser positivo");
        }

        Cliente cliente = connection.getCliente().get(id_cliente)
                .orElseThrow(() -> new DataAccessException("No se ha podido encontrar el cliente con ese ID"));
        pedido.setId_Cliente(cliente);

        if (!connection.getPedido().update(pedido)) {
            throw new DataAccessException("No se han podido modificar los datos del pedido");
        }

        System.out.println("El pedido ha sido modificado");
    }

    public void eliminarPedido() throws DataAccessException {
        System.out.print("Escribe el ID del pedido a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id <= 0) {
            throw new DataAccessException("El ID del pedido debe ser mayor que 0");
        }

        if (connection.getPedido().delete(id)) {
            System.out.println("El pedido ha sido eliminado");
        } else {
            System.out.println("No se ha podido encontrar un pedido con ese ID");
        }
    }
}