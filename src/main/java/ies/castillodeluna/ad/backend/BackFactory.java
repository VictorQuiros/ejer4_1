package ies.castillodeluna.ad.backend;

import java.util.Map;

import ies.castillodeluna.ad.backend.sqlite.ConnectionSqlite;

/**
 * Fábrica para crear conexiones a diferentes tipos de bases de datos.
 * Actualmente soporta conexiones SQLite.
 */
public class BackFactory {
    
    /**
     * Crea una conexión a la base de datos según las opciones especificadas.
     * 
     * @param opciones Mapa con las opciones de conexión. Debe contener al menos:
     *                 - "base": tipo de base de datos (actualmente solo "sqlite")
     *                 - "url": URL de conexión a la base de datos
     * 
     * @return Una implementación de Connect para el tipo de base de datos especificado
     * 
     * @throws IllegalArgumentException si el tipo de base de datos no está especificado
     *                                  o si el tipo especificado no está soportado
     * @throws RuntimeException si ocurre un error al crear la conexión
     */
    public static Connect crearConexion(Map<String, Object> opciones) {
        String base = (String) opciones.get("base");
        
        if (base == null) {
            throw new IllegalArgumentException("No se ha especificado el tipo de base de datos");
        }
        
        if (base.equalsIgnoreCase("sqlite")) {
            try {
                String url = (String) opciones.get("url");
                return new ConnectionSqlite(url);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new IllegalArgumentException(String.format("'%s': formato desconocido", base));
    }
}