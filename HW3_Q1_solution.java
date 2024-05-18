import java.util.*;

public class HW3_Q1_solution {
  public static void main(String[] args) {
    FileRead txt = new FileRead("HW3_Q1.txt");
    Valuefinder k = new Valuefinder();
    int V = Integer.parseInt(txt.Read());
    System.out.println("V=" + V);
    int E = Integer.parseInt(txt.Read());
    System.out.println("E=" + E);

    EdgeWeightedGraph G = new EdgeWeightedGraph(V);
    for (int i = 0; i < E; i++) {
      int[] temp = k.value(txt.Read());
      int v = temp[0];
      int w = temp[1];
      int weight = temp[2];
      Edge e = new Edge(v, w, weight);
      G.addEdge(e);
    }
    G.printGraph();
    System.out.println();
    System.out.println("The Minimum Spanning Tree Path: ");
    LazyPrimMST mst = new LazyPrimMST(G);
    for (Edge e : mst.edges())
      System.out.println(e);
    System.out.println("\nThe Minimum Spanning Tree value: " + mst.weight());
  }

  static public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
      this.V = V;
      this.E = 0;
      adj = (Bag<Edge>[]) new Bag[V];
      for (int v = 0; v < V; v++)
        adj[v] = new Bag<Edge>();
    }

    public int V() {
      return V;
    }

    public int E() {
      return E;
    }

    public void addEdge(Edge e) {
      int v = e.either(), w = e.other(v);
      adj[v].add(e);
      adj[w].add(e);
      E++;
    }

    public void printGraph() {
      for (int v = 0; v < V; v++) {
        for (Edge e : adj[v]) {
          System.out.println(v + " " + e.other(v) + " " + (int) e.weight());
        }
      }
    }

    public Iterable<Edge> adj(int v) {
      return adj[v];
    }

  }

  static public class LazyPrimMST {
    private Edge[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;

    public LazyPrimMST(EdgeWeightedGraph G) {
      edgeTo = new Edge[G.V()];
      distTo = new double[G.V()];
      marked = new boolean[G.V()];
      for (int v = 0; v < G.V(); v++)
        distTo[v] = Double.POSITIVE_INFINITY;
      pq = new IndexMinPQ<Double>(G.V());
      distTo[0] = 0.0;
      pq.insert(0, 0.0);
      while (!pq.isEmpty())
        visit(G, pq.delMin());
    }

    private void visit(EdgeWeightedGraph G, int v) {

      marked[v] = true;
      for (Edge e : G.adj(v)) {
        int w = e.other(v);
        if (marked[w])
          continue;
        if (e.weight() < distTo[w]) {
          edgeTo[w] = e;
          distTo[w] = e.weight();
          if (pq.contains(w))
            pq.change(w, distTo[w]);
          else
            pq.insert(w, distTo[w]);
        }
      }
    }

    public Iterable<Edge> edges() {
      Bag<Edge> mst = new Bag<Edge>();
      for (int v = 1; v < edgeTo.length; v++)
        mst.add(edgeTo[v]);
      return mst;
    }

    public double weight() {
      double weight = 0;
      for (Edge e : edges())
        weight += e.weight();
      return weight;
    }
  }

  static public class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, int weight) {
      this.v = v;
      this.w = w;
      this.weight = weight;
    }

    public double weight() {
      return weight;
    }

    public int either() {
      return v;
    }

    public int other(int vertex) {
      if (vertex == v)
        return w;
      else if (vertex == w)
        return v;
      else
        throw new RuntimeException("Inconsistent edge");
    }

    public int compareTo(Edge that) {
      if (this.weight() < that.weight())
        return -1;
      else if (this.weight() > that.weight())
        return +1;
      else
        return 0;
    }

    public String toString() {
      return String.format(v + " " + w + " " + (int) weight);
    }
  }

  static public class IndexMinPQ<Key extends Comparable<Key>> {
    private int N;
    private int[] pq;
    private int[] qp;
    private Key[] keys;

    public IndexMinPQ(int NMAX) {
      keys = (Key[]) new Comparable[NMAX + 1];
      pq = new int[NMAX + 1];
      qp = new int[NMAX + 1];
      for (int i = 0; i <= NMAX; i++)
        qp[i] = -1;
    }

    public boolean isEmpty() {
      return N == 0;
    }

    public boolean contains(int k) {
      return qp[k] != -1;
    }

    public void insert(int k, Key key) {
      if (contains(k))
        throw new RuntimeException("item is already in pq");
      N++;
      qp[k] = N;
      pq[N] = k;
      keys[k] = key;
      swim(N);
    }

    public int delMin() {
      if (N == 0)
        throw new RuntimeException("Priority queue underflow");
      int min = pq[1];
      exch(1, N--);
      sink(1);
      qp[min] = -1;
      keys[pq[N + 1]] = null;
      pq[N + 1] = -1;
      return min;
    }

    public void change(int k, Key key) {
      if (!contains(k))
        throw new RuntimeException("item is not in pq");
      keys[k] = key;
      swim(qp[k]);
      sink(qp[k]);
    }

    private boolean greater(int i, int j) {
      return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
      int swap = pq[i];
      pq[i] = pq[j];
      pq[j] = swap;
      qp[pq[i]] = i;
      qp[pq[j]] = j;
    }

    private void swim(int k) {
      while (k > 1 && greater(k / 2, k)) {
        exch(k, k / 2);
        k = k / 2;
      }
    }

    private void sink(int k) {
      while (2 * k <= N) {
        int j = 2 * k;
        if (j < N && greater(j, j + 1))
          j++;
        if (!greater(k, j))
          break;
        exch(k, j);
        k = j;
      }
    }
  }

  static public class Bag<Item> implements Iterable<Item> {
    private LinkedList<Item> items;

    public Bag() {
      items = new LinkedList<>();
    }

    public void add(Item item) {
      items.add(item);
    }

    @Override
    public Iterator<Item> iterator() {
      return items.iterator();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (Item item : items) {
        sb.append(item).append(" ");
      }
      return sb.toString();
    }
  }
}
