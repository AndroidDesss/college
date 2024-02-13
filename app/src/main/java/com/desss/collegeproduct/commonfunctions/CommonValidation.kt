package com.desss.collegeproduct.commonfunctions

import android.widget.EditText
import java.util.regex.Pattern

class CommonValidation {

    companion object {

        private const val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        private const val PHONE_NUMBER_PATTERN =
            "^[+]?[0-9]{1,4}[-\\s.?]?[(]?[0-9]{1,3}[)]?[-\\s.?]?[0-9]{1,4}[-\\s.?]?[0-9]{1,9}$"

        private val emailPattern: Pattern = Pattern.compile(EMAIL_PATTERN)

        private val numberPattern: Pattern = Pattern.compile(PHONE_NUMBER_PATTERN)

        //To Check Whether the editText is empty or not
        fun isEditTextNotEmpty(editText: EditText): Boolean {
            val text = editText.text.toString().trim()
            return text.isNotEmpty()
        }

        //To Check Valid Email
        fun isValidEmail(email: String): Boolean {
            val matcher = emailPattern.matcher(email)
            return matcher.matches()
        }

        //To Check Valid PhoneNumber
        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            val matcher = numberPattern.matcher(phoneNumber)
            return matcher.matches()
        }

        //To Check Valid Password
        fun isPasswordValid(password: String, passwordLength: Int): Boolean {
            return password.length >= passwordLength
        }

        //To Check Spinner Value
        fun isSpinnerValidate(spinnerValue: String): Boolean {
            return spinnerValue != "Select"
        }
    }
}