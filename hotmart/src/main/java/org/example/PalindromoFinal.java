package org.example;
// se a palavra não for um palidromo e nao ter nenhum palindromo dentro dela, retorna "Sem palindromo"
// Se a palavra for um palindromo, retornar a palavra
// Se a palavra nao é um palindromo, mas dentro dela tiver 1, Retornar esse palindromo


//Pedro, um programador talentoso, enfrenta o desafio de decifrar uma mensagem criptografada
//e precisa da sua ajuda para encontrar os palíndromos nas palavras da mensagem. Seu objetivo
//é desenvolver um código que identifique o maior palíndromo na substring de cada palavra.
//Com seu código, você ajudará Pedro a descobrir quais palíndromos formam uma chave de decriptação
//crucial. Essa chave revelará uma ameaça iminente e permitirá que Pedro e sua equipe neutralizem a
//ameaça, tornando você um aliado essencial na segurança cibernética.
//
//Caso a palavra não contenha um palíndromo, a resposta deve ser "Sem palindromo"
//
//< [*] Stage: 1/40
//< [*] Word: radar


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class PalindromoFinal {

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start palindromo");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);
                String word = extractWordFromMessage(message);
                if (word != null && !word.isEmpty()) {
                    if (isPalindrome(word)) {
                        send(word);
                    } else {
                        String largestPalindrome = findLargestPalindromeMid(word);
                        if (largestPalindrome != null) {
                            send(largestPalindrome);
                        } else {
                            send("Sem palindromo");
                        }
                    }
                }
            }

            private String extractWordFromMessage(String message) {
                String keyword = "Word: ";
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

            private static boolean isPalindrome(String s) {
                int left = 0;
                int right = s.length() - 1;

                while (left < right) {
                    if (s.charAt(left) != s.charAt(right)) {
                        return false;
                    }
                    left++;
                    right--;
                }
                return true;
            }

            public static String findLargestPalindromeMid(String word) {
                String lowerCaseWord = word.toLowerCase();
                int length = lowerCaseWord.length();
                String largestPalindrome = null;

                for (int i = 0; i < length; i++) {
                    for (int j = i + 1; j < length; j++) {
                        String substring = lowerCaseWord.substring(i, j + 1);
                        if (isPalindrome(substring) && substring.length() > 1) {
                            if (largestPalindrome == null || substring.length() > largestPalindrome.length()) {
                                largestPalindrome = substring;
                            }
                        }
                    }
                }
                return largestPalindrome;
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Conexão fechada: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        client.connect();
    }
}
