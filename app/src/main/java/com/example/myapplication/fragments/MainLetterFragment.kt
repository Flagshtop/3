package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.databinding.FragmentMainLetterBinding
import com.example.myapplication.viewmodel.NoteViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.model.Note
import kotlinx.coroutines.launch

class MainLetterFragment : Fragment(R.layout.fragment_main_letter), SearchView.OnQueryTextListener, MenuProvider {
    private var HomeBinding: FragmentMainLetterBinding?= null
    private val binding get() = HomeBinding!!

    private lateinit var notesViewModel : NoteViewModel
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        HomeBinding=FragmentMainLetterBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        notesViewModel=(activity as MainActivity).noteViewModel
        setupHomeRecyclerView()
        binding.addNoteFab.setOnClickListener() {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }
    private fun updateUI(note: List<Note>?){
        if(note != null){
            binding.emptyNotesImage.visibility = View.GONE
            binding.homeRecyclerView.visibility = View.VISIBLE
        }else{
            binding.emptyNotesImage.visibility = View.VISIBLE
            binding.homeRecyclerView.visibility = View.GONE
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupHomeRecyclerView(){
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        noteAdapter.setOnItemClickListener { note ->
            val direction = MainLetterFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
            findNavController().navigate(direction)
        }

        noteAdapter.setOnItemLongClickListener { note, view ->
            showPopupMenu(note, view)
        }

        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { noteList ->
                noteAdapter.differ.submitList(noteList)
                updateUI(noteList)
            }
        }
    }

    private fun showPopupMenu(note: Note, view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_edit_note, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.deleteMenu -> {
                    notesViewModel.deleteNote(note)
                    true
                }
                else -> false
            }
        }

        popup.show()
    }


    private fun searchNote(query: String?){
        val searchQuery = "%$query"

        notesViewModel.searchNote(searchQuery).observe(this){
            list -> noteAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null ){
            searchNote(newText)

        }
        return true
    }

    override fun onDestroy(){
        super.onDestroy()
        HomeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

}