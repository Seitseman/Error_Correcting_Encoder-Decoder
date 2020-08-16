import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        StringBuilder output = new StringBuilder();

        int curCount = 1;
        for (int i = 1; i < input.length(); ++i) {
            if (input.charAt(i) == input.charAt(i - 1)) {
                curCount++;
                continue;
            }
            output.append(input.charAt(i - 1));
            output.append(curCount);
            curCount = 1;
        }

        output.append(input.charAt(input.length() - 1));
        output.append(curCount);

        System.out.println(output.toString());
    }
}