package quagem.com.uba.model;

/**
 * {@link Recipe} Represents a recipe that the user can select.
 * It contains an id, name, ingredients list and step by step instructions.
 */

public class Recipe {

    private String mId;
    private String mName;

    public Recipe(){

    }

    public Recipe(String id, String name) {
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
