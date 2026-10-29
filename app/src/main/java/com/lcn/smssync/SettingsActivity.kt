package com.lcn.smssync

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val edtUsername = findViewById<TextInputEditText>(R.id.edtUsername)
        val btnSaveAuth = findViewById<Button>(R.id.btnSaveAuth)
        val switchSync = findViewById<SwitchMaterial>(R.id.switchSync)

        val sharedPreferences = getSharedPreferences("LCN_Storage", Context.MODE_PRIVATE)

        edtUsername.setText(sharedPreferences.getString("auth_user", ""))
        switchSync.isChecked = sharedPreferences.getBoolean("sync_enabled", false)

        btnSaveAuth.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            if (username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên tài khoản!", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.edit().putString("auth_user", username).apply()
                Toast.makeText(this, "Đã cập nhật tài khoản kết nối", Toast.LENGTH_SHORT).show()
            }
        }

        switchSync.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("sync_enabled", isChecked).apply()
            val msg = if (isChecked) "Hệ thống đồng bộ đã bật" else "Đã tắt hệ thống đồng bộ"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
