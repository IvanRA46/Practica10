package com.example.practica10

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Form : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val idEditText = findViewById<EditText>(R.id.idEditText)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)

        val mostrarButton = findViewById<Button>(R.id.button)
        mostrarButton.setOnClickListener{
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            registerUser(name, email, phone)
        }

        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val id = idEditText.text.toString()
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            editUser(id, name, email, phone)
        }

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val id = idEditText.text.toString()
            searchUser(id)
        }

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            val id = idEditText.text.toString()
            deleteUser(id)
        }
    }

    private fun registerUser(name: String, email: String, phone: String) {
        val url = "http://192.168.1.80/movil/api.php"
        val formBody = FormBody.Builder()
            .add("action", "register")
            .add("name", name)
            .add("email", email)
            .add("phone", phone)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Form, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData!!)
                val success = json.getBoolean("success")
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this@Form, "User Registered", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Form, "Failed to Register", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun editUser(id: String, name: String, email: String, phone: String) {
        val url = "http://192.168.1.80/movil/api.php"
        val formBody = FormBody.Builder()
            .add("action", "edit")
            .add("id", id)
            .add("name", name)
            .add("email", email)
            .add("phone", phone)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Form, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData!!)
                val success = json.getBoolean("success")
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this@Form, "User Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Form, "Failed to Update User", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun searchUser(id: String) {
        val url = "http://192.168.1.80/movil/api.php"
        val formBody = FormBody.Builder()
            .add("action", "search")
            .add("id", id)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Form, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData!!)
                runOnUiThread {
                    if (json.has("id")) {
                        findViewById<EditText>(R.id.nameEditText).setText(json.getString("name"))
                        findViewById<EditText>(R.id.emailEditText).setText(json.getString("email"))
                        findViewById<EditText>(R.id.phoneEditText).setText(json.getString("phone"))
                        Toast.makeText(this@Form, "User Found", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Form, "User Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun deleteUser(id: String) {
        val url = "http://192.168.1.80/movil/api.php"
        val formBody = FormBody.Builder()
            .add("action", "delete")
            .add("id", id)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Form, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData!!)
                val success = json.getBoolean("success")
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this@Form, "User Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Form, "Failed to Delete User", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


