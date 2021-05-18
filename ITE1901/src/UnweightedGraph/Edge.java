public class Edge {
  private int u, v;

  public Edge(int u, int v) {
    this.u = u;
    this.v = v;
  }

  public int getU() {
    return u;
  }

  public void setU(int u) {
    this.u = u;
  }

  public int getV() {
    return v;
  }

  public void setV(int v) {
    this.v = v;
  }

  public boolean equals(Object o) {
    if (o instanceof Edge) {
      Edge other = (Edge) o;
      return u == other.u && v == other.v;
    } else {
      return false;
    }
  }
}
