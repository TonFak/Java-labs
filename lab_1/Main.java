import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            // Выводим меню
            System.out.println("Выберите задачу:");
            System.out.println("1. Сиракузская последовательность");
            System.out.println("2. Знакочередующаяся сумма ряда");
            System.out.println("3. Поиск клада");
            System.out.println("4 Логистический максимин");
            System.out.println("5 Дважды четное число");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    syracuseSequence();
                    break;
                case 2:
                    alternatingSum();
                    break;
                case 3:
                    treasureHunt();
                    break;
                case 4:
                    logisticMaximin();
                    break;
                case 5:
                    twiceEvenNumber();
                    break;
                case 0:
                    System.out.println("Выход из программы");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ошибка");
            }
        }
    }

    // 1. Сиракузская последовательность
    public static void syracuseSequence() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите натуральное число n: ");
        int n = scanner.nextInt();
        int steps = 0;

        while (n != 1) {
            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
            steps++;
        }

        System.out.println("Количество шагов: " + steps);
    }

    // 2. Знакочередующаяся сумма ряда
    public static void alternatingSum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Количество чисел n: ");
        int n = scanner.nextInt();
        int sum = 0;

        for (int i = 1; i <= n; i++) {
            System.out.print("Число #" + i + ": ");
            int number = scanner.nextInt();
            if (i % 2 == 0) {
                sum -= number;
            } else {
                sum += number;
            }
        }

        System.out.println("Знакочередующаяся сумма ряда: " + sum);
    }

    // 3. Поиск клада
    public static void treasureHunt() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Координата x: ");
        int treasureX = scanner.nextInt();
        System.out.print("Координата y: ");
        int treasureY = scanner.nextInt();

        int currentX = 0;
        int currentY = 0;
        int instructionsCount = 0;
        boolean treasureFound = false;

        while (true) {
            System.out.print("Направление (север/юг/запад/восток) или стоп: ");
            String direction = scanner.next();

            if (direction.equals("стоп")) {
                break;
            }

            System.out.print("Количество шагов: ");
            int steps = scanner.nextInt();
            instructionsCount++;

            switch (direction) {
                case "север":
                    currentY += steps;
                    break;
                case "юг":
                    currentY -= steps;
                    break;
                case "запад":
                    currentX -= steps;
                    break;
                case "восток":
                    currentX += steps;
                    break;
                default:
                    System.out.println("Ошибка");
                    continue;
            }

            if (currentX == treasureX && currentY == treasureY) {
                treasureFound = true;
                break;
            }
        }

        if (treasureFound) {
            System.out.println("Клад найден! Выполненных указаний: " + instructionsCount);
        } else {
            System.out.println("Клад не найден.");
        }
    }

    // 4. Логистический максимин
    public static void logisticMaximin() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Количество дорог: ");
            int numRoads = scanner.nextInt();
            int bestRoad = 0;
            int maxHeight = 0;

            for (int i = 1; i <= numRoads; i++) {
                System.out.print("Количество туннелей на дороге " + i + ": ");
                int numTunnels = scanner.nextInt();
                int minHeightOnRoad = Integer.MAX_VALUE;
                for (int j = 0; j < numTunnels; j++) {
                    System.out.print("Высоту туннеля " + (j + 1) + " на дороге " + i + ": ");
                    int height = scanner.nextInt();
                    if (height < minHeightOnRoad) {
                        minHeightOnRoad = height;
                    }
                }

                if (minHeightOnRoad > maxHeight) {
                    maxHeight = minHeightOnRoad;
                    bestRoad = i;
                }
            }

            // Выводим результат
            System.out.println("Номер дороги: " + bestRoad);
            System.out.println("Максимальная высота грузовика: " + maxHeight + " см");
    }

    // 5. Дважды четное число
    public static void  twiceEvenNumber() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Tрехзначное число: ");
            int number = scanner.nextInt();
            if (number < 100 || number > 999) {
                System.out.println("Hе трехзначное число.");
                return;
            }

            int digit1 = number / 100;
            int digit2 = (number / 10) % 10;
            int digit3 = number % 10;
            int sum = digit1 + digit2 + digit3;
            int product = digit1 * digit2 * digit3;

            if (sum % 2 == 0 && product % 2 == 0) {
                System.out.println("Число " + number + " является дважды четным.");
            } else {
                System.out.println("Число " + number + " не является дважды четным.");
            }
        }
}