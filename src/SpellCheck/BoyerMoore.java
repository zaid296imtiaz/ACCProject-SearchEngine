package SpellCheck;

public class BoyerMoore {
	private final int R; 
	private static int[] right; 

	private char[] pattern; 
	private String pat; 

	// pattern provided as a string
	public BoyerMoore(String pat) {
		this.R = 10000;
		this.pat = pat;


		right = new int[R];
		for (int c = 0; c < R; c++)
			right[c] = -1;
		for (int j = 0; j < pat.length(); j++)
			right[pat.charAt(j)] = j;
	}


	public static int search(String pat, String txt) {
		int M = pat.length();
		int N = txt.length();
		int skip;
		for (int i = 0; i <= N - M; i += skip) {
			skip = 0;
			for (int j = M - 1; j >= 0; j--) {
				if (pat.charAt(j) != txt.charAt(i + j)) {
					skip = Math.max(1, j - right[txt.charAt(i + j)]);
					break;
				}
			}
			if (skip == 0)
				return i; 
		}
		return N; 
	}

}
