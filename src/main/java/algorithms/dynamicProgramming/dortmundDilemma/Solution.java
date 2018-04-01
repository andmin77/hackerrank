package algorithms.dynamicProgramming.dortmundDilemma;

import java.util.*;

public class Solution {
    
    private static final int mx = 100010;
    private static final long mod = 1000000009;
    
    private static long[][] dp = new long[mx][27];
    private static long[][] ncdp = new long[28][28];
    
    private static long ncr(int n, int r) {
        if ( r > n ) return 0;
        if ( n == r ) return 1;
        if ( r == 0 ) return 1;
        if ( r == 1 ) return n % mod;
        if ( ncdp[n][r] == -1 ) {
            ncdp[n][r] = ( ncr(n - 1, r - 1) + ncr(n - 1, r) ) % mod;
        }
        return ncdp[n][r];
    }

    public static void main(String[] args) {
        for (long[] row: ncdp)
            Arrays.fill(row, -1);

        /// Calculating F(N,K)
        for ( int L = 1; L <= 100000; L++ ) {
            for ( int k = 1; k <= 26; k++ ) {
                if ( L == 1 ) {
                    dp[L][k] = k;
                } else {
                    long now = dp[L][k];
                    now = dp[L - 1][k];
                    now *= k;
                    now %= mod;
                    if ( L % 2 == 0 ) {
                        now -= dp[ L/2 ][k];
                        now %= mod;
                        if ( now < 0 ) {
                            now += mod;
                        }
                    }
                    dp[L][k] = now;
                }
            }
        }
        
        /// Calculating G(N,K)
        for ( int k = 1; k <= 26; k++ ) {
            long now = 1;
            for ( int L = 1; L <= 100000; L++) {
                now *= k;
                now %= mod;
                dp[L][k] = now - dp[L][k];
                if ( dp[L][k] < 0 ) {
                    dp[L][k] += mod;
                }
            }
        }

        /// Calculating P(N,K)
        for ( int k = 1; k <= 26; k++ ) {
            for ( int L = 1; L <= 100000; L++ ) {
                for ( int j = k; j >= 1; j-- ) {
                    dp[L][k] -= ( ncr(k,j-1) * dp[L][j-1] )%mod;
                    if( dp[L][k] < 0 ) dp[L][k] += mod;
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for ( int t = 0; t < T; t++ ) {
            int N = scanner.nextInt();
            int K = scanner.nextInt();
            long result = ( dp[N][K] * ncr(26, K) ) % mod;
            System.out.println(result);
        }
    }
}