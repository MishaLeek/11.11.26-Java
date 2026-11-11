import java.util.Locale;
import java.util.Scanner;

/** Задача 1. Кредит с досрочным погашением. */
public class Task1 {

    /** Результат расчёта графика погашения. */
    static class Result {
        int months;
        double interest;
        double lastPayment;
    }

    /** Считывает число; при неверном вводе просит повторить. Допускается и запятая, и точка. */
    static double readDouble(Scanner sc, String prompt, double min, double max, boolean minStrict) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                System.exit(1);
            }
            String token = sc.next().replace(',', '.');
            try {
                double v = Double.parseDouble(token);
                boolean ok = Double.isFinite(v) && v <= max && (minStrict ? v > min : v >= min);
                if (ok) return v;
            } catch (NumberFormatException ignored) {
                // обрабатывается ниже
            }
            System.out.println("Ошибка: введите число в допустимых пределах.");
        }
    }

    static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                System.exit(1);
            }
            String token = sc.next();
            try {
                int v = Integer.parseInt(token);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {
                // обрабатывается ниже
            }
            System.out.println("Ошибка: введите целое число от " + min + " до " + max + ".");
        }
    }

    /** Моделирует погашение: проценты на остаток, затем платёж A + E (последний не больше долга). */
    static Result simulate(double principal, double r, double payment) {
        Result res = new Result();
        double balance = principal;
        while (balance > 1e-9 && res.months < 100000) {
            double interest = balance * r;
            double due = balance + interest;
            double pay = Math.min(payment, due);
            balance = due - pay;
            if (balance < 1e-7) balance = 0;   // убираем «хвост» от погрешности double
            res.interest += interest;
            res.lastPayment = pay;
            res.months++;
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double p = readDouble(sc, "Сумма кредита P (тенге, > 0): ", 0, Double.MAX_VALUE, true);
        double rate = readDouble(sc, "Годовая ставка R (%, 0..100): ", 0, 100, false);
        int n = readInt(sc, "Срок N (месяцев, 1..360): ", 1, 360);
        double e = readDouble(sc, "Дополнительный платёж E (тенге/мес, >= 0): ", 0, Double.MAX_VALUE, false);

        double r = rate / 1200.0;
        double a = (r > 0) ? p * r / (1 - Math.pow(1 + r, -n)) : p / n;

        Result base = simulate(p, r, a);       // без доплаты
        Result fast = simulate(p, r, a + e);   // с доплатой

        System.out.println();
        System.out.println(String.format(Locale.US, "Платёж без доплаты: %.2f", a));
        System.out.println("Фактический срок: " + fast.months + " мес.");
        System.out.println(String.format(Locale.US, "Всего процентов: %.2f", fast.interest));
        System.out.println(String.format(Locale.US, "Последний платёж: %.2f", fast.lastPayment));
        System.out.println("Сэкономлено месяцев: " + (base.months - fast.months));
        System.out.println(String.format(Locale.US, "Экономия на процентах: %.2f", base.interest - fast.interest));
    }
}
