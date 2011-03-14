package websitereader;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

public class Callback extends ParserCallback {

    volatile PageFetcherManager pageFetcherManager;
    private final static boolean DEBUG = false;
    private final static boolean GETLINKS = true;
    PageInfo pageInfo;

    public Callback(PageInfo p, PageFetcherManager pfm) {
        this.pageInfo = p;
        this.pageFetcherManager = pfm;
    }

    @Override
    public void handleText(char[] data, int pos) {
        int i = 0;
        String str = "";
        for (char ch : data) {
            str += ch;
        }
        str = this.stripPunctuation(str).trim();
        if (!str.equals("")) {
            String[] tmp = str.split("\\s+");
            for (String s : tmp) {
                if (DEBUG) {
                    System.out.println("Text: " + s);
                }                
                pageInfo.addWord(s);
            }
        }
    }

    @Override
    public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
        if (GETLINKS) {
            if (t.equals(HTML.Tag.A)) {
                Object value = a.getAttribute(HTML.Attribute.HREF);
                if (value != null && value.toString().startsWith("http://")) {
                    if (DEBUG) {
                        System.out.println("FOUND URL!!!: " + value.toString());
                    }
                    pageFetcherManager.addToLinksQueue(value.toString());                    
                }
            }
        }

    }

    public String stripPunctuation(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) >= 48 && s.charAt(i) <= 57 || s.charAt(i) == 32 || s.charAt(i) >= 65 && s.charAt(i) <= 90) || (s.charAt(i) >= 97 && s.charAt(i) <= 122)) {
                sb = sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
}
