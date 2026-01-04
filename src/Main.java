import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== QUIZ SISTEMINE HOSGELDINIZ ===");
        
        while (true) {
            System.out.println("\nLutfen giris turunu seciniz:");
            System.out.println("1. Ogrenci Girisi");
            System.out.println("2. Yonetici Girisi");
            System.out.println("0. Cikis");
            System.out.print("Secim: ");
            String mainChoice = scanner.nextLine();

            if (mainChoice.equals("0")) break;

            if (mainChoice.equals("1")) {
                StudentPanel studentPanel = new StudentPanel(scanner);
                studentPanel.start();

            } else if (mainChoice.equals("2")) {
                if (loginAdmin()) {
                    AdminPanel adminPanel = new AdminPanel(scanner);
                    adminPanel.start();
                } else {
                    System.out.println("Hatali sifre!");
                }
            }
        }
        scanner.close();
    }

    public static boolean loginAdmin() {
        System.out.print("Admin Sifresi: ");
        return scanner.nextLine().equals("1234");
    }
}
