import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sentence = scanner.nextLine();

        System.out.println(sentence.toLowerCase(Locale.US).indexOf("the"));
    }
}