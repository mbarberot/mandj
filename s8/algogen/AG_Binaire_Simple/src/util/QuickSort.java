package util;

import base.variables.*;
import java.util.*;


public class QuickSort {

	static List<Individual> list;

	public QuickSort(){

	}

	public static List<Individual> sort(List<Individual> inList) {
		list = new ArrayList();
		list = inList;
		quickSort(list, 0, list.size() - 1);
		return list;
	} // sort

	private static void quickSort(List<Individual> list, int low0, int high0) {

		int low = low0, high = high0;

		if (low >= high) {

			return;

		} else if (low == high - 1) {
			if (list.get(low).getFitness() > list.get(high).getFitness()) {
				Individual temp = list.get(low);
				replace(low, list.get(high));
				replace(high, temp);
			}
			return;
		}

		Individual pivot = list.get((low + high) / 2);
		replace((low + high) / 2, list.get(high));
		replace(high, pivot);

		while (low < high) {
			while (list.get(low).getFitness() <= list.get(high).getFitness() && low < high) {
				low++;
			}

			while (list.get(low).getFitness() >= list.get(high).getFitness() && low < high) {
				high--;
			}

			if (low < high) {
				Individual temp = list.get(low);
				replace(low, list.get(high));
				replace(high, temp);
			}
		}

		replace(high0, list.get(high));
		replace(high, pivot);
		quickSort(list, low0, low - 1);
		quickSort(list, high + 1, high0);
	} //quickSort

	public static void replace(int position, Individual indiv) {
		if (position > list.size()) {
			list.add(indiv);
		} // if 
		list.remove(position);
		list.add(position,indiv);
	} // replace
}
