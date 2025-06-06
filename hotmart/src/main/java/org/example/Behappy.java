package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Behappy {
    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start behappy");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("Mensagem recebida: " + message);

                // Extrair o número da mensagem
                int number = extractNumber(message);

                // Verifica se um número válido foi extraído
                if (number != -1) {
                    // Determina se o número é feliz
                    if (isHappy(number)) {
                        System.out.println("Feliz");
                        send("Feliz");
                    } else {
                        System.out.println("Infeliz");
                        send("Infeliz");
                    }
                }
            }

            // Método para extrair o número da mensagem
            public static int extractNumber(String message) {
                String[] parts = message.split("Number: ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter número.");
                    }
                }
                return -1; // Retorna -1 se o número não for encontrado
            }

            // Método para verificar se um número é feliz
            public static boolean isHappy(int number) {
                int slow = number;
                int fast = number;

                do {
                    slow = squareDigitSum(slow); // Move um passo
                    fast = squareDigitSum(squareDigitSum(fast)); // Move dois passos
                } while (slow != fast);

                // Se atingir 1, é um número feliz
                return slow == 1;
            }

            // Método para calcular a soma dos quadrados dos dígitos de um número
            public static int squareDigitSum(int number) {
                int sum = 0;
                while (number > 0) {
                    int digit = number % 10;
                    sum += digit * digit;
                    number /= 10;
                }
                return sum;
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
