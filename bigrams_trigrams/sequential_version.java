package bigrams_trigrams;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.HashMap;
public class sequential_version {

	public static char[] readText() {
        Path path = Paths.get("/Users/Armando/eclipse-workspace/bigrams_trigrams/src/1-text50KB.txt");

        try {
        	//Read all rows from a file as a Stream. 
            Stream<String> rows = Files.lines(path);
            
            // Returns a Collector that concatenates the input elements into a String and converts this string to a new character array.
            char[] file = (rows.collect(Collectors.joining())).replaceAll("[ ;:&'(),.]", "").toCharArray();

            for(int i = 0; i < file.length - 1; ++i) {
                if (Character.isUpperCase(file[i])) {
                    file[i] = Character.toLowerCase(file[i]);
                }
            }
            return file;
        }

        catch (IOException e) {
            System.out.println(e);
            System.exit(1); //bad command-line, can't find file, unsuccessful termination.
            return null;
        }
    } // public static char[]
	
	 public static HashMap<String, Integer> ngrams(int n, char[] file) {
        HashMap<String, Integer> hashMap = new HashMap();

        for(int i = 0; i < file.length - n + 1; ++i) {
        	
        	//A string buffer is like a String, but can be modified.String buffers are safe for use by multiple threads.
            StringBuffer buffer = new StringBuffer();
            //StringBuilder builder = new StringBuilder();
            for(int j = 0; j < n; ++j) {
                buffer.append(file[i + j]);
            }

            String key = buffer.toString();
            //Control if the key is added one time in the hashMap
            if (!hashMap.containsKey(key)) {
                hashMap.put(key, 1);
            }
            //if it is added before increment the value with one
            else if (hashMap.containsKey(key)) {
                hashMap.put(key, hashMap.get(key) + 1);
            }
        }

        return hashMap;
        
    }//public static HashMap <String, Integer> ngrams
	 public static void main(String[] args) {

         char[] file = readText();
         long start, end;

         start = System.currentTimeMillis();
         HashMap hmap = ngrams(3, file);
         end = System.currentTimeMillis();
         
         System.out.println("Time in milliseconds is: " + (end - start) + " milliseconds.");
         System.out.println("Elements of the HashMap are: " + hmap);
 } //public static void main
} // public class sequential_version
