package algorithms.implementation.emasSupercomputer;

import java.util.*;

public class Solution {
   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] rcn = scanner.nextLine().split(" ");
        int n = Integer.parseInt(rcn[0]);
        int m = Integer.parseInt(rcn[1]);
        char[][] grid = new char[20][20];
        for ( int row = 1; row <= n; row++ ) {
            String gridItem = scanner.nextLine();
            for ( int col = 1; col <= m; col++ ) {
                grid[row][col] = gridItem.charAt(col - 1);
            }
        }
        int result = 0;
        for (int row = 1; row <= n; row++) {
            for (int col = 1;col <= m; col++){
                int r = 0;
                while (grid[row + r][col] == 'G' && grid[row - r][col] == 'G' &&
                       grid[row][col + r] == 'G' && grid[row][col - r] == 'G') {
                    grid[row + r][col] = grid[row - r][col] = grid[row][col + r] = grid[row][col - r] = 'g';
                    for (int X = 1; X <= n; X++) {
                        for (int Y = 1; Y <= m; Y++){
                            int R = 0;
                            while (grid[X + R][Y] == 'G' && grid[X - R][Y] == 'G' && 
                                    grid[X][Y + R] == 'G' && grid[X][Y - R] == 'G') {
                                result = Math.max(result, (1 + 4 * r) * (1 + 4 * R));
                                R++;
                            }
                        }
                    }
                    r++;
                }
                r = 0;
                while (grid[row + r][col] == 'g' && grid[row - r][col] == 'g' && 
                        grid[row][col + r] == 'g' && grid[row][col - r] == 'g') {
                    grid[row + r][col] = grid[row - r][col] = grid[row][col + r] = grid[row][col - r] = 'G';
                    r++;
                }
            }
        }
        
        System.out.println("" + result );
    }
}
