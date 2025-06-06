package org.example;

import java.util.*;

public class UailogManual {

    public static void main(String[] args) {
        // Sample list of edges
        List<String[]> edges = new ArrayList<>();
        edges.add(new String[]{"N", "P", "18"});
        edges.add(new String[]{"V", "Q", "17"});
        edges.add(new String[]{"E", "Q", "4"});
        edges.add(new String[]{"N", "G", "2"});
        edges.add(new String[]{"F", "H", "7"});
        edges.add(new String[]{"F", "G", "6"});
        edges.add(new String[]{"E", "G", "15"});
        edges.add(new String[]{"P", "F", "5"});
        edges.add(new String[]{"E", "N", "12"});
        edges.add(new String[]{"N", "Y", "2"});
        edges.add(new String[]{"G", "F", "13"});
        edges.add(new String[]{"H", "V", "10"});
        edges.add(new String[]{"N", "Q", "5"});
        edges.add(new String[]{"Q", "N", "17"});
        edges.add(new String[]{"N", "E", "17"});
        edges.add(new String[]{"N", "V", "16"});
        edges.add(new String[]{"H", "P", "5"});
        edges.add(new String[]{"N", "H", "8"});
        edges.add(new String[]{"V", "P", "3"});
        edges.add(new String[]{"F", "V", "4"});
        edges.add(new String[]{"G", "E", "1"});
        edges.add(new String[]{"Y", "E", "7"});
        edges.add(new String[]{"Q", "G", "13"});
        edges.add(new String[]{"V", "Q", "12"});
        edges.add(new String[]{"G", "P", "19"});
        edges.add(new String[]{"H", "V", "12"});
        edges.add(new String[]{"E", "Y", "20"});
        edges.add(new String[]{"G", "H", "16"});
        edges.add(new String[]{"N", "H", "7"});
        edges.add(new String[]{"F", "Y", "20"});
        edges.add(new String[]{"Y", "V", "14"});
        edges.add(new String[]{"N", "P", "3"});
        edges.add(new String[]{"F", "P", "19"});
        edges.add(new String[]{"N", "P", "6"});
        edges.add(new String[]{"V", "E", "17"});




        // Prompt user for initial and final points
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the initial delivery point: ");
        String initialPoint = scanner.nextLine().trim();
        System.out.print("Enter the final delivery point: ");
        String finalPoint = scanner.nextLine().trim();

        // Find the shortest path
        Result result = findShortestPath(edges, initialPoint, finalPoint);

        // Display the result
        if (result.distance < Integer.MAX_VALUE) {
            System.out.println("Shortest distance: " + result.distance);
            System.out.println("Path: " + String.join(" -> ", result.path));
        } else {
            System.out.println("No path found from " + initialPoint + " to " + finalPoint);
        }

        scanner.close();
    }

    static class Result {
        int distance;
        List<String> path;

        Result(int distance, List<String> path) {
            this.distance = distance;
            this.path = path;
        }
    }

    static class Edge {
        String target;
        int weight;

        Edge(String target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    static class Node {
        String id;
        int distance;

        Node(String id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }

    public static Result findShortestPath(List<String[]> edges, String start, String end) {
        // Create graph as an adjacency map
        Map<String, List<Edge>> graph = new HashMap<>();
        for (String[] edge : edges) {
            String u = edge[0];
            String v = edge[1];
            int weight = Integer.parseInt(edge[2]);

            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, weight));
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, weight)); // non-directed graph
        }

        // Initialize distances and previous path
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));

        // Setup initial distances and queue
        queue.add(new Node(start, 0));
        dist.put(start, 0);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            String u = node.id;

            if (!visited.add(u)) {
                continue;
            }

            if (u.equals(end)) {
                break;
            }

            // For each neighbor of u
            for (Edge edge : graph.getOrDefault(u, new ArrayList<>())) {
                String v = edge.target;
                int weight = edge.weight;

                int alt = dist.getOrDefault(u, Integer.MAX_VALUE) + weight;
                if (alt < dist.getOrDefault(v, Integer.MAX_VALUE)) {
                    dist.put(v, alt);
                    previous.put(v, u);
                    queue.add(new Node(v, alt));
                }
            }
        }

        // Reconstruct the shortest path
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return new Result(dist.getOrDefault(end, Integer.MAX_VALUE), path);
    }
}
