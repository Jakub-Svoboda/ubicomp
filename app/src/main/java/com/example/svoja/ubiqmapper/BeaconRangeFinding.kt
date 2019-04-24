package com.example.svoja.ubiqmapper

import android.content.Context
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.Region
import org.json.JSONObject
import java.util.*

val UUID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e"

fun startBeaconRangeFinderService(beaconManager: BeaconManager, beaconList: List<JSONObject>, context: Context) {
    beaconManager.addRangeNotifier { beacons: Collection<Beacon>, range: Region ->
        var closest: Beacon? = null
        beacons.forEach {
            if (it.distance < (closest?.distance ?: Double.MAX_VALUE)) {
                closest = it;
            }
        }

        if (closest != null) {
            val json = beaconList.find { it.getString("major").equals(closest!!.id2) && it.getString("minor").equals(closest!!.id3) }


            Toast.makeText(context, beaconList.toString(), Toast.LENGTH_LONG)
            //TODO let app know to switch from gps
            Log.d("beacontest", json?.getString("room") ?: "Not found - "+ closest!!.toString())
        }
    }
    try {
        beaconManager.startRangingBeaconsInRegion( Region ("myRangingUniqueId", Identifier.parse(UUID), null, null));
    } catch (e: RemoteException) {
        Log.e("beacontest", "Error was thrown: " + e.message);
    }
}