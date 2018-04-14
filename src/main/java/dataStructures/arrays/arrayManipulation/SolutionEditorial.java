package dataStructures.arrays.arrayManipulation;

import java.io.*;
import java.util.*;

public class SolutionEditorial {

    private static class PairIntLong implements Comparable<PairIntLong> {
        final int first;
        final long second;
        
        public PairIntLong(int first, long second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(PairIntLong o) {
            if ( this.first > o.first ) return 1;
            if ( this.first < o.first ) return -1;
            if ( this.second > o.second ) return 1;
            if ( this.second < o.second ) return -1;
            return 0;
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        ArrayList<PairIntLong> v = new ArrayList<>();
        
        for ( int i = 0 ; i < m; i++ ) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            long k = scanner.nextLong();
            
            v.add( new PairIntLong(a, k) );
            v.add( new PairIntLong(b + 1, -k) );
        }
        
        long sum = 0;
        long max = 0;
        Collections.sort(v);
        for ( PairIntLong e : v ) {
            sum += e.second;
            if ( sum > max) {
                max = sum;
            }
        }
        
        System.out.println(max);
    }
}
