package ies.castillodeluna.ad;

import java.util.Map;

import edu.acceso.sqlutils.errors.DataAccessException;
import ies.castillodeluna.ad.Interfaz.InterfazUsuario;
import ies.castillodeluna.ad.backend.sqlite.ConnectionSqlite;

public class Main {
    /**
     * Método principal que inicia la aplicación.
     * Configura las opciones de conexión a la base de datos SQLite y
     * lanza la interfaz de usuario con el menú principal.
     * 
     * @param args argumentos de línea de comando (no utilizados)
     * @throws DataAccessException si ocurre un error al acceder a la base de datos
     */
    public static void main(String[] args) throws DataAccessException {
        // Obtiene las opciones de conexión para SQLite
        Map<String, Object> map = ConnectionSqlite.connectionOptions();
        
        // Inicializa la interfaz de usuario con las opciones de conexión
        InterfazUsuario interfaz = new InterfazUsuario(map);
        
        // Lanza el menú principal
        interfaz.menu();
    }
}