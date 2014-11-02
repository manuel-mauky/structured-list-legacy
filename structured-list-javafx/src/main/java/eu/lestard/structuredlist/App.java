package eu.lestard.structuredlist;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.cdi.MvvmfxCdiApplication;
import eu.lestard.structuredlist.ui.main.MainView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends MvvmfxCdiApplication{

    public static void main(String...args) {
        launch(args);
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        final Parent root = FluentViewLoader.fxmlView(MainView.class).load().getView();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
