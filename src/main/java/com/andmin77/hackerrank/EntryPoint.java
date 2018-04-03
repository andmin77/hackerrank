package com.andmin77.hackerrank;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class EntryPoint {
    
    private static final String SFORMAT = "|%1$-10s|%2$-20s|%3$-20s|";

    public static void main(String[] args) throws FileNotFoundException, IOException  {
        System.err.println( Arrays.toString(args) );
        boolean printDetails = false;
        String className = args[0];
        List<String> filtersKey = new ArrayList<>();
        if ( args.length > 1 ) {
            for ( int index = 1; index < args.length; index++ ) {
                if ( args[index].equals("printDetails") ) {
                    printDetails = true;
                } else {
                    filtersKey.add(args[index]);
                }                
            }
        }
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        String packageName = clazz.getPackage().getName();
        Method method = null;
        try {
            method = clazz.getMethod("main", String[].class);
            method.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        File testCasesDirectory = new File("src/main/resources/" + packageName.replaceAll("\\.", "/"));
        if ( !testCasesDirectory.exists() ) {
            System.err.println("'" + testCasesDirectory.getAbsolutePath() + "' not exists!");
            System.exit(1);
        }
        
        File[] files = testCasesDirectory.listFiles();
        if ( files.length == 0 ) {
            System.err.println("Test Case files not found in directory '" + testCasesDirectory.getAbsolutePath() + "'");
            System.exit(1);
        }
        
        Map<String, File> input = new TreeMap();
        Map<String, File> output = new TreeMap();
        
        for ( File file : files ) {
            if ( file.getName().startsWith("input") && file.getName().endsWith(".txt") ) {
                String key = file.getName().replace("input", "").replace(".txt", "");
                input.put(key, file);
            }
            if ( file.getName().startsWith("output") && file.getName().endsWith(".txt") ) {
                String key = file.getName().replace("output", "").replace(".txt", "");
                output.put(key, file);
            }
        }
        List<String> testCaseFailed = new ArrayList<>();
        PrintStream console = System.out;
        long maxDelay = 0;
        String testCaseWithMaxDelay = null;
        for ( String key : input.keySet() ) {
            if ( filtersKey.size() == 0 || filtersKey.contains(key) ) {
                File inputFile = input.get(key);
                File outputFile = output.get(key);
                if ( outputFile != null ) {
                    printLeft("*", 100, 10, "Start Execute Test Case " + key);
                    //print("*", 50, "Start Execute Test Case " + key);
                    long t1 = System.currentTimeMillis();
                    System.setIn(new FileInputStream(inputFile.getAbsolutePath()) );
                    List<String> outputExpectedLines = Files.readAllLines(outputFile.toPath(), Charset.defaultCharset());
                    String consoleFilename = System.getProperty("java.io.tmpdir") + UUID.randomUUID() + System.currentTimeMillis() + ".txt";
                    File consoleFile = new File(consoleFilename);
                    FileOutputStream fos = new FileOutputStream(consoleFile);
                    System.setOut(new PrintStream(fos));

                    String[] params = null;
                    try {
                        method.invoke(null, (Object) params);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                    fos.flush();
                    fos.close();
                    System.setOut(console);
                    List<String> outputRealLines = Files.readAllLines(consoleFile.toPath(), Charset.defaultCharset());

                    int failed = 0;
                    if ( outputExpectedLines.size() == outputRealLines.size() ) {
                        for ( int index = 0; index < outputExpectedLines.size(); index++ ) {
                            if ( !outputExpectedLines.get(index).equals( outputRealLines.get(index) ) ) {
                                failed++;
                                if ( printDetails) {
                                    if ( failed == 1 ) {
                                        print("-", 54);
                                        System.err.println( String.format(SFORMAT, "Row", "Your Output", "Expected Output") );
                                        print("-", 54);
                                    }
                                    System.err.println( String.format(SFORMAT, index, outputRealLines.get(index), outputExpectedLines.get(index)) );
                                }
                            }
                        }
                    } else {
                        System.err.println("Failed: expected = " + outputExpectedLines.size() + ", real = " + outputRealLines.size());
                    }
                    if ( failed == 0 ) {
                        System.err.println("OK");
                    } else {
                        testCaseFailed.add(key);
                        System.err.println("lines failed = " + failed);
                    }
                    long t2 = System.currentTimeMillis();
                    long delay = t2 - t1;
                    if ( delay > maxDelay) {
                        maxDelay = delay;
                        testCaseWithMaxDelay = key;
                    }
                    printLeft("*", 100, 10, "End Execute Test Case " + key + ": " + delay + " [mills]");
                    //print("*", 50, "End Execute Test Case " + key + ": " + delay + " [mills]");
                    print("-", 100);
                }
            }
        }
        print("_", 100);
        if ( testCaseFailed.size() == 0 ) {
            print("*", 100, "ALL TEST CASE SUCCESS!");
        } else {
            System.err.println(testCaseFailed.size() + " TEST CASE FAILED: " + Arrays.toString(testCaseFailed.toArray()) );
        }
        if ( testCaseWithMaxDelay != null ) {
            print("*", 100, "maxDelay = " + maxDelay + " [mills] in testCase: " + testCaseWithMaxDelay);
        }
        print("_", 100);
    }
    
    private static void printLeft(String s, int n, int left, String message) {
        int right = n - 2 - left - message.length();
        if ( right < 0 ) {
            right = 0;
        }
        System.err.println( String.format("%0" + left + "d", 0).replace("0", s) + " " + message + " " + String.format("%0" + right + "d", 0).replace("0", s));
    }
    private static void print(String s, int n, String message) {
        int left = 0;
        int right = 0;
        if ( n > message.length() ) {
            left = ( n - message.length() - 2 ) / 2;
            right = left;
            if ( message.length() % 2 == 1 ) {
                right++;
            }
        }
        
        System.err.println( String.format("%0" + left + "d", 0).replace("0", s) + " " + message + " " + String.format("%0" + right + "d", 0).replace("0", s));
    }
    private static void print(String s, int n) {
        System.err.println( String.format("%0" + n + "d", 0).replace("0", s) );
    }
}