package ryuunta.iot.ryuuntaesp.home

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import ryuunta.iot.ryuuntaesp.adapter.RoomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentHomeBinding
import ryuunta.iot.ryuuntaesp.utils.RLog

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {

    private var posRoomSelected: Int = 0

    private val customRoomSpinnerAdapter: RoomSpinnerAdapter by lazy {
        RoomSpinnerAdapter(requireContext(), viewModel.roomList)
    }

    override fun initViews(view: View) {

    }

    override fun initEvents() {
        binding.apply {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                swiperRefresh.isEnabled = nestedScrollView.scrollY == 0 && verticalOffset == 0
            }

            swiperRefresh.setOnRefreshListener {
                swiperRefresh.setRefreshing(true)
                showLoading()
                Handler(Looper.getMainLooper()).postDelayed({
                    swiperRefresh.setRefreshing(false)
                    hideLoading()
                }, 3000)

            }

            spinRoom.adapter = customRoomSpinnerAdapter
            spinRoom.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == customRoomSpinnerAdapter.count - 1) {
                        Toast.makeText(requireContext(), "thêm phòng mới", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        posRoomSelected = position
                        customRoomSpinnerAdapter.currRoomSelectedPosition = posRoomSelected
                        customRoomSpinnerAdapter.notifyDataSetChanged()
                    }
                    spinRoom.setSelection(posRoomSelected)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }

            }
        }
    }
}