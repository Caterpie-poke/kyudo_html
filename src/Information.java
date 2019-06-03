import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by origami on 2018/03/14.
 */
public class Information {
    private String title;
    private int year,month,date;
    private String dayOfWeek;
    private String weather;
    private String place;
    private ArrayList<String> teams = new ArrayList<>();
    private int counter = 0;
    private String first,second;

    public Information(){
        this.title = "タイトル未入力";
        this.year = 2000;
        this.month = 1;
        this.date = 1;
        this.dayOfWeek = "不明";
        this.weather = "未入力";
        this.place = "未入力";
        this.first = "未入力";
        this.second = "未入力";
    }

    public void read(String str){
        switch (this.counter){
            case 0://title
                setTitle(str);this.counter++;break;
            case 1://date
                setYMD(str);this.counter++;break;
            case 2://weather
                setWeather(str);this.counter++;break;
            case 3://place
                setPlace(str);this.counter++;break;
            default ://team
                setTeams(str);this.counter++;break;
        }
    }

    public void setTitle(String str){
        this.title = str;
    }
    public void setYMD(String str){
        this.year = Integer.parseInt(str.split("[/ ]",0)[0]);
        this.month = Integer.parseInt(str.split("[/ ]",0)[1]);
        this.date = Integer.parseInt(str.split("[/ ]",0)[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month-1, this.date);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                this.dayOfWeek = "日";break;
            case Calendar.MONDAY:
                this.dayOfWeek = "月";break;
            case Calendar.TUESDAY:
                this.dayOfWeek = "火";break;
            case Calendar.WEDNESDAY:
                this.dayOfWeek = "水";break;
            case Calendar.THURSDAY:
                this.dayOfWeek = "木";break;
            case Calendar.FRIDAY:
                this.dayOfWeek = "金";break;
            case Calendar.SATURDAY:
                this.dayOfWeek = "土";break;
            default:
                break;
        }
    }
    public void setWeather(String str){
        this.weather = str;
    }
    public void setPlace(String str){
        this.place = str;
    }
    public void setTeams(String team) {
        this.teams.add(team);
    }

    public String getTitle(){
        return this.title;
    }
    public ArrayList<String> getTeams() {
        return teams;
    }
    public String getOthers(){
        if(this.counter>3) {
            return String.format("%s年%s月%s日（%s）　天気：%s　於：%s", this.year, this.month, this.date, this.dayOfWeek, this.weather, this.place);
        }
        else{
            return "　年　月　日（）　天気：　於：";
        }
    }
    public String getVs(){
        return String.format("%s　対　%s",teams.get(0),teams.get(1));
    }
}
