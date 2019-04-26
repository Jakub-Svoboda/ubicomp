package com.example.svoja.ubiqmapper

import android.content.Context
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.Region
import org.json.JSONObject
import java.util.*
import kotlin.math.abs

val UUID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e"

fun startBeaconRangeFinderService(beaconManager: BeaconManager, context: MapsActivity) {
    beaconManager.addRangeNotifier { beacons: Collection<Beacon>, range: Region ->
        var closest: Beacon? = null
        beacons.forEach {
            if (it.distance < (closest?.distance ?: Double.MAX_VALUE)) {
                closest = it;
            }
        }

        if (closest != null) {
            val beaconInfo = Shame.beacons.find { it.major == closest!!.id2.toString()  && it.minor == closest!!.id3.toString() }

            if (beaconInfo != null)
            {
                val newInfo = LocInfo(LatLng(beaconInfo.lat, beaconInfo.lon), closest!!.distance, beaconInfo.alias, beaconInfo.room)
                if (context.beaconInfo == null || context.beaconInfo.alias != newInfo.alias || abs(context.beaconInfo.accuracy - newInfo.accuracy) > 1.0)
                {
                    context.beaconInfo = newInfo;
                    context.updatePosition()
                }
            }
            else if (context.beaconInfo != null){
                context.beaconInfo = null;
                context.updatePosition()
            }

            Log.d("beacontest", beaconInfo?.alias ?: "Not found - "+ closest!!.toString())
        }
        else if (context.beaconInfo != null){
            context.beaconInfo = null;
            context.updatePosition()
        }
    }
    try {
        beaconManager.startRangingBeaconsInRegion( Region ("myRangingUniqueId", Identifier.parse(UUID), null, null));
    } catch (e: RemoteException) {
        Log.e("beacontest", "Error was thrown: " + e.message);
    }
}