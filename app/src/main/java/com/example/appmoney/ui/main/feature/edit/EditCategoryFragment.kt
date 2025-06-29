package com.example.appmoney.ui.main.feature.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmoney.R
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryColorWrapper
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.CategoryImageWrapper
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.databinding.FragmentEditCategoryBinding
import com.example.appmoney.ui.common.adapter.CategoryColorAdapter
import com.example.appmoney.ui.common.adapter.CategoryColorListener
import com.example.appmoney.ui.common.adapter.CategoryImageAdapter
import com.example.appmoney.ui.common.adapter.CategoryImageListener
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class EditCategoryFragment : Fragment(), CategoryImageListener,
    CategoryColorListener {
    // binding
    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private lateinit var viewModel: EditCategoryViewModel

    // adapter
    private val imageAdapter = CategoryImageAdapter()
    private val colorAdapter = CategoryColorAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[EditCategoryViewModel::class.java]

        initViews()
        observer()
        binding.apply {
            rcImages.layoutManager = GridLayoutManager(requireContext(), 4)
            rcColors.layoutManager = GridLayoutManager(requireContext(), 4)
            rcImages.adapter = imageAdapter
            rcColors.adapter = colorAdapter

            createCategory()

            backButton.setOnClickListener {
                backToPrevious()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createCategory() {
        binding.saveButton.setOnClickListener {
            // category name input from keyboard
            val sName = binding.edtTransaction.text.toString()

            val typeId = if (TabObject.tabPosition == 0) {
                getString(R.string.cat_expenditure)
            } else {
                getString(R.string.cat_income)
            }
            viewModel.checkCategoryExists(typeId, sName) { exists ->
                if (exists) {
                    Toast.makeText(requireContext(), "Tên category đã tồn tại", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addCategory(
                        typeId,
                        sName,
                        onSuccess = {
                            showApiResultToast(isSuccess = true)
                            backToPrevious()
                        },
                        onFailure = { error ->
                            showApiResultToast(isSuccess = false, message = error)
                        })
                }
            }
        }
    }


    private fun backToPrevious() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun initViews() {

        imageAdapter.setListener(this)
        colorAdapter.setListener(this)

        val listImage = listOf(
            CategoryImage.BOOK,
            CategoryImage.BUS,
            CategoryImage.GAME,
            CategoryImage.BASKET,
            CategoryImage.BALL,
            CategoryImage.BYCICLE,
            CategoryImage.CALL,
            CategoryImage.CUT,
            CategoryImage.ELECTRIC,
            CategoryImage.EDUCATION,
            CategoryImage.FRUIT,
            CategoryImage.FUELSTATION,
            CategoryImage.GIFT,
            CategoryImage.LIGHT,
            CategoryImage.MEDICAL,
            CategoryImage.MUSIC,
            CategoryImage.SKINCARE,
            CategoryImage.TELEVISION,
            CategoryImage.TSHIRT,
            CategoryImage.WIFI,
            CategoryImage.SALARY,
            CategoryImage.INVESTMENT,
            CategoryImage.REFUND,
            CategoryImage.RENT
        )
        val listColor = listOf(
            CategoryColor.BLUE,
            CategoryColor.GREEN,
            CategoryColor.ORANGE,
            CategoryColor.PURPLE,
            CategoryColor.RED,
            CategoryColor.PINK,
            CategoryColor.TEAL,
            CategoryColor.YELLOW,
            CategoryColor.BLACK,
            CategoryColor.GRAY,
            CategoryColor.BROWN,
            CategoryColor.DARK_BLUE
        )
        viewModel.loadItemsImage(listImage)
        viewModel.loadItemsColor(listColor)
    }

    private fun observer() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
        }

        viewModel.colors.observe(viewLifecycleOwner) { colors ->
            colors.firstOrNull { it.isSelected }?.let {
                imageAdapter.setTintColor(it.categoryColor)
            }
            colorAdapter.submitList(colors)
        }

        viewModel.err.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErr()
            }
        }
    }


    override fun onItemClick(categoryImage: CategoryImageWrapper) {
        viewModel.onSelectedCategoryImage(categoryImage.categoryImage)

    }

    override fun onItemClick(categoryColor: CategoryColorWrapper) {
        viewModel.onSelectedCategoryColor(categoryColor.categoryColor)

    }


}

