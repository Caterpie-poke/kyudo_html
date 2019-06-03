import java.io.*;
import java.util.Scanner;

/**
 * Created by origami on 2017/12/13.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        String inputFilePath = "";
        String outputFilePath = "";

        if(args.length==1){
            inputFilePath = args[0];
            outputFilePath = autoFileNaming(inputFilePath);
        }
        else if(args.length==2) {
            inputFilePath = args[0];
            outputFilePath = args[1];
        }
        else{
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("入力ファイルを指定してください\n>>");
                String str = br.readLine();
                String[] selfArgs = str.trim().split(" |,",0);
                if(selfArgs.length==1){
                    inputFilePath = selfArgs[0];
                    outputFilePath = autoFileNaming(inputFilePath);
                }
                else if(selfArgs.length>=2){
                    inputFilePath = selfArgs[0];
                    outputFilePath = selfArgs[1];
                }
                else{
                    System.err.println("Not allowed input stream.");
                }
            }catch(IOException e){
                System.out.println("Exception :" + e);
            }
        }

        try{
            PrintHtml main = new PrintHtml(outputFilePath+".html", inputFilePath);
            main.loadData();
            main.writeMain();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Press Enter");
            Scanner scanner = new Scanner(System.in);
        }
    }

    public static String autoFileNaming(String inputPath){
        String[] temp = inputPath.split("[/¥]",0);
        return temp[temp.length-1].split("[.]",0)[0];
    }
}
