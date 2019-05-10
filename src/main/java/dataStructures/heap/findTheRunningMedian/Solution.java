package dataStructures.heap.findTheRunningMedian;

import java.text.*;
import java.util.*;

public class Solution {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] list = new int[n];
        double[] medians = new double[n];
        int size = 0, a = 0, b = 0;
        double mediam = 0;
        for ( int index = 0; index < n; index++ ) {            
            list[index] = scanner.nextInt();
            size++;
            
            Arrays.sort( list, 0, size );
            
            if ( size % 2 == 0 ) {
                mediam = (double) ( list[ size / 2 ] + list[ size / 2 + 1] ) / 2d;
            } else {
                mediam = (double) list[ size / 2 ];
            }
            medians[index] = mediam;
        }
        DecimalFormat df = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.ITALY);
        df.setMaximumFractionDigits(1);
        
        for ( int index = 0; index < n; index++ ) {
            System.out.printf( "%.1f", medians[index] );
        }
    }
}
