package agh.oop.GUI;

import agh.oop.animal.*;
import agh.oop.map.Earth;
import agh.oop.map.Hell;
import agh.oop.map.IMapType;
import agh.oop.plant.IPlantType;
import agh.oop.plant.Toxic;
import agh.oop.plant.Trees;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;

public class AppHolder extends Application {
    int span = 1;
    private int width=20;
    private int height = 20;
    private int energyFromGrass = 5;
    private int energyToReproduce = 30;
    private int animalStartEnergy = 15;
    private int grasPerCycle = 1;
    private int energyLostPerCycle = 1;
    private int numberOfAnimalsOnStart = 35;
    private IPlantType plantType = new Trees();
    private String plantTypeArr[] = {"Trees", "Toxic"};
    private IGeneMutator mutatorType = new MutatorRandom();
    private String mutatorTypeArr[] = {"MutatorRandom", "SlightImprovement"};
    private INextGene nextGeneType = new NextGeneNormal();
    private String nextGeneTypeArr[] = {"NextGeneNormal", "nextGeneCreazy"}; //TODO: not implemented?
    private IMapType mapType;
    private String mapTypeArr[] = {"Hell", "World"};
    private String presetsArr[] = {"preset1", "preset2", "preset3"};
    private int cols = 20 + 1;
    private int rows = 20 + 1;
    private final int cellSize = 40;
    private int animalImgSize = cellSize - 10;
    private int imgSize = cellSize + 1;
    @Override
    public void start(Stage primaryStage){
        VBox vBox= setupMenu(primaryStage);
        Scene scene = new Scene(vBox, 720, 780);
        primaryStage.setScene(scene);
        primaryStage.setTitle("World");
        primaryStage.show();
    }
    public VBox setupMenu(Stage primaryStage) {
        TextField movesInput = new TextField();
        movesInput.setPrefWidth(Math.floor(600));
        movesInput.setText("");

        Label mapWidthLabel = new Label("map width: ");
        mapWidthLabel.setStyle("-fx-text-fill: white");
        Label mapHeightLabel = new Label("map height: ");
        Label energyFromGrassLabel = new Label("energy per plant: ");
        Label energyRequiredToReproduceLabel = new Label("energy required to reproduce: ");
        Label animalStartEnergyLabel = new Label("animal starting energy: ");
        Label grassPerCycleLabel = new Label("grassPerCycle-depricated: ");
        Label energyLostPerCycleLabel = new Label("energy lost per cycle: ");
        Label numberOfAnimalsOnStartLabel = new Label("number of animals: ");
        Label worldTypeLabel = new Label("type of world: ");
        Label nextGeneTypeLabel = new Label("type of nextGene: ");
        Label geneMutatorTypeLabel = new Label("type of geneMutator: ");
        Label plantTypeLabel = new Label("type of plants: ");
        Label presetsLabel = new Label("Choose preset ");

        mapHeightLabel.setStyle("-fx-text-fill: white");
        energyFromGrassLabel.setStyle("-fx-text-fill: white");
        energyRequiredToReproduceLabel.setStyle("-fx-text-fill: white");
        animalStartEnergyLabel.setStyle("-fx-text-fill: white");
        grassPerCycleLabel.setStyle("-fx-text-fill: white");
        energyLostPerCycleLabel.setStyle("-fx-text-fill: white");
        numberOfAnimalsOnStartLabel.setStyle("-fx-text-fill: white");
        presetsLabel.setStyle("-fx-text-fill: white");
        worldTypeLabel.setStyle("-fx-text-fill: white");
        nextGeneTypeLabel.setStyle("-fx-text-fill: white");
        geneMutatorTypeLabel.setStyle("-fx-text-fill: white");
        plantTypeLabel.setStyle("-fx-text-fill: white");


        TextField mapWidthField = new TextField(String.valueOf(width));
        TextField mapHeightField = new TextField(String.valueOf(height));
        TextField energyFromGrassField = new TextField(String.valueOf(energyFromGrass));
        TextField energyRequiredToReproduceField = new TextField(String.valueOf(energyToReproduce));
        TextField animalStartEnergyField = new TextField(String.valueOf(animalStartEnergy));
        TextField grassPerCycleField = new TextField(String.valueOf(grasPerCycle));
        TextField energyLostPerCycleField = new TextField(String.valueOf(energyLostPerCycle));
        TextField numberOfAnimalsOnStartField = new TextField(String.valueOf(numberOfAnimalsOnStart));
        ComboBox worldTypeField = new ComboBox();
        worldTypeField.getItems().addAll(
                mapTypeArr
        );
        ComboBox nextGeneTypeField = new ComboBox();
        nextGeneTypeField.getItems().addAll(
                nextGeneTypeArr
        );
        ComboBox geneMutatorTypeField = new ComboBox();
        geneMutatorTypeField.getItems().addAll(
                mutatorTypeArr
        );
        ComboBox plantTypeField = new ComboBox();
        plantTypeField.getItems().addAll(
                plantTypeArr
        );

        ComboBox presets = new ComboBox();
        presets.getItems().addAll(
                presetsArr
        );
        Button btn = new Button("set preset");
        btn.setOnAction(ac -> {
            Object pre = presets.getValue();
            if ("preset1".equals(pre)) {
                this.plantTypeArr = new String[]{"Trees", "Toxic"};
                this.mutatorTypeArr = new String[]{"MutatorRandom", "SlightImprovement"};
                this.nextGeneTypeArr = new String[]{"NextGeneNormal", "nextGeneCreazy"};
                this.mapTypeArr = new String[]{"Hell", "World"};
                this.width = 20;
                this.height = 20;
                this.energyFromGrass = 5;
                this.energyToReproduce = 30;
                this.animalStartEnergy = 15;
                this.grasPerCycle = 1;
                this.energyLostPerCycle = 1;
                this.numberOfAnimalsOnStart = 35;
                this.presetsArr = new String[]{"preset1", "preset2", "preset3"};
            }
            if ("preset2".equals(pre)) {
                this.plantTypeArr = new String[]{"Trees", "Toxic"};
                this.mutatorTypeArr = new String[]{ "SlightImprovement", "MutatorRandom",};
                this.nextGeneTypeArr = new String[]{"NextGeneNormal", "nextGeneCreazy"};
                this.mapTypeArr = new String[]{ "World", "Hell",};
                this.width = 10;
                this.height = 10;
                this.energyFromGrass = 7;
                this.energyToReproduce = 36;
                this.animalStartEnergy = 17;
                this.grasPerCycle = 2;
                this.energyLostPerCycle = 2;
                this.numberOfAnimalsOnStart = 5;
                this.presetsArr = new String[]{"preset2", "preset3", "preset1"};
            }
            if ("preset3".equals(pre)) {
                this.plantTypeArr = new String[]{"Trees", "Toxic"};
                this.mutatorTypeArr = new String[]{"MutatorRandom","SlightImprovement"};
                this.nextGeneTypeArr = new String[]{"NextGeneNormal", "nextGeneCreazy"};
                this.mapTypeArr = new String[]{ "World", "Hell",};
                this.width = 15;
                this.height = 15;
                this.energyFromGrass = 3;
                this.energyToReproduce = 20;
                this.animalStartEnergy = 10;
                this.grasPerCycle = 1;
                this.energyLostPerCycle = 3;
                this.numberOfAnimalsOnStart = 20;
                this.presetsArr = new String[]{"preset3", "preset1", "preset2"};
            }
            start(primaryStage);
        });

        worldTypeField.getSelectionModel().selectFirst();
        nextGeneTypeField.getSelectionModel().selectFirst();
        geneMutatorTypeField.getSelectionModel().selectFirst();
        plantTypeField.getSelectionModel().selectFirst();
        presets.getSelectionModel().selectFirst();

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
        txt.add(presets, 1, 12, span, span);
        txt.add(presetsLabel, 0, 12, span, span);
        txt.add(btn, 1, 13, span, span);

        var images = new Image("border_textures/wbricks.jpg", true);
        var bgImages = new BackgroundImage(
                images,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(30, 30, false, false, false, false)

        );
        txt.setBackground(new Background(bgImages));

        VBox vBox = new VBox();
        Button btn2 = new Button("run new simulation");
        btn2.setStyle("-fx-font-size: 30px");
        btn2.setOnAction(ac -> {

            this.width = Integer.parseInt(mapWidthField.getText());
            this.height = Integer.parseInt(mapHeightField.getText());
            this.rows = height + 2;
            this.cols = width + 2;
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
            if ("Hell".equals(mp)) {
                this.mapType = new Hell(this.energyToReproduce);
            } else if ("World".equals(mp)) {
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

            App newInstance = new App();
            newInstance.setupInfo(width, height, energyFromGrass, energyToReproduce, animalStartEnergy, grasPerCycle, energyLostPerCycle, numberOfAnimalsOnStart, plantType, mutatorType, nextGeneType, mapType, cols, rows, cellSize, animalImgSize, imgSize);

        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(txt);
        txt.setStyle("-fx-font-size: 20px");
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, btn2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #56565e;");
        return vBox;

    }
}
