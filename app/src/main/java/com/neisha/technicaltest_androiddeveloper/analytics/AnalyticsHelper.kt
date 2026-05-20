package com.neisha.technicaltest_androiddeveloper.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor() {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun logScreenView(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        })
    }

    fun logUserAdded() {
        firebaseAnalytics.logEvent("user_added", null)
    }

    fun logSearch(query: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        })
    }

    fun logFilterApplied(city: String) {
        firebaseAnalytics.logEvent("filter_applied", Bundle().apply {
            putString("city", city)
        })
    }
}