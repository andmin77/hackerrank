package algorithms.implementation.encryption;

import java.util.*;

public class Solution {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        s = s.replaceAll(" ", "");
        int L = s.length();
        double radix = Math.sqrt(L);
        int rows = (int) Math.floor(radix);
        int columns = (int) Math.ceil(radix);
        while ( rows * columns < L ) {
            rows++;
        }
        int k = 0;
        char[][] matrix = new char[rows][columns];
        for ( int row = 0; row < rows; row++ ) {
            for ( int column = 0; column < columns; column++ ) {
                if ( k < L ) {
                    matrix[row][column] = s.charAt(k);
                } else {
                    matrix[row][column] = ' ';
                }
                k++;
            }
        }
        String output = "";
        int t = 0;
        for ( int column = 0; column < columns; column++ ) {
            for ( int row = 0; row < rows; row++ ) {
                if ( t < k && matrix[row][column] != ' ' ) {
                    output += matrix[row][column];
                }
                t++;
            }
            if ( t < k ) {
                output += " ";
            }
        }
        System.out.println( output );
        
    }
}
