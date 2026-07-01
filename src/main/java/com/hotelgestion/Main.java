package com.hotelgestion;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.hotelgestion.exception.EntityNotFoundException;
import com.hotelgestion.exception.InsufficientStockException;
import com.hotelgestion.exception.PaymentFailedException;
import com.hotelgestion.exception.RoomUnavailableException;
import com.hotelgestion.model.*;
import com.hotelgestion.service.*;
import com.hotelgestion.dao.*;

public class Main {
    private static AmenityDAO amenityDAO;
    private static DishDAO dishDAO;
    private static EmployeeDAO employeeDAO;
    private static GuestDAO guestDAO;
    private static HotelDAO hotelDAO;
    private static IngredientDAO ingredientDAO;
    private static InvoiceDAO invoiceDAO;
    private static MenuDAO menuDAO;
    private static PaymentDAO paymentDAO;
    private static ReservationDAO reservationDAO;
    private static RestaurantOrderDAO restaurantOrderDAO;
    private static RoomDAO roomDAO;

    private static EmployeeService employeeService;
    private static PaymentService paymentService;
    private static ReservationService reservationService;
    private static RestaurantService restaurantService;
    private static RoomService roomService;

    private static final Scanner scanner = new Scanner(System.in);

    public static void simulatedLoading(String message) {
        System.out.print(message);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(500);
                System.out.print(".");
            }
            System.out.println(" [OK]");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int readIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("⚠️ Entrée invalide. Veuillez saisir un nombre : ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    public static void main(String[] args) {
        try {
            amenityDAO = new AmenityDAO();
            dishDAO = new DishDAO();
            employeeDAO = new EmployeeDAO();
            guestDAO = new GuestDAO();
            hotelDAO = new HotelDAO();
            ingredientDAO = new IngredientDAO();
            invoiceDAO = new InvoiceDAO();
            menuDAO = new MenuDAO();
            paymentDAO = new PaymentDAO();
            reservationDAO = new ReservationDAO();
            restaurantOrderDAO = new RestaurantOrderDAO();
            roomDAO = new RoomDAO();

            roomService = new RoomService(hotelDAO, roomDAO);
            reservationService = new ReservationService(reservationDAO, roomDAO);
            restaurantService = new RestaurantService(restaurantOrderDAO, employeeDAO);
            paymentService = new PaymentService(paymentDAO, invoiceDAO, reservationDAO);
            employeeService = new EmployeeService(employeeDAO, roomDAO);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'initialisation des composants BDD : " + e.getMessage());
        }

        System.out.println("=======================================");
        System.out.println("    Bienvenue à Lemuri Hotel Mada      ");
        System.out.println("=======================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- Sélection de rôle ---");
            System.out.println("1. \uD83D\uDC64 Client");
            System.out.println("2. \uD83D\uDCBC Manager");
            System.out.println("3. \uD83E\uDDD1\u200D\uD83C\uDF73 Cuisinier");
            System.out.println("4. \uD83E\uDDF9 Nettoyeur");
            System.out.println("5. ❌ Quitter l'application");
            System.out.print("Choisissez une option (1-5) : ");

            int choice = readIntegerInput();

            switch (choice) {
                case 1 -> loginFlow("GUEST");
                case 2 -> loginFlow("MANAGER");
                case 3 -> loginFlow("COOK");
                case 4 -> loginFlow("CLEANER");
                case 5 -> {
                    System.out.println("\nFermeture du système de l'hôtel. A bientôt !");
                    running = false;
                }
                default -> System.out.println("⚠️ Option invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private static void loginFlow(String role) {
        System.out.println("\n------------------------------------------------");
        System.out.print("🔑 Entrez votre ID de connexion pour le rôle " + role + " : ");
        int id = readIntegerInput();

        simulatedLoading("Vérification des identifiants dans la base de données");

        try {
            if (role.equals("GUEST")) {
                guestDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Client inconnu."));
            } else if (role.equals("MANAGER")) {
                var emp = (Manager) employeeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Employé inconnu."));
            } else if (role.equals("COOK")) {
                var emp = (Cook) employeeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Employé inconnu."));
            } else if (role.equals("CLEANER")) {
                var emp = (Cleaner) employeeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Employé inconnu."));
            }

            System.out.println("✅ Connexion réussie !");

            switch (role) {
                case "GUEST" -> guestMenu(id);
                case "MANAGER" -> managerMenu(id);
                case "COOK" -> cookMenu(id);
                case "CLEANER" -> cleanerMenu(id);
            }
        } catch (Exception e) {
            System.out.println("❌ Échec de l'authentification : " + e.getMessage());
        }
    }

    private static void guestMenu(int guestId) {
        var guest = guestDAO.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'ID : " + guestId));

        System.out.println("\n✨ Bienvenue à Lemuri Hotel, " + guest.getName() + " !");

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- \uD83D\uDC64 ESPACE CLIENT ---");
            System.out.println("1. \uD83D\uDECF\uFE0F Voir les chambres disponibles");
            System.out.println("2. 📅 Réserver une chambre");
            System.out.println("3. 🍽️ Commander un plat (Room Service)");
            System.out.println("4. 🚪 Se déconnecter");
            System.out.print("Votre choix (1-4) : ");

            int choice = readIntegerInput();
            switch (choice) {
                case 1 -> {
                    System.out.println("\n[BDD] Recherche des chambres disponibles...");
                    simulatedLoading("Consultation du catalogue");

                    var availableRooms = roomService.listAvailable(1);
                    if (availableRooms.isEmpty()) {
                        System.out.println("⚠️ Désolé, aucune chambre n'est disponible actuellement.");
                    } else {
                        System.out.println("\n--- CHAMBRES DISPONIBLES ---");
                        for (Room r : availableRooms) {
                            System.out.printf("- ID: %d | N° %s | Type: %s | Prix: %.2f Ar\n",
                                    r.getId(), r.getRoomNumber(), r.getClass().getSimpleName(), r.getBasePrice());
                        }
                    }
                }
                case 2 -> {
                    try {
                        System.out.print("Entrez l'ID de la chambre à réserver : ");
                        int roomId = readIntegerInput();

                        var room = roomDAO.findById(roomId)
                                .orElseThrow(() -> new EntityNotFoundException("Chambre introuvable."));

                        System.out.print("Date d'arrivée (Format AAAA-MM-JJ, ex: 2026-06-25) : ");
                        LocalDate checkIn = LocalDate.parse(scanner.nextLine());

                        System.out.print("Date de départ (Format AAAA-MM-JJ, ex: 2026-06-30) : ");
                        LocalDate checkOut = LocalDate.parse(scanner.nextLine());

                        simulatedLoading("Vérification des disponibilités");

                        var res = reservationService.createReservation(guest, room, checkIn, checkOut);

                        System.out.println("\n🎉 Succès ! Réservation enregistrée.");
                        System.out.printf("   ID Réservation : %d | Prix Total : %.2f Ar | Statut : %s\n",
                                res.getId(), res.getTotalPrice(), res.getStatus());
                        System.out.println("   ⚠️ Note : Votre réservation est 'PENDING'. Un manager doit la valider.");
                    } catch (RoomUnavailableException e) {
                        System.out.println("\n⚠️ [ÉCHEC] " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n❌ Erreur lors de la saisie des dates. Format attendu : AAAA-MM-JJ.");
                    }
                }
                case 3 -> {
                    try {
                        System.out.println("\n[BDD] Chargement de la carte du restaurant...");
                        List<Dish> menuDishes = dishDAO.findAll();

                        if (menuDishes.isEmpty()) {
                            System.out.println("⚠️ La cuisine n'a pas encore de plats enregistrés au menu.");
                            break;
                        }

                        System.out.println("\n--- CARTE DU RESTAURANT ---");
                        for (Dish d : menuDishes) {
                            System.out.printf("- [%d] %s (%s) — %.2f Ar\n", d.getId(), d.getName(), d.getCategory(), d.getPrice());
                        }

                        System.out.print("\nEntrez l'ID du plat choisi : ");
                        int dishId = readIntegerInput();
                        var selectedDish = dishDAO.findById(dishId)
                                .orElseThrow(() -> new EntityNotFoundException("Plat introuvable."));

                        System.out.print("Quantité souhaitée : ");
                        int quantity = readIntegerInput();

                        System.out.println("\nIndiquez où livrer la commande :");
                        System.out.println("1. En chambre");
                        System.out.println("2. Sur place (Restaurant)");
                        System.out.print("Votre choix : ");
                        int deliveryChoice = readIntegerInput();

                        Room deliveryRoom = null;
                        if (deliveryChoice == 1) {
                            System.out.print("Entrez votre ID de chambre actuelle : ");
                            int rId = readIntegerInput();
                            deliveryRoom = roomDAO.findById(rId).orElse(null);
                        }

                        simulatedLoading("Vérification des stocks en cuisine en temps réel");

                        var order = restaurantService.placeOrder(
                                guest, deliveryRoom, List.of(selectedDish), List.of(quantity)
                        );

                        System.out.println("\n🍳 Commande reçue en cuisine ! ID Commande : " + order.getId());
                        System.out.println("   Statut actuel : " + order.getStatus());
                    } catch (InsufficientStockException e) {
                        System.out.println("\n⚠️ [ÉCHEC CUISINE] Rupture de stock : " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n❌ Impossible de traiter la commande : " + e.getMessage());
                    }
                }
                case 4 -> {
                    simulatedLoading("Déconnexion de votre espace sécurisé");
                    inMenu = false;
                }
                default -> System.out.println("⚠️ Option invalide.");
            }
        }
    }

    private static void managerMenu(int managerId) {
        var employee = employeeDAO.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable."));

        if (!(employee instanceof Manager)) {
            System.out.println("❌ Accès refusé. Cet identifiant n'appartient pas à un Manager.");
            return;
        }
        var currentManager = (Manager) employee;

        System.out.println("\n💼 Bonjour Manager " + currentManager.getName() + ".");

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- 💼 ESPACE ADMINISTRATION & GESTION ---");
            System.out.println("1. 📋 Lister toutes les réservations en attente (PENDING)");
            System.out.println("2. ✅ Approuver et encaisser une réservation");
            System.out.println("3. 💰 Voir la masse salariale brute (Payroll)");
            System.out.println("4. 🚪 Se déconnecter");
            System.out.print("Votre choix (1-4) : ");

            int choice = readIntegerInput();
            switch (choice) {
                case 1 -> {
                    System.out.println("\n[BDD] Recherche des dossiers en attente...");
                    List<Reservation> pendings = reservationDAO.findByStatus(ReservationStatus.PENDING);

                    if (pendings.isEmpty()) {
                        System.out.println("✨ Aucune réservation n'est en attente !");
                    } else {
                        System.out.println("\n--- DOSSIERS EN ATTENTE DE VALIDATION ---");
                        for (Reservation res : pendings) {
                            System.out.printf("- ID: %d | Client: %s | Chambre: %s | Montant: %.2f Ar\n",
                                    res.getId(), res.getGuest().getName(), res.getRoom().getRoomNumber(), res.getTotalPrice());
                        }
                    }
                }
                case 2 -> {
                    try {
                        System.out.print("Entrez l'ID de la réservation à traiter : ");
                        int resId = readIntegerInput();

                        simulatedLoading("Analyse du dossier client");
                        reservationService.validateReservation(resId, currentManager);
                        System.out.println("✅ Réservation approuvée par le Manager !");

                        var reservationToPay = reservationDAO.findById(resId).orElseThrow();
                        String guestPhone = reservationToPay.getGuest().getPhone();

                        System.out.println("\nMode de règlement requis :");
                        System.out.println("1. Carte Bancaire");
                        System.out.println("2. Espèces");
                        System.out.println("3. Mvola");
                        System.out.print("Sélection : ");
                        int payMethod = readIntegerInput();
                        var method = (payMethod == 1) ?  new CardPayment("token-card", "1111") : (payMethod == 2) ? new CashPayment() : new MvolaPayment(guestPhone, "TSX-...");

                        simulatedLoading("Interconnexion avec le terminal bancaire & génération de la facture");
                        var payment = paymentService.processPayment(reservationToPay, method);

                        System.out.println("\n💳 Facture acquittée avec succès !");
                        System.out.println("   ID Paiement : " + payment.getId() + " | Statut : " + payment.getStatus());
                        System.out.println("   La chambre est maintenant officiellement bloquée.");
                    } catch (PaymentFailedException e) {
                        System.out.println("\n⚠️ [ERREUR TRANSACTION] " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n❌ Erreur lors du traitement : " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Calcul des charges salariales de l'hôtel... ");
                    simulatedLoading("Exécution de la requête");

                    double payroll = employeeService.calculateTotalPayroll(1);
                    System.out.printf("\n📊 Masse salariale brute de l'établissement : %.2f Ar / mois.\n", payroll);
                }
                case 4 -> inMenu = false;
                default -> System.out.println("⚠️ Choix invalide.");
            }
        }
    }

    private static void cookMenu(int cookId) {
        var employee = employeeDAO.findById(cookId)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable."));

        if (!(employee instanceof Cook)) {
            System.out.println("❌ Accès refusé. Cet identifiant n'est pas lié à un poste en cuisine.");
            return;
        }
        var currentCook = (Cook) employee;

        System.out.println("\n👨‍🍳 Chef " + currentCook.getName() + " connecté. Spécialité : " + currentCook.getSpeciality());

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- 👨‍🍳 GESTION DE LA CUISINE ---");
            System.out.println("1. 🔔 Voir les commandes en attente de préparation");
            System.out.println("2. 🍳 Prendre en charge et finaliser une commande");
            System.out.println("3. 📦 Inspecter l'état des stocks d'ingrédients");
            System.out.println("4. 🚪 Se déconnecter");
            System.out.print("Votre choix (1-4) : ");

            int choice = readIntegerInput();
            switch (choice) {
                case 1 -> {
                    System.out.println("\n[BDD] Lecture de la table 'restaurant_order'...");
                    List<RestaurantOrder> activeOrders = restaurantOrderDAO.findByStatus(OrderStatus.IN_PREPARATION);

                    if (activeOrders.isEmpty()) {
                        System.out.println("☕ Aucune commande en cours.");
                    } else {
                        System.out.println("\n--- BONS DE COMMANDE EN CUISINE ---");
                        for (RestaurantOrder order : activeOrders) {
                            System.out.printf("- Bon N° %d | Pour: %s | Reçu à: %s\n",
                                    order.getId(), order.getGuest().getName(), order.getOrderDate());
                        }
                    }
                }
                case 2 -> {
                    try {
                        System.out.print("Entrez le N° de bon de commande à préparer : ");
                        int orderId = readIntegerInput();

                        restaurantService.assignCook(orderId, currentCook);
                        System.out.println("-> Commande assignée au Chef " + currentCook.getName() + ". Préparation lancée...");

                        simulatedLoading("Cuisson et dressage des plats");

                        restaurantService.completeOrder(orderId);
                        System.out.println("\n🍲 Plat prêt ! Le statut passe à DELIVERED.");
                    } catch (Exception e) {
                        System.out.println("\n❌ Impossible de traiter ce bon : " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("\n--- 📦 ÉTAT DE L'ÉCONOMAT (STOCKS) ---");
                    List<Ingredient> stockList = ingredientDAO.findAll();

                    for (Ingredient ing : stockList) {
                        System.out.printf("- %s : %.2f %s ", ing.getName(), ing.getStockQuantity(), ing.getUnit());
                        if (ing.getStockQuantity() <= ing.getAlertThreshold()) {
                            System.out.print(" ⚠️ [STOCK CRITIQUE : Seuil d'alerte à " + ing.getAlertThreshold() + "]");
                        }
                        System.out.println();
                    }
                }
                case 4 -> inMenu = false;
                default -> System.out.println("⚠️ Choix invalide.");
            }
        }
    }

    private static void cleanerMenu(int cleanerId) {
        var employee = employeeDAO.findById(cleanerId)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable."));

        if (!(employee instanceof Cleaner)) {
            System.out.println("❌ Accès refusé. Cet identifiant n'est pas lié à l'équipe d'entretien.");
            return;
        }

        System.out.println("\n🧹 Service de nettoyage connecté. ID Personnel : " + cleanerId);

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- 🧹 SERVICES DES ÉTAGES ---");
            System.out.println("1. 🛏️ Lister les chambres à nettoyer (CLEANING)");
            System.out.println("2. 🧼 Lancer le protocole de nettoyage sur une chambre");
            System.out.println("3. 🚪 Se déconnecter");
            System.out.print("Votre choix (1-3) : ");

            int choice = readIntegerInput();
            switch (choice) {
                case 1 -> {
                    System.out.println("\n[BDD] Recherche des chambres au statut 'CLEANING'...");
                    List<Room> dirtyRooms = roomDAO.findByStatus(RoomStatus.CLEANING);

                    if (dirtyRooms.isEmpty()) {
                        System.out.println("✨ Toutes les chambres sont impeccables !");
                    } else {
                        System.out.println("\n--- CHAMBRES EN ATTENTE DE NETTOYAGE ---");
                        for (Room r : dirtyRooms) {
                            System.out.printf("- Chambre N° %s (ID: %d) | Type: %s\n",
                                    r.getRoomNumber(), r.getId(), r.getClass().getSimpleName());
                        }
                    }
                }
                case 2 -> {
                    try {
                        System.out.print("Entrez l'ID de la chambre à nettoyer : ");
                        int roomId = readIntegerInput();

                        System.out.print("Application des protocoles sanitaires... ");
                        simulatedLoading("Désinfection et réapprovisionnement");

                        employeeService.assignCleanerToRoom(cleanerId, roomId);

                        System.out.println("\n✨ Nettoyage terminé avec succès ! La chambre est à nouveau 'AVAILABLE'.");
                    } catch (Exception e) {
                        System.out.println("\n❌ Échec du nettoyage : " + e.getMessage());
                    }
                }
                case 3 -> inMenu = false;
                default -> System.out.println("⚠️ Choix invalide.");
            }
        }
    }
}