package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Simple Dictionary");
        primaryStage.setScene(new Scene(root, 750, 500));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Boolean answer = ConfirmBox.display("Exit", "Are You Sure do you want to Exit ?");
            if(answer)
                primaryStage.close();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
