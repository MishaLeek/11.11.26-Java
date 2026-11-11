import java.util.Locale;
import java.util.Scanner;

/** Задача 2. Расчёт счёта за воду по прогрессивному тарифу. */
public class Task2 {

    static final double RATE1 = 85;      // первые 10 м3
    static final double RATE2 = 120;     // от 10 до 20 м3
    static final double RATE3 = 175;     // свыше 20 м3
    static final double SUBSCRIPTION = 600;
    static final double DISCOUNT_LIMIT = 15;   // скидка 10 % при V <= 15

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double v;
        while (true) {
            System.out.print("Объём потребления V (м³, >= 0): ");
            if (!sc.hasNext()) {
                System.out.println("\nВвод завершён раньше времени.");
                return;
            }
            String token = sc.next().replace(',', '.');
            try {
                v = Double.parseDouble(token);
                if (Double.isFinite(v) && v >= 0) break;
            } catch (NumberFormatException ignored) {
                // повторяем ввод
            }
            System.out.println("Ошибка: введите неотрицательное число.");
        }

        double part1 = Math.min(v, 10) * RATE1;
        double part2 = Math.max(0, Math.min(v, 20) - 10) * RATE2;
        double part3 = Math.max(0, v - 20) * RATE3;
        double water = part1 + part2 + part3;

        double discount = (v <= DISCOUNT_LIMIT) ? water * 0.10 : 0;   // только на воду, не на абонплату
        double total = water - discount + SUBSCRIPTION;
        double effective = (v > 0) ? (water - discount) / v : 0;       // цена 1 м³ без абонплаты, с учётом скидки

        System.out.println();
        System.out.println(String.format(Locale.US, "Первые 10 м³ (по 85 тг): %.2f тг", part1));
        System.out.println(String.format(Locale.US, "От 10 до 20 м³ (по 120 тг): %.2f тг", part2));
        System.out.println(String.format(Locale.US, "Свыше 20 м³ (по 175 тг): %.2f тг", part3));
        System.out.println(String.format(Locale.US, "Стоимость воды до скидки: %.2f тг", water));
        System.out.println(String.format(Locale.US, "Скидка: %.2f тг", discount));
        System.out.println(String.format(Locale.US, "Абонентская плата: %.2f тг", SUBSCRIPTION));
        System.out.println(String.format(Locale.US, "Итоговый счёт: %.2f тг", total));
        System.out.println(String.format(Locale.US, "Эффективная цена 1 м³: %.2f тг", effective));
    }
}
