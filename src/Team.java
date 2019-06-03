import java.util.*;

/**
 * Created by origami on 2018/03/15.
 */
public class Team {
    private ArrayList<Player> players;
    private int playerCount;
    private Change cg;
    private TableHeader tableHeader;
    private ArrayList<Player> reserves;
    private int teamType;//1:league, 2:multi, 3:indiv

    public Team(){
        this.players = new ArrayList<>();
        this.playerCount = 0;
        this.cg = new Change();
        this.tableHeader = new TableHeader();
        this.reserves = new ArrayList<>();
        this.teamType = -1;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public ArrayList<Player> getReserves() {
        return reserves;
    }
    public Change getChange() {
        return cg;
    }
    public int getPlayerCount() {
        return playerCount;
    }
    public ArrayList<String> getReserveList(){
        ArrayList<String> rsvArray = new ArrayList<>();
        rsvArray.add("（控え）");
        for(int i=0;i<reserves.size();i++){
            rsvArray.add(String.format("%s（%s）",reserves.get(i).getName(),reserves.get(i).getGrade()));
        }
        return rsvArray;
    }
    public ArrayList<String> getMvpList(){

        ArrayList<String> mvpArray = new ArrayList<>();
        List mvps = new ArrayList<Mvp>();
        mvpArray.add("成績優秀者");
        for(int i=0;i<players.size();i++){
            if(players.get(i).getMvp()){
                mvpArray.add(String.format("%s（%s）",players.get(i).getName(),players.get(i).getGrade()));
                mvps.add(new Mvp(players.get(i).getName(),players.get(i).getGrade(),players.get(i).getTotal()));
            }
        }
        for(int i=0;i<reserves.size();i++){
            if(reserves.get(i).getMvp()) {
                mvpArray.add(String.format("%s（%s）", reserves.get(i).getName(), reserves.get(i).getGrade()));
                mvps.add(new Mvp(reserves.get(i).getName(),reserves.get(i).getGrade(),reserves.get(i).getTotal()));
            }
        }

//        Collections.sort(mvps, new mvpForSort());
//        for(Object m : mvps){
//            System.out.println(m.toString());
//        }

        return mvpArray;
    }
    public TableHeader getTableHeader() {
        return tableHeader;
    }
    public int getTeamType() {
        return teamType;
    }

    public class Mvp{
        private String name;
        private int grade;
        private int hit;

        public Mvp(String name, int grade, int hit) {
            this.name = name;
            this.grade = grade;
            this.hit = hit;
        }

        public int getGrade() {
            return grade;
        }
        public int getHit() {
            return hit;
        }
        public String getName() {
            return name;
        }
        @Override
        public String toString(){
            return String.format("%s（%s）",this.name,this.grade);
        }
    }
    public class mvpForSort implements Comparator<Mvp>{
        @Override
        public int compare(Mvp obj1, Mvp obj2) {
            if(obj1.grade<obj2.grade){
                return -1;
            }else if(obj1.grade>obj2.grade){
                return 1;
            }else{
                String str1 = String.valueOf(obj1.getHit());
                String str2 = String.valueOf(obj2.getHit());
                return str1.compareTo(str2);
            }
        }
    }


}
