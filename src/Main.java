import java.util.Scanner;

public class Main {
    // Arrays to store tenant record history in memory (up to 50 records)
    private static final int MAX_TENANTS = 50;
    private static String[] storeNames = new String[MAX_TENANTS];
    private static String[] unitCodes = new String[MAX_TENANTS];
    private static String[] unitCategories = new String[MAX_TENANTS];
    private static double[] floorAreas = new double[MAX_TENANTS];
    private static double[] totalInvoices = new double[MAX_TENANTS];
    private static boolean[] paymentStatuses = new boolean[MAX_TENANTS]; // NEW: Track payment status (true = Paid, false = Unpaid)
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
            System.out.println("3. Update Tenant Details / Mark Payment Status");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");

            if (!scanner.hasNextInt()) {
                System.out.println("\n[ERROR] Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    processTenantBilling(scanner);
                    break;
                case 2:
                    displayTenantInformation(scanner);
                    break;
                case 3:
                    updateTenantRecord(scanner);
                    break;
                case 4:
                    running = false;
                    System.out.println("\nExiting Shopping Center Billing System... Goodbye!");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option. Please select 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }

    // CASE 1: PROCESS BILL & SAVE TENANT
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

        // Tiered Lease Rate Logic (in PHP)
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
        double electricityCost = electricityKWh * 12.50;

        System.out.print("Enter Water Usage (Cubic Meters): ");
        double waterCuMeters = scanner.nextDouble();
        double waterCost = waterCuMeters * 45.00;

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
        double taxAmount = subtotal * 0.12;
        double latePenaltyFee = subtotal * penaltyRate;
        double grandTotalBill = subtotal + taxAmount + latePenaltyFee;

        // Save into memory arrays
        storeNames[tenantCount] = storeName;
        unitCodes[tenantCount] = unitCode;
        unitCategories[tenantCount] = unitCategory;
        floorAreas[tenantCount] = sqMeters;
        totalInvoices[tenantCount] = grandTotalBill;
        paymentStatuses[tenantCount] = false; // Initial status set to UNPAID
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
        System.out.println("PAYMENT STATUS         : UNPAID");
        System.out.println("==================================================");
        System.out.println("[SUCCESS] Record saved to active tenant list.");
    }

    // CASE 2: DISPLAY TENANT INFORMATION
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
            System.out.println("\n--------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-18s | %-10s | %-20s | %-10s | %-14s | %-10s\n",
                    "NO.", "STORE NAME", "UNIT CODE", "CATEGORY", "AREA(sq.m)", "TOTAL BILL", "STATUS");
            System.out.println("--------------------------------------------------------------------------------------------------");

            for (int i = 0; i < tenantCount; i++) {
                String statusStr = paymentStatuses[i] ? "PAID" : "UNPAID";
                System.out.printf("%-5d | %-18s | %-10s | %-20s | %-10.2f | PHP %-10.2f | %-10s\n",
                        (i + 1), storeNames[i], unitCodes[i], unitCategories[i], floorAreas[i], totalInvoices[i], statusStr);
            }
            System.out.println("--------------------------------------------------------------------------------------------------");
        } else if (subChoice == 2) {
            System.out.print("\nEnter Unit Code to search: ");
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
                    System.out.println("Payment Status  : " + (paymentStatuses[i] ? "PAID" : "UNPAID"));
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("\n[NOT FOUND] No record matching Unit Code '" + searchCode + "'.");
            }
        }
    }

    // CASE 3: UPDATE TENANT DETAILS OR MARK AS PAID
    private static void updateTenantRecord(Scanner scanner) {
        System.out.println("\n==================================================");
        System.out.println("       UPDATE TENANT RECORD / PAYMENT STATUS      ");
        System.out.println("==================================================");

        if (tenantCount == 0) {
            System.out.println("[ERROR] No tenant records found to update.");
            return;
        }

        System.out.print("Enter Unit Code of tenant: ");
        String searchCode = scanner.nextLine();
        int foundIndex = -1;

        for (int i = 0; i < tenantCount; i++) {
            if (unitCodes[i].equalsIgnoreCase(searchCode)) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex == -1) {
            System.out.println("[NOT FOUND] No tenant registered under Unit Code: " + searchCode);
            return;
        }

        System.out.println("\n--- CURRENT RECORD ---");
        System.out.println("Store Name     : " + storeNames[foundIndex]);
        System.out.println("Unit Code      : " + unitCodes[foundIndex]);
        System.out.printf("Total Invoice  : PHP %.2f\n", totalInvoices[foundIndex]);
        System.out.println("Payment Status : " + (paymentStatuses[foundIndex] ? "PAID" : "UNPAID"));

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Mark Bill as PAID / UNPAID");
        System.out.println("2. Update Store Details (Name & Area)");
        System.out.print("Choice (1-2): ");

        int updateChoice = scanner.nextInt();
        scanner.nextLine();

        if (updateChoice == 1) {
            System.out.print("Has this tenant paid the invoice? (true/false): ");
            boolean isPaid = scanner.nextBoolean();
            paymentStatuses[foundIndex] = isPaid;
            System.out.println("\n[SUCCESS] Payment status updated to: " + (isPaid ? "PAID" : "UNPAID"));
        } else if (updateChoice == 2) {
            System.out.print("Enter New Store Name: ");
            storeNames[foundIndex] = scanner.nextLine();

            System.out.print("Enter New Occupied Floor Area (sq. meters): ");
            double newArea = scanner.nextDouble();
            floorAreas[foundIndex] = newArea;

            if (newArea < 30) {
                unitCategories[foundIndex] = "Concourse Kiosk";
            } else if (newArea <= 250) {
                unitCategories[foundIndex] = "Inline Retail Unit";
            } else {
                unitCategories[foundIndex] = "Anchor Department Unit";
            }

            System.out.println("\n[SUCCESS] Tenant details updated successfully!");
        } else {
            System.out.println("[ERROR] Invalid option selected.");
        }
    }
}