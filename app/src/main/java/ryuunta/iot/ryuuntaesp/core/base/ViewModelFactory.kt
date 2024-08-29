package ryuunta.iot.ryuuntaesp.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.authentication.AuthViewModel
import ryuunta.iot.ryuuntaesp.main.devices.AddDeviceViewModel
import ryuunta.iot.ryuuntaesp.main.home.content.DevicesViewModel
import ryuunta.iot.ryuuntaesp.main.manage.ManageViewModel
import ryuunta.iot.ryuuntaesp.main.notification.NotiViewModel

open class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel() as T
            }

            modelClass.isAssignableFrom(ManageViewModel::class.java) -> {
                return ManageViewModel() as T
            }

            modelClass.isAssignableFrom(NotiViewModel::class.java) -> {
                return NotiViewModel() as T
            }

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel() as T
            }

            modelClass.isAssignableFrom(AddDeviceViewModel::class.java) -> {
                return AddDeviceViewModel() as T
            }

            modelClass.isAssignableFrom(DevicesViewModel::class.java) -> {
                return DevicesViewModel() as T
            }

            else -> {
                return super.create(modelClass)
            }
        }

    }
}