package model;

import java.util.ArrayList;

public class PossibilityGenerator {

	private static ArrayList<ArrayList<Integer>> blanks = new ArrayList<>();

	public static ArrayList<ArrayList<Boolean>> getPossibleCombinations(ArrayList<Integer> indicator, boolean[] line) {

		blanks.clear();
		ArrayList<ArrayList<Boolean>> possibilities = new ArrayList<>();

		int totalBlankSpaces = getNumBlank(line, indicator) - (indicator.size() - 1);
		int[] blankArray = initializeBlank(totalBlankSpaces, indicator.size() + 1); // # zeros between items
		blankSpaceGenerator(blankArray, totalBlankSpaces, blankArray.length, 0);

		for (ArrayList<Integer> blank : blanks) {
			ArrayList<Boolean> possibility = fetchLine(blank, indicator);
			possibilities.add(possibility);
		}

		return possibilities;
	}

	private static ArrayList<Boolean> fetchLine(ArrayList<Integer> blankRow, ArrayList<Integer> indicator) {

		ArrayList<Boolean> possibility = new ArrayList<>();

		for (int i = 0; i < indicator.size(); i++) {
			for (int j = 0; j < blankRow.get(i); j++) {
				possibility.add(false);
			}
			for (int j = 0; j < indicator.get(i); j++) {
				possibility.add(true);
			}
		}

		for (int i = 0; i < blankRow.get(blankRow.size() - 1); i++) {
			possibility.add(false);
		}

		return possibility;
	}

	private static void blankSpaceGenerator(int[] blankArray, int totalBlankSpaces, int index, int deduction) {
		int length = blankArray.length;

		if (index <= 2) {
			for (int i = 0; i <= totalBlankSpaces - deduction; i++) {
				blankArray[length - 1] = totalBlankSpaces - i - deduction;
				blankArray[length - 2] = totalBlankSpaces - blankArray[length - 1] - deduction;

				ArrayList<Integer> list = new ArrayList<>();
				for (int item : blankArray) {
					list.add(item);
				}
				list = incrementCenter(list);
				blanks.add(list);
			}
		} else {
			for (int i = 0; i <= totalBlankSpaces; i++) {
				blankArray[length - index] = i;
				blankSpaceGenerator(blankArray, totalBlankSpaces, index - 1, deduction + i);
			}
		}

	}

	private static ArrayList<Integer> incrementCenter(ArrayList<Integer> arr) {

		for (int i = 1; i < arr.size() - 1; i++) {
			arr.set(i, arr.get(i) + 1);
		}

		return arr;
	}

	// places all items in last member of array
	private static int[] initializeBlank(int totalZeros, int arrSize) {
		int[] blankArray = new int[arrSize];

		for (int i = 0; i < arrSize - 1; i++) {
			blankArray[i] = 0;
		}
		blankArray[arrSize - 1] = totalZeros;

		return blankArray;
	}

	// returns number false in array
	private static int getNumBlank(boolean[] row, ArrayList<Integer> indicator) {
		int sum = 0;

		for (Integer number : indicator) {
			sum += number;
		}

		return row.length - sum;
	}

	public static void main(String[] args) {
		boolean[] row = {false, true, true, false, false, true, true, true, false, true, false, false, false};
		ArrayList<Integer> indicator = new ArrayList<>();
		indicator.add(2);
		indicator.add(3);
		indicator.add(2);

		ArrayList<ArrayList<Boolean>> possibilities = getPossibleCombinations(indicator, row);

		for (ArrayList<Boolean> possibility : possibilities) {
			for (Boolean item : possibility) {
				System.out.print((item) ? "X" : "-");
			}
			System.out.println();
		}
	}
}

