package com.ludovic.vimont

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val source: List<String>
        get() {
            val lowerBound = (1..100).random()
            return (lowerBound..(lowerBound..101).random()).map { "This is item number $it" }
        }
    private val simpleAdapter = SimpleAdapter().apply { dataSource = source }
    private val simulatedNetworkDelay = TimeUnit.SECONDS.toMillis(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recyclerView).adapter = simpleAdapter

        val lottiePullToRefreshLayout = findViewById<LottiePullToRefreshLayout>(R.id.swipe_refresh)
        lottiePullToRefreshLayout.refreshes()
                .subscribe {
                    Handler().postDelayed({
                        simpleAdapter.dataSource = source
                        lottiePullToRefreshLayout.stopRefreshing()
                    }, simulatedNetworkDelay)
                }

        lottiePullToRefreshLayout.onProgressListener {
            Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
        }
    }
}

fun ClosedRange<Int>.random() = Random(System.currentTimeMillis()).nextInt((endInclusive + 1) - start) +  start
