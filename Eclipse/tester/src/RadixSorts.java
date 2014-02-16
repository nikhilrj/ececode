import java.util.*;

public class RadixSorts {

    private void popBuckets(int[] nums,
            HashMap<Integer, ArrayList<Integer>> buckets) {
        // get the iterator from the key set.
        Iterator<Integer> itKey = buckets.keySet().iterator();
        // index is used as counter of num array.
        int index = 0;
        while (itKey.hasNext()) {
            ArrayList<Integer> al = buckets.get(itKey.next());
            int size = al.size();
            if (size != 0) {
                for (Integer n : al) {
                    try {
                        nums[index++] = n;
                    } catch (Exception ex) {
                        System.out.println("the index is out of the array");
                    }
                }
            }
        }
    }

    /*
     * get the digit on the specified position. 
     */
    private int getDigit(int num, int index) {
        int digit = 0;
        for (int i = 0; i < index + 1; i++) {
            digit = num % 10;
            num /= 10;
        }
        return digit;
    }

    /*
     * put the number into the associated bucket. 
     */
    private void pushBuckets(int[] nums, 
            HashMap<Integer, ArrayList<Integer>> buckets,
            int i) {
        int digit = 0;
        for (int n : nums) {
            digit = getDigit(n, i);
            if (buckets.get(digit) == null) {
                ArrayList<Integer> al = new ArrayList<Integer>();
                al.add(n);
                buckets.put(digit, al);
            } else {
                buckets.get(digit).add(n);
            }
        }
    }

    private int findLargestNum(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (max < nums[i]) {
                max = nums[i];
            }
        }
        return max;
    }

    private void radixSort(int[] nums) {
        // get the largest number to decide how many
        // times the sort loops will be excuted.
        int max = findLargestNum(nums);
        int loops = (max + "").length();
        HashMap<Integer, ArrayList<Integer>> buckets = new 
            HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < loops; i++) {
            // i indicates which digit will be as the radix.
            pushBuckets(nums, buckets, i);
            popBuckets(nums, buckets);
            buckets.clear();
        }
    }

    private void printArray(int[] nums) {
        for (int n = 0 ; n < 5; n++) {
            System.out.println(nums[n]);
        }
    }

    private void go() {
    	Random r = new Random(17);
    	
        int[] nums = new int[50000];
        for(int i = 0; i < nums.length; i++)
        	nums[i] = r.nextInt(Integer.MAX_VALUE);
        System.out.println("+++++++++++++++");
        printArray(nums);
        StopWatch sw = new StopWatch();
        sw.start();
        radixSort(nums);
        sw.stop();
        System.out.println("---------------");
        printArray(nums);
        System.out.println(sw.getElapsedTime());
    }

    public static void main(String[] args) {
        new RadixSorts().go();
    }
}