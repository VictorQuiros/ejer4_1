package ies.castillodeluna.ad.backend.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import edu.acceso.sqlutils.errors.DataAccessException;
import edu.acceso.sqlutils.SqlUtils;
import edu.acceso.sqlutils.dao.Crud;
import ies.castillodeluna.ad.modelo.Cliente;
import ies.castillodeluna.ad.modelo.ZonaEnvio;

/**
 * Implementation of CRUD operations for Cliente entities using SQLite database.
 * This class provides methods to create, read, update and delete Cliente records.
 */
public class ClienteSqlite implements Crud<Cliente> {

    private DataSource ds;
    
    /**
     * Constructs a new ClienteSqlite with the specified DataSource.
     *
     * @param ds The DataSource to be used for database connections
     */
    public ClienteSqlite(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Converts a ResultSet row to a Cliente object.
     *
     * @param rs The ResultSet containing cliente data
     * @param ds The DataSource instance
     * @return A new Cliente object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private Cliente resultToCliente(ResultSet rs, DataSource ds) throws SQLException {
        int id = rs.getInt("id_cliente");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String telefono = rs.getString("telefono");
        int idZona = rs.getInt("id_zona");
        
        ZonaEnvio zonaEnvio = new ZonaEnvio();
        zonaEnvio.setId(idZona);

        return new Cliente(id, nombre, email, telefono, zonaEnvio);
    }
    
    /**
     * Sets the parameters of a PreparedStatement with Cliente data.
     *
     * @param cliente The Cliente object containing the data
     * @param pstmt The PreparedStatement to set parameters for
     * @throws SQLException if a database access error occurs
     */
    private static void setClienteParams(Cliente cliente, PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, cliente.getId());
        pstmt.setString(2, cliente.getNombre());
        pstmt.setString(3, cliente.getEmail());
        pstmt.setString(4, cliente.getTelefono());
        pstmt.setObject(5, cliente.getId_zona().getId());
    }

    /**
     * Deletes a cliente record from the database.
     *
     * @param id The ID of the cliente to delete
     * @return true if the deletion was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Clientes WHERE id_cliente = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Retrieves all cliente records from the database.
     *
     * @return A Stream of Cliente objects
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Stream<Cliente> get() throws DataAccessException {
        final String sqlString = "SELECT * FROM Clientes";

        try {
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);

            return SqlUtils.resultSetToStream(conn, rs, fila -> {
                try {
                    return resultToCliente(fila, ds);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Retrieves a specific cliente record by ID.
     *
     * @param id The ID of the cliente to retrieve
     * @return An Optional containing the Cliente if found, empty Optional otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Optional<Cliente> get(int id) throws DataAccessException {
        final String sqlString = "SELECT * FROM Clientes WHERE id_cliente = ?";

        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(resultToCliente(rs, ds)) : Optional.empty();
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Inserts a new cliente record into the database.
     *
     * @param cliente The Cliente object to insert
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public void insert(Cliente cliente) throws DataAccessException {
        final String sqlString = "INSERT INTO Clientes (id_cliente, nombre, email, telefono, id_zona) VALUES (?, ?, ?, ?, ?)";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setClienteParams(cliente, pstmt);
            pstmt.executeUpdate();
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates an existing cliente record in the database.
     *
     * @param cliente The Cliente object containing updated data
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(Cliente cliente) throws DataAccessException {
        final String sqlString = "UPDATE Clientes SET id_cliente = ?, nombre = ?, email = ?, telefono = ?, id_zona = ? WHERE id_cliente = ?";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setClienteParams(cliente, pstmt);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates the ID of an existing cliente record.
     *
     * @param oldId The current ID of the cliente
     * @param newId The new ID to set
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Cliente SET id_cliente = ? WHERE id_cliente = ?";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, newId);
            pstmt.setInt(2, oldId);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }
}