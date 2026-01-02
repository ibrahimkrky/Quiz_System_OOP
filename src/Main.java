import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== QUIZ SISTEMI ===");
        while (true) {
            System.out.println("1. Ogrenci, 2. Yonetici, 0. Cikis");
            String choice = scanner.nextLine();
            if (choice.equals("0")) break;
            if (choice.equals("1")) new StudentPanel(scanner).Start();
            else if (choice.equals("2")) {
                System.out.println("Sifre: ");
                if(scanner.nextLine().equals("1234")) new AdminPanel(scanner).Start();
            }
        }
    }
}
