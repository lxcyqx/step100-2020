package com.google.sps.graph;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra<V extends Vertex<E>, E extends Edge<V>> {

  public Vertex<E> runDijkstra(V src, V dest) {
    // Used to keep track of vertices closest to the source vertex
    PriorityQueue<V> minHeap = new PriorityQueue<V>(new VertexComparator<E>());
    src.setDistance(0);
    minHeap.add(src);
    Set<V> visitedSet = new HashSet<V>();
    while (!minHeap.isEmpty()) {
      // Pop off vertex with smallest distance from src
      V current = minHeap.peek();
      visitedSet.add(minHeap.poll());
      if (current.equals(dest)) {
        return current;
      }
      List<E> outgoingEdges = current.getOutgoingEdges();
      if (outgoingEdges == null) {
        return null;
      }
      this.processNeighbors(current, outgoingEdges, visitedSet, minHeap);
    }
    return dest;
  }

  private void processNeighbors(
      V current, List<E> outgoingEdges, Set<V> visitedSet, PriorityQueue<V> minHeap) {
    for (E e : outgoingEdges) {
      // Get the vertex on the other side of the edge
      V adjacent = e.getDestVertex();
      // Update the shortest path to adjacent vertex
      double newDistanceToAdjacent = current.getDistance() + e.getEdgeWeight();
      if (newDistanceToAdjacent < adjacent.getDistance()
          || (!minHeap.contains(adjacent) && !visitedSet.contains(adjacent))) {
        adjacent.setDistance(newDistanceToAdjacent);
        // remember path to this vertex
        adjacent.setPrev(e);
        // Trigger reheapify since vertex has been mutated
        if (minHeap.contains(adjacent)) {
          minHeap.add(minHeap.remove());
        } else {
          minHeap.add(adjacent);
        }
      }
    }
  }
}
