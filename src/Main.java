import java.util.Scanner;

public class Main {

    private static final int MAX_TENANTS = 50;
    private static String[] storeNames = new String[MAX_TENANTS];
    private static String[] unitCodes = new String[MAX_TENANTS];
    private static String[] unitCategories = new String[MAX_TENANTS];
    private static double[] floorAreas = new double[MAX_TENANTS];
    private static double[] totalInvoices = new double[MAX_TENANTS];
    private static int tenantCount = 0; // Tracks number of registered records

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==================================================");
        System.out.println("   SHOPPING CENTER LEASE & UTILITY BILLING SYSTEM ");
        System.out.println("==================================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Process Monthly Tenant Bill");
            System.out.println("2. Display Tenant Information & Records");
            System.out.println("3. Exit");
            System.out.print("Select an option (1-3): ");


            if (!scanner.hasNextInt()) {
                System.out.println("\n[ERROR] Invalid input. Please enter a number between 1 and 3.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    processTenantBilling(scanner);
                    break;
                case 2:
                    displayTenantInformation(scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("\nExiting Shopping Center Billing System... Goodbye!");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }


    private static void processTenantBilling(Scanner scanner) {
        if (tenantCount >= MAX_TENANTS) {
            System.out.println("\n[WARNING] Tenant database memory limit reached (50 records). Cannot store more.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("STEP 1: MALL TENANT & LEASE SPACE DETAILS");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter Tenant/Store Name (e.g., Urban Apparel): ");
        String storeName = scanner.nextLine();

        System.out.print("Enter Unit Code (e.g., L2-45): ");
        String unitCode = scanner.nextLine();

        System.out.print("Enter Occupied Floor Area (sq. meters): ");
        double sqMeters = scanner.nextDouble();


        double ratePerSqMeter;
        String unitCategory;

        if (sqMeters < 30) {
            ratePerSqMeter = 1200.00;
            unitCategory = "Concourse Kiosk";
        } else if (sqMeters <= 250) {
            ratePerSqMeter = 850.00;
            unitCategory = "Inline Retail Unit";
        } else {
            ratePerSqMeter = 600.00;
            unitCategory = "Anchor Department Unit";
        }
        double baseLeaseAmount = sqMeters * ratePerSqMeter;

        System.out.println("\n--------------------------------------------------");
        System.out.println("STEP 2: SUB-METERED UTILITY CONSUMPTION");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter Electricity Usage (kWh): ");
        double electricityKWh = scanner.nextDouble();
        double electricityRate = 12.50;
        double electricityCost = electricityKWh * electricityRate;

        System.out.print("Enter Water Usage (Cubic Meters): ");
        double waterCuMeters = scanner.nextDouble();
        double waterRate = 45.00;
        double waterCost = waterCuMeters * waterRate;

        double totalUtilities = electricityCost + waterCost;

        System.out.println("\n--------------------------------------------------");
        System.out.println("STEP 3: OVERDUE ASSESSMENT & PENALTIES");
        System.out.println("--------------------------------------------------");
        System.out.print("Is this payment overdue? (true/false): ");
        boolean isOverdue = scanner.nextBoolean();

        double penaltyRate = 0.0;
        int daysOverdue = 0;

        if (isOverdue) {
            System.out.print("Enter number of days past due date: ");
            daysOverdue = scanner.nextInt();


            if (daysOverdue > 0 && daysOverdue <= 7) {
                penaltyRate = 0.03;
            } else if (daysOverdue <= 15) {
                penaltyRate = 0.07;
            } else if (daysOverdue > 15) {
                penaltyRate = 0.15;
            }
        }


        double subtotal = baseLeaseAmount + totalUtilities;

        )
        double taxRate = 0.12;
        double taxAmount = subtotal * taxRate;


        double latePenaltyFee = subtotal * penaltyRate;


        double grandTotalBill = subtotal + taxAmount + latePenaltyFee;


        storeNames[tenantCount] = storeName;
        unitCodes[tenantCount] = unitCode;
        unitCategories[tenantCount] = unitCategory;
        floorAreas[tenantCount] = sqMeters;
        totalInvoices[tenantCount] = grandTotalBill;
        tenantCount++;


        System.out.println("\n==================================================");
        System.out.println("       SHOPPING CENTER MONTHLY STATEMENT          ");
        System.out.println("==================================================");
        System.out.printf("Store Name             : %s (Unit: %s)\n", storeName, unitCode);
        System.out.printf("Category               : %s\n", unitCategory);
        System.out.printf("Occupied Space         : %.2f sq. m (@ PHP %.2f/sq.m)\n", sqMeters, ratePerSqMeter);
        System.out.println("--------------------------------------------------");
        System.out.printf("Base Rent Charge       : PHP %.2f\n", baseLeaseAmount);
        System.out.printf("Electricity Charge     : PHP %.2f (%.2f kWh)\n", electricityCost, electricityKWh);
        System.out.printf("Water Consumption      : PHP %.2f (%.2f cu.m)\n", waterCost, waterCuMeters);
        System.out.printf("Total Sub-Metered Utils: PHP %.2f\n", totalUtilities);
        System.out.println("--------------------------------------------------");
        System.out.printf("Net Subtotal           : PHP %.2f\n", subtotal);
        System.out.printf("Government Tax (12%%)   : PHP %.2f\n", taxAmount);
        System.out.printf("Late Penalty (%d days)  : PHP %.2f (%.0f%% rate)\n", daysOverdue, latePenaltyFee, penaltyRate * 100);
        System.out.println("==================================================");
        System.out.printf("GRAND TOTAL BILL       : PHP %.2f\n", grandTotalBill);
        System.out.println("==================================================");
        System.out.println("[SUCCESS] Record saved to active tenant list.");
    }


    private static void displayTenantInformation(Scanner scanner) {
        System.out.println("\n==================================================");
        System.out.println("           TENANT INFORMATION RECORDS             ");
        System.out.println("==================================================");

        if (tenantCount == 0) {
            System.out.println("No tenant records found. Please process a bill first (Option 1).");
            return;
        }

        System.out.println("1. View All Processed Tenants");
        System.out.println("2. Search Tenant by Unit Code");
        System.out.print("Select an option (1-2): ");

        int subChoice = scanner.nextInt();
        scanner.nextLine();

        if (subChoice == 1) {
            System.out.println("\n----------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-20s | %-10s | %-22s | %-12s | %-14s\n",
                    "NO.", "STORE NAME", "UNIT CODE", "CATEGORY", "AREA (sq.m)", "TOTAL BILL");
            System.out.println("----------------------------------------------------------------------------------");

            for (int i = 0; i < tenantCount; i++) {
                System.out.printf("%-5d | %-20s | %-10s | %-22s | %-12.2f | PHP %-10.2f\n",
                        (i + 1), storeNames[i], unitCodes[i], unitCategories[i], floorAreas[i], totalInvoices[i]);
            }
            System.out.println("----------------------------------------------------------------------------------");
        } else if (subChoice == 2) {
            System.out.print("\nEnter Unit Code to search (e.g., L2-45): ");
            String searchCode = scanner.nextLine();
            boolean found = false;

            for (int i = 0; i < tenantCount; i++) {
                if (unitCodes[i].equalsIgnoreCase(searchCode)) {
                    System.out.println("\n--- TENANT RECORD FOUND ---");
                    System.out.println("Store Name      : " + storeNames[i]);
                    System.out.println("Unit Code       : " + unitCodes[i]);
                    System.out.println("Category        : " + unitCategories[i]);
                    System.out.println("Allocated Area  : " + floorAreas[i] + " sq. meters");
                    System.out.printf("Latest Total Bill: PHP %.2f\n", totalInvoices[i]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("\n[NOT FOUND] No record matching Unit Code '" + searchCode + "'.");
            }
        } else {
            System.out.println("\n[ERROR] Invalid sub-option selected.");
        }
    }
}2