
import java.util.*;

// (Question 2) Write code to compute (x - a1)(x - a2) ... (x - an) for n given numbers, in O(n^2) time.

public class Problem2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String numbers = " ";

        while (numbers.equals(" ")) {
            System.out.println("Enter n numbers: ");
            numbers = in.nextLine();
        }
        String[] numbersArr = numbers.split(" ");
        HashMap<Double, Double> polyMap = new HashMap<>();
        HashMap<Double, Double> newPolyMap = new HashMap<>();
        polyMap.put(1.0, 1.0);
        polyMap.put(0.0, -1 * Double.parseDouble(numbersArr[0]));

        Boolean timeToBreak = false;
        for (int j = 1; j < numbersArr.length; j++) {
                Double next = Double.parseDouble(numbersArr[j]);
                HashMap<Double, Double> nextPolyMap = new HashMap<>();
                nextPolyMap.put(1.0, 1.0);
                nextPolyMap.put(0.0, -1 * next);

                for (Double key1 : polyMap.keySet()) {
                    for (Double key2 : nextPolyMap.keySet()) {

                        if (!newPolyMap.containsKey(key1 + key2)) {
                            double exp = key1 + key2;
                            double coeff = polyMap.get(key1) * nextPolyMap.get(key2);

                            newPolyMap.put(exp, coeff);
                        } else {
                            double coeff = polyMap.get(key1) * nextPolyMap.get(key2);
                            double sumToAppend = newPolyMap.get(key1 + key2) + coeff;
                            double newKey = key1 + key2;
                            newPolyMap.put(newKey, sumToAppend);
                        }
                    }
                }
                polyMap = (HashMap<Double, Double>) newPolyMap.clone();
                newPolyMap.clear();
        }
        Object[] keys = polyMap.keySet().toArray();
        Arrays.sort(keys);
        for (int a = keys.length-1; a >= 0; a--) {
            if (((Double)keys[a]).equals(0.0)) {
                if (polyMap.get(keys[a]) % 1 != 0 ) {
                    System.out.printf( "%.2f", polyMap.get(keys[a]) );
                }
                else{
                    System.out.printf( "%.0f", polyMap.get(keys[a]) );
                }
            }
            else if (((Double)keys[a]).equals(1.0)) {
                if (polyMap.get(keys[a]) % 1 != 0) {
                    System.out.printf("%.2fx + ", polyMap.get(keys[a]));
                }
                else{
                    System.out.printf("%.0fx + ", polyMap.get(keys[a]));
                }
            }
            else {
                if (polyMap.get(keys[a]) % 1 != 0) {
                    System.out.printf("%.2fx^%.0f + ", polyMap.get(keys[a]), keys[a]);
                }
                else {
                    System.out.printf("%.0fx^%.0f + ", polyMap.get( keys[a] ), keys[a]);
                }
            }
        }
        return;
    }
}
