package com.johnhshapiro.rockpaperscissors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_leader_board.*

class LeaderBoard : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leader_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var leaderNames = ""
        var leaderScores = ""
        val db = Firebase.firestore
        db
            .collection("highScores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    leaderNames += "${document.getString("username")}\n"
                    leaderScores += "${document.getString("score")}\n"
                }
                leader_names.text = leaderNames
                leader_scores.text = leaderScores
            }
    }

}
