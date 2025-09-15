package com.firas.simplelauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.firas.simplelauncher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppsAdapter
    private val allApps = mutableListOf<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadApps()

        adapter = AppsAdapter { app ->
            val launchIntent = packageManager.getLaunchIntentForPackage(app.pkg)
            if (launchIntent != null) {
                startActivity(launchIntent)
            }
        }

        binding.recycler.layoutManager = GridLayoutManager(this, 4)
        binding.recycler.adapter = adapter

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterApps(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadApps() {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolved = pm.queryIntentActivities(intent, 0)
        allApps.clear()
        for (ri in resolved) {
            val label = ri.loadLabel(pm).toString()
            val pkg = ri.activityInfo.packageName
            val icon = ri.loadIcon(pm)
            allApps.add(AppInfo(label, pkg, icon))
        }
        allApps.sortBy { it.label.lowercase() }
        adapter.submitList(allApps.toList())
    }

    private fun filterApps(query: String) {
        val q = query.trim().lowercase()
        val filtered = if (q.isEmpty()) allApps else allApps.filter { it.label.lowercase().contains(q) }
        adapter.submitList(filtered)
    }
}
