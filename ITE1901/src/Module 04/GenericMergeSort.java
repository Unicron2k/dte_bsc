public class GenericMergeSort{
    public static <E extends Comparable<E>> void mergeSort(E[] list) {
        if (list.length > 1) {
            // Split list

            // Merge sort the first half
            E[] firstHalf = (E[]) new Comparable[list.length / 2];
            System.arraycopy(list, 0, firstHalf, 0, firstHalf.length);
            mergeSort(firstHalf);

            // Merge sort the second half
            E[] secondHalf = (E[]) new Comparable[list.length - (list.length/2)];
            System.arraycopy(list, list.length / 2, secondHalf, 0, secondHalf.length);
            mergeSort(secondHalf);

            // Merge firstHalf with secondHalf
            E[] cache = merge(firstHalf, secondHalf);
            System.arraycopy(cache, 0, list, 0, cache.length);
        }
    }

    /**
     * Merges the two lists together into one list
     *
     * @param list1 First half to merge
     * @param list2 Second half to merge
     * @param <E> The generic
     * @return The new merged list
     */
    public static <E extends Comparable<E>> E[] merge(E[] list1, E[] list2) {
        E[] cache = (E[]) new Comparable[list1.length + list2.length];

        int current1 = 0; // Current index in list1
        int current2 = 0; // Current index in list2
        int current3 = 0; // Current index in cache

        while (current1 < list1.length && current2 < list2.length) { //While neither of lists are used up
            if (list1[current1].compareTo(list2[current2]) <= 0)
                cache[current3++] = list1[current1++];
            else
                cache[current3++] = list2[current2++];
        }

        while (current1 < list1.length) //If list2 is used up
            cache[current3++] = list1[current1++];

        while (current2 < list2.length) //If list1 is used up
            cache[current3++] = list2[current2++];

        return cache;
    }
}