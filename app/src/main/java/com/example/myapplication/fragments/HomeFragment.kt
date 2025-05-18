package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import androidx.appcompat.app.AlertDialog
import android.content.ContentResolver
import android.net.Uri

import android.provider.OpenableColumns
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider


class HomeFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val fileNames = mutableListOf<String>()

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                savePdfToInternalStorage(it)
                refreshPdfList()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val pickButton = view.findViewById<Button>(R.id.pickPdfButton)
        listView = view.findViewById(R.id.pdfListView)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fileNames)
        listView.adapter = adapter

        pickButton.setOnClickListener {
            pickPdfLauncher.launch(arrayOf("application/pdf"))
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val fileName = fileNames[position]
            openPdfFile(fileName)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val fileName = fileNames[position]
            showDeleteDialog(fileName)
            true
        }

        refreshPdfList()

        return view
    }

    private fun savePdfToInternalStorage(uri: Uri) {
        val fileName = queryFileName(uri) ?: "document_${System.currentTimeMillis()}.pdf"
        val file = File(requireContext().filesDir, fileName)

        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            Toast.makeText(requireContext(), "PDF сохранён: $fileName", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Ошибка при сохранении PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun queryFileName(uri: Uri): String? {
        val returnCursor = requireContext().contentResolver.query(uri, null, null, null, null)
        returnCursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return null
    }

    private fun refreshPdfList() {
        fileNames.clear()
        val files = requireContext().filesDir.listFiles { file -> file.extension == "pdf" }
        files?.forEach { file ->
            fileNames.add(file.name)
        }
        adapter.notifyDataSetChanged()
    }

    private fun openPdfFile(fileName: String) {
        val file = File(requireContext().filesDir, fileName)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Проверка, есть ли приложения для открытия PDF
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Нет приложений для открытия PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteDialog(fileName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить файл")
            .setMessage("Вы уверены, что хотите удалить $fileName?")
            .setPositiveButton("Удалить") { _, _ ->
                val file = File(requireContext().filesDir, fileName)
                if (file.exists()) {
                    file.delete()
                    Toast.makeText(requireContext(), "Файл удалён", Toast.LENGTH_SHORT).show()
                    refreshPdfList()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
