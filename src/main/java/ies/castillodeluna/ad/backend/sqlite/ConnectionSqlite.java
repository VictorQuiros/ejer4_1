package ies.castillodeluna.ad.backend.sqlite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import edu.acceso.sqlutils.SqlUtils;
import edu.acceso.sqlutils.dao.Crud;
import edu.acceso.sqlutils.errors.DataAccessException;
import ies.castillodeluna.ad.backend.Connect;
import ies.castillodeluna.ad.modelo.Cliente;
import ies.castillodeluna.ad.modelo.Pedido;
import ies.castillodeluna.ad.modelo.ZonaEnvio;

/**
 * Implementación de la interfaz Connect para gestionar conexiones SQLite.
 * Proporciona acceso a las operaciones CRUD para Clientes, Pedidos y Zonas de Envío,
 * además de gestionar la inicialización de la base de datos.
 */
public class ConnectionSqlite implements Connect {

    /** Ruta al archivo SQL con el esquema de la base de datos */
    static Path ruta = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "pedidos.sql");
    
    /** URL de conexión a la base de datos SQLite */
    static final String URL = "jdbc:sqlite:" + ruta.toString();

    /** Pool de conexiones SQLite */
    private SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource();

    /**
     * Constructor que inicializa la conexión a la base de datos.
     * 
     * @param url URL de conexión a la base de datos SQLite
     * @throws DataAccessException si hay errores al iniciar la base de datos
     */
    public ConnectionSqlite(String url) throws DataAccessException {
        ds.setUrl(url);
        iniciarBaseDatos();
    }

    /**
     * Obtiene una implementación CRUD para gestionar Clientes.
     * 
     * @return Objeto que implementa operaciones CRUD para Clientes
     */
    @Override
    public Crud<Cliente> getCliente() {
        return new ClienteSqlite(ds);
    }

    /**
     * Obtiene una implementación CRUD para gestionar Pedidos.
     * 
     * @return Objeto que implementa operaciones CRUD para Pedidos
     */
    @Override
    public Crud<Pedido> getPedido() {
        return new PedidoSqlite(ds);
    }

    /**
     * Obtiene una implementación CRUD para gestionar Zonas de Envío.
     * 
     * @return Objeto que implementa operaciones CRUD para Zonas de Envío
     */
    @Override
    public Crud<ZonaEnvio> getZonaEnvio() {
        return new ZonaEnvioSqlite(ds);
    }

    /**
     * Verifica si la base de datos está inicializada y la crea si no existe.
     * Intenta obtener los clientes y si falla, crea el esquema de la base de datos.
     * 
     * @throws DataAccessException si hay errores al acceder o crear la base de datos
     */
    private void iniciarBaseDatos() throws DataAccessException {
        try (Stream<Cliente> clientes = getCliente().get()) {
            clientes.close();
        } catch (DataAccessException err) {
            esquema();
        }
    }

    /**
     * Crea el esquema de la base de datos utilizando el archivo SQL especificado.
     * 
     * @throws DataAccessException si hay errores al crear el esquema o acceder al archivo SQL
     */
    private void esquema() throws DataAccessException {
        try (InputStream esquema = Files.newInputStream(ruta);
             Connection conn = ds.getConnection()) {
            SqlUtils.executeSQL(conn, esquema);
        } catch (SQLException e) {
            throw new DataAccessException("No puede crearse el esquema de la base de datos", e);
        } catch (IOException e) {
            throw new DataAccessException(String.format("No puede acceder al esquema: %s", ruta), e);
        }
    }

    /**
     * Proporciona las opciones de conexión para la base de datos SQLite.
     * 
     * @return Mapa con las opciones de conexión incluyendo base, url, usuario y contraseña
     */
    public static Map<String, Object> connectionOptions() {
        Path ruta = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "pedidos.db");
        String URL = String.format("jdbc:sqlite:%s", ruta.toString());
        
        return Map.of(
            "base", "sqlite",
            "url", URL,
            "user", "",
            "password", ""
        );
    }
}