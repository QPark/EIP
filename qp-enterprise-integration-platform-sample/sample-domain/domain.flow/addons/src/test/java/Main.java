import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	Task copyWorker;

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Background Processes");
		Group root = new Group();
		Scene scene = new Scene(root, 330, 120, Color.WHITE);

		BorderPane mainPane = new BorderPane();
		root.getChildren().add(mainPane);

		final Label label = new Label("Files Transfer:");
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);

		final HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(label, progressIndicator);
		mainPane.setTop(hb);

		final Button startButton = new Button("Start");
		final Button cancelButton = new Button("Cancel");
		final HBox hb2 = new HBox();
		hb2.setSpacing(5);
		hb2.setAlignment(Pos.CENTER);
		hb2.getChildren().addAll(startButton, cancelButton);
		mainPane.setBottom(hb2);

		startButton.setOnAction(event -> {
			startButton.setDisable(true);
			progressIndicator.setProgress(0);
			cancelButton.setDisable(false);
			Main.this.copyWorker = Main.this.createWorker();

			progressIndicator.progressProperty().unbind();
			progressIndicator.progressProperty()
					.bind(Main.this.copyWorker.progressProperty());

			Main.this.copyWorker.messageProperty()
					.addListener((ChangeListener<String>) (observable, oldValue,
							newValue) -> System.out.println(newValue));

			new Thread(Main.this.copyWorker).start();
		});
		cancelButton.setOnAction(event -> {
			startButton.setDisable(false);
			cancelButton.setDisable(true);
			Main.this.copyWorker.cancel(true);
			progressIndicator.progressProperty().unbind();
			progressIndicator.setProgress(0);
			System.out.println("cancelled.");
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Task createWorker() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(2000);
					this.updateMessage("2000 milliseconds");
					this.updateProgress(i + 1, 10);
				}
				return true;
			}
		};
	}
}