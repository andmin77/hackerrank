package algorithms.strings.bearAndSteadyGene;

import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String s = scanner.next();
        char[] gene = s.toCharArray();
        char[] chars = { 'A', 'C', 'G', 'T' };
        int max = gene.length / 4;
        Map<Character, Integer> occourrences = new HashMap<Character, Integer>() {
            {
                put('A', 0);
                put('C', 0);
                put('G', 0);
                put('T', 0);
            }
        };
        for ( int index = 0; index < gene.length; index++ ) {
            occourrences.put( gene[index], occourrences.get(gene[index]) + 1 );
        }
        int count = 0;
        for ( char ch : occourrences.keySet() ) {
            if ( occourrences.get(ch) == max ) {
                count++;
            }
        }
        if ( count == 4 ) {
            System.out.println("0");
        } else {
            int infIndex = 0, supIndex = gene.length;
            while (supIndex - infIndex > 1) {
                int middleIndex = (infIndex + supIndex) / 2;
                Map<Character, Integer> occourrencesTmp = new HashMap<>( occourrences );
                for ( int index = 0; index < middleIndex; index++ ) {
                    occourrencesTmp.put( gene[index], occourrencesTmp.get(gene[index]) - 1 );
                }
                count = 0;
                for ( char ch : occourrencesTmp.keySet() ) {
                    if ( occourrences.get(ch) <= max ) {
                        count++;
                    }
                }
                if ( count == 4 ) {
                    supIndex = middleIndex;
                } else {
                    boolean exit = false;
                    for (int index = middleIndex; index < n && !exit; index++) {
                        occourrencesTmp.put( gene[index - middleIndex], occourrencesTmp.get(gene[index - middleIndex]) + 1 );
                        occourrencesTmp.put( gene[index], occourrencesTmp.get(gene[index]) - 1 );
				
                        count = 0;
                        for ( char ch : occourrencesTmp.keySet() ) {
                            if ( occourrences.get(ch) <= max ) {
                                count++;
                            }
                        }
                        if ( count == 4 ) {
                            supIndex = middleIndex;
                            exit = true;
                        }
                    }
                    if ( !exit ) {
                        infIndex = middleIndex;
                    }
                }
            }
            System.out.println("" + supIndex);
        }
    }
}