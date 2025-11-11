package Controller;

import Model.Pengeluaran;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller untuk mengelola CRUD Pengeluaran Harian
 */
public class HomeController implements Initializable {

    // FXML Components - Input Fields
    @FXML
    private TextField txtKeterangan;

    @FXML
    private TextField txtJumlah;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private ComboBox<String> cbKategori;

    // FXML Components - Buttons
    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnClear;

    // FXML Components - TableView
    @FXML
    private TableView<Pengeluaran> tableView;

    @FXML
    private TableColumn<Pengeluaran, String> colKeterangan;

    @FXML
    private TableColumn<Pengeluaran, Double> colJumlah;

    @FXML
    private TableColumn<Pengeluaran, LocalDate> colTanggal;

    @FXML
    private TableColumn<Pengeluaran, String> colKategori;

    // ObservableList untuk menyimpan data pengeluaran
    private ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();

    // Variable untuk menyimpan pengeluaran yang sedang dipilih
    private Pengeluaran selectedPengeluaran = null;

    // Formatter untuk currency Indonesia
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup TableView columns
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        // Format kolom Jumlah menjadi currency
        colJumlah.setCellFactory(column -> new TableCell<Pengeluaran, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Format kolom Tanggal
        colTanggal.setCellFactory(column -> new TableCell<Pengeluaran, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Set data ke TableView
        tableView.setItems(pengeluaranList);

        // Setup ComboBox kategori
        cbKategori.setItems(FXCollections.observableArrayList(
                "Makanan & Minuman",
                "Transport",
                "Belanja",
                "Hiburan",
                "Kesehatan",
                "Pendidikan",
                "Tagihan",
                "Lain-lain"
        ));

        // Set default tanggal ke hari ini
        dpTanggal.setValue(LocalDate.now());

        // Tambahkan listener untuk selection di TableView
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedPengeluaran = newValue;
                        populateFields(newValue);
                    }
                }
        );

        // Tambahkan data dummy untuk testing
        addDummyData();

        System.out.println("✓ Controller initialized successfully!");
    }

    /**
     * Menambahkan data dummy untuk testing
     */
    private void addDummyData() {
        pengeluaranList.add(new Pengeluaran("Makan siang", 25000, LocalDate.now(), "Makanan & Minuman"));
        pengeluaranList.add(new Pengeluaran("Bensin motor", 20000, LocalDate.now().minusDays(1), "Transport"));
        pengeluaranList.add(new Pengeluaran("Bayar listrik", 150000, LocalDate.now().minusDays(2), "Tagihan"));
    }

    /**
     * Handler untuk tombol Tambah
     */
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            String keterangan = txtKeterangan.getText().trim();
            double jumlah = Double.parseDouble(txtJumlah.getText().trim());
            LocalDate tanggal = dpTanggal.getValue();
            String kategori = cbKategori.getValue();

            // Buat object Pengeluaran baru
            Pengeluaran pengeluaranBaru = new Pengeluaran(keterangan, jumlah, tanggal, kategori);

            // Tambahkan ke ObservableList
            pengeluaranList.add(pengeluaranBaru);

            // Clear form dan refresh
            clearForm();
            showAlert("Sukses", "Data pengeluaran berhasil ditambahkan!", Alert.AlertType.INFORMATION);
            tableView.refresh();

            System.out.println("✓ Data ditambahkan: " + pengeluaranBaru);
        }
    }

    /**
     * Handler untuk tombol Update
     */
    @FXML
    private void handleUpdate() {
        if (selectedPengeluaran == null) {
            showAlert("Warning", "Pilih data yang ingin diupdate terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            // Konfirmasi update
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Update");
            confirmAlert.setHeaderText("Update Data Pengeluaran");
            confirmAlert.setContentText("Apakah Anda yakin ingin mengubah data ini?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Update data pengeluaran yang dipilih
                selectedPengeluaran.setKeterangan(txtKeterangan.getText().trim());
                selectedPengeluaran.setJumlah(Double.parseDouble(txtJumlah.getText().trim()));
                selectedPengeluaran.setTanggal(dpTanggal.getValue());
                selectedPengeluaran.setKategori(cbKategori.getValue());

                // Refresh TableView
                tableView.refresh();
                clearForm();
                showAlert("Sukses", "Data pengeluaran berhasil diupdate!", Alert.AlertType.INFORMATION);

                System.out.println("✓ Data diupdate: " + selectedPengeluaran);
            }
        }
    }

    /**
     * Handler untuk tombol Delete
     */
    @FXML
    private void handleDelete() {
        if (selectedPengeluaran == null) {
            showAlert("Warning", "Pilih data yang ingin dihapus terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        // Konfirmasi delete
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Hapus Data Pengeluaran");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus data: " 
                + selectedPengeluaran.getKeterangan() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String keterangan = selectedPengeluaran.getKeterangan();
            pengeluaranList.remove(selectedPengeluaran);
            clearForm();
            showAlert("Sukses", "Data berhasil dihapus: " + keterangan, Alert.AlertType.INFORMATION);
            tableView.refresh();

            System.out.println("✓ Data dihapus: " + keterangan);
        }
    }

    /**
     * Handler untuk tombol Clear
     */
    @FXML
    private void handleClear() {
        clearForm();
        System.out.println("✓ Form dibersihkan");
    }

    /**
     * Membersihkan form input
     */
    private void clearForm() {
        txtKeterangan.clear();
        txtJumlah.clear();
        dpTanggal.setValue(LocalDate.now());
        cbKategori.getSelectionModel().clearSelection();
        tableView.getSelectionModel().clearSelection();
        selectedPengeluaran = null;
    }

    /**
     * Mengisi form dengan data pengeluaran yang dipilih
     */
    private void populateFields(Pengeluaran pengeluaran) {
        txtKeterangan.setText(pengeluaran.getKeterangan());
        txtJumlah.setText(String.valueOf(pengeluaran.getJumlah()));
        dpTanggal.setValue(pengeluaran.getTanggal());
        cbKategori.setValue(pengeluaran.getKategori());
    }

    /**
     * Validasi input form
     */
    private boolean validateInput() {
        // Validasi Keterangan
        if (txtKeterangan.getText().trim().isEmpty()) {
            showAlert("Error", "Keterangan tidak boleh kosong!", Alert.AlertType.ERROR);
            txtKeterangan.requestFocus();
            return false;
        }

        // Validasi Jumlah
        if (txtJumlah.getText().trim().isEmpty()) {
            showAlert("Error", "Jumlah tidak boleh kosong!", Alert.AlertType.ERROR);
            txtJumlah.requestFocus();
            return false;
        }

        // Validasi format angka
        try {
            double jumlah = Double.parseDouble(txtJumlah.getText().trim());
            if (jumlah <= 0) {
                showAlert("Error", "Jumlah harus lebih dari 0!", Alert.AlertType.ERROR);
                txtJumlah.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Jumlah harus berupa angka yang valid!\nContoh: 25000", Alert.AlertType.ERROR);
            txtJumlah.requestFocus();
            return false;
        }

        // Validasi Tanggal
        if (dpTanggal.getValue() == null) {
            showAlert("Error", "Pilih tanggal terlebih dahulu!", Alert.AlertType.ERROR);
            dpTanggal.requestFocus();
            return false;
        }

        // Validasi tanggal tidak boleh di masa depan
        if (dpTanggal.getValue().isAfter(LocalDate.now())) {
            showAlert("Error", "Tanggal tidak boleh di masa depan!", Alert.AlertType.ERROR);
            dpTanggal.requestFocus();
            return false;
        }

        // Validasi Kategori
        if (cbKategori.getValue() == null || cbKategori.getValue().isEmpty()) {
            showAlert("Error", "Pilih kategori terlebih dahulu!", Alert.AlertType.ERROR);
            cbKategori.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Menampilkan alert dialog
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method untuk mendapatkan total pengeluaran (bisa dipanggil dari luar)
     */
    public double getTotalPengeluaran() {
        return pengeluaranList.stream()
                .mapToDouble(Pengeluaran::getJumlah)
                .sum();
    }

    /**
     * Method untuk mendapatkan jumlah transaksi
     */
    public int getJumlahTransaksi() {
        return pengeluaranList.size();
    }
}