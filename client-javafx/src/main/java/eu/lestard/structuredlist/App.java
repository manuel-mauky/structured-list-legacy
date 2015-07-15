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
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class App extends Application {

	private EasyDI context = new EasyDI();

    public static void main(String...args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
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
		
		bootstrap(stage);
    }
	
	private void bootstrap(Stage stage) {
		VBox root = new VBox();


		Label loadingLabel = new Label("Loading Items");
		
		ProgressIndicator indicator = new ProgressIndicator(0.4);
		root.getChildren().addAll(loadingLabel,indicator);
		root.setPadding(new Insets(10));
		root.setSpacing(10);
		
		stage.setScene(new Scene(root));
		stage.show();
		
		
		
		final RootItemFactory rootItemFactory = context.getInstance(RootItemFactory.class);
		rootItemFactory.onProgressChanged((value) ->
				Platform.runLater(() ->
						indicator.setProgress(value)));
		
		
		long start = System.currentTimeMillis();

		final CompletableFuture<Item> itemFuture = CompletableFuture
				.supplyAsync(rootItemFactory::createRootItem);
		
		itemFuture.thenAccept(item -> {
			context.bindInstance(Item.class, item);
			
			long end = System.currentTimeMillis();
	
			System.out.println("Time taken for loading:" + (end - start) + "ms");
			
			Platform.runLater(() -> showApp(stage));
		});
	}
	
	private void showApp(Stage stage) {
		

		final Parent root = ViewLoader.load(MainView.class);

		stage.setTitle("Structure-List");
		stage.setScene(new Scene(root));
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
