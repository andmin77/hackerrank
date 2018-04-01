package algorithms.dynamicProgramming.onesAndTwos;

import java.util.*;

public class Solution {
    
    private static final int MXB = 1000;
    private static final int MOD = 1000000000 + 7;
    
    private static int[] up = new int[33];
    private static int[][] p = new int[33][1005];

    private static void pre(int S) {
        if ( up[S] == 0 ) {
            up[S] = 1;
            p[S][0] = 1;
            for (int l = S; l <= MXB; l++ ) {
                for ( int j = MXB; j >= l; j-- ) {
                    p[S][j] = ( p[S][j] + p[S][ j - l ] ) % MOD;
                }
            }
            for ( int j = 1; j <= MXB; j++ ) {
                p[S][j] = ( p[S][j] + p[S][ j - 1 ] ) % MOD;
            }
        }
    }

    public static void main(String[] args) {
        Arrays.fill(up, 0);
        for ( int[] row : p ) {
            Arrays.fill(row, 0);
        }
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for ( int k = 0; k < T; k++ ) {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            long result = 0;
            if ( A == 0 ) {
                pre(1);
                result = ( p[1][B] + MOD - 1 ) % MOD;
            } else {
                long t = 0;
                int s = 0;
                for ( t = 2, s = 1; t <= A; t *= 2, s++ ) { ; }
                t *= 2; 
                s++;
                pre(s);
                long last = 0, last2 = t / 2 + A + 1;
                for ( int u = 0; u < s && u <= B; u++ ) {
                    long nxt = Math.min(t, (1 << u) + A + (( u > 0 ) ? 1 : 0) );
                    long nxt2 = Math.min(t, (1 << u) + A + t/2 + (( u > 0 ) ? 1 : 0) );
                    result = ( result + ((nxt - last) * p[s][B - u]) ) % MOD;
                    if ( (u + s - 1) <= B ) {
                        result = (result + ((nxt2 - last2) * p[s][B - ( u + s - 1)]) ) % MOD;
                    }
                    last = nxt;
                    last2 = nxt2;
                }
                result = ( result + MOD - 1 ) % MOD;
            }
            System.out.println(result);
        }
    }
}