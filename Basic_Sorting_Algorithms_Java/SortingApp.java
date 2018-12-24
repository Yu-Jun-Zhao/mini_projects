package sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Using random numbers to benchmark different sorting algorithms
 * 
 * @author: Yu Jun Zhao
 * 
 */

public class SortingApp {
	
	public static int ITERATION = 0; //for testing

	private static ArrayList<Integer> randomGen(int size,int start, int endExclusive){
		IntStream stream = new Random().ints(size, start, endExclusive);
		return (ArrayList<Integer>) stream.boxed().collect(Collectors.toList());
	}
	// swapping the element of the two index in the arraylist
	// could potentially cause indexoutofbound exception
	private static void swap(List<Integer> list, int a,int b){
		int temp = list.get(a);
		list.set(a, list.get(b));
		list.set(b, temp);
	}
	
	// For base 10 only
	// getDigitAt(1234, 3) return 2
	private static int getDigitAt(int number, int digitAt){
		int remainder =   (number % (int)Math.pow(10, digitAt));
		return remainder / (int)Math.pow(10, digitAt-1);
	}
	
	private static boolean isSorted(ArrayList<Integer> list){
		
		for(int i = 0; i < list.size()-1; i++){
			// if the next element is less than current element at i
			// then the list is not sorted
			if(list.get(i + 1) < list.get(i)){
				return false;
			}
		}
		return true;
	}
	
	// -----------------------------------------
	
	public static void selectionSort(List<Integer> list){
		for(int i = 0; i < list.size(); i++){
			int smallestIndex = i;
			for(int j = i; j < list.size(); j++){
				if(list.get(j) < list.get(smallestIndex)){
					smallestIndex = j;
				}
			}
			swap(list, i, smallestIndex);
		}
	}
	
	
	// -----------------------------------------
	public static void insertionSort(List<Integer> list){
		for(int j = 1; j < list.size(); j++){
			for(int i = j; i > 0; i--){
				if(list.get(i) < list.get(i-1)){
					swap(list, i, i-1);
				}else{
					break;
				}
			}
		}
	}
	
	//----------------------------------------------
	
	public static void bubbleSort(List<Integer> list){
		
		// should be looping at least list size - 1 times.
		for(int j = list.size()-1; j > 0; j--){
			for(int i = 0; i < j; i++){
				if(list.get(i) > list.get(i+1)){
					swap(list, i, i+1);
				}
			}	
		}
	}
	
	// -----------------------------------------------------------
	public static ArrayList<Integer> mergeSort(List<Integer> list){
		if(list.size() == 1) return new ArrayList<Integer>(list);
		
		ArrayList<Integer> leftList = mergeSort(list.subList(0, list.size()/2));
		ArrayList<Integer> rightList = mergeSort(list.subList(list.size()/2, list.size()));

		return merge(leftList, rightList);
	}
	
	private static ArrayList<Integer> merge(ArrayList<Integer> list1, ArrayList<Integer> list2){
		ArrayList<Integer> newList = new ArrayList<>();
		int l = 0, r = 0;
		while(l < list1.size() || r < list2.size()){
			int leftValue = list1.get(l);
			int rightValue = list2.get(r);

			if(leftValue < rightValue){
				newList.add(leftValue);
				l++;
				// one list will reach the end before the other
				if(l == list1.size()){
					newList.addAll(list2.subList(r, list2.size()));
					break;
				}
			}else{
				newList.add(rightValue);
				r++;
				if(r == list2.size()){
					newList.addAll(list1.subList(l, list1.size()));
					break;
				}
			}
		}
		return newList;
	}
	// -----------------------------------------------------------

	// Sort digits (keys) 0-9
	// No error/exception handling
	public static ArrayList<Integer> countingSort(List<Integer> list, List<Integer> key){
		if(list.size() != key.size()) return null;
		
		int temp[] = new int[10]; 
		for(int i = 0; i < list.size(); i++){
			temp[key.get(i)]++;
		}
		
		//adding previous value to the current value at j
		// if storage[j] == 0, then that value does not exist.
		for(int j = 1; j < temp.length; j++){
			temp[j] += temp[j-1];
		}
		
		// shifting the values of temp to the right by 1
		// first value would be 0
		int storage[] = new int[temp.length];
		for(int shift = 1; shift < storage.length; shift++){
			storage[shift] = temp[shift-1];
		}
		 
		Integer result[] = new Integer[list.size()];
		for(int i = 0; i < key.size(); i++){
			// Uses keys to find the index for the actual value
			// For example, it see the key 9 at index 0 of key list. 
			// The index location for this key to be placed is at index 45 of result array
			// The actual value which is 89 would be placed in index 45 instead.
			int storageIndex = key.get(i);
			int resultIndex = storage[storageIndex]++;
			
			// store the actual value (not the keys)
			result[resultIndex] = list.get(i); 
		}
		return new ArrayList<Integer>(Arrays.asList(result));
	}
	
	
	public static ArrayList<Integer> radixSort(ArrayList<Integer> list, int maxDigit){
		
		// Create the keys 
		ArrayList<Integer> keys = new ArrayList<>();

		for(int digitPlace = 1; digitPlace < maxDigit + 1; digitPlace++){
			// Create the keys
			keys.clear();

			for(int i = 0; i < list.size(); i++){
				keys.add(getDigitAt(list.get(i), digitPlace));				
			}
			// Sort
			list = countingSort(list, keys);
		}	
		
		return list;
		
	
	}
	
	// ---------------------------------------
	public static void heapSort(ArrayList<Integer> list){
//		ArrayList<Integer> newList = new ArrayList<>();
		
		int times = list.size()-2; // we do not use index 0 and if list contains 1 element, we stop
		int lastIndex = list.size()-1;
		buildMaxHeap(list, lastIndex);

		
		while(times > 0){
			
			swap(list, 1, lastIndex);
			//newList.add(0,value);
			lastIndex--;

			maxHeapify(list, 1, lastIndex);
			times--;
		}
		list.remove(0);
	}
	
	private static void buildMaxHeap(ArrayList<Integer> list, int lastIndex){
		// minus 1 because we want the index starts at 1 and not 0
		// The bottom half of the list are leaf nodes
		for(int i = lastIndex / 2; i >= 1; i--){
			maxHeapify(list, i, lastIndex);
		}
	}
	private static void maxHeapify(ArrayList<Integer> list, int index, int lastIndex){
		int valueAtIndex = list.get(index);
		int leftChildIndex = 2 * index;
		int rightChildIndex = (2 * index) + 1;
		int valueAtLeftChild = leftChildIndex <= lastIndex ? list.get(leftChildIndex) : -1;
		int valueAtRightChild = rightChildIndex <= lastIndex ? list.get(rightChildIndex) : -1;
		// if this node has no children
		if(valueAtLeftChild == -1 && valueAtRightChild == -1) return;
		
		int potentialIndex = -1;
		int potentialValue = -1;
		if(valueAtLeftChild >= valueAtRightChild){
			potentialValue = valueAtLeftChild;
			potentialIndex = leftChildIndex;
		}else{
			potentialValue = valueAtRightChild;
			potentialIndex = rightChildIndex;
		}
		
		if(potentialValue > valueAtIndex){
			swap(list,index, potentialIndex);
			maxHeapify(list, potentialIndex, lastIndex);
		}
	}
	
	// -----------------------------------------------
	// This quickSort pivot starts at the end. 
	// Because this is an unordered list
	
	public static void quickSort(List<Integer> list, int startIndex, int pivot){
		// already sorted
		if(startIndex >= pivot || pivot < 0 || pivot >= list.size()) return; // since pivot should always be at the end of the "round"


		// partition part
		int iIndex = startIndex;
		int jIndex = startIndex;
		
		int valueAtPivot = list.get(pivot);
		while(jIndex < pivot){
			if(list.get(jIndex) <= valueAtPivot){
				swap(list, iIndex, jIndex); // swap i and j elements
				iIndex++;
			}
			jIndex++;
		}
		
		// Once jIndex reaches the pivot
		swap(list, iIndex, pivot);
		pivot = iIndex;
		
		// Left
		quickSort(list, startIndex, pivot-1);
							
		// Right
		quickSort(list, pivot+1, jIndex);	

	}
	

	public static void main(String[] args) {
		
		int size = 101200;
		int startAt = 0;
		int endExclusive = 10123;
		
		long startTime = 0;
		long endTime = 0;
		
		ArrayList<Integer> randNumberList = randomGen(size, startAt, endExclusive);
		
		
		//---------------------------------------------------------
		ArrayList<Integer> selectionList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		selectionSort(selectionList);
		endTime = System.currentTimeMillis();
		//selectionList.stream().forEach(System.out::println);
		System.out.println("Selection Sort - List size: " + selectionList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(selectionList));
		
		
		// ------------------------------------------------------
		ArrayList<Integer> insertionList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		insertionSort(insertionList);
		endTime = System.currentTimeMillis();
		//insertionList.stream().forEach(System.out::println);
		System.out.println("Insertion Sort - List size: " + insertionList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(insertionList));
		
		
		// -------------------------------------------------------------------
		
		ArrayList<Integer> bubbleList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		bubbleSort(bubbleList);
		endTime = System.currentTimeMillis();
		//bubbleList.stream().forEach(System.out::println);
		System.out.println("Bubble Sort - List size: " + bubbleList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(bubbleList));
		
	
		//-----------------------------------------

		ArrayList<Integer> mList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		ArrayList<Integer> mergedList = mergeSort(mList);
		endTime = System.currentTimeMillis();
		//mergedList.stream().forEach(System.out::println);
		System.out.println("Merge Sort - List size: " + mergedList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(mergedList));
		
		//-----------------------------------------
		// minus 1 because the random gen does not include endExclusive.
		// if endExclusive is 10000, random gen max value would be 9999
		// so we want 4 digtis instead of 5.
		int maxDigit = Integer.toString(endExclusive-1).length();  
		ArrayList<Integer> rList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		ArrayList<Integer> radixedList = radixSort(rList, maxDigit);
		endTime = System.currentTimeMillis();
		//radixedList.stream().forEach(System.out::println);
		System.out.println("Radix Sort - List size: " + radixedList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(radixedList));

		
		ArrayList<Integer> hList = new ArrayList<>();
		hList.add(0);
		hList.addAll((ArrayList<Integer>) randNumberList.clone());
		startTime = System.currentTimeMillis();
		//ArrayList<Integer> heapedList = heapSort(hList);
		heapSort(hList);
		endTime = System.currentTimeMillis();
		//hList.stream().forEach(System.out::println);
		System.out.println("Heap Sort - List size: " + hList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(hList));

		
		ArrayList<Integer> qList = (ArrayList<Integer>) randNumberList.clone();
		startTime = System.currentTimeMillis();
		quickSort(qList, 0, qList.size()-1);
		endTime = System.currentTimeMillis();
		//qList.stream().forEach(System.out::println);
		System.out.println("Quick Sort - List size: " + qList.size() 
							+ " Time taken to complete: " + (endTime - startTime) + " millisecond"
							+ " is Sorted: " + isSorted(qList));
		//System.out.println(ITERATION);
	}

}
