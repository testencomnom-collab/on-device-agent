package com.example.services

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalInferenceEngine(private val context: Context) {
    
    private var llmInference: LlmInference? = null
    
    suspend fun initialize(modelName: String = "gemma_2b_it_cpu_int4.bin"): Boolean = withContext(Dispatchers.IO) {
        try {
            val modelFile = File(context.filesDir, modelName)
            if (!modelFile.exists()) {
                Log.e("LocalInferenceEngine", "Model file not found: ${modelFile.absolutePath}")
                return@withContext false
            }

            // SAFETY CHECK: Prevent native crash (OOM) when loading 2GB Gemma on limited emulator VMs
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            
            // If less than 1.5 GB of free RAM, loading Gemma 2B will cause a hard native crash.
            val freeMemoryMB = memoryInfo.availMem / (1024 * 1024)
            Log.d("LocalInferenceEngine", "Free memory available: $freeMemoryMB MB")
            
            if (freeMemoryMB < 1000) {
                Log.e("LocalInferenceEngine", "Insufficient RAM to load the LLM model safely. Available: $freeMemoryMB MB. Aborting to prevent native crash.")
                return@withContext false
            }
            
            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath(modelFile.absolutePath)
                .setMaxTokens(1024)
                .setTemperature(0.7f)
                .build()
                
            llmInference = LlmInference.createFromOptions(context, options)
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
    
    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val inference = llmInference
                ?: return@withContext "Fehler: Die lokale MediaPipe Engine ist nicht bereit. (Möglicherweise reicht der Arbeitsspeicher (RAM) dieses Geräts nicht aus, um ein 2B LLM zu laden, oder die Datei fehlt.)"
            
            inference.generateResponse(prompt)
        } catch (e: Throwable) {
            e.printStackTrace()
            "Fehler bei der echten lokalen On-Device Generierung: ${e.message}"
        }
    }
    
    fun close() {
        llmInference?.close()
        llmInference = null
    }
}
