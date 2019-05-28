import java.util.*;
import java.io.*;

public class Problem1{
  public static int clock = 0;
  public static int ccnum = 0;
  public static void main(String[] args){
    Scanner in = new Scanner(System.in);

    System.out.println("Enter a directed graph:");

    int n = -1;
    HashMap<Integer,ArrayList<Integer>> adj = new HashMap<>();
    HashMap<Integer, Boolean> visited = new HashMap<>();
    HashMap<Integer, Integer> pre = new HashMap<>();
    HashMap<Integer, Integer> post = new HashMap<>();
    HashMap<Integer, Integer> ccNumMap = new HashMap<>();
    HashSet<Integer> notSource = new HashSet<>();

    HashMap<Integer, Integer> mappings = new HashMap<>();

    while (in.hasNext()) {
      String str = in.next();
      if (str.contains(",")) {
        // add to adjacency list of u
        if (str.equals("0,0")) {break;}

        String[] strArr = str.split(",");
        int u = Integer.parseInt(strArr[0]), v = Integer.parseInt(strArr[1]);

        mappings.put(u, v); // keep up with each mapping to later test for existence of back edge

        if (!adj.containsKey(u)) {adj.put(u, new ArrayList<>()); }
        if (!adj.containsKey(v)) {adj.put(v, new ArrayList<>());}

        ArrayList<Integer> uAdj = adj.get(u), vAdj = adj.get(v);
        uAdj.add(v); notSource.add(v);
      }
      else {
        n = Integer.parseInt(str);
      }
    }
    for (int i = 1; i <= n; ++i){
      if (!adj.containsKey(i)) {
        adj.put(i, new ArrayList<>());
      }
    }

    HashSet<Integer> source = new HashSet<>();
    for (Integer key : adj.keySet()) {
      if (!notSource.contains(key) && !adj.get(key).isEmpty()) {source.add(key);}
    }
    System.out.println(source);
    dfs(adj, visited, pre, post, ccNumMap);

    boolean cycleExists = false;
    for (Integer u : mappings.keySet()) {
      Integer v = mappings.get(u);
      if ((pre.get(v) < pre.get(u) && pre.get(v) < post.get(u)) && (post.get(v) > pre.get(u) && post.get(v) > post.get(u))) {
        cycleExists = true;
        System.out.println("YES");
      }
    }
    if (!cycleExists) {
      System.out.println("NO");
      HashMap<Integer, Integer> postKeyToValueSwitch = new HashMap<>();
      for (Integer key : post.keySet()) {
        postKeyToValueSwitch.put(post.get(key), key);
      }
      Object[] keys = postKeyToValueSwitch.keySet().toArray();
      Arrays.sort(keys);
      System.out.print("Topological sorting: ");
      for (int i = keys.length-1; i >= 0; i--) {
        if (i != 0) {
          System.out.print(postKeyToValueSwitch.get(keys[i]) + " ");
        } else {
          System.out.print(postKeyToValueSwitch.get(keys[i]) + "\n");
        }
      }
      System.out.println("Length of the longest path in the DAG: " + findLongestPath(adj, source));
    }
  }

  public static void dfs(HashMap<Integer, ArrayList<Integer>> G, HashMap<Integer, Boolean> visited, HashMap<Integer, Integer> pre, HashMap<Integer, Integer> post, HashMap<Integer, Integer> ccNumMap ) {
    ccnum = 0; clock = 0;
    boolean wait = false;
    for (Integer u : G.keySet()) {
      if (!visited.containsKey(u)){
        visited.put(u, false);
        pre.put(u, 0);
        post.put(u, 0);
        ccNumMap.put(u,0);
      }
    }

    for (Integer u : G.keySet()) {
      for (Integer key : G.keySet()) {
        if (wait == true) {break;}
        if (key != u) {
          for (Integer v : G.get(key)) {
            if (v == u) {
              wait = true;
              break;
            }
          }
        }
      }
      if (!wait) {
        if (!visited.get(u)) {
          ++ccnum;
          explore(G, u, visited, pre, post, ccNumMap, ccnum);
        }
      }
      wait = false;
    }
  }

  public static void explore(HashMap<Integer, ArrayList<Integer>> G, Integer u, HashMap<Integer, Boolean> visited, HashMap<Integer, Integer> pre, HashMap<Integer, Integer> post, HashMap<Integer, Integer> ccNumMap, int ccnum) {

    visited.put(u, true);
    ccNumMap.put(u,ccnum);
    previsit(pre, u);
    for (Integer v : G.get(u)) {
      if (!visited.get(v)){
        explore(G, v, visited, pre, post, ccNumMap, ccnum);}
    }
    postvisit(post, u);
  }

  public static void previsit(HashMap<Integer, Integer> pre, int u) {
    clock += 1;
    pre.put(u, clock);
  }

  public static void postvisit(HashMap<Integer, Integer> post, int u) {
    clock += 1;
    post.put(u, clock);
  }

  public static int findLongestPath(HashMap<Integer, ArrayList<Integer>> G, HashSet<Integer> source) {
    int pathLength = 0;
    for (Integer src : source) {
      HashMap<Integer, Integer> dist = new HashMap<>();
      for (Integer node : G.keySet()) { dist.put(node, Integer.MAX_VALUE);}
      dist.put(src, 0);
      LinkedList<Integer> queue = new LinkedList<>();
      queue.add(src);

      while (!queue.isEmpty()) {
        int u = queue.poll();
        for (Integer node : G.get(u)) {
          if (dist.get(node) == Integer.MAX_VALUE) {
            queue.add(node);
            dist.put(node, dist.get(u) + 1);
          }
        }
      }
      int max = 0;
      for (Integer node : dist.keySet()) {
        if (dist.get(node) > max && dist.get(node) != Integer.MAX_VALUE) {
          max = dist.get(node);
        }
      }
      if (max > pathLength) {pathLength = max;}
    }
    return pathLength;
  }
}
