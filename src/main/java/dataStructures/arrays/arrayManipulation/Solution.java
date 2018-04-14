package dataStructures.arrays.arrayManipulation;

import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        long[] array = new long[n + 1];
        Arrays.fill(array, 0L);
        
        for ( int i = 0 ; i < m; i++ ) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            long k = scanner.nextLong();
            
            array[a] += k;

            if ( b + 1 <= n ) {
                array[b + 1] -= k;
            }
        }
        
        long x = 0;
        long max = 0;
        for ( int i = 0 ; i < array.length ; i++ ) {
            x += array[i]; 
            if ( max < x ) {
                max = x;
            }
        }
        
        System.out.println(max);
    }
}
