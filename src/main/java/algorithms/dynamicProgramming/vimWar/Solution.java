package algorithms.dynamicProgramming.vimWar;

import java.util.*;

public class Solution {
    
    private static final int MOD = 1000000007;
    private static final int MAXV = 1048576;
    
    private static int[] p = new int[MAXV + 3];
    private static int[] mp = new int[MAXV + 3];

    public static void main(String[] args) {
        p[0] = 1;
        for ( int i = 1; i <= MAXV; i++ ) {
            p[i] = ( p[i - 1] * 2) % MOD;
        }
        for ( int i = 0; i <= MAXV; i++ ) {
            p[i]--;
            p[i] += MOD;
            p[i] %= MOD;
        }
        Arrays.fill(mp, 0);
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        for ( int i = 0; i < n; i++ ) {
            int val = 0;
            String str = scanner.next();
            if ( str.length() <= m ) {
                for ( int j = 0; j < m; j++ ) {
                    int x = str.charAt(j) - '0';
                    val = val * 2 + x;
                }
                mp[val]++;
            }
        }
        int req = 0;
        String str = scanner.next();
        if ( str.length() <= m ) {
            for ( int j = 0; j < m; j++ ) {
                int x = str.charAt(j) - '0';
                req = req * 2 + x;
            }
        }
        for ( int i = 0; i < 20; i++ ) {
            for ( int j = 0; j <= MAXV; j++ ) {
                if ( (j & (1 << i)) != 0 ) {
                   mp[j] += mp[ j ^ ( 1 << i ) ];
                }
            }    
        }
        int ans = 0;
        for ( int j = req; j >= 0; j-- ) {
            if ( ( j | req ) != req ) {
              continue;
            }
            if ( ( Integer.bitCount( j ^ req) % 2 ) != 0 )
                ans = ( ans - p[mp[j]] + MOD ) % MOD;
            else
                ans = ( ans + p[mp[j]] ) % MOD;
        }
        System.out.println(ans);
    }
}
