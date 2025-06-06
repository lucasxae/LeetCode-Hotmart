package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoFinal {

    private static String methodFinal;
    private static String inputFinal;
    private static String encinputFinal;

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start cryptomix");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);

                String method = extractMethod(message);
                String input = extractInput(message);
                String encodedInput = extractEncodedInput(message);

                if(method != null) {
                    methodFinal = method;
                }
                if(input != null) {
                    inputFinal = input;
                }
                if(encodedInput != null) {
                    encinputFinal = encodedInput;
                }
                if(methodFinal != null && inputFinal != null) {
                    switch (methodFinal.toLowerCase()) {
                        case "md5":
                        case "sha1":
                            send(generateHash(methodFinal, inputFinal));
                            break;
                        default:
                            send(inputFinal.replace(" ", "_"));
                            break;
                    }
                }
                if(methodFinal != null && encinputFinal != null) {
                    switch (methodFinal.toLowerCase()) {
                        case "md5":
                        case "sha1":
                            send(generateHash(methodFinal, inputFinal));
                            break;
                        case "base64":
                            send(decodeBase64(inputFinal));
                            break;
                        case "binary":
                            send(convertFromBinary(inputFinal));
                            break;
                        case "hex":
                            send(convertFromHex(inputFinal));
                            break;
                        default:
                            send(inputFinal.replace(" ", "_"));
                            break;
                    }
                }

                System.out.println("Method: " + methodFinal);
            }
            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Conexão fechada: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

            public String extractMethod(String message) {
                String keyword = "Method: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    int endIndex = message.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = message.length();
                    }
                    return message.substring(startIndex, endIndex).trim();
                }
                return null;
            }

            public String extractInput(String message) {
                String keyword = "Sentence: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    int endIndex = message.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = message.length();
                    }
                    return message.substring(startIndex, endIndex).trim();
                }
                return null;
            }

            public String extractEncodedInput(String message) {
                String keyword = "Encoded: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    int endIndex = message.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = message.length();
                    }
                    return message.substring(startIndex, endIndex).trim();
                }
                return null;
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
        };
        client.connect();
    }
}




