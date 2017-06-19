package pixelart;

import java.io.InputStream;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static final String DEFAULT_TITLE = "Select a Game";

	private static final int CONTENT_HEIGHT = 550;

	private static final int HEIGHT = 670;

	private static final int WIDTH = 450;

	private static final int COLS = 3;

	private Font font8bitStrong;

	private Font font8bitNormal;

	private GridPane grid;

	private ScrollPane scrollPane;

	private Scene mainScene;

	private Label pageTitle;

	private VBox mainVBox;

	private VBox gameDescriptionVBox;

	private Button backToMainMenuButton;

	private GridPane titleGrid;

	@Override
	public void start(Stage primaryStage) {
		try {

			BorderPane root = new BorderPane();
			root.setMinHeight(670);
			primaryStage.setTitle("8Bit Games");
			mainScene = new Scene(root, WIDTH, HEIGHT);
			InputStream path = Main.class.getResourceAsStream("/resources/icon.png");
			Image icon = new Image(path);
			primaryStage.getIcons().add(icon);
			mainScene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

			// Loading fonts
			font8bitStrong = Font.loadFont(Main.class.getResourceAsStream("/resources/8bit.ttf"), 40);
			font8bitNormal = Font.loadFont(Main.class.getResourceAsStream("/resources/8bit.ttf"), 20);

			// Grid Constraints
			grid = new GridPane();
			grid.setMinWidth(450);
			RowConstraints row1 = new RowConstraints();
			RowConstraints row2 = new RowConstraints();
			row1.setPercentHeight(50);
			row2.setPercentHeight(50);
			row1.setFillHeight(true);
			row2.setFillHeight(true);
			row1.setValignment(VPos.TOP);
			row2.setValignment(VPos.TOP);

			ColumnConstraints col1 = new ColumnConstraints();
			ColumnConstraints col2 = new ColumnConstraints();
			ColumnConstraints col3 = new ColumnConstraints();
			col1.setPrefWidth(200);
			col2.setPrefWidth(200);
			col3.setPrefWidth(200);
			col1.setFillWidth(true);
			col2.setFillWidth(true);
			col3.setFillWidth(true);
			col1.setHalignment(HPos.CENTER);
			col2.setHalignment(HPos.CENTER);
			col3.setHalignment(HPos.CENTER);

			grid.getRowConstraints().addAll(row1, row2);
			grid.getColumnConstraints().addAll(col1, col2, col3);

			// Mouse Listeners
			highLightNode(grid);

			// Scroll Listeners
			addScrollListeners(mainScene);

			Label label1 = new Label("Space");
			Label label2 = new Label("PacMan");
			Label label3 = new Label("Pong");
			Label label4 = new Label("Space Invaders");
			Label label5 = new Label("8bit banana");
			Label label6 = new Label("Mario");

			path = Main.class.getResourceAsStream("/resources/space.png");
			addVBox(path, label1);

			path = Main.class.getResourceAsStream("/resources/pacman_logo.png");
			addVBox(path, label2);

			path = Main.class.getResourceAsStream("/resources/pong.png");
			addVBox(path, label3);

			path = Main.class.getResourceAsStream("/resources/space_invaders.png");
			addVBox(path, label4);

			path = Main.class.getResourceAsStream("/resources/banana.png");
			addVBox(path, label5);

			path = Main.class.getResourceAsStream("/resources/mushroom.png");
			addVBox(path, label6);

			scrollPane = new ScrollPane(grid);
			scrollPane.setMinHeight(CONTENT_HEIGHT);
			scrollPane.setMaxHeight(CONTENT_HEIGHT);
			scrollPane.setHmin(0);
			scrollPane.setHvalue(0);
			scrollPane.setHmax(15);

			// setting scroll-pane height
			scrollPane.setStyle("-fx-font-size: 20px");
			scrollPane.setFocusTraversable(false);
			scrollPane.setFitToHeight(true);

			pageTitle = new Label(DEFAULT_TITLE);
			pageTitle.setStyle("-fx-text-fill: yellow");
			pageTitle.setFont(font8bitStrong);
			titleGrid = new GridPane();
			titleGrid.getStyleClass().add("title-grid");
			ColumnConstraints col0 = new ColumnConstraints();
			ColumnConstraints col = new ColumnConstraints();
			col.setFillWidth(true);
			col.setHalignment(HPos.CENTER);
			col.setHgrow(Priority.ALWAYS);
			col0.setMaxWidth(16);
			col0.setHgrow(Priority.NEVER);
			col0.setHalignment(HPos.LEFT);

			titleGrid.getColumnConstraints().addAll(col0, col);
			titleGrid.add(pageTitle, 1, 0);

			mainVBox = new VBox(titleGrid, scrollPane);
			mainVBox.getStyleClass().add("vbox");
			mainVBox.setMinHeight(580);
			mainVBox.setAlignment(Pos.BOTTOM_CENTER);
			root.setCenter(mainVBox);

			MenuBar menuBar = new MenuBar();
			Menu menu1 = new Menu("Game");
			Menu menu2 = new Menu("Options");
			Menu menu3 = new Menu("About");
			menuBar.getMenus().addAll(menu1, menu2, menu3);
			root.setTop(menuBar);

			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void highLightNode(Node n) {
		n.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mainScene.setCursor(Cursor.HAND);
			}
		});

		n.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mainScene.setCursor(Cursor.DEFAULT);
			}
		});
	}

	private void addScrollListeners(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();
				int unit_increment = 5;
				double hvalue = scrollPane.getHvalue();
				if (code == KeyCode.LEFT) {
					scrollPane.setHvalue(hvalue - unit_increment);
				} else if (code == KeyCode.RIGHT) {
					scrollPane.setHvalue(hvalue + unit_increment);
				}
			}
		});

		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double hvalue = scrollPane.getHvalue();
				double deltaY = event.getDeltaY();
				scrollPane.setHvalue(hvalue - deltaY / 8);
			}
		});
	}

	private void addVBox(InputStream path, Label label) {
		label.setFont(font8bitNormal);
		label.getStyleClass().add("black_label");

		Image img = new Image(path);
		ImageView imageView = new ImageView(img);
		imageView.setFitWidth(190);
		imageView.setPreserveRatio(true);
		VBox vbox_small = new VBox(label, imageView);
		vbox_small.getStyleClass().add("vbox_small");
		vbox_small.setAlignment(Pos.CENTER);
		ObservableList<Node> children = grid.getChildren();
		int size = children.size();
		grid.add(vbox_small, size % COLS, size / COLS);

		vbox_small.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetColors();
				vbox_small.setStyle("-fx-background-color: derive(yellow,0.65);-fx-border-width: 0 1 4 0");
				Label label = (Label) vbox_small.getChildren().get(0);
				initGameDescriptionScene(label.getText());

			}

			private void initGameDescriptionScene(String title) {
				ImageView imageView = new ImageView(
						Main.class.getResource("/resources/chevron_left.png").toExternalForm());
				imageView.setFitWidth(12);
				imageView.setPreserveRatio(true);
				backToMainMenuButton = new Button("", imageView);
				highLightNode(backToMainMenuButton);

				backToMainMenuButton.setMaxWidth(16);
				backToMainMenuButton.setMaxHeight(16);
				backToMainMenuButton.getStyleClass().add("button-back");
				pageTitle.setText(title);
				titleGrid.add(backToMainMenuButton, 0, 0);
				backToMainMenuButton.setOnAction(e -> backToMainMenu());

				ObservableList<Node> vboxChildren = mainVBox.getChildren();
				vboxChildren.remove(scrollPane);
				gameDescriptionVBox = new VBox();
				gameDescriptionVBox.setMinHeight(CONTENT_HEIGHT);
				gameDescriptionVBox.setMaxHeight(CONTENT_HEIGHT);
				gameDescriptionVBox.setStyle("-fx-background-color: yellow");
				vboxChildren.add(gameDescriptionVBox);
			}

			private void resetColors() {
				for (Node n : children) {
					n.setStyle(null);
				}
			}
		});
	}

	protected void backToMainMenu() {
		pageTitle.setText(DEFAULT_TITLE);
		titleGrid.getChildren().remove(backToMainMenuButton);
		ObservableList<Node> children = mainVBox.getChildren();
		children.remove(gameDescriptionVBox);
		children.add(scrollPane);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
