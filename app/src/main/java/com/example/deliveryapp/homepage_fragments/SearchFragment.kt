package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
//import androidx.compose.ui.input.key.type
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
//import androidx.compose.ui.semantics.requestFocus
//import androidx.compose.ui.semantics.setText
//import androidx.compose.ui.semantics.text
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
//import androidx.glance.visibility
//import androidx.preference.isNotEmpty
//import androidx.glance.visibility
import com.example.deliveryapp.Dishes.DishItems
import com.example.deliveryapp.Dishes.ShopsFragment
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.tabs.TabLayout
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlin.text.clear


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragmentNavigation: HomepageNavigation? = null




    private var currentSearchQuery: String = ""
    private var isSearchActive = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomepageNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

      updateSearchUIVisibility()
        binding.searchIcon.setOnClickListener {

          toggleSearchActivation()
        }



        binding.backButton.setOnClickListener {


           if(isSearchActive){
               toggleSearchActivation(forceDeactivate= true)

            } else{
                fragmentNavigation?.replaceFragment(HomeFragment())
                bottomNavigationView.selectedItemId = R.id.bottom_home
            }

        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()

                if(isSearchActive) {
                    filterCurrentFragment(currentSearchQuery)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString().trim()

               currentSearchQuery = query
                filterCurrentFragment(currentSearchQuery)
                hideKeyboard()
                binding.searchEditText.clearFocus()

                return@setOnEditorActionListener true
            }
            false
        }

        setupTabs()
//        displayRecentSearches()

        // Load the first fragment by default
        if (savedInstanceState == null) {

            replaceFragment(ShopsFragment(), currentSearchQuery)
        }else{
            currentSearchQuery = savedInstanceState.getString("search_query_key", "")
            isSearchActive = savedInstanceState.getBoolean("search_active_key", false)

            binding.searchEditText.setText(currentSearchQuery)

            updateSearchUIVisibility()

            val selectedTabPosition = binding.tabLayout.selectedTabPosition
            val fragmentToLoad = if (selectedTabPosition == 1) DishItems() else ShopsFragment()
            replaceFragment(fragmentToLoad, currentSearchQuery)
        }

    }
//    private fun performSearch(query: String) {
//        Log.d(TAG, "performSearch called with query: '$query'")
//        currentSearchQuery = query
//        saveSearch(query)
//        displayRecentSearches()
//        filterCurrentFragment(query)
//        hideKeyboard()
//        binding.searchEditText.clearFocus()
//    }






    private fun toggleSearchActivation(forceDeactivate: Boolean = false) {

       val newActiveState= if (forceDeactivate) false else !isSearchActive
//        updateSearchUIVisibility()

        if (newActiveState == isSearchActive && !forceDeactivate) {

            return
        }
        isSearchActive = newActiveState
        updateSearchUIVisibility()

        if (isSearchActive) {
            currentSearchQuery = binding.searchEditText.text.toString()

            updateSearchUIVisibility()
            binding.searchEditText.requestFocus()
            showKeyboard()
            filterCurrentFragment(currentSearchQuery)

        } else {


            hideKeyboard()
            binding.searchEditText.clearFocus()
            if (forceDeactivate) {
                binding.searchEditText.setText("")
                currentSearchQuery = ""
            }

        }
    }
    private fun updateSearchUIVisibility() {



        if (isSearchActive) {
            binding.titleTextView.visibility = View.GONE
            binding.searchInputLayout.visibility = View.VISIBLE


            bottomNavigationView.visibility = View.GONE




            binding.searchEditText.requestFocus()
            showKeyboard()

            bottomNavigationView.visibility = View.GONE

        } else {

            binding.titleTextView.visibility = View.VISIBLE
            binding.searchInputLayout.visibility = View.GONE

            bottomNavigationView.visibility = View.VISIBLE

        }
    }


    private fun setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.shops)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.dishes)))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val  fragmentToLoad = when (tab.position) {
                    0 -> ShopsFragment()
                    1 -> DishItems()
                    else -> ShopsFragment()
                }
                replaceFragment(fragmentToLoad, currentSearchQuery)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun filterCurrentFragment(query: String) {

        val currentFragment = childFragmentManager.findFragmentById(R.id.fragmentContainer)

        when (currentFragment) {
            is ShopsFragment -> currentFragment.filterShops(query)
            is DishItems -> currentFragment.filterDishes(query)
        }
    }

    private fun replaceFragment(fragment: Fragment, queryForFragment: String) {
        val args = Bundle().apply {
            putString("search_query", queryForFragment)
        }
        fragment.arguments = args
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    private fun showKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
