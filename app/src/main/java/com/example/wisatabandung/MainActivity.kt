package com.example.wisatabandung

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var tvNamaAkun: TextView
    private lateinit var tvEmailAkun: TextView

    private lateinit var btnSemua: TextView
    private lateinit var btnAlam: TextView
    private lateinit var btnBudaya: TextView
    private lateinit var btnKuliner: TextView

    private lateinit var daftarKartu: List<LinearLayout>

    private var kategoriAktif = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil data akun dari sesi login
        val session = getSharedPreferences("LoginSession", MODE_PRIVATE)

        tvNamaAkun = findViewById(R.id.tvNamaAkun)
        tvEmailAkun = findViewById(R.id.tvEmailAkun)

        tvNamaAkun.text = session.getString("nama", "Admin")
        tvEmailAkun.text = session.getString("email", "admin@gmail.com")

        // Hubungkan komponen
        etSearch = findViewById(R.id.etSearch)

        btnSemua = findViewById(R.id.btnSemua)
        btnAlam = findViewById(R.id.btnAlam)
        btnBudaya = findViewById(R.id.btnBudaya)
        btnKuliner = findViewById(R.id.btnKuliner)

        // Daftar kartu wisata
        daftarKartu = listOf(
            findViewById(R.id.cardBraga),
            findViewById(R.id.cardKawah),
            findViewById(R.id.cardLembang),
            findViewById(R.id.cardDusun),
            findViewById(R.id.cardDago),
            findViewById(R.id.cardAngklung)
        )

        // Tombol kategori
        btnSemua.setOnClickListener {
            pilihKategori("Semua", btnSemua)
        }

        btnAlam.setOnClickListener {
            pilihKategori("Alam", btnAlam)
        }

        btnBudaya.setOnClickListener {
            pilihKategori("Budaya", btnBudaya)
        }

        btnKuliner.setOnClickListener {
            pilihKategori("Kuliner", btnKuliner)
        }

        // Fungsi pencarian
        etSearch.addTextChangedListener {
            filterWisata()
        }

        // Klik kartu wisata
        daftarKartu.forEach { kartu ->
            kartu.setOnClickListener {
                val namaWisata = kartu.contentDescription.toString()

                Toast.makeText(
                    this,
                    "Kamu memilih $namaWisata",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Navigasi bawah
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            etSearch.setText("")
            pilihKategori("Semua", btnSemua)
            Toast.makeText(this, "Halaman Home", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.navCari).setOnClickListener {
            etSearch.requestFocus()

            val keyboard = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            keyboard.showSoftInput(
                etSearch,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        findViewById<TextView>(R.id.navFavorit).setOnClickListener {
            Toast.makeText(
                this,
                "Fitur Favorit akan dibuat selanjutnya",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Tombol profil di bagian atas dan navigasi bawah
        findViewById<TextView>(R.id.btnProfil).setOnClickListener {
            tampilkanProfil()
        }

        findViewById<TextView>(R.id.navProfil).setOnClickListener {
            tampilkanProfil()
        }
    }

    // Memilih kategori wisata
    private fun pilihKategori(kategori: String, tombolAktif: TextView) {

        kategoriAktif = kategori

        val semuaTombol = listOf(
            btnSemua,
            btnAlam,
            btnBudaya,
            btnKuliner
        )

        semuaTombol.forEach { tombol ->
            tombol.setBackgroundResource(R.drawable.bg_chip)
            tombol.setTextColor(android.graphics.Color.DKGRAY)
        }

        tombolAktif.setBackgroundResource(R.drawable.bg_chip_active)
        tombolAktif.setTextColor(android.graphics.Color.WHITE)

        filterWisata()
    }

    // Filter kategori dan pencarian
    private fun filterWisata() {

        val kataKunci = etSearch.text.toString().trim()

        daftarKartu.forEach { kartu ->

            val nama = kartu.contentDescription.toString()
            val kategori = kartu.tag.toString()

            val cocokKategori =
                kategoriAktif == "Semua" || kategori == kategoriAktif

            val cocokPencarian =
                nama.contains(kataKunci, ignoreCase = true)

            kartu.visibility =
                if (cocokKategori && cocokPencarian) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    // Menampilkan informasi akun dan pilihan logout
    private fun tampilkanProfil() {

        val session = getSharedPreferences("LoginSession", MODE_PRIVATE)

        val nama = session.getString("nama", "Admin")
        val email = session.getString("email", "admin@gmail.com")

        AlertDialog.Builder(this)
            .setTitle("Profil Akun")
            .setMessage(
                "Nama: $nama\nEmail: $email"
            )
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    // Keluar dari akun
    private fun logout() {

        val session = getSharedPreferences("LoginSession", MODE_PRIVATE)

        session.edit()
            .clear()
            .apply()

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
    }
}