package ryuunta.iot.ryuuntaesp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionUtils {
    val listPermission: List<String>
        get() = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S)
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        else
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            )

    val permissionsForScanWifi: List<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        else
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            )

    fun checkPermission(
        context: Activity,
        permission: String
    ) {
//        Dexter.withContext(context)
//            .withPermissions(

//            )
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    onResult(report)
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            })
//            .check()

        when {
            ContextCompat.checkSelfPermission(
                context,
                permission

            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }

            shouldShowRequestPermissionRationale(context, permission) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.

            }

            else -> {

            }
        }
    }
//
//    fun checkPermission2(context: Context, permission: String) {
//        Dexter.withContext(context)
//            .withPermissions(
//                permission,
//            )
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    val lm: LocationManager =
//                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    var gpsEnabled = false
//                    var network_enabled = false
//
//                    try {
//                        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                    } catch (ex: Exception) {
//                    }
//
//                    if (!gpsEnabled) {
//                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                    }
//
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            })
//            .check()
//    }
//
//    var listPermission: MutableList<Permission> = mutableListOf()
//
//    @JvmName("getlistPermission1")
//    fun getlistPermission(context: Context): MutableList<Permission> {
//        if (listPermission.isEmpty()) {
//            listPermission.add(
//                Permission(
//                    context.getString(R.string.grant_storage_permission),
//                    context.getString(R.string.grant_storage_permission),
//                    false
//                )
//            )
////        listPermission.add(
////            Permission(
////                context.getString(R.string.modify_settings),
////                context.getString(R.string.description),
////                false
////            )
////        )
//        }
//        return listPermission
//    }

    fun checkPermission(
        context: Context,
        permission: String,
        onSuccess: () -> Unit = {},
        onCancel: () -> Unit = {}
    ) {
        Dexter.withContext(context)
            .withPermissions(
                permission
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            onSuccess()
                        } else {
                            onCancel()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    fun checkPermissions(
        context: Context,
        permissions: List<String>,
        onSuccess: (Boolean) -> Unit = {}
    ) {
        Dexter.withContext(context)
            .withPermissions(
                permissions
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        onSuccess(report.areAllPermissionsGranted())
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    fun checkPermissionsNew(
        context: Context,
        permissions: List<String>,
        onSuccess: () -> Unit = {},
        onCancel: (List<PermissionDeniedResponse>) -> Unit
    ) {
        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            onSuccess()
                        } else if (report.deniedPermissionResponses.isNotEmpty()) {
                            onCancel(report.deniedPermissionResponses)
                        } else {
                            onCancel(listOf())
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

    private val permissionNames = hashMapOf(
        "ACCESS_FINE_LOCATION" to "Truy cập vị trí"
    )

    fun getNamePermission(keys: List<String>): MutableList<String> {
        val listName = mutableListOf<String>()
        keys.forEach {
            listName.add(permissionNames[it].toString())
        }
        return listName
    }

//    fun checkAllPermission(context: Context): Boolean {
////        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        var check: Boolean
//        val lm: LocationManager =
//            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        var gps_enabled = false
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        } catch (ex: Exception) {
//        }
//
//        check = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED && gps_enabled
//
//        check = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.BLUETOOTH
//        ) == PackageManager.PERMISSION_GRANTED
//
//        check = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.BLUETOOTH_ADMIN
//        ) == PackageManager.PERMISSION_GRANTED
//
//        check = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//        return check
//    }
}

fun getStoragePermission() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.WRITE_EXTERNAL_STORAGE


