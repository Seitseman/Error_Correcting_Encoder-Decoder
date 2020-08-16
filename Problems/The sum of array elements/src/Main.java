import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextInt();
        long sum = 0;
        while (scanner.hasNext()) {
            sum += scanner.nextLong();
        }

        System.out.println(sum);
    }
}