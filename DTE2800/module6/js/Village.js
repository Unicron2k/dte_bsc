// Contains code sourced from Werner Farstad
"use strict";
class Application{
    constructor() {
        this.gl = null;
        this.canvas = null;

        this.currentlyPressedKeys = [];

        this.lastTime = 0.0;
        this.fpsData = {};
        this.selectedCar = 1;

        //initial lighting variables
        this.lights = {
            lightPosition: [0.0, 5.0, 0.0],
            diffuseLightColor: [1.0, 1.0, 1.0],
            ambientLightColor: [0.1, 0.1, 0.1],
            lightDirection: [1.0, 0.0, 0.0],
            specularLightColor: [0.1, 0.8, 0.3]
        };
    }

    start() {
        this.initContext();

        // Retrieve shaders
        // Coord-shader (fra html-fila):
        let coordVertexShaderSource = document.getElementById("coord-vertex-shader").innerHTML;
        let coordFragmentShaderSource = document.getElementById("coord-fragment-shader").innerHTML;
        this.gl.coordShaderProgram = createProgram(this.gl, coordVertexShaderSource, coordFragmentShaderSource);
        if (!this.gl.coordShaderProgram) {
            console.log('Error initializing coordinate-system-shaders!');
            return;
        }
        // xzplane-shader: BRUK ENTEN xzplane-gourad-vertex-shader ELLER xzplane-phong-vertex-shader
        let phongVertexShaderSource = document.getElementById("phong-vertex-shader").innerHTML;
        let phongFragmentShaderSource = document.getElementById("phong-fragment-shader").innerHTML;
        this.gl.phongShaderProgram = createProgram(this.gl, phongVertexShaderSource, phongFragmentShaderSource);
        if (!this.gl.phongShaderProgram) {
            console.log('Error initializing XZ-plane-shaders!');
            return;
        }
        // LightSource - shader:
        let lightSourceVertexShaderSource = document.getElementById("light-source-vertex-shader").innerHTML;
        let lightSourceFragmentShaderSource = document.getElementById("light-source-fragment-shader").innerHTML;
        this.gl.lightSourceShaderProgram = createProgram(this.gl, lightSourceVertexShaderSource, lightSourceFragmentShaderSource);
        if (!this.gl.lightSourceShaderProgram) {
            console.log('Error initializing light-source-shaders!');
            return;
        }

        //Enable backface-culling
        this.gl.frontFace(this.gl.CCW);
        this.gl.enable(this.gl.CULL_FACE);
        this.gl.cullFace(this.gl.BACK);
        //

        //Enables depth testing
        this.gl.enable(this.gl.DEPTH_TEST);
        this.gl.depthFunc(this.gl.LESS);

        //Enable alpha blending
        this.gl.enable(this.gl.BLEND);
        this.gl.blendFunc(this.gl.SRC_ALPHA, this.gl.ONE_MINUS_SRC_ALPHA);

        // Init camera:
        this.camera = new Camera(this.canvas, this.currentlyPressedKeys);
        this.camera.setCamera();

        // init coordinate-grid:
        this.coord = new Coord(this.gl, this.camera);
        this.coord.initBuffers();

        // init XZPlane:
        this.xzplane = new XZPlane(this.gl, this.camera, 50);
        this.xzplane.initBuffers();

        this.lightSourceIndicator = new LightSourceIndicator(this.gl, this.camera);
        this.lightSourceIndicator.initLightSourceBuffer();


        this.house1 = new House(this.gl, this.camera, "textures/houseTexture1.png", 9,14,0)
        this.house1.initBuffers();

        this.house2 = new House(this.gl, this.camera, "textures/houseTexture2.png", 6,-23,90)
        this.house2.initBuffers();

        this.house3 = new House(this.gl, this.camera, "textures/houseTexture1.png", 28,-15,0)
        this.house3.initBuffers();

        this.house4 = new House(this.gl, this.camera, "textures/houseTexture1.png", -23,-40,90,2)
        this.house4.initBuffers();

        this.house5 = new House(this.gl, this.camera, "textures/houseTexture2.png", -23,-15,90)
        this.house5.initBuffers();

        this.house6 = new House(this.gl, this.camera, "textures/houseTexture2.png", -23,15,90)
        this.house6.initBuffers();

        this.house7 = new House(this.gl, this.camera, "textures/houseTexture1.png", -23,40,90, 2.5)
        this.house7.initBuffers();

        this.house8 = new House(this.gl, this.camera, "textures/houseTexture2.png", 34,14,0,2.5)
        this.house8.initBuffers();

        this.house9 = new House(this.gl, this.camera, "textures/houseTexture1.png", 6,35,90,2)
        this.house9.initBuffers();

        this.car1 = new Car(this.gl, this.camera, "textures/carTexture1.png", 3,-5,180);
        this.car1.initBuffers();

        this.car2 = new Car(this.gl, this.camera, "textures/carTexture2.png", -5, 40, 90);
        this.car2.initBuffers();

        this.car3 = new Car(this.gl, this.camera, "textures/carTexture1.png", -13,-25,-90);
        this.car3.initBuffers();

        this.car4 = new Car(this.gl, this.camera, "textures/carTexture.png", -25,5,15);
        this.car4.initBuffers();



        // Background color
        this.gl.clearColor(0, 0, 0, 1.0); //RGBA

        // init fps-data
        this.fpsData.numFrames = 0;
        this.fpsData.prevTimeStamp = 0;


        document.getElementById("instructions").innerHTML =
            "FPS: <span id=\"fps\">--</span><br>"+
            "Controlls:</br></br>"+
            "W/A/S/D: Move camera-position:</br>"+
            "Z/X: Zoom out/in </br>"+
            "I/J/K/L: Move X/Z-position of point-light</br>"+
            "M/,: Move Y-position of point-light</br>"+
            "U/O: Rotate directional light left/right (similar to camera)</br>"+
            "Y/H: Rotate directional light up/down (similar to camera)</br>"+
            "1/2/3/4: Select car</br>"+
            "Q/E: Steer wheels of selected car</br>"+
            "R/F: Increase/decrease ambient lighting</br>"+
            "</p>"


        // Start main loop
        this.mainLoop();
    }

    initContext() {
        this.canvas = document.getElementById("webgl");
        this.gl = getWebGLContext(this.canvas);
        if (!this.gl) {
            console.log("Could not retrieve rendering context for WebGL");
            return false;
        }
        this.gl.viewport(0,0,this.canvas.width,this.canvas.height);

        // Bind key-handlers to document
        document.addEventListener('keyup', this.handleKeyUp.bind(this), false);
        document.addEventListener('keydown', this.handleKeyDown.bind(this), false);
    }

    handleKeyUp(event) {
        if (event.defaultPrevented) return;
        this.currentlyPressedKeys[event.code] = false;
        event.preventDefault();
    }

    handleKeyDown(event) {
        if (event.defaultPrevented) return;
        this.currentlyPressedKeys[event.code] = true;
        event.preventDefault();
    }

    handleLights() {
        let lightDirVec = vec3.fromValues(this.lights.lightDirection[0], this.lights.lightDirection[1], this.lights.lightDirection[2]);


        //Move point light
        if (this.currentlyPressedKeys["KeyL"]) {
            this.lights.lightPosition[0] += 1;
        }
        if (this.currentlyPressedKeys["KeyJ"]) {
            this.lights.lightPosition[0] -= 1;
        }
        if (this.currentlyPressedKeys["KeyK"]) {
            this.lights.lightPosition[2] += 1;
        }
        if (this.currentlyPressedKeys["KeyI"]) {
            this.lights.lightPosition[2] -= 1;
        }
        if (this.currentlyPressedKeys["KeyM"]) {
            this.lights.lightPosition[1] += 1;
        }
        if (this.currentlyPressedKeys["Comma"]) {
            this.lights.lightPosition[1] -= 1;
        }
        if (this.currentlyPressedKeys["KeyR"] && this.lights.ambientLightColor[0]<1) {
            this.lights.ambientLightColor[0] += 0.01;
            this.lights.ambientLightColor[1] += 0.01;
            this.lights.ambientLightColor[2] += 0.01;
        }
        if (this.currentlyPressedKeys["KeyF"] && this.lights.ambientLightColor[0]>=0) {
            this.lights.ambientLightColor[0] -= 0.01;
            this.lights.ambientLightColor[1] -= 0.01;
            this.lights.ambientLightColor[2] -= 0.01;
        }

        //rotate light direction
        if (this.currentlyPressedKeys["KeyO"]) {
            rotateVector(2, lightDirVec, 0, 1, 0);
        }
        if (this.currentlyPressedKeys["KeyU"]) {
            rotateVector(-2, lightDirVec, 0, 1, 0);
        }
        if (this.currentlyPressedKeys["KeyH"]) {
            rotateVector(2, lightDirVec, 1, 0, 0);
        }
        if (this.currentlyPressedKeys["KeyY"]) {
            rotateVector(-2, lightDirVec, 1, 0, 0);
        }
        this.lights.lightDirection[0] = lightDirVec[0];
        this.lights.lightDirection[1] = lightDirVec[1];
        this.lights.lightDirection[2] = lightDirVec[2];

        this.camera.setLookAt( this.lights.lightPosition[0], this.lights.lightPosition[1], this.lights.lightPosition[2])

    }

    handleCars(elapsed){

        if (this.currentlyPressedKeys["Digit1"]) {
            this.selectedCar=1
        }
        if (this.currentlyPressedKeys["Digit2"]) {
            this.selectedCar=2
        }
        if (this.currentlyPressedKeys["Digit3"]) {
            this.selectedCar=3
        }
        if (this.currentlyPressedKeys["Digit4"]) {
            this.selectedCar=4
        }

        switch (this.selectedCar){
            case 1:
                this.car1.handleKeys(elapsed, this.currentlyPressedKeys);
                break;
            case 2:
                this.car2.handleKeys(elapsed, this.currentlyPressedKeys);
                break;
            case 3:
                this.car3.handleKeys(elapsed, this.currentlyPressedKeys);
                break;
            case 4:
                this.car4.handleKeys(elapsed, this.currentlyPressedKeys);
                break;
            default:
                break;
        }
    }

    handleKeys(elapsed) {
        this.camera.handleKeys(elapsed, this.currentlyPressedKeys);
        this.handleLights();
        this.handleCars();
    }

    mainLoop(currentTime) {
        if (currentTime === undefined)
            currentTime = 0;

        //Updates the frame-count once per second
        if (currentTime - this.fpsData.prevTimeStamp >= 1000) {
            document.getElementById("fps").innerHTML = this.fpsData.numFrames;
            this.fpsData.numFrames = 0;
            this.fpsData.prevTimeStamp = currentTime;
        }

        // Increases frame-count by 1 for each re-draw
        let elapsed = 0.0;
        if (this.lastTime !== 0.0)
            elapsed = (currentTime - this.lastTime)/1000;
        this.lastTime = currentTime;
        this.fpsData.numFrames++;

        // Clear screen
        this.gl.clear(this.gl.COLOR_BUFFER_BIT);

        // Draw
        this.coord.draw(elapsed);
        this.xzplane.draw(elapsed, this.lights);
        this.lightSourceIndicator.draw(elapsed, this.lights);


        this.house1.draw(elapsed, this.lights);
        this.house2.draw(elapsed, this.lights);
        this.house3.draw(elapsed, this.lights);
        this.house4.draw(elapsed, this.lights);
        this.house5.draw(elapsed, this.lights);
        this.house6.draw(elapsed, this.lights);
        this.house7.draw(elapsed, this.lights);
        this.house8.draw(elapsed, this.lights);
        this.house9.draw(elapsed, this.lights);

        this.car1.draw(elapsed, this.lights);
        this.car2.draw(elapsed, this.lights);
        this.car3.draw(elapsed, this.lights);
        this.car4.draw(elapsed, this.lights);


        // handle user input
        this.handleKeys(elapsed);

        // Request redraw
        requestAnimFrame(this.mainLoop.bind(this));
    }

}