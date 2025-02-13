import java.util.*;

public class Main {

    // 1. Найти наибольшую подстроку без повторяющихся символов
    public static String longestUniqueSubstring(String s) {
        if (s == null || s.length() == 0) return "";

        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0;
        int start = 0;
        int maxStart = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c) && map.get(c) >= start) {
                start = map.get(c) + 1;
            }
            map.put(c, i);
            if (i - start + 1 > maxLen) {
                maxLen = i - start + 1;
                maxStart = start;
            }
        }
        return s.substring(maxStart, maxStart + maxLen);
    }

    // 2. Объединить два отсортированных массива
    public static int[] mergeSortedArrays(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length];
        int i = 0, j = 0, k = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] < arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }

        while (i < arr1.length) {
            result[k++] = arr1[i++];
        }

        while (j < arr2.length) {
            result[k++] = arr2[j++];
        }

        return result;
    }

    // 3. Найти максимальную сумму подмассива
    public static int maxSubarraySum(int[] arr) {
        int maxSum = arr[0];
        int currentSum = arr[0];

        for (int i = 1; i < arr.length; i++) {
            currentSum = Math.max(arr[i], currentSum + arr[i]);
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }

    // 4. Повернуть массив на 90 градусов по часовой стрелке
    public static int[][] rotate90Clockwise(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    // 5. Найти пару элементов в массиве, сумма которых равна заданному числу
    public static int[] findPairWithSum(int[] arr, int target) {
        Set<Integer> set = new HashSet<>();

        for (int num : arr) {
            int complement = target - num;
            if (set.contains(complement)) {
                return new int[]{complement, num};
            }
            set.add(num);
        }

        return null;
    }

    // 6. Найти сумму всех элементов в двумерном массиве
    public static int sumOf2DArray(int[][] arr) {
        int sum = 0;

        for (int[] row : arr) {
            for (int num : row) {
                sum += num;
            }
        }

        return sum;
    }

    // 7. Найти максимальный элемент в каждой строке двумерного массива
    public static int[] maxInEachRow(int[][] arr) {
        int[] maxElements = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int max = arr[i][0];
            for (int j = 1; j < arr[i].length; j++) {
                if (arr[i][j] > max) {
                    max = arr[i][j];
                }
            }
            maxElements[i] = max;
        }

        return maxElements;
    }

    // 8. Повернуть двумерный массив на 90 градусов против часовой стрелки
    public static int[][] rotate90CounterClockwise(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[n - 1 - j][i] = matrix[i][j];
            }
        }

        return rotated;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Выберите пункт (1-8) или 0 для выхода:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            switch (choice) {
                case 0:
                    System.out.println("Выход из программы.");
                    return;
                case 1:
                    System.out.println("Введите строку:");
                    String inputString = scanner.nextLine();
                    System.out.println("Наибольшая подстрока без повторяющихся символов: " + longestUniqueSubstring(inputString));
                    break;
                case 2:
                    System.out.println("Введите размер первого массива:");
                    int size1 = scanner.nextInt();
                    int[] arr1 = new int[size1];
                    System.out.println("Введите элементы первого массива:");
                    for (int i = 0; i < size1; i++) {
                        arr1[i] = scanner.nextInt();
                    }
                    System.out.println("Введите размер второго массива:");
                    int size2 = scanner.nextInt();
                    int[] arr2 = new int[size2];
                    System.out.println("Введите элементы второго массива:");
                    for (int i = 0; i < size2; i++) {
                        arr2[i] = scanner.nextInt();
                    }
                    int[] mergedArray = mergeSortedArrays(arr1, arr2);
                    System.out.println("Объединенный массив: " + Arrays.toString(mergedArray));
                    break;
                case 3:
                    System.out.println("Введите размер массива:");
                    int size3 = scanner.nextInt();
                    int[] arr3 = new int[size3];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < size3; i++) {
                        arr3[i] = scanner.nextInt();
                    }
                    System.out.println("Максимальная сумма подмассива: " + maxSubarraySum(arr3));
                    break;
                case 4:
                    System.out.println("Введите размер квадратного массива:");
                    int size4 = scanner.nextInt();
                    int[][] matrix = new int[size4][size4];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < size4; i++) {
                        for (int j = 0; j < size4; j++) {
                            matrix[i][j] = scanner.nextInt();
                        }
                    }
                    int[][] rotatedMatrix = rotate90Clockwise(matrix);
                    System.out.println("Повернутый массив на 90 градусов по часовой стрелке:");
                    for (int[] row : rotatedMatrix) {
                        System.out.println(Arrays.toString(row));
                    }
                    break;
                case 5:
                    System.out.println("Введите размер массива:");
                    int size5 = scanner.nextInt();
                    int[] arr5 = new int[size5];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < size5; i++) {
                        arr5[i] = scanner.nextInt();
                    }
                    System.out.println("Введите целое число:");
                    int target = scanner.nextInt();
                    int[] pair = findPairWithSum(arr5, target);
                    if (pair != null) {
                        System.out.println("Пара элементов: " + Arrays.toString(pair));
                    } else {
                        System.out.println("Пара не найдена.");
                    }
                    break;
                case 6:
                    System.out.println("Введите количество строк и столбцов массива:");
                    int rows = scanner.nextInt();
                    int cols = scanner.nextInt();
                    int[][] matrix2 = new int[rows][cols];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            matrix2[i][j] = scanner.nextInt();
                        }
                    }
                    System.out.println("Сумма всех элементов: " + sumOf2DArray(matrix2));
                    break;
                case 7:
                    System.out.println("Введите количество строк и столбцов массива:");
                    int rows2 = scanner.nextInt();
                    int cols2 = scanner.nextInt();
                    int[][] matrix3 = new int[rows2][cols2];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < rows2; i++) {
                        for (int j = 0; j < cols2; j++) {
                            matrix3[i][j] = scanner.nextInt();
                        }
                    }
                    int[] maxElements = maxInEachRow(matrix3);
                    System.out.println("Максимальные элементы в каждой строке: " + Arrays.toString(maxElements));
                    break;
                case 8:
                    System.out.println("Введите размер квадратного массива:");
                    int size8 = scanner.nextInt();
                    int[][] matrix4 = new int[size8][size8];
                    System.out.println("Введите элементы массива:");
                    for (int i = 0; i < size8; i++) {
                        for (int j = 0; j < size8; j++) {
                            matrix4[i][j] = scanner.nextInt();
                        }
                    }
                    int[][] rotatedMatrixCCW = rotate90CounterClockwise(matrix4);
                    System.out.println("Повернутый массив на 90 градусов против часовой стрелки:");
                    for (int[] row : rotatedMatrixCCW) {
                        System.out.println(Arrays.toString(row));
                    }
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }
}