package dataStructures.advanced.networkAdministration;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class HR_net_admin {

    static class Node {
        Node l = null, r = null, p = null;
        boolean reversed = false;
        int value = 0, sum = 0;
        int u, v, admin;

        Node(int u, int v, int admin) {
            this.u = u;
            this.v = v;
            this.admin = admin;
        }
    }

    static int sum(Node n) {
        return n == null ? 0 : n.sum;
    }

    static void fixUp(Node n) {
        n.sum = n.value + sum(n.l) + sum(n.r);
    }

    static void fixDown(Node n) {
        if (n.reversed) {
            Node tmp = n.l;
            n.l = n.r;
            n.r = tmp;
            int ti = n.u;
            n.u = n.v;
            n.v = ti;
            if (n.l != null) {
                n.l.reversed ^= true;
            }
            if (n.r != null) {
                n.r.reversed ^= true;
            }
            n.reversed = false;
        }
    }

    static void rotate(Node x) {
        Node y = x.p;
        x.p = y.p;
        if (y.p != null) {
            if (y == y.p.l) {
                y.p.l = x;
            } else {
                y.p.r = x;
            }
        }
        y.p = x;
        if (x == y.l) {
            y.l = x.r;
            if (y.l != null) {
                y.l.p = y;
            }
            x.r = y;
        } else {
            y.r = x.l;
            if (y.r != null) {
                y.r.p = y;
            }
            x.l = y;
        }
        fixUp(y);
        fixUp(x);
    }

    static Node splay(Node x) {
        while (x.p != null) {
            if (x.p.p != null) {
                fixDown(x.p.p);
            }
            fixDown(x.p);
            fixDown(x);
            if (x.p.p == null) {
                rotate(x);
            } else {
                if (x == x.p.l && x.p == x.p.p.l || x == x.p.r && x.p == x.p.p.r) {
                    rotate(x.p);
                    rotate(x);
                } else {
                    rotate(x);
                    rotate(x);
                }
            }
        }
        return x;
    }

    static Node join(Node l, Node x, Node r) {
        x.reversed = false;
        x.l = l;
        x.r = r;
        if (l != null) {
            l.p = x;
        }
        if (r != null) {
            r.p = x;
        }
        fixUp(x);
        return x;
    }

    static Node q, r;

    static void split(Node x) {
        splay(x);
        fixDown(x);
        if (x.l != null) {
            x.l.p = null;
        }
        if (x.r != null) {
            x.r.p = null;
        }
        x.p = null;
        q = x.l;
        r = x.r;
        x.l = x.r = null;
        fixUp(x);
    }

    static Node min(Node n) {
        fixDown(n);
        while (n.l != null) {
            n = n.l;
            fixDown(n);
        }
        splay(n);
        return n;
    }

    static Node max(Node n) {
        fixDown(n);
        while (n.r != null) {
            n = n.r;
            fixDown(n);
        }
        splay(n);
        return n;
    }

    public static void solve(Input in, PrintWriter out) throws IOException {
        int n = in.nextInt(); // servers
        int m = in.nextInt(); // links
        int k = in.nextInt(); // admins
        int q = in.nextInt(); // queries
        HashMap<Integer, Node>[] edges = new HashMap[n];
        HashMap<Integer, Node[]>[] admins = new HashMap[n];
        for (int i = 0; i < n; ++i) {
            edges[i] = new HashMap<>();
            admins[i] = new HashMap<>();
        }
        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int a = in.nextInt() - 1;
            Node e = new Node(u, v, a);
            edges[u].put(v, e);
            edges[v].put(u, e);
            insert(e, admins);
        }
        for (int i = 0; i < q; ++i) {
            int t = in.nextInt();
            if (t == 1) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int a = in.nextInt() - 1;
                if (!edges[u].containsKey(v)) {
                    out.println("Wrong link");
                    continue;
                }
                Node e = edges[u].get(v);
                if (e.admin == a) {
                    out.println("Already controlled link");
                    continue;
                }
                Node l = null, r = null;
                if (admins[u].containsKey(a)) {
                    Node[] ar = admins[u].get(a);
                    if (ar.length > 1) {
                        out.println("Server overload");
                        continue;
                    }
                    if (ar.length == 1) {
                        l = ar[0];
                    }
                }
                if (admins[v].containsKey(a)) {
                    Node[] ar = admins[v].get(a);
                    if (ar.length > 1) {
                        out.println("Server overload");
                        continue;
                    }
                    if (ar.length == 1) {
                        r = ar[0];
                    }
                }
                if (l != null && r != null && min(splay(l)) == min(splay(r))) {
                    out.println("Network redundancy");
                    continue;
                }
                admins[u].put(e.admin, remove(admins[u].get(e.admin), e));
                admins[v].put(e.admin, remove(admins[v].get(e.admin), e));
                split(e);
                e.admin = a;
                insert(e, admins);
                out.println("Assignment done");
            } else if (t == 2) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int x = in.nextInt();
                if (!edges[u].containsKey(v)) {
                    throw new AssertionError();
                }
                Node e = edges[u].get(v);
                splay(e);
                e.value = x;
                fixUp(e);
            } else if (t == 3) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int a = in.nextInt() - 1;
                Node l = any(u, a, admins);
                Node r = any(v, a, admins);
                if (l == null || r == null || min(splay(l)) != min(splay(r))) {
                    out.println("No connection");
                    continue;
                }
                int prefixU = prefix(u, a, l);
                int prefixV = prefix(v, a, r);
                out.println(Math.abs(prefixU - prefixV) + " security devices placed");
            }
        }
    }

    private static int prefix(int u, int a, Node e) {
        splay(e);
        fixDown(e);
        if (u == e.u) {
            return sum(e.l);
        } else if (u == e.v) {
            return sum(e.l) + e.value;
        } else {
            throw new AssertionError();
        }
    }

    private static Node any(int u, int a, HashMap<Integer, Node[]>[] admins) {
        Node[] ar = admins[u].get(a);
        return ar == null || ar.length == 0 ? null : ar[0];
    }

    private static Node insert(Node e, HashMap<Integer, Node[]>[] admins) {
        int u = e.u;
        int v = e.v;
        fixDown(e);
        int a = e.admin;
        Node l = null, r = null;
        if (admins[u].containsKey(a)) {
            Node[] ar = admins[u].get(a);
            if (ar.length > 1) {
                throw new AssertionError();
            }
            if (ar.length == 1) {
                l = ar[0];
            }
        }
        if (admins[v].containsKey(a)) {
            Node[] ar = admins[v].get(a);
            if (ar.length > 1) {
                throw new AssertionError();
            }
            if (ar.length == 1) {
                r = ar[0];
            }
        }
        if (l != null && r != null && min(splay(l)) == min(splay(r))) {
            throw new AssertionError();
        }
        if (l != null && max(splay(l)).v != u) {
            splay(l).reversed ^= true;
            if (max(splay(l)).v != u) {
                throw new AssertionError();
            }
        }
        if (r != null && min(splay(r)).u != v) {
            splay(r).reversed ^= true;
            if (min(splay(r)).u != v) {
                throw new AssertionError();
            }
        }
        admins[u].put(a, append(admins[u].get(a), e));
        admins[v].put(a, append(admins[v].get(a), e));
        return join(l, e, r);
    }

    private static Node[] append(Node[] ar, Node e) {
        if (ar == null) {
            return new Node[] {e};
        }
        ar = Arrays.copyOf(ar, ar.length + 1);
        ar[ar.length - 1] = e;
        return ar;
    }

    private static Node[] remove(Node[] ar, Node e) {
        if (ar == null) {
            throw new AssertionError();
        }
        Node[] ar1 = new Node[ar.length - 1];
        int len = 0;
        for (int i = 0; i < ar.length; ++i) {
            if (ar[i] != e) {
                ar1[len++] = ar[i];
            }
        }
        if (len != ar1.length) {
            throw new AssertionError();
        }
        return ar1;
    }

    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(System.out);
//        Random rnd = new Random(5234);
//        loop: while (rnd != null) {
//            StringBuilder sb = new StringBuilder();
//            int n = rnd.nextInt(100) + 1;
//            int m = rnd.nextInt(100) + 1;
//            if (n * (n - 1) / 2 < m) {
//                continue;
//            }
//            int a = rnd.nextInt(100) + 1;
//            int q = rnd.nextInt(100) + 1;
//            sb.append(n + " " + m + " " + a + " " + q + "\n");
//            int[][] g = new int[n][n];
//            for (int[] ar : g) {
//                Arrays.fill(ar, -1);
//            }
//            for (int i = 0; i < m; ++i) {
//                int u, v;
//                do {
//                    u = rnd.nextInt(n);
//                    v = rnd.nextInt(n);
//                } while (u >= v || g[u][v] != -1);
//                int c = rnd.nextInt(a);
//                g[u][v] = c;
//                sb.append((u + 1) + " " + (v + 1) + " " + (c + 1) + "\n");
//            }
//            for (int c = 0; c < a; ++c) {
//                boolean[] col = new boolean[n];
//                for (int i = 0; i < n; ++i) {
//                    if (!col[i]) {
//                        if (!cdfs(i, -1, g, col, c)) {
//                            continue loop;
//                        }
//                    }
//                }
//            }
//            for (int i = 0; i < q; ++i) {
//                int t = rnd.nextInt(3) + 1;
//                int u, v;
//                do {
//                    u = rnd.nextInt(n);
//                    v = rnd.nextInt(n);
//                } while (u >= v);
//                if (t == 1 || t == 3) {
//                    sb.append(t + " " + (u + 1) + " " + (v + 1) + " " + (rnd.nextInt(a) + 1) + "\n");
//                } else if (t == 2) {
//                    do {
//                        u = rnd.nextInt(n);
//                        v = rnd.nextInt(n);
//                    } while (u >= v || g[u][v] == -1);
//                    sb.append(t + " " + (u + 1) + " " + (v + 1) + " " + (rnd.nextInt(1000) + 1) + "\n");
//                }
//            }
//            System.err.print('.');
//            StringWriter ans1 = new StringWriter();
//            solve(new Input(sb.toString()), new PrintWriter(ans1));
//            StringWriter ans2 = new StringWriter();
//            solveSlow(new Input(sb.toString()), new PrintWriter(ans2));
//            if (!ans1.toString().equals(ans2.toString())) {
//                System.err.println(sb);
//                System.err.println("------");
//                System.err.println(ans1);
//                System.err.println("------");
//                System.err.println(ans2);
//                System.err.println("------");
//                break;
//            }
//        }
        solve(new Input(new BufferedReader(new InputStreamReader(System.in))), out);
        out.close();
    }

    private static boolean cdfs(int i, int p, int[][] g, boolean[] col, int c) {
        int count = 0;
        col[i] = true;
        for (int j = 0; j < g.length; ++j) {
            if (g[i][j] != c || j == p) {
                continue;
            }
            if (col[j] || !cdfs(j, i, g, col, c)) {
                return false;
            }
            count++;
        }
        return count <= (p == -1 ? 2 : 1);
    }

    public static void solveSlow(Input in, PrintWriter out) throws IOException {
        int n = in.nextInt(); // servers
        int m = in.nextInt(); // links
        int k = in.nextInt(); // admins
        int q = in.nextInt(); // queries
        int[][] g = new int[n][n];
        for (int[] ar : g) {
            Arrays.fill(ar, -1);
        }
        int[][] c = new int[n][n];
        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int a = in.nextInt() - 1;
            g[u][v] = g[v][u] = a;
        }
        for (int it = 0; it < q; ++it) {
            int t = in.nextInt();
            if (t == 1) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int a = in.nextInt() - 1;
                if (g[u][v] == -1) {
                    out.println("Wrong link");
                    continue;
                }
                if (g[u][v] == a) {
                    out.println("Already controlled link");
                    continue;
                }
                int uc = 0, vc = 0;
                for (int i = 0; i < n; ++i) {
                    if (g[u][i] == a) {
                        uc++;
                    }
                    if (g[v][i] == a) {
                        vc++;
                    }
                }
                if (uc == 2 || vc == 2) {
                    out.println("Server overload");
                    continue;
                }
                int[] d = new int[n];
                Arrays.fill(d, -1);
                dfs(u, 0, a, g, c, d);
                if (d[v] != -1) {
                    out.println("Network redundancy");
                    continue;
                }
                g[u][v] = g[v][u] = a;
                out.println("Assignment done");
            } else if (t == 2) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int x = in.nextInt();
                c[u][v] = c[v][u] = x;
            } else if (t == 3) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int a = in.nextInt() - 1;
                int[] d = new int[n];
                Arrays.fill(d, -1);
                dfs(u, 0, a, g, c, d);
                if (d[v] == -1) {
                    out.println("No connection");
                    continue;
                }
                out.println(d[v] + " security devices placed");
            }
        }
    }

    private static void dfs(int i, int dist, int a, int[][] g, int[][] c, int[] d) {
        if (d[i] != -1) {
            return;
        }
        d[i] = dist;
        for (int j = 0; j < g.length; ++j) {
            if (g[i][j] == a) {
                dfs(j, dist + c[i][j], a, g, c, d);
            }
        }
    }

    static class Input {
        BufferedReader in;
        StringBuilder sb = new StringBuilder();

        public Input(BufferedReader in) {
            this.in = in;
        }

        public Input(String s) {
            this.in = new BufferedReader(new StringReader(s));
        }

        public String next() throws IOException {
            sb.setLength(0);
            while (true) {
                int c = in.read();
                if (c == -1) {
                    return null;
                }
                if (" \n\r\t".indexOf(c) == -1) {
                    sb.append((char)c);
                    break;
                }
            }
            while (true) {
                int c = in.read();
                if (c == -1 || " \n\r\t".indexOf(c) != -1) {
                    break;
                }
                sb.append((char)c);
            }
            return sb.toString();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        public double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }
    }
}
