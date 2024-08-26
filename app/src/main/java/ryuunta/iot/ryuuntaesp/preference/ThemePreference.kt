package ryuunta.iot.ryuuntaesp.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val THEME_PREFERENCES = "ryuunta_theme_preferences"

val Context.themePreference by preferencesDataStore(
    name = THEME_PREFERENCES
)

class ThemePreference(val isDarkMode: Boolean = false) {


    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

        fun getThemePreferenceData(
            context: Context,
            onError: (Throwable) -> Unit,
            onComplete: (ThemePreference) -> Unit
        ) {
            val themePreferenceFlow: Flow<ThemePreference> = context.themePreference.data
                .catch { exception ->
                    // dataStore.data throws an IOException when an error is encountered when reading data
//            emit(emptyPreferences())
                    onError(exception)
                    // if get preference error -> return default ThemePreference
                    onComplete(ThemePreference())
                }.map { preferences ->
                    // Get our show completed value, defaulting to false if not set:
                    val showCompleted = preferences[IS_DARK_MODE] ?: false
                    ThemePreference(showCompleted)
                }
            CoroutineScope(Dispatchers.Default).launch {
                themePreferenceFlow.collect {
                    onComplete(it)
                }
            }
        }

        suspend fun setDarkMode(context: Context, isDarkMode: Boolean = false) {
            context.themePreference.edit { preferences ->
                preferences[IS_DARK_MODE] = isDarkMode
            }
        }

    }
}