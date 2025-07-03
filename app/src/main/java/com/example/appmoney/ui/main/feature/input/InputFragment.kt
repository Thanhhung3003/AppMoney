package com.example.appmoney.ui.main.feature.input

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.R
import com.example.appmoney.databinding.FragmentInputBinding
import com.example.appmoney.ui.common.helper.Constant.BUNDLE_KEY_TRANSACTION
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.TimeFormat
import com.example.appmoney.ui.common.helper.TimeHelper
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.input.viewPagger.InputViewpagerAdapter
import com.example.appmoney.ui.main.feature.transactionhistory.TransactionDetail
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar


class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: InputViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel
    private lateinit var adapter: InputViewpagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[InputViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]

        // set ngày hiện tại làm mặc định
        val today = Calendar.getInstance()
        viewModel.updateState(viewModel.state.value?.copy(date = today))
        binding.tvDate.text = TimeHelper.getByFormat(today, TimeFormat.Date)

        setupViewPager()
        setupObserver()
        setupTabSelected()
        setupDatePicker()
        bindViewsEvent()
    }

    private fun bindViewsEvent() {
        binding.btnSave.setOnClickListener {
            handleDoneButton()
        }

        binding.btnBack.setOnClickListener {
            viewModel.updateState(TransactionState())
            requireActivity().navigateFragment(AppScreen.HistoryTrans)
            binding.btnBack.visibility = View.GONE
        }

        binding.edtNote.doOnTextChanged {
                text, _, _, _ ->
            viewModel.updateState(viewModel.state.value?.copy(note = text.toString()))
        }
        binding.edtMoney.doOnTextChanged { text, _, _, _ ->
            val amountText = text.toString()
            val amount = amountText.toLongOrNull() ?: 0L
            viewModel.updateState(viewModel.state.value?.copy(amount = amount))
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()

        sharedViewModel.getExpenditureCat {
            showApiResultToast(false, it)
        }
        sharedViewModel.getIncomeCat {
            showApiResultToast(false, it)
        }
    }

    // Thêm/update transaction
    private fun handleDoneButton() {
        val currentTab = viewModel.selectedTab.value ?: 0
        // truy xuất fragment và ép kiểu sau đó gọi getSelectedCategory() lấy category được chọn
        (adapter.map[currentTab] as? InputFragmentBehaviour)?.getSelectedCategory()
            ?.let { category ->
                val typeTrans = if (TabObject.tabPosition == 0) {
                    getString(R.string.cat_expenditure)
                } else {
                    getString(R.string.cat_income)
                }
                viewModel.handleDoneButton(
                    category.idCat, typeTrans,
                    onSuccess = {
                        showApiResultToast(true)
                        binding.edtNote.setText("")
                        binding.edtMoney.setText("")

                        // onUpdate success
                        if (viewModel.state.value?.idTrans != null) {
                            viewModel.updateState(TransactionState())
                            binding.btnBack.visibility = View.GONE
                            requireActivity().navigateFragment(AppScreen.HistoryTrans)
                        }
                    },
                    onFailure = { err ->
                        showApiResultToast(false, err)
                    })
            }

    }

    // Setup Observer ------------------------
    private fun setupObserver() {
        viewModel.selectedTab.observe(viewLifecycleOwner) { tab ->
            binding.Vp.currentItem = tab
        }
        viewModel.err.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErr()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            val dateString = TimeHelper.getByFormat(it.date, TimeFormat.Date)
            binding.apply {
                if (it.amount != 0L) {
                    val amountText = it.amount.toString()
                    if (binding.edtMoney.text.toString() != amountText) {
                        binding.edtMoney.setText(amountText)
                    }
                } else {
                    if (binding.edtMoney.text.toString().isNotBlank()) {
                        binding.edtMoney.setText("")
                    }
                }
            }

            viewModel.state.value?.idTrans?.let {
                binding.btnBack.visibility = View.VISIBLE
            }
        }
    }

    // Setup Date-----------------------------
    private fun setupDatePicker() {
        binding.apply {
            btnPre.setOnClickListener { changeDate(-1) }
            btnAfter.setOnClickListener { changeDate(1) }
            tvDate.setOnClickListener { showDatePicker() }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = selection
            }
            viewModel.updateState(viewModel.state.value?.copy(date = calendar))
            binding.tvDate.text = TimeHelper.getByFormat(calendar, TimeFormat.Date)
        }
        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun changeDate(day: Int) {
        val calendar = Calendar.getInstance().apply {
            viewModel.state.value?.date?.let {
                time = it.time
            } ?: run {
                timeInMillis = System.currentTimeMillis()
            }
        }
        calendar.add(Calendar.DAY_OF_MONTH, day)
        viewModel.updateState(viewModel.state.value?.copy(date = calendar))
        binding.tvDate.text = TimeHelper.getByFormat(calendar, TimeFormat.Date)
    }

    // setup Tab and ViewPager-----------------------------------
    private fun setupTabSelected() {

        binding.tabMoney.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setTab(it.position)
                    if (it.position == 0) {
                        binding.tvTypeMoney.text = getString(R.string.expenditure_money)
                        binding.btnSave.text = getString(R.string.save_expenditure)

                    } else {
                        binding.tvTypeMoney.text = getString(R.string.income_money)
                        binding.btnSave.text = getString(R.string.save_income)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun setupViewPager() {
        adapter = InputViewpagerAdapter(childFragmentManager, lifecycle)
        binding.Vp.adapter = adapter
        binding.Vp.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabMoney, binding.Vp) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.expenditure_money)
                }

                1 -> {
                    tab.text = getString(R.string.income_money)
                }
            }
        }.attach()
    }

    //-----------------------------------
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // fragment hiển thị trở lại
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            binding.Vp.setCurrentItem(TabObject.tabPosition, true)

            receiveDataFromBundle()
        }
    }
    // Lấy dữ liệu từ Bundle
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun receiveDataFromBundle() {
        val trans = arguments?.getSerializable(BUNDLE_KEY_TRANSACTION, TransactionDetail::class.java)
        trans?.let {

            val tab = if (it.typeTrans == "Expenditure") 0 else 1
            viewModel.setTab(tab)

            val timeMillis = TimeHelper.stringToTimestamp(it.date)?.toDate()
            val date = Calendar.getInstance().apply {
                timeMillis?.let {
                    time = timeMillis
                }
            }
            val newState = viewModel.state.value?.copy(
                idTrans = it.idTrans,
                date = date,
                note = it.note,
                amount = it.amount,
            )
            viewModel.updateState(newState)

            val categoryId = it.categoryId
            (adapter.map[tab] as? InputFragmentBehaviour)?.setSelectedCategoryById(categoryId)

        } ?: run {
            viewModel.updateState(TransactionState())
        }
    }
}