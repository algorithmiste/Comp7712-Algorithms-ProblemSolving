
// Problem 1[30 + 30 + 40 pts] To give you an input a polynomial it will be2
// represented as a sequence of numbers, each
// time an exponent followed by a cefficient as a list. The exponents will be in decreasing order.
// So for example, 3 5 1 10 0 5 represents the polynomial 5x^3+10x+5. While the exponents are
// always integers, the coefficients may be rational numbers.
// Now, write programs to do the following:
// (1) Prompt the user for a polynomial input. Once that is entered, prompt the user for the input
// of some number a. Now compute the quotient and remainder obtained when the input polynomial
// P(x) is divided by x-a. For example, suppose
// the user enters the polynomial 3x^4 + 7x^2 - x + 3 and enters a = 1, the result should be,
// Quotient : 3x^3 + 3x^2 + 10x + 9, Remainder : 12.
// Make sure this runs in O(n) time where n is the degree of the polynomial.
// (2) Write code to compute (x - a1)(x - a2) ... (x - an) for n given numbers, in O(n^2) time.
// (3) Now write code to do interpolation in O(n^2) time. The problem input be a set of pairs of
// (x, y) values like  3 4 7 2 4 10 ... rest on pdf

import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Problem1 {
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		String str = "";
		while (str.isEmpty()) {
			System.out.println("Enter the polynomial: ");
			str = in.nextLine();
		}

		String[] pArray = str.split(" ");

		ArrayList<Double> polyArray = new ArrayList();
		double degree = Double.parseDouble(pArray[0]);
		int globalDegree = (int) degree;
		polyArray.add(degree);

		Double inputNum = Double.POSITIVE_INFINITY;
		while (inputNum.isInfinite()) {
			System.out.println("Enter a number: ");
			inputNum = in.nextDouble();
			inputNum *= -1;
		}

		for (int i = 1; i < pArray.length; i++){
			double next;
			if (pArray[i].contains("/")) {
				String[] fractArr = pArray[i].split("/");
				Double converted = (double) Integer.parseInt(fractArr[0])/Integer.parseInt(fractArr[1]);
				next = converted;
			}
			else {
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
				}
				else {
			 		polyArray.add(next);
				}
			}
			else {
				polyArray.add(next);
			}
		}
		double[] quotientArr = new double[2*globalDegree];
		int qEven = 0, qOdd = 1, pEven = 0, pOdd = 1;
		Double nextToCompare = polyArray.get(pOdd);
		Double remainder = 0.0;

		for (int i = 0; i <= globalDegree; i++) { //at last iteration retrieve "Remainder"
			if (i < globalDegree) {
				quotientArr[qEven] = Math.abs(polyArray.get(pEven) - 1);
				quotientArr[qOdd] = nextToCompare;

				nextToCompare = polyArray.get((pOdd+=2)) - quotientArr[qOdd]*inputNum; // s.t. inputNum == a
				pEven += 2;
				if (((qEven + 2) < quotientArr.length) && ((qOdd + 2) < quotientArr.length)){
					qEven += 2; qOdd += 2;
				}
			}
			else {
				nextToCompare = polyArray.get(pOdd) - quotientArr[qOdd]*inputNum;
				remainder = nextToCompare;
			}
		}
		System.out.print("Quotient: ");
		for (int q = 0; q < quotientArr.length; q+=2) {
			if (quotientArr[q] == 0.0) {
				if ((quotientArr[q+1] % 1 == 0)) {
					System.out.print(((int)quotientArr[q+1]) + ", ");
				}
				else { System.out.print(quotientArr[q+1] + ", "); }


			}
			else if (quotientArr[q] == 1.0) {
				if ((quotientArr[q+1] % 1) == 0) { System.out.print(((int)quotientArr[q+1]) + "x + "); }
				else { System.out.print(quotientArr[q+1] + "x + "); }
			}
			else if (quotientArr[q] > 1.0) {
				if ((quotientArr[q+1] % 1) == 0) { System.out.print(((int)quotientArr[q+1]) + "x^" + (int) quotientArr[q] + " + "); }
				else { System.out.print(quotientArr[q+1] + "x^" + (int) quotientArr[q] + " + "); }
			}
		}
		if (remainder % 1 == 0) { System.out.print("Remainder: " + remainder.intValue() + ".\n"); }
		else { System.out.printf("Remainder: %.2f.\n", remainder); }
		return;

	}
}
