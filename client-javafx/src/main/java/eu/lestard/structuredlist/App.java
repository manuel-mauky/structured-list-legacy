package eu.lestard.structuredlist;

import eu.lestard.easydi.EasyDI;
import eu.lestard.fluxfx.ViewLoader;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.JsonEventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
import eu.lestard.structuredlist.stores.items.Item;
import eu.lestard.structuredlist.stores.items.RootItemFactory;
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
        
        context.bindInterface(EventStore.class, JsonEventStore.class);

        ViewLoader.setDependencyInjector(context::getInstance);


        final EventStore eventStore = context.getInstance(EventStore.class);

        if(false) {

        eventStore.push(new ItemCreatedEvent(RootItemFactory.ROOT_ID, "1", "Eins"));
        eventStore.push(new ItemCreatedEvent(RootItemFactory.ROOT_ID, "2", "Zwei"));
        eventStore.push(new ItemCreatedEvent(RootItemFactory.ROOT_ID, "3", "Drei"));
        eventStore.push(new ItemCreatedEvent("1", "1_1", "Eins_Eins"));
        eventStore.push(new ItemCreatedEvent("1", "1_2", "Eins_Zwei"));
        eventStore.push(new ItemCreatedEvent("1", "1_3", "Eins_Drei"));
        eventStore.push(new ItemCreatedEvent("2", "2_1", "Zwei_Eins"));
        eventStore.push(new ItemCreatedEvent("2", "2_2", "Zwei_Zwei"));
        eventStore.push(new ItemCreatedEvent("2", "2_3", "Zwei_Drei"));
        }

        final RootItemFactory rootItemFactory = context.getInstance(RootItemFactory.class);

        final Item rootItem = rootItemFactory.createRootItem();

        context.bindInstance(Item.class, rootItem);


        final Parent root = ViewLoader.load(MainView.class);

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
