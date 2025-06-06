package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Heros {

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start rpg");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);

                // Extrair a lista de números e a habilidade da mensagem
                int[] habilidades = extractListFromMessage(message);
                int habilidade = extractHabilidade(message);

                if (habilidades != null && habilidade != -1) {
                    // Encontrar os índices dos três heróis cujo total de habilidade é igual ao valor necessário
                    int[] resultado = encontrarTrioPerfeito(habilidades, habilidade);
                    if (resultado != null) {
                        String resposta = Arrays.toString(resultado);
                        System.out.println("Índices dos heróis: " + resposta);
                        send(resposta);
                    } else {
                        System.out.println(".");
                    }
                } else {
                    System.out.println(".");
                }
            }

            // Método para encontrar um trio perfeito de heróis
            public static int[] encontrarTrioPerfeito(int[] habilidades, int habilidadeNecessaria) {
                int n = habilidades.length;
                for (int i = 0; i < n - 2; i++) {
                    for (int j = i + 1; j < n - 1; j++) {
                        for (int k = j + 1; k < n; k++) {
                            if (habilidades[i] + habilidades[j] + habilidades[k] == habilidadeNecessaria) {
                                return new int[]{i, j, k};
                            }
                        }
                    }
                }
                return null;
            }

            // Método para extrair o valor da habilidade necessária da mensagem
            public static int extractHabilidade(String message) {
                String keyword = "Habilidade: ";
                int startIndex = message.indexOf(keyword);
                if (startIndex != -1) {
                    startIndex += keyword.length();
                    String habilidadeString = message.substring(startIndex).trim();
                    try {
                        return Integer.parseInt(habilidadeString);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter habilidade.");
                    }
                }
                return -1; // Retorna -1 se o valor não for encontrado
            }

            // Método para extrair a lista de habilidades dos heróis da mensagem
            public static int[] extractListFromMessage(String message) {
                String keyword = "Hérois: ";
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
