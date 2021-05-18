"use strict";
class Camera {

    constructor(canvas,
                currentlyPressedKeys,
                camPosX=42,
                camPosY=42,
                camPosZ=120,
                lookAtX=0,
                lookAtY=0,
                lookAtZ=0,
                upX=0,
                upY=1,
                upZ=0) {

        this.canvas = canvas;
        this.currentlyPressedKeys = currentlyPressedKeys;

        // Camera position
        this.camPosX = camPosX;
        this.camPosY = camPosY;
        this.camPosZ = camPosZ;

        // Camera target
        this.lookAtX = lookAtX;
        this.lookAtY = lookAtY;
        this.lookAtZ = lookAtZ;

        // Camera orientation
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;

        // Camera settings
        this.near = 0.1;
        this.far = 10000;
        this.fov = 45;

        this.viewMatrix = new Matrix4();
        this.projectionMatrix = new Matrix4();
    }

    setCamera() {
        // Where to look
        this.viewMatrix.setLookAt(this.camPosX, this.camPosY, this.camPosZ, this.lookAtX, this.lookAtY, this.lookAtZ, this.upX, this.upY, this.upZ);
        // How it should look
        this.projectionMatrix.setPerspective(this.fov, this.canvas.width / this.canvas.height, this.near, this.far);
    }

    getModelViewMatrix(modelMatrix) {
        return new Matrix4(this.viewMatrix.multiply(modelMatrix)); // NB! rekkefølge!
    }

    setPosition(posX, posY, posZ) {
        this.camPosX = posX;
        this.camPosY = posY;
        this.camPosZ = posZ;
    }

    setLookAt(lookX, lookY, lookZ) {
        this.lookAtX = lookX;
        this.lookAtY = lookY;
        this.lookAtZ = lookZ;
    }

    setUp(upX, upY, upZ) {
        this.lookAtX = upX;
        this.lookAtY = upY;
        this.lookAtZ = upZ;
    }

    setNear(near) {
        this.near = near;
    }

    setFar(far) {
        this.far = far;
    }

    setFOV(fov) {
        this.fov = fov;
    }

    handleKeys(elapsed) {
        let camPosVec = vec3.fromValues(this.camPosX, this.camPosY, this.camPosZ);
        // Simple camera-controls
        if (this.currentlyPressedKeys["KeyD"]) {
            rotateVector(2, camPosVec, 0, 1, 0);
        }
        if (this.currentlyPressedKeys["KeyA"]) {
            rotateVector(-2, camPosVec, 0, 1, 0);
        }
        if (this.currentlyPressedKeys["KeyS"]) {
            rotateVector(2, camPosVec, 1, 0, 0);
        }
        if (this.currentlyPressedKeys["KeyW"]) {
            rotateVector(-2, camPosVec, 1, 0, 0);
        }
        /* Repurpose for turning
        if (this.currentlyPressedKeys["KeyF"]) {
            if(moveDeg<=-45)
                moveForward=true;
            if(moveDeg>=45)
                moveForward=false;

            //FPS-independent animation-steps
            if(moveForward){
                moveDeg+=135*timeElapsed;
            } else moveDeg-=135*timeElapsed;
        }
        */

        /*/ Enable wireframe - Repurpose?
        if (this.currentlyPressedKeys["Space"]) {
            if(drawMode === gl.TRIANGLE_STRIP)
                drawMode = gl.LINES;
            else drawMode = gl.TRIANGLE_STRIP;
        }
        */

        // Zoom
        if (this.currentlyPressedKeys["KeyZ"]) {
            vec3.scale(camPosVec, camPosVec, 1.05);
        }
        if (this.currentlyPressedKeys["KeyX"]) {
            vec3.scale(camPosVec, camPosVec, 0.95);
        }

        this.camPosX = camPosVec[0];
        this.camPosY = camPosVec[1];
        this.camPosZ = camPosVec[2];

        this.setCamera();
    }
}


