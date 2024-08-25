package ryuunta.iot.ryuuntaesp.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.adapter.CustomSpinnerAdapter
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentRyuuntaMainBinding
import ryuunta.iot.ryuuntaesp.utils.gone
import ryuunta.iot.ryuuntaesp.utils.show

class RMainFragment : BaseFragment<FragmentRyuuntaMainBinding, MainViewModel>(
    FragmentRyuuntaMainBinding::inflate,
    MainViewModel::class.java
) {

    private var currentHomePos: Int = 0

    private val userHomeAdapter: CustomSpinnerAdapter by lazy {
        CustomSpinnerAdapter(requireContext(), viewModel.listHomeUser)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        binding.apply {
            initViewPager()

            bottomNavigationView.menu.getItem(2).isEnabled = false  //disable placeholder item
            bottomNavigationView.setOnItemSelectedListener {
                vpMain.currentItem = when (it.itemId) {
                    R.id.homeFragment -> {
                        spinHome.show()
                        0
                    }
                    R.id.notificationFragment -> {
                        spinHome.show()
                        1
                    }
                    R.id.manageFragment -> {
                        spinHome.gone()
                        2
                    }
                    R.id.userFragment -> {
                        spinHome.gone()
                        3
                    }
                    else -> {
                        spinHome.show()
                        0
                    }
                }
                return@setOnItemSelectedListener true
            }
            spinHome.adapter = userHomeAdapter
            spinHome.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position == userHomeAdapter.count - 1) {
                        Toast.makeText(requireContext(), "add more", Toast.LENGTH_SHORT).show()

                    } else {
//                        Toast.makeText(this@MainActivity, userHomeAdapter.getItem(position).text, Toast.LENGTH_SHORT).show()
                        currentHomePos = position
                        userHomeAdapter.currentPosSelected = currentHomePos
                        userHomeAdapter.notifyDataSetChanged()
                        spinHome.setSelection(currentHomePos)
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

    }

    override fun initEvents() {
        super.initEvents()
        binding.fabAddDevice.setOnClickListener {
            navController.navigate(R.id.action_ryuuntaMainFragment_to_addDeviceFragment)
        }

    }
}