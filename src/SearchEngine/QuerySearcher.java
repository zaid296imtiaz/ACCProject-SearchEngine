package SearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import SpellCheck.*;

public class QuerySearcher {
	static ArrayList<String> key = new ArrayList<String>();
	static HashMap<String, Integer> numbers = new HashMap<String, Integer>();
	// Positions and Occurrences of the words
		public static int querySearch(String data, String word, String fileName) {
			int count = 0;

			int offset = 0;
			BoyerMoore boyerMoore = new BoyerMoore(word);

			for (int location = 0; location <= data.length(); location += offset + word.length()) {
				offset = boyerMoore.search(word, data.substring(location));
				if ((offset + location) < data.length()) {
					count++;
				}
			}
			if (count != 0) {
				System.out.println(count + " occurances found in " + fileName);
																											
			}
			return count;
		}


		public static void suggestAltWord(String wordToSearch) {
			String line = " ";
			String regex = "[a-z0-9]+";

			// Create a Pattern object
			Pattern pattern = Pattern.compile(regex);
			// Now create matcher object.
			Matcher matcher = pattern.matcher(line);
			int fileNumber = 0;

			File dir = new File(PathFinder.txtDirectoryPath);
			File[] fileArray = dir.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				try {
					findWord(fileArray[i], fileNumber, matcher, wordToSearch);
					fileNumber++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

			Integer allowedDistance = 1; 
			boolean matchFound = false; 

			
			int i = 0;
			for (Map.Entry entry : numbers.entrySet()) {
				if (allowedDistance == entry.getValue()) {
					
					i++;
					
					if(i==1)
					System.out.println("Did you mean? ");
					
					System.out.print( i + ") " + entry.getKey() + "\n");
					matchFound = true;
				}
			}
			if (!matchFound)
				System.out.println("Entered word not found");
		}


		public static void findWord(File sourceFile, int fileNumber, Matcher match, String str)
				throws FileNotFoundException, ArrayIndexOutOfBoundsException {
			try {
				BufferedReader my_rederObject = new BufferedReader(new FileReader(sourceFile));
				String line = null;

				while ((line = my_rederObject.readLine()) != null) {
					match.reset(line);
					while (match.find()) {
						key.add(match.group());
					}
				}

				my_rederObject.close();
				for (int p = 0; p < key.size(); p++) {
					numbers.put(key.get(p), editDistance(str.toLowerCase(), key.get(p).toLowerCase()));
				}
			} catch (Exception e) {
				System.out.println("Exception:" + e);
			}

		}

		public static int editDistance(String str1, String str2) {
			int len1 = str1.length();
			int len2 = str2.length();

			int[][] my_array = new int[len1 + 1][len2 + 1];

			for (int i = 0; i <= len1; i++) {
				my_array[i][0] = i;
			}

			for (int j = 0; j <= len2; j++) {
				my_array[0][j] = j;
			}

			for (int i = 0; i < len1; i++) {
				char c1 = str1.charAt(i);
				for (int j = 0; j < len2; j++) {
					char c2 = str2.charAt(j);

					if (c1 == c2) {
						my_array[i + 1][j + 1] = my_array[i][j];
					} else {
						int replace = my_array[i][j] + 1;
						int insert = my_array[i][j + 1] + 1;
						int delete = my_array[i + 1][j] + 1;

						int min = replace > insert ? insert : replace;
						min = delete > min ? min : delete;
						my_array[i + 1][j + 1] = min;
					}
				}
			}

			return my_array[len1][len2];
		}

}
