package algorithms.dynamicProgramming.separateTheChocolate;

import java.util.*;

public class Solution {
    
    private static class Node implements Comparable<Node> {
        int  num;   // black - white
        char[] a = new char[9];   //the number of the grid even-white odd-black
        char no;    //the forbideen color the 0-white 1-black 2-both can
        char vwb;   //the valid color 0-white 1-black 2-both 3-neither
        char dwb;   //0-dead white (Never can appear a white grid) 1-dead black 3-neither dead

        @Override
        public int compareTo(Node o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int M = scanner.nextInt();
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        for ( int r = 0; r < M; r++ ) {
            String line = scanner.nextLine();
            
            
        }
    }
}
