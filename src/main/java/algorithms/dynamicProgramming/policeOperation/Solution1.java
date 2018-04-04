package algorithms.dynamicProgramming.policeOperation;

import java.util.*;

public class Solution1 {
    
//    https://github.com/marioyc/Online-Judge-Solutions/blob/master/hackerrank/101aug14/police-operation.cpp
    
    
    /*
  j < k: j better than k iff
  s[j]-s[k] + a[j+1]^2-a[k+1]^2 + 2a[i](a[k+1]-a[j+1]) < 0
  2a[i](a[k+1]-a[j+1])  < a[k+1]^2-a[j+1]^2+s[k]-s[j]
*/
    /*
#include <algorithm>
#include <cstdio>
using namespace std;

#define FOR(i, a, b) for (int i = (a); i < (b); i++)
#define REP(i, n) for (int i = 0; i < (n); i++)
#define REP1(i, n) for (int i = 1; i <= (n); i++)
#define ROF(i, a, b) for (int i = (b); --i >= (a); )
#define P(x) ((x)*(x))
#define F(x,y) (P(a[(y)+1])-P(a[(x)+1])+dp[y]-dp[x])
*/
    

    private static final int N = 2000000;
    private static int[] q;
    private static long[] a;
    private static long[] dp;

    private static long F(int x, int y) {
        return ( a[y + 1] + a[y + 1] - a[x + 1] * a[x + 1] + dp[y] - dp[x] );
    }
 
        
        
    private static int unique(long[] array, int n) {
        int count = 0;
        Map<Long, Integer> data = new TreeMap<>();
        for ( int i = 0; i < n; i++ ) {
            if ( data.get( array[i] ) == null ) {
                data.put( array[i], i );
                count++;
            }
        }
        for ( long key : data.keySet() ) {
            int i = data.get(key);
            array[i] = key;
        }
        return count;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int x = scanner.nextInt();
        
        q = new int[N + 1];
        Arrays.fill(q, 0);
        a = new long[N + 1];
        Arrays.fill(a, 0);
        dp = new long[N + 1];
        Arrays.fill(dp, 0);
        //*fr = q, *re = q+1;
        
        int fr = 0;
        int re = 1;

        for ( int i = 1; i <= n; i++ ) {
            a[i] = scanner.nextLong();
        }
        // opzionale: n = unique(a+1, a+n+1) - (a+1);
        n = unique(a, n);
        for ( int i = 1; i <= n; i++ ) {
            while ( q[fr + 1] < q[re] && 2 * a[i] * ( a[q[fr + 1] + 1] - a[q[fr] + 1] ) > F(q[fr], q[fr + 1]) ) {
                fr++;
            }
            dp[i] = dp[fr] + (a[i] - a[q[fr] + 1]) * (a[i] - a[q[fr] + 1]) + x;
            if ( i < n) {
               // System.err.println("fr = " + fr + ", re = " + re);
               /*
               if ( re > 1 ) {
                 long d1 = a[q[re - 1] + 1] - a[q[re - 2] + 1] ;
                    long d2 = a[i + 1] - a[q[re - 1] + 1];
                    System.err.println("d1 = " + d1 + ", d2 = " + d2);
               }*/
                while ( re > 1 && a[q[re - 1] + 1] != a[q[re - 2] + 1] && a[i + 1] != a[q[re - 1] + 1] && q[fr] <= q[re - 2] && ( F(q[re - 2], q[re - 1]) / (a[q[re - 1] + 1] - a[q[re - 2] + 1] ) > F(q[re - 1], i) / (a[i + 1] - a[q[re - 1] + 1]) ) ) {
                    re--;
                      
                }
               
                re++;
                q[re] = i;
            }
        }
        System.out.println(dp[n]);
    }
        /*
  REP1(i, n) {
    while (fr+1 < re && 2*a[i]*(a[fr[1]+1]-a[*fr+1]) > F(*fr, fr[1]))
      fr++;
    dp[i] = dp[*fr]+P(a[i]-a[*fr+1])+x;
    if (i < n) {
      while (fr <= re-2 && F(re[-2], re[-1]) / (a[re[-1]+1]-a[re[-2]+1]) > F(re[-1], i) / (a[i+1]-a[re[-1]+1]))
        re--;
      *re++ = i;
    }
  }
  printf("%ld\n", dp[n]);
}*/

}
