package com.coyo.codechallenge

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.coyo.codechallenge.ui.MainActivity
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
    fun fetchAllPosts_shouldNotPassMaxCount() {
        for (i in 1..10) {
            // Fetch ten item each time
            onView(withId(R.id.postsListView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>((i * 10), scrollTo()))

            // Wait till data loaded
            onView(isRoot()).perform(TestUtil.waitFor(4000))
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

        // Check the item count against maximum item count
        val maxItemCount = 101
        assertEquals(maxItemCount, itemCounts)
    }

    @Test(expected = PerformException::class)
    fun scrollToPosition_doesNotExist() {
        // Fetch all posts
        for (i in 1..10) {
            onView(withId(R.id.postsListView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>((i * 10), scrollTo()))
            onView(isRoot()).perform(TestUtil.waitFor(4000))
        }

        // Scroll to item's position which not exist
        val invalidPosition = 101
        onView(withId(R.id.postsListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(invalidPosition, scrollTo()))
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

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.coyo.codechallenge", appContext.packageName)
    }

}