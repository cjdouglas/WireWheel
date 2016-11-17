package com.wirewheel.parsing;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wirewheel.listings.Listing;
import com.wirewheel.listings.ListingDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chris on 9/22/2016.
 *
 * This class handles all of the information parsing for the application.
 * The WebScraper utilizes Java's concurrency package to execute multiple
 * HTTP requests concurrently, in order to provide minimum loading time
 * while maintaining a good UX.
 *
 * Listings are created by information that is parsed by each Thread
 * using Jsoup to parse the HTML data and the Listings are pushed
 * to the database into their appropriate table, where they are
 * either added or replace the previous version of itself.
 */
public class WebScraper {

    /**
     * Class to provide easy access to links
     * that are accessed often
     */
    public static class WebLinks {
        public static final String HOMEPAGE = "http://www.wirewheel.com/";
        public static final String RACECARS = "http://www.wirewheel.com/RACING-CARS.html";
        public static final String AUSTIN = "http://www.wirewheel.com/AUSTIN-HEALEY.html";
        public static final String JAGUAR = "http://www.wirewheel.com/JAGUAR.html";
        public static final String LOTUS = "http://www.wirewheel.com/LOTUS.html";
        // public static final String JUNO = "http://www.wirewheel.com/JUNO-Race-Cars.html";
        public static final String MARCOS = "http://www.wirewheel.com/MARCOS.html";
        public static final String MINI = "http://www.wirewheel.com/MINI-Austin---Morris.html";
        public static final String MG = "http://www.wirewheel.com/MG.html";
        public static final String PANOZ = "http://www.wirewheel.com/PANOZ.html";
        public static final String TRIUMPH = "http://www.wirewheel.com/TRIUMPH.html";
        public static final String TVR = "http://www.wirewheel.com/TVR.html";
        public static final String OTHERS = "http://www.wirewheel.com/OTHER-SPORTSCARS.html";
    }

    private static final int MAX_THREADS = 5;

    private ArrayList<String> ignoreLinks;
    private Context mContext;
    private ExecutorService service;

    /**
     * Constructor for the WebScraper
     * @param context The Context this WebScraper will be used in (used to query the main database)
     */
    public WebScraper(Context context) {
        ignoreLinks = new ArrayList<>();
        mContext = context;
        init();
    }

    /**
     * Initializes the ignoreLinks list, adds links that should
     * be ignored when the WebScraper is looking for links
     */
    private void init() {
        ignoreLinks.add(WebLinks.HOMEPAGE + "Sell-Us-Your-Car-WireWheel-com.html");
        ignoreLinks.add(WebLinks.HOMEPAGE + "CONTACT---DIRECTIONS.html");
        ignoreLinks.add(WebLinks.HOMEPAGE + "PARTS-AND-ENGINES-FOR-SALE.html");
    }

    /**
     * Opens the Executor Service to allow the application
     * to make HTTP requests concurrently
     */
    public void openService() {
        service = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * Closes the Executor Service
     *
     * Times out after 5 seconds and force closes
     * all threads if it comes to that point
     */
    public void closeService() {
        try {
            service.shutdown();
            service.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            service.shutdownNow();
            Toast.makeText(mContext, "Error Occurred - Please Refresh", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the document associated with the given URL
     * @param url The URL to retrieve the document from
     * @return The document associated with the given URL, null if the URL does not exist
     * @throws IOException When the given URL does not exist
     */
    public Document get(String url) throws IOException {
        return Jsoup.connect(url).userAgent("Chrome").get();
    }

    /**
     * Returns an ArrayList of links associated with a certain make of cars
     * Ex: Scanning the WebLinks.LOTUS page will return a list of links
     * representing each listing under the Lotus make.
     * @param url The URL to parse links from
     * @return A list of links associated with the given make of cars
     */
    public ArrayList<String> getLinksFromMake(String url) {
        try {
            Document document = get(url);
            Elements elements = document.select("td.content").select("div").select("a[href");
            return parseLinksFromElements(elements);
        } catch  (IOException e) {
            Log.e("WebScraper", "Error connecting to url...");
            // Toast.makeText(mContext, "Connection Error!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayList<String> parseLinksFromElements(Elements elements) {
        ArrayList<String> links = new ArrayList<>();

        Element tempElement;
        for (int i = 0; i < elements.size(); i++) {

            tempElement = elements.get(i);
            String link = tempElement.attr("href");

            if (link.charAt(0) == '/') {
                link = WebLinks.HOMEPAGE + link.substring(1); //www.wirewheel.com/" + extension

                if (!links.contains(link) && !ignoreLinks.contains(link)) {
                    links.add(link);
                }
            }
        }

        return links;
    }

    public ArrayList<Document> getDocumentsFromLinks(ArrayList<String> urls) {

        if (urls == null) {
            return null;
        }

        ArrayList<Future<Document>> futures = new ArrayList<>();
        ArrayList<Document> documents = new ArrayList<>();

        class DocParser implements Callable<Document> {
            private String url;

            public DocParser(String url) {
                this.url = url;
            }

            @Override
            public Document call() throws IOException {
                try {
                    return get(url);
                } catch (IOException e) {
                    return null;
                }
            }
        }

        for (int i = 0; i < urls.size(); i++) {
            Callable<Document> callable = new DocParser(urls.get(i));
            Future<Document> documentFuture = service.submit(callable);
            futures.add(documentFuture);
        }

        for (Future<Document> future : futures) {
            try {
                Document document = future.get();
                if (document != null) {
                    documents.add(document);
                }
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        }

        return documents;
    }

    public void loadPage(String url, String tableId) {
        ArrayList<Document> documents = getDocumentsFromLinks(getLinksFromMake(url));
        if (documents == null) {
            return;
        }

        for (Document document : documents) {
            Listing listing = new Listing();
            listing.setLink(document.location());
            listing.setTitle(document.title());

            // Filter out "for sale" in title
            String title = listing.getTitle();
            if (title.contains("for sale")) {
                String substring = title.substring(0, title.indexOf("for") - 1);
                listing.setTitle(substring);
            }

            Elements elementsP = document.select("p");
            Elements elementsLi = document.select("li");
            for (Element element : elementsP) {
                elementsLi.add(element);
            }

            // StringBuffer buffer = new StringBuffer();

            boolean mileage = false;
            for (Element element : elementsLi) {
                String str = element.text();

                if (str.contains("$")) {
                    String regex = "\\$\\d{1,3},\\d+";
                    Matcher matcher = Pattern.compile(regex).matcher(str);

                    if (matcher.find()) {
                        listing.setPrice(matcher.group());
                    } else {
                        listing.setPrice("Unlisted Price");
                    }
                }

                if (str.contains("miles") && !mileage) {
                    String regex = "\\d{1,3},\\d+";
                    Matcher matcher = Pattern.compile(regex).matcher(str);

                    if (matcher.find()) {
                        listing.setMileage(matcher.group() + " miles");
                    } else {
                        listing.setMileage("Unlisted Mileage");
                    }
                    mileage = true;
                }
            }

            Elements elements = document.select("td.content").select("img");
            String link = WebLinks.HOMEPAGE + elements.get(3).attr("src").substring(1);
            listing.setKeyImageLink(link);

            StringBuffer buffer = new StringBuffer();
            for (int i = 3; i < elements.size(); i++) {
                buffer.append(WebLinks.HOMEPAGE + elements.get(i).attr("src").substring(1));

                if (i + 1 != elements.size()) {
                    buffer.append("|");
                }
            }
            listing.setImageString(buffer.toString());

            ListingDatabase.get(mContext).addListing(listing, tableId);
        }
    }

    public ArrayList<Listing> getListingsFromUrl(String url) {
        return null;
    }

    public ArrayList<String> getRaceCarLinks() {
        return getLinksFromMake(WebLinks.RACECARS);
    }

    public ArrayList<String> getAustinLinks() {
        return getLinksFromMake(WebLinks.AUSTIN);
    }

    public ArrayList<String> getJaguarLinks() {
        return getLinksFromMake(WebLinks.JAGUAR);
    }

    public ArrayList<String> getLotusLinks() {
        return getLinksFromMake(WebLinks.LOTUS);
    }

    /*
    public ArrayList<String> getJunoLinks() {
        return getLinksFromMake(WebLinks.JUNO);
    }
    */

    public ArrayList<String> getMarcosLinks() {
        return getLinksFromMake(WebLinks.MARCOS);
    }

    public ArrayList<String> getMiniLinks() {
        return getLinksFromMake(WebLinks.MINI);
    }

    public ArrayList<String> getMgLinks() {
        return getLinksFromMake(WebLinks.MG);
    }

    public ArrayList<String> getPanozLinks() {
        return getLinksFromMake(WebLinks.PANOZ);
    }

    public ArrayList<String> getTriumphLinks() {
        return getLinksFromMake(WebLinks.TRIUMPH);
    }

    public ArrayList<String> getTvrLinks() {
        return getLinksFromMake(WebLinks.TVR);
    }

    public ArrayList<String> getOtherLinks() {
        return getLinksFromMake(WebLinks.OTHERS);
    }
}
