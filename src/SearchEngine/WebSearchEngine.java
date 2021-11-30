package SearchEngine;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;
import WebCrawler.*;
import SpellCheck.*;
import Input.*;

public class WebSearchEngine {
	
	private static Scanner sc = new Scanner(System.in);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		WebSearchEngine SearchEngine = new WebSearchEngine();
		
		System.out.println("--------------------- Web Search Engine ---------------------");
		System.out.println();
		System.out.println("------------- Team Members -------------");
		System.out.println();
		System.out.println("Zaid Imtiaz Chughtai \t Habiba Khan \t Ali Tahir");
		System.out.println();
		System.out.println("=============================================================");
		
		System.out.println();
		System.out.println("------------- Enter any URL to start crawling -------------");
		
		String URL = sc.next();
		URL = "https://"+URL+"/";
		SearchEngine.searchWord(URL);
		
	}

	private void searchWord(String URL) {
		
		if(!validate(URL)) {
			 System.out.println("Enterd URL " + URL + " isn't valid");
			 System.out.println("Please try again....\n");
		}
		
		System.out.println("Validation success\n");
		
		WebCrawler.startCrawler(URL, 0); 						

		Hashtable<String, Integer> listOfFiles = new Hashtable<String, Integer>();
		
	
			System.out.println("\nSearch Something....");
			String searchInp = sc.next();
			int freq = 0;
			int numOfFiles = 0;
			listOfFiles.clear();
			try {
				System.out.println("Searching...");
				File Files = new File(PathFinder.txtDirectoryPath);

				File[] ArrayofFiles = Files.listFiles();

				for (int i = 0; i < ArrayofFiles.length; i++) {

					In data = new In(ArrayofFiles[i].getAbsolutePath());

					String txt = data.readAll();
					data.close();
					Pattern p = Pattern.compile("::");
					String[] file_name = p.split(txt);
					freq = QuerySearcher.querySearch(txt, searchInp.toLowerCase(), file_name[0]);

					if (freq != 0) {
						listOfFiles.put(file_name[0], freq);
						numOfFiles++;
					}
					
				}

				if(numOfFiles>0) {
				System.out.println("\n" + searchInp + " is found in " + numOfFiles + " files\n");
				}else {
					System.out.println("\n" + searchInp + " not found in any file");
					QuerySearcher.suggestAltWord(searchInp.toLowerCase()); 
				}
			 
				

			} catch (Exception e) {
				System.out.println("Exception:" + e);
			}
		
		
		removeFiles();					
		
	}


	private void removeFiles() {
		File files = new File(PathFinder.txtDirectoryPath);
		File[] ArrayofFiles = files.listFiles();

		for (int i = 0; i < ArrayofFiles.length; i++) {
			ArrayofFiles[i].delete();
		}
		
		File HTMLFiles= new File(PathFinder.htmlDirectoryPath);
		File[] fileArrayhtml = HTMLFiles.listFiles();

		for (int i = 0; i < fileArrayhtml.length; i++) {
			
			fileArrayhtml[i].delete();
		}
	}
	
	
	public boolean validate(String URL)
    {
        /* Try creating a valid URL */
        try {
        	System.out.println("Validating URL...");
        	URL obj = new URL(URL);
            HttpURLConnection CON = (HttpURLConnection) obj.openConnection();
            //Sending the request
            CON.setRequestMethod("GET");
            int response = CON.getResponseCode();
            if(response==200) {
            	 return true;
            }else {
            	return false;
            }
           
        }
          
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
