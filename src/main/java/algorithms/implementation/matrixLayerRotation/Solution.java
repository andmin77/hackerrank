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
        int middle = Math.min(M, N) / 2;
        List<List<Integer>> data = new ArrayList<>();
        for ( int index = 0; index < middle; index++ ) {
            List<Integer> e = new ArrayList<>();
            for ( int col = index; col < N - index; col++ ) {
                e.add( matrix[index][col] );
            }            
            for ( int row = index + 1; row < M - index; row++ ) {
                e.add( matrix[row][N - index - 1] );
            }
            for ( int col = N - index - 1; col > index; col-- ) {
                e.add( matrix[M - index - 1][col - 1] );
            } 
            for ( int row = M - index - 2; row > index; row-- ) {
                e.add( matrix[row][index] );
            }
            data.add( e );            
        }        
        int[][] output = new int[M][N];
        int index = 0;
        for ( List<Integer> e : data ) {
            int rotation = R % e.size();
            int[] t_array = new int[e.size()];
            for ( int i = 0; i < t_array.length; i++ ) {
                if ( i + rotation < e.size() ) {
                    t_array[i] = e.get(i + rotation);
                } else {
                    t_array[i] = e.get( rotation - ( e.size() - i) );
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