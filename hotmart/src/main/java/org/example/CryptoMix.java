package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoMix {
    public static String processInput(String method, String inputString) {
        switch (method.toLowerCase()) {
            case "md5":
            case "sha1":
                return generateHash(method, inputString);
            case "base64":
                return decodeBase64(inputString);
            case "binary":
                return convertFromBinary(inputString);
            case "hex":
                return convertFromHex(inputString);
            default:
                return inputString.replace(" ", "_");
        }
    }

    private static String generateHash(String algorithm, String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm.toUpperCase());
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro: Algoritmo de hash não encontrado", e);
        }
    }

    private static String decodeBase64(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }

    private static String convertFromBinary(String input) {
        StringBuilder text = new StringBuilder();
        String[] binaryTokens = input.split(" ");
        for (String binaryToken : binaryTokens) {
            int charCode = Integer.parseInt(binaryToken, 2);
            text.append((char) charCode);
        }
        return text.toString().replace(" ", "_");
    }

    private static String convertFromHex(String input) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < input.length(); i += 2) {
            String hexChar = input.substring(i, i + 2);
            int charCode = Integer.parseInt(hexChar, 16);
            text.append((char) charCode);
        }
        return text.toString().replace(" ", "_");
    }

    public static void main(String[] args) {
        // Exemplos de uso:
        String method1 = "md5";
        String input1 = "escape_bands_deputies_creek_frame_fact";
        System.out.println("MD5 Hash: " + processInput(method1, input1));

        String method2 = "sha1";
        String input2 = "escape_bands_deputies_creek_frame_fact";
        System.out.println("SHA1 Hash: " + processInput(method2, input2));

        String method3 = "base64";
        String input3 = "c2lyc19jb252ZXJzaW9uX3RvZV9jb3VydF93YW50X2RlZmVhdA";
        System.out.println("Decodificado Base64: " + processInput(method3, input3));

        String method4 = "binary";
        String input4 = "01010100 01100101 01111000 01110100 01101111 00100000 01000010 01101001 01101110 11101001 01110010 01101001 01101111";
        System.out.println("Decodificado Binário: " + processInput(method4, input4));

        String method5 = "hex";
        String input5 = "737461746f725f617265615f6669726d776172655f6d696c6c696d6574657273";
        System.out.println("Decodificado Hexadecimal: " + processInput(method5, input5));

        String method6 = "textoClaro";
        String input6 = "Mais um exemplo de frase";
        System.out.println("Texto Claro: " + processInput(method6, input6));
    }
}
