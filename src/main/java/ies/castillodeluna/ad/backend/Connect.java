package ies.castillodeluna.ad.backend;

import edu.acceso.sqlutils.dao.Crud;
import ies.castillodeluna.ad.modelo.Cliente;
import ies.castillodeluna.ad.modelo.Pedido;
import ies.castillodeluna.ad.modelo.ZonaEnvio;

/**
 * Interfaz que define los métodos para acceder a las operaciones CRUD
 * de las diferentes entidades del sistema de pedidos.
 * Proporciona acceso a la gestión de Clientes, Pedidos y Zonas de Envío.
 */
public interface Connect {
    
    /**
     * Obtiene el gestor CRUD para las operaciones con Clientes.
     * 
     * @return Una implementación de Crud para la entidad Cliente
     */
    public Crud<Cliente> getCliente();

    /**
     * Obtiene el gestor CRUD para las operaciones con Pedidos.
     * 
     * @return Una implementación de Crud para la entidad Pedido
     */
    public Crud<Pedido> getPedido();

    /**
     * Obtiene el gestor CRUD para las operaciones con Zonas de Envío.
     * 
     * @return Una implementación de Crud para la entidad ZonaEnvio
     */
    public Crud<ZonaEnvio> getZonaEnvio();
}