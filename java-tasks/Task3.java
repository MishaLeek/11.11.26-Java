import java.util.Locale;
import java.util.Scanner;

/** Задача 3. Анализ бюджета за N дней. */
public class Task3 {

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

    static double readMoney(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                System.exit(1);
            }
            try {
                double v = Double.parseDouble(sc.next().replace(',', '.'));
                if (Double.isFinite(v) && v >= 0) return Math.round(v * 100) / 100.0;   // копейки
            } catch (NumberFormatException ignored) {
                // повторяем ввод
            }
            System.out.println("Ошибка: введите неотрицательное число.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = readInt(sc, "Количество дней N (3..100): ", 3, 100);

        double[] income = new double[n];
        double[] expense = new double[n];
        double[] daily = new double[n];
        for (int i = 0; i < n; i++) {
            income[i] = readMoney(sc, "День " + (i + 1) + ", доход: ");
            expense[i] = readMoney(sc, "День " + (i + 1) + ", расход: ");
            daily[i] = Math.round((income[i] - expense[i]) * 100) / 100.0;
        }

        double totalIncome = 0, totalExpense = 0;
        for (int i = 0; i < n; i++) {
            totalIncome += income[i];
            totalExpense += expense[i];
        }
        double balance = totalIncome - totalExpense;
        double average = balance / n;

        int bestDay = 0;                       // самый ранний при равенстве: сравниваем строго «больше»
        for (int i = 1; i < n; i++) {
            if (daily[i] > daily[bestDay]) bestDay = i;
        }

        int bestStreak = 0, streak = 0;        // ноль прерывает серию убыточных дней
        for (int i = 0; i < n; i++) {
            if (daily[i] < 0) {
                streak++;
                if (streak > bestStreak) bestStreak = streak;
            } else {
                streak = 0;
            }
        }

        double cumulative = 0, minCumulative = Double.MAX_VALUE;
        int minDay = 0;
        for (int i = 0; i < n; i++) {
            cumulative = Math.round((cumulative + daily[i]) * 100) / 100.0;
            if (cumulative < minCumulative) {  // строго меньше: первый день при равенстве
                minCumulative = cumulative;
                minDay = i + 1;
            }
        }

        System.out.println();
        System.out.println(String.format(Locale.US, "Доходы всего: %.2f", totalIncome));
        System.out.println(String.format(Locale.US, "Расходы всего: %.2f", totalExpense));
        System.out.println(String.format(Locale.US, "Итоговый баланс: %.2f", balance));
        System.out.println(String.format(Locale.US, "Средний баланс: %.2f", average));
        System.out.println(String.format(Locale.US, "Лучший день: %d (%.2f)", bestDay + 1, daily[bestDay]));
        System.out.println("Длина серии убыточных дней: " + bestStreak);
        System.out.println(String.format(Locale.US, "Минимальный накопленный баланс: %.2f (день %d)", minCumulative, minDay));
    }
}
