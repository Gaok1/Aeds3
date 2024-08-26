package TP4;

import Structures.Musica;
import Tools.Arq;

public class BoyerMoore {
    static Musica[] local;
    static String pattern;
    static int[] badChar;
    static int[] goodSuffix;

    public static void Start() {
        local = Arq.gettAllSongs();
    }

    public static Musica[] FindPattern(String pat) {
        pattern = pat;
        preprocessPattern(pat);
        return searchPatternInSongs();
    }

    private static void preprocessPattern(String pat) {
        badChar = preprocessBadChar(pat);
        goodSuffix = preprocessGoodSuffix(pat);
    }

    private static int[] preprocessBadChar(String pat) {
        int[] badChar = new int[256];
        int m = pat.length();
        for (int i = 0; i < 256; i++) {
            badChar[i] = -1;
        }
        for (int i = 0; i < m; i++) {
            int charCode = (int) pat.charAt(i);
            if (charCode < 256) {
                badChar[charCode] = i;
            }
        }
        return badChar;
    }

    private static int[] preprocessGoodSuffix(String pat) {
        int m = pat.length();
        int[] goodSuffix = new int[m + 1];
        int[] borderPos = new int[m + 1];
        int i = m, j = m + 1;
        borderPos[i] = j;
        while (i > 0) {
            while (j <= m && pat.charAt(i - 1) != pat.charAt(j - 1)) {
                if (goodSuffix[j] == 0) goodSuffix[j] = j - i;
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }
        j = borderPos[0];
        for (i = 0; i <= m; i++) {
            if (goodSuffix[i] == 0) goodSuffix[i] = j;
            if (i == j) j = borderPos[j];
        }
        return goodSuffix;
    }

    private static Musica[] searchPatternInSongs() {
        Musica[] result = new Musica[local.length];
        int count = 0;
        for (Musica musica : local) {
            if (musica == null) {
                continue; // Pula músicas que são null
            }
            //System.out.println("Searching in song: " + musica.getNome() + " by " + musica.getArtista());
            if ((musica.getNome() != null && searchInText(musica.getNome())) ||
                (musica.getArtista() != null && searchInText(musica.getArtista()))) {
                result[count++] = musica;
            }
        }
        Musica[] trimmedResult = new Musica[count];
       // System.arraycopy(result, 0, trimmedResult, 0, count);
        return trimmedResult;
    }

    private static boolean searchInText(String text) {
        int n = text.length();
        int m = pattern.length();
        int s = 0;

        //System.out.println("Searching for pattern: " + pattern + " in text: " + text);

        while (s <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
              //  System.out.println("Pattern found at position: " + s);
                return true; // Pattern found
            } else {
                int charCode = (int) text.charAt(s + j);
                int badCharShift = (charCode < 256) ? j - badChar[charCode] : j + 1;
                if (badCharShift < 1) {
                    badCharShift = 1;
                }
                int goodSuffixShift = (j + 1 < goodSuffix.length) ? goodSuffix[j + 1] : 1;
           //     System.out.println("Bad char shift: " + badCharShift + ", Good suffix shift: " + goodSuffixShift);
                s += Math.max(badCharShift, goodSuffixShift);
            }
        }
       // System.out.println("Pattern not found in text.");
        return false; // Pattern not found
    }
    public static void main(String[] args) {
        
    }
}
