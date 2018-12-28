package hulubrothers;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ReadRecord extends Thread {
    private File file;
    private ArrayList<String> arrayList;
    private Semaphore ReadMutexi;
    private int MoveIndex;
    private String name;

    ReadRecord(File FileSelected) {
        file=FileSelected;
        arrayList=new ArrayList<>();
        ReadMutexi=new Semaphore(1);
        try {
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            String str;
            for(int i=0; i<2; i++) {
                if ((str = bf.readLine()) != null) {
                    //abandon first line
                }
            }
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized int GetMoveIndex() {
        return MoveIndex;
    }

    /*public synchronized String GetName() {
        return name;
    }*/

    public synchronized void Release() {
        ReadMutexi.release();
    }

    public void run() {
        for(int i=0; i<arrayList.size(); i++) {
            try {
                ReadMutexi.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str = arrayList.get(i);
            ArrayList<String> ListPerLine=new ArrayList<>();
            String ListPerString="";
            for(int s=0; s<str.length(); s++) {
                if(str.charAt(s)==' ') {
                    ListPerLine.add(ListPerString);
                    ListPerString="";
                }
                else if(s==str.length()-1) {
                    ListPerString=ListPerString+str.charAt(s);
                    ListPerLine.add(ListPerString);
                    ListPerString="";
                }
                else {
                    ListPerString=ListPerString+str.charAt(s);
                }
            }
            final int FirstChar = Integer.parseInt(ListPerLine.get(1));
            MoveIndex = FirstChar;
            name=ListPerLine.get(2);
            System.out.println(ListPerLine.get(0)+" "+ListPerLine.get(1)+" "+name);
            Main.mutexi[Integer.parseInt(ListPerLine.get(0))].release();
        }
    }
}