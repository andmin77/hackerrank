package algorithms.strings.sherlockandAnagrams;

import java.util.*;

public class Solution {
    
    public static void main(String[] args) {            
        Scanner scanner = new Scanner(System.in);
        int q = scanner.nextInt();
        for ( int t = 0; t < q; t++ ) {
            String s = scanner.next();
            int length = s.length();
            Map<String, Integer> map = new HashMap<>();
            for ( int beginIndex = 0; beginIndex < length; beginIndex++ ) {
                for ( int endIndex = beginIndex + 1; endIndex < length + 1; endIndex++ ) {
                    char[] array = s.substring(beginIndex, endIndex).toCharArray();
                    Arrays.sort(array);
                    String word = new String( array );
                    
                    if ( map.get( word ) == null ) {
                        map.put( word, 0 );
                    }
                    map.put( word, map.get( word ) + 1 );
                }
            }
            int sum = 0;
            for ( String key : map.keySet() ) {
                int count = map.get(key);
                sum += ( count * ( count - 1 ) ) / 2;
            }
            System.out.println("" + sum);
        }        
    }
}