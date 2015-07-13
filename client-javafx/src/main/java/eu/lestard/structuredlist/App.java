package eu.lestard.structuredlist;

import eu.lestard.easydi.EasyDI;
import eu.lestard.fluxfx.ViewLoader;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.InMemoryEventStore;
import eu.lestard.structuredlist.eventsourcing.JsonEventStore;
import eu.lestard.structuredlist.stores.SystemStore;
import eu.lestard.structuredlist.stores.items.Item;
import eu.lestard.structuredlist.stores.items.RootItemFactory;
import eu.lestard.structuredlist.ui.main.MainView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class App extends Application {

    public static void main(String...args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        EasyDI context = new EasyDI();
		context.getInstance(SystemStore.class); // instantiate SystemStore

        final Optional<File> file = getStorageFile();

        if(file.isPresent()) {

            JsonEventStore eventStore = new JsonEventStore(file.get());

            context.bindInstance(EventStore.class, eventStore);
        } else {
            System.err.println("Can't open storage file :-( - Use in-memory storage instead. All data will be lost after application shutdown");

            context.bindInterface(EventStore.class, InMemoryEventStore.class);
        }



        ViewLoader.setDependencyInjector(context::getInstance);

        final RootItemFactory rootItemFactory = context.getInstance(RootItemFactory.class);

        final Item rootItem = rootItemFactory.createRootItem();

        context.bindInstance(Item.class, rootItem);


        final Parent root = ViewLoader.load(MainView.class);

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }



    private Optional<File> getStorageFile() {
        final String userhome = System.getProperty("user.home");

        final Path appDirPath = Paths.get(userhome, ".structure-list");

        final File appDirFile = appDirPath.toFile();

        if(!appDirFile.exists()) {
            final boolean mkDirsSuccessful = appDirFile.mkdirs();

            if(!mkDirsSuccessful) {
                System.err.println("can't create app directory under \"" + appDirPath.toAbsolutePath() + "\"");
                return Optional.empty();
            }
        }

        final Path storagePath = appDirPath.resolve("storage.json");

        final File storageFile = storagePath.toFile();

        if(!storageFile.exists()) {

            try {
                boolean createFileSuccessful = storageFile.createNewFile();

                if(!createFileSuccessful) {
                    System.err.println("Can't create storage file under \"" + storagePath.toAbsolutePath() + "\"");
                }
            } catch (IOException e) {
                System.err.println("Can't create storage file under \"" + storagePath.toAbsolutePath() + "\"");
                e.printStackTrace();
            }
        }

        return Optional.of(storageFile);
    }
}
