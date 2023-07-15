package com.github.goldy1992.mp3player.client.ui.utils

import android.content.Context
import android.util.Log
import com.github.goldy1992.mp3player.client.ui.utils.ContextUtils.getActivity
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode

private const val LOG_TAG = "RatingUtils"
object RatingUtils  {
    fun submit(context: Context) {
        val manager = ReviewManagerFactory.create(context)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            Log.d(LOG_TAG, "OnCompleteListener() invoked with task: $task")
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "OnCompleteListener() was successful")
                // We got the ReviewInfo object
                val reviewInfo = task.result
                Log.d(LOG_TAG, "OnCompleteListener() reviewInfo $reviewInfo")
                val appCompatActivity = context.getActivity()
                if (appCompatActivity != null) {
                    Log.d(LOG_TAG, "OnCompleteListener() Retriever AppCompatActivity, launching review flow")
                    val reviewFlow = manager.launchReviewFlow(appCompatActivity, reviewInfo)
                    reviewFlow.addOnCompleteListener {
                        Log.d(LOG_TAG, "Review Flow complete")
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                } else {
                    Log.w(LOG_TAG, "OnCompleteListener() Could not retrieve AppCompatActivity, returned as null")
                }


            } else {
                val exception = (task.exception as ReviewException)
                @ReviewErrorCode val reviewErrorCode = exception.errorCode
                Log.d(LOG_TAG, "requestFlow.OnCompleteListener() returned with error code $reviewErrorCode and exception: ${exception.message}")
                // There was some problem, log or handle the error code.

            }
        }

    }
}