package com.example.practica10

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UserListActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userListAdapter = UserListAdapter()
        recyclerView.adapter = userListAdapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val url = "http://192.168.1.80/movil/api.php?action=fetch_all"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@UserListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (!response.isSuccessful || responseData.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@UserListActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                val users = mutableListOf<User>()
                val jsonArray = JSONArray(responseData)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    users.add(
                        User(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            email = jsonObject.getString("email"),
                            phone = jsonObject.getString("phone")
                        )
                    )
                }

                runOnUiThread {
                    userListAdapter.updateUsers(users)
                }
            }
        })
    }
}

data class User(val id: Int, val name: String, val email: String, val phone: String)

class UserListAdapter : RecyclerView.Adapter<UserViewHolder>() {
    private val users = mutableListOf<User>()

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): UserViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}

class UserViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) {
        val text1 = itemView.findViewById<android.widget.TextView>(android.R.id.text1)
        val text2 = itemView.findViewById<android.widget.TextView>(android.R.id.text2)
        text1.text = user.name
        text2.text = "${user.email} - ${user.phone}"
    }
}
