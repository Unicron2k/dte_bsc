//<editor-fold desc="Shaders">
// Vertex shader program
let VSHADER_SOURCE =
    'attribute vec3 a_Position;\n' +
    'attribute vec4 a_Color;\n' +
    'uniform mat4 u_modelviewMatrix;\n' +
    'uniform mat4 u_projectionMatrix;\n' +
    'varying vec4 v_Color;\n' +
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
let COORD_BOUNDARY = 100;
let currentlyPressedKeys = [];

// buffers:
let cubeVertices= null;
let cubeVertexBuffer = null;
let cubeColors = null
let cubeColorBuffer = null;


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
let camPosX=2, camPosY=2, camPosZ=20;
let lookAtX=0, lookAtY=0, lookAtZ=0;
let upX=0, upY=1, upZ=0;

// FPS-calculation-variables
let fpsData = {};
let lastTime = 0.0;
let timeElapsed = 0.0;
//</editor-fold>

function main() {

    if (!init()) {
        console.log('Error initializing shaders');
        return;
    }
    if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
        console.log('Error initializing shaders');
        return;
    }
    // TODO: Add error-checks
    initCubeBuffer();
    initCoordBuffer();
    if (!bindShaderParameters()) {
        console.log('Unable to bind shader-parameters')
        return;
    }
    // Setting background-color
    gl.clearColor(0.8, 0.8, 0.6, 1.0); //RGBA
    // Enter main program-loop
    console.log('AwesomeCam(™) and AwesomeCoord(™) was definitely not ripped from Werner. Pinky Promise! 👼🏻')
    mainLoop();
}

function mainLoop(currentTime) {
    // Clear the color-buffer
    gl.clear(gl.COLOR_BUFFER_BIT);
    timeElapsed = calculateFPS(currentTime);
    handleKeys(timeElapsed);
    drawCoordinateGrid()

    //Add your drawing-code here
    drawCube();

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

    // Initialize som fpsData-vars
    fpsData.numFrames = 0;
    fpsData.prevTime = 0;

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

    return true;
}

function initCubeBuffer() {
    // TODO: Add error-checks?

    // Vertex-buffer:
    cubeVertices = new Float32Array([
        // Front
        1, -1,  1,//FBR 3
        -1, -1,  1,//FBL 0
        1,  1,  1,//FTR 2

        -1, -1,  1,//FBL 0
        1,  1,  1,//FTR 2
        -1,  1,  1,//FTL 1

        // Top
        1,  1,  1,//FTR 2
        -1,  1,  1,//FTL 1
        1,  1, -1,//BTR 6

        -1,  1,  1,//FTL 1
        1,  1, -1,//BTR 6
        -1,  1, -1,//BTL 5

        // Left
        -1,  1, -1,//BTL 5
        -1,  1,  1,//FTL 1
        -1, -1, -1,//BBL 4

        -1,  1,  1,//FTL 1
        -1, -1, -1,//BBL 4
        -1, -1,  1,//FBL 0

        //Bottom
        -1, -1, -1,//BBL 4
        -1, -1,  1,//FBL 0
        1, -1,  1,//FBR 3

        1, -1,  1,//FBR 3
        -1, -1, -1,//BBL 4
        1, -1, -1, //BBR 7

        // Right
        1, -1, -1, //BBR 7
        1, -1,  1,//FBR 3
        1,  1,  1,//FTR 2

        1,  1,  1,//FTR 2
        1, -1, -1, //BBR 7
        1,  1, -1,//BTR 6

        // Back
        1, -1, -1,//7
        -1, -1, -1,//4
        1, 1, -1,//6

        -1, -1, -1,//4
        -1, 1, -1,//5
        1, 1, -1,//6

        /*
        1, -1, -1, //BBR 7
        1,  1, -1,//BTR 6
        -1,  1, -1,//BTL 5

        -1,  1, -1,//BTL 5
        1, -1, -1, //BBR 7
        -1, -1, -1,//BBL 4
        */
    ]);
    // Bind vertex-buffer:
    cubeVertexBuffer = gl.createBuffer();
    cubeVertexBuffer.itemSize = 3;
    cubeVertexBuffer.numberOfItems = (cubeVertices.length)/3;
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, cubeVertices, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ARRAY_BUFFER, null);


    // Color-buffer
    cubeColors = new Float32Array([
        //Forsiden:
        1.0, 0.125, 0.0, 1,
        0.0, 0.250, 1.0, 1,
        1.0, 0.375, 0.0, 1,

        0.0, 0.500, 1.0, 1,
        1.0, 0.625, 0.0, 1,
        0.0, 0.750, 1.0, 1,


        1.0, 0.875, 0.0, 1,
        0.0, 1.000, 1.0, 1,
        0.0, 0.250, 1.0, 1,

        1.0, 0.375, 0.0, 1,
        0.0, 0.500, 1.0, 1,
        1.0, 0.625, 0.0, 1,


        0.0, 0.750, 1.0, 1,
        1.0, 0.875, 0.0, 1,
        0.0, 1.000, 1.0, 1,

        0.0, 0.250, 1.0, 1,
        1.0, 0.375, 0.0, 1,
        0.0, 0.500, 1.0, 1,


        1.0, 0.625, 0.0, 1,
        0.0, 0.750, 1.0, 1,
        1.0, 0.875, 0.0, 1,

        0.0, 1.000, 1.0, 1,
        0.0, 0.250, 1.0, 1,
        1.0, 0.375, 0.0, 1,


        0.0, 0.500, 1.0, 1,
        1.0, 0.625, 0.0, 1,
        0.0, 0.750, 1.0, 1,

        1.0, 0.875, 0.0, 1,
        0.0, 1.000, 1.0, 1,
        0.0, 1.000, 1.0, 1,


        0.0, 0.750, 1.0, 1,
        1.0, 0.875, 0.0, 1,
        0.0, 1.000, 1.0, 1,

        0.0, 0.250, 1.0, 1,
        1.0, 0.375, 0.0, 1,
        0.0, 0.500, 1.0, 1,
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

    let coordColors = new Float32Array([
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

function bindShaderParameters() {
    u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
    u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');
    a_Position = gl.getAttribLocation(gl.program, "a_Position");
    a_Color = gl.getAttribLocation(gl.program, "a_Color");
    return (u_modelviewMatrix!=null || u_projectionMatrix!=null || a_Position!=null || a_Color!=null );

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

function drawCube(){
    // a_Position must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    a_Position = gl.getAttribLocation(gl.program, "a_Position");
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    // a_Color must be re-bound
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeColorBuffer);
    a_Color = gl.getAttribLocation(gl.program, "a_Color");
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);


    updateCamera();
    //modelMatrix.setIdentity();
    modelMatrix.translate(0, 0, 0);
    modelMatrix.scale(2,0.5,2);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);

    // Draws the cube via index-buffer and drawElements()
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, cubeVertexBuffer.numberOfItems);

    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(-0.5,1, 0);
    modelMatrix.rotate(85, 0, 0, 1);
    modelMatrix.scale(1.0,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);

    // Draws the cube via index-buffer and drawElements()
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, cubeVertexBuffer.numberOfItems);

    updateCamera();
    modelMatrix.setIdentity();
    modelMatrix.translate(0.5,1, 0);
    modelMatrix.rotate(95, 0, 0, 1);
    modelMatrix.scale(1.0,0.3,0.3);
    modelviewMatrix = viewMatrix.multiply(modelMatrix);
    // Sends the projection-and-modelview-matrix to the shader
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);

    // Draws the cube via index-buffer and drawElements()
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, cubeVertexBuffer.numberOfItems);
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
    //Enkel rotasjon av kameraposisjonen:
    if (currentlyPressedKeys["KeyD"]) {
        rotateVector(2, camPosVec, 0, 1, 0);  //Roterer camPosVec 2 grader om y-aksen.
    }
    if (currentlyPressedKeys["KeyA"]) {
        rotateVector(-2, camPosVec, 0, 1, 0);  //Roterer camPosVec 2 grader om y-aksen.
    }
    if (currentlyPressedKeys["KeyS"]) {
        rotateVector(2, camPosVec, 1, 0, 0);  //Roterer camPosVec 2 grader om x-aksen.
    }
    if (currentlyPressedKeys["KeyW"]) {
        rotateVector(-2, camPosVec, 1, 0, 0);  //Roterer camPosVec 2 grader om x-aksen.
    }

    //Zoom inn og ut:
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

