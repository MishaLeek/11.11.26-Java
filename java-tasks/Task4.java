import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/** Задача 4. Статистика измерений. */
public class Task4 {

    static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                System.exit(1);
            }
            try {
                int v = Integer.parseInt(sc.next());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {
                // повторяем ввод
            }
            System.out.println("Ошибка: введите целое число от " + min + " до " + max + ".");
        }
    }

    static double readValue(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                System.exit(1);
            }
            try {
                double v = Double.parseDouble(sc.next().replace(',', '.'));
                if (Double.isFinite(v)) return v;
            } catch (NumberFormatException ignored) {
                // повторяем ввод
            }
            System.out.println("Ошибка: введите число.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = readInt(sc, "Количество измерений N (3..1000): ", 3, 1000);

        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = readValue(sc, "Значение " + (i + 1) + ": ");
        }

        // 1. Среднее арифметическое
        double sum = 0;
        for (double v : x) sum += v;
        double mean = sum / n;

        // Медиана: сортируем КОПИЮ, исходный порядок нужен для возрастающего участка
        double[] sorted = x.clone();
        Arrays.sort(sorted);
        double median = (n % 2 == 1) ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;

        // 2. Стандартное отклонение по генеральной совокупности
        double sq = 0;
        for (double v : x) sq += (v - mean) * (v - mean);
        double sigma = Math.sqrt(sq / n);

        // 3. Количество значений строго выше среднего (небольшой допуск на погрешность double)
        int above = 0;
        for (double v : x) {
            if (v > mean + 1e-9) above++;
        }

        // 4. Самый длинный строго возрастающий участок в исходном порядке
        int longest = 1, current = 1;
        for (int i = 1; i < n; i++) {
            if (x[i] > x[i - 1]) {
                current++;
                if (current > longest) longest = current;
            } else {
                current = 1;
            }
        }

        System.out.println();
        System.out.println(String.format(Locale.US, "Среднее: %.2f", mean));
        System.out.println(String.format(Locale.US, "Медиана: %.2f", median));
        System.out.println(String.format(Locale.US, "Стандартное отклонение: %.2f", sigma));
        System.out.println("Выше среднего: " + above);
        System.out.println("Самый длинный возрастающий участок: " + longest);
    }
}
