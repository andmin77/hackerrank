package algorithms.dynamicProgramming.unfairGame;

import java.util.*;

/**
 *
 * @author muoi
 */
public class SolutionGr17 {

    public static long MAX_BIT = 32;
    public static long n;
    static long a[] = new long[100];
    static long b[] = new long[100];
    static long c[] = new long[100];
    static long dd[] = new long[100];

    static long bitCheck(long a, long b) {
        return ((a) & (1l << (b)));
    }

    static long bitSet(long a, long b) {
        return ((1l << (b)));
    }

    static long check() {
        long res = 0l;


        Arrays.fill(c, 0);


        long sl = 0;

        for (long bb = MAX_BIT; bb >= 0; --bb) {
            sl = 0;
            Arrays.fill(dd, 0l);
            for (int i = 0; i < n; ++i) {
                if (bitCheck(a[i], bb) != 0l) {
                    if (c[i] <= a[i]) {

                        sl++;
                        c[i] |= bitSet(c[i], bb);
                        dd[i] = 1;

                        if (sl > b[(int)bb]) {
                            return -1;

                        }
                    }
                }
            }

            for (long ii = 0; ii < b[(int)bb] - sl; ++ii) {
                long mn = Long.MAX_VALUE;
                long pos = 0;
                for (long j = 0; j < n; ++j) {
                    if (dd[(int)j] == 0) {
                        long temp = c[(int)j];
                        temp |= bitSet(temp, bb);

                        if (temp - a[(int)j] < mn) {
                            mn = temp - a[(int)j];
                            pos = j;
                        }
                    }
                }

                dd[(int)pos] = 1l;
                c[(int)pos] |= bitSet(c[(int)pos], bb);
            }

        }

        for (long i = 0; i < n; ++i) {

            res += (c[(int)i] - a[(int)i]);
        }

        return res;
    }

    static void process() {

        if (n % 2 == 0) {
            Arrays.fill(b, n);
        } else {
            Arrays.fill(b, n - 1);
        }

        long mn = Integer.MAX_VALUE;

        for (long bb = MAX_BIT; bb >= 0; --bb) {

            while (true) {

                long temp = check();
                if (temp != -1) {

                    if (b[(int)bb] > 0) {
                        b[(int)bb] -= 2;
                    } else {
                        break;
                    }

                } else {
                    b[(int)bb] += 2;
                    break;
                }
            }

            long temp = check();

            if (temp != -1) {
                if (temp < mn) {
                    mn = temp;
                }
            }

        }

        System.out.println(mn);

    }

    public static void main(String args[]) {

        long ntest;
        Scanner scanner = new Scanner(System.in);
        ntest = scanner.nextLong();
        for (; ntest-- > 0;) {
            n = scanner.nextLong();
            for (long i = 0; i < n; ++i) {
                a[(int)i] = scanner.nextLong();
            }
            process();
        }


    }
}
