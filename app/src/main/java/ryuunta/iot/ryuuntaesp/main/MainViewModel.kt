package ryuunta.iot.ryuuntaesp.main

import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.base.BaseViewModel
import ryuunta.iot.ryuuntaesp.data.model.IconWithTextObj
import ryuunta.iot.ryuuntaesp.data.network.RetrofitService

class MainViewModel(): BaseViewModel() {

    val listHomeUser = listOf(
        IconWithTextObj(0, R.drawable.ic_no_face, "Ryuunta"),
        IconWithTextObj(1, R.drawable.ic_no_face, "Ryuunta 2"),
        IconWithTextObj(2, R.drawable.ic_no_face, "Ryuunta 3")
    )
}