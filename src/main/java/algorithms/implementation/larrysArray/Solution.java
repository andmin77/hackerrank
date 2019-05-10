package algorithms.implementation.larrysArray;

import java.util.*;

public class Solution {
    public static void main(String[] args) {            
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for ( int t = 0; t < T; t++ ) {
            int n = scanner.nextInt();
            int[] A = new int[n];
            for ( int index = 0; index < n; index++ ) {
                A[index] = scanner.nextInt();
            }
            int count = 0;
            for ( int index = 0; index < n; index++ ) {
                for ( int i = index + 1; i < n; i++ ) {
                    if ( A[index] > A[i] ) {
                        count++;
                    }
                }
            }
            if ( count % 2 == 0 ) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }
}