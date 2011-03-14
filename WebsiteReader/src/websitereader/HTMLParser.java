package websitereader;

import java.io.BufferedReader;
import javax.swing.text.html.*;
import java.io.IOException;

public class HTMLParser extends HTMLEditorKit {
    PageFetcherManager pageFetcherManager;
    PageInfo pageInfo;

    public HTMLParser() {
    }

    public void createParser(BufferedReader input, PageFetcherManager pfm, PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        this.pageFetcherManager = pfm;
        HTMLEditorKit.Parser parser = new HTMLParser().getParser();
        BufferedReader in = null;        
        HTMLEditorKit.ParserCallback callback = null;

        try {
            in = input;            
            callback = new Callback(pageInfo, pageFetcherManager);

            parser.parse(in, callback, true);
            in.close();

        } catch (IOException e) {           
        } catch (Exception e){            
        }
    }
}
