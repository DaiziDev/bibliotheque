// src/LivreDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    
    // 1. Ajouter un livre
    public boolean ajouterLivre(Livre livre) {
        String sql = "INSERT INTO livres (titre, auteur, categorie, nombre_exemplaires) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getCategorie());
            pstmt.setInt(4, livre.getNombreExemplaires());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    livre.setId(rs.getInt(1));
                }
                System.out.println("✅ Livre ajouté avec succès! ID: " + livre.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du livre: " + e.getMessage());
        }
        return false;
    }
    
    // 2. Rechercher par titre
    public List<Livre> rechercherParTitre(String titre) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE LOWER(titre) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + titre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getInt("nombre_exemplaires")
                );
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de recherche: " + e.getMessage());
        }
        
        return livres;
    }
    
    // 3. Rechercher par auteur
    public List<Livre> rechercherParAuteur(String auteur) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE LOWER(auteur) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + auteur + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getInt("nombre_exemplaires")
                );
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de recherche: " + e.getMessage());
        }
        
        return livres;
    }
    
    // 4. Rechercher par catégorie
    public List<Livre> rechercherParCategorie(String categorie) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE LOWER(categorie) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + categorie + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getInt("nombre_exemplaires")
                );
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de recherche: " + e.getMessage());
        }
        
        return livres;
    }
    
    // 5. Afficher tous les livres
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getInt("nombre_exemplaires")
                );
                livres.add(livre);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        
        return livres;
    }
    
    // 6. Modifier un livre
    public boolean modifierLivre(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, categorie = ?, " +
                    "nombre_exemplaires = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getCategorie());
            pstmt.setInt(4, livre.getNombreExemplaires());
            pstmt.setInt(5, livre.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Livre modifié avec succès!");
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de modification: " + e.getMessage());
        }
        return false;
    }
    
    // 7. Supprimer un livre
    public boolean supprimerLivre(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Livre supprimé avec succès!");
                return true;
            } else {
                System.out.println("❌ Aucun livre trouvé avec l'ID: " + id);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur de suppression: " + e.getMessage());
        }
        return false;
    }
    
    // 8. Trouver un livre par ID
    public Livre trouverParId(int id) {
        String sql = "SELECT * FROM livres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("categorie"),
                    rs.getInt("nombre_exemplaires")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
        return null;
    }
}