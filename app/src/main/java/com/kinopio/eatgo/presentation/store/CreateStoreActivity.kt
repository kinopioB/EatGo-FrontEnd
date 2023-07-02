package com.kinopio.eatgo.presentation.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils.dpToPx
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding
import com.kinopio.eatgo.databinding.OpenInfoTimePickerBinding
import com.kinopio.eatgo.domain.store.ui_model.MenuForm
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo


class CreateStoreActivity : AppCompatActivity() {
    private lateinit var menuFormAdapter: MenuFormAdapter
    private lateinit var  openInfoAdapter :  OpenInfoAdapter

    private val menuList= mutableListOf<MenuForm>()
    private val openInfoList = mutableListOf<OpenInfo>()

    private lateinit var binding: ActivityCreateStoreBinding

    private var selectedMenuImageUri: Uri? = null
    private var selectedStoreImgUri : Uri?= null

    private var selectedToggleButton: ToggleButton? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var storeImageLauncher: ActivityResultLauncher<Intent>



    private val toggleButtonChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            // 하나의 토글 버튼이 선택되면 이전에 선택되었던 토글 버튼의 선택 상태를 해제합니다.
            if (selectedToggleButton != null && selectedToggleButton != buttonView) {
                selectedToggleButton?.isChecked = false
            }
            selectedToggleButton = buttonView as ToggleButton
        } else {
            // 선택이 해제되면 selectedToggleButton을 null로 설정합니다.
            if (selectedToggleButton == buttonView) {
                selectedToggleButton = null
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToggleButtons()


        menuFormAdapter = MenuFormAdapter(menuList)
        openInfoAdapter = OpenInfoAdapter(openInfoList)

        binding.mRecyclerView.apply {
            adapter = menuFormAdapter
            layoutManager = LinearLayoutManager(this@CreateStoreActivity)
        }

        binding.openRecyclerView.apply {
            adapter = openInfoAdapter
            layoutManager = LinearLayoutManager(this@CreateStoreActivity)
        }

        // 메뉴 추가 창 보이게
        binding.menuAddBtn.setOnClickListener {
            val isInputVisible = binding.menuInputLayout.visibility == View.VISIBLE
            if (isInputVisible) {
                binding.menuInputLayout.visibility = View.GONE
            } else {
                binding.menuInputLayout.visibility = View.VISIBLE
            }
        }

        // 가게 사진 추가
        binding.storeImgAddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            storeImageLauncher.launch(intent)
        }


        // 가게  사진 추가 -> 갤러리 연동
        storeImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        selectedStoreImgUri = imageUri
                        binding.storeImgLayout.visibility = View.VISIBLE
                        binding.storeImg.setImageURI(selectedStoreImgUri)
                    }
                }
            }
        }


        // 메뉴 사진 추가
        binding.buttonAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }


        // 메뉴 사진 추가 -> 갤러리 연동
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        selectedMenuImageUri = imageUri
                        binding.menuImg.setImageURI(selectedMenuImageUri)
                    }
                }
            }
        }


        // 메뉴 추가
        binding.buttonAdd.setOnClickListener {
            val menuName = binding.editTextMenuName.text.toString()
            val menuCount = binding.editTextMenuCount.text.toString().toIntOrNull()
            val menuPrice = binding.editTextMenuPrice.text.toString().toIntOrNull()
            val menuInfo = "test" //칸 추가 필요
            if (menuName.isNotEmpty() && menuCount != null && menuPrice != null) {
                val menu = MenuForm(menuName, menuCount, menuPrice, menuInfo, selectedMenuImageUri)
                menuList.add(menu)
                menuFormAdapter.notifyDataSetChanged()

                // Reset input fields
                binding.editTextMenuName.text.clear()
                binding.editTextMenuCount.text.clear()
                binding.editTextMenuPrice.text.clear()

                // Hide the input layout
                binding.menuInputLayout.visibility = View.GONE

                binding.menuImg.setImageURI(null)
                selectedMenuImageUri = null
            } else {
                // Handle input validation failure
            }
        }

        // 카테고리 버튼 리스너
        binding.cate1.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate2.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate3.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate4.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate5.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate6.setOnCheckedChangeListener(toggleButtonChangeListener)



        // 태그 추가
        binding.tagAddBtn.setOnClickListener {
            val inputText = binding.tagEt.text.toString().trim()

            if (inputText.isNotEmpty()) {
                val textView = TextView(this)
                textView.text = inputText
                textView.setPadding(20,20,20,20)
                textView.setBackgroundResource(R.drawable.store_create_box_border) // 노란색 태두리 설정
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 텍스트 크기 설정

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dpToPx(40)

                )
                layoutParams.setMargins(0, 0, 20, 0) // TextView 간격 설정
                textView.layoutParams = layoutParams

                binding.tagInputed.addView(textView)
            }
        }


    } // onCreate  종료
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
    // 영업일 정보 토글 버튼 리스너 세팅
    private fun setupToggleButtons() {
        val toggleButtons = listOf(
            binding.tbMon,
            binding.tbTue,
            binding.tbWed,
            binding.tbThu,
            binding.tbFri,
            binding.tbSat,
            binding.tbSun
        )

        toggleButtons.forEach { toggleButton ->
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val day = toggleButton.text.toString()
                if (isChecked) {
                    showTimePicker(day)
                } else {
                    removeOpenInfo(day)
                }
            }
        }
    }

    // 영업 시작, 종료 시간 선택 time picker open
    private fun showTimePicker(day: String) {
        val dialogBinding = OpenInfoTimePickerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Done") { _, _ ->
                val startTimeHour = dialogBinding.timePicker.hour
                val startTimeMinute = dialogBinding.timePicker.minute
                val endTimeHour = dialogBinding.timePicker2.hour
                val endTimeMinute = dialogBinding.timePicker2.minute

                val startTime = String.format("%02d:%02d", startTimeHour, startTimeMinute)
                val endTime = String.format("%02d:%02d", endTimeHour, endTimeMinute)

                if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    val openInfo = OpenInfo(day, startTime, endTime)
                    addOpenInfo(openInfo)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }

        dialog.show()
    }

    // recycler view에 open Info 추가
    private fun addOpenInfo(openInfo: OpenInfo) {
        openInfoList.add(openInfo)
        openInfoAdapter.notifyDataSetChanged()
    }

    // recycler view에 open Info 삭제
    private fun removeOpenInfo(day: String) {
        val removedStores = openInfoList.filter { it.day == day }
        openInfoList.removeAll(removedStores)
        openInfoAdapter.notifyDataSetChanged()
    }






}
