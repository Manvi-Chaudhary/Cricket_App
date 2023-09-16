package com.example.internapp

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recView : RecyclerView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        val adapter: ScoresAdapter = ScoresAdapter()
        recView.adapter = adapter

        val progBar : ProgressBar = findViewById(R.id.progress_bar)

        getResult(adapter,progBar)












    }
    fun getScoreCard(fid : Int,adapter: ScoresAdapter)  {

        val queue = Volley.newRequestQueue(this)

        val url1 = "https://cricket.sportmonks.com/api/v2.0/fixtures/${fid}?api_token=vrA3tzyYqYNAzuAZh3pHOaPk366dOOvs9r6HRqWB46bvw2FkktOLk7DjCLUq&include=runs"
        val JsonRequest = JsonObjectRequest(
            Request.Method.GET, url1,null,
            { response ->
                var data =response.getJSONObject("data")
                var runs =data.getJSONArray("runs")
                if(runs.length()>1){
                    var s1=runs.getJSONObject(0).getInt("score")
                    var t1=runs.getJSONObject(0).getInt("team_id")
                    var w1=runs.getJSONObject(0).getInt("wickets")
                    var o1=runs.getJSONObject(0).getDouble("overs")
                    var s2=runs.getJSONObject(1).getInt("score")
                    var t2=runs.getJSONObject(1).getInt("team_id")
                    var w2=runs.getJSONObject(1).getInt("wickets")
                    var o2=runs.getJSONObject(1).getDouble("overs")
                    var Score1 : ScoreCard = ScoreCard(t1,s1,w1,o1.toFloat())
                    var Score2 : ScoreCard = ScoreCard(t2,s2,w2,o2.toFloat())
                    var arr : ArrayList<ScoreCard> = arrayListOf(Score1,Score2)
                    adapter.updateDataScoreCard(arr,fid)

                }


            },
            { Log.d("errorScore","can't get")})

        queue.add(JsonRequest)

    }
    fun getteam(id : Int,adapter: ScoresAdapter)  {

        val queue = Volley.newRequestQueue(this)

        val url = "https://cricket.sportmonks.com/api/v2.0/teams/${id}?api_token=OkBPOyoi2oQIO1aP06qAkNM9jnUi5NkcYFGzv1L9nwM5h3Wg82Yw0mnP20GD"
        val JsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                var data =response.getJSONObject("data")

                val team : Team=Team(name = data.getString("name"), imageurl = data.getString("image_path"))
                Log.d("team",team.toString())
                adapter.updateDataTeams(team,id)
            },
            { Log.d("error","can't get result")})

        queue.add(JsonRequest)

    }




    fun getResult(adapter: ScoresAdapter,progressBar: ProgressBar ) {
        val arr : ArrayList<Scores> = arrayListOf()

        val queue = Volley.newRequestQueue(this)
        val url = "https://cricket.sportmonks.com/api/v2.0/fixtures?api_token=vp9rNJOG4WpAgc0gg3MmOwtdAPQbftabFbliG3eXkdp8E1HbZEfEPO0vj1Uj"

        progressBar.visibility= View.VISIBLE
        val JsonRequest = JsonObjectRequest(
                Request.Method.GET, url,null,
            { response ->
                var data : JSONArray ? =null
                try {
                    data = response.getJSONArray("data")
                    Log.d("data",data.length().toString())
                }catch (e : Exception){

                }
                var len= data!!.length()
                var i : Int=0
                val arr: ArrayList<Scores> = arrayListOf()
                while (i<10) {

                    val res = data.getJSONObject(i)
                    var name=" "
                    var date = res.getString("starting_at")
                    var matchType : String? =null
                    var status = " "
                    try {
                        name=res.getString("name")
                    }catch (e : Exception){

                    }
                    try {
                        date = res.getString("date")
                    }catch (e : Exception){

                    }

                    try {
                        status = res.getString("note")
                    }catch (e : Exception){

                    }

                    var teamInfo : JSONArray?=null
                    try {
                        teamInfo=res.getJSONArray("teamInfo")
                    }catch (e : Exception){

                    }
                    if(adapter.teams.containsKey(res.getInt("localteam_id"))){

                    }else{
                        getteam(res.getInt("localteam_id"),adapter)
                    }
                    if(adapter.teams.containsKey(res.getInt("visitorteam_id"))){

                    }else{
                        getteam(res.getInt("visitorteam_id"),adapter)
                    }
                    getScoreCard(res.getInt("id"),adapter)



                        val newScores: Scores =
                            Scores(res.getInt("id"),date, name, res.getString("type"), res.getInt("localteam_id"), res.getInt("visitorteam_id"), status)
                        arr.add(newScores)




                    i++

                }

                progressBar.visibility= View.GONE

                adapter.updateDataScores(arr)







            },
            { Log.d("error","can't get result")
                progressBar.visibility= View.GONE
                Toast.makeText(this,"Unable to get Results",Toast.LENGTH_SHORT).show()})

        queue.add(JsonRequest)




    }







}

