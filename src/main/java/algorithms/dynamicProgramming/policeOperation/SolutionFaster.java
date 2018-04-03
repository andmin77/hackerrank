package algorithms.dynamicProgramming.policeOperation;

import java.util.*;

public class SolutionFaster {
        
    private static final int mx = 2000010;
    private static long[] polyFirst;
    private static long[] polySecond;
    private static int polySize;
        
    private static void add( long idx, long first, long second ) {
        while ( polySize - idx > 1  ) {
            int i = polySize - 2, j = polySize - 1;
            long a = polySecond[j] - polySecond[i];
            long b = polyFirst[i] - polyFirst[j];
            long c = polySecond[j] - second;
            long d = first - polyFirst[j];
            double d1 = (double) a / (double) b;
            double d2 = (double) c / (double) d;
            if ( d1 < d2 ) {
                polySize--;
                continue;
            }
            break;
        }
        polyFirst[polySize] = first;
        polySecond[polySize] = second;
        polySize++;
    }

    private static long which( long first, long second, long x ) {
        return ( second + x * first );
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int h = scanner.nextInt();
        long result = 0;
        if ( n > 0 ) {
            long[] x = new long[n];
            Arrays.fill( x, 0 );
            for ( int i = 0; i < n; i++ ) {
                x[i] = scanner.nextLong();
            }
            polyFirst = new long[mx];
            polySecond = new long[mx];
            polySize = 0;
            polyFirst[polySize] = -(x[n - 1] << 1L);
            polySecond[polySize] = x[n - 1] * x[n - 1];
            polySize++;
            for ( int i = n - 1 , idx = 0; i >= 0; i--) {
                while ( (polySize - idx > 1) && (which( polyFirst[idx], polySecond[idx], x[i] ) > which( polyFirst[idx + 1], polySecond[idx + 1], x[i] )) ) {
                    idx++;
                }
                result = h + x[i] * x[i] + which(polyFirst[idx], polySecond[idx], x[i]);
                if ( i > 0 ) {
                    add(idx, -(x[i - 1] << 1L), result + x[i - 1] * x[i - 1] );
                } else {
                    add(idx, 0L, result );
                }
            }
        }
        System.out.println(result);
    }
}