package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class IndexNew {

    private static int[] array;
    private static int target;

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start search");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("Mensagem recebida: " + message);

                // Extrair o array e o target da mensagem
                int[] extractedArray = extractListFromMessage(message);
                int extractedTarget = extractTarget(message);

                if (extractedArray != null) {
                    array = extractedArray;
                    System.out.println("Array salvo: " + Arrays.toString(array));
                }

                if (extractedTarget != -1) {
                    target = extractedTarget;
                    System.out.println("Target salvo: " + target);
                }

                // Encontrar o índice do target no array se ambos estiverem disponíveis
                if (array != null && target > 0) {
                    int resultado = findIndex(array, target);
                    if (resultado != -1) {
                        System.out.println("Índice encontrado: " + resultado);
                        send(String.valueOf(resultado));
                    } else {
                        System.out.println("Target não encontrado no array.");
                        send("Target não encontrado");
                    }

                    // Limpar o array e o target após o envio
                    array = null;
                    target = -1;
                }
            }

            // Método para extrair o valor do target da mensagem
            public static int extractTarget(String message) {
                String keyword = "[*] Target: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    int endIndex = message.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = message.length();
                    }
                    String targetString = message.substring(startIndex, endIndex).trim();
                    try {
                        return Integer.parseInt(targetString);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter o valor alvo.");
                    }
                }
                return -1;
            }

            // Método para encontrar o índice do target no array
            public static int findIndex(int[] array, int target) {
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == target) {
                        return i;
                    }
                }
                return -1; // Retorna -1 se o elemento não for encontrado
            }

            // Método para extrair o array da mensagem
            public static int[] extractListFromMessage(String message) {
                String keyword = "[*] Array: ";
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
                        try {
                            numbers[i] = Integer.parseInt(numberStrings[i].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Erro ao converter número da lista.");
                        }
                    }
                    return numbers;
                }
                return null;
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
