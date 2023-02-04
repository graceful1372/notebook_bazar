package com.graceful1372.notebook.ui.about

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.graceful1372.notebook.R
import com.graceful1372.notebook.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentAboutBinding

    //Links
    private val linkedin = "https://www.linkedin.com/in/hossein-moghadasi-barazande-a70687178/"
    private val github = "https://github.com/graceful1372"
    private val gmail = "graceful1372@gmail.com"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitView
        binding.apply {
            imgLinkedin.setOnClickListener {


                openUrl(linkedin)
            }
            imgGithub.setOnClickListener {
                openUrl(github)
            }
            imgGmail.setOnClickListener {
                copyToClipboard(gmail)
                Toast.makeText(activity, R.string.address_copy, Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun openUrl(link: String) =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    private fun copyToClipboard(text: String) {

        val clip = ClipData.newPlainText("label", text)
        getSystemService(requireContext(), ClipboardManager::class.java)?.setPrimaryClip(clip)
    }

}