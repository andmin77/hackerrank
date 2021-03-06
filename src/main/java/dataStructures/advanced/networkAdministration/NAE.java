package dataStructures.advanced.networkAdministration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Random;

public class NAE {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";
	
	static void solve()
	{
		int n = ni(), m = ni(), na = ni(), Q = ni();
		int[] from = new int[m];
		int[] to = new int[m];
		int[] w = new int[m];
		for(int i = 0;i < m;i++){
			int f = ni()-1, t = ni()-1, a = ni()-1;
			from[i] = f; to[i] = t; w[i] = a;
		}
		
		int[][] ug = packU(n, from, to);
		for(int[] row : ug){
			Arrays.sort(row);
		}
		int[][][] g = packWU(n, from, to, w);
		for(int[][] row : g){
			Arrays.sort(row, new Comparator<int[]>() {
				public int compare(int[] a, int[] b) {
					return a[0] - b[0];
				}
			});
		}
		
		Node[][] nodes = new Node[n][];
		for(int i = 0;i < n;i++){
			nodes[i] = new Node[g[i].length];
			for(int j = 0;j < g[i].length;j++){
				nodes[i][j] = new Node(i, g[i][j][0], 0);
				if(i > g[i][j][0]){
					int rind = Arrays.binarySearch(ug[g[i][j][0]], i);
					nodes[i][j].reverse = nodes[g[i][j][0]][rind];
					nodes[g[i][j][0]][rind].reverse = nodes[i][j];
				}
			}
		}
		
		for(int i = 0;i < n;i++){
			Node[] ks = new Node[na];
			for(int j = 0;j < g[i].length;j++){
				if(ks[g[i][j][1]] == null){
					ks[g[i][j][1]] = nodes[i][j];
				}else{
					merge(root(ks[g[i][j][1]].reverse), root(nodes[i][j]));
					merge(root(nodes[i][j].reverse), root(ks[g[i][j][1]]));
				}
			}
		}
		
		gouter:
		for(int z = 0;z < Q;z++){
			int type = ni();
			if(type == 1){
				int f = ni()-1, t = ni()-1, K = ni()-1;
				int find = Arrays.binarySearch(ug[f], t);
				if(find < 0){
					out.println("Wrong link");
				}else{
					if(g[f][find][1] == K){
						out.println("Already controlled link");
					}else{
						Node fx = null, tx = null;
						{
							int deg = 0;
							for(int i = 0;i < g[f].length;i++)if(g[f][i][1] == K){fx = nodes[f][i]; deg++;}
							if(deg == 2){
								out.println("Server overload");
								continue gouter;
							}
						}
						{
							int deg = 0;
							for(int i = 0;i < g[t].length;i++)if(g[t][i][1] == K){tx = nodes[t][i]; deg++;}
							if(deg == 2){
								out.println("Server overload");
								continue gouter;
							}
						}
						if(fx != null && tx != null && (root(fx) == root(tx) || root(fx) == root(tx.reverse))){
							out.println("Network redundancy");
						}else{
							Node ft = nodes[f][find];
							int rind = Arrays.binarySearch(ug[t], f);
							splitEx(ft);
							splitEx(ft.reverse);
							
							g[f][find][1] = K;
							g[t][rind][1] = K;
							merge(root(fx != null ? fx.reverse : null), merge(root(ft), root(tx)));
							merge(root(tx != null ? tx.reverse : null), merge(root(ft.reverse), root(fx)));
							out.println("Assignment done");
						}
					}
				}
			}else if(type == 2){
				int a = ni()-1, b = ni()-1, x = ni();
				Node v = null;
				{
					Node u = nodes[a][Arrays.binarySearch(ug[a], b)];
					v = u.reverse;
					int plus = x - u.v;
					u.v += plus;
					while(u != null){
						u.sum += plus;
						u = u.parent;
					}
				}
				{
					int plus = x - v.v;
					v.v += plus;
					while(v != null){
						v.sum += plus;
						v = v.parent;
					}
				}
			}else if(type == 3){
				int a = ni()-1, b = ni()-1, K = ni()-1;
				if(a == b){
					out.println(0 + " security devices placed");
					continue;
				}
				for(int i = 0;i < g[a].length;i++){
					if(g[a][i][1] == K){
						Node u = nodes[a][i];
						for(int j = 0;j < g[b].length;j++){
							if(g[b][j][1] == K){
								Node v = nodes[b][j];
								if(root(u) == root(v.reverse)){
									int ret = 0;
									if(index(u) <= index(v.reverse)){
										ret = sum(root(u), index(u), index(v.reverse) + 1);
									}else{
										ret = sum(root(u), index(v.reverse)+1, index(u));
									}
									out.println(ret + " security devices placed");
									continue gouter;
								}else if(root(u) == root(v)){
									int ret = 0;
									if(index(u) <= index(v)){
										ret = sum(root(u), index(u), index(v));
									}else{
										ret = sum(root(u), index(v), index(u));
									}
									out.println(ret + " security devices placed");
									continue gouter;
								}
							}
						}
					}
				}
				out.println("No connection");
			}else{
				throw new RuntimeException();
			}
		}
	}
	
	static int[][] packU(int n, int[] from, int[] to) {
		int[][] g = new int[n][];
		int[] p = new int[n];
		for (int f : from)
			p[f]++;
		for (int t : to)
			p[t]++;
		for (int i = 0; i < n; i++)
			g[i] = new int[p[i]];
		for (int i = 0; i < from.length; i++) {
			g[from[i]][--p[from[i]]] = to[i];
			g[to[i]][--p[to[i]]] = from[i];
		}
		return g;
	}
	
	public static int[][][] packWU(int n, int[] from, int[] to, int[] w) {
		int[][][] g = new int[n][][];
		int[] p = new int[n];
		for (int f : from)
			p[f]++;
		for (int t : to)
			p[t]++;
		for (int i = 0; i < n; i++)
			g[i] = new int[p[i]][2];
		for (int i = 0; i < from.length; i++) {
			--p[from[i]];
			g[from[i]][p[from[i]]][0] = to[i];
			g[from[i]][p[from[i]]][1] = w[i];
			--p[to[i]];
			g[to[i]][p[to[i]]][0] = from[i];
			g[to[i]][p[to[i]]][1] = w[i];
		}
		return g;
	}
	
	public static Random gen = new Random(5);
	
	static public class Node
	{
		public int from, to;
		public int v; // value
		public long priority;
		public Node left, right, parent;
		
		public int count;
		
		public int sum; 
		public Node reverse;
		
		public Node(int from, int to, int v)
		{
			this.from = from; this.to = to;
			this.v = v;
			priority = gen.nextLong();
			update(this);
		}

		@Override
		public String toString() {
			return "Node [from=" + from + ", to=" + to + ", v=" + v
					+ ", par=" + (parent != null) + ", count=" + count + ", sum=" + sum + "]";
		}
	}

	public static Node update(Node a)
	{
		if(a == null)return null;
		a.count = 1;
		if(a.left != null)a.count += a.left.count;
		if(a.right != null)a.count += a.right.count;
		
		a.sum = a.v;
		if(a.left != null)a.sum += a.left.sum;
		if(a.right != null)a.sum += a.right.sum;
		return a;
	}
	
	public static Node disconnect(Node a)
	{
		if(a == null)return null;
		a.left = a.right = a.parent = null;
		return update(a);
	}
	
	public static Node root(Node x)
	{
		if(x == null)return null;
		while(x.parent != null)x = x.parent;
		return x;
	}
	
	public static int count(Node a)
	{
		return a == null ? 0 : a.count;
	}
	
	public static void setParent(Node a, Node par)
	{
		if(a != null)a.parent = par;
	}
	
	public static int sum(Node a, int L, int R)
	{
		if(a == null || L >= R || L >= count(a) || R <= 0)return 0;
		if(L <= 0 && R >= count(a)){
			return a.sum;
		}else{
			int lmin = sum(a.left, L, R);
			int rmin = sum(a.right, L-count(a.left)-1, R-count(a.left)-1);
			int sum = lmin + rmin;
			if(L <= count(a.left) && count(a.left) < R)sum += a.v;
			return sum;
		}
	}
	
	public static Node merge(Node a, Node b)
	{
		if(b == null)return a;
		if(a == null)return b;
		if(a.priority > b.priority){
			setParent(a.right, null);
			setParent(b, null);
			a.right = merge(a.right, b);
			setParent(a.right, a);
			return update(a);
		}else{
			setParent(a, null);
			setParent(b.left, null);
			b.left = merge(a, b.left);
			setParent(b.left, b);
			return update(b);
		}
	}
	
	public static Node[] splitEx(Node x)
	{
		if(x == null)return new Node[]{null, null};
		if(x.left != null)x.left.parent = null;
		if(x.right != null)x.right.parent = null;
		Node[] sp = new Node[]{x.left, x.right};
		x.left = null;
		x.right = null;
		update(x);
		while(x.parent != null){
			Node p = x.parent;
			x.parent = null;
			if(x == p.left){
				p.left = sp[1];
				if(sp[1] != null)sp[1].parent = p;
				sp[1] = p;
			}else{
				p.right = sp[0];
				if(sp[0] != null)sp[0].parent = p;
				sp[0] = p;
			}
			update(p);
			x = p;
		}
		return sp;
	}
	
	public static Node[] split(Node x)
	{
		if(x == null)return new Node[]{null, null};
		if(x.left != null)x.left.parent = null;
		Node[] sp = new Node[]{x.left, x};
		x.left = null;
		update(x);
		while(x.parent != null){
			Node p = x.parent;
			x.parent = null;
			if(x == p.left){
				p.left = sp[1];
				if(sp[1] != null)sp[1].parent = p;
				sp[1] = p;
			}else{
				p.right = sp[0];
				if(sp[0] != null)sp[0].parent = p;
				sp[0] = p;
			}
			update(p);
			x = p;
		}
		return sp;
	}
	
	// [0,K),[K,N)
	public static Node[] split(Node a, int K)
	{
		if(a == null)return new Node[]{null, null};
		if(K <= count(a.left)){
			setParent(a.left, null);
			Node[] s = split(a.left, K);
			a.left = s[1];
			setParent(a.left, a);
			s[1] = update(a);
			return s;
		}else{
			setParent(a.right, null);
			Node[] s = split(a.right, K-count(a.left)-1);
			a.right = s[0];
			setParent(a.right, a);
			s[0] = update(a);
			return s;
		}
	}
	
	public static Node insert(Node a, int K, Node b)
	{
		if(a == null)return b;
		if(b.priority < a.priority){
			if(K <= count(a.left)){
				a.left = insert(a.left, K, b);
				setParent(a.left, a);
			}else{
				a.right = insert(a.right, K-count(a.left)-1, b);
				setParent(a.right, a);
			}
			return update(a);
		}else{
			Node[] ch = split(a, K);
			b.left = ch[0]; b.right = ch[1];
			setParent(b.left, b);
			setParent(b.right, b);
			return update(b);
		}
	}
	
	// delete K-th
	public static Node erase(Node a, int K)
	{
		if(a == null)return null;
		if(K < count(a.left)){
			a.left = erase(a.left, K);
			setParent(a.left, a);
			return update(a);
		}else if(K == count(a.left)){
			setParent(a.left, null);
			setParent(a.right, null);
			Node aa = merge(a.left, a.right);
			disconnect(a);
			return aa;
		}else{
			a.right = erase(a.right, K-count(a.left)-1);
			setParent(a.right, a);
			return update(a);
		}
	}
	
	public static Node get(Node a, int K)
	{
		while(a != null){
			if(K < count(a.left)){
				a = a.left;
			}else if(K == count(a.left)){
				break;
			}else{
				K = K - count(a.left)-1;
				a = a.right;
			}
		}
		return a;
	}
	
	static Node[] Q = new Node[100];
	public static Node update(Node a, int K, int v)
	{
		int p = 0;
		while(a != null){
			Q[p++] = a;
			if(K < count(a.left)){
				a = a.left;
			}else if(K == count(a.left)){
				break;
			}else{
				K = K - count(a.left)-1;
				a = a.right;
			}
		}
		a.v = v;
		while(p > 0){
			update(Q[--p]);
		}
		return a;
	}
	
	public static int index(Node a)
	{
		if(a == null)return -1;
		int ind = count(a.left);
		while(a != null){
			Node par = a.parent;
			if(par != null && par.right == a){
				ind += count(par.left) + 1;
			}
			a = par;
		}
		return ind;
	}
	
	public static Node[] nodes(Node a) { return nodes(a, new Node[a.count], 0, a.count); }
	public static Node[] nodes(Node a, Node[] ns, int L, int R)
	{
		if(a == null)return ns;
		nodes(a.left, ns, L, L+count(a.left));
		ns[L+count(a.left)] = a;
		nodes(a.right, ns, R-count(a.right), R);
		return ns;
	}
	
	public static String toString(Node a, String indent)
	{
		if(a == null)return "";
		StringBuilder sb = new StringBuilder();
		sb.append(toString(a.left, indent + "  "));
		sb.append(indent).append(a).append("\n");
		sb.append(toString(a.right, indent + "  "));
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	private static boolean eof()
	{
		if(lenbuf == -1)return true;
		int lptr = ptrbuf;
		while(lptr < lenbuf)if(!isSpaceChar(inbuf[lptr++]))return false;
		
		try {
			is.mark(1000);
			while(true){
				int b = is.read();
				if(b == -1){
					is.reset();
					return true;
				}else if(!isSpaceChar(b)){
					is.reset();
					return false;
				}
			}
		} catch (IOException e) {
			return true;
		}
	}
	
	private static byte[] inbuf = new byte[1024];
	static int lenbuf = 0, ptrbuf = 0;
	
	private static int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	
	private static boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
	private static int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private static double nd() { return Double.parseDouble(ns()); }
	private static char nc() { return (char)skip(); }
	
	private static String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private static char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private static char[][] nm(int n, int m)
	{
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private static int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private static int ni()
	{
		int num = 0, b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static long nl()
	{
		long num = 0;
		int b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}