import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.ArrayList;

/**
 * Created by origami on 2017/12/13.
 */
public class Player {
    private String positionStr;
    private int positionNum;
    private String name;
    private String firstName;
    private int grade;
    private ArrayList<Integer> score = new ArrayList<>();
    private ArrayList<String> score2by2 = new ArrayList<>();
    private int totalHit;
    private int totalNum;
    private int commonSet;
    private boolean mvp;
    private static int amountOfPlayers=0;
    private Player reserve = null;
    private int changeTiming;

    //players
    public Player(int position, String name, int grade, String hit){
        this.name = name;
        setFirstName();
        this.grade = grade;
        this.totalHit = 0;
        this.totalNum =0;
        this.commonSet = 0;
        this.mvp = false;
        this.positionNum = position;
        this.setScore(hit);
        amountOfPlayers++;
        this.changeTiming = -1;
    }
    //reserves
    public Player(String name, int grade, String hit){
        this.name = name;
        setFirstName();
        this.grade = grade;
        this.totalHit = 0;
        this.mvp = false;
        this.totalNum =0;
        setScore(hit);
    }

    //setProcess
    public void setPosition(int mother){
        int child = this.positionNum;
        if(mother%2==1 && child==(mother/2 + 1))
            this.positionStr = "中";
        else if(child==(mother-1))
            this.positionStr = "落前";
        else if(child==1)
            this.positionStr = "大前";
        else if(child==mother)
            this.positionStr = "落";
        else
            this.positionStr = numConverter(child) + "的";
    }
    public void setPosition(int mother, String twin){
        int child = this.positionNum;
        if(child > (mother/2)){
            child = mother-child+1;
        }
        mother = mother/2;
        if(mother%2==1 && child==(mother/2 + 1))
            this.positionStr = "中";
        else if(child==(mother-1))
            this.positionStr = "落前";
        else if(child==1)
            this.positionStr = "大前";
        else if(child==mother)
            this.positionStr = "落";
        else
            this.positionStr = numConverter(child) + "的";
    }
    public void setName(String name){
        this.name = name;
    }
    public static void setAmountOfPlayers(int amountOfPlayers) {
        Player.amountOfPlayers = amountOfPlayers;
    }
    public void setFirstName(){
        this.firstName = this.name.split("[ 　]",0)[0];
    }
    public void setGrade(int grade){
        this.grade = grade;
    }
    public void setScore(String hit){
        this.totalNum = hit.length();
        char[] hits = hit.toCharArray();
        StringBuilder set4 = new StringBuilder();
        StringBuilder set2 = new StringBuilder();
        int tempNum = 0;
        for(char ch:hits){
            set4.append(ch);
            set2.append(ch);
            if(ch=='1')tempNum++;
            if(set2.length()==2){
                this.score2by2.add(set2.toString());
                set2.delete(0,2);
            }
            if(set4.length()==4){
                this.score.add(tempNum);
                set4.delete(0,4);
                tempNum = 0;
            }
        }
        if(set2.length()!=0)this.score2by2.add(set2.toString());
        if(set4.length()!=0)this.score.add(tempNum);
        setTotal();
        this.commonSet = this.score.size();
    }
    public void setTotal(){
        int total = 0;
        for(int i=0;i<this.score.size();i++){
            total = total + this.score.get(i);
        }
        this.totalHit = total;
        checkMvp();
    }
    public void adjustScore2by2(int times){
        for(int i=0;i<times;i++){
            this.score2by2.add(0,"none");
        }
    }
    public void adjustScore(int times){
        for(int i=0;i<times;i++){
            this.score.add(0,-1);
        }
    }
    public void checkMvp(){
        if(this.totalNum>=16){
            double x = (double)this.totalHit;
            double y = (double)this.totalNum;
            if(x/y >= 0.75){this.mvp = true;}
        }
    }
    public void setReserve(Change cg, ArrayList<Player> reserves){
        for (int i=0;i<cg.getBefore().size();i++){
            if(this.getName().equals(cg.getBefore().get(i))){
                for(int k=0;k<reserves.size();k++){
                    if(reserves.get(k).getName().equals(cg.getAfter().get(i))){
                        this.reserve = reserves.get(k);
                        this.changeTiming = cg.getTiming()[i];
                        this.reserve.adjustScore2by2(this.changeTiming-1);
                        this.reserve.adjustScore(this.changeTiming-1);
                        this.reserve.setReserve(cg,reserves);
                    }
                }
            }
        }
    }

    //getProcess
    public String getPositionStr(){
        return this.positionStr;
    }
    public String getName(){
        return this.name;
    }
    public String getFirstName() {
        return firstName;
    }
    public int getGrade(){
        return this.grade;
    }
    public int getScore(int order){
        if(order>=this.score.size()){
            return this.reserve.getScore(order);
        }
        return this.score.get(order);
    }
    public String getScore2by2(int order){
        if (order>=this.score2by2.size()) {
            if (reserve!=null)return reserve.getScore2by2(order);
            else return "none";
        }
        return this.score2by2.get(order);
    }
    public int getTotal(){
        if(this.changeTiming>0) return this.totalHit + this.reserve.getTotal();
        return this.totalHit;
    }
    public static int getAmountOfPlayers() {
        return amountOfPlayers;
    }
    public boolean getMvp(){
        return this.mvp;
    }
    public Player getReserve() {
        return reserve;
    }
    public int getCommonSet() {
        if(reserve==null) return commonSet;
        else return commonSet + reserve.getCommonSet();
    }

    //漢字変換
    public String numConverter(int i){
        if(i==2)
            return "二";
        else if(i==3)
            return "三";
        else if(i==4)
            return "四";
        else if(i==5)
            return "五";
        else if(i==6)
            return "六";
        else if(i==7)
            return "七";
        else if(i==8)
            return "八";
        else if(i==9)
            return "九";
        else
            return "◯";
    }

}
