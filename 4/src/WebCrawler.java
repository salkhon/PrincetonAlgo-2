import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    public void crawl() {
        Queue<String> webQueue = new Queue<>();
        SET<String> discoveredWeb = new SET<>();

        String root = "https://www.princeton.edu";
        webQueue.enqueue(root);
        discoveredWeb.add(root);

        while (!webQueue.isEmpty()) {
            String currentWeb = webQueue.dequeue();
            System.out.println(currentWeb);
            In in = new In(currentWeb);
            String rawHtml = in.readAll();
//            System.out.println(rawHtml);

            String regex = "http://(\\w+\\.)*(\\w+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(rawHtml);

            while (matcher.find()) {
                String match = matcher.group();
                if (!discoveredWeb.contains(match)) {
                    discoveredWeb.add(match);
                    webQueue.enqueue(match);
                }
            }
        }
    }

    // test client
    public static void main(String[] args) {
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.crawl();
    }
}
