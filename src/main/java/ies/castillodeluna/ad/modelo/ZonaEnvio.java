package ies.castillodeluna.ad.modelo;

import edu.acceso.sqlutils.Entity;

/**
 * Representa una zona de envío en el sistema.
 * Contiene la información sobre una zona geográfica de entrega y su tarifa asociada.
 * Implementa la interfaz Entity para su integración con el sistema de persistencia.
 */
public class ZonaEnvio implements Entity {
    
    /** Identificador único de la zona de envío */
    private int id;
    
    /** Nombre o descripción de la zona de envío */
    private String nombre;
    
    /** Tarifa de envío aplicable a la zona */
    private Double tarifa;

    /**
     * Constructor por defecto.
     * Crea una instancia de ZonaEnvio sin inicializar sus campos.
     */
    public ZonaEnvio() {
    }

    /**
     * Constructor con todos los campos.
     * 
     * @param id Identificador único de la zona
     * @param nombre Nombre o descripción de la zona
     * @param tarifa Tarifa de envío para la zona
     */
    public ZonaEnvio(int id, String nombre, Double tarifa) {
        this.id = id;
        this.nombre = nombre;
        this.tarifa = tarifa;
    }

    /**
     * Obtiene el identificador de la zona.
     * @return el identificador único de la zona
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador de la zona.
     * @param id el nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la zona.
     * @return el nombre o descripción de la zona
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la zona.
     * @param nombre el nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la tarifa de envío de la zona.
     * @return la tarifa aplicable a la zona
     */
    public Double getTarifa() {
        return tarifa;
    }

    /**
     * Establece la tarifa de envío de la zona.
     * @param tarifa la nueva tarifa
     */
    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }
}