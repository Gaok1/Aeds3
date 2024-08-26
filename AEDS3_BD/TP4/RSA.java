package TP4;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {
    private static BigInteger n, d, e;
    private static final int BIT_LENGTH = 2048;
    private static final SecureRandom random = new SecureRandom();

    public static void start() {
        BigInteger p = largePrime(BIT_LENGTH / 2);
        BigInteger q = largePrime(BIT_LENGTH / 2);
        n = p.multiply(q);
        BigInteger phi = getPhi(p, q);
        e = genE(phi);
        d = extEuclid(e, phi)[1];

        // Ajuste de d se for negativo
        if (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(phi);
        }
    }

    public static void encrypt(String inputFile, String outputFile) throws IOException {
        processFile(inputFile, outputFile, true);
    }

    public static void decrypt(String inputFile, String outputFile) throws IOException {
        processFile(inputFile, outputFile, false);
    }

    private static void processFile(String inputFile, String outputFile, boolean encrypt) throws IOException {
        try (RandomAccessFile inFile = new RandomAccessFile(inputFile, "r");
             RandomAccessFile outFile = new RandomAccessFile(outputFile, "rw")) {

            long fileLength = inFile.length();
            byte[] buffer = new byte[(int) fileLength];
            inFile.readFully(buffer);

            int blockSize = encrypt ? BIT_LENGTH / 8 - 1 : BIT_LENGTH / 8;
            for (int i = 0; i < buffer.length; i += blockSize) {
                int end = Math.min(i + blockSize, buffer.length);
                byte[] tempBuffer = new byte[end - i];
                System.arraycopy(buffer, i, tempBuffer, 0, end - i);
                BigInteger input = new BigInteger(1, tempBuffer);
                BigInteger output;
                if (encrypt) {
                    output = input.modPow(e, n);
                } else {
                    output = input.modPow(d, n);
                }
                byte[] outputBytes = output.toByteArray();
                if (outputBytes.length < BIT_LENGTH / 8) {
                    byte[] paddedBytes = new byte[BIT_LENGTH / 8];
                    System.arraycopy(outputBytes, 0, paddedBytes, paddedBytes.length - outputBytes.length, outputBytes.length);
                    outputBytes = paddedBytes;
                }
                outFile.write(outputBytes);
            }

            if (!encrypt) {
                outFile.setLength(fileLength); // Ajusta o tamanho do arquivo descriptografado
            }
        }
    }

    private static BigInteger getPhi(BigInteger p, BigInteger q) {
        return (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }

    private static BigInteger largePrime(int bits) {
        return BigInteger.probablePrime(bits, random);
    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }

    private static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };
        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger d = vals[0];
        BigInteger p = vals[2];
        BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
        return new BigInteger[] { d, p, q };
    }

    private static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e;
        do {
            e = new BigInteger(BIT_LENGTH / 2, rand);
        } while (!gcd(e, phi).equals(BigInteger.ONE));
        return e;
    }
}
