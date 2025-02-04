package ies.castillodeluna.ad.backend.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import edu.acceso.sqlutils.dao.Crud;
import edu.acceso.sqlutils.errors.DataAccessException;
import ies.castillodeluna.ad.modelo.ZonaEnvio;

/**
 * Implementation of CRUD operations for ZonaEnvio entities using SQLite database.
 * This class provides methods to create, read, update and delete shipping zone records.
 */
public class ZonaEnvioSqlite implements Crud<ZonaEnvio> {

    private DataSource ds;
    
    /**
     * Constructs a new ZonaEnvioSqlite with the specified DataSource.
     *
     * @param ds The DataSource to be used for database connections
     */
    public ZonaEnvioSqlite(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Converts a ResultSet row to a ZonaEnvio object.
     *
     * @param rs The ResultSet containing shipping zone data
     * @param ds The DataSource instance
     * @return A new ZonaEnvio object populated with data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private ZonaEnvio resultToZonaEnvio(ResultSet rs, DataSource ds) throws SQLException {
        int id_zona = rs.getInt("id_zona");
        String nombre_zona = rs.getString("nombre_zona");
        Double tarifa_envio = rs.getDouble("tarifa_envio");

        return new ZonaEnvio(id_zona, nombre_zona, tarifa_envio);
    }

    /**
     * Sets the parameters of a PreparedStatement with ZonaEnvio data.
     *
     * @param zonaEnvio The ZonaEnvio object containing the data
     * @param pstmt The PreparedStatement to set parameters for
     * @throws SQLException if a database access error occurs
     */
    private static void setZonaEnvioParams(ZonaEnvio zonaEnvio, PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, zonaEnvio.getId());
        pstmt.setString(2, zonaEnvio.getNombre());
        pstmt.setDouble(3, zonaEnvio.getTarifa());
    }

    /**
     * Deletes a shipping zone record from the database.
     *
     * @param id The ID of the shipping zone to delete
     * @return true if the deletion was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Zonas_Envio WHERE id_zona = ?";

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
     * Retrieves all shipping zone records from the database.
     *
     * @return A Stream of ZonaEnvio objects
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Stream<ZonaEnvio> get() throws DataAccessException {
        final String sqlString = "SELECT * FROM Zonas_Envio";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
            ResultSet rs = pstmt.executeQuery();
        ) {
            Stream.Builder<ZonaEnvio> builder = Stream.builder();
            while (rs.next()) {
                builder.add(resultToZonaEnvio(rs, ds));
            }
            return builder.build();
        } catch (SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Retrieves a specific shipping zone record by ID.
     *
     * @param id The ID of the shipping zone to retrieve
     * @return An Optional containing the ZonaEnvio if found, empty Optional otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public Optional<ZonaEnvio> get(int id) throws DataAccessException {
        final String sqlString = "SELECT * FROM Zonas_Envio WHERE id_zona = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(resultToZonaEnvio(rs, ds));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Inserts a new shipping zone record into the database.
     *
     * @param zonaEnvio The ZonaEnvio object to insert
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public void insert(ZonaEnvio zonaEnvio) throws DataAccessException {
        final String sqlString = "INSERT INTO Zonas_Envio (id_zona, nombre_zona, tarifa_envio) VALUES (?, ?, ?)";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setZonaEnvioParams(zonaEnvio, pstmt);
            pstmt.executeUpdate();
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates an existing shipping zone record in the database.
     *
     * @param zonaEnvio The ZonaEnvio object containing updated data
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(ZonaEnvio zonaEnvio) throws DataAccessException {
        final String sqlString = "UPDATE Zonas_Envio SET id_zona = ?, nombre_zona = ?, tarifa_envio = ? WHERE id_zona = ?";
        
        try(
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setZonaEnvioParams(zonaEnvio, pstmt);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    /**
     * Updates the ID of an existing shipping zone record.
     *
     * @param oldId The current ID of the shipping zone
     * @param newId The new ID to set
     * @return true if the update was successful, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Zonas_Envio SET id_zona = ? WHERE id_zona = ?";
        
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