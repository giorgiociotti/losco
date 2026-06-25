package com.generation.dao;

import com.generation.database.Database;
import com.generation.models.Armi;
import com.generation.models.Criminali;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CriminaleDao {

    private Connection connection;

    public CriminaleDao() {
        this.connection = Database.getInstance().getConnection();
    }

    // CREATE

    public void insert(Criminali c) {
        String sql = "INSERT INTO criminali (nome, cognome, alias, dob, gruppo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCognome());
            ps.setString(3, c.getAlias());
            ps.setDate(4, Date.valueOf(c.getDob()));
            ps.setString(5, c.getGruppo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next())
                c.setId(keys.getInt(1));
            System.out.println("Criminale inserito con id=" + c.getId());
        } catch (SQLException e) {
            System.out.println("Errore insert criminale: " + e.getMessage());
        }
    }

    // READ

    public List<Criminali> findAll() {
        List<Criminali> lista = new ArrayList<>();
        String sql = "SELECT * FROM criminali";
        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                lista.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Errore findAll criminali: " + e.getMessage());
        }
        return lista;
    }

    public Criminali findById(int id) {
        String sql = "SELECT * FROM criminali WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Errore findById criminale: " + e.getMessage());
        }
        return null;
    }

    public List<Criminali> findByGruppo(String gruppo) {
        List<Criminali> lista = new ArrayList<>();
        String sql = "SELECT * FROM criminali WHERE gruppo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, gruppo);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                lista.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Errore findByGruppo: " + e.getMessage());
        }
        return lista;
    }

    // LEFT JOIN per caricare criminali + armi in una sola query
    public List<Criminali> findAllWithArmi() {
        List<Criminali> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.nome, c.cognome, c.alias, c.dob, c.gruppo, " +
                "       a.id AS arma_id, a.nome AS arma_nome, a.caricatore, " +
                "       a.calibro, a.automatica, a.colore, a.id_criminale " +
                "FROM criminali c " +
                "LEFT JOIN armi a ON a.id_criminale = c.id " +
                "ORDER BY c.id";
        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            Criminali current = null;
            while (rs.next()) {
                int cid = rs.getInt("id");
                if (current == null || current.getId() != cid) {
                    current = mapRow(rs);
                    lista.add(current);
                }
                if (rs.getObject("arma_id") != null) {
                    Armi a = new Armi();
                    a.setId(rs.getInt("arma_id"));
                    a.setNome(rs.getString("arma_nome"));
                    a.setCaricatore(rs.getInt("caricatore"));
                    a.setCalibre(rs.getString("calibro"));
                    a.setAutomatica(rs.getBoolean("automatica"));
                    a.setColore(rs.getString("colore"));
                    int idCrim = rs.getInt("id_criminale");
                    a.setIdCriminale(rs.wasNull() ? null : idCrim);
                    current.getArmi().add(a);
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore findAllWithArmi: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE

    public void update(Criminali c) {
        String sql = "UPDATE criminali SET nome=?, cognome=?, alias=?, dob=?, gruppo=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCognome());
            ps.setString(3, c.getAlias());
            ps.setDate(4, Date.valueOf(c.getDob()));
            ps.setString(5, c.getGruppo());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
            System.out.println("Criminale aggiornato.");
        } catch (SQLException e) {
            System.out.println("Errore update criminale: " + e.getMessage());
        }
    }

    // DELETE

    public void delete(int id) {
        String sql = "DELETE FROM criminali WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Criminale eliminato.");
        } catch (SQLException e) {
            System.out.println("Errore delete criminale: " + e.getMessage());
        }
    }

    // UTILITY

    private Criminali mapRow(ResultSet rs) throws SQLException {
        Criminali c = new Criminali();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCognome(rs.getString("cognome"));
        c.setAlias(rs.getString("alias"));
        Date d = rs.getDate("dob");
        c.setDob(d != null ? d.toLocalDate() : null);
        c.setGruppo(rs.getString("gruppo"));
        return c;
    }
}
