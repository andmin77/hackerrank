package algorithms.dynamicProgramming.theLongestIncreasingSubsequence;

import java.util.*;

public class Solution {

    private static int lis(int[] data) {
        int lis[] = new int[ data.length ]; 

        /* Initialize LIS values for all indexes */
        for ( int i = 0; i < lis.length; i++ ) 
              lis[i] = 1; 
  
        /* Compute optimized LIS values in bottom up manner */
        for ( int i = 0; i < lis.length; i++ ) {
            for ( int j = 0; j < i; j++ ) {
                if ( data[i] > data[j] && lis[i] < lis[j] + 1) {
                    lis[i] = lis[j] + 1; 
                }
            }
        }
        int max = 0;
        /* Pick maximum of all LIS values */
        for ( int i = 0; i < lis.length; i++ ) {
            if ( max < lis[i] ) {
                max = lis[i];
            }
        }
        
        return max; 
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        int[] m = new int[n + 1];
        int INF = 1 << 30;
        
        for ( int index = 0; index < a.length; index++ ) {
            a[index] = scanner.nextInt();
            m[index + 1] = INF;
        }
        for ( int i = 0; i < n; i++ ) {
            int lo = 0, hi = n;
            while (hi - lo > 1) {
                int md = lo + hi >> 1;
                if ( m[md] < a[i] ) {
                    lo = md;
                } else {
                    hi = md;
                }
            }
            m[hi] = a[i];
        }
        int result = 0;
        while ( result < n && m[result + 1] < INF ) {
            result++;
        }
        System.out.println("" + result);
    }
}