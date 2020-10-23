package com.jnelsonjava.taskmaster;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddTaskRenderOnMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addTaskRenderOnMainActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_task_button), withText("Add Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.add_task_button2), withText("Add Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextTaskTitle), withText("My Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Another Task"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextTaskTitle), withText("Another Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextTaskDescription), withText("Do something"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Do something else"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextTaskDescription), withText("Do something else"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_task_button2), withText("Add Task"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.taskTitleTextView), withText("Another Task"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Another Task")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.taskBodyTextView), withText("Do something else"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Do something else")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.taskStateTextView), withText("new"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                0)),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("new")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.taskTitleTextView), withText("My Task"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                1)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("My Task")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.taskBodyTextView), withText("Do something"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                1)),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Do something")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.taskStateTextView), withText("new"),
                        childAtPosition(
                                allOf(withId(R.id.task_fragment),
                                        childAtPosition(
                                                withId(R.id.task_list_main_recycler_view),
                                                1)),
                                2),
                        isDisplayed()));
        textView6.check(matches(withText("new")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
