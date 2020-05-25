package com.project.app;

import com.project.controller.ProjectController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProjectClientApplication extends Application {
	private Parent root;
	private FXMLLoader fxmlLoader;

	public static void main(String[] args) {
		launch(ProjectClientApplication.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/fxml/ProjectFrame.fxml"));
		root = fxmlLoader.load();
		primaryStage.setTitle("Projekty");
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		ProjectController controller = fxmlLoader.getController();
		primaryStage.setOnCloseRequest(event -> {
			primaryStage.hide();
			controller.shutdown();
			Platform.exit();
			System.exit(0);
		});
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

	}
}