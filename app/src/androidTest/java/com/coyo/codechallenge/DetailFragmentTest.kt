package com.coyo.codechallenge

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coyo.codechallenge.ui.MainActivity
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DetailFragmentTest {

    // Use [ActivityScenario] to create and launch the activity under test.
    val scenario = launchActivity<MainActivity>()

    @Test
    fun whenClickPostItem_shouldShowDetailFragment() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        // Detail fragment must shown, check it by an item existence
        onView(withId(R.id.description)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun whenDetailFragmentArias_shouldFetchPostAuthor() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))

        // Wait to load data
        onView(isRoot()).perform(TestUtil.waitFor(15000))

        //Check comments count should not be empty
        onView(withId(R.id.author)).check(ViewAssertions.matches(not(withText(""))))
    }

    @Test
    fun whenDetailFragmentArias_shouldFetchComments() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        // Wait to load data
        onView(isRoot()).perform(TestUtil.waitFor(15000))

        // Get comments list view item count
        var itemCounts = 0
        onView(withId(R.id.commentsListView)).check(ViewAssertions.matches(object :
            TypeSafeMatcher<View?>() {
            override fun matchesSafely(item: View?): Boolean {
                val listView = item as RecyclerView
                itemCounts = listView.adapter!!.itemCount
                return true
            }

            override fun describeTo(description: org.hamcrest.Description?) {}
        }))

        // Check item count
        assertThat(itemCounts, greaterThan(1))
    }

    @Test
    fun whenCommentsFetched_shouldSetCommentsCount() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        // Wait to load data
        onView(isRoot()).perform(TestUtil.waitFor(15000))

        //Check comments count should not be empty
        onView(withId(R.id.commentsCount)).check(ViewAssertions.matches(not(withText(""))))
    }
}