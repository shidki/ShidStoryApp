package com.example.submissionstoryapp.ui.register

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissionstoryapp.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs){

    private var showWarnImage: Drawable
    private var iconPassword: Drawable
    private var errorTextView: TextView? = null

    init {
        showWarnImage = ContextCompat.getDrawable(context, R.drawable.warn) as Drawable
        iconPassword = ContextCompat.getDrawable(context, R.drawable.password) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && s.length < 8) {
                    showClearButton()
                    if (s.toString().isNotEmpty() && s.length < 8) {
                        showClearButton()
                        showErrorTextView(true, "Password tidak boleh kurang dari 8 huruf")
                    } else {
                        hideClearButton()
                        showErrorTextView(false)
                    }
                } else {
                    hideClearButton()
                    showErrorTextView(false)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = showWarnImage, startOfTheText = iconPassword)
    }

    private fun hideClearButton() {
        setButtonDrawables(startOfTheText = iconPassword)
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
    fun setErrorTextView(textView: TextView) {
        errorTextView = textView
    }
    private fun showErrorTextView(show: Boolean, errorText: String? = null) {
        errorTextView?.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            errorTextView?.text = errorText
        }
    }

}

