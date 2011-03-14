package websitereader;

import java.util.concurrent.ThreadPoolExecutor;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

        while (frame == null) {
        }

        pageManager = PageManager.getPageManager(frame);
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//        }
        pageFetcherManager = new PageFetcherManager(pageManager);
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//        }
        threadPool = pageFetcherManager.threadPool;
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//        }
        // GUI gui = new GUI();



        try {
            pageFetcherManager.start();
            Thread.sleep(750);
            while (true) {
                Thread.sleep(900);
                frame.updateSitesParsed();
                frame.updateLinksLeft();
                frame.updatePagesLeft();
                frame.updateConfigLoadingStatus(pageFetcherManager.doneLoadingConfig());
            }



        } catch (Exception ex) {
        }
        System.out.println("Sites parsed: " + pageFetcherManager.getSitesParsed());
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new GUI();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    public static ThreadPoolExecutor getTreadPool() {
        return threadPool;
    }
    private static volatile GUI frame;
    public static volatile ThreadPoolExecutor threadPool;
    public static volatile PageManager pageManager;
    public static volatile PageFetcherManager pageFetcherManager;
}
