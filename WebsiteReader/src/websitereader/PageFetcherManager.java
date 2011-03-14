package websitereader;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PageFetcherManager extends Thread {

    int corePoolSize = 1000;
    int maxPoolSize = 3000;
    long keepAliveTime = 10500;
    volatile ThreadPoolExecutor threadPool = null;
    volatile ThreadPoolExecutor priorityThreadPool = null;
    private LinkedBlockingQueue<PageFetcher> queue;
    private LinkedBlockingQueue<PageFetcher> priorityQueue;
    private volatile PageManager pageManager;
    private volatile LinkedBlockingQueue<String> links;
    private volatile LinkedBlockingQueue<String> priorityLinks;
    private int sitesParsed = 0;
    private int pagesThrownAway = 0;
    private volatile boolean spider = false;
    Vector<String> startURLS;

    public PageFetcherManager(PageManager p) {
        this.pageManager = p;
        this.initializeQueues();
        this.initializeExecutor();
        this.getPageManager().getGui().setPageFetcherManager(this);
    }

    private void initializeExecutor() {
        threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS, (BlockingQueue) getQueue());
        priorityThreadPool = new ThreadPoolExecutor(4, 6,
                keepAliveTime, TimeUnit.MILLISECONDS, (BlockingQueue) getPriorityQueue());
    }

    private void initializeQueues() {
        queue = new LinkedBlockingQueue<PageFetcher>();
        priorityQueue = new LinkedBlockingQueue<PageFetcher>();
        links = new LinkedBlockingQueue<String>();
        priorityLinks = new LinkedBlockingQueue<String>();
    }

    public void addToLinksQueue(String url) {
        try {
            getLinks().put(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToPriorityLinksQueue(String url) {
        try {
            getPriorityLinks().put(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean doneLoadingConfig() {
        for (String s : startURLS) {
            String[] tmp = s.split("e;.!!");
            if (!this.getPageManager().getGroups().containsKey(tmp[0])) {
                return false;
            }
            if (!this.getPageManager().getGroups().get(tmp[0]).containsKey(tmp[1])) {
                return false;
            }
        }
        return true;
    }

    private void fetch(String configFile) {
        startURLS = new Vector<String>();
        try {
            File config = new File(configFile);
            Scanner sc = new Scanner(config);
            String currentGroup = null;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("[") && line.endsWith("]")) {
                    line = line.replace("[", "");
                    line = line.replace("]", "");
                    currentGroup = line;
                } else if (line.startsWith("http://")) {
                    try {
                        startURLS.add(currentGroup + "e;.!!" + line);
                        threadPool.submit(new PageFetcher(line, this, currentGroup));
                    } catch (RejectedExecutionException e) {
                        this.incPagesThrownAway();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.fetch("links.conf");

        while (!this.doneLoadingConfig()) {
        }

        if (this.doneLoadingConfig()) {
            //tell it to fetch a new page
            //priorityThreadPool.submit(new PageFetcher("http://www.oreillynet.com/pub/d/826", this));

            for (;;) {
                try {
                    if (!this.getPriorityLinks().isEmpty()) {
                        priorityThreadPool.submit(new PageFetcher(getPriorityLinks().take(), this));
                    }
                } catch (RejectedExecutionException e) {
                    this.incPagesThrownAway();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (this.isSpider()) {
                    try {
                        threadPool.submit(new PageFetcher(getLinks().take(), this));
                    } catch (RejectedExecutionException e) {
                        this.incPagesThrownAway();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void shutDown() {
        threadPool.shutdown();
    }

    /**
     * @return the sitesParsed
     */
    public int getSitesParsed() {
        return sitesParsed;
    }

    /**
     * @param sitesParsed the sitesParsed to set
     */
    public void incSitesParsed() {
        this.sitesParsed++;
    }

    /**
     * @return the pageManager
     */
    public PageManager getPageManager() {
        return pageManager;
    }

    /**
     * @return the queue
     */
    public LinkedBlockingQueue<PageFetcher> getQueue() {
        return queue;
    }

    /**
     * @return the links
     */
    public LinkedBlockingQueue<String> getLinks() {
        return links;
    }

    /**
     * @return the pagesThrownAway
     */
    public int getPagesThrownAway() {
        return pagesThrownAway;
    }

    /**
     * @param pagesThrownAway the pagesThrownAway to set
     */
    public void incPagesThrownAway() {
        this.pagesThrownAway++;
    }

    /**
     * @return the spider
     */
    public boolean isSpider() {
        return spider;
    }

    /**
     * @param spider the spider to set
     */
    public void setSpider(boolean spider) {
        this.spider = spider;
    }

    /**
     * @return the priorityLinks
     */
    public LinkedBlockingQueue<String> getPriorityLinks() {
        return priorityLinks;
    }

    /**
     * @return the priorityQueue
     */
    public LinkedBlockingQueue<PageFetcher> getPriorityQueue() {
        return priorityQueue;
    }
}
