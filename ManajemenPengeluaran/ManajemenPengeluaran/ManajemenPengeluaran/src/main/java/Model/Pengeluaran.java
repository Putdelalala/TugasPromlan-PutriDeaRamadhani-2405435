package Model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Model class untuk data Pengeluaran Harian
 * Menggunakan JavaFX Properties untuk binding dengan TableView
 */
public class Pengeluaran {
    private final StringProperty keterangan;
    private final DoubleProperty jumlah;
    private final ObjectProperty<LocalDate> tanggal;
    private final StringProperty kategori;

    // Constructor
    public Pengeluaran(String keterangan, double jumlah, LocalDate tanggal, String kategori) {
        this.keterangan = new SimpleStringProperty(keterangan);
        this.jumlah = new SimpleDoubleProperty(jumlah);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.kategori = new SimpleStringProperty(kategori);
    }

    // Getter dan Setter untuk Keterangan
    public String getKeterangan() {
        return keterangan.get();
    }

    public StringProperty keteranganProperty() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan.set(keterangan);
    }

    // Getter dan Setter untuk Jumlah
    public double getJumlah() {
        return jumlah.get();
    }

    public DoubleProperty jumlahProperty() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah.set(jumlah);
    }

    // Getter dan Setter untuk Tanggal
    public LocalDate getTanggal() {
        return tanggal.get();
    }

    public ObjectProperty<LocalDate> tanggalProperty() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal.set(tanggal);
    }

    // Getter dan Setter untuk Kategori
    public String getKategori() {
        return kategori.get();
    }

    public StringProperty kategoriProperty() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori.set(kategori);
    }

    @Override
    public String toString() {
        return "Pengeluaran{" +
                "keterangan='" + keterangan.get() + '\'' +
                ", jumlah=" + jumlah.get() +
                ", tanggal=" + tanggal.get() +
                ", kategori='" + kategori.get() + '\'' +
                '}';
    }
}