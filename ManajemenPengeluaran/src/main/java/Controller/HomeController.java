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

public class HomeController implements Initializable {

   
    @FXML
    private TextField txtKeterangan;

    @FXML
    private TextField txtJumlah;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private ComboBox<String> cbKategori;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnClear;

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

    private ObservableList<Pengeluaran> pengeluaranList = FXCollections.observableArrayList();

    private Pengeluaran selectedPengeluaran = null;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
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

        tableView.setItems(pengeluaranList);
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

        dpTanggal.setValue(LocalDate.now());
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedPengeluaran = newValue;
                        populateFields(newValue);
                    }
                }
        );

        addDummyData();

        System.out.println("✓ Controller initialized successfully!");
    }
    
    private void addDummyData() {
        pengeluaranList.add(new Pengeluaran("Makan siang", 25000, LocalDate.now(), "Makanan & Minuman"));
        pengeluaranList.add(new Pengeluaran("Bensin motor", 20000, LocalDate.now().minusDays(1), "Transport"));
        pengeluaranList.add(new Pengeluaran("Bayar listrik", 150000, LocalDate.now().minusDays(2), "Tagihan"));
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            String keterangan = txtKeterangan.getText().trim();
            double jumlah = Double.parseDouble(txtJumlah.getText().trim());
            LocalDate tanggal = dpTanggal.getValue();
            String kategori = cbKategori.getValue();
            Pengeluaran pengeluaranBaru = new Pengeluaran(keterangan, jumlah, tanggal, kategori);
            pengeluaranList.add(pengeluaranBaru);
            clearForm();
            showAlert("Sukses", "Data pengeluaran berhasil ditambahkan!", Alert.AlertType.INFORMATION);
            tableView.refresh();

            System.out.println("✓ Data ditambahkan: " + pengeluaranBaru);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedPengeluaran == null) {
            showAlert("Warning", "Pilih data yang ingin diupdate terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Update");
            confirmAlert.setHeaderText("Update Data Pengeluaran");
            confirmAlert.setContentText("Apakah Anda yakin ingin mengubah data ini?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedPengeluaran.setKeterangan(txtKeterangan.getText().trim());
                selectedPengeluaran.setJumlah(Double.parseDouble(txtJumlah.getText().trim()));
                selectedPengeluaran.setTanggal(dpTanggal.getValue());
                selectedPengeluaran.setKategori(cbKategori.getValue());
                tableView.refresh();
                clearForm();
                showAlert("Sukses", "Data pengeluaran berhasil diupdate!", Alert.AlertType.INFORMATION);

                System.out.println("✓ Data diupdate: " + selectedPengeluaran);
            }
        }
    }
    @FXML
    private void handleDelete() {
        if (selectedPengeluaran == null) {
            showAlert("Warning", "Pilih data yang ingin dihapus terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

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

    @FXML
    private void handleClear() {
        clearForm();
        System.out.println("✓ Form dibersihkan");
    }

    private void clearForm() {
        txtKeterangan.clear();
        txtJumlah.clear();
        dpTanggal.setValue(LocalDate.now());
        cbKategori.getSelectionModel().clearSelection();
        tableView.getSelectionModel().clearSelection();
        selectedPengeluaran = null;
    }

    private void populateFields(Pengeluaran pengeluaran) {
        txtKeterangan.setText(pengeluaran.getKeterangan());
        txtJumlah.setText(String.valueOf(pengeluaran.getJumlah()));
        dpTanggal.setValue(pengeluaran.getTanggal());
        cbKategori.setValue(pengeluaran.getKategori());
    }

    private boolean validateInput() {
        if (txtKeterangan.getText().trim().isEmpty()) {
            showAlert("Error", "Keterangan tidak boleh kosong!", Alert.AlertType.ERROR);
            txtKeterangan.requestFocus();
            return false;
        }

        if (txtJumlah.getText().trim().isEmpty()) {
            showAlert("Error", "Jumlah tidak boleh kosong!", Alert.AlertType.ERROR);
            txtJumlah.requestFocus();
            return false;
        }
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

        if (dpTanggal.getValue() == null) {
            showAlert("Error", "Pilih tanggal terlebih dahulu!", Alert.AlertType.ERROR);
            dpTanggal.requestFocus();
            return false;
        }

        if (dpTanggal.getValue().isAfter(LocalDate.now())) {
            showAlert("Error", "Tanggal tidak boleh di masa depan!", Alert.AlertType.ERROR);
            dpTanggal.requestFocus();
            return false;
        }

        if (cbKategori.getValue() == null || cbKategori.getValue().isEmpty()) {
            showAlert("Error", "Pilih kategori terlebih dahulu!", Alert.AlertType.ERROR);
            cbKategori.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public double getTotalPengeluaran() {
        return pengeluaranList.stream()
                .mapToDouble(Pengeluaran::getJumlah)
                .sum();
    }

    public int getJumlahTransaksi() {
        return pengeluaranList.size();
    }
}