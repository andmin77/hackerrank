package algorithms.implementation._3DSurfaceArea;

import java.util.*;

public class Solution {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int H = scanner.nextInt();
        int W = scanner.nextInt();
        int[][] A = new int[H][W];
        int[][] partial = new int[H][W];
        for ( int i = 0; i < H ; i++ ) {
            for ( int j = 0; j < W ; j++ ) {
                A[i][j] = scanner.nextInt();
                partial[i][j] = 4 * A[i][j] + 2;
            }
        }
        
        for ( int i = 0; i < H; i++ ) {
            for ( int j = 1; j < W; j++ ) {
                if ( A[i][j - 1] > 0 && A[i][j] > 0 ) {
                    int min = Math.min( A[i][j - 1], A[i][j] );
                    partial[i][j - 1] = partial[i][j - 1] - min;
                    partial[i][j] = partial[i][j] - min;
                }
            }
        }
        
        for ( int j = 0; j < W; j++ ) {
            for ( int i = 1; i < H; i++ ) {
                if ( A[i - 1][j] > 0 && A[i][j] > 0 ) {
                    int min = Math.min( A[i - 1][j], A[i][j] );
                    partial[i - 1][j] = partial[i - 1][j] - min;
                    partial[i][j] = partial[i][j] - min;
                }
            }
        }
        int prize = 0;
        for ( int i = 0; i < H; i++ ) {
            for ( int j = 0; j < W; j++ ) {
                prize += partial[i][j];
            }
        }
        
        System.out.println("" + prize);
        
    }
}
