/**
 * Created by dautry1 on 4/11/2018.
 */

package edu.umsl.autryhangman

import java.util.*
import kotlin.collections.ArrayList

const val NUM_OF_PHRASES = 10
val mPhraseList = arrayListOf<String>(  // phrase length should not exceed 25 characters
            "JUMPING FOR JOY",
            "AN ARM AND A LEG",
            "BACK TO THE DRAWING BOARD",
            "BEATING AROUND THE BUSH",
            "LONG IN THE TOOTH",
            "WILD GOOSE CHASE",
            "FISH OUT OF WATER",
            "HARD PILL TO SWALLOW",
            "THROW IN THE TOWEL",
            "BARKING UP THE WRONG TREE")

class MysteryPhrase {

    var mCharsToBeFound: Int = 0            // the characters the user must guess before hangman dies
    var mLettersPicked = ArrayList<Char>()  // letters that have been already be chosen
    var mMysteryPhraseHidden: String = ""   // shows underscores in place of letter not correctly guessed
    private var mTotalNonSpaceChars = 0     // the # of characters the player must correctly guess
    private var mPartiallyDiscoveredMysteryPhrase: String = ""  // this is used to keep track of which correct letters have been chosen
    private var mMysteryPhrase: String = "" // a randomly chosen phrase from the list above

    init {
        generateNewMysteryPhrase()
    }

    private fun generateNewMysteryPhrase() {
        val random = Random()
        mLettersPicked.clear()
        mMysteryPhrase = mPhraseList[random.nextInt(NUM_OF_PHRASES)]
        mMysteryPhraseHidden = convertToHiddenPhrase(mMysteryPhrase.toLowerCase())
        mMysteryPhrase.forEach {
            if (Character.isLetter(it)) {   // determine how many characters the player must guess correctly
                mTotalNonSpaceChars++
            }
        }
        mCharsToBeFound = mTotalNonSpaceChars
        mPartiallyDiscoveredMysteryPhrase = mMysteryPhrase.toLowerCase()    // lower case characters represent correct letters that have not been guessed by the player
    }                                                                       // they are capitalized in this string after correctly guessed

    fun checkForLetterInPuzzle(letter: Char):Boolean {
        var wasCharFound = false
        mLettersPicked.add(letter)
        mLettersPicked.sort()
        mMysteryPhraseHidden = ""

        if (mPartiallyDiscoveredMysteryPhrase.contains(letter.toLowerCase())) {     // a lowercase letter represents an unknown letter
            wasCharFound = true

            // now decrease found character count by total # of same character found
            mPartiallyDiscoveredMysteryPhrase.forEach {
                if (letter.toLowerCase() == it) {
                    mCharsToBeFound--
//                    Log.e("chars to be found", mCharsToBeFound.toString())
                }
            }

            mPartiallyDiscoveredMysteryPhrase = mPartiallyDiscoveredMysteryPhrase.replace(letter.toLowerCase(), letter.toUpperCase()) // Change letter to upper case to indicate it has been found
        }
        mMysteryPhraseHidden = convertToHiddenPhrase(mPartiallyDiscoveredMysteryPhrase)
        return wasCharFound
    }

    private fun convertToHiddenPhrase(thePhrase: String): String {      // hide letters by replaced them with underscores
        var hiddenPhrase = ""
        thePhrase.forEach {
            if (Character.isLetter(it) && Character.isUpperCase(it)) {
                hiddenPhrase += it
            } else if (Character.isLetter(it)) {
                hiddenPhrase += "__ "
            } else if (it == ' ') {
                hiddenPhrase += "  "     // convert one space into two for readability
            } else {
                hiddenPhrase += it     // allow other punctuation such as comma to pass through
            }
        }
        return hiddenPhrase
    }
}

