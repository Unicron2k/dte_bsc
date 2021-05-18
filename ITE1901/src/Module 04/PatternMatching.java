class PatternMatching {

    /**
     * Finds a pattern in a given string
     *
     * @param str The string to search in
     * @param pattern The pattern to search for in str
     * @return The index of first char of pattern, if not found: -1
     */
     int findPatternInString(String str, String pattern) {
        int index = -1;
        for (int i = 0, k = 0; i < str.length(); i++) {
            if (str.charAt(i) == pattern.charAt(k)) {
                k++;
                if (k == pattern.length()) {
                    index = i - k + 1; // +1 because zero-indexing
                    break;
                }
            } else {
                k = 0;
                if (str.charAt(i) == pattern.charAt(k))
                    k++;
            }
        }
        return index;
     }
}