package org.brainless.telyucreative.views.mainscreen.save

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider
import org.brainless.telyucreative.databinding.FragmentSaveBinding
import org.brainless.telyucreative.utils.BaseFragment
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity


class SaveFragment : BaseFragment() {

    private lateinit var binding : FragmentSaveBinding
    private lateinit var saveAdapter: SaveAdapter
    private var arrayOfSave = arrayListOf<Favorite>()

    private val viewModel: SaveViewModel by lazy {
        ViewModelProvider(this)[SaveViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkFavorite()
        successFavoriteList()
        observeData()
    }

    @SuppressLint("Recycle")
    fun successFavoriteList() {
        saveAdapter = SaveAdapter(arrayOfSave){

            saveAdapter.setOnClickListener( object : SaveAdapter.OnClickListener{
                override fun onClick(position: Int, favorite: Favorite) {
                    val intent = Intent(requireContext(), CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, favorite.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, favorite.userId)
                    startActivity(intent)
                }

                override fun onDelete(position: Int, favorite: Favorite) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Konfirmasi penghapusan")
                        .setMessage("Yakin ingin menghapus karya ini dari favorit ?")
                        .setNegativeButton("Batal") { _, _ ->
                        }
                        .setPositiveButton("Hapus") { _, _ ->
                            FirestoreProvider().removeItemFromFavorite(
                                this@SaveFragment,
                                favorite.favoriteId
                            )
                            showProgressDialog(resources.getString(R.string.please_wait))
                        }.show()


                }
            })

        }
        with(binding.rvFavoriteList){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = saveAdapter
        }
    }

    private fun observeData(){
        viewModel.initData().observe(viewLifecycleOwner) {
            saveAdapter.setListData(it)
        }
    }

    private fun checkFavorite() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreProvider().checkEmptyFavorite(this@SaveFragment)
    }

    fun ifFavoriteListIsEmpty() {
        hideProgressDialog()
        binding.rvFavoriteList.visibility = View.VISIBLE
        binding.frameEmptyFavorit.visibility = View.GONE
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()
        observeData()
    }

}