package ryuunta.iot.ryuuntaesp.main.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ryuunta.iot.ryuuntaesp.core.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.HouseObj
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService
import ryuunta.iot.ryuuntaesp.helper.GroupHelper

class ManageViewModel() : BaseViewModel() {

    private val _listHouse: MutableLiveData<List<HouseObj>> = MutableLiveData(listOf())
    val listHouse: LiveData<List<HouseObj>> = _listHouse

    private val groupHelper = GroupHelper()

    fun fetchHouseData() {
        loading.postValue(true)
        groupHelper.getHouseList {
            _listHouse.postValue(it)
            loading.postValue(false)
        }
    }

}
