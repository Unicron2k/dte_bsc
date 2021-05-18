import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UnweightedGraph<V> implements Graph<V> {
  protected List<V> vertices = new ArrayList<>(); // Store vertices
  protected List<List<Edge>> neighbors = new ArrayList<>(); // Adjacency lists

  /** Construct an empty graph */
  protected UnweightedGraph() {
  }

  /** Construct a graph from vertices and edges stored in arrays */
  protected UnweightedGraph(V[] vertices, int[][] edges) {
    for (V vertex : vertices) {
      addVertex(vertex);
    }

    createAdjacencyLists(edges, vertices.length);
  }

  /** Construct a graph from vertices and edges stored in List */
  protected UnweightedGraph(List<V> vertices, List<Edge> edges) {
    for (V vertex : vertices) {
      addVertex(vertex);
    }

    createAdjacencyLists(edges, vertices.size());
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  protected UnweightedGraph(List<Edge> edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) {
      addVertex((V) (new Integer(i))); // vertices is {0, 1, ...}
    }

    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Construct a graph from integer vertices 0, 1, and edge array */
  protected UnweightedGraph(int[][] edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) {
      addVertex((V) (new Integer(i))); // vertices is {0, 1, ...}
    }

    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Add a vertex to the graph */
  @Override
  public boolean addVertex(V vertex) {
    if (!vertices.contains(vertex)) {
      vertices.add(vertex);
      neighbors.add(new ArrayList<Edge>());
      return true;
    } else {
      return false;
    }
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(int[][] edges, int numberOfVertices) {
    for (int[] edge : edges) {
      addEdge(edge[0], edge[1]);
    }
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(List<Edge> edges, int numberOfVertices) {
    for (Edge edge : edges) {
      addEdge(edge);
    }
  }

  /** Add an edge to the graph */
  @Override
  public boolean addEdge(int u, int v) {
    return addEdge(new Edge(u, v));
  }

  /** Add an edge to the graph */
  @Override
  public boolean addEdge(Edge e) {
    if (e.getU() < 0 || e.getU() > getSize() - 1) {
      throw new IllegalArgumentException("No such index: " + e.getU());
    }

    if (e.getV() < 0 || e.getV() > getSize() - 1) {
      throw new IllegalArgumentException("No such index: " + e.getV());
    }

    if (!neighbors.get(e.getU()).contains(e)) {
      neighbors.get(e.getU()).add(e);
      return true;
    }

    return false;
  }

  /** Return the number of vertices in the graph */
  @Override
  public int getSize() {
    return vertices.size();
  }

  /** Return the vertices in the graph */
  @Override
  public List<V> getVertices() {
    return vertices;
  }

  /** Return the object for the specified vertex */
  @Override
  public V getVertex(int index) {
    checkValidIndex(index);

    return vertices.get(index);
  }

  /** Return the index for the specified vertex object */
  @Override
  public int getIndex(V v) {
    return vertices.indexOf(v);
  }

  /** Return the neighbors of the specified vertex */
  @Override
  public List<Integer> getNeighbors(int index) {
    checkValidIndex(index);

    List<Integer> result = new ArrayList<>();
    for (Edge e : neighbors.get(index)) {
      result.add(e.getV());
    }

    return result;
  }

  private void checkValidIndex(int index) {
    if (getSize() <= index) {
      throw new IllegalArgumentException("No such index: " + index);
    }
  }

  /** Return the degree for a specified vertex */
  @Override
  public int getDegree(int v) {
    return neighbors.get(v).size();
  }

  /** Print the edges */
  @Override
  public void printEdges() {
    for (int u = 0; u < neighbors.size(); u++) {
      System.out.print(getVertex(u) + " (" + u + "): ");

      for (Edge e : neighbors.get(u)) {
        System.out.print("(" + getVertex(e.getU()) + ", " + getVertex(e.getV()) + ") ");
      }

      System.out.println();
    }
  }

  /** Clear the graph */
  @Override
  public void clear() {
    vertices.clear();
    neighbors.clear();
  }

  @Override
  public boolean remove(V v) {
    return false; // Left as an exercise
  }

  @Override
  public boolean remove(int u, int v) {
    return false; // Left as an exercise
  }

  /** Obtain a DFS tree starting from vertex v */
  @Override
  public SearchTree dfs(int v) {
    List<Integer> searchOrder = new ArrayList<>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++) {
      parent[i] = -1; // Initialize parent[i] to -1
    }

    // Mark visited vertices
    boolean[] isVisited = new boolean[vertices.size()];

    // Recursively search
    dfs(v, parent, searchOrder, isVisited);

    // Return a search tree
    return new SearchTree(v, parent, searchOrder);
  }

  /** Recursive method for DFS search */
  private void dfs(int u, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
    // Store the visited vertex
    searchOrder.add(u);
    isVisited[u] = true; // Vertex v visited

    for (Edge e : neighbors.get(u)) {
      if (!isVisited[e.getV()]) {
        parent[e.getV()] = u; // The parent of vertex e.v is u
        dfs(e.getV(), parent, searchOrder, isVisited); // Recursive search
      }
    }
  }

  /** Starting bfs search from vertex v */
  @Override
  public SearchTree bfs(int v) {
    List<Integer> searchOrder = new ArrayList<>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++) {
      parent[i] = -1; // Initialize parent[i] to -1
    }

    LinkedList<Integer> queue = new LinkedList<>(); // list used as a queue
    boolean[] isVisited = new boolean[vertices.size()];

    queue.offer(v); // Enqueue v
    isVisited[v] = true; // Mark it visited

    while (!queue.isEmpty()) {
      int u = queue.removeFirst(); // Dequeue to u
      searchOrder.add(u); // u searched
      for (Edge e : neighbors.get(u)) {
        if (!isVisited[e.getV()]) {
          queue.addLast(e.getV()); // Enqueue w
          parent[e.getV()] = u; // The parent of w is u
          isVisited[e.getV()] = true; // Mark it visited
        }
      }
    }

    return new SearchTree(v, parent, searchOrder);
  }

  /** Tree inner class inside the UnweightedGraph class */
  public class SearchTree {
    private int root; // The root of the tree
    private int[] parent; // Store the parent of each vertex
    private List<Integer> searchOrder; // Store the search order

    /** Construct a tree with root, parent, and searchOrder */
    public SearchTree(int root, int[] parent, List<Integer> searchOrder) {
      this.root = root;
      this.parent = parent;
      this.searchOrder = searchOrder;
    }

    /** Return the root of the tree */
    public int getRoot() {
      return root;
    }

    /** Return the parent of vertex v */
    public int getParent(int v) {
      return parent[v];
    }

    /** Return an array representing search order */
    public List<Integer> getSearchOrder() {
      return searchOrder;
    }

    /** Return number of vertices found */
    public int getNumberOfVerticesFound() {
      return searchOrder.size();
    }

    /** Return the path of vertices from a vertex to the root */
    public List<V> getPath(int index) {
      checkValidIndex(index);

      List<V> path = Arrays.asList(vertices.get(index));
      index = parent[index];

      while (index != -1) {
        path.add(vertices.get(index));
        index = parent[index];
      }

      return path;
    }

    /** Print a path from the root to vertex v */
    public void printPath(int index) {
      checkValidIndex(index);

      List<V> path = getPath(index);
      System.out.print("A path from " + vertices.get(root) + " to " + vertices.get(index) + ": ");

      for (int i = path.size() - 1; i >= 0; i--) {
        System.out.print(path.get(i) + " ");
      }

      System.out.println();
    }

    /** Print the whole tree */
    public void printTree() {
      System.out.println("Root is: " + vertices.get(root));
      System.out.print("Edges: ");

      for (int i = 0; i < parent.length; i++) {
        if (parent[i] != -1) {
          System.out.print("(" + vertices.get(parent[i]) + ", " + vertices.get(i) + ") ");
        }
      }

      System.out.println();
    }
  }
}
