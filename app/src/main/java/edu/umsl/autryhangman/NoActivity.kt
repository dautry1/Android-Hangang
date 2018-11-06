package edu.umsl.autryhangman

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat

class NoActivity : Activity() {     // this activity is called when the user says no to coming back to the game
                                    // in the pester notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationManagerCompat.from(this).cancel(NotificationService.NOTIFICATION_ID)    // cancel the notification
        finish()
    }
}
