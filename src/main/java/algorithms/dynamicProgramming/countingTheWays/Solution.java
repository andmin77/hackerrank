package algorithms.dynamicProgramming.countingTheWays;

import java.util.*;

public class Solution {
    
    private static final int MAXW = 100000;
    private static final int MAXN = 11;
    private static final int MOD = 1000000007;
    private static final int L = 1400100;

    private static int N;
    private static long[][] inv;
    private static int[] a;
    private static int[][] f;

    private static long mod(long x) {
        if ( x >= 0 ) {
            return x % MOD;
        }
        return MOD + x % MOD;
    }

    private static long bin(long x, long y) {
        if (y == 0) {
            return 1;
        }
        long u = bin(x, y / 2);
        if (y % 2 == 0) {
            return u * u % MOD;
        } else {
            return u * u % MOD * x % MOD;
        }
    }
    
    private static long calc(ArrayList<Long> x, ArrayList<Long> y, int n, long X) {
        long res = 0;
        for ( int i = 0; i < n; i++ ) {
            long cur = 1;
            for ( int j = 0; j < n; j++ ) {
                if (i == j) {
                    continue;
                }
                long num = mod( X - x.get(j) );                
                long denum = inv[i][j];
                cur = cur * num % MOD * denum % MOD;
            }
            res += cur * y.get(i);
            res %= MOD;
        }
        return res;
    }
    
    private static long kill(int rem, long wh, int w) {
        wh--;
        ArrayList<Long> x = new ArrayList<>();
        ArrayList<Long> y = new ArrayList<>();
        int s = 0;
        for ( int i = 0; i <= N; i++ ) {
            x.add(new Long(i));
        }
        for ( int i = 0; i <= N; i++ ) {
            s += f[ w * i + rem ][ N - 1 ];
            if (s >= MOD) {
                s -= MOD;
            }
            y.add(new Long(s));
        }
        return calc(x, y, N + 1, wh);
    }

    private static int fun(long X) {
        for ( int i = 0; i < 15; i++ ) {
            for ( int j = 0; j < 15; j++ ) {
                inv[i][j] = bin( mod(i - j), MOD - 2 );
            }
        }
        int w = 1;
        for ( int i = 0; i < N; i++ ) {
            w *= a[i];
        }
        int res = 0;
        for ( int rem = 0; rem < w; rem++ ) {
            if (rem > X) {
                continue;
            }
            long wh = (long) ((double) X / (double) w) + (( X % w >= rem ) ? 1 : 0);
            res += kill(rem, wh, w);
            if (res >= MOD) {
                res -= MOD;
            }
        }
        return res;
    }
    
    private static int brute( int l, int r ) {
        int res = 0;
        for ( int i = l; i <= r; i++ ) {
            res += f[i][N - 1];
            if ( res >= MOD ) {
                res -= MOD;
            }
        }
        return res;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        assert(1 <= N && N <= 10);
        
        inv = new long[15][15];
        a = new int[MAXN];
        f = new int[L][MAXN];
        
        long ww = 1;
        for ( int i = 0; i < N; i++ ) {
            a[i] = scanner.nextInt();
            ww *= a[i];
            assert(a[i] > 0 && a[i] <= MAXW);
        }
        assert(ww <= MAXW);
        for ( int j = 0; j < N; j++ ) {
            f[0][j] = 1;
        }
        for ( int i = 1; i < L; i++ ) {
            for ( int j = 0; j < N; j++ ) {
                if ( j > 0 ) {
                    f[i][j] = f[i][j - 1];
                }
                if ( i >= a[j] ) {
                    f[i][j] += f[ i - a[j] ][j];
                    if ( f[i][j] >= MOD ) {
                        f[i][j] -= MOD;
                    }
                }
            }
        }
        long L = scanner.nextLong();
        long R = scanner.nextLong();
        assert( R < 1E18 );
        int stupid = 0;
        if (R <= 100000) {
            stupid =  brute((int) L, (int) R);            
        }
        long result = mod( fun(R) - fun(L - 1) );
        System.out.println(result);
        if ( R <= 100000 ) {
            assert( result == stupid );
        } 
    }
}