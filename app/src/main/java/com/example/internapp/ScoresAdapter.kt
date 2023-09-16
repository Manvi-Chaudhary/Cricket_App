package com.example.internapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class ScoresAdapter : Adapter<ScoresViewHolder>() {

    val arr : ArrayList<Scores> = arrayListOf()
    var teams : HashMap<Int,Team> = HashMap<Int,Team>()
    var score : HashMap<Int,ArrayList<ScoreCard>> = HashMap<Int,ArrayList<ScoreCard>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoresViewHolder {
        val view :View= LayoutInflater.from(parent.context).inflate(R.layout.rec_item,parent,false)
        return ScoresViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ScoresViewHolder, position: Int) {
        try{
            Glide.with(holder.itemView).load(teams.get(arr[position].team1)!!.imageurl).error(R.drawable.ic_launcher_background).into(holder.team1image)
            Glide.with(holder.itemView).load(teams.get(arr[position].team2)!!.imageurl).error(R.drawable.ic_launcher_background).into(holder.team2image)
            holder.team1.text=teams.get(arr[position].team1)!!.name
            holder.team2.text=teams.get(arr[position].team2)!!.name
            holder.matchName.text=teams.get(arr[position].team1)!!.name+" VS "+teams.get(arr[position].team2)!!.name
        }
        catch(e : Exception){

        }

        holder.date.text=arr[position].date.substring(0,10)

        try{

            holder.matchType.text=arr[position].type!!.uppercase()


        }catch (e : Exception){

        }

        try{
            holder.team1score.text=score.get(arr[position].fixId)!![0].score.toString()+"-"+score.get(arr[position].fixId)!![0].wickets.toString()+"("+score.get(arr[position].fixId)!![0].overs.toString()+")"
            holder.team2score.text=score.get(arr[position].fixId)!![1].score.toString()+"-"+score.get(arr[position].fixId)!![1].wickets.toString()+"("+score.get(arr[position].fixId)!![1].overs.toString()+")"
        }catch (e : Exception){

        }

        holder.status.text=arr[position].status

    }


    fun updateDataScores(newArr : ArrayList<Scores>){
        arr.clear()
        arr.addAll(newArr)
        notifyDataSetChanged()
    }
    fun updateDataTeams(newteam : Team,id : Int){
        teams.put(id,newteam)
        notifyDataSetChanged()
    }
    fun updateDataScoreCard(newScore : ArrayList<ScoreCard>,id : Int){
        score.put(id,newScore)
        notifyDataSetChanged()
    }





}

class ScoresViewHolder(itemView: View) : ViewHolder(itemView) {
    val date : TextView = itemView.findViewById(R.id.matchDate)
    val matchName : TextView = itemView.findViewById(R.id.matchName)
    val matchType : TextView = itemView.findViewById(R.id.matchType)
    val team1 : TextView = itemView.findViewById(R.id.team1)
    val team2 : TextView = itemView.findViewById(R.id.team2)
    val team1image : ImageView =itemView.findViewById(R.id.t1image)
    val team2image : ImageView =itemView.findViewById(R.id.t2image)
    val team1score : TextView = itemView.findViewById(R.id.team1scores)
    val team2score : TextView = itemView.findViewById(R.id.team2scores)
    val status : TextView = itemView.findViewById(R.id.status)

}