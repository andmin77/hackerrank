package algorithms.dynamicProgramming.aSuperHero;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {   
    
    private static class Pair<A extends Comparable<? super A>, B extends Comparable<? super B>> implements Comparable<Pair<A, B>> {

        public final A first;
        public final B second;

        private Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public static <A extends Comparable<? super A>,
                        B extends Comparable<? super B>>
                Pair<A, B> of(A first, B second) {
            return new Pair<A, B>(first, second);
        }

        @Override
        public int compareTo(Pair<A, B> o) {
            int cmp = o == null ? 1 : (this.first).compareTo(o.first);
            return cmp == 0 ? (this.second).compareTo(o.second) : cmp;
        }

        @Override
        public int hashCode() {
            return 31 * hashcode(first) + hashcode(second);
        }

        // TODO : move this to a helper class.
        private static int hashcode(Object o) {
            return o == null ? 0 : o.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Pair))
                return false;
            if (this == obj)
                return true;
            return equal(first, ((Pair<?, ?>) obj).first)
                    && equal(second, ((Pair<?, ?>) obj).second);
        }

        // TODO : move this to a helper class.
        private boolean equal(Object o1, Object o2) {
            return o1 == o2 || (o1 != null && o1.equals(o2));
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ')';
        }        
    }

    private static final int inf = 10000000;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tMax = scanner.nextInt();
        for ( int tCount = 0; tCount < tMax; tCount++ ) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            scanner.nextLine();
            int[][] P = new int[n][m];
            int[][] MW = new int[n][m];
            for ( int i = 0; i < n; i++ ) {
                for ( int j = 0; j < m; j++ ) {
                    P[i][j] = scanner.nextInt();
                    MW[i][j] = inf;
                }
            }
            int[][] W = new int[n][m];
            for ( int i = 0; i < n; i++ ) {
                for ( int j = 0; j < m; j++ ) {
                    W[i][j] = scanner.nextInt();
                }
            }
            int ans;
            ArrayList<Pair<Integer, Integer>> MS = new ArrayList();
            for ( int i = 0; i < m; i++ ) {
                MW[0][i]=P[0][i];
                MS.add( Pair.of( W[0][i], MW[0][i] ) );
            }
            Collections.sort(MS);
            
            for ( int i = 1 ; i < n; i++ ) {
                ArrayList<Pair<Integer, Integer>> t = new ArrayList();
                for ( int j = 0; j < m; j++ ) {
                    t.add( Pair.of( P[i][j], j ) );
                }
                Collections.sort(t);
                
                int k = 0, l = 0, maxx = inf;
                while ( l < m ) {
                    while ( k < m && MS.get(k).first <= t.get(l).first ) {
                        maxx = Math.min( maxx, MS.get(k).second - MS.get(k).first );                    
                        k++; 
                    }
                    MW[i][ t.get(l).second ] = Math.min( MW[i][ t.get(l).second], maxx + P[i][ t.get(l).second] );
                    l++;
                }
                k = m - 1;
                l = m - 1;
                maxx = inf;
                while ( l >=0 ) {            
                    while ( k >= 0 && MS.get(k).first >= t.get(l).first ) {
                        maxx = Math.min( maxx, MS.get(k).second );
                        k--;
                    }
                    MW[i][ t.get(l).second ] = Math.min( MW[i][ t.get(l).second], maxx );
                    l--;            
                }
                MS.clear();
        
                for ( int j = 0; j < m; j++ ) {
                    MS.add( Pair.of( W[i][j], MW[i][j] ) );
                }
                Collections.sort(MS);
            }
            ans = inf;
            for ( int i = 0; i < m; i++ ) {
                ans = Math.min( ans, MW[n - 1][i] );
            }
            System.out.println(ans);
        }        
    }
}
