package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;

public class PageFetcher implements Runnable {

    private final static boolean DEBUG = false;
    private boolean cont = true;
    volatile URL url;
   // volatile PageFetcherManager pageFetcherManager;
    //LinkedList<String> words;
    //volatile PageInfo newPage;
    private String pURL;
   // volatile String group;

    //public PageFetcher(String url, PageFetcherManager pfm) {
    public PageFetcher(String url) {
        //this.pageFetcherManager = pfm;
        this.pURL = url;
       // this.group = null;
    }

//    public PageFetcher(String url, PageFetcherManager pfm, String group) {
//        //this.pageFetcherManager = pfm;
//        this.pURL = url;
//        this.group = group;
//    }

    private void createPageFetcher() {
        //this.newPage = new PageInfo();
        //this.words = new LinkedList<String>();
        try {
            this.url = new URL(pURL);
        } catch (MalformedURLException e) {
            System.out.println("Not a valid url: " + url);
        }
    }

    public BufferedReader getPageContent() {
        try {
            return new BufferedReader(new InputStreamReader(this.url.openConnection().getInputStream()));
        } catch (IllegalArgumentException e) {           
        } catch (IOException e) {
            this.cont = false;            
        }
        return null;
    }

    public void run() {
        this.createPageFetcher();
        BufferedReader read = this.getPageContent();
        if (cont) {
            if (DEBUG) {
                System.out.println("PAGE STAAAAAAAAAAAARRRRRRRRRRRRRRRRTTTTTTTTTTTTT");
            }
            
//            HTMLParser parser = new HTMLParser();
//            try {
//                parser.createParser(read, pageFetcherManager, newPage);
//            } catch (Exception e) {              
//            }
//            this.pageFetcherManager.incSitesParsed();
//            for (String s : words) {
//                if (DEBUG) {
//                    System.out.println("Word: " + s);
//                }                 
//                newPage.addWord(s);
//            }
//            newPage.setUrl(url);
//            this.pageFetcherManager.getPageManager().getGui().updateWordScannerLabel(url.toString());
//            newPage.updateAllWordWeights();
//            if (group != null) {             
//                this.pageFetcherManager.getPageManager().addPage(newPage, group);
//            } else {                
//                this.pageFetcherManager.getPageManager().addPage(newPage);
//            }
            if (DEBUG) {
                System.out.println("END OF PAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            }
        }
    }
}