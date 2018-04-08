package dataStructures.advanced.networkAdministration;

import java.util.*;

public class SolutionDebug {
    
    //private static final int MAXSERVER = 8000;
    private static final int MAXADMIN = 100;
   // private static final int MAXLINK = 100000;
    
    private static final int MAXSERVER = 100000;
    private static final int MAXLINK = 500000;
            
    private static class Pair<A, B> {

        public A first;
        public B second;

        private Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public static <A, B> Pair<A, B> of(A first, B second) {
            return new Pair<A, B>(first, second);
        }
        
        @Override
        public int hashCode() {
            return 31 * hashcode(first) + hashcode(second);
        }

        // TODO : move this to a helper class.
        private static int hashcode(Object o) {
            return o == null ? 0 : o.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Pair))
                return false;
            if (this == obj)
                return true;
            return equal(first, ((Pair<?, ?>) obj).first)
                    && equal(second, ((Pair<?, ?>) obj).second);
        }

        // TODO : move this to a helper class.
        private boolean equal(Object o1, Object o2) {
            return o1 == o2 || (o1 != null && o1.equals(o2));
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ')';
        }        
    }
    
    private static class Node {
        Node prx;
        Node lft;
        Node rgt;
        boolean rev;
        int sum;
        int fx;
        int size;

        public Node() {
            this.clear();
        }

        void clear() {
            lft = rgt = prx = null;
            rev = false;
            sum = fx = 0;
            size = 1;
        }

        boolean isRoot() {
            return ( (prx == null ) || ( prx.lft != this && prx.rgt != this ) );
        }
    };
    
    private static class Lnk {
        Node a;
        Node b;
        
        public Lnk() {
            this.clear();
        }
        
        Node get() {
            if ( a != null ) 
                return a;
            else if ( b != null ) 
                return b;
            return null;
        }
        
        Node getDiff(Node x) {
            if ( a == x ) 
                return b;
            else if ( b == x ) 
                return a;
            return get();
        }
        
        void add(Node x) {
            if ( a == null ) 
                a = x;
            else if ( b == null ) 
                b = x;
        }

        void del(Node x) {
            if ( a == x ) 
                a = null;
            else if ( b == x ) 
                b = null;
        }

        void clear() {
            a = b = null;
        }

        int count() {
            return ( ((a != null) ? 1 : 0) + ((b != null) ? 1 : 0) );
        }
    };        
    
    private static interface NodeOperation {
        public void splay(Node px);

        public void makeLast(Node x);

        public void makeFirst(Node x);

        public void addEdge(int a, int b, int i, Node px);

        public void delEdge(int a, int b, int x, Node px);

        public boolean connected(int a, int b, int x);

        public int xrank(Node px);
        
        public void fixSizeAndSum(Node px);
        
        public void cleanReverse(Node px);
        
        public Pair<Integer, Node> search(Pair<Integer, Integer> key);
    }
    
    
    
    private static class NetAdminDebuggingNodeOperation implements NodeOperation { 
        
        private Map<String, Long> sumTimeMapping;
        private Map<String, Long> countMapping;
        private NodeOperation nodeOperation;
        private HashMap<Pair<Integer, Integer>, Pair<Integer, Node>> ehash;
        
        public NetAdminDebuggingNodeOperation(NodeOperation nodeOperation, HashMap<Pair<Integer, Integer>, Pair<Integer, Node>> ehash) {
            this.nodeOperation = nodeOperation;
            this.ehash = ehash;
            sumTimeMapping = new HashMap<>();  
            countMapping = new HashMap<>(); 
        }
        
        public void printReport() {
            System.err.println( "-------------------------------------------------------------------------------------" );
            System.err.println( "| Function           |      Total Time    |      Count         |      Tot (sec)     |" );
            System.err.println( "-------------------------------------------------------------------------------------" );
            for ( String functionName : this.sumTimeMapping.keySet() ) {
                long totalTime = this.sumTimeMapping.get(functionName);
                long count = this.countMapping.get(functionName);
                long tot = (long) ((double) totalTime / (double) count / Math.pow(10, 9));
                System.err.println( String.format( "|%1$-20s|%2$-20s|%3$-20s|%4$-20s|", functionName, "" + totalTime, "" + count, "" + tot) );
            }
            System.err.println( "-------------------------------------------------------------------------------------" );
        }                

        private void update(String key, long timeStart) {
            long timeStop = System.nanoTime();            
            long delay = timeStop - timeStart;
            if ( countMapping.get(key) == null ) {
                countMapping.put(key, 0L);
            }
            long count = countMapping.get(key) + 1;
            countMapping.put(key, count);
            
            if ( sumTimeMapping.get(key) == null ) {
                sumTimeMapping.put(key, delay);
            } 
            long partial = sumTimeMapping.get(key) + delay;
            sumTimeMapping.put(key, partial);
        }
        @Override
        public void splay(Node px) {
            long t1 = System.nanoTime();
            nodeOperation.splay(px);            
            update("splay", t1);            
        }

        @Override
        public void makeLast(Node x) {
            long t1 = System.nanoTime();
            nodeOperation.makeLast(x);
            update("makeLast", t1); 
        }

        @Override
        public void makeFirst(Node x) {
            long t1 = System.nanoTime();
            nodeOperation.makeFirst(x);
            update("makeFirst", t1); 
        }

        @Override
        public void addEdge(int a, int b, int i, Node px) {
            long t1 = System.nanoTime();
            nodeOperation.addEdge(a, b, i, px); 
            update("addEdge", t1);
        }

        @Override
        public void delEdge(int a, int b, int x, Node px) {
            long t1 = System.nanoTime();
            nodeOperation.delEdge(a, b, x, px);
            update("delEdge", t1);            
        }

        @Override
        public boolean connected(int a, int b, int x) {
            long t1 = System.nanoTime();
            boolean result = nodeOperation.connected(a, b, x);
            update("connected", t1); 
            return result;
        }

        @Override
        public int xrank(Node px) {
            long t1 = System.nanoTime();
            int result = nodeOperation.xrank(px);
            update("xrank", t1); 
            return result;
        }

        @Override
        public void fixSizeAndSum(Node px) {
            long t1 = System.nanoTime();
            nodeOperation.fixSizeAndSum(px);
            update("fixSizeAndSum", t1); 
        }

        @Override
        public void cleanReverse(Node px) {
            long t1 = System.nanoTime();
            nodeOperation.cleanReverse(px);
            update("cleanReverse", t1);
        }

        @Override
        public Pair<Integer, Node> search(Pair<Integer, Integer> key) {
            long t1 = System.nanoTime();
            Pair<Integer, Node> result = nodeOperation.search(key);            
            update("search", t1);
            return result;
        }
        
    }
    
    private static class NetAdminNodeOperation implements NodeOperation {            
        public void cleanReverse(Node px) {
            if (px.rev) {
                px.rev = false;
                Node swap = px.lft;
                px.lft = px.rgt;
                px.rgt = swap;
                if (px.lft != null)
                    px.lft.rev ^= true;
                if (px.rgt != null)
                    px.rgt.rev ^= true;
            }
        }

        public void fixSizeAndSum(Node px) {
            int lsize = px.lft != null ? px.lft.size : 0;
            int rsize = px.rgt != null ? px.rgt.size : 0;
            px.size = lsize + rsize + 1;

            int lsum = px.lft != null ? px.lft.sum : 0;
            int rsum = px.rgt != null ? px.rgt.sum : 0;
            px.sum = lsum + rsum + px.fx;
        }

        public void zig(Node p) {
            Node q = p.prx;
            Node r = q.prx;
            q.lft = p.rgt;
            if ( q.lft != null) 
                q.lft.prx = q;
            p.rgt = q;
            q.prx = p;
            p.prx = r;
            if (p.prx != null && r != null) {
                if (r.lft == q) r.lft = p;
                if (r.rgt == q) r.rgt = p;
            }
        }

        public void zag(Node p) {
            Node q = p.prx;
            Node r = q.prx;
            q.rgt = p.lft;                
            if ( q.rgt != null ) {
                q.rgt.prx = q;
            }
            p.lft = q;
            q.prx = p;
            p.prx = r;
            if ( p.prx != null && r != null) {
                if (r.lft == q) r.lft = p;
                if (r.rgt == q) r.rgt = p;
            }
        }    

        public void splay(Node px) {
            Node f;
            Node gf;
            while (!px.isRoot()) {
                f = px.prx;
                gf = f.prx;
                if (gf == null) {
                    cleanReverse(f);
                    cleanReverse(px);
                    if (f.lft == px) 
                        zig(px);
                    else 
                        zag(px);
                    fixSizeAndSum(f);
                    fixSizeAndSum(px);
                } else {
                    cleanReverse(gf);
                    cleanReverse(f);
                    cleanReverse(px);
                    if (gf.lft == f) {
                        if (f.lft == px) {
                            zig(f);
                            zig(px);
                        } else {
                            zag(px);
                            zig(px);
                        }
                    } else if (f.lft == px) {
                        zig(px);
                        zag(px);
                    } else {
                        zag(f);
                        zag(px);
                    }
                    fixSizeAndSum(gf);
                    fixSizeAndSum(f);
                    fixSizeAndSum(px);
                }
            }
            cleanReverse(px);
        }

        public void makeLast(Node x) {
            splay(x);
            if (x.rgt != null) 
                x.rev ^= true;
        }

        public void makeFirst(Node x) {
            splay(x);
            if (x.lft != null) 
                x.rev ^= true;
        }

        public void addEdge(int a, int b, int i, Node px) {
            Node l;
            Node r;

            l = table[a][i].get();
            if (l != null) 
                makeLast(l);

            r = table[b][i].get();       
            if (r != null) 
                makeFirst(r);

            px.lft = l;
            if (l != null) 
                l.prx = px;

            px.rgt = r;
            if (r != null) 
                r.prx = px;

            px.rev = false;
            px.prx = null;

            fixSizeAndSum(px);

            table[a][i].add(px);
            table[b][i].add(px);
        }

        public void delEdge(int a, int b, int x, Node px) {
            splay(px);
            if (px.lft != null) px.lft.prx = null;
            if (px.rgt != null) px.rgt.prx = null;
            table[a][x].del(px);
            table[b][x].del(px);
            px.lft = null;
            px.rgt = null;
            px.prx = null;
            fixSizeAndSum(px);
        }

        public boolean connected(int a, int b, int x) {
            Node pa = table[a][x].get();
            Node pb = table[b][x].get();
            if (pa != pb) {
                splay(pa);
                splay(pb);
                return pa.prx != null;
            }
            return true;
        }

        public int xrank(Node px) {
            splay(px);
            return px.lft != null ? px.lft.size : 0;
        }

        @Override
        public Pair<Integer, Node> search(Pair<Integer, Integer> key) {
            return ehash.get(key);
        }
    }

    private static Lnk[][] table;
    private static HashMap<Pair<Integer, Integer>, Pair<Integer, Node>> ehash;
    private static Node[] frx;

    public static void main(String[] args) {
        table = new Lnk[MAXSERVER][MAXADMIN];
        for ( int i = 0; i < MAXSERVER; i++ ) {
            for ( int j = 0; j < MAXADMIN; j++ ) {
                table[i][j] = new Lnk();
            }
        }
        ehash = new HashMap<>();
        frx = new Node[MAXLINK];
        
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int C = scanner.nextInt();
        int T = scanner.nextInt();
        
        for ( int i = 0; i < M; ++i )
            frx[i] = new Node();
        ehash.clear();
        
        NodeOperation nodeOperation = new NetAdminDebuggingNodeOperation( new NetAdminNodeOperation(), ehash );
        Pair<Integer, Integer> key;
        for ( int i = 0; i < M; ++i ) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int j = scanner.nextInt();
            --a; 
            --b;
            --j;
            key = new Pair(a, b);
            Node ptx = frx[i];
            ehash.put(key, new Pair(j + 1, ptx));
            nodeOperation.addEdge(a, b, j, ptx);            
        }
        Node px, qx, tx, ux;
        int rpx, rqx, rtx, rux, x;
        for ( int tCount = 0; tCount < T; tCount ++ ) {
            int cmd = scanner.nextInt();
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int i = scanner.nextInt();
            --a;
            --b;
            if (cmd == 1) {
                --i;
                key = new Pair(a, b);
                Pair<Integer, Node> data = nodeOperation.search(key);
                
                x = data != null ? data.first: 0;
                if (x == 0) { //not found in edges storage
                    System.out.println("Wrong link");
                    continue;
                }
                --x;
                if (x == i) { //this cable is already controlled by this admin
                    System.out.println("Already controlled link");
                    continue;
                }
                if (table[a][i].count() == 2 || table[b][i].count() == 2) { //at least one of the serves has 2 cables related the new admin
                    System.out.println("Server overload");
                    continue;
                }
                if (table[a][i].count() > 0 && table[b][i].count() > 0 && nodeOperation.connected(a, b, i)) { //it is possible to get from one of them to the other
                    System.out.println("Network redundancy");
                    continue;
                }
                px = data.second; //get the proper node (i.e. the node for edge  a <-> b)

                nodeOperation.delEdge(a, b, x, px);

                nodeOperation.addEdge(a, b, i, px);

                data.first = i + 1;

                System.out.println("Assignment done");
            } else if (cmd == 2) {
                key = new Pair(a, b);

                Pair<Integer, Node> data = nodeOperation.search(key);

                px = data.second;

                nodeOperation.splay(px);

                px.fx = i;

                nodeOperation.fixSizeAndSum(px);
            } else {
                --i;
                if (a > b) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
                key = new Pair(a, b);
                Pair<Integer, Node> data = nodeOperation.search(key);
                
                if (data != null && data.second != null && data.first == i + 1) {
                    System.out.println( data.second.fx + " security devices placed");
                    continue;
                }
                Lnk al = table[a][i], bl = table[b][i];
                if (al.count() > 0 && bl.count() > 0 && nodeOperation.connected(a, b, i)) {
                    if (al.count() == 1 && bl.count() == 1) { //happy case :)
                        px = al.get();
                        nodeOperation.makeFirst(px);
                        x = px.sum;
                    } else if (al.count() + bl.count() == 3) { //one is extreme, we need to find the next one
                        if (al.count() > 1) {
                            Lnk tmp = al;
                            al = bl;
                            bl = tmp;
                        }

                        px = al.get();
                        nodeOperation.makeFirst(px);
                        x = px.sum;

                        qx = bl.a;
                        tx = bl.b;
                        
                        if ( nodeOperation.xrank(qx) > nodeOperation.xrank(tx)) {
                            Node tmp = qx;
                            qx = tx;
                            tx = tmp;
                        }

                        nodeOperation.splay(qx);
                        x -= qx.rgt.sum;
                    } else { //hard case, invoking creativity :)
                        px = al.a;
                        qx = al.b;
                        tx = bl.a;
                        ux = bl.b;
                        rpx = nodeOperation.xrank(px);
                        rqx = nodeOperation.xrank(qx);
                        rtx = nodeOperation.xrank(tx);
                        rux = nodeOperation.xrank(ux);
                        if (rpx > rqx) {
                            Node tmp = px;
                            px = qx;
                            qx = tmp;
                            
                            int tmp2 = rpx;
                            rpx = rqx;
                            rqx = tmp2;
                        }
                        if (rtx > rux) {
                            Node tmp = tx;
                            tx = ux;
                            ux = tmp;
                            
                            int tmp2 = rtx;
                            rtx = rux;
                            rux = tmp2;
                        }

                        if (rpx > rtx) {
                            Node tmp = px;
                            px = tx;
                            tx = tmp;
                            
                            int tmp2 = rpx;
                            rpx = rtx;
                            rtx = tmp2;
                            
                            Node tmp3 = qx;
                            qx = ux;
                            ux = tmp3;
                            
                            int tmp4 = rqx;
                            rqx = rux;
                            rux = tmp4;
                        }
                        /**
                         * At this point:
                         * a is to the left and b to the right
                         * px is the lowest a's edge and qx is the highest a's edge
                         * tx is the lowest b's edge and ux is the highest b's edge
                         * */

                        nodeOperation.splay(qx);
                        x = qx.fx + qx.rgt.sum;

                        nodeOperation.splay(tx);
                        x -= tx.rgt.sum;
                    }
                    System.out.println(x + " security devices placed");
                } else {
                    System.out.println("No connection");
                }
            }
        }
        ((NetAdminDebuggingNodeOperation) nodeOperation).printReport();
    }
}