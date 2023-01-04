package agh.oop.GUI;

import agh.oop.MapDirection;
import agh.oop.SimulationEngine;
import agh.oop.animal.*;
import agh.oop.map.*;
import agh.oop.plant.IPlantType;
import agh.oop.plant.Plant;
import agh.oop.plant.Toxic;
import agh.oop.plant.Trees;
import com.sun.jdi.InterfaceType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class App extends Application implements IMapRefreshObserver {
    private List<Animal> animals;
    private List<Plant> plants;
    public int span = 1;
    final GridPane grid = new GridPane();
    final GridPane info = new GridPane();
    final GridPane info_right = new GridPane();
    GridPane startMenu = new GridPane();
    SimulationEngine engine;
    private int width;
    private int height;
    private int energyFromGrass;
    private int energyToReproduce;
    private int animalStartEnergy;
    private int grasPerCycle;
    private int energyLostPerCycle;
    private int numberOfAnimalsOnStart;
    private final int cellSize = 40;
    private int cols = 20 + 1;
    private int rows = 20 + 1;
    private int animalImgSize = cellSize - 10;
    private int imgSize = cellSize + 1; //replace this with cellsize if bcimage can be sized fully to fit sqare or when span removed
    private IPlantType plantType = new Trees();
    private IGeneMutator mutatorType = new MutatorRandom();
    private INextGene nextGeneType = new NextGeneNormal();
    private IMapType mapType;
    private Animal selectedAnimal;

    @Override
    public void init() {
        try {


        } catch (IllegalArgumentException e) {
            System.out.println(e + " an unordinary exeption has been called upon this creation that should be dealt without further ado as it is urging matter to anihilate all exeptions ");
        }
    }

    private void generateMap() {
        this.plants = engine.getplants();
        this.animals = engine.getanimals();
        addConstaints();
        updateInfo();
        updateInfo_right();
        populateWorld();
    }

    @Override
    public void refresh() { // possibly can be replaced by antoher call: https://stackoverflow.com/questions/25873769/launch-javafx-application-from-another-class

        Platform.runLater(() -> {

            while (this.grid.getRowConstraints().size() > 0) {
                this.grid.getRowConstraints().remove(0);
            }

            while (this.grid.getColumnConstraints().size() > 0) {
                this.grid.getColumnConstraints().remove(0);
            }

            Node node = this.grid.getChildren().get(0);
            this.grid.getChildren().clear();
            this.grid.getChildren().add(0, node);

            while (this.info.getRowConstraints().size() > 0) {
                this.info.getRowConstraints().remove(0);
            }

            while (this.info.getColumnConstraints().size() > 0) {
                this.info.getColumnConstraints().remove(0);
            }

            this.info.getChildren().clear();
            while (this.info_right.getRowConstraints().size() > 0) {
                this.info_right.getRowConstraints().remove(0);
            }

            while (this.info_right.getColumnConstraints().size() > 0) {
                this.info_right.getColumnConstraints().remove(0);
            }

            this.info_right.getChildren().clear();
            generateMap();

        });
    }


    public GridPane setupStart(Stage primaryStage) {
        TextField movesInput = new TextField();
        movesInput.setPrefWidth(Math.floor(600));
        movesInput.setText("");

        Label mapWidthLabel = new Label("map width: ");
        mapWidthLabel.setStyle("-fx-text-fill: white");
        Label mapHeightLabel = new Label("map height: ");
        Label energyFromGrassLabel = new Label("energy per plant: ");
        Label energyRequiredToReproduceLabel = new Label("energy required to reproduce: ");
        Label animalStartEnergyLabel = new Label("animal starting energy: ");
        Label grassPerCycleLabel = new Label("grass per cycle: ");
        Label energyLostPerCycleLabel = new Label("energy lost per cycle: ");
        Label numberOfAnimalsOnStartLabel = new Label("number of animals: ");
        Label worldTypeLabel = new Label("type of world: ");
        Label nextGeneTypeLabel = new Label("type of nextGene: ");
        Label geneMutatorTypeLabel = new Label("type of geneMutator: ");
        Label plantTypeLabel = new Label("type of plants: ");


        mapHeightLabel.setStyle("-fx-text-fill: white");
        energyFromGrassLabel.setStyle("-fx-text-fill: white");
        energyRequiredToReproduceLabel.setStyle("-fx-text-fill: white");
        animalStartEnergyLabel.setStyle("-fx-text-fill: white");
        grassPerCycleLabel.setStyle("-fx-text-fill: white");
        energyLostPerCycleLabel.setStyle("-fx-text-fill: white");
        numberOfAnimalsOnStartLabel.setStyle("-fx-text-fill: white");

        worldTypeLabel.setStyle("-fx-text-fill: white");
        nextGeneTypeLabel.setStyle("-fx-text-fill: white");
        geneMutatorTypeLabel.setStyle("-fx-text-fill: white");
        plantTypeLabel.setStyle("-fx-text-fill: white");


        TextField mapWidthField = new TextField("20");
        TextField mapHeightField = new TextField("20");
        TextField energyFromGrassField = new TextField("9");
        TextField energyRequiredToReproduceField = new TextField("20");
        TextField animalStartEnergyField = new TextField("15");
        TextField grassPerCycleField = new TextField("15");
        TextField energyLostPerCycleField = new TextField("1");
        TextField numberOfAnimalsOnStartField = new TextField("35");
//
        ComboBox worldTypeField = new ComboBox();
        worldTypeField.getItems().addAll(
                "hell",
                "round"
        );
        ComboBox nextGeneTypeField = new ComboBox();
        nextGeneTypeField.getItems().addAll(
                "NextGeneNormal",

                "nextGeneCreazy"
        );
        ComboBox geneMutatorTypeField = new ComboBox();
        geneMutatorTypeField.getItems().addAll(
                "MutatorRandom",
                "SlightImprovement"
        );
        ComboBox plantTypeField = new ComboBox();
        plantTypeField.getItems().addAll(
                "Trees",
                "Toxic"

        );


        worldTypeField.getSelectionModel().selectFirst();
        nextGeneTypeField.getSelectionModel().selectFirst();
        geneMutatorTypeField.getSelectionModel().selectFirst();
        plantTypeField.getSelectionModel().selectFirst();

        GridPane txt = new GridPane();
        txt.add(mapWidthLabel, 0, 0, span, span);
        txt.add(mapWidthField, 1, 0, span, span);

        txt.add(mapHeightLabel, 0, 1, span, span);
        txt.add(mapHeightField, 1, 1, span, span);

        txt.add(energyFromGrassLabel, 0, 2, span, span);
        txt.add(energyFromGrassField, 1, 2, span, span);

        txt.add(energyRequiredToReproduceLabel, 0, 3, span, span);
        txt.add(energyRequiredToReproduceField, 1, 3, span, span);

        txt.add(animalStartEnergyLabel, 0, 4, span, span);
        txt.add(animalStartEnergyField, 1, 4, span, span);

        txt.add(grassPerCycleLabel, 0, 5, span, span);
        txt.add(grassPerCycleField, 1, 5, span, span);

        txt.add(energyLostPerCycleLabel, 0, 6, span, span);
        txt.add(energyLostPerCycleField, 1, 6, span, span);

        txt.add(numberOfAnimalsOnStartLabel, 0, 7, span, span);
        txt.add(numberOfAnimalsOnStartField, 1, 7, span, span);

        txt.add(worldTypeLabel, 0, 8, span, span);
        txt.add(worldTypeField, 1, 8, span, span);
        txt.add(nextGeneTypeLabel, 0, 9, span, span);
        txt.add(nextGeneTypeField, 1, 9, span, span);
        txt.add(geneMutatorTypeLabel, 0, 10, span, span);
        txt.add(geneMutatorTypeField, 1, 10, span, span);
        txt.add(plantTypeLabel, 0, 11, span, span);
        txt.add(plantTypeField, 1, 11, span, span);
        Button startButton = new Button("start");


        startButton.setOnAction(action -> {

            this.width = Integer.parseInt(mapWidthField.getText());
            this.height = Integer.parseInt(mapHeightField.getText());
            this.rows = height + 2;
            this.cols = width + 2;
            //TODO: set cellsize proportion
//            this.cellSize = (int) (width*0.1*height*0.1*10);
            this.animalImgSize = (int) (cellSize * 0.8);
            this.imgSize = cellSize + 1;
            this.energyFromGrass = Integer.parseInt(energyFromGrassField.getText());
            this.energyToReproduce = Integer.parseInt(energyRequiredToReproduceField.getText());
            this.animalStartEnergy = Integer.parseInt(animalStartEnergyField.getText());
            this.grasPerCycle = Integer.parseInt(grassPerCycleField.getText());
            this.energyLostPerCycle = Integer.parseInt(energyLostPerCycleField.getText());
            this.numberOfAnimalsOnStart = Integer.parseInt(numberOfAnimalsOnStartField.getText());
            Object value = plantTypeField.getValue();
            if ("Toxic".equals(value)) {
                this.plantType = new Toxic();
            } else if ("Trees".equals(value)) {
                this.plantType = new Trees();
            }
            Object mp = worldTypeField.getValue();
            if ("hell".equals(mp)) {
                this.mapType = new Hell(this.energyToReproduce);
            } else if ("round".equals(mp)) {
                this.mapType = new Earth();
            }
            Object gm = geneMutatorTypeField.getValue();
            if ("MutatorRandom".equals(gm)) {
                this.mutatorType = new MutatorRandom();
            } else if ("slightCorrection".equals(gm)) {
                this.mutatorType = new SlightImprovement();
            }
            Object ng = geneMutatorTypeField.getValue();
            if ("nextGeneCreazy".equals(ng)) {
                this.nextGeneType = new NextGeneCrazy();
            } else if ("NextGeneNormal".equals(ng)) {
                this.nextGeneType = new NextGeneNormal();
            }


            startMenu.setVisible(false);
            this.engine = new SimulationEngine(new MapSize(this.width, this.height), this.mutatorType, this.nextGeneType, this.mapType, this.plantType, this.numberOfAnimalsOnStart, 40, this.animalStartEnergy, this.energyFromGrass, this.energyToReproduce, this.grasPerCycle, this.energyLostPerCycle , 10 , 2, 2 );
            this.plants = engine.getplants();
            this.animals = engine.getanimals();
            this.engine.addObserverMap(this);

            this.engine.run();
//            updateInfo();
//            updateInfo_right();
            this.selectedAnimal = this.engine.map.getAnimals().get(0);
            this.selectedAnimal.setSelected();
            generateMap();
            Thread engineThread = new Thread(this.engine);
            engineThread.start();

            Button btn = new Button("stop");
            //FIXME: merge it into one btn and replace --
            btn.setOnAction(ac -> {
                engineThread.suspend();
            });
            Button btn2 = new Button("start");
            btn2.setOnAction(ac -> {
                engineThread.resume();
            });
            Button newSimulationBtn = new Button("new simulation");
            //FIXME: merge it into one btn and replace --
            newSimulationBtn.setOnAction(ac -> {

                Stage new_stage = new Stage();
                HBox hBox = new HBox();
                GridPane txtx = setupStart(new_stage);
                hBox.getChildren().addAll(txtx);
                hBox.setAlignment(Pos.CENTER);
                hBox.setStyle("-fx-font-size: 20px");
                VBox vBox = new VBox();
                vBox.getChildren().addAll(hBox);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-background-color: #56565e;");

                VBox.setMargin(hBox, new Insets(10, 0, 50, 0));
                Scene scene = new Scene(vBox, 720, 780);
                new_stage.setScene(scene);
                new_stage.setTitle("World");
                new_stage.show();
            });
            HBox hBox = new HBox();
            hBox.getChildren().addAll(this.info,btn, btn2,newSimulationBtn, this.info_right);
            hBox.setAlignment(Pos.CENTER);


            HBox vBox = new HBox();
            GridPane tmp1 = new GridPane();

            tmp1.add(this.grid, 0,0,span,span);
            tmp1.add(hBox, 0,1, span,span);


            vBox.getChildren().addAll(tmp1);
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-background-color: #56565e;");
            GridPane tmp2 = new GridPane();

            tmp2.add(this.info, 0,0,span,span);
            tmp2.add(vBox, 1,0, span,span);
            tmp2.add(this.info_right, 2,0,span,span);


            VBox vBoxx = new VBox();

            vBoxx.getChildren().addAll(tmp2);
            vBoxx.setAlignment(Pos.CENTER);
            vBoxx.setStyle("-fx-background-color: #56565e;");

            Scene scene = new Scene(vBoxx, (this.cols * cellSize + 2 * cellSize)*2, this.rows * cellSize + 2 * cellSize);
            primaryStage.setScene(scene);
            this.engine.gate();
        });
        HBox hBox = new HBox();
        hBox.getChildren().addAll(startButton);
        hBox.setAlignment(Pos.CENTER);

        startMenu.add(txt, 1, 0, span, span);
        startMenu.add(hBox, 1, 1, span, span);

        var images = new Image("border_textures/wbricks.jpg", true);
        var bgImages = new BackgroundImage(
                images,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(30, 30, false, false, false, false)

        );
        var image = new Image("background_textures/grass.jpg", true); //FIXME: fix map background when it is not a square
        var bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize((int)(this.cols * cellSize/2336), (int)(this.rows * cellSize/2334), false, false, true, false)
        );
        this.grid.setBackground(new Background(bgImage));
        this.grid.setGridLinesVisible(false);
        this.grid.setMaxHeight(0);
        this.grid.setMaxWidth(0);
        startMenu.setBackground(new Background(bgImages));
        return startMenu;

    }

    @Override
    public void start(Stage primaryStage) {
        HBox hBox = new HBox();
        GridPane txt = setupStart(primaryStage);
        hBox.getChildren().addAll(txt);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-font-size: 20px");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #56565e;");

        VBox.setMargin(hBox, new Insets(10, 0, 50, 0));
        Scene scene = new Scene(vBox, 720, 780);
        primaryStage.setScene(scene);
        primaryStage.setTitle("World");



        primaryStage.show();
    }

    public void addConstaints() {
        for (int i = 0; i < rows - 1; i++) {
            this.grid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }
        for (int i = 0; i < cols - 1; i++) {
            this.grid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }
    public void updateInfo_right() {

        Label allAnimals = new Label("Aktualna pozycja: "+ selectedAnimal.getPosition() + "");
        allAnimals.setStyle("-fx-text-fill: white");
        this.info_right.add(allAnimals, 2,0,span,span);

        Label allPlants = new Label("Aktualna ilosc energii: "+ selectedAnimal.getEnergy() + "");
        allPlants.setStyle("-fx-text-fill: white");
        this.info_right.add(allPlants, 2,1,span,span);

        Label gene = new Label("Genotyp: "+ selectedAnimal.getGenome() + "");
        gene.setStyle("-fx-text-fill: white");
        this.info_right.add(gene, 2,3,span,span);

        Label avg = new Label("Aktywny gen: "+ selectedAnimal.getActiveGene() + "");
        avg.setStyle("-fx-text-fill: white");
        this.info_right.add(avg, 2,4,span,span);

        Label days = new Label("Dlugosc zycia: "+ selectedAnimal.getTimeAlive() + "");
        this.info_right.add(days, 2,6,span,span);
        days.setStyle("-fx-text-fill: white");


        Label avgLife = new Label("Aktualna snrednia dlugosc zycia martwych zwierzat: "+this.engine.map.getAverageLifespan() + "");
        avgLife.setStyle("-fx-text-fill: white");
        this.info_right.add(avgLife, 2,5,span,span);


        var images = new Image("border_textures/wblocks.jpg", true);
        var bgImages = new BackgroundImage(
                images,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(30, 30, false, false, false, false)

        );

        info_right.setStyle("-fx-font-size: 20px");
        info_right.setBackground(new Background(bgImages));

    }
    public void updateInfo() {
//        for (int i = 0; i < rows - 1; i++) {
//            this.info.getColumnConstraints().add(new ColumnConstraints(200));
//        }
//        for (int i = 0; i < cols - 1; i++) {
//            this.info.getRowConstraints().add(new RowConstraints(200));
//        }

        Label allAnimals = new Label("Aktualna ilosc zwierzat: "+this.engine.map.getAnimals().size() + "");
        allAnimals.setStyle("-fx-text-fill: white");
        this.info.add(allAnimals, 2,0,span,span);

        Label allPlants = new Label("Aktualna ilosc roslin: "+this.engine.map.getPlants().size() + "");
        allPlants.setStyle("-fx-text-fill: white");
        this.info.add(allPlants, 2,1,span,span);

        Label allFreeSpace = new Label("Aktualna ilosc wolnych miejsc: "+this.engine.map.getFreeSpace() + "");
        allFreeSpace.setStyle("-fx-text-fill: white");
        this.info.add(allFreeSpace, 2,2,span,span);

        Label gene = new Label("Aktualna najpopularniejszy gen: "+this.engine.map.getTopGeneFromAllGenomes() + "");
        gene.setStyle("-fx-text-fill: white");
        this.info.add(gene, 2,3,span,span);

        Label avg = new Label("Aktualna srednia energi dla zyjacych zwierzat: "+this.engine.map.getAverageEnergy() + "");
        avg.setStyle("-fx-text-fill: white");
        this.info.add(avg, 2,4,span,span);

        Label avgLife = new Label("Aktualna snrednia dlugosc zycia martwych zwierzat: "+this.engine.map.getAverageLifespan() + "");
        avgLife.setStyle("-fx-text-fill: white");
        this.info.add(avgLife, 2,5,span,span);

        Label days = new Label("Aktualna liczba dni: "+this.engine.map.getDays() + "");
        this.info.add(days, 2,6,span,span);
        days.setStyle("-fx-text-fill: white");

        var images = new Image("border_textures/wblocks.jpg", true);
        var bgImages = new BackgroundImage(
                images,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(30, 30, false, false, false, false)

        );

        info.setStyle("-fx-font-size: 20px");
        info.setBackground(new Background(bgImages));

    }

    public void populateWorld() {
        // for finding one uneatable plant:
//    File dir = new File("src/main/resources/flower_textures");
//    File[] files = dir.listFiles();
//    Random rand = new Random();
//    File file = files[rand.nextInt(files.length)];
//    System.out.println(file.getName());
//    Image imgp = new Image("flower_textures/"+file.getName());
        this.plants = engine.getplants();
        this.animals = engine.getanimals();
        for (Plant iMapElement : this.plants) {
            Label field = new Label("");
//            ImageView view = new ImageView(imgp);
            ImageView view = new ImageView(iMapElement.getTexture());
            view.setFitHeight(imgSize);
            view.setPreserveRatio(true);
            field.setGraphic(view);
            this.grid.add(field, iMapElement.getPosition().x + 1, iMapElement.getPosition().y + 1, span, span);
            GridPane.setHalignment(field, javafx.geometry.HPos.CENTER);
        }
        for (Animal d : this.animals) {
            Label field = new Label("");
            Image img = d.getTexture();
            ImageView view = new ImageView(img);
            view.setFitHeight(animalImgSize);
            view.setPreserveRatio(true);
            field.setGraphic(view);

            int head = d.getNextDirection();
            head = (head + 9 + 5) % 8;
            switch (Integer.toString(head)) { // FIXME: getAcriveGene returns same value for all animals
                case "0" -> field.setRotate(0);
                case "1" -> field.setRotate(-45);
                case "2" -> field.setRotate(-90);
                case "3" -> field.setRotate(-135);
                case "4" -> field.setRotate(-180);
                case "5" -> field.setRotate(-225);
                case "6" -> field.setRotate(-270);
                case "7" -> field.setRotate(-315);
            }

            this.grid.add(field, d.getPosition().x + 1, d.getPosition().y + 1, span, span);
            GridPane.setHalignment(field, javafx.geometry.HPos.CENTER);
        }
        Image img = new Image("border_textures/wood.jpg");
        for (int i = 0; i < rows; i++) {
            Label field = new Label("");
            ImageView view = new ImageView(img);
            view.setFitHeight(imgSize);
            view.setPreserveRatio(true);
            field.setGraphic(view);
            this.grid.add(field, 0, i, span, span);
            Label field2 = new Label("");
            ImageView view2 = new ImageView(img);
            view2.setFitHeight(imgSize);
            view2.setPreserveRatio(true);
            field2.setGraphic(view2);
            this.grid.add(field2, cols - 1, i, span, span);

        }
        for (int i = 0; i < cols; i++) {
            Label field = new Label("");
            ImageView view = new ImageView(img);
            view.setFitHeight(imgSize);
            view.setPreserveRatio(true);
            field.setGraphic(view);
            field.setRotate(90);
            this.grid.add(field, i, 0, span, span);
            Label field2 = new Label("");
            ImageView view2 = new ImageView(img);
            view2.setFitHeight(imgSize);
            view2.setPreserveRatio(true);
            field2.setGraphic(view2);
            field2.setRotate(90);
            this.grid.add(field2, i, rows - 1, span, span);
        }
    }


}