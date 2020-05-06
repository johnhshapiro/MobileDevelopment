package com.johnhshapiro.rockpaperscissors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_first_time_user.*
import kotlinx.android.synthetic.main.fragment_game.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class FirstTimeUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_time_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filename = "rps_extreme_data"
        val file = File(activity?.filesDir, filename)
        val textInBox = username_box as EditText

        first_play_button.setOnClickListener {
            val username = textInBox.text.toString()
            if (username.isNotEmpty()) {
                file.writeText(username)
                val newUser = hashMapOf(
                    "username" to username,
                    "score" to "0"
                )
                Firebase
                    .firestore
                    .collection("highScores")
                    .document(username).set(newUser)
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment, GameFragment())
                    .commit()
            }

        }
    }

}
