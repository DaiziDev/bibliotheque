// src/DatabaseConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/bibliotheque";
    private static final String USER = "postgres";  
    private static final String PASSWORD = "password"; 
    
    public static Connection getConnection() {
        try {
            
            Class.forName("org.postgresql.Driver");
            
            
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion à la base de données réussie!");
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver PostgreSQL non trouvé!");
            System.out.println("Téléchargez le fichier JAR: https://jdbc.postgresql.org/download/");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion à la base de données!");
            System.out.println("Vérifiez:");
            System.out.println("1. PostgreSQL est-il démarré?");
            System.out.println("2. La base 'bibliotheque' existe-t-elle?");
            System.out.println("3. Les identifiants sont-ils corrects?");
            e.printStackTrace();
            return null;
        }
    }
    
    // Méthode pour tester la connexion
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Test de connexion réussi!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Échec du test de connexion");
            e.printStackTrace();
        }
    }
}