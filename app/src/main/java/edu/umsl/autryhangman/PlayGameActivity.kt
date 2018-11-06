package edu.umsl.autryhangman

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_play_game.*
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.MenuItem

const val letterButtonWidth  = 150      // the keyboard button width
const val letterButtonHeight = 150      // the keyboard button height
const val hangmanDies = 11      // this is how many body parts hangman has

class PlayGameActivity : AppCompatActivity() {

    private lateinit var mystery:MysteryPhrase
    private var wrongLetterCount = 0                                    // hang the man if wrongLetterCount reaches # of letters in phrase
    private val mLetterButtonList: ArrayList<Button> = ArrayList()      // for onscreen keyboard letters A - Z

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        for (i in 0..25) {          // create the onscreen alphabet keyboard
            mLetterButtonList.add(Button(this))
            mLetterButtonList[i].layoutParams = LinearLayout.LayoutParams(letterButtonWidth, letterButtonHeight)
            mLetterButtonList[i].text = (i+65).toChar().toString()

            mLetterButtonList[i].setOnClickListener() {
                if (it is TextView) {
                    if (!mystery.checkForLetterInPuzzle(it.text.toString().toCharArray()[0])) {
                        val hangmanDrawableId = resources.getIdentifier("hang" + (++wrongLetterCount), "drawable", "edu.umsl.autryhangman")
                        play_game_hangman.setImageResource(hangmanDrawableId)                       // add another body part to hangman
                        if (wrongLetterCount == hangmanDies){           // player exceeded maximum letter guesses
                            for (j in 0..25){
                                mLetterButtonList[j].isEnabled = false
                                mysteryPhraseHeader.text = getString(R.string.youLose)
                            }
                            askToPlayAgain(false)
                        }
                    }
                    mysteryPhraseTextView.text = mystery.mMysteryPhraseHidden
                    lettersPickedTextView.text = mystery.mLettersPicked.toString().replace("[","").replace("]","").replace(",","")  // get rid of unwanted characters of the .toString() method
                    if (mystery.mCharsToBeFound == 0) {
                        for (k in 0..25){
                            mLetterButtonList[k].isEnabled = false
                        }
                        mysteryPhraseHeader.text = getString(R.string.you_win)
                        play_game_hangman.setImageResource(R.drawable.happyhanget)
                        askToPlayAgain(true)
                    }
                    mLetterButtonList[i].isEnabled = false          // deactivate the onscreen keyboard until a new game is started
                }
            }
            buttonGrid.addView(mLetterButtonList[i])
        }
        startNewGame()
    }

    private fun startNewGame(){         // reset everything and get a new mystery phrase
        mysteryPhraseHeader.text = getString(R.string.mystery_phrase)
        play_game_hangman.setImageResource(R.drawable.noose)
        mystery = MysteryPhrase()
        mysteryPhraseTextView.text = mystery.mMysteryPhraseHidden
        lettersPickedTextView.text = ""
        wrongLetterCount = 0
        for (i in 0..25){               // activate the alphabet keyboard
            mLetterButtonList[i].isEnabled = true
        }
    }

    private fun askToPlayAgain(wonGame:Boolean) {

        val alertDialogBuilder = AlertDialog.Builder(
                this)

        if (wonGame) {
            alertDialogBuilder.setTitle("You won the game!")
        } else {
            alertDialogBuilder.setTitle("You lost the game and hangman broke his neck!")
        }
        alertDialogBuilder
                .setMessage("Would you like to play again?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    startNewGame()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    finish()
    })
        val alertDialog = alertDialogBuilder.create()   // create the "play again?" alert box and show it
        alertDialog.show()
    }

    override fun onBackPressed() {

        val alertDialogBuilder = AlertDialog.Builder(
                this)

        if (mystery.mCharsToBeFound == 0) {
            super.onBackPressed()
        }
        else {
            alertDialogBuilder.setTitle("Are you sure you want to quit?")       // chicken test the player
            alertDialogBuilder
                    .setMessage("Click yes to leave the game.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        // if this button is clicked, close
                        // current activity
                        super.onBackPressed()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel()
                    })
            val alertDialog = alertDialogBuilder.create()   // create the alert box and show it
            alertDialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {       // this one handles the back arrow in the upper left hand-side of the screen
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}

