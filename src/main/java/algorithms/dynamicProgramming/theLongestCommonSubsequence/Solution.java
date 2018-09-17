package algorithms.dynamicProgramming.theLongestCommonSubsequence;

import java.io.*;
import java.util.*;

public class Solution {

    // Complete the longestCommonSubsequence function below.
    static int[] longestCommonSubsequence(int[] a, int[] b) {
        int[][] M = new int[ a.length + 1 ][ b.length + 1 ];
        for ( int r = 0; r < M.length; r++ ) {
            for ( int c = 0; c < M[r].length; c++ ) {
                M[r][c] = 0;
            }
        }
        for ( int i = 0; i < a.length; i++ ) {
            for ( int j = 0; j < b.length; j++ ) {
                if ( a[i] == b[j] ) {
                    M[i + 1][j + 1] = 1 + M[i][j];
                } else {
                    M[i + 1][j + 1] = Math.max( M[i][j + 1], M[i + 1][j]);
                }
            }
        }
        
        
        int i = a.length - 1, j = b.length - 1;
        List<Integer> tmp = new ArrayList<>();
        while ( i >= 0 && j >= 0 ) {
            if ( a[i] == b[j] ) {
                tmp.add( a[i] );
                i--;
                j--;
            } else {
                if ( M[i][j + 1] > M[i + 1][j] ) {
                    i--;
                } else {
                    j--;
                }
            }
        }
        Collections.reverse(tmp);        
        int[] result = new int[ tmp.size() ];
        for ( int k = 0; k < tmp.size(); k++ ) {
            result[k] = tmp.get(k);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        
        int m = scanner.nextInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }

        int[] result = longestCommonSubsequence(a, b);

        String output = "";
        for (int i = 0; i < result.length; i++) {
            output += result[i] + " ";
        }
        System.out.println( output.substring(0, output.length() - 1) );
    }
}
