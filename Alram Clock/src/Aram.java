import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Aram {


    public Aram(){

    }
    public static void main(String args[]) throws IOException, InterruptedException {



        //System.out.print(test.getHours()+" "+test.getMinutes());
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter Hours 24");
        int h = scan.nextInt();
        System.out.println(h);
        System.out.println("Enter minutes 60");
        int m = scan.nextInt();
        System.out.println(m);

 while(true) {
     Date date = new Date();
     System.out.println(date.getSeconds());
     TimeUnit.MILLISECONDS.sleep(1000);
if(date.getHours()==h&& date.getMinutes()==m) {
    Desktop des = Desktop.getDesktop();
    try {

        URI url = new URI("https://www.youtube.com/watch?v=PVx1kq0B38U");
        des.browse(url);

        break;
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }


}
 }
    }

}
