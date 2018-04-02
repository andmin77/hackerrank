package algorithms.dynamicProgramming.divisibleNumbers;

import java.util.*;

public class Solution {

    private static final int maxS = 75;
    private static final int maxN = 30000;
    private static final int[][][] d = new int[maxS][maxS][maxN];

    private static int clever(int n) {
        int res = -1;
        if (n % 10 != 0) {
            final int inf = 250;
            for (int i = 0; i < maxS; ++i) {
                for (int j = 0; j < maxS; ++j) {
                    for (int k = 0; k < maxN; ++k) {
                        d[i][j][k] = inf;
                    }
                }
            }
            d[0][1][0] = 0;
            for (int i = 0; i < maxS; ++i) {
                for (int j = 0; j < maxS; ++j) {
                    for (int k = 0; k < n; ++k) {
                        if (d[i][j][k] != inf) {
                            for (int digit = 1; digit < 10; ++digit) {
                                if (i + digit < maxS && j * digit < maxS) {
                                    int l = (k * 10 + digit) % n;
                                    d[i + digit][j * digit][l] = Math.min( d[i + digit][j * digit][l], d[i][j][k] + 1 );
                                }
                            }
                        }
                    }
                }
            }
            res = 1000000;
            for (int i = 1; i < maxS; ++i) {
                for (int j = 0; j <= i; ++j) {
                    if (d[i][j][0] != inf) {
                        res = Math.min( res, d[i][j][0] );
                    }
                }
            }
        }
        return res;
    }
    
    private static boolean add(int value, int[] digits) {
        digits[0] += value;
        for (int i = 0; i + 1 < digits.length && digits[i] >= 10; ++i) {
            digits[i + 1] += digits[i] / 10;
            digits[i] %= 10;
        }
        return digits[digits.length - 1] < 10;
    }

    private static int getSum(int[] digits) {
        int res = 0;
        final int significant_digits = Math.min( digits.length, 30);
        for (int i = 0; i < significant_digits; ++i) {
            res += digits[i];
        }
        res += digits.length - significant_digits;
        return res;
    }
    
    private static int getProduct(int[] digits) {
        int res = 1;
        final int significant_digits = Math.min( digits.length, 30 );
        final int inf = 1000000;
        for (int i = 0; i < significant_digits && res < inf; ++i) {
            res *= digits[i];
            if (res > inf) {
                res = inf;
            }
        }
        return res;
    }
    
    private static boolean build(int n, int length, int[] digits) {
        boolean result = false;
        int rem = 0;
        for (int i = length - 1; i >= 0; --i) {
            rem = rem * 10 + digits[i];
            rem %= n;
        }
        if ( add((n - rem) % n, digits) ) {
            final int max_iterations = n;
            boolean exit = false;
            for ( int iterations = 0; iterations < max_iterations && !exit; ++iterations) {
                int sum = getSum(digits);
                int product = getProduct(digits);
                if (product == 0 || sum < product) {
                    if (!add(n, digits)) {
                        exit = true;
                    }
                } else {
                    result = true;
                    exit = true;
                }
            }
        }
        return result;
    }

    private static boolean try_to_build(int n, int length) {
        int[] digits = new int[length];
        Arrays.fill(digits, 1);
        return build(n, length, digits);
    }
    
    private static int solve(int n) {
        int result = -1;
        if (n % 10 != 0) {
            int starting_length = 60;
            int length = starting_length;
            boolean exit = false;
            while ( !exit ) {
                if (try_to_build(n, length)) {
                    result = length;
                    exit = true;
                }
                ++length;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int res = clever(n);
        if (res == 1000000) {
            res = solve(n);
        }
        System.out.println(res);
    }
}
