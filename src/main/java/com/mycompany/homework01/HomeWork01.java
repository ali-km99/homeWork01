package com.mycompany.homework01;


import java.util.Arrays;
import java.util.Random;
import java.util.Locale;

public class HomeWork01 {

    
    private static final int N = 100_000;
    private static final Random RANDOM = new Random();

    
    @FunctionalInterface
    interface Sorter {
        void sort(int[] array);
    }

    public static void main(String[] args) {

       
        int[] randomArray = createRandomArray();
        int[] reverseArray = createReverseSortedArray();
        int[] partialArray = createPartiallySortedArray();
        int[] sortedArray = createSortedArray();

       
        int[][] testCases = {
            randomArray,
            reverseArray,
            partialArray,
            sortedArray
        };

        String[] caseNames = {
            "Random",
            "Reverse",
            "Partial",
            "Sorted"
        };

      
        String[] algorithmNames = {
            "Selection Sort",
            "Insertion Sort",
            "Bubble Sort",
            "Merge Sort",
            "Quick Sort"
        };

        Sorter[] algorithms = {
            HomeWork01::selectionSort,
            HomeWork01::insertionSort,
            HomeWork01::bubbleSort,
            HomeWork01::mergeSort,
            HomeWork01::quickSort
        };

        
        double[][] results = new double[algorithms.length][testCases.length];

     
        for (int i = 0; i < algorithms.length; i++) {
            for (int j = 0; j < testCases.length; j++) {

              
                int[] testArray = Arrays.copyOf(
                    testCases[j],
                    testCases[j].length
                );

              
                long startTime = System.nanoTime();

                algorithms[i].sort(testArray);

                long endTime = System.nanoTime();

                // تحويل من النانو الى الملي ساكند.
                results[i][j] = (endTime - startTime) / 1000.0;

                // تحقق من النتائج اذا كانت مرتبة او لا
                if (!isSorted(testArray)) {
                    throw new IllegalStateException(
                        algorithmNames[i] + " failed to sort correctly."
                    );
                }
            }
        }

      
        printResults(algorithmNames, caseNames, results);
    }

    
    private static int[] createRandomArray() {
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = RANDOM.nextInt(1000001);
        }

        return array;
    }

    // من الصغير الى الكبير
    private static int[] createReverseSortedArray() {
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = N - i;
        }

        return array;
    }

    // من الصغير الى الكبير
    private static int[] createSortedArray() {
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = i;
        }

        return array;
    }

  // الترتيب بنسبة 20% من  عناصر المصفوفة
    private static int[] createPartiallySortedArray() {
        int[] array = createSortedArray();

        int numberOfChanges = N / 5;

        for (int i = 0; i < numberOfChanges; i++) {
            int randomIndex = RANDOM.nextInt(N);
            array[randomIndex] = RANDOM.nextInt(1_000_001);
        }

        return array;
    }

    // Selection Sort
    public static void selectionSort(int[] array) {
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }

    // Insertion Sort
    public static void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
    }

    // Bubble Sort
    public static void bubbleSort(int[] array) {
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    swapped = true;
                }
            }

            // Stop early if the array is already sorted.
            if (!swapped) {
                break;
            }
        }
    }

    // Merge Sort
    public static void mergeSort(int[] array) {
        if (array.length < 2) {
            return;
        }

        int[] temp = new int[array.length];
        mergeSort(array, temp, 0, array.length - 1);
    }

    private static void mergeSort(
        int[] array, int[] temp, int left, int right
    ) {
        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

        mergeSort(array, temp, left, middle);
        mergeSort(array, temp, middle + 1, right);

        merge(array, temp, left, middle, right);
    }

    private static void merge(
        int[] array, int[] temp, int left, int middle, int right
    ) {
        int i = left;
        int j = middle + 1;
        int k = left;

        while (i <= middle && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= middle) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        for (int index = left; index <= right; index++) {
            array[index] = temp[index];
        }
    }

    // Quick Sort using a middle-element pivot.
  
    public static void quickSort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int low, int high) {
        while (low < high) {
            int left = low;
            int right = high;

            int pivot = array[low + (high - low) / 2];

            while (left <= right) {

                while (array[left] < pivot) {
                    left++;
                }

                while (array[right] > pivot) {
                    right--;
                }

                if (left <= right) {
                    int temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;

                    left++;
                    right--;
                }
            }

            // Sort the smaller partition recursively.
            if (right - low < high - left) {
                if (low < right) {
                    quickSort(array, low, right);
                }

                low = left;
            } else {
                if (left < high) {
                    quickSort(array, left, high);
                }

                high = right;
            }
        }
    }

    // Check whether the array is sorted in ascending order.
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }

        return true;
    }

    // Print the benchmark results as a formatted table.
    private static void printResults(
        String[] algorithmNames,
        String[] caseNames,
        double[][] results
    ) {
        System.out.println();
        System.out.println("Sorting Algorithms Benchmark");
        System.out.println("Array size: " + N + " elements");
        System.out.println("Time unit: microseconds (us)");
        System.out.println();

        System.out.printf(
            "%-18s %14s %14s %14s %14s%n",
            "Algorithm",
            caseNames[0],
            caseNames[1],
            caseNames[2],
            caseNames[3]
        );

        System.out.println("-".repeat(78));

        for (int i = 0; i < algorithmNames.length; i++) {
            System.out.printf(
                Locale.US,
                "%-18s %14.2f %14.2f %14.2f %14.2f%n",
                algorithmNames[i],
                results[i][0],
                results[i][1],
                results[i][2],
                results[i][3]
            );
        }
    }
}
