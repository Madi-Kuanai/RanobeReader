package com.mk.ranobereader.presentation.ranobeInfoScreen.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.FragmentRanobeInformationFragmentBinding
import com.mk.ranobereader.presentation.homeScreen.GenresAdapter
import com.mk.ranobereader.presentation.homeScreen.MarginItemDecoration

import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModel
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModelFactory
import okhttp3.internal.notify


class RanobeInformationFragment : Fragment() {
    lateinit var binding: FragmentRanobeInformationFragmentBinding
    private lateinit var ranobeVM: RanobeInfoViewModel
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var tagsAdapter: GenresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRanobeInformationFragmentBinding.inflate(layoutInflater)
        ranobeVM =
            ViewModelProvider(
                requireActivity(),
                RanobeInfoViewModelFactory()
            )[RanobeInfoViewModel::class.java]

        setObserver()

        ranobeVM.ranobe.observe(viewLifecycleOwner) { nRanobeModel ->
            binding.apply {
                Log.d(TAG, nRanobeModel.author.toString())
                description.text = nRanobeModel.description
                if (nRanobeModel.author.isNullOrEmpty()) {
                    author.visibility = View.GONE
                } else {
                    author.text = nRanobeModel.author
                }
                title.text = nRanobeModel.title
                description.text = nRanobeModel.description
                stateOfTitle.text = nRanobeModel.statusOfTitle
                stateOfTranslationTextView.text = nRanobeModel.stateOfTranslation
                numberOfChaptersTextView.text = nRanobeModel.numberOfChapters
                yearOfAnons.text = nRanobeModel.yearOfAnons
                genresAdapter = GenresAdapter()
                tagsAdapter = GenresAdapter()
                genresRecView.addItemDecoration(
                    MarginItemDecoration(
                        top = 2,
                        left = 10,
                        right = 5,
                        bottom = 5
                    )
                )

                tags.addItemDecoration(
                    MarginItemDecoration(
                        top = 2,
                        left = 10,
                        right = 5,
                        bottom = 5
                    )
                )
                genresRecView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                genresRecView.adapter = genresAdapter

// Attach the tagsAdapter to tags
                tags.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                tags.adapter = tagsAdapter
                tagsAdapter.addGenres(nRanobeModel.tags)
                tagsAdapter.notifyDataSetChanged();
                genresAdapter.addGenres(nRanobeModel.genres)
                genresAdapter.notifyDataSetChanged()

            }

        }

        return binding.root
    }

    private fun setObserver() {
    }
}