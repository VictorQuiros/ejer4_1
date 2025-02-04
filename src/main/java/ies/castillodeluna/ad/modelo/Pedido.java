package ies.castillodeluna.ad.modelo;

import java.time.LocalDate;
import edu.acceso.sqlutils.Entity;

/**
 * Representa un pedido en el sistema.
 * Contiene la información básica de un pedido incluyendo su fecha, importe y cliente asociado.
 * Implementa la interfaz Entity para su integración con el sistema de persistencia.
 */
public class Pedido implements Entity {
    
    /** Identificador único del pedido */
    private int id;
    
    /** Fecha en que se realizó el pedido */
    private LocalDate fecha;
    
    /** Importe total del pedido */
    private float importe_total;

    /** Cliente que realizó el pedido */
    private Cliente id_Cliente;

    /**
     * Constructor por defecto.
     * Crea una instancia de Pedido sin inicializar sus campos.
     */
    public Pedido() {
    }

    /**
     * Constructor con todos los campos.
     * 
     * @param id Identificador único del pedido
     * @param fecha Fecha del pedido
     * @param importe_total Importe total del pedido
     * @param id_Cliente Cliente que realizó el pedido
     */
    public Pedido(int id, LocalDate fecha, float importe_total, Cliente id_Cliente) {
        this.id = id;
        this.fecha = fecha;
        this.importe_total = importe_total;
        this.id_Cliente = id_Cliente;
    }

    /**
     * Obtiene el identificador del pedido.
     * @return el identificador único del pedido
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del pedido.
     * @param id el nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha del pedido.
     * @return la fecha en que se realizó el pedido
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del pedido.
     * @param fecha la nueva fecha
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el importe total del pedido.
     * @return el importe total
     */
    public float getImporte_total() {
        return importe_total;
    }

    /**
     * Establece el importe total del pedido.
     * @param importe_total el nuevo importe total
     */
    public void setImporte_total(float importe_total) {
        this.importe_total = importe_total;
    }
    
    /**
     * Obtiene el cliente que realizó el pedido.
     * @return el cliente asociado al pedido
     */
    public Cliente getId_Cliente() {
        return id_Cliente;
    }

    /**
     * Establece el cliente del pedido.
     * @param id_Cliente el nuevo cliente
     */
    public void setId_Cliente(Cliente id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    /**
     * Devuelve una representación en cadena del pedido.
     * Incluye todos los campos del pedido en un formato legible.
     * 
     * @return una cadena con toda la información del pedido
     */
    @Override
    public String toString() {
        return "Pedido [id=" + id + ", fecha=" + fecha + ", importe_total=" + importe_total + ", id_Cliente="
                + id_Cliente + "]";
    }
}