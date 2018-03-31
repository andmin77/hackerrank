package com.andmin77.hackerrank;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class EntryPoint {

    public static void main(String[] args) throws FileNotFoundException, IOException  {
        System.err.println( Arrays.toString(args) );
        String className = args[0];
        List<String> filtersKey = new ArrayList<>();
        if ( args.length > 1 ) {
            for ( int index = 1; index < args.length; index++ ) {
                filtersKey.add(args[index]);
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
        PrintStream console = System.out;
        for ( String key : input.keySet() ) {
            if ( filtersKey.size() == 0 || filtersKey.contains(key) ) {
                File inputFile = input.get(key);
                File outputFile = output.get(key);                        

                if ( outputFile != null ) {
                    System.err.println( "********* Execute Test Case " + key + " *********" );

                    System.setIn(new FileInputStream(inputFile.getAbsolutePath()) );
                    List<String> outputExpectedLines = Files.readAllLines(outputFile.toPath(), Charset.defaultCharset());
                    String consoleFilename = System.getProperty("java.io.tmpdir") + "/" + System.currentTimeMillis() + ".txt";
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

                    fos.close();
                    System.setOut(console);
                    List<String> outputRealLines = Files.readAllLines(consoleFile.toPath(), Charset.defaultCharset());
                    consoleFile.delete();

                    int failed = 0;
                    if ( outputExpectedLines.size() == outputRealLines.size() ) {
                        for ( int index = 0; index < outputExpectedLines.size(); index++ ) {
                            if ( !outputExpectedLines.get(index).equals( outputRealLines.get(index) ) ) {
                                failed++;
                                print("_", 20);
                                System.err.println("[" + index + "] ");
                                System.err.println( "" + outputExpectedLines.get(index) );
                                System.err.println( "" + outputRealLines.get(index) );
                                print("_", 20);
                            }
                        }
                    } else {
                        System.err.println("Failed: expected = " + outputExpectedLines.size() + ", real = " + outputRealLines.size());
                    }
                    if ( failed > 0 ) {
                        System.err.println("lines failed = " + failed);
                    }
                    print("*", 40);
                }
            }
        }       
    }
    
    
    private static void print(String s, int n) {
        System.err.println( String.format("%0" + n + "d", 0).replace("0", s) );
    }
}