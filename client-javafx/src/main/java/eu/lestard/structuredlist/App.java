package eu.lestard.structuredlist;

import eu.lestard.easydi.EasyDI;
import eu.lestard.fluxfx.ViewLoader;
import eu.lestard.structuredlist.ui.main.MainView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String...args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        EasyDI context = new EasyDI();

        ViewLoader.setDependencyInjector(context::getInstance);

        final Parent root = ViewLoader.load(MainView.class);

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
