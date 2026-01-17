// src/EmpruntDAO.java
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {
    
    // 1. Enregistrer un nouvel emprunt
    public boolean enregistrerEmprunt(Emprunt emprunt) {
        String sql = "INSERT INTO emprunts (membre_id, livre_id, date_emprunt, " +
                    "date_retour_prevue) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, emprunt.getMembreId());
            pstmt.setInt(2, emprunt.getLivreId());
            pstmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    emprunt.setId(rs.getInt(1));
                }
                System.out.println("✅ Emprunt enregistré avec succès! ID: " + emprunt.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur d'enregistrement: " + e.getMessage());
        }
        return false;
    }
    
    // 2. Enregistrer le retour d'un livre
    public boolean retournerLivre(int empruntId) {
        String sql = "UPDATE emprunts SET date_retour_effective = ?, " +
                    "penalite = ? WHERE id = ? AND date_retour_effective IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Créer un objet temporaire pour calculer la pénalité
            Emprunt emprunt = trouverParId(empruntId);
            if (emprunt == null) {
                System.out.println("❌ Emprunt non trouvé!");
                return false;
            }
            
            emprunt.setDateRetourEffective(LocalDate.now());
            double penalite = emprunt.calculerPenalite();
            
            pstmt.setDate(1, Date.valueOf(emprunt.getDateRetourEffective()));
            pstmt.setDouble(2, penalite);
            pstmt.setInt(3, empruntId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Livre retourné avec succès!");
                if (penalite > 0) {
                    System.out.println("⚠️  Pénalité à payer: " + penalite + " FCFA");
                }
                return true;
            } else {
                System.out.println("❌ Cet emprunt a déjà été retourné!");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de retour: " + e.getMessage());
        }
        return false;
    }
    
    // 3. Trouver un emprunt par ID
    public Emprunt trouverParId(int id) {
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Emprunt(
                    rs.getInt("id"),
                    rs.getInt("membre_id"),
                    rs.getInt("livre_id"),
                    rs.getDate("date_emprunt").toLocalDate(),
                    rs.getDate("date_retour_prevue").toLocalDate(),
                    rs.getDate("date_retour_effective") != null ? 
                        rs.getDate("date_retour_effective").toLocalDate() : null
                );
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        return null;
    }
    
    // 4. Lister tous les emprunts en cours
    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL " +
                    "ORDER BY date_retour_prevue";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                    rs.getInt("id"),
                    rs.getInt("membre_id"),
                    rs.getInt("livre_id"),
                    rs.getDate("date_emprunt").toLocalDate(),
                    rs.getDate("date_retour_prevue").toLocalDate(),
                    null
                );
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        
        return emprunts;
    }
    
    // 5. Lister tous les emprunts
    public List<Emprunt> getAllEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts ORDER BY date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                    rs.getInt("id"),
                    rs.getInt("membre_id"),
                    rs.getInt("livre_id"),
                    rs.getDate("date_emprunt").toLocalDate(),
                    rs.getDate("date_retour_prevue").toLocalDate(),
                    rs.getDate("date_retour_effective") != null ? 
                        rs.getDate("date_retour_effective").toLocalDate() : null
                );
                emprunts.add(emprunt);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        
        return emprunts;
    }
    
    // 6. Vérifier si un livre est disponible
    public boolean estLivreDisponible(int livreId) {
        String sql = "SELECT COUNT(*) FROM emprunts " +
                    "WHERE livre_id = ? AND date_retour_effective IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, livreId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // Disponible si aucun emprunt en cours
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        return false;
    }
}