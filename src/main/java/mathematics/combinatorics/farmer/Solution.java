package mathematics.combinatorics.farmer;

import java.util.*;

public class Solution {

    private static int findSubMatrix(boolean[][] matrix, int height, int width) {
        int count = 0;
        for ( int i = 0; i < matrix.length; i++ ) {
            for ( int j = 0; j < matrix[i].length; j++ ) {
                int k = 0;
                for ( int h = 0; h < height; h++ ) {
                    if ( (i + h) < matrix.length ) {
                        for ( int w = 0; w < width; w++ ) {
                            if ( (j + w) < matrix[i].length ) {
                                if ( matrix[i + h][j + w] ) {
                                    k++;
                                }        
                            }
                        }
                    }        
                }
                if ( k == (height * width) ) {
                    count++;    
                }
            }
        }
        return count;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        boolean[][] farm = new boolean[N][M];
        for ( int i = 0; i < N; i++ ) {
            String line = scanner.next();
            for ( int j = 0; j < M; j++ ) {
                farm[i][j] = line.charAt(j) == '0' ? false : true;
            }
        }
        int[][] output = new int[N][M];        
        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < M; j++ ) {
                output[i][j] = 0;
            }
        }
        for ( int height = 1; height <= N; height++ ) {
           for ( int width = 1; width <= M; width++ ) {              
               output[height - 1][width - 1] = findSubMatrix(farm, height, width);
           }
        }
            
        for ( int i = 0; i < N; i++ ) {
            String line = "";
            for ( int j = 0; j < M; j++ ) {
                line += output[i][j] + " ";
            }
            System.out.println( line.substring(0, line.length() - 1) );
        }
    }
}