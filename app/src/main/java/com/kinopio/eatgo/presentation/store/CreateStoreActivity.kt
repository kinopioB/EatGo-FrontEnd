package com.kinopio.eatgo.presentation.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding
import com.kinopio.eatgo.databinding.OpenInfoTimePickerBinding
import com.kinopio.eatgo.domain.store.ui_model.Menu
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo


class CreateStoreActivity : AppCompatActivity() {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var  openInfoAdapter :  OpenInfoAdapter

    private val menuList = mutableListOf<Menu>()
    private val openInfoList = mutableListOf<OpenInfo>()

    private lateinit var binding: ActivityCreateStoreBinding

    private var selectedImageUri: Uri? = null

    private var selectedToggleButton: ToggleButton? = null


    private val PICK_IMAGE_REQUEST_CODE = 1

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



    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToggleButtons()
        menuAdapter = MenuAdapter(menuList)
        openInfoAdapter = OpenInfoAdapter(openInfoList)

        binding.mRecyclerView.apply {
            adapter = menuAdapter
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

        // 사진 추가 -> 갤러리 연동
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Handle the selected image URI here
                if (data != null) {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        selectedImageUri = imageUri
                        binding.menuImg.setImageURI(selectedImageUri)
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

        // 메뉴 추가
        binding.buttonAdd.setOnClickListener {
            val menuName = binding.editTextMenuName.text.toString()
            val menuCount = binding.editTextMenuCount.text.toString().toIntOrNull()
            val menuPrice = binding.editTextMenuPrice.text.toString().toIntOrNull()
            if (menuName.isNotEmpty() && menuCount != null && menuPrice != null) {
                val menu = selectedImageUri?.let { it1 ->
                    Menu(menuName, menuCount, menuPrice,
                        it1
                    )
                }
                menu?.let { it1 -> menuList.add(it1) }
                menuAdapter.notifyDataSetChanged()

                // Reset input fields
                binding.editTextMenuName.text.clear()
                binding.editTextMenuCount.text.clear()
                binding.editTextMenuPrice.text.clear()

                // Hide the input layout
                binding.menuInputLayout.visibility = View.GONE

                binding.menuImg.setImageURI(null)
                selectedImageUri = null
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
            .setTitle("Set Opening and Closing Time")
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
