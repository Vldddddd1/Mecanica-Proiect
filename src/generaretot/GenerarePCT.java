package generaretot;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.lang.*;


import static java.lang.Math.*;
import static javafx.scene.paint.Color.RED;

public class GenerarePCT extends Application {

    private final double A1;
    private final double A2;
    private final double A3;
    private final double phi1;
    private final double phi2;
    private final double phi3;
    private final double o;


    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final Xform dotGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 80.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 240.0;
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
        cameraXform.setRotateZ(675);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(180);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 0.5, 0.5);
        final Box yAxis = new Box(0.5, AXIS_LENGTH, 0.5);
        final Box zAxis = new Box(0.5, 0.5, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(blueMaterial);
        zAxis.setMaterial(greenMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
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
        });
    }
    public double ec(double A, double angle){

        double val;
        val = A*(sin(angle));
        return val;
    }

    private void vectorViteza(double x, double y, double z, double o, int i, double dt)
    {
        double x1,y1,z1;
        double t = (i + 1) * dt;
        t =abs(t);

        x1=A1*sin(phi1+o* t);
        y1=A2*sin(phi2+o* t);
        z1=A3*sin(phi3+o* t);

        double dx,dy,dz;
        dx=x1-x;
        dy=y1-y;
        dz=z1-z;

        for(double j=0;j<40;j+=1)
        {
            miscare5((x+dx),(y+dy),(z+dz));dx+=x1-x;dy+=y1-y;dz+=z1-z;
        }
        miscare6((x+dx),(y+dy),(z+dz));

        dx=x*0.05;
        dy=y*0.05;
        dz=z*0.05;

        for(double j=0;j<20;j++)
        {
            miscare5((dx),(dy),(dz));dx+=x*0.05;dy+=y*0.05;dz+=z*0.05;
        }

    }
    private void ecuatie(double x1,double y1,double z1,double x2,double y2,double z2)
    {
        if(x1==x2 && y1==y2)
        {
            if(z1>z2)
            {
                double aux=z1;
                z1=z2;
                z2=aux;
            }
            for(double t=z1;t<=z2;t+=1)
                miscare1(x1,y1,t);
        }
        else
        if(x1==x2 && z1==z2)
        {
            if(y1>y2)
            {
                double aux=y1;
                y1=y2;
                y2=aux;
            }
            for(double t=y1;t<=y2;t+=1)
                miscare1(x1,t,z1);
        }
        else
        if(y1==y2 && z1==z2)
        {
            if(x1>x2)
            {
                double aux=x1;
                x1=x2;
                x2=aux;
            }
            for(double t=x1;t<=x2;t+=1)
                miscare1(t,y1,z1);
        }
    }
    private void miscare(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.GOLD);

        formaPunct(x, y, z, dotColor);
    }

    private void formaPunct(double x, double y, double z, PhongMaterial dotColor) {
        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(1);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);

        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }

    private void miscare1(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.WHITE);

        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(0.25);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);

        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }
    private void miscare2(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.WHITE);

        formaPunct(x, y, z, dotColor);
    }
    private void miscare7(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.BLUE);

        Point(x, y, z, dotColor);
    }

    private void Point(double x, double y, double z, PhongMaterial dotColor) {
        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(2);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);

        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }

    private void Point1(double x, double y, double z, PhongMaterial dotColor) {
        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(3);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);

        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }

    private void miscare4(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.WHITE);

        Xform formaPunct = new Xform();
        Point1(x, y, z, dotColor, formaPunct);
    }

    private void Point1(double x, double y, double z, PhongMaterial dotColor, Xform formaPunct) {
        Sphere punct = new Sphere(2);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);
        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.020));
        pause.setOnFinished(_ -> formaPunct.getChildren().remove(punct));
        pause.play();
    }

    private void miscare6(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.RED);

        Xform formaPunct = new Xform();
        Point1(x, y, z, dotColor, formaPunct);
    }
    private void miscare5(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.RED);

        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(0.2);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);
        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.020));
        pause.setOnFinished(_ -> formaPunct.getChildren().remove(punct));
        pause.play();
    }
    private void miscare8(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.RED);

        Xform formaPunct = new Xform();
        Sphere punct = new Sphere(0.2);
        punct.setMaterial(dotColor);
        punct.setTranslateX(x);
        punct.setTranslateY(y);
        punct.setTranslateZ(z);
        formaPunct.getChildren().add(punct);
        world.getChildren().addAll(formaPunct);
    }
    private void miscare9(double x, double y, double z){

        final PhongMaterial dotColor = new PhongMaterial();
        dotColor.setDiffuseColor(Color.RED);

        Point1(x, y, z, dotColor);
    }

    private void buildPct() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        Xform pctXform = new Xform();
        Xform pct1Xform = new Xform();

        Sphere pct1Sphere = new Sphere(10);
        pct1Sphere.setMaterial(redMaterial);

        pctXform.getChildren().add(pct1Xform);
        pct1Xform.getChildren().add(pct1Sphere);

        dotGroup.getChildren().add(pctXform);
        //Calculate the needed period of one oscillation
        double T=2*PI/o;
        double deltaT=T/500;
        //The sample represent maximum of the period
        int sample=(int)(T/deltaT);

        final int[] i = {0};
        final double[] angle1 = { 0.0 };
        final double[] angle2 = { 0.0 };
        final double[] angle3 = { 0.0 };

        //We initialize a timer so that we can see how the elipse is generating

        AnimationTimer timer = new AnimationTimer() {

            private long lastUpdate = 0;
            double t=0;

            public void handle(long now) {
                if (now - lastUpdate >= 16_000_000) { // roughly 60 FPS
                    t= i[0] *deltaT;

                    //Calculation of angles
                    angle1[0] =phi1+o*t;
                    angle2[0] =phi2+o*t;
                    angle3[0] =phi3+o*t;

                    //Calculation of xi,yj,zk for the function
                    double x = ec(A1, angle1[0]);
                    double y = ec(A2, angle2[0]);
                    double z = ec(A3, angle3[0]);

                    //Representation of it
                    miscare(x,y,z);

                    if(i[0] >=sample && o!=0)
                    {
                        //Creating the semi axes
                        double teta;
                        double a,b;

                        a=((A1*A1*sin(2*phi1))+(A2*A2*sin(2*phi2))+(A3*A3*sin(2*phi3)));
                        b=((A1*A1*cos(2*phi1))+(A2*A2*cos(2*phi2))+(A3*A3*cos(2*phi3)));

                        teta=atan2(a,-b);
                        teta=-(teta/2);
                        //System.out.println(teta);

                        double x2 = A1 * sin(phi1 + teta);
                        double y2 = A2 * sin(phi2 + teta);
                        double z2 = A3 * sin(phi3 + teta);
                        miscare9(x2, y2, z2);

                        double x3 = A1 * cos(phi1 + teta);
                        double y3 = A2 * cos(phi2 + teta);
                        double z3 = A3 * cos(phi3 + teta);
                        miscare9(x3, y3, z3);

                        pct(x2, y2, z2);
                        pct(x3, y3, z3);

                        //Creating the points that intersects with the parallelepiped
                        miscare7(A1,A2*sin(o*((PI/2-phi1)/o)+phi2),A3*sin(o*((PI/2-phi1)/o)+phi3));
                        double v = o * ((3 * PI / 2 - phi1) / o);
                        miscare7(-A1,A2*sin(v +phi2),A3*sin(v +phi3));

                        miscare7(A1*sin(o*((PI/2-phi2)/o)+phi1),A2,A3*sin(o*((PI/2-phi2)/o)+phi3));
                        double v1 = o * ((3 * PI / 2 - phi2) / o);
                        miscare7(A1*sin(v1 +phi1),-A2,A3*sin(v1 +phi3));

                        miscare7(A1*sin(o*((PI/2-phi3)/o)+phi1),A2*sin(o*((PI/2-phi3)/o)+phi2),A3);
                        double v2 = o * ((3 * PI / 2 - phi3) / o);
                        miscare7(A1*sin(v2 +phi1),A2*sin(v2 +phi2),-A3);
                        stop();
                    }
                    i[0]++;
                    lastUpdate = now;
                }
            }

        };

        //We initialize another timer so that we can see the point that is creating the Whole elipse even after it stops to generate(after one period)
        AnimationTimer timer1 = new AnimationTimer() {

            private long lastUpdate = 0;
            double t=0;
            int j=0;
            int k=0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_000_000) { // roughly 60 FPS

                    t= j *deltaT;

                    angle1[0] =phi1+o*t;
                    angle2[0] =phi2+o*t;
                    angle3[0] =phi3+o*t;

                    double x = ec(A1, angle1[0]);
                    double y = ec(A2, angle2[0]);
                    double z = ec(A3, angle3[0]);

                    if(j >=sample)
                    {
                        j=0;
                        k++;
                    }
                    miscare4(x,y,z);

                    if(k==3)stop();
                    //Representing the Speed vector and the Position vector
                    vectorViteza(x,y,z, o,j,deltaT);
                    j++;

                    lastUpdate = now;
                }
            }
        };


        //Creating the parallelepiped`

        miscare2(A1,A2,A3);

        miscare2(A1,A2,-A3);
        miscare2(A1,-A2,A3);
        miscare2(-A1,A2,A3);
        miscare2(A1,-A2,-A3);
        miscare2(-A1,A2,-A3);
        miscare2(-A1,-A2,A3);
        miscare2(-A1,-A2,-A3);

        ecuatie(-A1,-A2,A3,A1,-A2,A3);
        ecuatie(-A1,-A2,A3,-A1,-A2,-A3);
        ecuatie(-A1,-A2,A3,-A1,A2,A3);

        ecuatie(A1,A2,A3,-A1,A2,A3);
        ecuatie(A1,A2,A3,A1,-A2,A3);
        ecuatie(A1,A2,A3,A1,A2,-A3);

        ecuatie(A1,-A2,-A3,A1,-A2,A3);
        ecuatie(A1,-A2,-A3,-A1,-A2,-A3);
        ecuatie(A1,-A2,-A3,A1,A2,-A3);

        ecuatie(-A1,A2,-A3,A1,A2,-A3);
        ecuatie(-A1,A2,-A3,-A1,-A2,-A3);
        ecuatie(-A1,A2,-A3,-A1,A2,A3);

        //Starting the timers
        timer1.start();
        timer.start();
    }

    private void pct(double x2, double y2, double z2) {
        double x1;
        double y1;
        double z1;
        double dx;
        double dy;
        double dz;

        x1= x2;
        y1= y2;
        z1= z2;

        //Creating a vector with a maximum of 20 points
        dx=x1*0.05;
        dy=y1*0.05;
        dz=z1*0.05;
        for(double j=0;j<20;j++)
        {
            miscare8((dx),(dy),(dz));dx+=x1*0.05;dy+=y1*0.05;dz+=z1*0.05;
        }
    }

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {
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
        });
    }
    public void start(Stage primaryStage) {

        System.out.println("start()");

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        //Representing the X,Y,Z axes with they re names
        Text x = new Text("X");
        x.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
        x.setFont(Font.font(20));
        x.setFill(Color.WHITE);
        x.setTranslateX(100);
        x.setTranslateY(0);
        x.setTranslateZ(0);
        root.getChildren().add(x);

        Text y = new Text("Y");
        y.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
        y.setFont(Font.font(20));
        y.setFill(Color.WHITE);
        y.setTranslateX(0);
        y.setTranslateY(100);
        y.setTranslateZ(0);
        root.getChildren().add(y);

        Text z = new Text("Z");
        z.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
        z.setFont(Font.font(20));
        z.setFill(Color.WHITE);
        z.setTranslateX(0);
        z.setTranslateY(0);
        z.setTranslateZ(100);
        root.getChildren().add(z);

        //Representing the Corners of the parallelepiped when some action are applied,like an amplitude is missing or two of them
        if(A1==0 || A2==0 || A3==0) {
            if (A1 == 0 && A2!=0 && A3!=0) {
                Text P4 = new Text(String.format("(%.2f %.2f)",A2,A3));
                P4.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P4.setFont(Font.font(7));
                P4.setFill(Color.WHITE);
                P4.setTranslateX(-A1);
                P4.setTranslateY(A2);
                P4.setTranslateZ(A3);
                root.getChildren().add(P4);


                Text P6 = new Text(String.format("(%.2f %.2f)",A2,-A3));
                P6.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P6.setFont(Font.font(7));
                P6.setFill(Color.WHITE);
                P6.setTranslateX(-A1);
                P6.setTranslateY(A2);
                P6.setTranslateZ(-A3);
                root.getChildren().add(P6);

                Text P7 = new Text(String.format("(%.2f %.2f)",-A2,A3));
                P7.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P7.setFont(Font.font(7));
                P7.setFill(Color.WHITE);
                P7.setTranslateX(-A1);
                P7.setTranslateY(-A2-90);
                P7.setTranslateZ(A3);
                root.getChildren().add(P7);

                Text P8 = new Text(String.format("(%.2f %.2f)",-A2,-A3));
                P8.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P8.setFont(Font.font(7));
                P8.setFill(Color.WHITE);
                P8.setTranslateX(-A1);
                P8.setTranslateY(-A2-90);
                P8.setTranslateZ(-A3);
                root.getChildren().add(P8);
            }
            if (A2 == 0 && A1!=0 && A3!=0) {
                Text P1 = new Text(String.format("(%.2f %.2f)",A1,A3));
                P1.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P1.setFont(Font.font(7));
                P1.setFill(Color.WHITE);
                P1.setTranslateX(A1);
                P1.setTranslateY(A2);
                P1.setTranslateZ(A3);
                root.getChildren().add(P1);

                Text P2 = new Text(String.format("(%.2f %.2f)",A1,-A3));
                P2.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P2.setFont(Font.font(7));
                P2.setFill(Color.WHITE);
                P2.setTranslateX(A1);
                P2.setTranslateY(A2);
                P2.setTranslateZ(-A3);
                root.getChildren().add(P2);

                Text P3 = new Text(String.format("(%.2f %.2f)",A1,A3));
                P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P3.setFont(Font.font(7));
                P3.setFill(Color.WHITE);
                P3.setTranslateX(-A1);
                P3.setTranslateY(-A2);
                P3.setTranslateZ(A3);
                root.getChildren().add(P3);

                Text P5 = new Text(String.format("(%.2f %.2f)",-A2,-A3));
                P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P5.setFont(Font.font(7));
                P5.setFill(Color.WHITE);
                P5.setTranslateX(-A1);
                P5.setTranslateY(-A2);
                P5.setTranslateZ(-A3);
                root.getChildren().add(P5);
            }
            if (A3 == 0 && A1!=0 && A2!=0) {
                Text P1 = new Text(String.format("(%.2f %.2f)",A1,A2));
                P1.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P1.setFont(Font.font(7));
                P1.setFill(Color.WHITE);
                P1.setTranslateX(A1);
                P1.setTranslateY(A2);
                P1.setTranslateZ(A3);
                root.getChildren().add(P1);

                Text P2 = new Text(String.format("(%.2f %.2f)",A1,-A2));
                P2.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P2.setFont(Font.font(7));
                P2.setFill(Color.WHITE);
                P2.setTranslateX(A1);
                P2.setTranslateY(-A2-90);
                P2.setTranslateZ(-A3);
                root.getChildren().add(P2);

                Text P3 = new Text(String.format("(%.2f %.2f)",-A1,A2));
                P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P3.setFont(Font.font(7));
                P3.setFill(Color.WHITE);
                P3.setTranslateX(-A1);
                P3.setTranslateY(A2);
                P3.setTranslateZ(A3);
                root.getChildren().add(P3);

                Text P5 = new Text(String.format("(%.2f %.2f)",-A1,-A2));
                P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P5.setFont(Font.font(7));
                P5.setFill(Color.WHITE);
                P5.setTranslateX(-A1);
                P5.setTranslateY(-A2-90);
                P5.setTranslateZ(-A3);
                root.getChildren().add(P5);
            }
            if(A1==0 && A2==0 && A3!=0)
            {
                Text P3 = new Text(String.format("(%.2f)",A3));
                P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P3.setFont(Font.font(7));
                P3.setFill(Color.WHITE);
                P3.setTranslateX(-A1);
                P3.setTranslateY(A2);
                P3.setTranslateZ(A3);
                root.getChildren().add(P3);

                Text P5 = new Text(String.format("(%.2f)",-A3));
                P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P5.setFont(Font.font(7));
                P5.setFill(Color.WHITE);
                P5.setTranslateX(-A1);
                P5.setTranslateY(-A2);
                P5.setTranslateZ(-A3-90);
                root.getChildren().add(P5);
            }
            if(A1==0 && A3==0 && A2!=0)
            {
                Text P3 = new Text(String.format("(%.2f)",A2));
                P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P3.setFont(Font.font(7));
                P3.setFill(Color.WHITE);
                P3.setTranslateX(-A1);
                P3.setTranslateY(A2);
                P3.setTranslateZ(A3);
                root.getChildren().add(P3);

                Text P5 = new Text(String.format("(%.2f)",-A2));
                P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P5.setFont(Font.font(7));
                P5.setFill(Color.WHITE);
                P5.setTranslateX(-A1);
                P5.setTranslateY(-A2-90);
                P5.setTranslateZ(-A3);
                root.getChildren().add(P5);
            }
            if(A2==0 && A3==0 && A1!=0)
            {
                Text P3 = new Text(String.format("(%.2f)",A1));
                P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P3.setFont(Font.font(7));
                P3.setFill(Color.WHITE);
                P3.setTranslateX(A1);
                P3.setTranslateY(A2);
                P3.setTranslateZ(A3);
                root.getChildren().add(P3);

                Text P5 = new Text(String.format("(%.2f)",-A1));
                P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
                P5.setFont(Font.font(7));
                P5.setFill(Color.WHITE);
                P5.setTranslateX(-A1);
                P5.setTranslateY(-A2);
                P5.setTranslateZ(-A3);
                root.getChildren().add(P5);
            }
        }
        else {
            Text P4 = new Text(String.format("(%.2f %.2f %.2f)",-A1,A2,A3));
            P4.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(270,Rotate.X_AXIS));
            P4.setFont(Font.font(10));
            P4.setFill(Color.WHITE);
            P4.setTranslateX(-A1);
            P4.setTranslateY(A2);
            P4.setTranslateZ(A3);
            root.getChildren().add(P4);


            Text P6 = new Text(String.format("(%.2f %.2f %.2f)",-A1,A2,-A3));
            P6.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(270,Rotate.X_AXIS));
            P6.setFont(Font.font(10));
            P6.setFill(Color.WHITE);
            P6.setTranslateX(-A1);
            P6.setTranslateY(A2);
            P6.setTranslateZ(-A3-5);
            root.getChildren().add(P6);

            Text P7 = new Text(String.format("(%.2f %.2f %.2f)",-A1,-A2,A3));
            P7.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(270,Rotate.X_AXIS));
            P7.setFont(Font.font(10));
            P7.setFill(Color.WHITE);
            P7.setTranslateX(-A1);
            P7.setTranslateY(-A2-90);
            P7.setTranslateZ(A3);
            root.getChildren().add(P7);

            Text P8 = new Text(String.format("(%.2f %.2f %.2f)",-A1,-A2,-A3));
            P8.getTransforms().addAll(new Rotate(90,Rotate.Z_AXIS),new Rotate(270,Rotate.X_AXIS));
            P8.setFont(Font.font(10));
            P8.setFill(Color.WHITE);
            P8.setTranslateX(-A1);
            P8.setTranslateY(-A2-90);
            P8.setTranslateZ(-A3-5);
            root.getChildren().add(P8);

            Text P1 = new Text(String.format("(%.2f %.2f %.2f)",A1,A2,A3));
            P1.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
            P1.setFont(Font.font(10));
            P1.setFill(Color.WHITE);
            P1.setTranslateX(A1);
            P1.setTranslateY(A2);
            P1.setTranslateZ(A3);
            root.getChildren().add(P1);

            Text P2 = new Text(String.format("(%.2f %.2f %.2f)",A1,A2,-A3));
            P2.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
            P2.setFont(Font.font(10));
            P2.smoothProperty();
            P2.setFill(Color.WHITE);
            P2.setTranslateX(A1);
            P2.setTranslateY(A2);
            P2.setTranslateZ(-A3-5);
            root.getChildren().add(P2);

            Text P3 = new Text(String.format("(%.2f %.2f %.2f)",A1,-A2,A3));
            P3.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
            P3.setFont(Font.font(10));
            P3.setFill(Color.WHITE);
            P3.setTranslateX(A1);
            P3.setTranslateY(-A2-90);
            P3.setTranslateZ(A3);
            root.getChildren().add(P3);

            Text P5 = new Text(String.format("(%.2f %.2f %.2f)",A1,-A2,-A3));
            P5.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Rotate(270, Rotate.X_AXIS));
            P5.setFont(Font.font(10));
            P5.setFill(Color.WHITE);
            P5.setTranslateX(A1);
            P5.setTranslateY(-A2-90);
            P5.setTranslateZ(-A3-5);
            root.getChildren().add(P5);
        }


        buildCamera();
        buildAxes();

        //Creating the scene where the elipse is generating
        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.BLACK);
        handleKeyboard(scene);
        handleMouse(scene);

        primaryStage.setTitle("Instanta");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(camera);
        buildPct();

    }

    public static void main(String[] args) {

        Thread t2 = new Thread(() -> MyFrame.main(args));
        t2.start();
    }
}