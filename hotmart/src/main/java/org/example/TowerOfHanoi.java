package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TowerOfHanoi {
    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("wss://ctf-challenges.devops.hotmart.com/echo")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Conexão aberta.");
                send("echo");
                send("start towerofhanoi");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(message);

               int[] principes = extractListFromMessage(message);

                List<String> moves = resolverHanoi(principes[0], 'A', 'C', 'B');
                System.out.println(moves);
                send(String.valueOf(moves));
            }

            public int[] extractListFromMessage(String message) {
                String keyword = "Príncipes: ";
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

            private static void hanoi(int n, char origem, char destino, char auxiliar, List<String> movimentos) {
                if (n == 1) {
                    movimentos.add("('" + origem + "', '" + destino + "')");
                    return;
                }
                hanoi(n - 1, origem, auxiliar, destino, movimentos);
                movimentos.add("('" + origem + "', '" + destino + "')");
                hanoi(n - 1, auxiliar, destino, origem, movimentos);
            }

            public static List<String> resolverHanoi(int n, char origem, char destino, char auxiliar) {
                List<String> movimentos = new ArrayList<>();
                hanoi(n, origem, destino, auxiliar, movimentos);
                return movimentos;
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





//        **Torre de Hanói: A Jornada dos Príncipes:**
//
//Era uma vez, no reino encantado de Hanópolis, um antigo templo abrigava uma lenda que há
//gerações fascinava sábios, aventureiros e estudiosos. No alto da montanha dourada,
//um desafio ancestral conhecido como A Jornada dos Príncipes aguardava aqueles que
//ousavam testar suas mentes e conquistar a sabedoria.
//
//        No templo, três pinos dourados se erguiam, e sobre o primeiro deles repousavam discos
//de diferentes tamanhos, dispostos em uma torre majestosa.
//Cada disco representava um príncipe em uma missão para cruzar as terras de Hanópolis
//e alcançar o grande trono dourado do outro lado.
//Mas, como toda jornada épica, havia regras e desafios a serem enfrentados.
//
//Missão: Os príncipes devem viajar do pino inicial (Torre A) para o pino final (Torre C),
//usando o pino auxiliar (Torre B) como ponto de passagem seguro.
//Todos os príncipes devem chegar ao trono em sua ordem original, do maior ao menor, sem quebrar a hierarquia.
//
//        **Regras**
//
//        *Movimento Solitário*: Apenas um príncipe pode se mover por vez, em passos calculados e precisos.
//        *Ordem Hierárquica*: Um príncipe mais forte (maior disco) nunca pode ser colocado sobre um príncipe mais fraco (menor disco).
//        *Caminho Ideal*: A jornada deve ser completada no menor número possível de movimentos, refletindo a destreza e sabedoria do viajante.
//
//Você receberá a quantidade de princípes e a quantidade mínima de movimentos necessários para concluir a missão.
//
//        **Instruções de Envio:**
//Envie sua solução como uma lista de tuplas [(de, para), ...] em uma única linha, onde cada tupla representa um movimento entre as torres A, B ou C.
//Exemplo de formato de envio: [('A', 'B'), ('A', 'C'), ('B', 'C')]
//
//<
//        **Detalhes da Missão:**
//
//Príncipes: [7]
//Movimentos Mínimos Necessários: [127]
//
//Você deve completar 10 jornadas corretas consecutivas para ganhar a bandeira de sabedoria do reino.
//
//< Entrada inválida. Você tem 10 segundos para enviar uma lista de movimentos válidas.