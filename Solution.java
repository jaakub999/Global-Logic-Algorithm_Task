import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Solution {
    public void solution(String A, String B) {
        int appeared;
        int total;
        double frequency;
        BigDecimal bd;
        Set<Character> set;
        String[] words;
        Integer[] counters;
        Object[][] array;

        words = A.split(" ");
        set = stringToSet(B);
        counters = countLetters(set, words);
        appeared = counters[0];
        total = counters[1];
        array = resultsToArray(set, words, appeared);
        bubbleSort(array);
        frequency = (double) appeared / (double) total;
        bd = BigDecimal.valueOf(frequency).setScale(2, RoundingMode.HALF_UP);
        writeToFile(array, bd);

        //output
        for (Object[] objects : array)
            System.out.println("{" + objects[0] + ", " + objects[1] + "}" + " = " + objects[2] + " (" + objects[3] + "/" + appeared + ")");

        System.out.println("TOTAL Frequency: " + bd + " (" + appeared + "/" + total + ")");
    }

    private Set<Character> stringToSet(String str) {
        int size = str.length();
        Set<Character> set = new HashSet<>();

        str = str.toLowerCase();

        for (var i = 0; i < size; i++)
            set.add(str.charAt(i));

        return set;
    }

    private Integer[] countLetters(Set<Character> set, String[] words) {
        int x = 0;
        int y = 0;
        int size = words.length;
        String regex = "[^a-zA-Z0-9]";

        for (var i = 0; i < size; i++) {
            words[i] = words[i].replaceAll(regex, "").toLowerCase();
            var wordLength = words[i].length();
            y += wordLength;

            for (var j = 0; j < wordLength; j++) {
                if (set.contains(words[i].charAt(j))) {
                    x++;
                }
            }
        }

        return new Integer[]{x, y};
    }

    private Object[][] resultsToArray(Set<Character> set, String[] words, int appeared) {
        int size = words.length;
        Set<Character> setA = new HashSet<>();
        Object[][] array = new Object[size][];
        Object[] temp;

        for (var i = 0; i < size; i++) {
            var wordLength = words[i].length();
            var quantity = 0;

            for (var j = 0; j < wordLength; j++) {
                if (set.contains(words[i].charAt(j))) {
                    setA.add(words[i].charAt(j));
                    quantity++;
                }
            }

            var frequency = (double) quantity / (double) appeared;
            var bd = BigDecimal.valueOf(frequency).setScale(2, RoundingMode.HALF_UP);
            temp = new Object[]{setA.toString(), wordLength, bd.doubleValue(), quantity};
            array[i] = temp;
            setA.clear();
        }

        return array;
    }

    private void bubbleSort(Object[][] array) {
        int size = array.length;
        Object[] temp;

        for (var i = 0; i < size; i++) {
            for (var j = 1; j < size - i; j++) {
                if ((double) array[j - 1][2] > (double) array[j][2]) {
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    private void writeToFile(Object[][] array, BigDecimal bd) {
        try (PrintWriter writer = new PrintWriter(new File("results.csv"))) {
            StringBuilder sb = new StringBuilder();
            String semicolon = ";";
            sb.append("letters");
            sb.append(semicolon);
            sb.append("word length");
            sb.append(semicolon);
            sb.append("frequency");
            sb.append("\n");

            for (Object[] objects : array) {
                sb.append(objects[0]);
                sb.append(semicolon);
                sb.append(objects[1]);
                sb.append(semicolon);
                sb.append(objects[2]);
                sb.append("\n");
            }

            sb.append("\nTOTAL Frequency");
            sb.append(semicolon);
            sb.append(bd);

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}