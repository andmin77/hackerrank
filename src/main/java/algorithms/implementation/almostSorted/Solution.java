package algorithms.implementation.almostSorted;

import java.util.*;

public class Solution {
   
    public static void main(String[] args) {            
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] d = new int[n + 1];
        for ( int index = 1; index <= n; index++ ) {
            d[index] = scanner.nextInt();
        }

        int idx_sorted = 0;
        
        int[] not_sorted = new int[n + 1];
        int idx_not_sorted = 0;
        for ( int index = 2; index <= n; index++ ) {
            if ( d[index] > d[index - 1] ) {
                idx_sorted++;
            } else {
                if ( idx_not_sorted > 0 ) {
                    not_sorted[idx_not_sorted] = index;
                    idx_not_sorted++;
                } else {
                    not_sorted[idx_not_sorted] = index - 1;
                    idx_not_sorted++;
                    not_sorted[idx_not_sorted] = index;
                    idx_not_sorted++;
                }
            }
        }
        if ( idx_sorted == n - 1 ) {
            System.out.println( "yes" );
        } else if ( n == 2 ) {
            System.out.println( "yes" );
            System.out.println(  "swap 1 2" );
        } else {
            int[] d1 = new int[n + 1];
            for ( int index = 1; index <= n; index++ ) {
                d1[index] = d[index];
            }
            int idx1 = not_sorted[0];
            int idx2 = not_sorted[idx_not_sorted - 1];
            int swap = d1[idx1];
            d1[idx1] = d1[idx2];
            d1[idx2] = swap;
            boolean swapped = true;
            for ( int index = 2; index <= n && swapped; index++ ) {
                if ( d1[index] < d1[index - 1] ) {
                    swapped = false;
                }
            }
            if ( swapped ) {
                System.out.println( "yes" );
                System.out.println(  "swap " + not_sorted[0] + " " + not_sorted[idx_not_sorted - 1] );
            } else {
                boolean reversed = true;
                for ( int index = 1; index < idx_not_sorted && reversed; index++ ) {
                    if ( not_sorted[index - 1] + 1 != not_sorted[index] ) {
                        reversed = false;
                    }                
                }
                if ( reversed ) {
                    int[] d2 = new int[n + 1];
                    for ( int index = 1; index <= n; index++ ) {
                        d2[index] = d[index];
                    }
                    for ( int index = idx_not_sorted; index > 0; index-- ) {
                        d2[ not_sorted[index - 1] ] = d[ not_sorted[idx_not_sorted - index] ];
                    }
                    for ( int index = 2; index <= n && reversed; index++ ) {
                        if ( d2[index] < d2[index - 1] ) {
                            reversed = false;
                        }
                    }
                    if ( reversed ) {
                        System.out.println( "yes" );
                        System.out.println( "reverse " + not_sorted[0] + " " + not_sorted[idx_not_sorted - 1] );
                    } else {
                        System.out.println( "no" );
                    }
                } else {
                    System.out.println( "no" );
                }
            }
        }
    }
}