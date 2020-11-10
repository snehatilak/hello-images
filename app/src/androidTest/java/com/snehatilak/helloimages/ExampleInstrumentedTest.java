package com.snehatilak.helloimages;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity;
    private RecyclerView imagesRecView;
    private int recViewId;
    private int imageCount;


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.snehatilak.helloimages", appContext.getPackageName());
    }

    @Before
    public void setup() {
        this.mainActivity = this.mainActivityRule.getActivity();
        this.recViewId = R.id.recView_images;
        this.imagesRecView = this.mainActivity.findViewById(R.id.recView_images);
        this.imageCount = this.imagesRecView.getAdapter().getItemCount();
    }

    @Test
    public void reloadButton_shouldBeClickable() {
        onView(withId(R.id.btn_fab)).perform(click());
    }

    @Test
    public void recyclerView_shouldBeClickable() {
        onView(withId(R.id.btn_fab)).perform(click());
        for (int i = 0; i < this.imageCount; i++) {
            onView(withId(this.recViewId)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
        }
    }


}