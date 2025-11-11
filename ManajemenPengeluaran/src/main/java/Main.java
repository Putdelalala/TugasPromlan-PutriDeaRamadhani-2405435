import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== Starting Aplikasi Manajemen Pengeluaran Harian ===");
            URL fxmlLocation = null;
            String[] paths = {
                "/View/HomeView.fxml",     
                "View/HomeView.fxml",       
                "/HomeView.fxml",           
                "HomeView.fxml"             
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
            
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 500);
            
            primaryStage.setTitle("Manajemen Pengeluaran Harian");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); 
            primaryStage.setMinWidth(620);   
            primaryStage.setMinHeight(520);  
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