# Rancangan Aplikasi Manajemen Tugas Mahasiswa

## User Flow
- Daftar Tugas → Tambah Tugas → Simpan → Daftar Tugas
- Daftar Tugas → Detail Tugas → Edit / Hapus → Daftar Tugas

## Layar
1. Daftar Tugas (pencarian + filter status)
2. Tambah / Edit Tugas (satu form dipakai untuk keduanya)
3. Detail Tugas (ubah status, edit, hapus dengan konfirmasi)

## Model Data (Task)
id, mataKuliah, judul, deskripsi, deadline, prioritas, status

## Arsitektur
Compose UI -> ViewModel -> Repository -> Room DAO -> Database