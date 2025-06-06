package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheKing {
    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start lost_treasure");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);

                // Extrair a lista de inteiros da mensagem
                int[] list = extractListFromMessage(message);

                if (list != null) {
                    int maxForce = maxSubarraySum(list);
                    send(String.valueOf(maxForce));
                } else {
                    System.out.println("Nenhuma lista encontrada na mensagem.");
                }
            }

            public int[] extractListFromMessage(String message) {
                String keyword = "Array: ";
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
            };

            public static int maxSubarraySum(int[] nums) {
                // Verifica se o array está vazio
                if (nums == null || nums.length == 0) {
                    throw new IllegalArgumentException("O array não pode estar vazio");
                }

                // Inicializa as variáveis
                int maxAtual = nums[0]; // A soma máxima que termina na posição atual
                int maxGlobal = nums[0]; // A maior soma de qualquer subarray encontrada até agora

                // Percorre o array a partir do segundo elemento
                for (int i = 1; i < nums.length; i++) {
                    // Atualiza maxAtual para ser o maior entre o elemento atual
                    // e maxAtual mais o elemento atual
                    maxAtual = Math.max(nums[i], maxAtual + nums[i]);

                    // Atualiza maxGlobal se maxAtual for maior
                    if (maxAtual > maxGlobal) {
                        maxGlobal = maxAtual;
                    }
                }

                return maxGlobal; // Retorna a soma máxima encontrada
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
