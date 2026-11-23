package com.pop.fox

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private var directLabel: TextView? = null
    private var copyDirectBtn: Button? = null
    private var directLink: String = ""
    // Must match the GitHub Pages URL after publishing the /docs folder.
    private val pagesBaseUrl = "https://ailrhame.github.io/klencod-kotlin-Pop-up_N5CR4_Android_GitHubPages/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun buildUI() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }
        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)

        root.addView(TextView(this).apply {
            text = "Pop-up"
            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        })

        val urlInput = EditText(this).apply {
            hint = "URL الهدف"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        }
        root.addView(urlInput)

        val countInput = EditText(this).apply {
            hint = "عدد النوافذ المنبثقة"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        root.addView(countInput)

        val msgInput = EditText(this).apply {
            hint = "الرسالة التي تظهر في النافذة"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 3
        }
        root.addView(msgInput)

        val genBtn = Button(this).apply { text = "تأكيد" }
        root.addView(genBtn)

        root.addView(TextView(this).apply {
            text = "رابط HTTPS للمشاركة (يفتح في المتصفح دون التطبيق):"
            textSize = 13f
            setPadding(0, 32, 0, 4)
        })

        directLabel = TextView(this).apply {
            textSize = 13f
            setTextIsSelectable(true)
        }
        root.addView(directLabel)

        copyDirectBtn = Button(this).apply {
            text = "نسخ رابط HTTPS"
            visibility = View.GONE
        }
        root.addView(copyDirectBtn)

        genBtn.setOnClickListener {
            val url = urlInput.text.toString().trim()
            val count = countInput.text.toString().trim().toIntOrNull() ?: 0
            val msg = msgInput.text.toString()
            if (count < 1) {
                Toast.makeText(this, "أدخل عدداً صحيحاً موجباً", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val normalizedUrl = normalizeTargetUrl(url)
            if (normalizedUrl.isEmpty()) {
                Toast.makeText(this, "أدخل رابطاً صحيحاً مثل https://example.com", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            directLink = Uri.parse(pagesBaseUrl).buildUpon()
                .appendQueryParameter("count", count.toString())
                .appendQueryParameter("message", msg)
                .appendQueryParameter("url", normalizedUrl)
                .build().toString()

            directLabel?.text = directLink
            copyDirectBtn?.visibility = View.VISIBLE
        }

        copyDirectBtn?.setOnClickListener { copy(directLink, "تم نسخ رابط HTTPS") }
    }

    private fun copy(text: String, msg: String) {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("link", text))
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // Kept for Android lifecycle callbacks; shared HTTPS links are handled by the browser page.
    private fun handleIntent(intent: Intent?) {
        // No custom app scheme is required for the GitHub Pages HTTPS flow.
    }

    private fun normalizeTargetUrl(raw: String): String {
        val value = raw.trim()
        if (value.isEmpty()) return ""
        val candidate = if (value.startsWith("https://", true) || value.startsWith("http://", true)) {
            value
        } else {
            "https://$value"
        }
        return try {
            val parsed = Uri.parse(candidate)
            if ((parsed.scheme == "https" || parsed.scheme == "http") && !parsed.host.isNullOrBlank()) {
                parsed.toString()
            } else ""
        } catch (_: Exception) {
            ""
        }
    }
}