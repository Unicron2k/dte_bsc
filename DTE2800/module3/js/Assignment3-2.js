"use strict";
class Application{
    constructor() {
        this.gl = null;
        this.canvas = null;

        this.currentlyPressedKeys = [];

        this.lastTime = 0.0;
        this.fpsData = {};
    }

    start() {
        this.initContext();

        // Retrieve shaders
        let vertexShaderSource = document.getElementById("vertex-shader").innerHTML;
        let fragmentShaderSource = document.getElementById("fragment-shader").innerHTML;
        if (!initShaders(this.gl, vertexShaderSource, fragmentShaderSource)) {
            console.log('Error initializing shaders');
            return;
        }

        //Enable backface-culling
        this.gl.frontFace(this.gl.CW);
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
        this.xzplane = new XZPlane(this.gl, this.camera, this.canvas);
        this.xzplane.initBuffers();


        this.kickbike = new Kickbike(this.gl, this.camera);
        this.kickbike.initBuffers();


        // Background color
        this.gl.clearColor(0.4, 1.0, 1.0, 1.0); //RGBA

        // init fps-data
        this.fpsData.numFrames = 0;
        this.fpsData.prevTimeStamp = 0;


        document.getElementById("instructions").innerHTML =

                "FPS: <span id=\"fps\">--</span><br>"+
                "Steering-angle: <span id=\"angle\">--</span>"+
            "<p>" +
            "Use 'W','A','S','D' to move camera." +
            "<br>" +
            "Press and hold 'Q' or 'E' to turn." +
            "<br>" +
            "Zoom in/out by using 'Z' and 'X'." +
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

    handleKeys(elapsed) {
        this.camera.handleKeys(elapsed, this.currentlyPressedKeys);
        this.kickbike.handleKeys(elapsed, this.currentlyPressedKeys)
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
        this.xzplane.draw(elapsed);

        let modelMatrix = new Matrix4();
        modelMatrix.setIdentity();
        modelMatrix.translate(0, 3.5, 0);
        this.kickbike.draw(elapsed, modelMatrix);

        // handle user input
        this.handleKeys(elapsed);

        // Request redraw
        requestAnimFrame(this.mainLoop.bind(this));
    }
}