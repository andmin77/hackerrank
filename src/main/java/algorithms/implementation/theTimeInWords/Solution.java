package algorithms.implementation.theTimeInWords;

import java.util.*;

public class Solution {
    
    private final static Map<Integer, String> numbers = new TreeMap<Integer, String>() {
        {
            put(0, "zero");
            put(1, "one");
            put(2, "two");
            put(3, "three");
            put(4, "four");
            put(5, "five");
            put(6, "six");
            put(7, "seven");
            put(8, "eight");
            put(9, "nine");
            put(10, "ten");
            put(11, "eleven");
            put(12, "twelve");
            put(13, "thirteen");
            put(20, "twenty");
            put(30, "thirty");
            put(40, "forty");
            put(50, "fifty");
        }
    };
    
    private static final String O_CLOCK_WORD = "o' clock";
    private static final String PAST_WORD = "past";
    private static final String TO_WORD = "to";
    private static final String MINUTE_WORD = "minute";
    private static final String MINUTES_WORD = "minutes";
    private static final String HALF_WORD = "half";
    private static final String QUARTER_WORD = "quarter";
    
    private static String numberToWord(int n) {
        String word = numbers.get(n);
        
        if ( word == null ) {
            int D = 10 * ( n / 10 );
            int U = n - D;
            word = numbers.get(D) + " " + numbers.get(U);
        }
        
        return word;
    }
    
    public static void main(String[] args) {            
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        int m = scanner.nextInt();
        
        String result = "";
        if ( m == 0) {
            result = numberToWord(h) + " " + O_CLOCK_WORD;
        } else if ( m > 0 && m <= 30) {
            switch ( m ) {
                case 15:
                    result = QUARTER_WORD + " " + PAST_WORD + " " + numberToWord(h);
                    break;
                case 30:
                    result = HALF_WORD + " " + PAST_WORD + " " + numberToWord(h);
                    break;
                default:
                    if ( m != 1 ) {
                        result = numberToWord(m) + " " + MINUTES_WORD + " " + PAST_WORD + " " + numberToWord(h);
                    } else {
                        result = numberToWord(m) + " " + MINUTE_WORD + " " + PAST_WORD + " " + numberToWord(h);
                    }
                    break;
            }
        } else {
            if ( h < 23 ) {
                h++;
            } else {
                h = 0;
            }
            if ( m == 45 ) {
                result = QUARTER_WORD + " " + TO_WORD + " " + numberToWord(h);
            } else {
                if ( 60 - m != 1 ) {
                    result = numberToWord(60 - m) + " " + MINUTES_WORD + " " + TO_WORD + " " + numberToWord(h);
                } else {
                    result = numberToWord(60 - m) + " " + MINUTE_WORD + " " + TO_WORD + " " + numberToWord(h);
                }
            }
        }
        
        System.out.println( result );
    }
}