import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class UnweightedGraphModifiedDFS<V> extends UnweightedGraph<V> {
  /** Construct an empty graph */
  public UnweightedGraphModifiedDFS() {
  }

  /** Construct a graph from vertices and edges stored in arrays */
  public UnweightedGraphModifiedDFS(V[] vertices, int[][] edges) {
    super(vertices, edges);
  }

  /** Construct a graph from vertices and edges stored in List */
  public UnweightedGraphModifiedDFS(List<V> vertices, List<Edge> edges) {
    super(vertices, edges);
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  public UnweightedGraphModifiedDFS(List<Edge> edges, int numberOfVertices) {
    super(edges, numberOfVertices);
  }

  /** Construct a graph from integer vertices 0, 1, and edge array */
  public UnweightedGraphModifiedDFS(int[][] edges, int numberOfVertices) {
    super(edges, numberOfVertices);
  }

  @Override
  public SearchTree dfs(int v) {
    List<Integer> searchOrder = new ArrayList<>();  // Store the order we searched in
    Stack<Integer> vertices = new Stack<>();        // Stack holding the vertices we are to visit

    int[] parent = new int[this.vertices.size()];             // Store the parents of each vertice
    Arrays.fill(parent, -1);                             // Initialize parents to -1
    boolean[] isVisited = new boolean[this.vertices.size()];  // store if vertice is visited

    vertices.push(v); // Push initial start-vertex onto the stack

    while(!vertices.empty()){     // While we have vertices
      v = vertices.pop();         // pop an element from vertices
      if(!isVisited[v]){          // if not visited, lets visit it!
        searchOrder.add(v);       // add it to the search-order-list
        isVisited[v] = true;      // and mark it as visited
      }

      for (Edge edge : neighbors.get(v)) {  // for each edge neighboring to vertice V
        if(!isVisited[edge.getV()]){        // check if this edge is visited
          vertices.push(edge.getV());       // if not, push the edge to the stack
          parent[edge.getV()] = v;          // mark current vertice as the parent of the vertice we checked
        }
      }
    }
    return new SearchTree(v, parent, searchOrder);  // Now we plant a SearchTree, and return it
  }
}
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::N
N......................................................................................................................N
N.. ,--.   ,--.                             ,--.                                    ,--.                          .....N
N.. |   `.'   |,--. ,--.     ,---. ,---.  ,-|  | ,---.     ,--.   ,--. ,---. ,--.--.|  |,-.   ,---.                .....N
N.. |  |'.'|  | \  '  /     | .--'| .-. |' .-. || .-. :    |  |.'.|  || .-. ||  .--'|     /  (  .-'                .....N
N.. |  |   |  |  \   '      \ `--.' '-' '\ `-' |\   --.    |   .'.   |' '-' '|  |   |  \  \ .-'  `).--..--..--.   .....N
N.. `--'   `--'.-'  /        `---' `---'  `---'  `----'    '--'   '--' `---' `--'   `--'`--'`----' '--''--''--'   .....N
N.............`---'....................................................................................................N
N......................................................................................................................N
N...................................................................................................... ,------.  .....N
N............................................................                        ,--.              '  .--.  ' .....N
N............................................................             ,--.   ,--.|  ,---. ,--. ,--.'--' _|  | .....N
N............................................................             |  |.'.|  ||  .-.  | \  '  /  .--' __'  .....N
N............................................................ .--..--..--.|   .'.   ||  | |  |  \   '   `---'     .....N
N............................................................ '--''--''--''--'   '--'`--' `--'.-'  /    .---.     .....N
N.............................................................................................`---'.....'---'..........N
N......................................................................................................................N
N..............................................................................---:////////::--........................N
N.......................................................................-:/oso+++/::-------:/++oso/-...................N
N....................................................................-oy/:.`                     `:os/.................N
N...................................................................os.                             `/y+...............N
N.................................................................-h/                                 `os..............N
N................................................................-d-                                    /h.............N
N................................................................h:                                      so............N
N.........-oooooooooooooooooooooooooooooooooo+..................+y                                       .d............N
N.......:os:                                `d:.................d.                                        y/...........N
N.......ds++++++++++++++++++++++++++++++ss   +s................-d`        `. `                            oo...........N
N.......d.                             :hd-  .d................od/::-.`.y.-`.y+://+/++++++s`              oo...........N
N.......h:                            :y.+o   y+...............:m-+/:/+d-    `d//-````````s+              h/...........N
N.......oy/////////:::::::::-.......-oy` `h`  :h................d:/- `+y      os+:      `+y.             :h............N
N........h/........---------:::::::::y+   s/  `d:...............+do++o:`      `/o+///++oo-`             .d:............N
N......../h                          .d   -h   +s................h:       ...   ``...```               -h:.............N
N.........d-                          y/   h:  .d................:d.  -sssyyyso+-`                   `+y:..............N
N.........+y     o+/////////////.//   -h   /y   y/................/h/oo:s/:-//y+os/`                -so-...............N
N..........d.   `d-``````......./yd`   h:  `d.  :y.................:md`  `+++o.  `oy-             -oo-.................N
N..........os   -h              s/h-   /y   oo  `d-................-dos/`          .s+`        `:yy:...................N
N..........-d.  oo              d.+s   `d-  .d`  so................+s.-+s/-`        `+s.   `:/ooomm....................N
N...........so  h-             .d /s    +s   s/  -d................y+...-:+o+/:-......+d++yhs/-..:m+...................N
N...........-d``d`             :y y/    `d.  -h`  h/...............d:.......-://////////hhy:......dm/..................N
N............y+:y              o+`d`     oo   h:  /y...............m-..................:ydo......-d:h:.................N
N............:ds/              h--h      .h`  /y  `d-.............-m.................:ss:-d:.....+y./h.................N
N.............hN-             `d`oo       s/  `d.  so.............-m...............-os:...:h-....s+..y+................N
N............./m`             :y h-       -h`  oo  /h..............m-............:so/......os....d:..-d-...............N
N.............+y              +o`m`````````h/-.-d-oo:..............y/..........:os:........-d-..:d....h/...............N
N.............y/              h:/myyyyyyyyydyyhhdo:................oo.......-/so:...........ss..y+....+o...............N
N.............d.             `d.sy+++++++++//////::::::/o+/:--...../y.....:os+-.............-m./h.....:h...............N
N------------/d--------------:mohdyyyyhhyyy+/:::::::::-y+.:/+s/....-d-.-+ss/.................d+d/.----:m------------...N
N///////////yh/::::::::::::::::h`          -s/`````````s+..--ss+++++ysooyo+++++++++++++++++++dhs++ooooos/++ooossyhhh/..N
N         `ss-.......----------h+///////////+y/////////+///:::`                              `.```.--:/++++++//:-.`oo..N
N         `///////////:::::::::-`                                                  ````.-:/++oo++//::..`           oo..N
m++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++osyhdddhysoo++++++++++++++++++++++yy:*/