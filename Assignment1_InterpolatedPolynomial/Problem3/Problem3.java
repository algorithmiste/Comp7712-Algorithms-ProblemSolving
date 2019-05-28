
/* Question 3: Now write code to do interpolation in O(n^2) time, where n is the degree of the polynomial.
*  The problem input will be a set of pairs of (x, y) values like 3 4 7 2 4 10
*  where the list is the x value followed by the y value. Here, the output will be a polynomial of degree 2.
*  Suppose the input is (x1,a1), (x2,a2),...,(xn,an). Then the interpolated polynomial is given by:
*  SUM ai*Pi(x), for i = 1,...,n
*  where the polynomial Pi(x) is defined as,
*  PI (x - xj) , j != i / PI (xi - xj) , j != i
*  Notice that Pi(xi) = 1 and Pi(xj) = 0 for j != i
*  */

import java.util.*;

public class Problem3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String pairs = "";
        while (pairs.isEmpty()) {
            System.out.println("Enter a list of numbers as a set of pairs, x value followed by y value: ");
            pairs = in.nextLine();
        }
        String[] pairsArr = pairs.split(" ");
        HashMap<Double, Double> m = new HashMap<>();
        int pairIndex = 1;

        ArrayList<HashMap<Double, Double>> lastP = new ArrayList<>();
        HashSet<Double> keySet = new HashSet<>();
        HashMap<Double, Double> polynomialToDivide = generatePolynomial(pairsArr, -1);

        for (int j = 1; j < pairsArr.length; j+=2) {
            Double a = Double.parseDouble(pairsArr[j]);
            a /= generateDivisor(pairsArr, pairIndex);
            double[] quotientArray = dividePolynomial(polynomialToDivide,  Double.parseDouble(pairsArr[j])); // index = 2k is exponent, index = 2k+1 is coeff
            HashMap<Double, Double> transitionMap = new HashMap<>();
            for (int n = 0; n <= quotientArray.length-2; n+=2) {
                transitionMap.put(quotientArray[n], quotientArray[n+1]);
            }
            m = transitionMap;
            for (Map.Entry<Double, Double> e : m.entrySet()) {
                m.put(e.getKey(), a * e.getValue());
            }
            lastP.add(m);
            for (Double key : m.keySet()) {
                keySet.add(key);
            }
            pairIndex++;
        }
        HashMap<Double, Double> answerPolynomial = new HashMap<>();
        Object[] keys = keySet.toArray();
        Arrays.sort(keys);
        for (int k = keys.length -1; k >= 0; k--) {
            Double sumCoeff = 0.0;
            for (HashMap<Double, Double> element : lastP) {
                sumCoeff += element.get((Double) keys[k]);
            }
            answerPolynomial.put((Double)keys[k], sumCoeff);
        }
        for (Double key : answerPolynomial.keySet()) {
            if (key.equals(0.0)) {
                if (answerPolynomial.get(key) % 1 != 0 ) {
                    System.out.printf( "%.2f", answerPolynomial.get(key) );
                }
                else{
                    System.out.printf( "%.0f", answerPolynomial.get(key) );
                }
            }
            else if (key.equals(1.0)) {
                if (answerPolynomial.get(key) % 1 != 0) {
                    System.out.printf("%.2fx + ", answerPolynomial.get(key));
                }
                else{
                    System.out.printf("%.0fx + ", answerPolynomial.get(key));
                }
            }
            else {
                if (answerPolynomial.get(key) % 1 != 0) {
                    System.out.printf("%.2fx^%.0f + ", answerPolynomial.get(key), key);
                }
                else {
                    System.out.printf("%.0fx^%.0f + ", answerPolynomial.get( key ), key);
                }
            }
        }
    }
    public static double generateDivisor(String[] pairsArr, int indexToIgnore) {
        ArrayList<String> arrToMultiply = new ArrayList<>();
        int xIndex = 1;
        Double xFixed = Double.POSITIVE_INFINITY;
        for (int w = 0; w < pairsArr.length; w+=2) {
            if (xIndex != indexToIgnore) {arrToMultiply.add(pairsArr[w]);}
            else {xFixed = Double.parseDouble(pairsArr[w]);}
            ++xIndex;
        }
        if (xFixed == Double.POSITIVE_INFINITY) {return Double.NaN;}

        Double result = 1.0;
        for (int i = 0; i < arrToMultiply.size(); i++) {
            result *= (xFixed - Double.parseDouble(arrToMultiply.get(i)));
        }
        return result;
    }

    public static HashMap<Double, Double> generatePolynomial(String[] pairsArr, int indexToIgnore) {
        ArrayList<String> arrToMultiply = new ArrayList<>();

        int xIndex = 1;
        for (int w = 0; w < pairsArr.length; w+=2) {
            if (xIndex != indexToIgnore) {arrToMultiply.add(pairsArr[w]);}
            ++xIndex;
        }

        HashMap<Double, Double> polyMap = new HashMap<>();
        HashMap<Double, Double> newPolyMap = new HashMap<>();
        polyMap.put(1.0, 1.0);
        polyMap.put(0.0, -1 * Double.parseDouble(arrToMultiply.get(0)));

        Boolean timeToBreak = false;
        for (int j = 1; j < arrToMultiply.size(); j++) {

            Double next = Double.parseDouble(arrToMultiply.get(j));
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
    return polyMap;
    }
    public static double[] dividePolynomial(HashMap<Double, Double> polyMap , Double inputNum) {
        String[] pArray = new String[2*polyMap.size()];
        Object[] keys = polyMap.keySet().toArray();
        Arrays.sort(keys);
        for (int k = keys.length -1, f = 0; k >= 0; k--,f+=2) {
            pArray[f] = String.valueOf(keys[k]);
            pArray[f+1] = String.valueOf(polyMap.get(keys[k]));
        }

        ArrayList<Double> polyArray = new ArrayList();
        double degree = Double.parseDouble(pArray[0]);
        int globalDegree = (int) degree;
        polyArray.add(degree);

        inputNum *= -1;

        for (int i = 1; i < pArray.length; i++) {
            double next;
            if (pArray[i].contains("/")) {
                String[] fractArr = pArray[i].split("/");
                Double converted = (double) Integer.parseInt(fractArr[0]) / Integer.parseInt(fractArr[1]);
                next = converted;
            } else {
                next = Double.parseDouble(pArray[i]);
            }
            if (i % 2 == 0) {
                --degree;
                if (next != degree) {
                    while (next != degree) {
                        polyArray.add(degree);
                        polyArray.add(0.0);
                        --degree;
                    }
                    polyArray.add(next);
                } else {
                    polyArray.add(next);
                }
            } else {
                polyArray.add(next);
            }
        }
        double[] quotientArr = new double[2 * globalDegree];
        int qEven = 0, qOdd = 1, pEven = 0, pOdd = 1;
        Double nextToCompare = polyArray.get(pOdd);
        Double remainder = 0.0;

        for (int i = 0; i <= globalDegree; i++) { //at last iteration retrieve "Remainder"
            if (i < globalDegree) {
                quotientArr[qEven] = Math.abs(polyArray.get(pEven) - 1);
                quotientArr[qOdd] = nextToCompare;

                nextToCompare = polyArray.get((pOdd += 2)) - quotientArr[qOdd] * inputNum; // s.t. inputNum == a
                pEven += 2;
                if (((qEven + 2) < quotientArr.length) && ((qOdd + 2) < quotientArr.length)) {
                    qEven += 2;
                    qOdd += 2;
                }
            } else {
                nextToCompare = polyArray.get(pOdd) - quotientArr[qOdd] * inputNum;
                remainder = nextToCompare;
            }
        }
        return quotientArr;
    }
}
