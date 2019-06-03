import java.util.ArrayList;

/**
 * Created by origami on 2018/03/20.
 */
public class TableHeader {
    private ArrayList<String> headName = new ArrayList<>();
    private ArrayList<Integer> splitCount = new ArrayList<>();

    public void read(String str){
        String[] temp = str.split(",|、",0);
        if(temp.length<2)System.err.println("header miss");
        else{
            setHeadName(temp[0].trim());
            setSplitCount(Integer.parseInt(temp[1].trim()));
        }
    }

    public void setHeadName(String name) {
        this.headName.add(name);
    }
    public void setSplitCount(int count) {
        this.splitCount.add(count);
    }

    public ArrayList<String> getHeadName(){
        return this.headName;
    }
    public ArrayList<Integer> getSplitCount() {
        return splitCount;
    }
}