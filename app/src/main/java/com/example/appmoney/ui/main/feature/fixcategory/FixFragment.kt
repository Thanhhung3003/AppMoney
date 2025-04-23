package com.example.appmoney.ui.main.feature.fixcategory

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmoney.R
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryColorWrapper
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.CategoryImageWrapper
import com.example.appmoney.databinding.FragmentFixBinding
import com.example.appmoney.ui.common.adapter.CategoryColorAdapter
import com.example.appmoney.ui.common.adapter.CategoryColorListener
import com.example.appmoney.ui.common.adapter.CategoryImageAdapter
import com.example.appmoney.ui.common.adapter.CategoryImageListener
import com.example.appmoney.ui.common.helper.Constant.BUNDLE_KEY_CATEGORY
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class FixFragment : Fragment(), CategoryImageListener,
    CategoryColorListener {
    private var _binding: FragmentFixBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FixViewModel
    private val imageAdapter = CategoryImageAdapter()
    private val colorAdapter = CategoryColorAdapter()
    private lateinit var auth: FirebaseAuth
    private var catUpdate: Category = Category()
    private lateinit var typeCat: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFixBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        viewModel = ViewModelProvider(requireActivity())[FixViewModel::class.java]

        initViews()
        observer()
        bindViewEvent()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        arguments?.getSerializable(BUNDLE_KEY_CATEGORY, Category::class.java)?.let {
            viewModel.setCat(it)
        }
    }

    private fun bindViewEvent() {
        binding.apply {
            rcImages.layoutManager = GridLayoutManager(requireContext(), 4)
            rcColors.layoutManager = GridLayoutManager(requireContext(), 4)
            rcImages.adapter = imageAdapter
            rcColors.adapter = colorAdapter
            btnUpdate.setOnClickListener {
                updateCat()
            }

            backButton.setOnClickListener {
                backToPrevious()
            }
        }
    }

    private fun updateCat() {
        val sImage = viewModel.getImageSelected()
        val sColor = viewModel.getColorSelected()
        val sName = binding.edtTransaction.text.toString()

        if (sImage == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn biểu tượng", Toast.LENGTH_SHORT).show()
            return
        }

        if (sColor == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn màu", Toast.LENGTH_SHORT).show()
            return
        }

        if (sName.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val updateCategory = Category(
            image = sImage,
            color = sColor,
            desCat = sName,
            timeCreate = catUpdate.timeCreate

        )
        viewModel.updateCat(
            typeCat,
            catUpdate.idCat,
            updateCategory,
            onSuccess = { showApiResultToast(true) },
            onFailure = { showApiResultToast(false, it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun backToPrevious() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun initViews() {

        typeCat = if (TabObject.tabPosition == 0) {
            getString(R.string.cat_expenditure)
        } else {
            getString(R.string.cat_income)
        }

        imageAdapter.setListener(this)
        colorAdapter.setListener(this)

        val listImage = listOf(
            CategoryImage.BOOK,
            CategoryImage.BUS,
            CategoryImage.GAME,
            CategoryImage.BASKET,
            CategoryImage.BALL,
            CategoryImage.EDIT
        )
        val listColor = listOf(
            CategoryColor.BLUE,
            CategoryColor.GREEN,
            CategoryColor.ORANGE,
            CategoryColor.PURPLE,
            CategoryColor.RED
        )
        viewModel.loadItemsImage(listImage)
        viewModel.loadItemsColor(listColor)
    }

    private fun observer() {

        viewModel.err.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.category.observe(viewLifecycleOwner) { category ->
            viewModel.onSelectedCategoryColor(category.color)
            viewModel.onSelectedCategoryImage(category.image)
            binding.edtTransaction.setText(category.desCat)
            catUpdate = category
        }

        viewModel.images.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
        }

        viewModel.colors.observe(viewLifecycleOwner) { colors ->
            colors.firstOrNull { it.isSelected }?.let {
                imageAdapter.setTintColor(it.categoryColor)
            }
            colorAdapter.submitList(colors)
        }
    }


    override fun onItemClick(categoryImage: CategoryImageWrapper) {
        viewModel.onSelectedCategoryImage(categoryImage.categoryImage)

    }

    override fun onItemClick(categoryColor: CategoryColorWrapper) {
        viewModel.onSelectedCategoryColor(categoryColor.categoryColor)

    }


}