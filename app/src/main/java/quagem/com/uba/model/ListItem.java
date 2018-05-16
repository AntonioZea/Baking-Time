package quagem.com.uba.model;

public class ListItem {

    private String mId;
    private String mName;

    public ListItem(){
    }

    public ListItem(String id, String name) {
        mId = id;
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
