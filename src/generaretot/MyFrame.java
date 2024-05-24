package generaretot;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                valueLabel.setText("Value: " + newValue);
                // FIX HERE
                sineparams[sineParamIndex] = (Double) newValue;
            }
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
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                valueLabel.setText("Value: " + newValue);
                // FIX HERE
                sineparams[sineParamIndex] = (Double) newValue;
            }
        });



        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(label, valueLabel, slider);

        return root;
    }

    public VBox createButton(String valueLabel) {

        Button buton = new Button(valueLabel);

        buton.setOnAction(event -> {
            GenerarePCT gen = new GenerarePCT(sineparams);

            gen.start(new Stage());
        });

        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(buton);

        return root;
    }

    private void runExternalJavaFile() throws IOException {
        // Use the absolute path to the Java executable and the classpath
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = "C:\\Users\\vlddd\\IdeaProjects\\MoleculeSampleApp\\out\\production\\MoleculeSampleApp\\generaretot"; // Replace with the absolute path to your class directory
        String className = "generaretot/GenerarePCT"; // Replace with the fully qualified name of your class

        // Command to run the external Java file
        String command = String.format("%s -cp %s %s", javaBin, classpath, className);

        // Print the command for debugging purposes
        System.out.println("Executing command: " + command);

        // Execute the command
        Process process = Runtime.getRuntime().exec(command);

        // Capture and print the output and error streams
        captureOutputAndErrors(process);

        // Handle any potential errors in the process
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error executing external Java file. Exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to capture and print output and errors from the process
    private void captureOutputAndErrors(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("OUTPUT: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("ERROR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void start(Stage stage) throws IOException {

        // create label
        Label label = new Label("Select the Brightness");
        Label l = new Label(" ");
        Label A1= new Label("Amplitudine 1");
        Label A2= new Label("Amplitudine 2");
        Label A3= new Label("Amplitudine 3");

        // set the color of the text
        l.setTextFill(Color.BLACK);

        VBox[] root = new VBox[8];
        Label[] A = new Label[3];
        Label[] Def = new Label[3];
        Label Puls = new Label("Value: "+(0));
        Double[] defAValues = { (double) 25.0, (double)-25.0, (double) 0};
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

        root[6] = createSlider("Pulsatie",Puls, 6, (double)60.0);
        /*Button reset = new Button("Reset");
        reset.setOnAction(event ->{
            for(int j = 0; j < 3; j++){
                A[j] = new Label("Value: "+(0));
                String s = String.format("A"+(j+1));
                root[j] = createSlider(s,A[j],7);
            }

            for(int j = 0; j < 3; j++){
                Def[j] = new Label("Value: "+(0));
                String s = String.format("Defazaj"+(j+1));
                root[j+3] = createSlider(s,Def[j]);
            }

        });*/
        //root[7] = createButton("Generate");


        root[7] = createButton("Generate");
        //root[8] = createButton("Reset");


        VBox root1 = new VBox();
        root1.setPadding(new Insets(20));
        root1.setSpacing(10);

        for (VBox vbox : root) {
            root1.getChildren().add(vbox);
        }


        stage.setTitle("Slider Sample");

        // create Scene and add to the frame
        Scene scene = new Scene(root1, 960, 800);
        stage.setScene(scene);
        stage.show();
    }

    // Main Method
    public static void main(String[] args)
    {
        // Launch Application
        MyFrame.launch(args);
    }
}
