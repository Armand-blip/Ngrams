package bigrams_trigrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class parallel_main {
	
	// Merge n_grams in finalNgrams
    public static ConcurrentHashMap<String, Integer> HashMerge(ConcurrentHashMap<String, Integer> n_grams, ConcurrentHashMap<String, Integer> finalNgrams) {
        for (ConcurrentHashMap.Entry<String, Integer> entry : n_grams.entrySet()) {
            int newValue = entry.getValue();
            Integer existingValue = finalNgrams.get(entry.getKey());
            if (existingValue != null) {
                newValue = newValue + existingValue;
            }
            finalNgrams.put(entry.getKey(), newValue);
        }
        return finalNgrams;
    }
    // Read from the text and remove special characters ( ();:,.& ).
    public static char[] readTextFromFile() {
        Path path = Paths.get("/Users/Armando/eclipse-workspace/bigrams_trigrams/src/1-text50KB.txt");

        try {
            Stream<String> lines = Files.lines(path);
            char[] file = (lines.collect(Collectors.joining())).replaceAll("[ '&();:,.]", "").toCharArray();

            for(int i = 0; i < file.length - 1; ++i) {
                if (Character.isUpperCase(file[i])) {
                    file[i] = Character.toLowerCase(file[i]);
                }
            }

            return file;

        }

        catch (IOException e) {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    public static void main(String args[]) {

        char[] fileString = readTextFromFile();

        int fileLen = fileString.length;
        //int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = 2;

        ConcurrentHashMap<String, Integer> finalNgrams = new ConcurrentHashMap(); // Create ConcurrentHashMap containing <gram, value>

        ArrayList<Future> futuresArray = new ArrayList<>(); // Create an ArrayList of Future that will contain the threads results

        ExecutorService executor = Executors.newFixedThreadPool(realThreads); // create the threads

        long start, end;
        start = System.currentTimeMillis();

        int n = 3; //n grams
        double k = Math.floor(fileLen/realThreads);// divide text according to number of threads

        for (int i = 0; i < realThreads; i++) {

            Future f = executor.submit(new parallel_thread("t" + i, i * k, ((i+1) * k) + (n - 1) - 1, n, fileString));//executor.submit start the Callable call that save the results in a Future
            futuresArray.add(f);

        }

        try{
            for (Future <ConcurrentHashMap<String, Integer>> f : futuresArray) {
                ConcurrentHashMap<String, Integer> n_grams = f.get(); // f.get return the thread result
                HashMerge(n_grams,finalNgrams);
            }
            System.out.println(finalNgrams);
            awaitTerminationAfterShutdown(executor);

            end = System.currentTimeMillis();

            System.out.println(n + "-grams calculated in " + (end - start) + " ms");

        }

        catch (Exception e){
            System.out.println(e);
        }

    }//public static void main

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}//public class parallel_main
