// src/MembreDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    
    // 1. Inscrire un nouveau membre
    public boolean inscrireMembre(Membre membre) {
        String sql = "INSERT INTO membres (nom, prenom, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    membre.setId(rs.getInt(1));
                }
                System.out.println("✅ Membre inscrit avec succès! ID: " + membre.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur d'inscription: " + e.getMessage());
        }
        return false;
    }
    
    // 2. Rechercher par nom
    public List<Membre> rechercherParNom(String nom) {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres WHERE LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nom + "%");
            pstmt.setString(2, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Membre membre = new Membre(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getDate("date_adhesion").toString()
                );
                membres.add(membre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de recherche: " + e.getMessage());
        }
        
        return membres;
    }
    
    // 3. Afficher tous les membres
    public List<Membre> getAllMembres() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membre membre = new Membre(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getDate("date_adhesion").toString()
                );
                membres.add(membre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        
        return membres;
    }
    
    // 4. Supprimer un membre
    public boolean supprimerMembre(int id) {
        String sql = "DELETE FROM membres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Membre supprimé avec succès!");
                return true;
            } else {
                System.out.println("❌ Aucun membre trouvé avec l'ID: " + id);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de suppression: " + e.getMessage());
        }
        return false;
    }
    
    // 5. Trouver un membre par ID
    public Membre trouverParId(int id) {
        String sql = "SELECT * FROM membres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Membre(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getDate("date_adhesion").toString()
                );
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        return null;
    }
}