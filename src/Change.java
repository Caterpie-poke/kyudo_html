import javax.print.attribute.standard.PrinterLocation;
import java.util.ArrayList;

/**
 * Created by origami on 2018/03/14.
 */
public class Change {
    private ArrayList<String> before;
    private ArrayList<String> after;
    private int[] timing;
    private boolean changeExist;
    private int amountOfChanges;

    public Change(){
        this.before = new ArrayList<>();
        this.after = new ArrayList<>();
        this.timing = new int[5];
        this.changeExist = false;
        this.amountOfChanges = 0;
    }

    public void read(String str){
        String[] temp = str.split("[,、]",0);
        this.before.add(temp[0].trim());
        this.timing[amountOfChanges] = Integer.parseInt(temp[1].trim());
        this.after.add(temp[2].trim());
        this.amountOfChanges++;
    }

    public Player compare(Player target, int order, ArrayList<Player> reserves){
        for(int i=0;i<this.before.size();i++){
            if(this.before.get(i).equals(target.getName()) && this.timing[i]==order+1){
                for(int k=0;k<reserves.size();k++) {
                    if(reserves.get(k).getName().equals(this.after.get(i))){
                        this.changeExist = true;
                        return reserves.get(k);
                    }
                }
            }
        }
        return null;
    }

    public boolean check(){
        if(this.changeExist){
            this.changeExist=false;
            return true;
        }
        return false;
    }
    public void checkOn(){
        this.changeExist=true;
    }

    public ArrayList<String> getAfter() {
        return after;
    }
    public ArrayList<String> getBefore() {
        return before;
    }
    public int getAmountOfChanges() {
        return amountOfChanges;
    }
    public int[] getTiming() {
        return timing;
    }
}
