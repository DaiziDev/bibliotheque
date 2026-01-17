// src/Emprunt.java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    private int id;
    private int membreId;
    private int livreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    
    // Constructeur pour nouvel emprunt
    public Emprunt(int membreId, int livreId) {
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = dateEmprunt.plusDays(14);
    }
    
    // Constructeur complet pour lecture depuis BD
    public Emprunt(int id, int membreId, int livreId, LocalDate dateEmprunt, 
                   LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        this.id = id;
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getMembreId() { return membreId; }
    public void setMembreId(int membreId) { this.membreId = membreId; }
    
    public int getLivreId() { return livreId; }
    public void setLivreId(int livreId) { this.livreId = livreId; }
    
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { 
        this.dateEmprunt = dateEmprunt; 
    }
    
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { 
        this.dateRetourPrevue = dateRetourPrevue; 
    }
    
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { 
        this.dateRetourEffective = dateRetourEffective; 
    }
    
    // Méthode pour calculer les pénalités
    public double calculerPenalite() {
        if (dateRetourEffective == null) {
            // Si pas encore retourné, calculer par rapport à aujourd'hui
            LocalDate aujourdhui = LocalDate.now();
            long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, aujourdhui);
            return Math.max(joursRetard, 0) * 100;
        }
        
        long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        return Math.max(joursRetard, 0) * 100; // 100 FCFA par jour
    }
    
    // Méthode pour afficher les détails
    public void afficherDetails() {
        System.out.println("══════════════════════════════════════");
        System.out.println("ID Emprunt: " + id);
        System.out.println("ID Membre: " + membreId);
        System.out.println("ID Livre: " + livreId);
        System.out.println("Date emprunt: " + dateEmprunt);
        System.out.println("Date retour prévue: " + dateRetourPrevue);
        System.out.println("Date retour effective: " + 
            (dateRetourEffective != null ? dateRetourEffective : "Non retourné"));
        
        double penalite = calculerPenalite();
        if (penalite > 0) {
            System.out.println("Pénalité: " + penalite + " FCFA");
        }
        System.out.println("══════════════════════════════════════");
    }
}