package com.mygit.diff;

import java.util.*;

public class Diff {

    // Returns diff lines: "  line" = unchanged, "- line" = removed, "+ line" = added
    public static List<String> diffLines(String[] a, String[] b) {
        int n = a.length, m = b.length;
        int[][] dp = new int[n + 1][m + 1];

        // Build the LCS length table, working backwards from the end
        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                if (a[i].equals(b[j])) {
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }

        // Walk the table forward, deciding unchanged/removed/added at each step
        List<String> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < n && j < m) {
            if (a[i].equals(b[j])) {
                result.add("  " + a[i]);
                i++; j++;
            } else if (dp[i + 1][j] >= dp[i][j + 1]) {
                result.add("- " + a[i]);
                i++;
            } else {
                result.add("+ " + b[j]);
                j++;
            }
        }
        while (i < n) { result.add("- " + a[i]); i++; }
        while (j < m) { result.add("+ " + b[j]); j++; }

        return result;
    }
}