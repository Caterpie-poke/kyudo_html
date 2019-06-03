import java.io.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by origami on 2018/03/14.
 * for League(women3:3 or men4:4)
 */
public class PrintHtml {
    private PrintWriter pw;
    private int indentLevel;
    private Input2Data I2D;
    private String inputPath;
    private ResourceBundle rb;
    private String cssLeagueFile;
    private String cssIndivMultiFile;
    private String cssFolder;
    private String imgFolder;

    public PrintHtml(String outputPath, String inputPath){
        this.indentLevel = 0;
        this.inputPath = inputPath;
        this.I2D = new Input2Data();
        try{
            File file = new File(outputPath);
            this.pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        }catch(IOException e){
            System.out.println(e);
        }
        this.rb = ResourceBundle.getBundle("setting");
        this.cssLeagueFile = rb.getString("cssLeagueFile");
        this.cssIndivMultiFile = rb.getString("cssIndivMultiFile");
        this.cssFolder = rb.getString("cssFolder");
        this.imgFolder = rb.getString("imgFolder");
    }
    
    public void loadData() throws FileNotFoundException {
        this.I2D.convert(this.inputPath);
        for(int k=0;k<this.I2D.getTeam().length;k++) {
            for (int i = 0; i < this.I2D.getTeam()[k].getPlayers().size(); i++) {
                switch (this.I2D.getTeam()[k].getTeamType()) {
                    case 1://league
                        this.I2D.getTeam()[k].getPlayers().get(i).setPosition(I2D.getTeam()[k].getPlayerCount(), "twin");break;
                    case 2://multi
                        this.I2D.getTeam()[k].getPlayers().get(i).setPosition(I2D.getTeam()[k].getPlayerCount());break;
                    case 3://indiv
                        this.I2D.getTeam()[k].getPlayers().get(i).setPosition(I2D.getTeam()[k].getPlayerCount());break;
                    default:
                        System.err.println("Team type error. Can't load teamType.");
                }
                this.I2D.getTeam()[k].getPlayers().get(i).setReserve(I2D.getTeam()[k].getChange(),I2D.getTeam()[k].getReserves());
            }
        }
    }

    public void writeMain(){
        println("<!DOCTYPE html>");
        writeHeader();
        writeBody();
        print("</html>");
        pw.close();
    }

    public void writeHeader(){
        println("<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"content-language\" content=\"ja\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">");
        if(selector(0)==1)println("    <link rel=\"stylesheet\" href=\"%s/%s.css\">",cssFolder,cssLeagueFile);
        else println("    <link rel=\"stylesheet\" href=\"%s/%s.css\">",cssFolder,cssIndivMultiFile);
        println("</head>");
    }

    public void writeBody(){
        println("<body>");
        incIndent();
        writeTitle();
        if(selector(0)==1)writeVs();
        writeInfo();
        for(int turnCount=0;turnCount<I2D.getTeamCount();turnCount++) {
            writeTeamBlock(turnCount);
        }
        writeEnd();
        decIndent();
        println("</body>");
    }

    public void writeTitle(){
        println("<div class=\"title\">%s</div>", I2D.getInfo().getTitle());
    }
    public void writeVs(){
        println("<div class=\"vs\">%s</div>", I2D.getInfo().getVs());
    }
    public void writeInfo(){
        println("<div class=\"info\">%s</div>", I2D.getInfo().getOthers());
    }

    public void writeTeamBlock(int turnCount){
        println("<div class=\"team_block\">");
        incIndent();
        writeTeam(turnCount);
        writeResult(turnCount);
        if(selector(turnCount)==1)writeReserve(turnCount);
        decIndent();
        println("</div>");
    }

    public void writeTeam(int turnCount){
        try {
            String turn = "";
            if (selector(turnCount) == 1) {
                switch (turnCount) {
                    case 0:
                        turn += "先攻　";
                        break;
                    case 1:
                        turn += "後攻　";
                        break;
                    default:
                        break;
                }
            }
            String teamName = I2D.getInfo().getTeams().get(turnCount);
            println("<div class=\"team\">%s%s</div>", turn, teamName);
        }catch (Exception e){
            println("<div class=\"team\">チーム名</div>");
        }
    }

    public void writeResult(int turnCount){
        println("<div class=\"result\">");
        incIndent();
        writeTable(turnCount);
        decIndent();
        println("</div>");
    }

    public void writeReserve(int turnCount){
        println("<div class=\"reserve\">");
        incIndent();
        writeResAndMvp(turnCount);
        decIndent();
        println("</div>");
    }
    public void writeResAndMvp(int turnCount){
        try {
            StringBuilder strRes = new StringBuilder();
            StringBuilder strMvp = new StringBuilder();
            for (int i = 0; i < I2D.getTeam()[turnCount].getReserveList().size(); i++) {
                strRes.append(I2D.getTeam()[turnCount].getReserveList().get(i));
                strRes.append("<br>");
            }
            println(strRes.toString());
            println("<br>");
            for (int i = 0; i < I2D.getTeam()[turnCount].getMvpList().size(); i++) {
                strMvp.append(I2D.getTeam()[turnCount].getMvpList().get(i));
                strMvp.append("<br>");
            }
            println(strMvp.toString());
        }catch (Exception e){
            System.err.println("控え不明");
            println("（控え）<br>");
        }
    }

    public void writeTable(int turnCount){
        println("<table>");
        incIndent();
        writeTrHeader(turnCount);
        switch (selector(turnCount)){
            case 1://league
                for(int i=0;i<I2D.getTeam()[turnCount].getPlayerCount()/2;i++){
                    writeTrPlayer(turnCount, i);
                }
                writeTrSum(turnCount);
                for(int i=I2D.getTeam()[turnCount].getPlayerCount()/2;i<I2D.getTeam()[turnCount].getPlayerCount();i++){
                    writeTrPlayer(turnCount, i);
                }
                break;
            case 2://multi
                for(int i=0;i<I2D.getTeam()[turnCount].getPlayerCount();i++){
                    writeTrPlayer(turnCount, i);
                }
                writeTrSum(turnCount);
                break;
            case 3://indiv
                for(int i=0;i<I2D.getTeam()[turnCount].getPlayerCount();i++){
                    writeTrPlayer(turnCount, i);
                }
                break;
            default:
                System.err.println("Cant't switch teamtype");
        }
        decIndent();
        println("</table>");
    }

    public void writeTrHeader(int turnCount){
        println("<tr class=\"header\">");
        incIndent();
        if(selector(turnCount)!=3)println("<th class=\"position\"></th>");
        println("<th class=\"name\">氏名</th>");
        println("<th class=\"grade\">学年</th>");
        if(selector(turnCount)==3) {
            for(int i=0;i<I2D.getTeam()[turnCount].getTableHeader().getHeadName().size();i++) {
                println("<th class=\"set\">%s</th>",I2D.getTeam()[turnCount].getTableHeader().getHeadName().get(i));
            }
        } else {
            for (int i = 0; i < I2D.getTeam()[turnCount].getPlayers().get(0).getCommonSet(); i++) {
                println("<th class=\"set\">%s立目</th>", i + 1);
            }
        }
        println("<th class=\"total\">合計</th>");
        decIndent();
        println("</tr>");
    }

    public void writeTrPlayer(int turnCount, int order){
        println("<tr>");
        incIndent();
        if(selector(turnCount)!=3){
            println("<td>%s</td>", I2D.getTeam()[turnCount].getPlayers().get(order).getPositionStr());
        }
        println("<td>%s</td>", I2D.getTeam()[turnCount].getPlayers().get(order).getName());
        println("<td>%s</td>", I2D.getTeam()[turnCount].getPlayers().get(order).getGrade());
        if(selector(turnCount)==1 || selector(turnCount)==2) hit(turnCount, I2D.getTeam()[turnCount].getPlayers().get(order));
        else if(selector(turnCount)==3) hit(turnCount, order);
        println("<td>%s</td>", I2D.getTeam()[turnCount].getPlayers().get(order).getTotal());
        decIndent();
        println("</tr>");
    }
    public void hit(int turnCount, Player player){
        Change cg = I2D.getTeam()[turnCount].getChange();
        Player target = player;
        for(int i=0;i<player.getCommonSet();i++){
            Player reserve = cg.compare(target, i, I2D.getTeam()[turnCount].getReserves());
            if(reserve!=null){
                target = reserve;
                target.adjustScore2by2(i);
            }
            print("<td>");
            if(cg.check()){nonTabPrint("<div class=\"change\"><p>%s</p>",target.getFirstName());cg.checkOn();}
            nonTabPrint("<img src=\"%s/%s.gif\">",this.imgFolder,player.getScore2by2(2*i));
            nonTabPrint("<img src=\"%s/%s.gif\">",this.imgFolder,player.getScore2by2(2*i+1));
            if(cg.check()){nonTabPrint("</div>");}
            nonTabPrintln("</td>");
        }
    }
    public void hit(int turnCount, int order){
        int counter=0;
        for(int k=0;k<I2D.getTeam()[turnCount].getTableHeader().getSplitCount().size();k++) {
            print("<td>");
            for (int i = 0; i < Math.ceil((double)(I2D.getTeam()[turnCount].getTableHeader().getSplitCount().get(k)) / 2.0); i++) {
                nonTabPrint("<img src=\"%s/%s.gif\">",this.imgFolder, I2D.getTeam()[turnCount].getPlayers().get(order).getScore2by2(counter++));
            }
            nonTabPrintln("</td>");
        }
    }

    public void writeTrSum(int turnCount){
        if(selector(turnCount)==1) {
            int[][] sum = calc3row(turnCount);
            writeTrMiniSum(sum[0]);
            writeTrMiniSum(sum[1]);
            writeTrMiniSum(sum[2]);
        }
        else if(selector(turnCount)==2){
            int[] sum = calc1row(turnCount);
            writeTrMiniSum(sum);
        }
    }
    public void writeTrMiniSum(int[] ssum){
        int tempSum = 0;
        println("<tr>");
        incIndent();
        println("<td></td>");
        println("<td></td>");
        println("<td></td>");
        tempSum+=ssum[0];
        println("<td>%s</td>",ssum[0]);
        for(int i=1;i<ssum.length;i++) {
            tempSum += ssum[i];
            println("<td>%s/%s</td>", ssum[i], tempSum);
        }
        println("<td>%s</td>",tempSum);
        decIndent();
        println("</tr>");
    }
    public int[][] calc3row(int turnCount){
        int[][] table = new int[3][5];
        for(int i=0;i<5;i++){
            int tempA = 0;
            int tempB = 0;
            for(int k=0;k<I2D.getTeam()[turnCount].getPlayerCount()/2;k++){
                tempA += I2D.getTeam()[turnCount].getPlayers().get(k).getScore(i);
            }
            table[0][i] = tempA;
            for(int k=I2D.getTeam()[turnCount].getPlayerCount()/2;k<I2D.getTeam()[turnCount].getPlayerCount();k++){
                tempB += I2D.getTeam()[turnCount].getPlayers().get(k).getScore(i);
            }
            table[2][i] = tempB;
            table[1][i] = tempA+tempB;
        }
        return table;
    }
    public int[] calc1row(int turnCount){
        int[] table = new int[I2D.getTeam()[turnCount].getPlayers().get(0).getCommonSet()];
        for(int i=0;i<table.length;i++){
            int tempA = 0;
            for(int k=0;k<I2D.getTeam()[turnCount].getPlayerCount();k++){
                tempA += I2D.getTeam()[turnCount].getPlayers().get(k).getScore(i);
            }
            table[i] = tempA;
        }
        return table;
    }

    public void writeEnd(){
        if(selector(0)==1) {
            int teamTotalFirst = 0;
            int teamTotalSecond = 0;
            for (int i = 0; i < I2D.getTeam()[0].getPlayerCount(); i++) {
                teamTotalFirst += I2D.getTeam()[0].getPlayers().get(i).getTotal();
            }
            for (int i = 0; i < I2D.getTeam()[1].getPlayerCount(); i++) {
                teamTotalSecond += I2D.getTeam()[1].getPlayers().get(i).getTotal();
            }
            String winner;
            winner = teamTotalFirst >= teamTotalSecond ? I2D.getInfo().getTeams().get(0) : I2D.getInfo().getTeams().get(1);
            println("<div class=\"end\">結果：　%s　の勝利</div>", winner);
        } else {
            println("<div class=\"end\">");
            incIndent();
            println("結果");
            println("（ここに記入）");
            decIndent();
            println("</div>");
        }

    }

    public void incIndent(){
        this.indentLevel++;
    }
    public void decIndent(){
        this.indentLevel--;
    }
    public int selector(int turnCount){
        return this.I2D.getTeam()[turnCount].getTeamType();
    }
    public void print(String format, Object... args){
        for(int i=0;i<this.indentLevel;i++){pw.print("    ");}
        pw.printf(format,args);
    }
    public void nonTabPrint(String format, Object... args){
        pw.printf(format,args);
    }
    public void println(String format, Object... args){
        for(int i=0;i<this.indentLevel;i++){pw.print("    ");}
        pw.printf(format,args);
        pw.print("\n");
    }
    public void nonTabPrintln(String format, Object... args){
        pw.printf(format,args);
        pw.print("\n");
    }
}
