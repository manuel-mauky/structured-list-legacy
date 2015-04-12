package eu.lestard.structuredlist;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import eu.lestard.easydi.EasyDI;
import eu.lestard.structuredlist.ui.main.MainView;
import javafx.application.Application;
import javafx.application.Platform;
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
        context.bindProvider(NotificationCenter.class, MvvmFX::getNotificationCenter);

        MvvmFX.setCustomDependencyInjector(context::getInstance);

        final NotificationCenter notificationCenter = context.getInstance(NotificationCenter.class);
        notificationCenter.subscribe("exit", (key, objects) -> Platform.exit());

        final Parent root = FluentViewLoader.fxmlView(MainView.class).load().getView();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
