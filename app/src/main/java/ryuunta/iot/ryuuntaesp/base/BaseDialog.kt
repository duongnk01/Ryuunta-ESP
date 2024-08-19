package ryuunta.iot.ryuuntaesp.base

import android.app.Dialog
import android.content.Context
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View

class BaseDialog(context: Context) : Dialog(context) {
    private var onCreateContextMenu: ((
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) -> Unit)? = null
    private var onContextItemSelected: ((item: MenuItem) -> Boolean)? = null

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        onCreateContextMenu?.invoke(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return onContextItemSelected?.invoke(item) ?: super.onContextItemSelected(item)
    }

    fun onCreateContextMenu(
        func: (
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) -> Unit
    ) {
        onCreateContextMenu = func
    }

    fun onContextItemSelected(func: (item: MenuItem) -> Boolean) {
        onContextItemSelected = func
    }
}