import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = loadFXML();

        configureAndDisplayStage(primaryStage, root);
    }

    private Parent loadFXML()
    {
        Parent parentNode = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/CoordinateSystemPlanner.fxml"));

        try
        {
            parentNode = fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.err.println("Error occurred while loading FXML: " + e);
        }

        return parentNode;
    }

    private void configureAndDisplayStage(Stage primaryStage, Parent sceneRootNode)
    {
        primaryStage.setTitle("Coordinate System Planner");
        primaryStage.setScene(new Scene(sceneRootNode, 300, 275));
//        primaryStage.setMaxWidth(sceneRootNode.prefWidth(-1));
        primaryStage.setMinWidth(sceneRootNode.prefWidth(-1));
//        primaryStage.setMaxHeight(sceneRootNode.prefHeight(-1));
        primaryStage.setMinHeight(sceneRootNode.prefHeight(-1));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
