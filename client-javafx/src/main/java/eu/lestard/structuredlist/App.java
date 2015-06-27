package eu.lestard.structuredlist;

import eu.lestard.easydi.EasyDI;
import eu.lestard.fluxfx.ViewLoader;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.InMemoryEventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
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
        
        context.bindInterface(EventStore.class, InMemoryEventStore.class);

        ViewLoader.setDependencyInjector(context::getInstance);


        final EventStore eventStore = context.getInstance(EventStore.class);

        eventStore.push(new ItemCreatedEvent(null, "root", "Root"));
        eventStore.push(new ItemCreatedEvent("root", "1", "Eins"));
        eventStore.push(new ItemCreatedEvent("root", "2", "Zwei"));
        eventStore.push(new ItemCreatedEvent("root", "3", "Drei"));
        eventStore.push(new ItemCreatedEvent("1", "1_1", "Eins_Eins"));
        eventStore.push(new ItemCreatedEvent("1", "1_2", "Eins_Zwei"));
        eventStore.push(new ItemCreatedEvent("1", "1_3", "Eins_Drei"));
        eventStore.push(new ItemCreatedEvent("2", "2_1", "Zwei_Eins"));
        eventStore.push(new ItemCreatedEvent("2", "2_2", "Zwei_Zwei"));
        eventStore.push(new ItemCreatedEvent("2", "2_3", "Zwei_Drei"));



        final Parent root = ViewLoader.load(MainView.class);

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
