package com.pcs.apptoko

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.pcs.apptoko.api.BaseRetrofit
import com.pcs.apptoko.response.produk.Produk
import com.pcs.apptoko.response.produk.ProdukResponsePost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuplierFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuplierFormFragment : Fragment() {
    private val api by lazy { BaseRetrofit().endPoint}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_suplier_form, container, false)

        val btnProsesProduk = view.findViewById<Button>(R.id.btnProsesProduk)

        val txtFormNama = view.findViewById<TextView>(R.id.txtFormNama)
        val txtFormHarga = view.findViewById<TextView>(R.id.txtFormHarga)
        val txtFormStok = view.findViewById<TextView>(R.id.txtFormStok)

        val status = arguments?.getString("status")
        val produk = arguments?.getParcelable<Produk>("produk")

        Log.d("produkForm", produk.toString())

        if(status=="edit"){
            txtFormNama.setText(produk?.nama.toString())
            txtFormHarga.setText(produk?.harga.toString())
            txtFormStok.setText(produk?.stok.toString())
        }

        btnProsesProduk.setOnClickListener {
            val txtFormNama = view.findViewById<TextInputEditText>(R.id.txtFormNama)
            val txtFormHarga = view.findViewById<TextInputEditText>(R.id.txtFormHarga)
            val txtFormStok2 = view.findViewById<TextInputEditText>(R.id.txtFormStok)
            val txtFormStok1 = view.findViewById<TextInputEditText>(R.id.txtFormStok1)


            val token = LoginActivity.sessionManager.getString("TOKEN")
            val adminId = LoginActivity.sessionManager.getString("ADMIN_ID")

            val currentStock = txtFormStok2.text.toString().toInt()
            val addedStock = txtFormStok1.text.toString().toInt()
            val newStock = currentStock + addedStock

            if(status=="edit"){
                api.putProduk(token.toString(),produk?.id.toString().toInt(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),newStock.toString().toInt()).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>,
                    ) {
                        Log.d("ResponData", response.body()!!.data.toString())
                        Toast.makeText(activity?.applicationContext, "Stok "+ response.body()!!.data.produk.nama.toString() +" di Tambahkan",
                            Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.produkFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }else{
                api.postProduk(token.toString(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>,
                    ) {
                        Log.d("Data", response.toString())
                        Toast.makeText(activity?.applicationContext, "Data di input", Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.suplierFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }

        }

        return view
    }
}