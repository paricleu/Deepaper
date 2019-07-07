package com.hackathon.deepaper.camera

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.hackathon.deepaper.R
import com.hackathon.deepaper.content.ContentActivity
import com.hackathon.deepaper.launchActivity
import com.hackathon.deepaper.rotate

class ImagePreviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ImageView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageFilePath = arguments?.getString(KEY_IMAGE_FILE_PATH)
        if (imageFilePath.isNullOrBlank()) {
            Navigation.findNavController(requireActivity(), R.id.mainContent).popBackStack()
        } else {
            (view as ImageView).setImageURI(Uri.parse(imageFilePath))
            val bitmap = BitmapFactory.decodeFile(imageFilePath).rotate(90)
            val image = FirebaseVisionImage.fromBitmap(bitmap)

            recognizeText(image)
        }
    }

    private fun recognizeText(image: FirebaseVisionImage) {
        val textRecognizer = FirebaseVision.getInstance()
            .onDeviceTextRecognizer

        textRecognizer.processImage(image)
            .addOnSuccessListener {
                val resultText = recognizedTextAsBlocks(it)
                if (resultText.contains(
                        "Ruhrgebiet ",
                        true
                    ) || resultText.contains(
                        "Verschwinden der Industrie ",
                        true
                    )
                ) {
                    context!!.launchActivity<ContentActivity> {
                        putExtra(ContentActivity.CONTENT_KEY, "text")
                    }
                } else {
                    Snackbar.make(requireView(), "Keine Dateien hinterlegt.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun recognizedTextAsBlocks(text: FirebaseVisionText): String = with(StringBuilder()) {
        text.textBlocks.forEach {
            append(it.text).append(" ")
        }

        toString()
    }

    companion object {
        private const val KEY_IMAGE_FILE_PATH = "key_image_file_path"
        fun arguments(absolutePath: String): Bundle {
            return Bundle().apply { putString(KEY_IMAGE_FILE_PATH, absolutePath) }
        }
    }
}