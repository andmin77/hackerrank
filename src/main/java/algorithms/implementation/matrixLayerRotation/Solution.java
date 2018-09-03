package algorithms.implementation.matrixLayerRotation;

import java.util.*;

public class Solution {
   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int M = scanner.nextInt();
        int N = scanner.nextInt();
        int R = scanner.nextInt();
        int[][] matrix = new int[M][N];                
        for ( int row = 0; row < M; row++ ) {
            for ( int col = 0; col < N; col++ ) {
                matrix[row][col] = scanner.nextInt();
            }
        }
        R = R % ( 2 * (M + N) );
        int middle = Math.min(M, N) / 2;
        for ( int r = 0; r < R; r++ ) {
            for ( int index = 0; index < middle; index++ ) {
                int temp = matrix[index][index];
                for ( int col = index; col < N - index - 1; col++ ) {
                    matrix[index][col] = matrix[index][col + 1];
                }            
                for ( int row = index; row < M - index - 1; row++ ) {
                    matrix[row][N - index - 1] = matrix[row + 1][N - index - 1];
                }
                for ( int col = N - index - 1; col > index; col-- ) {
                    matrix[M - index - 1][col] = matrix[M - index - 1][col - 1];
                } 
                for ( int row = M - index - 1; row > index; row-- ) {
                    matrix[row][index] = matrix[row - 1][index];
                }
                matrix[index + 1][index] = temp;
            }
        }
        
        /*
        String[] rcn = scanner.nextLine().split(" ");
        int M = Integer.parseInt(rcn[0]);
        int N = Integer.parseInt(rcn[1]);
        int R = Integer.parseInt(rcn[2]);
        int[][] matrix = new int[M][N];                
        for ( int row = 0; row < M; row++ ) {
            String gridItem = scanner.nextLine();
            for ( int col = 0; col < N; col++ ) {
                matrix[row][col] = Integer.parseInt( "" + gridItem.charAt(col) );
            }
        }
        */
        
        for ( int row = 0; row < M; row++ ) {
            String r = "";
            for ( int col = 0; col < N; col++ ) {
                r += matrix[row][col] + " ";
            }
            r = r.substring(0, r.length() - 1);
            System.out.println( r );
        }
    }
}