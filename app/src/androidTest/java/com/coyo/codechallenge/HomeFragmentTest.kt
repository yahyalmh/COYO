package com.coyo.codechallenge

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.coyo.codechallenge.ui.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    // Use [ActivityScenario] to create and launch the activity under test.
    val scenario = launchActivity<MainActivity>()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.coyo.codechallenge", appContext.packageName)
    }

    @Test
    fun fetchAllPosts_shouldNotPassMaxCount() {
        for (i in 1..10) {
            // Fetch ten item each time
            onView(withId(R.id.postsListView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>((i * 10), scrollTo()))

            // Wait till data loaded
            onView(isRoot()).perform(waitFor(2000))
        }

        // Get post list view item count
        var itemCounts = 0
        onView(withId(R.id.postsListView)).check(ViewAssertions.matches(object :
            TypeSafeMatcher<View?>() {
            override fun matchesSafely(item: View?): Boolean {
                val listView = item as RecyclerView
                itemCounts = listView.adapter!!.itemCount
                return true
            }

            override fun describeTo(description: org.hamcrest.Description?) {}
        }))

        // Check item count with maximum
        val maxItemCount = 101
        assertEquals(maxItemCount, itemCounts)
    }

    @Test(expected = PerformException::class)
    fun scrollToPosition_doesNotExist() {
        // Fetch all posts
        for (i in 1..10) {
            onView(withId(R.id.postsListView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>((i * 10), scrollTo()))
            onView(isRoot()).perform(waitFor(2000))
        }

        // Scroll to item's position which not exist
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(101, scrollTo()))
    }

    @Test(expected = PerformException::class)
    fun itemWithText_doesNotExist() {
        // Attempt to scroll to an item that contains the special text.
        onView(withId(R.id.postsListView))
            .perform(
                scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Not in list"))
                )
            )
    }

    /**
     * Perform action of waiting for a specific time.
     */
    private fun waitFor(millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }

    @Test
    fun whenClickPostItem_shouldShowDetailFragment() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        // Detail fragment must shown, check it by an item existence
        onView(withId(R.id.description)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun whenDetailFragmentArias_shouldFetchCommentsCount() {
        // Click on an item in post list view
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        // Wait to load data
        onView(isRoot()).perform(waitFor(15000))

        //Check comments count should not be empty
        onView(withId(R.id.commentsCount)).check(ViewAssertions.matches(not(withText(""))));
    }
}