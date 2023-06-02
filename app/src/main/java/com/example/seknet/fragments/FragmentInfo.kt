package com.example.seknet.fragments

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.seknet.R
import com.example.seknet.databinding.FragmentInfoBinding


class FragmentInfo : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    private var result : String = ""
    private var resultWifi : String = ""
    private var data : String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)
        activity?.title = "INFO";
        result = getSystemDetail()
        resultWifi = getSystemWifi()
        loadSystemInfo()
    }
    private fun getSystemDetail(): String {
        return  "Brand: ${Build.BRAND} \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }

    private fun getSystemWifi(): String {
        val mWifiManager: WifiManager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val connInfo = mWifiManager.connectionInfo

        val ipAddress = connInfo.ipAddress
        val ipAddressValue = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
        val NumOfRSSILevels = 5
        val wifiInfo = HashMap<String, String>()
        wifiInfo["SSID"] = connInfo.ssid

        wifiInfo["IP Address"] = ipAddressValue + ""
        wifiInfo["MAC Address"] = connInfo.macAddress
        wifiInfo["LinkSpeed"] = connInfo.linkSpeed.toString() + WifiInfo.LINK_SPEED_UNITS


        val it: MutableIterator<*> = wifiInfo.entries.iterator()

        while (it.hasNext()) {
            val (key, value) = it.next() as Map.Entry<*, *>
            data = (data + key).toString() + " : " + value + "\n"
            it.remove()
        }
        return data
    }

    private fun loadSystemInfo() {
        binding.infoDeviceNameValue.text = result
        binding.infoDeviceWifiNameValue.text = resultWifi
    }

}