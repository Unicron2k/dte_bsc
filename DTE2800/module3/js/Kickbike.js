"use strict";
class Kickbike {

    constructor(gl, camera) {
        this.gl = gl;
        this.camera = camera;

        this.stack = new Stack();

        this.cube = null;
        this.cylinder = null;
        this.cylinderColor = null;
        this.cylinderColor2 = null;

        this.steeringRot = 0;
    }

    initBuffers() {
        this.cube = new Cube(this.gl, this.camera, null);
        this.cube.initBuffers();
        this.cylinder = new Cylinder(this.gl, this.camera, null);
        this.cylinder.initBuffers();
        this.cylinderColor = new Cylinder(this.gl, this.camera, {red:0.4,green:0.2,blue:0.7,alpha:1.0});
        this.cylinderColor.initBuffers();
        this.cylinderColor2 = new Cylinder(this.gl, this.camera, {red:0.4,green:0.2,blue:0.7,alpha:1.0});
        this.cylinderColor2.initBuffers();
    }

    handleKeys(elapsed, currentlyPressedKeys) {
        if (currentlyPressedKeys["KeyQ"] && !(this.steeringRot>=45)) {
            this.steeringRot+=1;
        }
        if (currentlyPressedKeys["KeyE"] && !(this.steeringRot<=-45)) {
            this.steeringRot-=1;
        }
    }

    draw(elapsed, modelMatrix) {
        document.getElementById("angle").innerHTML = this.steeringRot;

        // Base-modelmatrix
        this.stack.pushMatrix(modelMatrix);
        modelMatrix.translate(0, 0, 0);
        modelMatrix.scale(1,1,1);

        // Modelmatrix for chassis and rear wheel
        this.stack.pushMatrix(modelMatrix);

        // I*T*O*R*S  der O = R * T

        // Main Body
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.translate(0,0,0);
        modelMatrix.scale(19,1.0,4);
        this.cube.draw(elapsed, modelMatrix);

        // Steering-linkage/neck
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-19,8, 0);// Where to place it
        modelMatrix.rotate(-60,0, 0, 1); // Which way to orient it
        modelMatrix.scale(6,2,2);
        this.cube.draw(elapsed, modelMatrix);

        // Neck, rear fender
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(14,6, 0);// Where to place it
        modelMatrix.rotate(45,0, 0, 1); // Which way to orient it
        modelMatrix.scale(4,1,3.5);
        this.cube.draw(elapsed, modelMatrix);

        // Top, rear fender
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(20.3,9, 0);// Where to place it
        modelMatrix.rotate(0,0, 0, 1); // Which way to orient it
        modelMatrix.scale(4,1,3.5);
        this.cube.draw(elapsed, modelMatrix);


        // Rear wheel
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(19,3.8, 0);// Where to place it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.scale(3,3.8,3.8);
        this.cylinder.draw(elapsed, modelMatrix);


        // Rear hub
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(19,3.8, 0);// Where to place it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.scale(3.1,2.4,2.4);
        this.cylinderColor.draw(elapsed, modelMatrix);






        // ***Never got this matrix-stack-thing to work as intended....
/*
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.rotate(90, 1, 0, 0);
        modelMatrix.translate(1, 1, 1);
        modelMatrix.rotate(this.steeringRot, 1, 0, 0);
        this.stack.pushMatrix(modelMatrix);
*/




        // front wheel
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-25,3.8, 0);// Where to place it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.rotate(20,1, 0, 0); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,0, 1, 0); // Which way to orient it
        modelMatrix.scale(3,3.8,3.8);
        this.cylinder.draw(elapsed, modelMatrix);


        // front hub
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-25,3.8, 0);// Where to place it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.rotate(20,1, 0, 0); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,0, 1, 0); // Which way to orient it
        modelMatrix.scale(3.1,2.4,2.4);
        this.cylinderColor.draw(elapsed, modelMatrix);


        //Steering shaft
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-17,25.5, 0);// Where to place it
        modelMatrix.rotate(70,0, 0, 1); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,1, 0, 0); // Which way to orient it
        modelMatrix.scale(40,1,1);
        this.cylinder.draw(elapsed, modelMatrix);


        //Handlebar center
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-10.25,44, 0);// Where to place it
        modelMatrix.rotate(-20,0, 0, 1); // Which way to orient it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,0, 1, 0); // Which way to orient it
        modelMatrix.scale(15,1,1);
        this.cylinder.draw(elapsed, modelMatrix);

        //Handlebar-grip left
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-10,44, 0);// Where to place it
        modelMatrix.rotate(-20,0, 0, 1); // Which way to orient it
        modelMatrix.rotate(-90,0, 1, 0); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,0, 1, 0); // Which way to orient it
        modelMatrix.translate(7.5,0, 0); // Which way to orient it
        modelMatrix.scale(7,1.5,1.5);
        this.cylinderColor2.draw(elapsed, modelMatrix);


        //Handlebar-grip left
        modelMatrix = this.stack.peekMatrix();
        modelMatrix.setTranslate(-10,44, 0);// Where to place it
        modelMatrix.rotate(-20,0, 0, 1); // Which way to orient it
        modelMatrix.rotate(90,0, 1, 0); // Which way to orient it
        modelMatrix.rotate(this.steeringRot,0, 1, 0); // Which way to orient it
        modelMatrix.translate(7.5,0, 0); // Which way to orient it
        modelMatrix.scale(7,1.5,1.5);
        this.cylinderColor2.draw(elapsed, modelMatrix);

        // Empty the stack
        this.stack.empty();
    }
}


