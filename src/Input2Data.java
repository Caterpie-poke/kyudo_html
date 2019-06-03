import java.io.*;
import java.text.Normalizer;

public class Input2Data {
    private BufferedReader br;
    private Information info;
    private Team[] tempTeams;
    private Team[] trueTeams;
    private int teamCount;
    private int playerCount;

    public Input2Data(){
        this.info = new Information();
        this.tempTeams = new Team[20];
        for(int i=0;i<20;i++){
            this.tempTeams[i] = new Team();
        }
        this.teamCount = 0;
        this.playerCount = 0;
    }

    public void convert(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        this.br = new BufferedReader(new FileReader(file));
        try{
            String str;
            while((str = this.br.readLine()) != null){
                String normalizedStr = Normalizer.normalize(str, Normalizer.Form.NFKC);
                check(normalizedStr.trim());
            }
            this.trueTeams = new Team[this.teamCount];
            for(int i=0;i<this.teamCount;i++){
                trueTeams[i] = tempTeams[i];
            }
            this.br.close();
        }catch(Exception e){
            System.err.println("convert miss");
            e.printStackTrace();
        }
    }

    public void check(String str) throws IOException {
        String str2;
        switch (str){
            case "<info>":
                while(!(str2 = this.br.readLine()).trim().equals("</info>")){
                    if(!str2.isEmpty())
                        info.read(str2.trim());
                }
                break;
            case "<league>":
                while(!(str2 = this.br.readLine()).trim().equals("</league>")){
                    readLeague(str2.trim());
                }
                tempTeams[teamCount].setTeamType(1);
                this.teamCount++;
                break;
            case "<multi>":
                while(!(str2 = this.br.readLine()).trim().equals("</multi>")){
                    readMulti(str2.trim());
                }
                tempTeams[teamCount].setTeamType(2);
                this.teamCount++;
                break;
            case "<indiv>":
                while(!(str2 = this.br.readLine()).trim().equals("</indiv>")){
                    readIndiv(str2.trim());
                }
                tempTeams[teamCount].setTeamType(3);
                this.teamCount++;
                break;
            default:
                break;
        }
    }

    public void readLeague(String str2) throws IOException {
        if(!str2.isEmpty()) {
            switch (str2) {
                case "<player>"://indiv,multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</player>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getPlayers().add(makePlayer(str2));
                    }
                    tempTeams[teamCount].setPlayerCount(playerCount);
                    this.playerCount = 0;
                    break;
                case "<change>"://multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</change>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getChange().read(str2);
                    }
                    break;
                case "<reserve>"://multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</reserve>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getReserves().add(makeReserves(str2));
                    }
                    break;
            }
        }
    }
    public void readMulti(String str2) throws IOException {
        if(!str2.isEmpty()) {
            switch (str2) {
                case "<player>"://indiv,multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</player>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getPlayers().add(makePlayer(str2));
                    }
                    tempTeams[teamCount].setPlayerCount(playerCount);
                    this.playerCount = 0;
                    break;
                case "<change>"://multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</change>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getChange().read(str2);
                    }
                    break;
                case "<reserve>"://multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</reserve>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getReserves().add(makeReserves(str2));
                    }
                    break;
            }
        }
    }
    public void readIndiv(String str2) throws IOException {
        if(!str2.isEmpty()) {
            switch (str2) {
                case "<player>"://indiv,multi,league
                    while (!(str2 = this.br.readLine()).trim().equals("</player>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getPlayers().add(makePlayer(str2));
                    }
                    tempTeams[teamCount].setPlayerCount(playerCount);
                    this.playerCount = 0;
                    break;
                case "<header>"://indiv
                    while (!(str2 = this.br.readLine()).trim().equals("</header>")) {
                        if (!str2.isEmpty())
                            tempTeams[teamCount].getTableHeader().read(str2);
                    }
                    break;
            }
        }
    }

    public Player makePlayer(String str){
        String[] temp = str.split("[,、]",0);
        String name = temp[0].trim();
        int grade;
        try {
            grade = Integer.parseInt(temp[1].trim());
        }catch (Exception e){
            grade = 0;
        }
        String hit;
        if(temp.length==3){
            hit = temp[2].trim();
        } else {
            hit = "";
        }
        return new Player(++this.playerCount,name,grade,hit);
    }
    public Player makeReserves(String str){
        String[] temp = str.split("[,、]",0);
        String name = temp[0].trim();
        int grade;
        try {
            grade = Integer.parseInt(temp[1].trim());
        }catch (Exception e){
            grade = 0;
        }        String hit;
        if(temp.length>2){
            hit = temp[2].trim();
        } else {
            hit = "";
        }
        return new Player(name,grade,hit);
    }

    public Team[] getTeam(){
        return this.trueTeams;
    }
    public Information getInfo(){
        return this.info;
    }
    public int getTeamCount() {
        return teamCount;
    }
}
