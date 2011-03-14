package websitereader;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PageManager {

    private static volatile PageManager ref;
    public final static boolean DEBUG = false;

    //First HashMap holds <GroupName, HashMap of pageInfos>
    private ConcurrentHashMap<String, ConcurrentHashMap> groups;
    //Second hold tmp variables for similar <NewURL, SuggestedGroup>.
    private ConcurrentHashMap<String, String> similar;
    private volatile GUI gui;

    private PageManager(GUI g) {
        this.groups = new ConcurrentHashMap<String, ConcurrentHashMap>();
        this.similar = new ConcurrentHashMap<String, String>();
        this.gui = g;    
    }

    public static PageManager getPageManager(GUI g) {
        if (getRef() == null) {
            ref = new PageManager(g);
        }
        return getRef();
    }

    public void addGroup(String groupName) {
        synchronized (groups) {
            getGroups().put(groupName, new ConcurrentHashMap<String, PageInfo>());
        }
    }

    public void addPage(PageInfo p) {
        synchronized (groups) {
            String groupName = this.suggestGroup(p);

            try {
                getGroups().get(groupName).put(p.getUrl().toString(), p);
            } catch (NullPointerException ex) {                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.getGui().updateWordScannerLabelDone();
    }

    public void addPage(PageInfo p, String groupName) {
        synchronized (groups) {
            if (!getGroups().containsKey(groupName)) {
                this.addGroup(groupName);
            }
            getGroups().get(groupName).put(p.getUrl().toString(), p);
        }
        this.getGui().updateWordScannerLabelDone();
    }

    public String suggestGroup(PageInfo np) {
        final PageInfo newPage = np;
        String suggestGroup = null;
        PageInfo mostSimilarPage = null;
        double mostSimilarPageValue = 99999;
        Enumeration group_ = getGroups().keys();
        while (group_.hasMoreElements()) {
            String currentGroup = group_.nextElement().toString();
            Enumeration urls = getGroups().get(currentGroup).keys();
            if (DEBUG) {
                System.out.println("GROUP: " + currentGroup);
            }
            while (urls.hasMoreElements()) {
                String currentURL = urls.nextElement().toString();
                if (DEBUG) {
                    System.out.println("CURRENTURL: " + currentURL);
                }
                PageInfo currentPage = (PageInfo) groups.get(currentGroup).get(currentURL);
                Enumeration words = newPage.getWords().keys();
                while (words.hasMoreElements()) {
                    double pageTotalCompare = 0;
                    String currentWord = words.nextElement().toString();
                    double newPageWordWeight = newPage.getWords().get(currentWord).getWordWeight();
                    if (currentPage.getWords().containsKey(currentWord)) {
                        double currentPageWordWieight = currentPage.getWords().get(currentWord).getWordWeight();
                        double weight = Math.abs(newPageWordWeight - currentPageWordWieight);
                        pageTotalCompare += weight;
                    } else {
                        pageTotalCompare += newPageWordWeight;
                    }

                    if (mostSimilarPageValue > pageTotalCompare) {
                        mostSimilarPageValue = pageTotalCompare;
                        mostSimilarPage = currentPage;
                        suggestGroup = currentGroup;
                    }
                }
            }
        }
      
        this.getGui().addToHistory("Creating New Page: " + newPage.getUrl().toString());
        this.getGui().updateWordScannerLabel(newPage.getUrl().toString());
        mostSimilarPage.getUrl().toString();       
        this.getGui().addToHistory("Most Similar Page: " + newPage.getUrl().toString());
        if (suggestGroup != null) {
            similar.put(newPage.getUrl().toString(), suggestGroup);
        } else {
            System.err.println("SOMETHING WENT REALLY REALLY WRONG AND GROUP IS NULL");
        }

        while (!similar.containsKey(newPage.getUrl().toString())) {
            //keep checking until it does
        }
        String group = similar.get(newPage.getUrl().toString());
        if (DEBUG) {
            System.out.println("Suggesting: " + group);
        }
        this.getGui().addToHistory(newPage.getUrl().toString() + " adding to group: " + group);
        this.getGui().addToHistory("");
        return group;
    }

    public void getHashedPages() {        
        Enumeration group = getGroups().keys();        
        while (group.hasMoreElements()) {
            String g = group.nextElement().toString();
            Enumeration urls = getGroups().get(g).keys();
            System.out.println("HASHEDGROUP: " + g);
            while (urls.hasMoreElements()) {
                System.out.println("Key: " + urls.nextElement().toString());
            }
        }
    }

    /**
     * @return the ref
     */
    private static PageManager getRef() {
        return ref;
    }

    /**
     * @return the groups
     */
    public ConcurrentHashMap<String, ConcurrentHashMap> getGroups() {
        return groups;
    }

    public PageInfo getPageInfo(String g, String url) {
        Enumeration urls = getGroups().get(g).keys();
        System.out.println("GROUP: " + g);
        while (urls.hasMoreElements()) {
            String tmp = urls.nextElement().toString();
            if (tmp.equals(url)) {
                return (PageInfo) urls;
            }
        }

        return null;
    }

    /**
     * @return the similar
     */
    public ConcurrentHashMap<String, String> getSimilar() {
        return similar;
    }

    /**
     * @return the gui
     */
    public GUI getGui() {
        return gui;
    }
}











