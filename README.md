User App - Technical Test Android Developer

Aplikasi Android untuk kelola data user dengan fitur lihat, cari, urut, filter, dan tambah user. Data bisa diakses offline dan tampilan sudah menyesuaikan ukuran layar HP maupun tablet.


A. Cara Penggunaan Aplikasi

1. Melihat Daftar User
Saat aplikasi dibuka, daftar user langsung muncul. Data diambil dari server lalu disimpan ke lokal jadi kedepannya bisa dibuka tanpa internet. Kalau mau refresh data tinggal tarik layar ke bawah (pull-to-refresh).

2. Mencari User
Di bagian atas ada kolom pencarian. Bisa cari berdasarkan nama, email, atau nomor telepon. Hasil pencarian langsung berubah real-time, ada jeda 300ms biar tidak terlalu berat pas mengetik.

3. Mengurutkan User
Di sebelah kanan header "Semua Pengguna" ada ikon sortir. Tinggal klik untuk ganti urutan A-Z atau Z-A.

4. Memfilter User Berdasarkan Kota
Klik ikon filter di samping tombol sortir, nanti muncul bottom sheet daftar kota. Bisa centang satu atau beberapa kota pakai checkbox. Filter yang aktif muncul sebagai chip di atas daftar, bisa dihapus satu-satu tinggal klik X nya.

5. Menambahkan User Baru
Klik tombol Tambah User di pojok kanan bawah (FAB). Isi form nama, alamat, email, no HP, kota, dan jenis kelamin. Klik Simpan. Email tidak boleh sama dengan yang sudah ada. Kalau berhasil otomatis balik ke daftar user.

6. Mode Offline
Aplikasi tetap bisa dipakai walau tidak ada internet, selama data pernah di-load sebelumnya. Data tersimpan di database lokal (Room). Kalau koneksi internet balik, aplikasi otomatis sync data lagi.


B. Teknologi Yang Digunakan

Kotlin - Bahasa utama.
Jetpack Compose + Material 3 - Buat UI.
Android Hilt - Dependency injection.
ViewModel + StateFlow - Ngatur state dan data reaktif.
Coroutine Flow - Buat aliran data asynchronous.
Retrofit + OkHttp + Moshi - Networking dan konversi JSON.
Room Database - Penyimpanan lokal offline.
WorkManager - Sinkronisasi background setiap 15 menit.
Firebase Analytics - Tracking event.
WindowSizeClass - Adaptive layout.

Arsitekturnya pakai Clean Architecture dengan 3 layer:
Domain layer (model, repository interface, use case).
Data layer (Room, Retrofit, repository impl).
UI layer (Compose screen, ViewModel).
Alur datanya: UI > ViewModel > UseCase > Repository > Room/API. Aplikasi selalu baca dari Room dulu (offline-first), jadi kalau lagi offline data tetap ada.


C. Kenapa Tampilan/Interaksi Seperti Itu

- Header gradasi biru-ungu biar aplikasi keliatan modern dan konsisten warnanya. Setiap user ditampilkan dalam card biar gampang dibaca informasinya. StatCard rangkuman di atas kasih info cepat berapa total user, jumlah kota, dan hasil filter.

- Search bar ditaruh di atas karena itu posisi yang paling familiar buat pengguna. Ada ikon clear biar gampang hapus kata kunci. Pencarian pakai debounce 300ms biar pas ngetik ga terlalu berat ngolah datanya.

- Filter kota pakai checkbox di bottom sheet biar bisa milih lebih dari satu kota, lebih fleksibel daripada radio button yang cuma bisa milih satu. Filter aktif ditampilin sebagai chip yang bisa dihapus satu-satu, jadi pengguna tau lagi filter apa aja yang aktif.

- Untuk layar HP ukuran kecil daftar user ditampilin 1 kolom biar tidak terlalu sempit. Kalau di tablet atau HP layar lebar otomatis jadi 2 kolom biar ruang layar terpakai maksimal.

- Masalah offline, semua data dari API langsung disimpan ke Room. Jadi aplikasi tetap bisa buka data meskipun lagi tidak ada internet. WorkManager jalan setiap 15 menit buat sinkronisasi data di background. Ada juga ConnectivityObserver yang mantau koneksi, pas online lagi setelah offline otomatis refresh data.
