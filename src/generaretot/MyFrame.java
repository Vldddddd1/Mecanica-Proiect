package generaretot;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;
import static java.lang.Math.PI;


public class MyFrame extends Application {
    private final Double[] sineparams = new Double[8];

    public VBox createSlider(String labelText, Label valueLabel, Integer sineParamIndex, Double defaultValue) {
        Label label = new Label(labelText);

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(0);
        sineparams[sineParamIndex] = defaultValue;
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(0.01);

        slider.valueProperty().addListener((_, _, newValue) -> {
            DecimalFormat df = new DecimalFormat("0.00");
            String formattedNumber = df.format(newValue);
            valueLabel.setText("Value: " + formattedNumber);
            sineparams[sineParamIndex] = (Double) newValue;
        });



        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(label, valueLabel, slider);

        return root;
    }

    public VBox createSlider1(String labelText, Label valueLabel, Integer sineParamIndex, Double defaultValue) {
        Label label = new Label(labelText);

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(2*PI);
        slider.setValue(0);
        sineparams[sineParamIndex] = defaultValue;
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(0.0001);

        slider.valueProperty().addListener((_, _, newValue) -> {
            DecimalFormat df = new DecimalFormat("0.0000");
            String formattedNumber = df.format(newValue);
            valueLabel.setText("Value: " + formattedNumber);
            sineparams[sineParamIndex] = (Double) newValue;
        });



        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(label, valueLabel, slider);

        return root;
    }

    public VBox createButton(String valueLabel) {

        Button buton = new Button(valueLabel);

        buton.setOnAction(_ -> {
            
            GenerarePCT gen = new GenerarePCT(sineparams);

            gen.start(new Stage());

        });

        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(buton);

        return root;
    }


    public void start(Stage stage) throws IOException {

        Label l = new Label(" ");
        new Label("Amplitudine 1");
        new Label("Amplitudine 2");
        new Label("Amplitudine 3");

        l.setTextFill(Color.BLACK);

        VBox[] root = new VBox[8];
        Label[] A = new Label[3];
        Label[] Def = new Label[3];
        Label Puls = new Label("Value: "+(0));
        Double[] defAValues = { (double) 0, (double)0, (double) 0};
        for(int j = 0; j < 3; j++){
            A[j] = new Label("Value: "+(0));
            String s = String.format("A"+(j+1));
            root[j] = createSlider(s,A[j], j, defAValues[j]);
        }

        Double[] defPhiValues = { (double) 0, (double)0, (double) 0};
        for(int j = 0; j < 3; j++){
            Def[j] = new Label("Value: "+(0));
            String s = String.format("Defazaj"+(j+1));
            root[j+3] = createSlider1(s,Def[j], j+3,defPhiValues[j]);
        }

        root[6] = createSlider("Pulsatie",Puls, 6, (double)0);
        root[7] = createButton("Generate");


        VBox root1 = new VBox();
        root1.setPadding(new Insets(20));
        root1.setSpacing(10);

        for (VBox vbox : root) {
            root1.getChildren().add(vbox);
        }


        stage.setTitle("Slider Sample");

        Scene scene = new Scene(root1, 960, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        MyFrame.launch(args);
    }
}
