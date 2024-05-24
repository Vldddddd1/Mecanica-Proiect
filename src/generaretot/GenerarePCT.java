package generaretot;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;


import static java.lang.Math.*;
public class GenerarePCT extends Application {

    private double A1=25,A2=-25,A3=50,phi1=0,phi2=0,phi3=0, o=60;


    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final Xform dotGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 90.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 9999.0;
    private static final double CONTROL_MULTIPLIER = 0.3;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.2;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.6;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    public GenerarePCT(Double[] sineparams) {
        A1 = sineparams[0];
        A2 = sineparams[1];
        A3 = sineparams[2];
        phi1 = sineparams[3];
        phi2 = sineparams[4];
        phi3 = sineparams[5];
        o = sineparams[6];
    }

    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        //redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        //greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        //blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX); 
                mouseDeltaY = (mousePosY - mouseOldY); 
                
                double modifier = 1.0;
                
                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                } 
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }     
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  
                }
            }
        });
    }
    public double ec(double A, double angle){

        double val = 0.0;
        val = A*(sin(angle));
        return val;
    }
    private void miscare(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.GOLD);

        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(1);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);

        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }

    private void buildMolecule() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        Xform moleculeXform = new Xform();
        Xform oxygenXform = new Xform();

        Sphere oxygenSphere = new Sphere(10);
        oxygenSphere.setMaterial(redMaterial);

        moleculeXform.getChildren().add(oxygenXform);
        oxygenXform.getChildren().add(oxygenSphere);

        dotGroup.getChildren().add(moleculeXform);

        double dt = o/1000;

        double angle1=0.0,angle2=0.0,angle3=0.0;
        for(double t = 0; t <= 5*o; t+=dt){

            angle1=phi1+o*t*dt;
            angle2=phi2+o*t*dt;
            angle3=phi3+o*t*dt;

            double x = ec(A1,angle1);
            double y = ec(A2,angle2);
            double z = ec(A3,angle3);

            miscare(x,y,z);
        }
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(90);
                        cameraXform.rx.setAngle(0);
                        break;
                    case X:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(0);
                        cameraXform.rx.setAngle(90);
                        break;
                    case Y:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(0);
                        cameraXform.rx.setAngle(0);
                        break;
                }
            }
        });
    }

    public void start(Stage primaryStage) {

        System.out.println("start()");

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);


        buildCamera();
        buildMolecule();
        buildAxes();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.BLACK);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        primaryStage.setTitle("Instanta");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(camera);

    }

    public static void main(String[] args) {
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                MyFrame.main(args);
            }
        });
        t2.start();

    }
}