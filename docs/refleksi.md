Refleksi Pengembangan TaskManager

# 1. Keputusan Desain

Dalam pengembangan aplikasi TaskManager, saya menggunakan Kotlin dan Jetpack Compose untuk membuat aplikasi Android. Saya membagi kode ke dalam beberapa package, yaitu data untuk data tugas, ui untuk tampilan aplikasi, dan util untuk fungsi bantuan seperti pengolahan tanggal.

Saya menggunakan enum untuk status dan prioritas tugas agar pilihan nilainya lebih teratur. Deadline disimpan dalam tipe Long agar data tanggal dapat diurutkan dengan mudah. Saya juga menggunakan Room Database agar data tugas dapat disimpan secara lokal di perangkat.

# 2. Kendala

Selama pengerjaan, saya mengalami beberapa kendala. Pertama, terdapat error pada konfigurasi KSP dan Kotlin saat proses build. Saya mencoba menyesuaikan konfigurasi Gradle dan mencari solusi yang sesuai dengan versi Kotlin yang digunakan.

Kedua, saya mengalami error pada file TaskListScreen.kt, seperti package yang tidak sesuai dengan lokasi file, referensi ikon yang belum dikenali, dan tanda koma yang terlewat pada parameter fungsi. Saya memperbaikinya dengan menyesuaikan deklarasi package, memeriksa import, dan memperbaiki sintaks kode.

Dari kendala tersebut, saya belajar bahwa penulisan kode harus teliti dan konfigurasi library perlu diperhatikan agar sesuai dengan proyek yang digunakan.

# 3. Perbaikan Berikutnya

Ke depannya, saya ingin menambahkan fitur notifikasi pengingat deadline agar pengguna mendapatkan pengingat sebelum batas waktu pengumpulan tugas. Saya juga ingin meningkatkan pengujian aplikasi untuk memastikan fitur-fitur yang tersedia berjalan dengan baik serta memperbaiki tampilan aplikasi agar lebih nyaman digunakan.