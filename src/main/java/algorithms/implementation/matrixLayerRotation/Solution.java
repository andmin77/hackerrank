package algorithms.implementation.matrixLayerRotation;

import java.util.*;

public class Solution {
   
    private static enum Orientation {
        North,
        East,
        South,
        West;
    }

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
        int[][] output = new int[M][N];
        int middle = Math.min(M, N) / 2;
        int index = 0;
        for ( int t = 0; t < middle; t++ ) {
            List<Integer> data = new ArrayList<>();
            for ( int col = index; col < N - index; col++ ) {
                data.add( matrix[index][col] );
            }            
            for ( int row = index + 1; row < M - index; row++ ) {
                data.add( matrix[row][N - index - 1] );
            }
            for ( int col = N - index - 1; col > index; col-- ) {
                data.add( matrix[M - index - 1][col - 1] );
            } 
            for ( int row = M - index - 2; row > index; row-- ) {
                data.add( matrix[row][index] );
            }
            int rotation = R % data.size();
            int[] t_array = new int[data.size()];
            for ( int i = 0; i < t_array.length; i++ ) {
                if ( i + rotation < data.size() ) {
                    t_array[i] = data.get(i + rotation);
                } else {
                    t_array[i] = data.get( rotation - ( data.size() - i) );
                }
            }
            Orientation orientation = Orientation.North;
            int row = index, col = index;
            for ( int i = 0; i < t_array.length; i++ ) {
                output[row][col] = t_array[i];
                switch ( orientation ) {
                    case North:
                        col++;
                        if ( col >= N - index - 1 ) {
                            orientation = Orientation.East;
                        }
                        break;
                    case East:
                        row++;
                        if ( row >= M - index - 1 ) {
                            orientation = Orientation.South;
                        }
                        break;
                    case South:
                        col--;
                        if ( col <= index ) {
                            orientation = Orientation.West;
                        }
                        break;
                    case West:
                        row--;
                        break;
                }
            }
            index++;
        }
        for ( int row = 0; row < M; row++ ) {
            String r = "";
            for ( int col = 0; col < N; col++ ) {
                r += output[row][col] + " ";
            }
            r = r.substring(0, r.length() - 1);
            System.out.println( r );
        }
    }
}