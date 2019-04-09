package retrofitdemo.com.restaurantsnearme.view


import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4

import android.R.attr.action
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofitdemo.com.restaurantsnearme.R
import retrofitdemo.com.restaurantsnearme.adapters.Resturant_adapter


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    private val ITEM_BELOW_THE_FOLD = 40

    @Rule
    var mMainActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, true)


    @Test
    fun scrollToItemBelowFold_checkItsText() {
        // Match the text in an item below the fold and check that it's displayed.
        val itemElementText = mMainActivityTestRule.activity.getString(R.string.loading) + (ITEM_BELOW_THE_FOLD).toString();
        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }


}