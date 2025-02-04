package ies.castillodeluna.ad.modelo;

import edu.acceso.sqlutils.Entity;

/**
 * Representa un cliente en el sistema de pedidos.
 * Contiene la información personal del cliente y su zona de envío asociada.
 * Implementa la interfaz Entity para su integración con el sistema de persistencia.
 */
public class Cliente implements Entity {
    
    /** Identificador único del cliente */
    private int id;
    
    /** Nombre completo del cliente */
    private String nombre;
    
    /** Dirección de correo electrónico del cliente */
    private String email;
    
    /** Número de teléfono del cliente */
    private String telefono;
    
    /** Zona de envío asignada al cliente */
    private ZonaEnvio id_zona;

    /**
     * Constructor por defecto.
     * Crea una instancia de Cliente sin inicializar sus campos.
     */
    public Cliente() {
    }

    /**
     * Constructor con todos los campos.
     * 
     * @param id Identificador único del cliente
     * @param nombre Nombre completo del cliente
     * @param email Dirección de correo electrónico
     * @param telefono Número de teléfono
     * @param id_zona Zona de envío asignada
     */
    public Cliente(int id, String nombre, String email, String telefono, ZonaEnvio id_zona) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.id_zona = id_zona;
    }

    /**
     * Obtiene el identificador del cliente.
     * @return el identificador único del cliente
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del cliente.
     * @param id el nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cliente.
     * @return el nombre completo del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     * @param nombre el nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el email del cliente.
     * @return la dirección de correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del cliente.
     * @param email la nueva dirección de correo
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el teléfono del cliente.
     * @return el número de teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del cliente.
     * @param telefono el nuevo número de teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la zona de envío del cliente.
     * @return la zona de envío asignada
     */
    public ZonaEnvio getId_zona() {
        return id_zona;
    }

    /**
     * Establece la zona de envío del cliente.
     * @param id_zona la nueva zona de envío
     */
    public void setId_zona(ZonaEnvio id_zona) {
        this.id_zona = id_zona;
    }

    /**
     * Devuelve una representación en cadena del cliente.
     * Incluye todos los campos del cliente en un formato legible.
     * 
     * @return una cadena con toda la información del cliente
     */
    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nombre=" + nombre + ", email=" + email + ", telefono=" + telefono + ", id_zona="
                + id_zona + "]";
    }
}