public class Membre {
    // Attributs
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String adhesionDate;  // Changé de LocalDate à String pour simplifier
    
    // Constructeur 1 - Pour création
    public Membre(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adhesionDate = "2024-01-01";  // Date fixe pour simplifier
    }
    
    // Constructeur 2 - Pour lecture depuis BD
    public Membre(int id, String nom, String prenom, String email, String adhesionDate) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adhesionDate = adhesionDate;
    }
    
    // GETTERS - Très importants !
    public int getId() { 
        return id; 
    }
    
    public String getNom() { 
        return nom; 
    }
    
    public String getPrenom() { 
        return prenom; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getAdhesionDate() { 
        return adhesionDate; 
    }
    
    // SETTERS
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setNom(String nom) { 
        this.nom = nom; 
    }
    
    public void setPrenom(String prenom) { 
        this.prenom = prenom; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setAdhesionDate(String adhesionDate) { 
        this.adhesionDate = adhesionDate; 
    }
    
    // Méthode pour afficher
    public void afficherDetails() {
        System.out.println("=== MEMBRE ===");
        System.out.println("ID: " + id);
        System.out.println("Nom: " + nom + " " + prenom);
        System.out.println("Email: " + email);
        System.out.println("Date adhesion: " + adhesionDate);
        System.out.println("==============");
    }
}