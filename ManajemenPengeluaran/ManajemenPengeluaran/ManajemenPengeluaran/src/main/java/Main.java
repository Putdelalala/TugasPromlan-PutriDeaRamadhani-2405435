import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Main Application Class untuk Aplikasi Manajemen Pengeluaran Harian
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== Starting Aplikasi Manajemen Pengeluaran Harian ===");
            
            // Coba beberapa path alternatif untuk FXML
            URL fxmlLocation = null;
            String[] paths = {
                "/View/HomeView.fxml",      // Path 1 (standard resources)
                "View/HomeView.fxml",       // Path 2 (tanpa leading slash)
                "/HomeView.fxml",           // Path 3 (root resources)
                "HomeView.fxml"             // Path 4 (current directory)
            };
            
            for (String path : paths) {
                fxmlLocation = getClass().getResource(path);
                if (fxmlLocation != null) {
                    System.out.println("✓ FXML ditemukan di: " + path);
                    break;
                } else {
                    System.out.println("✗ FXML tidak ditemukan di: " + path);
                }
            }
            
            if (fxmlLocation == null) {
                System.err.println("\n=== ERROR: File HomeView.fxml tidak ditemukan! ===");
                System.err.println("Pastikan file berada di salah satu lokasi:");
                System.err.println("  1. src/main/resources/View/HomeView.fxml");
                System.err.println("  2. src/main/resources/HomeView.fxml");
                return;
            }
            
            // Load FXML
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            
            // Setup Scene dengan ukuran sesuai FXML
            Scene scene = new Scene(root, 600, 500);
            
            // Setup Stage
            primaryStage.setTitle("Manajemen Pengeluaran Harian");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Bisa di-resize
            primaryStage.setMinWidth(620);   // Minimum width
            primaryStage.setMinHeight(520);  // Minimum height
            primaryStage.show();
            
            System.out.println("✓ Aplikasi berhasil dijalankan!");
            
        } catch (Exception e) {
            System.err.println("\n=== ERROR saat load aplikasi ===");
            e.printStackTrace();
            System.err.println("\nDetail error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}