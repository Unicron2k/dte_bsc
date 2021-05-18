"use strict";

//<editor-fold desc="Shaders">
// Vertex shader program
let VSHADER_SOURCE =
    'attribute vec3 a_Position;\n' +
    'attribute vec4 a_Color;\n' +
    'varying vec4 v_Color;\n' +
    'uniform mat4 u_modelviewMatrix;\n' +
    'uniform mat4 u_projectionMatrix;\n' +
    'void main() {\n' +
    '  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position,1.0);\n' +
    '  v_Color = a_Color;\n' +
    '}\n';

// Fragment shader program
// If i knew what i was doing, I could create a outline cel-shaded effect
let FSHADER_SOURCE =
    'precision mediump float;\n' +
    'varying vec4 v_Color;\n' +
    'void main() {\n' +
    '  gl_FragColor = v_Color;\n' +
    '}\n';
//</editor-fold>

//<editor-fold desc="Global variables">
// Misc.
let gl = null;
let canvas = null;
let currentlyPressedKeys = [];
let COORD_BOUNDARY = null;
let moveDeg = null;
let moveForward = null;
let drawMode = null;

// buffers:
let cubeVertices= null;
let cubeVertexBuffer = null;
let cubeColors = null
let cubeColorBuffer = null;
let cubeIndices = null;
let cubeIndexBuffer = null;
let coordPositionBuffer = null;
let coordColorBuffer = null
let xzplanePositionBuffer = null;
let xzplaneColorBuffer = null;


// Pointers to Shader-parameters
let u_modelviewMatrix = null;
let u_projectionMatrix = null;
let a_Position = null;
let a_Color = null;

// Matrices
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null;
let projectionMatrix = null;

// Camera-position
let camPosX=null;
let camPosY=null;
let camPosZ=null;
let lookAtX=null;
let lookAtY=null;
let lookAtZ=null;
let upX=null;
let upY=null;
let upZ=null;


// FPS-calculation-variables
let fpsData = null;
let lastTime = null;
let timeElapsed = null;
//</editor-fold>

function main() {

    if (!init()) {
        console.log('Error initializing shaders');
        return;
    }

    // Enter main program-loop
    console.log('AwesomeCam(™), UltiCoord(™) and MegaPlane(™) was definitely not ripped from Werner. Pinky Promise! 👼🏻')
    document.getElementById("instructions").innerHTML =
        "<p>" +
        "Use 'W','A','S','D' to move camera." +
        "<br>" +
        "Press and hold 'F' to walk." +
        "<br>" +
        "Zoom in/out by using 'Z' and 'X'." +
        "<br>" +
        "Enable wireframe-view by pressing 'Space'" +
        "</p>"
    mainLoop();
}

function mainLoop(currentTime) {
    // Clear the color-buffer
    gl.clear(gl.COLOR_BUFFER_BIT);

    timeElapsed = calculateFPS(currentTime);

    handleKeys(timeElapsed);

    gl.enable(gl.BLEND);
    // Angir blandefunksjon:
    gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);


    //Add your drawing-code here
    drawCoordinateGrid();
    drawXZPlane();
    drawCubeMan();


    updateCamera();
    requestAnimFrame(mainLoop);
}

function init() {
    // Hent <canvas> elementet
    canvas = document.getElementById('webgl');

    // Rendering context for WebGL:
    gl = getWebGLContext(canvas);
    if (!gl) {
        console.log('Unable to retrieve rendering-context for WebGL');
        return false;
    }

    modelMatrix = new Matrix4();
    viewMatrix = new Matrix4();
    modelviewMatrix = new Matrix4();
    projectionMatrix = new Matrix4();

    // Camera-position
    camPosX=7;
    camPosY=7;
    camPosZ=20;
    lookAtX=0;
    lookAtY=3;
    lookAtZ=0;
    upX=0;
    upY=1;
    upZ=0;

    // FPS-calculation-variables
    fpsData = {};
    fpsData.numFrames = 0;
    fpsData.prevTime = 0;
    lastTime = 0.0;
    timeElapsed = 0.0;

    //misc.
    COORD_BOUNDARY = 100;
    moveDeg = 0
    moveForward = true;
    drawMode = gl.TRIANGLE_STRIP

    //Enable backface-culling
    gl.frontFace(gl.CW);
    gl.enable(gl.CULL_FACE);
    gl.cullFace(gl.BACK);

    //Enables depth testing
    gl.enable(gl.DEPTH_TEST);
    gl.depthFunc(gl.LESS);

    //Handle camera input keys
    document.addEventListener('keyup', handleKeyUp, false);
    document.addEventListener('keydown', handleKeyDown, false);


    if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
        console.log('Error initializing shaders');
        return;
    }

    // TODO: Add error-checks
    initCubeBuffer();
    initCoordBuffer();
    initXZPlaneBuffer();

    if (!bindShaderParameters()) {
        console.log('Unable to bind shader-parameters')
        return;
    }
    // Setter bakgrunnsfarge:
    gl.clearColor(0.8, 0.8, 0.6, 1.0); //RGBA

    return true;
}

function bindShaderParameters() {
    u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
    u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');
    a_Position = gl.getAttribLocation(gl.program, "a_Position");
    a_Color = gl.getAttribLocation(gl.program, "a_Color");
    return (u_modelviewMatrix!=null || u_projectionMatrix!=null || a_Position!=null || a_Color!=null);
}

function initCubeBuffer() {
    // Vertex-buffer:
    cubeVertices = new Float32Array([
        -1, -1,  1,//FBL 0
        -1,  1,  1,//FTL 1
         1,  1,  1,//FTR 2
         1, -1,  1,//FBR 3

        -1, -1, -1,//BBL 4
        -1,  1, -1,//BTL 5
         1,  1, -1,//BTR 6
         1, -1, -1 //BBR 7

    ]);
    // Bind vertex-buffer:
    cubeVertexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, cubeVertices, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

    // Indexes from the vertex-buffer that defines a cube
    cubeIndices = new Uint16Array([
        // Front
        3,0,2,
        0,2,1,
        // Top
        2,1,6,
        1,6,5,
        // Left
        5,1,4,
        1,4,0,
        // Bottom
        4,0,3,
        3,4,7,
        // Right
        7,3,2,
        2,7,6,
        // Back
        7,6,5,
        5,7,4
    ]);
    // Bind index-buffer:
    cubeIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeIndexBuffer);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, cubeIndices, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    // Color-buffer
    // Using a this color-matrix allows one to more easily see the cuboidal shapes without proper lighting.
    cubeColors = new Float32Array([
        1.0, 0.125, 0.0, 1,
        0.0, 0.250, 1.0, 1,
        1.0, 0.375, 0.0, 1,
        0.0, 0.500, 1.0, 1,
        1.0, 0.625, 0.0, 1,
        0.0, 0.750, 1.0, 1,
        1.0, 0.875, 0.0, 1,
        0.0, 1.000, 1.0, 1,
    ]);
    // Bind color-buffer
    cubeColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, cubeColors, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

function initCoordBuffer(){
    // TODO: Add error-checks
    //Coordinate-grid
    let coordPositions = new Float32Array([
        //X-Axis
        -COORD_BOUNDARY, 0.0, 0.0,
        COORD_BOUNDARY, 0.0, 0.0,

        //Y-Axis
        0.0, COORD_BOUNDARY, 0.0,
        0.0, -COORD_BOUNDARY, 0.0,

        //Z-Axis
        0.0, 0.0, COORD_BOUNDARY,
        0.0, 0.0, -COORD_BOUNDARY,
    ]);
    coordPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, coordPositions, gl.STATIC_DRAW);
    coordPositionBuffer.itemSize = 3;
    coordPositionBuffer.numberOfItems = 6;
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

    var coordColors = new Float32Array([
        1.0, 0.0, 0.0, 1,   // X-axis
        1.0, 0.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,   // Y-axis
        0.0, 1.0, 0.0, 1,
        0.0, 0.0, 1.0, 1,   // Z-axis
        0.0, 0.0, 1.0, 1
    ]);
    coordColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, coordColors, gl.STATIC_DRAW);
    coordColorBuffer.floatsPerColor = 4;
    coordColorBuffer.numColors = 6;
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

}

function initXZPlaneBuffer() {
    // xzplanePosition-buffer:
    let xzplanePositions = new Float32Array([
        COORD_BOUNDARY / 2, 0, COORD_BOUNDARY / 2,
        -COORD_BOUNDARY / 2, 0, COORD_BOUNDARY / 2,
        COORD_BOUNDARY / 2, 0, -COORD_BOUNDARY / 2,
        -COORD_BOUNDARY / 2, 0, -COORD_BOUNDARY / 2,
    ]);
    xzplanePositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, xzplanePositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, xzplanePositions, gl.STATIC_DRAW);
    xzplanePositionBuffer.itemSize = 3;
    xzplanePositionBuffer.numberOfItems = 4;
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

    // xzplaneColor-buffer:
    let xzplaneColors = new Float32Array([
        1, 0.222, 0, 0.6,
        1, 0.222, 0, 0.6,
        1, 0.222, 0, 0.6,
        1, 0.222, 0, 0.6
    ]);
    xzplaneColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, xzplaneColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, xzplaneColors, gl.STATIC_DRAW);
    xzplaneColorBuffer.itemSize = 4;
    xzplaneColorBuffer.numberOfItems = 4;
    gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

function drawCubeMan(){
    // a_Position must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    // a_Color must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeColorBuffer);
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);

    // Re-binds the index-buffer
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeIndexBuffer);

    // Resets the view-matrix for the next draw
    // Right leg
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(-0.5,2.3, 0);// Where to place it
    modelMatrix.rotate(90, 0, 0, 1); // Which way to orient it
    modelMatrix.rotate(moveDeg, 0, 1, 0); // What angle to bend it at
    modelMatrix.translate(-1.1,0, 0); // Where will the bend be
    modelMatrix.scale(1.2,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);

    // Resets the view-matrix for the next draw
    // Left leg
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(0.5,2.3, 0);// Where to place it
    modelMatrix.rotate(90, 0, 0, 1); // Which way to orient it
    modelMatrix.rotate(-moveDeg, 0, 1, 0); // What angle to bend it at
    modelMatrix.translate(-1.1,0, 0); // Where will the bend be
    modelMatrix.scale(1.2,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);

    // Resets the view-matrix for the next draw
    // Torso
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(0, 3.4, 0);// Where to place it
    modelMatrix.rotate(0, 0, 0, 1); // Which way to orient it
    modelMatrix.scale(1.1,1.2,0.4);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);


    // Resets the view-matrix for the next draw
    // Right arm
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(-1.4, 4, 0);// Where to place it
    modelMatrix.rotate(90, 0, 0, 1); // Which way to orient it
    modelMatrix.rotate(-moveDeg, 0, 1, 0); // What angle to bend it at
    modelMatrix.translate(-1.1,0, 0); // Where will the bend be
    modelMatrix.scale(1.2,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);

    // Resets the view-matrix for the next draw
    // Left arm
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(1.4, 4, 0);// Where to place it
    modelMatrix.rotate(90, 0, 0, 1); // Which way to orient it
    modelMatrix.rotate(moveDeg, 0, 1, 0); // What angle to bend it at
    modelMatrix.translate(-1.1,0, 0); // Where will the bend be
    modelMatrix.scale(1.2,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);


    // Resets the view-matrix for the next draw
    // Neck
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(0, 4.8, 0);// Where to place it
    modelMatrix.rotate(0, 0, 0, 1); // Which way to orient it
    modelMatrix.scale(0.3, 0.3, 0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);

    // Resets the view-matrix for the next draw
    // Head
    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(0, 5.3, 0);// Where to place it
    modelMatrix.rotate(moveDeg, 0, 1, 0); // Which way to orient it
    modelMatrix.scale(0.5,0.5,0.5);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    // Draws the cube via index-buffer and drawElements()
    gl.drawElements(drawMode, cubeIndices.length, gl.UNSIGNED_SHORT,0);

}

function drawCoordinateGrid() {
    // a_Position must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    // a_Color must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);

    // Create modelview-matrix for the coordinate-grid
    modelMatrix.setIdentity();
    modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkef�lge!

    // Send
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

    // Draw the coordinate-grid
    gl.drawArrays(gl.LINES, 0, coordPositionBuffer.numberOfItems);
}

function drawXZPlane() {
    //Binder buffer og parametre:
    gl.bindBuffer(gl.ARRAY_BUFFER, xzplanePositionBuffer);
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    gl.bindBuffer(gl.ARRAY_BUFFER, xzplaneColorBuffer);
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);

    updateCamera();
    modelMatrix.setIdentity();
    modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkef�lge!
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

    gl.drawArrays(gl.TRIANGLE_STRIP, 0, xzplanePositionBuffer.numberOfItems);
}

function handleKeyUp(event) {
    if (event.defaultPrevented) return;
    currentlyPressedKeys[event.code] = false;
    event.preventDefault();
}

function handleKeyDown(event) {
    if (event.defaultPrevented) return;
    currentlyPressedKeys[event.code] = true;
    event.preventDefault();
}

function handleKeys(timeElapsed) {

    let camPosVec = vec3.fromValues(camPosX, camPosY, camPosZ);
    // Simple camera-controls
    if (currentlyPressedKeys["KeyD"]) {
        rotateVector(2, camPosVec, 0, 1, 0);
    }
    if (currentlyPressedKeys["KeyA"]) {
        rotateVector(-2, camPosVec, 0, 1, 0);
    }
    if (currentlyPressedKeys["KeyS"]) {
        rotateVector(2, camPosVec, 1, 0, 0);
    }
    if (currentlyPressedKeys["KeyW"]) {
        rotateVector(-2, camPosVec, 1, 0, 0);
    }
    if (currentlyPressedKeys["KeyF"]) {
        if(moveDeg<=-45)
            moveForward=true;
        if(moveDeg>=45)
            moveForward=false;

        //FPS-independent animation-steps
        if(moveForward){
            moveDeg+=135*timeElapsed;
        } else moveDeg-=135*timeElapsed;
    }

    // Enable wireframe
    if (currentlyPressedKeys["Space"]) {
        if(drawMode === gl.TRIANGLE_STRIP)
            drawMode = gl.LINES;
        else drawMode = gl.TRIANGLE_STRIP;
    }

    // Zoom
    if (currentlyPressedKeys["KeyZ"]) {
        vec3.scale(camPosVec, camPosVec, 1.05);
    }
    if (currentlyPressedKeys["KeyX"]) {
        vec3.scale(camPosVec, camPosVec, 0.95);
    }

    camPosX = camPosVec[0];
    camPosY = camPosVec[1];
    camPosZ = camPosVec[2];
}

function updateCamera() {
    // Where to look
    viewMatrix.setLookAt(camPosX, camPosY, camPosZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
    // How it will look
    projectionMatrix.setPerspective(45, canvas.width / canvas.height, 1, 1000);
}

function calculateFPS(currentTime) {
    if (currentTime === undefined)
        currentTime = 0;

    //Updates the frame-count once per second
    if (currentTime - fpsData.prevTime >= 1000) {
        document.getElementById("fps").innerHTML = fpsData.numFrames;
        fpsData.numFrames = 0;
        fpsData.prevTime = currentTime;
    }

    // Increases frame-count by 1 for each re-draw
    let elapsed = 0.0;
    if (lastTime !== 0.0)
        elapsed = (currentTime - lastTime) / 1000;
    lastTime = currentTime;
    fpsData.numFrames++;
    return elapsed;
}
