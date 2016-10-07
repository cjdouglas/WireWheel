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

/**
 * Created by Chris on 9/22/2016.
 */
public class WebScraper {

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

    public WebScraper(Context context) {
        ignoreLinks = new ArrayList<>();
        mContext = context;
        // service = Executors.newFixedThreadPool(MAX_THREADS);
        init();
    }

    public void closeService() {
        try {
            service.shutdown();
            service.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            service.shutdownNow();
            Toast.makeText(mContext, "Error Occurred - Please Refresh", Toast.LENGTH_SHORT).show();
        }
    }

    public void openService() {
        service = Executors.newFixedThreadPool(MAX_THREADS);
    }

    private void init() {
        ignoreLinks.add(WebLinks.HOMEPAGE + "Sell-Us-Your-Car-WireWheel-com.html");
        ignoreLinks.add(WebLinks.HOMEPAGE + "CONTACT---DIRECTIONS.html");
        ignoreLinks.add(WebLinks.HOMEPAGE + "PARTS-AND-ENGINES-FOR-SALE.html");
    }

    public Document get(String url) throws IOException {
        return Jsoup.connect(url).userAgent("Chrome").get();
    }

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
                // listing.setTitle(title.substring(0, title.indexOf("for sale")));
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
                    int start = str.indexOf("$");
                    int end = str.indexOf(" ", start);

                    if (end == -1) {
                        listing.setPrice(str.substring(start));
                    } else {
                        listing.setPrice(str.substring(start, end));
                    }
                }

                if (str.contains("miles") && !mileage) {
                    listing.setMileage(str);
                    mileage = true;
                }
            }

            Elements elements = document.select("td.content").select("img");

            /*
            String link = "";
            if (listing.getTitle().contains("Red Bull")) {
                link = "http://www.wirewheel.com" + elements.get(5).attr("src");
            } else {
                link = "http://www.wirewheel.com" + elements.get(4).attr("src");
            }
            */

            String link = "http://www.wirewheel.com" + elements.get(3).attr("src");

            listing.setKeyImageLink(link);

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
