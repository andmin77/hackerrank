package algorithms.implementation.theBombermanGame;

import java.util.*;

public class Solution {

    private static void setgrid(char[][] ngrid, int r, int c, int i, int j, char ch) {
        if ( 0 <= i && i < r && 0 <= j && j < c ) {
            ngrid[i][j] = ch;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] rcn = scanner.nextLine().split(" ");
        int r = Integer.parseInt(rcn[0]);
        int c = Integer.parseInt(rcn[1]);
        int n = Integer.parseInt(rcn[2]);
        
        char[][] grid = new char[r][c];
        for ( int row = 0; row < r; row++ ) {
            String gridItem = scanner.nextLine();
            for ( int col = 0; col < c; col++ ) {
                grid[row][col] = gridItem.charAt(col);
            }
        }
        
        if ( n % 2 == 0 ) {
            for ( int row = 0; row < r; row++ ) {
                for ( int col = 0; col < c; col++ ) {
                    grid[row][col] = 'O';
                }
            }
        } else {            
            n = n / 2;
            int min = Math.min( n, (n + 1) % 2 + 1 );
            for ( int it = 0; it < min; it++ ) {
                char[][] ngrid = new char[r][c];
                for ( int row = 0; row < r; row++ ) {
                    for ( int col = 0; col < c; col++ ) {
                        ngrid[row][col] = 'O';
                    }
                }                
                for ( int row = 0; row < r; row++ ) {
                    for ( int col = 0; col < c; col++ ) {
                        if ( grid[row][col] == 'O' ) {
                            setgrid(ngrid, r, c, row, col, '.');
                            setgrid(ngrid, r, c, row + 1, col, '.');
                            setgrid(ngrid, r, c, row - 1, col, '.');
                            setgrid(ngrid, r, c, row, col + 1, '.');
                            setgrid(ngrid, r, c, row, col - 1, '.');
                        }
                    }
                }
                for ( int row = 0; row < r; row++ ) {
                    for ( int col = 0; col < c; col++ ) {
                        grid[row][col] = ngrid[row][col];
                    }
                }
            }
        }
        
        for ( int row = 0; row < r; row++ ) {
            for ( int col = 0; col < c; col++ ) {
                System.out.print("" + grid[row][col]);
            }
            System.out.println("");
        }        
    }
}
