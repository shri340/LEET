
class Solution {
    public String reversePrefix(String word, char ch) {

        char[] arr = word.toCharArray();

        int left = 0;
        int right = word.indexOf(ch);

        if (right == -1) {
            return word;
        }

        while (left < right) {

            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }

        return new String(arr);
    }
}
