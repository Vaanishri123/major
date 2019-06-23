package com.example.hp.assistant;

public class FA {

    static int getNextState(String pat, int M, int state, int x) {
        if (state < M && x == pat.charAt(state))
            return state + 1;

        int ns, i;

        for (ns = state; ns > 0; ns--) {
            if (pat.charAt(ns - 1) == x) {
                for (i = 0; i < ns - 1; i++) {
                    if (pat.charAt(i) != pat.charAt(state - ns + 1 + i))
                        break;
                }
                if (i == ns - 1)
                    return ns;
            }
        }

        return 0;
    }

    static void computeTF(String pat, int M, int TF[][]) {
        int state, x;
        for (state = 0; state <= M; ++state)
            for (x = 0; x < 256; ++x)
                TF[state][x] = getNextState(pat, M, state, x);
    }

    public static boolean match(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        int TF[][] = new int[M + 1][256];

        computeTF(pat, M, TF);

        int i, state = 0;
        for (i = 0; i < N; i++) {
            state = TF[state][txt.charAt(i)];
            if (state == M) {
                return true;
            }
        }
        return false;
    }
}
