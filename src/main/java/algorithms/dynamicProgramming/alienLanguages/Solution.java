package algorithms.dynamicProgramming.alienLanguages;

import java.util.*;

public class Solution {
    
    private static final int MAX_LEN = 500005;
    private static final int MAXN = 100005;
    private static final long MOD = 100000007;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for ( int t = 0; t < T; t++ ) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            
            long[] dp = new long[MAX_LEN];
            long[][] f = new long[20][MAXN];
            long[] g = new long[20];
            
            int mid = n >> 1;
            for ( int i = 0; i <= mid; i++ ) {
                f[0][i] = 1;
            }
            int len = 0;
            for ( int i = 1, p = 1; i <= m && p <= mid; i++, p <<= 1 ) {
                for ( int j = p; j <= mid; j++ ) {
                    f[i][j] = ( f[i][j - 1] + f[i - 1][j >> 1] ) % MOD;
                }
                len = i;
            }
			
            for ( int i = 1; i <= len + 1; i++ ) {
                for ( int j = mid + 1; j <= n; j++ ) {
                    g[i] = ( g[i] + f[i - 1][j >> 1] ) % MOD;
                }
            }
            
            dp[0] = 1;
            for ( int i = 1; i <= m; i++ ) {
                for ( int j = 1; j <= len + 1; j++ ) {
                    if (i - j >= 0) {
                        dp[i] += dp[i - j] * g[j];
                        dp[i] %= MOD;
                    }
                }
            }

            System.out.println(dp[m]);
        }
    }
}