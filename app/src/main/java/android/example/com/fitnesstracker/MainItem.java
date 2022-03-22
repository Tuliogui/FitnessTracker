package android.example.com.fitnesstracker;

public class MainItem {

    private int id;
    private int drawableId;
    private int color;
    private int textStringId;

    public MainItem(int id, int drawableId,int textStringId , int color) {
        this.id = id;
        this.drawableId = drawableId;
        this.textStringId = textStringId;
        this.color = color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public void setDrawableId(int drawableId){
        this.drawableId = drawableId;
    }

    public void textStringId(int textStringId){
        this.textStringId = textStringId;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getTextStringId(){
        return textStringId;
    }

}
