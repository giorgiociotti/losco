package com.generation.dao;

import com.generation.database.Database;
import com.generation.models.Armi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArmiDao {

    private Connection connection;

    public ArmiDao() {
        this.connection = Database.getInstance().getConnection();
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public void insert(Armi a) {
        String sql = "INSERT INTO armi (nome, caricatore, calibro, automatica, colore, id_criminale) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNome());
            ps.setInt(2, a.getCaricatore());
            ps.setString(3, a.getCalibre());
            ps.setBoolean(4, a.isAutomatica());
            ps.setString(5, a.getColore());
            if (a.getIdCriminale() != null) ps.setInt(6, a.getIdCriminale());
            else ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) a.setId(keys.getInt(1));
            System.out.println("Arma inserita con id=" + a.getId());
        } catch (SQLException e) {
            System.out.println("Errore insert arma: " + e.getMessage());
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    public List<Armi> findAll() {
        List<Armi> lista = new ArrayList<>();
        String sql = "SELECT * FROM armi";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Errore findAll armi: " + e.getMessage());
        }
        return lista;
    }

    public Armi findById(int id) {
        String sql = "SELECT * FROM armi WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Errore findById arma: " + e.getMessage());
        }
        return null;
    }

    public List<Armi> findByCriminale(int idCriminale) {
        List<Armi> lista = new ArrayList<>();
        String sql = "SELECT * FROM armi WHERE id_criminale = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCriminale);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Errore findByCriminale: " + e.getMessage());
        }
        return lista;
    }

    public List<Armi> findSenzaProprietario() {
        List<Armi> lista = new ArrayList<>();
        String sql = "SELECT * FROM armi WHERE id_criminale IS NULL";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Errore findSenzaProprietario: " + e.getMessage());
        }
        return lista;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public void update(Armi a) {
        String sql = "UPDATE armi SET nome=?, caricatore=?, calibro=?, automatica=?, colore=?, id_criminale=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setInt(2, a.getCaricatore());
            ps.setString(3, a.getCalibre());
            ps.setBoolean(4, a.isAutomatica());
            ps.setString(5, a.getColore());
            if (a.getIdCriminale() != null) ps.setInt(6, a.getIdCriminale());
            else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, a.getId());
            ps.executeUpdate();
            System.out.println("Arma aggiornata.");
        } catch (SQLException e) {
            System.out.println("Errore update arma: " + e.getMessage());
        }
    }

    // Setta id_criminale = NULL su tutte le armi di un criminale
    // (necessario prima di eliminare il criminale, dato che la FK è NO ACTION)
    public void dissociaTutteDiCriminale(int idCriminale) {
        String sql = "UPDATE armi SET id_criminale = NULL WHERE id_criminale = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCriminale);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore dissociaTutte: " + e.getMessage());
        }
    }

    public void dissociaProprietario(int idArma) {
        String sql = "UPDATE armi SET id_criminale = NULL WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idArma);
            ps.executeUpdate();
            System.out.println("Proprietario rimosso dall'arma.");
        } catch (SQLException e) {
            System.out.println("Errore dissociaProprietario: " + e.getMessage());
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void delete(int id) {
        String sql = "DELETE FROM armi WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Arma eliminata.");
        } catch (SQLException e) {
            System.out.println("Errore delete arma: " + e.getMessage());
        }
    }

    // ── UTILITY ───────────────────────────────────────────────────────────────

    private Armi mapRow(ResultSet rs) throws SQLException {
        Armi a = new Armi();
        a.setId(rs.getInt("id"));
        a.setNome(rs.getString("nome"));
        a.setCaricatore(rs.getInt("caricatore"));
        a.setCalibre(rs.getString("calibro"));
        a.setAutomatica(rs.getBoolean("automatica"));
        a.setColore(rs.getString("colore"));
        int idCrim = rs.getInt("id_criminale");
        a.setIdCriminale(rs.wasNull() ? null : idCrim);
        return a;
    }
}
