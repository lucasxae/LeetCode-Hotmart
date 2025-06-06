package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Organizer {
    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start organizer");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);

                // Extrair a lista de inteiros da mensagem
                int[] list = extractListFromMessage(message);

                if (list != null) {
                    // Organizar a lista usando putInOrder
                    int[] orderedList = putInOrder(list);
                    System.out.println( Arrays.toString(orderedList));
                    send(Arrays.toString(orderedList));
                } else {
                    System.out.println("Nenhuma lista encontrada na mensagem.");
                }
            }

            public int[] extractListFromMessage(String message) {
                String keyword = "Lista: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    int endIndex = message.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = message.length();
                    }
                    String listString = message.substring(startIndex, endIndex).trim();

                    // Remover colchetes e espaços, e dividir a string em partes por vírgulas
                    String[] numberStrings = listString.replace("[", "").replace("]", "").split(",");
                    int[] numbers = new int[numberStrings.length];

                    for (int i = 0; i < numberStrings.length; i++) {
                        numbers[i] = Integer.parseInt(numberStrings[i].trim());
                    }
                    return numbers;
                }
                return null;
            }

            public int[] putInOrder(int[] array) {
                List<Integer> evens = new ArrayList<>();
                List<Integer> odds = new ArrayList<>();

                // Separar os números em pares e ímpares
                for (int num : array) {
                    if (num % 2 == 0) {
                        evens.add(num);
                    } else {
                        odds.add(num);
                    }
                }

                // Combinar pares e ímpares na ordem desejada
                int[] result = new int[array.length];
                int index = 0;

                for (int even : evens) {
                    result[index++] = even;
                }
                for (int odd : odds) {
                    result[index++] = odd;
                }

                return result;
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
