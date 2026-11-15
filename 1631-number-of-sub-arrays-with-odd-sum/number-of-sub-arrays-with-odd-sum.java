
class Solution {
    public int numOfSubarrays(int[] arr) {

        int MOD = 1_000_000_007;

        long even = 1;
        long odd = 0;
        long ans = 0;

        int prefix = 0;

        for (int num : arr) {

            prefix = (prefix + num) % 2;

            if (prefix == 1) {
                ans = (ans + even) % MOD;
                odd++;
            } 
            else {
                ans = (ans + odd) % MOD;
                even++;
            }
        }

        return (int) ans;
    }
}
