package ryuunta.iot.ryuuntaesp.main

import androidx.viewpager2.widget.ViewPager2
import ryuunta.iot.ryuuntaesp.adapter.MainPagerAdapter
import ryuunta.iot.ryuuntaesp.main.setting.UserFragment
import ryuunta.iot.ryuuntaesp.main.home.HomeFragment
import ryuunta.iot.ryuuntaesp.main.manage.ManageFragment
import ryuunta.iot.ryuuntaesp.main.notification.NotificationFragment

fun RMainFragment.initViewPager() {
    val fragmentList = listOf(
        HomeFragment.newInstance(),
        NotificationFragment.newInstance(),
        ManageFragment.newInstance(),
        UserFragment.newInstance()
    )

    binding.vpMain.apply {
        orientation = ViewPager2.ORIENTATION_HORIZONTAL
        isUserInputEnabled = false
        offscreenPageLimit = 1
        adapter = MainPagerAdapter(this@initViewPager, fragmentList)
        isSaveEnabled = false
    }
}