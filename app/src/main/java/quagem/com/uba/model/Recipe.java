package quagem.com.uba.model;

/**
 * {@link Recipe} Represents a recipe that the user can select.
 * It contains an id, name, ingredients list and step by step instructions.
 */

public class Recipe {

    private int mId;
    private String mName;

    public Recipe(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
