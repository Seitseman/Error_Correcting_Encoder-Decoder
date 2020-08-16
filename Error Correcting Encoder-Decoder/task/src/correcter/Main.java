package correcter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static void encode() {
        try (InputStream reader = new FileInputStream("send.txt");
             FileOutputStream writer = new FileOutputStream("encoded.txt")) {
            int curByte = reader.read();

            byte[] encoding = new byte[] {0};
            while (curByte != -1) {
                encoding[0] = 0;
                final byte firstQ = (byte) (curByte >> 4);
                encodeFourBits(firstQ, encoding);
                writer.write(encoding);

                encoding[0] = 0;
                final byte lastQ = (byte) ((curByte << 4) >> 4);
                encodeFourBits(lastQ, encoding);
                writer.write(encoding);

                curByte = reader.read();
            }
            System.out.println("send.txt:");

        } catch (Exception e) {
            System.out.println("Exception occurred " + e.getMessage());
        }
    }

    private static void encodeFourBits(byte fourBits, byte[] encoding) {
        encoding[0] |= ((fourBits & 1) ^ ((fourBits & 4) >> 2) ^ ((fourBits & 8) >> 3)) << 7;
        encoding[0] |= ((fourBits & 1) ^ ((fourBits & 2) >> 1) ^ ((fourBits & 8) >> 3)) << 6;
        encoding[0] |= ((fourBits & 8) >> 3 ) << 5;
        encoding[0] |= ((fourBits & 1) ^ ((fourBits & 2) >> 1) ^ ((fourBits & 4) >> 2)) << 4;
        encoding[0] |= ((fourBits & 4) >> 2) << 3;
        encoding[0] |= ((fourBits & 2) >> 1) << 2;
        encoding[0] |= ((fourBits & 1)) << 1;
    }

    private static void send() {
        try (InputStream reader = new FileInputStream("encoded.txt");
             FileOutputStream writer = new FileOutputStream("received.txt")) {

            StringBuilder encodedHexView = new StringBuilder();
            StringBuilder encodedBinView = new StringBuilder();

            Random random = new Random();
            int curByte = reader.read();
            while (curByte != -1) {
                curByte ^= 1 << random.nextInt(8);

                encodedHexView.append(String.format("%02X ", curByte));
                encodedBinView.append(String.format(String.format("%8s", Integer.toBinaryString(curByte)).replace(' ', '0') + " "));
                writer.write(curByte);

                curByte = reader.read();
            }

            System.out.println("encoded.txt:");
            System.out.println("hex view: " + encodedHexView);
            System.out.println("bin view: " + encodedBinView);

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private static void decode() {
        try (InputStream reader = new FileInputStream("received.txt");
             FileOutputStream writer = new FileOutputStream("decoded.txt")) {

            int curByte = reader.read();
            byte[] decodedData = new byte[] {0};

            while (curByte != -1) {
                decodedData[0] = 0;
                final byte highHalfByte = decodeHalfByte(curByte);
                curByte = reader.read();
                final byte lowHalfByte = decodeHalfByte(curByte);
                decodedData[0] |= (highHalfByte << 4) | lowHalfByte;
                writer.write(decodedData);
                curByte = reader.read();
            }

        } catch (Exception e) {
            System.out.println("Exception has occurred: " + e.getMessage());
        }
    }

    private static byte decodeHalfByte(int curByte) {
        final byte p1 = (byte) ((curByte >> 7) & 1);
        final byte p2 = (byte) ((curByte >> 6) & 1);
        final byte p4 = (byte) ((curByte >> 4) & 1);

        byte d3 = (byte) ((curByte >> 5) & 1);
        byte d5 = (byte) ((curByte >> 3) & 1);
        byte d6 = (byte) ((curByte >> 2) & 1);
        byte d7 = (byte) ((curByte >> 1) & 1);

        final byte p1Check = (byte) (p1 ^ d3 ^ d5 ^ d7);
        final byte p2Check = (byte) (p2 ^ d3 ^ d6 ^ d7);
        final byte p4Check = (byte) (p4 ^ d5 ^ d6 ^ d7);

        if (p1Check == 1 && p2Check == 1 && p4Check == 1) {
            d7 ^= 1;
        } else if (p2Check == 1 && p4Check == 1) {
            d6 ^= 1;
        } else if (p1Check == 1 && p4Check == 1) {
            d5 ^= 1;
        } else if (p1Check == 1 && p2Check == 1) {
            d3 ^= 1;
        }

        byte b = (byte) ((d3 << 3) | (d5 << 2) | (d6 << 1) | d7);
        return b;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String mode = scanner.nextLine();
        System.out.println();

        if (mode.equals("encode")) {
            encode();
        } else if (mode.equals("send")) {
            send();
        } else if (mode.equals("decode")) {
            decode();
        }
    }
}
