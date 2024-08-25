package ryuunta.iot.ryuuntaesp.preference

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object SettingPreference {
    private const val SETTING_PREFERENCE = "ryuunta_setting_preference"

    private val Context.ryuuntaSetting by preferencesDataStore(
        name = SETTING_PREFERENCE
    )

    fun <V> saveData(context: Context, data: Map<String, V>) {
        val convertData = data.map { (key, value) ->
            val newKey = stringPreferencesKey(key)
            val newValue = if (value is String) {
                value
            } else {
                Gson().toJson(value)
            }

            Pair(newKey, newValue)
        }.toMap()
        CoroutineScope(Dispatchers.Main).launch {
            convertData.forEach {
                context.ryuuntaSetting.edit { pref ->
                    pref[it.key] = it.value
                }
            }
        }
    }

    fun getData(context: Context, keys: List<String>, onComplete: (RPreference) -> Unit) {
        keys.forEach { key ->
            val prefKey = stringPreferencesKey(key)
            val settingPrefFlow: Flow<RPreference> =
                context.ryuuntaSetting.data.catch { exception ->
                    exception.printStackTrace()
                    onComplete(RPreference())
                }.map { pref ->
                    val value = pref[prefKey] ?: ""
                    RPreference(value)
                }
            CoroutineScope(Dispatchers.Main).launch {
                settingPrefFlow.collectLatest {
                    onComplete(it)
                }
            }
        }
    }

    fun clearData(context: Context, keys: List<String>) {
        val prefKey = keys.map { stringPreferencesKey(it) }
        CoroutineScope(Dispatchers.Main).launch {
            prefKey.forEach {
                context.ryuuntaSetting.edit { pref ->
                    if (pref.contains(it)) {
                        pref.remove(it)
                    }
                }
            }
        }
    }

    fun clearAllData(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            context.ryuuntaSetting.edit {
                it.clear()
            }
        }
    }
}