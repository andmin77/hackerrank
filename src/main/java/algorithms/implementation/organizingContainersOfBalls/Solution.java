package algorithms.implementation.organizingContainersOfBalls;

import java.util.*;

public class Solution {
    public static void main(String[] args) {            
        Scanner scanner = new Scanner(System.in);
        int q = scanner.nextInt();
        for ( int t = 0; t < q; t++ ) {
            int n = scanner.nextInt();
            int[][] M = new int[n][n];
            for ( int container = 0; container < n; container++ ) {
                for ( int type = 0; type < n; type++ ) {
                    M[container][type] = scanner.nextInt();
                }      
            }
            
            int[] bag = new int[n];
            Arrays.fill( bag, 0 );
            int[] counter = new int[n];
            Arrays.fill( counter, 0 );

            boolean possible = true;
            
            for ( int container = 0; container < n; container++ ) {
                for ( int type = 0; type < n; type++ ) {
                    counter[container] += M[type][container];
                }
            }
            for ( int container = 0; container < n; container++ ) {
                for ( int type = 0; type < n; type++ ) {
                    bag[container] += M[container][type];
                }
            }
            Arrays.sort(counter);
            Arrays.sort(bag);
         
            for ( int index = 0; index < n && possible; index++ ) {
                if ( counter[index] != bag[index] ) {
                    possible = false;
                }
            }
            if ( possible ) {
                System.out.println("Possible");
            } else {
                System.out.println("Impossible");
            }
        }
    }
}