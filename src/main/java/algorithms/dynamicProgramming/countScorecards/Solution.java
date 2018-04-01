package algorithms.dynamicProgramming.countScorecards;

import java.util.*;

public class Solution {

    private static final int mod = 1000000007;
    
    private static int[][] C = new int[55][55];
    private static int[] a = new int[55];
    
    public static void main(String[] args) {
        for ( int i = 0; i < 50; ++i ) {
            C[i][0] = 1;
            for ( int j = 1; j <= i; ++j ) {
                C[i][j] = (C[i - 1][j] + C[i - 1][j - 1]) % mod;
            }
	}
        
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for ( int tCount = 0; tCount < T; tCount++ ) {
            int n = 0, m = 0;
            int N = scanner.nextInt();
            
            for ( int i = 0; i < N; ++i )  {
                int x = scanner.nextInt();
                if (x == -1) {
                    ++m;
                } else {
                    a[n++] = x;
                }
            }
        }
    }
}