package ryuunta.iot.ryuuntaesp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ryuunta.iot.ryuuntaesp.InitiationViewModel
import ryuunta.iot.ryuuntaesp.SplashViewModel
import ryuunta.iot.ryuuntaesp.authentication.AuthViewModel
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService
import ryuunta.iot.ryuuntaesp.devices.AddDeviceViewModel
import ryuunta.iot.ryuuntaesp.home.HomeViewModel
import ryuunta.iot.ryuuntaesp.notification.NotiViewModel
import ryuunta.iot.ryuuntaesp.manage.ManageViewModel
import ryuunta.iot.ryuuntaesp.main.MainViewModel

open class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel() as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel() as T
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
            modelClass.isAssignableFrom(InitiationViewModel::class.java) -> {
                return InitiationViewModel() as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                return SplashViewModel() as T
            }
            modelClass.isAssignableFrom(AddDeviceViewModel::class.java) -> {
                return AddDeviceViewModel() as T
            }
            else -> {
                return super.create(modelClass)
            }
        }

    }
}