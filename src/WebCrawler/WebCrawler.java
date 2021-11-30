package WebCrawler;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import HTMLToText.*;

public class WebCrawler {


	private static Set<String> crawledList = new HashSet<String>();
	private static int maxDepth = 2; 
	private static String regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";


	public static void startCrawler(String url, int depth) {
		Pattern patternObject = Pattern.compile(regex);
		if (depth <= maxDepth) {
			try {
				Document document = Jsoup.connect(url).get();
				HTMLConverter.saveDoc(document, url);
				depth++;
				if (depth < maxDepth) {
					Elements links = document.select("a[href]");
					for (Element page : links) {

						if (verifyUrl(page.attr("abs:href")) && patternObject.matcher(page.attr("href")).find()) {
							
							System.out.println(page.attr("abs:href"));
							startCrawler(page.attr("abs:href"), depth);
							crawledList.add(page.attr("abs:href"));
						}
					}
				}
			} catch (Exception e) {

			}
		}
	}


	private static boolean verifyUrl(String nextUrl) {
		if (crawledList.contains(nextUrl)) {
			return false;
		}
		if (nextUrl.endsWith(".jpeg") || nextUrl.endsWith(".jpg") || nextUrl.endsWith(".png")
				|| nextUrl.endsWith(".pdf") || nextUrl.contains("#") || nextUrl.contains("?")
				|| nextUrl.contains("mailto:") || nextUrl.startsWith("javascript:") || nextUrl.endsWith(".gif")
				||nextUrl.endsWith(".xml")) {
			return false;
		}
		return true;
	}
}
