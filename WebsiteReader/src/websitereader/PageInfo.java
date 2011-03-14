package websitereader;

import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class PageInfo {

    private URL url;
    //Hold all words on page & how many time it appears
    private ConcurrentHashMap<String, Tuple> words;
    String group;

    //Hold number of words on page
    int numWordsOnPage;
    
    public PageInfo() {
        this.words = new ConcurrentHashMap<String, Tuple>();
        this.url = null;
        this.numWordsOnPage = 0;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    public void addWord(String word) {        
        Tuple t = getWords().put(word, new Tuple(1));
        if (t != null) {
            t.getAndIncrement();
            words.replace(word, t);
        }
        this.numWordsOnPage++;
    }

    public void updateAllWordWeights() {
        Enumeration<String> word = this.getWords().keys();
        while (word.hasMoreElements()) {
            String tmp = word.nextElement();           
            updateWordWeight(tmp);
        }
    }

    public void updateWordWeight(String w) {
        if (getWords().containsKey(w)) {           
            double d = (double)this.getWords().get(w).getWordCount() / (double)this.getWords().size();
            getWords().get(w).setWordWeight(d);
        }
    }

    public String wordsToString() {
        String tmp = "";
        Enumeration<String> word = this.getWords().keys();
        while (word.hasMoreElements()) {
            tmp += word.nextElement() + " ";
        }

        return tmp;
    }

    /**
     * @return the words
     */
    public ConcurrentHashMap<String, Tuple> getWords() {
        return words;
    }

    public class Tuple {

        private int wordCount;
        private double wordWeight;

        public Tuple(int wordCount) {
            this.wordCount = wordCount;
        }

        /**
         * @return the wordCount
         */
        public int getWordCount() {
            return wordCount;
        }

        /**
         * @param wordCount the wordCount to set
         */
        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        /**
         * @return the wordWeight
         */
        public double getWordWeight() {
            return wordWeight;
        }

        /**
         * @param wordWeight the wordWeight to set
         */
        public void setWordWeight(double wordWeight) {
            this.wordWeight = wordWeight;
        }

        /**
         * Atomically increments by one the current value of wordCount.
         *
         * @return the previous value
         */
        public int getAndIncrement() {
            int i = this.wordCount;
            this.wordCount++;            
            return i;
        }
    }
}














