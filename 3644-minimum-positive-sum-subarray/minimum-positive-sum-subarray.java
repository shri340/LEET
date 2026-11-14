
import java.util.*;

class Solution {
    public int minimumSumSubarray(List<Integer> nums, int l, int r) {

        int n = nums.size();

        int[] prefix = new int[n + 1];

        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums.get(i);
        }

        TreeMap<Integer, Integer> map = new TreeMap<>();

        int ans = Integer.MAX_VALUE;

        for (int j = l; j <= n; j++) {

            // Add prefix sum for the minimum length
            int add = prefix[j - l];
            map.put(add, map.getOrDefault(add, 0) + 1);

            // Remove prefix sums outside maximum length
            int removeIndex = j - r - 1;

            if (removeIndex >= 0) {
                int remove = prefix[removeIndex];

                int freq = map.get(remove);

                if (freq == 1) {
                    map.remove(remove);
                } else {
                    map.put(remove, freq - 1);
                }
            }

            // Find the largest prefix sum smaller than prefix[j]
            Integer previous = map.lowerKey(prefix[j]);

            if (previous != null) {
                ans = Math.min(ans, prefix[j] - previous);
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
