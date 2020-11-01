package com.example.hw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {
    private var adapter: NoteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = NoteAdapter(NoteRepository.getNotes()).also {
            rv.adapter = it
        }

        floatingActionButton.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                Dialog.show(fragmentManager, positiveAction = { title, description, position ->
                    adapter?.addElement(position, title, description)
                }, negativeAction = {
                    Snackbar.make(it, "Данные не сохранились", Snackbar.LENGTH_SHORT).show()
                })
            }
        }
    }

}