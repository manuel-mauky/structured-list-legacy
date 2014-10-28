package eu.lestard.structuredlist;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.cdi.MvvmfxCdiApplication;
import eu.lestard.structuredlist.model.Item;
import eu.lestard.structuredlist.model.ItemsModel;
import eu.lestard.structuredlist.ui.main.MainView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;

public class App extends MvvmfxCdiApplication{

    public static void main(String...args) {
        launch(args);
    }


    @Inject
    private ItemsModel itemsModel;


    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        Item t1 = new Item("Item 1");
        Item t2 = new Item("Item 2");
        Item t3 = new Item("Item 3");

        itemsModel.getRoot().getSubItems().addAll(t1,t2,t3);


        Item t11 = new Item("Item 1 - 1");
        t1.getSubItems().add(t11);

        Item t12 = new Item("Item 1 - 2");
        t1.getSubItems().add(t12);

        Item t121 = new Item("Item 1 - 2 - 1");
        t12.getSubItems().add(t121);

        final Parent root = FluentViewLoader.fxmlView(MainView.class).load().getView();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
