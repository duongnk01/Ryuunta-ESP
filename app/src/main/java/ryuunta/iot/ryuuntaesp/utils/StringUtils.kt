package ryuunta.iot.ryuuntaesp.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * method is used for checking valid email id format.
 *
 * @param email
 * @return boolean true for valid false for invalid
 */
fun isEmailPatternValid(email: String): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}
fun isEmailAccountValid(email: String): Boolean =
    isEmailPatternValid(email) && email.isNotEmpty()

fun isPasswordValid(password: String): Boolean =
    password.length >= 6