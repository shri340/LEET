
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int numIdenticalPairs(int[] nums) {

        Map<Integer, Integer> freq = new HashMap<>();

        int count = 0;

        for (int num : nums) {

            int previous = freq.getOrDefault(num, 0);

            count += previous;

            freq.put(num, previous + 1);
        }

        return count;
    }
}
