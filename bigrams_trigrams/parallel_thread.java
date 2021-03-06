package bigrams_trigrams;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class parallel_thread implements Callable<ConcurrentHashMap<String, Integer>> {

    private int n;
    private double start, stop;
    private String id;
    private ConcurrentHashMap<String, Integer> n_grams;
    private char[] file;

    StringBuilder builder;

    public parallel_thread(String id, double start, double stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.file = file;
        this.n_grams = new ConcurrentHashMap();

    }

    public ConcurrentHashMap<String, Integer> call() {

        if (stop > file.length-1){
            stop = file.length-1;
        }

        for (double i = this.start + n - 1; i <= this.stop; i++) {

            builder = new StringBuilder();      

            for (double j = n - 1; j >= 0; j--) {
                builder.append(this.file[(int)(i - j)]);
            }

            if (this.n == 2 || this.n == 3) {

                String key = builder.toString();

                if(!this.n_grams.containsKey(key)){
                    this.n_grams.put(builder.toString(), 1);
                }
                else{
                    if(this.n_grams.containsKey(key)){
                        this.n_grams.put(builder.toString(), this.n_grams.get(key) + 1);
                    }
                }
            }
            else {
                System.out.println("n is an invalid number");
            }
        }
        return n_grams;
    }
}
