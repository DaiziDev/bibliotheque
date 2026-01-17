// src/Main.java
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LivreDAO livreDAO = new LivreDAO();
    private static MembreDAO membreDAO = new MembreDAO();
    private static EmpruntDAO empruntDAO = new EmpruntDAO();
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GESTION DE BIBLIOTHÈQUE - v1.0     ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        // Tester la connexion à la base de données
        System.out.println("\n🔧 Test de connexion à la base de données...");
        DatabaseConnection.testConnection();
        
        boolean continuer = true;
        
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    menuGestionLivres();
                    break;
                case 2:
                    menuGestionMembres();
                    break;
                case 3:
                    menuGestionEmprunts();
                    break;
                case 4:
                    menuRecherche();
                    break;
                case 5:
                    afficherStatistiques();
                    break;
                case 0:
                    System.out.println("\n👋 Au revoir et à bientôt!");
                    continuer = false;
                    break;
                default:
                    System.out.println("❌ Choix invalide! Veuillez réessayer.");
            }
        }
        
        scanner.close();
    }
    
    private static void afficherMenuPrincipal() {
        System.out.println("\n════════════════════ MENU PRINCIPAL ════════════════════");
        System.out.println("1. 📚 Gestion des livres");
        System.out.println("2. 👥 Gestion des membres");
        System.out.println("3. 🔄 Gestion des emprunts");
        System.out.println("4. 🔍 Recherche avancée");
        System.out.println("5. 📊 Statistiques");
        System.out.println("0. 🚪 Quitter");
        System.out.println("═══════════════════════════════════════════════════════");
    }
    
    // ========== GESTION DES LIVRES ==========
    private static void menuGestionLivres() {
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\n══════════════════ GESTION DES LIVRES ══════════════════");
            System.out.println("1. ➕ Ajouter un livre");
            System.out.println("2. ✏️  Modifier un livre");
            System.out.println("3. 🗑️  Supprimer un livre");
            System.out.println("4. 📋 Lister tous les livres");
            System.out.println("5. 🔍 Rechercher un livre");
            System.out.println("0. ↩️  Retour au menu principal");
            System.out.println("═══════════════════════════════════════════════════════");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    ajouterLivre();
                    break;
                case 2:
                    modifierLivre();
                    break;
                case 3:
                    supprimerLivre();
                    break;
                case 4:
                    afficherTousLesLivres();
                    break;
                case 5:
                    rechercherLivreMenu();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void ajouterLivre() {
        System.out.println("\n📖 AJOUTER UN NOUVEAU LIVRE");
        
        System.out.print("Titre: ");
        String titre = scanner.nextLine();
        
        System.out.print("Auteur: ");
        String auteur = scanner.nextLine();
        
        System.out.print("Catégorie: ");
        String categorie = scanner.nextLine();
        
        int exemplaires = lireEntier("Nombre d'exemplaires: ");
        
        Livre livre = new Livre(titre, auteur, categorie, exemplaires);
        livreDAO.ajouterLivre(livre);
    }
    
    private static void modifierLivre() {
        System.out.println("\n✏️  MODIFIER UN LIVRE");
        int id = lireEntier("ID du livre à modifier: ");
        
        Livre livre = livreDAO.trouverParId(id);
        if (livre == null) {
            System.out.println("❌ Aucun livre trouvé avec l'ID: " + id);
            return;
        }
        
        System.out.println("Livre actuel:");
        livre.afficherDetails();
        
        System.out.println("\nNouvelles informations (laissez vide pour ne pas changer):");
        
        System.out.print("Nouveau titre [" + livre.getTitre() + "]: ");
        String nouveauTitre = scanner.nextLine();
        if (!nouveauTitre.isEmpty()) {
            livre.setTitre(nouveauTitre);
        }
        
        System.out.print("Nouvel auteur [" + livre.getAuteur() + "]: ");
        String nouvelAuteur = scanner.nextLine();
        if (!nouvelAuteur.isEmpty()) {
            livre.setAuteur(nouvelAuteur);
        }
        
        System.out.print("Nouvelle catégorie [" + livre.getCategorie() + "]: ");
        String nouvelleCategorie = scanner.nextLine();
        if (!nouvelleCategorie.isEmpty()) {
            livre.setCategorie(nouvelleCategorie);
        }
        
        System.out.print("Nouveau nombre d'exemplaires [" + livre.getNombreExemplaires() + "]: ");
        String nbExStr = scanner.nextLine();
        if (!nbExStr.isEmpty()) {
            try {
                livre.setNombreExemplaires(Integer.parseInt(nbExStr));
            } catch (NumberFormatException e) {
                System.out.println("❌ Nombre invalide, conservé l'ancienne valeur.");
            }
        }
        
        livreDAO.modifierLivre(livre);
    }
    
    private static void supprimerLivre() {
        System.out.println("\n🗑️  SUPPRIMER UN LIVRE");
        int id = lireEntier("ID du livre à supprimer: ");
        
        System.out.print("Êtes-vous sûr de vouloir supprimer ce livre? (oui/non): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            livreDAO.supprimerLivre(id);
        } else {
            System.out.println("✅ Suppression annulée.");
        }
    }
    
    private static void afficherTousLesLivres() {
        System.out.println("\n📚 LISTE DE TOUS LES LIVRES");
        List<Livre> livres = livreDAO.getAllLivres();
        
        if (livres.isEmpty()) {
            System.out.println("📭 Aucun livre dans la bibliothèque.");
        } else {
            System.out.println("📊 Total: " + livres.size() + " livre(s)");
            for (Livre livre : livres) {
                livre.afficherDetails();
            }
        }
    }
    
    private static void rechercherLivreMenu() {
        System.out.println("\n🔍 RECHERCHER UN LIVRE");
        System.out.println("1. Par titre");
        System.out.println("2. Par auteur");
        System.out.println("3. Par catégorie");
        System.out.println("0. Retour");
        
        int choix = lireEntier("Votre choix: ");
        
        switch (choix) {
            case 1:
                System.out.print("Entrez le titre à rechercher: ");
                String titre = scanner.nextLine();
                rechercherEtAfficherLivres(livreDAO.rechercherParTitre(titre), "titre", titre);
                break;
            case 2:
                System.out.print("Entrez l'auteur à rechercher: ");
                String auteur = scanner.nextLine();
                rechercherEtAfficherLivres(livreDAO.rechercherParAuteur(auteur), "auteur", auteur);
                break;
            case 3:
                System.out.print("Entrez la catégorie à rechercher: ");
                String categorie = scanner.nextLine();
                rechercherEtAfficherLivres(livreDAO.rechercherParCategorie(categorie), "catégorie", categorie);
                break;
            case 0:
                return;
            default:
                System.out.println("❌ Choix invalide!");
        }
    }
    
    private static void rechercherEtAfficherLivres(List<Livre> livres, String critere, String valeur) {
        if (livres.isEmpty()) {
            System.out.println("📭 Aucun livre trouvé pour " + critere + ": \"" + valeur + "\"");
        } else {
            System.out.println("✅ " + livres.size() + " livre(s) trouvé(s) pour " + critere + ": \"" + valeur + "\"");
            for (Livre livre : livres) {
                livre.afficherDetails();
            }
        }
    }
    
    // ========== GESTION DES MEMBRES ==========
    private static void menuGestionMembres() {
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\n══════════════════ GESTION DES MEMBRES ═════════════════");
            System.out.println("1. 👤 Inscrire un nouveau membre");
            System.out.println("2. 🗑️  Supprimer un membre");
            System.out.println("3. 📋 Lister tous les membres");
            System.out.println("4. 🔍 Rechercher un membre");
            System.out.println("0. ↩️  Retour au menu principal");
            System.out.println("═══════════════════════════════════════════════════════");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    inscrireMembre();
                    break;
                case 2:
                    supprimerMembre();
                    break;
                case 3:
                    afficherTousLesMembres();
                    break;
                case 4:
                    rechercherMembre();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void inscrireMembre() {
        System.out.println("\n👤 INSCRIRE UN NOUVEAU MEMBRE");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        Membre membre = new Membre(nom, prenom, email);
        membreDAO.inscrireMembre(membre);
    }
    
    private static void supprimerMembre() {
        System.out.println("\n🗑️  SUPPRIMER UN MEMBRE");
        int id = lireEntier("ID du membre à supprimer: ");
        
        System.out.print("Êtes-vous sûr? (oui/non): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("oui")) {
            membreDAO.supprimerMembre(id);
        } else {
            System.out.println("✅ Suppression annulée.");
        }
    }
    
    private static void afficherTousLesMembres() {
        System.out.println("\n📋 LISTE DE TOUS LES MEMBRES");
        List<Membre> membres = membreDAO.getAllMembres();
        
        if (membres.isEmpty()) {
            System.out.println("📭 Aucun membre inscrit.");
        } else {
            System.out.println("📊 Total: " + membres.size() + " membre(s)");
            for (Membre membre : membres) {
                membre.afficherDetails();
            }
        }
    }
    
    private static void rechercherMembre() {
        System.out.println("\n🔍 RECHERCHER UN MEMBRE");
        System.out.print("Entrez le nom ou prénom à rechercher: ");
        String nom = scanner.nextLine();
        
        List<Membre> membres = membreDAO.rechercherParNom(nom);
        
        if (membres.isEmpty()) {
            System.out.println("📭 Aucun membre trouvé pour: \"" + nom + "\"");
        } else {
            System.out.println("✅ " + membres.size() + " membre(s) trouvé(s)");
            for (Membre membre : membres) {
                membre.afficherDetails();
            }
        }
    }
    
    // ========== GESTION DES EMPRUNTS ==========
    private static void menuGestionEmprunts() {
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\n══════════════════ GESTION DES EMPRUNTS ════════════════");
            System.out.println("1. 📖 Enregistrer un nouvel emprunt");
            System.out.println("2. ↩️  Enregistrer un retour");
            System.out.println("3. 📋 Liste des emprunts en cours");
            System.out.println("4. 📜 Historique des emprunts");
            System.out.println("0. ↩️  Retour au menu principal");
            System.out.println("═══════════════════════════════════════════════════════");
            
            int choix = lireEntier("Votre choix: ");
            
            switch (choix) {
                case 1:
                    enregistrerEmprunt();
                    break;
                case 2:
                    retournerLivre();
                    break;
                case 3:
                    afficherEmpruntsEnCours();
                    break;
                case 4:
                    afficherHistoriqueEmprunts();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void enregistrerEmprunt() {
        System.out.println("\n📖 NOUVEL EMPRUNT");
        
        // Afficher les membres
        List<Membre> membres = membreDAO.getAllMembres();
        if (membres.isEmpty()) {
            System.out.println("❌ Aucun membre inscrit. Veuillez d'abord inscrire un membre.");
            return;
        }
        
        // Afficher les livres disponibles
        List<Livre> livres = livreDAO.getAllLivres();
        if (livres.isEmpty()) {
            System.out.println("❌ Aucun livre dans la bibliothèque.");
            return;
        }
        
        System.out.println("\n📋 Liste des membres:");
        for (Membre membre : membres) {
            System.out.println("ID: " + membre.getId() + " - " + membre.getNom() + " " + membre.getPrenom());
        }
        
        int membreId = lireEntier("\nID du membre: ");
        Membre membre = membreDAO.trouverParId(membreId);
        if (membre == null) {
            System.out.println("❌ Membre non trouvé!");
            return;
        }
        
        System.out.println("\n📚 Livres disponibles:");
        for (Livre livre : livres) {
            if (empruntDAO.estLivreDisponible(livre.getId())) {
                System.out.println("ID: " + livre.getId() + " - " + livre.getTitre() + " (" + livre.getAuteur() + ")");
            }
        }
        
        int livreId = lireEntier("ID du livre: ");
        Livre livre = livreDAO.trouverParId(livreId);
        if (livre == null) {
            System.out.println("❌ Livre non trouvé!");
            return;
        }
        
        // Vérifier la disponibilité
        if (!empruntDAO.estLivreDisponible(livreId)) {
            System.out.println("❌ Ce livre n'est pas disponible actuellement!");
            return;
        }
        
        Emprunt emprunt = new Emprunt(membreId, livreId);
        empruntDAO.enregistrerEmprunt(emprunt);
    }
    
    private static void retournerLivre() {
        System.out.println("\n↩️  RETOURNER UN LIVRE");
        
        // Afficher les emprunts en cours
        List<Emprunt> emprunts = empruntDAO.getEmpruntsEnCours();
        if (emprunts.isEmpty()) {
            System.out.println("📭 Aucun emprunt en cours.");
            return;
        }
        
        System.out.println("\n📋 Emprunts en cours:");
        for (Emprunt emprunt : emprunts) {
            Livre livre = livreDAO.trouverParId(emprunt.getLivreId());
            Membre membre = membreDAO.trouverParId(emprunt.getMembreId());
            System.out.println("ID Emprunt: " + emprunt.getId() + 
                             " | Livre: " + (livre != null ? livre.getTitre() : "Inconnu") +
                             " | Membre: " + (membre != null ? membre.getNom() + " " + membre.getPrenom() : "Inconnu") +
                             " | Retour prévu: " + emprunt.getDateRetourPrevue());
        }
        
        int empruntId = lireEntier("\nID de l'emprunt à retourner: ");
        empruntDAO.retournerLivre(empruntId);
    }
    
    private static void afficherEmpruntsEnCours() {
        System.out.println("\n📋 EMPRUNTS EN COURS");
        List<Emprunt> emprunts = empruntDAO.getEmpruntsEnCours();
        
        if (emprunts.isEmpty()) {
            System.out.println("📭 Aucun emprunt en cours.");
        } else {
            System.out.println("📊 Total: " + emprunts.size() + " emprunt(s) en cours");
            for (Emprunt emprunt : emprunts) {
                emprunt.afficherDetails();
                
                // Ajouter des informations supplémentaires
                Livre livre = livreDAO.trouverParId(emprunt.getLivreId());
                Membre membre = membreDAO.trouverParId(emprunt.getMembreId());
                
                if (livre != null) {
                    System.out.println("   📖 Livre: " + livre.getTitre());
                }
                if (membre != null) {
                    System.out.println("   👤 Emprunteur: " + membre.getNom() + " " + membre.getPrenom());
                }
                System.out.println("══════════════════════════════════════");
            }
        }
    }
    
    private static void afficherHistoriqueEmprunts() {
        System.out.println("\n📜 HISTORIQUE DES EMPRUNTS");
        List<Emprunt> emprunts = empruntDAO.getAllEmprunts();
        
        if (emprunts.isEmpty()) {
            System.out.println("📭 Aucun emprunt enregistré.");
        } else {
            System.out.println("📊 Total: " + emprunts.size() + " emprunt(s)");
            for (Emprunt emprunt : emprunts) {
                emprunt.afficherDetails();
            }
        }
    }
    
    // ========== RECHERCHE AVANCÉE ==========
    private static void menuRecherche() {
        System.out.println("\n🔍 RECHERCHE AVANCÉE");
        System.out.println("Cette fonctionnalité permet des recherches combinées.");
        System.out.println("(À implémenter selon vos besoins)");
    }
    
    // ========== STATISTIQUES ==========
    private static void afficherStatistiques() {
        System.out.println("\n📊 STATISTIQUES DE LA BIBLIOTHÈQUE");
        
        List<Livre> livres = livreDAO.getAllLivres();
        List<Membre> membres = membreDAO.getAllMembres();
        List<Emprunt> empruntsEnCours = empruntDAO.getEmpruntsEnCours();
        
        System.out.println("══════════════════════════════════════");
        System.out.println("📚 Livres: " + livres.size());
        System.out.println("👥 Membres: " + membres.size());
        System.out.println("📖 Emprunts en cours: " + empruntsEnCours.size());
        System.out.println("══════════════════════════════════════");
        
        // Livres les plus empruntés (exemple simple)
        System.out.println("\n🏆 Top catégories de livres:");
        // Cette partie nécessiterait une requête SQL plus complexe
    }
    
    // ========== UTILITAIRES ==========
    private static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide!");
            }
        }
    }
}