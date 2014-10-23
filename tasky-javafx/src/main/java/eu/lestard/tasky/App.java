package eu.lestard.tasky;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.cdi.MvvmfxCdiApplication;
import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.model.TasksModel;
import eu.lestard.tasky.ui.main.MainView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;

public class App extends MvvmfxCdiApplication{

    public static void main(String...args) {
        launch(args);
    }


    @Inject
    private TasksModel tasksModel;


    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        Task t1 = new Task("Task 1");
        Task t2 = new Task("Task 2");
        Task t3 = new Task("Task 3");
        Task t11 = new Task("Task 1 - 1");
        Task t12 = new Task("Task 1 - 2");
        Task t121 = new Task("Task 1 - 2 - 1");

        tasksModel.addTasks(t1, t2, t3, t11, t12, t121);


        final Parent root = FluentViewLoader.fxmlView(MainView.class).load().getView();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}
