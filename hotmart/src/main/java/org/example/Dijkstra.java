package org.example;

import java.util.*;

public class Dijkstra {

    static class Edge {
        String target;
        int weight;

        Edge(String target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        // Lista de arestas (origem, destino, peso)
        List<String[]> edges = Arrays.asList(
                new String[]{"C", "W", "11"},
                new String[]{"J", "Z", "18"},
                new String[]{"Z", "N", "16"},
                new String[]{"N", "J", "2"},
                new String[]{"W", "C", "13"},
                new String[]{"W", "J", "10"},
                new String[]{"J", "Z", "16"},
                new String[]{"Z", "N", "17"}
        );

        String start = "W";
        String end = "N";

        Dijkstra dijkstra = new Dijkstra();
        Result result = dijkstra.findShortestPath(edges, start, end);
        System.out.println("Menor distância: " + result.distance);
        System.out.println("Caminho mais curto: " + result.path);
    }

    static class Result {
        int distance;
        List<String> path;

        Result(int distance, List<String> path) {
            this.distance = distance;
            this.path = path;
        }
    }

    public Result findShortestPath(List<String[]> edges, String start, String end) {
        // Criar grafo como um mapa de adjacência
        Map<String, List<Edge>> graph = new HashMap<>();
        for (String[] edge : edges) {
            String u = edge[0];
            String v = edge[1];
            int weight = Integer.parseInt(edge[2]);

            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, weight));
        }

        // Inicializar distâncias e caminho
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        for (String node : graph.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
            previous.put(node, null);
        }
        dist.put(start, 0);

        // Fila de prioridade para selecionar o próximo nó com a menor distância
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            String u = queue.poll();

            // Para cada vizinho de u
            for (Edge edge : graph.getOrDefault(u, new ArrayList<>())) {
                String v = edge.target;
                int weight = edge.weight;

                int alt = dist.get(u) + weight;
                if (alt < dist.getOrDefault(v, Integer.MAX_VALUE)) {
                    dist.put(v, alt);
                    previous.put(v, u);
                    queue.add(v);
                }
            }
        }

        // Reconstruir o caminho mais curto
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return new Result(dist.getOrDefault(end, Integer.MAX_VALUE), path);
    }
}
