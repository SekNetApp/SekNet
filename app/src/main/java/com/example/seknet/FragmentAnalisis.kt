package com.example.seknet

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.seknet.databinding.FragmentAnalisisBinding
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.net.InetAddress

class FragmentAnalisis : Fragment(R.layout.fragment_analisis) {

    private lateinit var binding: FragmentAnalisisBinding
    private lateinit var listView: ListView
    private lateinit var resultListAdapter: ArrayAdapter<String>

    private val resultsList: ArrayList<String> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalisisBinding.bind(view)
        activity?.title = "ANALISIS"

        listView = binding.lvAnalyzeResults
        resultListAdapter = ArrayAdapter(requireContext(), R.layout.listview_item, resultsList)
        listView.adapter = resultListAdapter

        binding.btnAnalyze.setOnClickListener {
            performNetworkAnalysis()
        }

        // Verificar si el permiso ACCESS_NETWORK_STATE está otorgado
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            // Solicitar el permiso ACCESS_NETWORK_STATE
            requestAccessNetworkStatePermission()
        } else {
            // El permiso ya está otorgado, puedes utilizar getWifiMacAddress()
            val macAddress = getWifiMacAddress()
            Log.d("MAC Address", macAddress)
        }
    }

    private fun requestAccessNetworkStatePermission() {
        val permission = Manifest.permission.ACCESS_NETWORK_STATE
        val shouldShowRationale = shouldShowRequestPermissionRationale(permission)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso ACCESS_NETWORK_STATE otorgado, puedes utilizar getWifiMacAddress()
                val macAddress = getWifiMacAddress()
                Log.d("MAC Address", macAddress)
            } else {
                // Permiso ACCESS_NETWORK_STATE denegado, regresar a la pantalla anterior
                requireActivity().onBackPressed()
            }
        }

        if (shouldShowRationale) {
            // Mostrar un diálogo o mensaje explicativo al usuario
            AlertDialog.Builder(requireContext())
                .setTitle("Permiso requerido")
                .setMessage("Este permiso es necesario para obtener la dirección MAC.")
                .setPositiveButton("Solicitar permiso") { _, _ ->
                    // Solicitar el permiso ACCESS_NETWORK_STATE
                    requestPermissionLauncher.launch(permission)
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    // El usuario ha cancelado, regresar a la pantalla anterior
                    requireActivity().onBackPressed()
                }
                .show()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun performNetworkAnalysis() {
        val networkPrefix = "192.168.1." // Prefijo de la dirección IP de tu red

        resultsList.clear() // Limpiar los resultados anteriores

        lifecycleScope.launch {
            val devices = mutableListOf<Deferred<String>>()

            // Escanear las direcciones IP en paralelo
            for (i in 1..255) {
                val host = networkPrefix + i.toString()
                devices.add(async(Dispatchers.IO) { scanDevice(host) })
            }

            // Esperar a que se completen todas las tareas
            val results = devices.awaitAll()

            // Filtrar las direcciones IP activas y agregar los resultados a la lista
            val activeDevices = results.filter { it.isNotEmpty() }
            resultsList.addAll(activeDevices)

            // Notificar al adaptador que los datos han cambiado
            resultListAdapter.notifyDataSetChanged()
        }
    }

    private fun scanDevice(host: String): String {
        return try {
            val process = ProcessBuilder("/system/bin/ping", "-c", "1", "-W", "1", host).start()
            val exitCode = process.waitFor()
            val success = (exitCode == 0)

            if (success) {
                //val address = InetAddress.getByName(host)
                val macAddress = getWifiMacAddress()

                "$host\nMAC: $macAddress"
            } else {
                "" // Devuelve una cadena vacía si el dispositivo está fuera de línea
            }
        } catch (e: Exception) {
            "" // Devuelve una cadena vacía si se produce una excepción
        }
    }

    @SuppressLint("HardwareIds")
    private fun getWifiMacAddress(): String {
        val wifiManager =
            requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val macAddress = wifiInfo.macAddress

        return macAddress ?: "No disponible"
    }
}
