package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permissions.PermissionsManager
import com.example.ui.AgentViewModel
import com.example.ui.screens.MainAgentView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsManager = PermissionsManager(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val agentViewModel: AgentViewModel = viewModel()
                MainAgentView(viewModel = agentViewModel, permissionsManager = permissionsManager)
            }
        }
    }
}
