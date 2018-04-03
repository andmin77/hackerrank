package algorithms.dynamicProgramming.policeOperation;

import java.util.*;

public class Solution {
    
    private static class Pair<A, B> {

        public final A first;
        public final B second;

        private Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public static <A, B> Pair<A, B> of(A first, B second) {
            return new Pair<A, B>(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ')';
        }        
    }
    private static final int mx = 2000010;
    private static ArrayList<Pair<Long, Long>> poly = new ArrayList<>();
        
    private static void add( long idx, Pair<Long, Long> x ) {
        while ( poly.size() - idx > 1  ) {
            int i = poly.size() - 2, j = poly.size() - 1;
            long a = poly.get(j).second - poly.get(i).second ;
            long b = poly.get(i).first - poly.get(j).first ;
            long c = poly.get(j).second - x.second ;
            long d = x.first - poly.get(j).first ;                          
            double d1 = (double) a / (double) b;
            double d2 = (double) c / (double) d;
            if ( d1 < d2 ) {             
                poly.remove( poly.size() - 1 );
                
                continue;
            }
            break;
        }
        poly.add(x);
    }

    private static long which( Pair<Long, Long> v, long x ) {
        return ( v.second + x * v.first );
    }
    
    public static void main(String[] args) {
        poly.clear();
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
            poly.add( Pair.of( -(x[n - 1] << 1L), x[n - 1] * x[n - 1] ) );
            for ( int i = n - 1 , idx = 0; i >= 0; i--) {
                while ( (poly.size() - idx > 1) && (which( poly.get(idx), x[i] ) > which( poly.get(idx + 1), x[i] )) ) {
                    idx++;
                }
                result = h + x[i] * x[i] + which(poly.get(idx), x[i]);
                if ( i > 0 ) {
                    add(idx, Pair.of( -(x[i - 1] << 1L), result + x[i - 1] * x[i - 1] ) );
                } else {
                    //add(idx, Pair.of( 0L, result) );
                }
            }
        }
        System.out.println(result);
    }
}