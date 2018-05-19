package quagem.com.uba;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void clickOnRecipe_OpensRecipeDetails() {
        // For some unknown reason to me onData won;t work on my list.
        onView(withId(R.id.recipe_list_view)).perform(click());

    }

    @After
    public void recipeDetailsActivity_Launched() {
        intended(hasComponent(new ComponentName(getTargetContext(), RecipeDetailsActivity.class)));
    }

}