import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame {
    static String wining = "\n" +
            "\n" +
            "__   _______ _   _   _    _ _____ _   _ _ _ \n" +
            "\\ \\ / /  _  | | | | | |  | |_   _| \\ | | | |\n" +
            " \\ V /| | | | | | | | |  | | | | |  \\| | | |\n" +
            "  \\ / | | | | | | | | |/\\| | | | | . ` | | |\n" +
            "  | | \\ \\_/ / |_| | \\  /\\  /_| |_| |\\  |_|_|\n" +
            "  \\_/  \\___/ \\___/   \\/  \\/ \\___/\\_| \\_(_|_)\n" +
            "                                            \n" +
            "                                            \n" +
            "\n";
    static char[][] help = new char[15][20];
    static int[][] markMap = new int[15][20];
    static char Player = '@';
public Main(){

    setSize(1000,1080);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



}
    public static void create(){
        for (int i=0;i<20;i++){
            for (int j=0;j<15;j++) {
                help[j][i] = '*';
                markMap[j][i] = 0;
            }
            }

    }
    public static void delete(){
        Random rand = new Random();
        int rands = rand.nextInt(3);
        int place = 4;

        for(int i =0;i<19;i++){
            if (rands ==0){
                if(place ==13){
                    help[place][i]= ' ';
                    help[place][i+1] = ' ';

                }else {
                    help[place][i] = ' ';
                    help[place][i+1] = ' ';
                    place++;

                }
            }
           else if (rands ==1){
if(place ==1){
    help[place][i]= ' ';
    help[place][i+1] = ' ';

}
else {

    help[place][i] = ' ';
    help[place][i+1] = ' ';
    place--;

}
            }

            else {

                help[place][i] = ' ';
                help[place][i+1] = ' ';
            }
             rands = rand.nextInt(3);

        }

        int count =0;
    int randy=0,randx=0;
        while(count !=1000){
            randx=rand.nextInt(19);
            randy=rand.nextInt(14);
            rands = rand.nextInt(101);
            if(rands <20){
                if(randy==0){
                    randy=1;
                }
                if(randx==0){
                    randx=1;
                }
                help[randy][randx] = ' ';
            }
            count++;

        }



    }
    public static void print(){
        for (int i=0;i<20;i++){
            for (int j=0;j<15;j++) {
                System.out.print(help[j][i]);

            }
            System.out.println();
        }


    }
    public static void move() throws InterruptedException {
        int count =0;
int playerx = 0;
int playery=4;

        help[playery][playerx]= Player;
        markMap[playery][playerx] =1;
        while(playerx!=19){
                  if (help[playery+1][playerx]!='*'&&help[playery+1][playerx]!='@'&& markMap[playery+1][playerx] ==0){
                playery++;
                help[playery][playerx]= Player;
                markMap[playery][playerx] =1;
            }
           else if (help[playery][playerx+1]!='*'&&help[playery][playerx+1]!='@'&& markMap[playery][playerx+1] ==0){
                playerx++;
                help[playery][playerx]= Player;
                markMap[playery][playerx] =1;
            }

            else if (help[playery-1][playerx]!='*'&&help[playery-1][playerx]!='@'&& markMap[playery-1][playerx] ==0){
                playery--;
                help[playery][playerx]= Player;
                markMap[playery][playerx] =1;
            }
                  else if (help[playery+1][playerx]=='@'){
                      help[playery][playerx]='$';
                      playery++;


                  }
                  else if (help[playery-1][playerx]=='@'){
                      help[playery][playerx]='$';
                      playery--;


                  }
            else if (help[playery][playerx-1]=='@'){
                help[playery][playerx]='$';
                playerx--;

            }




            print();
            count++;
           //TimeUnit.MILLISECONDS.sleep(100);


        }win();

    }
    public static void win(){
        System.out.println(wining);
    }
    public static void main(String args[]) throws InterruptedException {





        create();
        print();
        System.out.println();
        delete();
        print();
        move();

        Main main = new Main();
        main.setVisible(true);




    }
    @Override
    public void paint(Graphics t){
        super.paint(t);
        int l=0,o=0;
        for (int i=0;i<20;i++){
            for (int j=0;j<15;j++) {
                Color c =  new Color(0,0,0);
                if(help[j][i]=='*'){
                    c = Color.BLACK;
                }
                if(help[j][i]=='@'){
                    c = Color.GREEN;
                }
                if(help[j][i]=='$'){
                    c = Color.RED;
                }
                if(help[j][i]==' '){
                    c = Color.WHITE;
                }


                t.setColor(c);
                t.fillRect(30*j,30*i,30,30);
                t.setColor(Color.BLACK);
                t.drawRect(30*j,30*i,30,30);
            }
        }


    }




}
