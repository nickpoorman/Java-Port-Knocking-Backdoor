package hooverville.client;


import java.io.BufferedReader;
import java.io.PrintWriter;

public class InputStreamHandler extends Thread {

    private BufferedReader stdIn;
    private PrintWriter out;

    public InputStreamHandler(BufferedReader b, PrintWriter p) {
        this.stdIn = b;
        this.out = p;        
    }

    /* when run will pipe our input to the output stream */
    public void run() {        
        String fromUser;
        try {
            while ((fromUser = getStdIn().readLine()) != null) {
                //System.out.println("GETTING INPUT fromUser: " + fromUser);
                getOut().println(fromUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the stdIn
     */
    public BufferedReader getStdIn() {
        return stdIn;
    }

    /**
     * @return the out
     */
    public PrintWriter getOut() {
        return out;
    }
}
