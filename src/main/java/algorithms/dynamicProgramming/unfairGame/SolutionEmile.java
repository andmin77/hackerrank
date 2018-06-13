package algorithms.dynamicProgramming.unfairGame;

import java.util.*;
import java.io.*;
import static java.lang.Math.*;

public class SolutionEmile {
	static class Foo57 {
		static final long INF = Long.MAX_VALUE/3;
		void main() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(System.in));
				int T = Integer.parseInt(br.readLine().trim());
				for (int i = 0; i < T; i++) {
					int N = Integer.parseInt(br.readLine().trim());
					int[] arr = new int[N];
					String[] s = br.readLine().trim().split("\\s+");
					for (int j = 0; j < N; j++) {						
						arr[j] = Integer.parseInt(s[j].trim());
					}
					long res = foo(arr);
					System.out.println(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try { br.close(); } catch (Exception e) { e.printStackTrace(); }
				}
			}
		}
		
		long foo(int[] arr) {
			int N = arr.length;
			int MAX = 29;
			while (MAX >= 0) {
				boolean find = false;
				for (int val : arr)
					if ((val & 1<<MAX) != 0) {
						find = true;
						break;
					}
				if (find) break;
				MAX--;
			}
			MAX += 3;
			int[] vec = new int[MAX];
			for (int i = 0; i < MAX; i++) {
				for (int j = 0; j < N; j++) {
					if ((arr[j] & 1<<i) != 0)
						vec[i] |= 1<<j;
				}
			}
			int MOD = (1<<N)-1;
			long[][] dp = new long[MAX][1<<N];
			for (long[] a : dp) Arrays.fill(a, INF);
			Arrays.fill(dp[0], 0);
			for (int i = 1; i < MAX; i++) {
				for (int state = 0; state < (1<<N); state++) {
					int bit = i-1;
					int v = Integer.bitCount(~state&vec[bit]&MOD)&1;
					//int v = 0;
					long add = -(long)Integer.bitCount(state&vec[bit]&MOD) << bit;
					//long add = 0;
					/*for (int j = 0; j < N; j++) {
						if ((state & 1<<j ) == 0 && (arr[j] & 1<<bit) != 0) {
							v ^= 1;
						} else if ((state & 1<<j) != 0 && (arr[j] & 1<<bit) != 0) {
							add -= 1<<bit;
						}
					}*/
					if (v == 0) {
						dp[i][state] = min(dp[i][state], dp[i-1][state] + add);
					} else {
						// find one to toggle
						int curr = MOD - (~state&vec[bit]&MOD);
						long toAdd = add + (1<<bit);
						while (curr > 0) {
							int a = curr-1;
							dp[i][state] = min(dp[i][state], dp[i-1][state|(~a&curr)] + toAdd);
							curr &= a;
						}
						/*for (int j = 0; j < N; j++) {
							if ((curr & 1<<j) != 0) {
								dp[i][state] = min(dp[i][state], dp[i-1][state|1<<j] + toAdd);
							}
						}*/
					}
					// when state == 0 and no need to toggle, try to find 2 zero to toggle
					if (state == 0 && v == 0) {
						for (int j = 0; j < N; j++) {
							if ((arr[j] & 1<<bit) != 0) continue;
							for (int k = j+1; k < N; k++) {
								if ((arr[k] & 1<<bit) != 0) continue;
								dp[i][state] = min(dp[i][state], dp[i-1][state|1<<j|1<<k] + (1L<<bit+1));
							}
						}
					}
				}
			}
			return dp[MAX-1][0];
		}
	}
	
	public static void main(String[] args) {
		Foo57 foo = new Foo57();
		foo.main();
	}
}