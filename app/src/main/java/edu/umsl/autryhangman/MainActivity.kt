package edu.umsl.autryhangman

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.support.v4.app.NotificationManagerCompat

//const val letterButtonWidth  = 150            // for the keyboard keys
//const val letterButtonHeight = 150
const val NOTIFICATION_DURATION = 10000          // in milliseconds

class MainActivity : AppCompatActivity() {

    private var doNotNotify = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openingScreenImage.setImageResource(R.drawable.noose)

        startButton.setOnClickListener {
            val playIntent = Intent(this, PlayGameActivity::class.java)
            doNotNotify = true              // because onStop() is called when switching to the play game activity,
            startActivity(playIntent)       // we need to make sure the pester notification is not issued
        }                                   // as the user has not actually exited the game
    }
        override fun onStop() {     // if the user has exited the game, schedule a pester notification
        val mNotificationTime = Calendar.getInstance().timeInMillis + NOTIFICATION_DURATION
            if (!doNotNotify) {
                NotificationUtils.setNotification(mNotificationTime, this@MainActivity)
            }
            super.onStop()
        }

    override fun onResume() {
        doNotNotify = false     // a notification should always be allowed right after the app is resumed

        // the purpose of the next line is to ensure a pending pester notifications are canceled, as the user
        // may re-enter the game before the pending pester notification was displayed
        NotificationUtils.cancelNotification(NotificationUtils.mNotificationTime, this@MainActivity)
        NotificationManagerCompat.from(this).cancel(NotificationService.NOTIFICATION_ID)
        super.onResume()
    }
}




