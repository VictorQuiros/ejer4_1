package ies.castillodeluna.ad.backend.sqlite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import edu.acceso.sqlutils.SqlUtils;
import edu.acceso.sqlutils.dao.Crud;
import edu.acceso.sqlutils.errors.DataAccessException;
import ies.castillodeluna.ad.modelo.Cliente;
import ies.castillodeluna.ad.modelo.Pedido;

/**
 * Implementation of CRUD operations for Pedido entities using SQLite database.
 * This class provides methods to create, read, update and delete Pedido records.
 */
public class PedidoSqlite implements Crud<Pedido> {

    private DataSource ds;
    
    /**
     * Constructs a new PedidoSqlite with the specified DataSource.
     *
     * @param ds The DataSource to be used for database connections
     */
    public PedidoSqlite(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Converts a ResultSet row to a Pedido object.
     *
     * @param rs The ResultSet containing pedido data
     * @param ds The DataSource instance
     * @return A new Pedido object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs
     * @throws DataAccessException if there's an error accessing related data
     */
    private Pedido resultToPedido(ResultSet rs, DataSource ds) throws SQLException, DataAccessException {
        int id = rs.getInt("id_pedido");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        float importeTotal = rs.getFloat("importe_total");
        int idCliente = rs.getInt("id_cliente");

        Cliente cliente = new ClienteSqlite(ds).get(idCliente).orElse(null);

        return new Pedido(id, fecha, importeTotal, cliente);
    }

    /**
     * Sets the parameters of a PreparedStatement with Pedido data.
     *
     * @param pedido The Pedido object containing the data
     * @param pstmt The PreparedStatement to set parameters for
     * @throws SQLException if a database access error occurs
     */
    private static void setPedidoParams(Pedido pedido, PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, pedido.getId());
        pstmt.setDate(2, Date.valueOf(pedido.getFecha()));
        pstmt.setFloat(3, pedido.getImporte_total());
        pstmt.setObject(4, pedido.getId_Cliente().getId());
    }

    /**
     * Deletes a pedido record from the database.
     *
     * @param id The ID of the pedido to delete
     * @return true if the deletion was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Pedidos WHERE id_pedido = ?";

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
     * Retrieves all pedido records from the database.
     *
     * @return A Stream of Pedido objects
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Stream<Pedido> get() throws DataAccessException {
        final String sqlString = "SELECT * FROM Pedidos";

        try {
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);

            return SqlUtils.resultSetToStream(conn, rs, fila -> {
                try {
                    return resultToPedido(fila, ds);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
                return null;
            });

        } catch (SQLException e) {
            throw new DataAccessException(e); 
        }
    }

    /**
     * Retrieves a specific pedido record by ID.
     *
     * @param id The ID of the pedido to retrieve
     * @return An Optional containing the Pedido if found, empty Optional otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Optional<Pedido> get(int id) throws DataAccessException {
        final String sqlString = "SELECT * FROM Pedidos WHERE id_pedido = ?";

        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(resultToPedido(rs, ds)) : Optional.empty();
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Inserts a new pedido record into the database.
     *
     * @param pedido The Pedido object to insert
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public void insert(Pedido pedido) throws DataAccessException {
        final String sqlString = "INSERT INTO Pedidos (id_pedido, fecha, importe_total, id_cliente) VALUES (?, ?, ?, ?)";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setPedidoParams(pedido, pstmt);
            pstmt.executeUpdate();
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates an existing pedido record in the database.
     *
     * @param pedido The Pedido object containing updated data
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(Pedido pedido) throws DataAccessException {
        final String sqlString = "UPDATE Pedidos SET fecha = ?, importe_total = ?, id_cliente = ? WHERE id_pedido = ?";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setPedidoParams(pedido, pstmt);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates the ID of an existing pedido record.
     *
     * @param oldId The current ID of the pedido
     * @param newId The new ID to set
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Pedidos SET id_pedido = ? WHERE id_pedido = ?";
        
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