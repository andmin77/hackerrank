package dataStructures.advanced.networkAdministration;

import java.util.*;

public class SolutionFaster {
    private static final int MAXADMIN = 100;
    private static final int MAXSERVER = 100000;
    private static final int MAXLINK = 500000;
            
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
    
    private static class PairIntInt implements Comparable<PairIntInt>  {
        int first;
        int second;
        
        int value;
        Node node;
        
        public PairIntInt(int first, int second) {
            this.first = first;
            this.second = second;
        }
        
        public PairIntInt(int first, int second, int value, Node node) {
            this.first = first;
            this.second = second;
            this.value = value;
            this.node = node;
        }
        
        @Override
        public int compareTo(PairIntInt o) {
            if ( this.first > o.first ) return 1;
            if ( this.first < o.first ) return -1;
            if ( this.second > o.second ) return 1;
            if ( this.second < o.second ) return -1;
            return 0;
        }

    }
    
    private static void cleanReverse(Node px) {
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

    private static void fixSizeAndSum(Node px) {
        int lsize = px.lft != null ? px.lft.size : 0;
        int rsize = px.rgt != null ? px.rgt.size : 0;
        px.size = lsize + rsize + 1;

        int lsum = px.lft != null ? px.lft.sum : 0;
        int rsum = px.rgt != null ? px.rgt.sum : 0;
        px.sum = lsum + rsum + px.fx;
    }

    private static void zig(Node p) {
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
    
    private static void zag(Node p) {
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
    
    private static void splay(Node px) {
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
    
    private static void makeLast(Node x) {
        splay(x);
        if (x.rgt != null) 
            x.rev ^= true;
    }
    
    private static void makeFirst(Node x) {
        splay(x);
        if (x.lft != null) 
            x.rev ^= true;
    }
    
    private static void addEdge(int a, int b, int i, Node px) {
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

    private static void delEdge(int a, int b, int x, Node px) {
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
    
    private static boolean connected(int a, int b, int x) {
        Node pa = table[a][x].get();
        Node pb = table[b][x].get();
        if (pa != pb) {
            splay(pa);
            splay(pb);
            return pa.prx != null;
        }
        return true;
    }
    
    private static int xrank(Node px) {
        splay(px);
        return px.lft != null ? px.lft.size : 0;
    }

    private static Lnk[][] table;
    private static PairIntInt[] ehash;
    
    private static Node[] frx;

    public static void main(String[] args) {
        
        table = new Lnk[MAXSERVER][MAXADMIN];
        for ( int i = 0; i < MAXSERVER; i++ ) {
            for ( int j = 0; j < MAXADMIN; j++ ) {
                table[i][j] = new Lnk();
            }
        }

        frx = new Node[MAXLINK];
        
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int C = scanner.nextInt();
        int T = scanner.nextInt();
        
        for ( int i = 0; i < M; ++i )
            frx[i] = new Node();
        
        ehash = new PairIntInt[M];
        for ( int i = 0; i < M; ++i ) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int j = scanner.nextInt();
            --a; 
            --b;
            --j;

            Node ptx = frx[i];
            ehash[i] = new PairIntInt(a, b, j + 1, ptx);
            addEdge(a, b, j, ptx);            
        }
        Arrays.sort(ehash);        
        PairIntInt key;
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
                
                key = new PairIntInt(a, b);
                int index = Arrays.binarySearch(ehash, key);
                
                x = index > -1 ? ehash[index].value : 0;
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
                if (table[a][i].count() > 0 && table[b][i].count() > 0 && connected(a, b, i)) { //it is possible to get from one of them to the other
                    System.out.println("Network redundancy");
                    continue;
                }
                px = ehash[index].node; //get the proper node (i.e. the node for edge  a <-> b)

                delEdge(a, b, x, px);

                addEdge(a, b, i, px);

                ehash[index].value = i + 1; //update the company owning this cable

                System.out.println("Assignment done");
            } else if (cmd == 2) {
                key = new PairIntInt(a, b);
                int index = Arrays.binarySearch(ehash, key);

                px = ehash[index].node;

                splay(px);

                px.fx = i;

                fixSizeAndSum(px);
            } else {
                --i;
                if (a > b) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
                key = new PairIntInt(a, b);
                int index = Arrays.binarySearch(ehash, key);
                
                if ( index > -1 && ehash[index].node != null && ehash[index].value == i + 1 ) {
                    System.out.println( ehash[index].node.fx + " security devices placed");
                    continue;
                }
                Lnk al = table[a][i], bl = table[b][i];
                if (al.count() > 0 && bl.count() > 0 && connected(a, b, i)) {
                    if (al.count() == 1 && bl.count() == 1) { //happy case :)
                        px = al.get();
                        makeFirst(px);
                        x = px.sum;
                    } else if (al.count() + bl.count() == 3) { //one is extreme, we need to find the next one
                        if (al.count() > 1) {
                            Lnk tmp = al;
                            al = bl;
                            bl = tmp;
                        }

                        px = al.get();
                        makeFirst(px);
                        x = px.sum;

                        qx = bl.a;
                        tx = bl.b;
                        
                        if (xrank(qx) > xrank(tx)) {
                            Node tmp = qx;
                            qx = tx;
                            tx = tmp;
                        }

                        splay(qx);
                        x -= qx.rgt.sum;
                    } else { //hard case, invoking creativity :)
                        px = al.a;
                        qx = al.b;
                        tx = bl.a;
                        ux = bl.b;
                        rpx = xrank(px);
                        rqx = xrank(qx);
                        rtx = xrank(tx);
                        rux = xrank(ux);
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

                        splay(qx);
                        x = qx.fx + qx.rgt.sum;

                        splay(tx);
                        x -= tx.rgt.sum;
                    }
                    System.out.println(x + " security devices placed");
                } else {
                    System.out.println("No connection");
                }
            }
        }
    }
}