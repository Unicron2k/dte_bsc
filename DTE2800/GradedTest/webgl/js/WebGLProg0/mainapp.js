"use strict";
/***
  Tegner diverse figurer.
  Koden er (med overlegg) stort sett uten kommentarer.
  Merk at variabler som brukes på tvers av filer er prefikset med g_
  for å skille dem ut fra andre variabler.
***/

// Globale variabler (prefikset med g_).
let g_gl = null;
let g_modelMatrix = null;
let g_viewMatrix = null;
let g_modelviewMatrix = null;
let g_projectionMatrix = null;

// Variabler kun brukt i denne fila:
let canvas = null;
let camPosX = 60;
let camPosY = 40;
let camPosZ = 135;
let lookAtX = 0;
let lookAtY = 0;
let lookAtZ = 0;
let upX = 0;
let upY = 1;
let upZ = 0;
let currentlyPressedKeys = [];
let lastTime = 0.0;
let fpsData = {};

function initContext() {
    canvas = document.getElementById("webgl");
    g_gl = canvas.getContext('webgl');
    if (!g_gl) {
        console.log("Fikk ikke tak i rendering context for WebGL");
        return false;
    }
    g_gl.viewport(0,0,canvas.width,canvas.height);
    document.addEventListener('keyup', handleKeyUp, false);
    document.addEventListener('keydown', handleKeyDown, false);
    return true;
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

function setupCamera() {
    g_viewMatrix.setLookAt(camPosX, camPosY, camPosZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
    g_projectionMatrix.setPerspective(45, canvas.width / canvas.height, 0.1, 10000);
}

function handleKeys(elapsed) {
    let camPosVec = vec3.fromValues(camPosX, camPosY, camPosZ);
    //Rotasjon av kameraposisjonen:
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
    //Zoom inn og ut:
    if (currentlyPressedKeys["KeyV"]) { //V
        vec3.scale(camPosVec, camPosVec, 1.05);
    }
    if (currentlyPressedKeys["KeyB"]) {	//B
        vec3.scale(camPosVec, camPosVec, 0.95);
    }
    camPosX = camPosVec[0];
    camPosY = camPosVec[1];
    camPosZ = camPosVec[2];
    setupCamera();
}

function draw(currentTime) {
    requestAnimFrame(draw);
    if (currentTime === undefined)
        currentTime = 0;
    if (currentTime - fpsData.lastTimestamp >= 1000) {
        document.getElementById("fps").innerHTML = fpsData.noFrames;
        fpsData.noFrames = 0;
        fpsData.lastTimestamp = currentTime;
    }
    let elapsed = 0.0;
    if (lastTime !== 0.0)
        elapsed = (currentTime - lastTime)/1000;
    lastTime = currentTime;

    // Rensk skjerm:
    g_gl.clear(g_gl.COLOR_BUFFER_BIT)
    g_gl.enable(g_gl.BLEND);
    g_gl.blendFunc(g_gl.SRC_ALPHA, g_gl.ONE_MINUS_SRC_ALPHA);

    // Input:
    handleKeys(elapsed);

    // Tegner:
    drawCoord(elapsed);
    g_modelMatrix.setIdentity();
    drawXZPlane(elapsed);

//Oppgave 1B from here
    let red=[255,0,0,1];
    let yellow=[165,165,0,1];
    let green=[0,255,0,1];
    let darkGreen=[0,165,0,1];

    g_modelMatrix.setIdentity();
    g_modelMatrix.setScale(1,10,1)
    g_modelMatrix.translate(-10,1,0)
    drawCylinder(elapsed);

    g_modelMatrix.setIdentity();
    g_modelMatrix.setScale(1,10,1)
    g_modelMatrix.translate(10,1,0)
    drawCylinder(elapsed);

    g_modelMatrix.setIdentity();
    g_modelMatrix.translate(0,20,0)
    g_modelMatrix.rotate(90,0,0,1);
    g_modelMatrix.scale(1,10,1)
    drawCylinder(elapsed);

    g_modelMatrix.setIdentity();
    g_modelMatrix.translate(0,20,0)
    g_modelMatrix.scale(4,1.5,1.5)
    drawCube(elapsed, darkGreen);

    g_modelMatrix.setIdentity();
    g_modelMatrix.translate(-2,20,3.5)
    g_modelMatrix.rotate(90,1,0,0);
    drawCircle(elapsed, red);

    g_modelMatrix.setIdentity();
    g_modelMatrix.translate(0,20,3.5)
    g_modelMatrix.rotate(90,1,0,0);
    drawCircle(elapsed, yellow);

    g_modelMatrix.setIdentity();
    g_modelMatrix.translate(2,20,3.5)
    g_modelMatrix.rotate(90,1,0,0);
    drawCircle(elapsed, green);
    //to here


    fpsData.noFrames++;
}

function main() {
    if (!initContext())
        return;

    // Initialiserere ulike shadere:
    // a-shader:
    let vertexShaderSource = document.getElementById("vertex-shader-a").innerHTML;
    let fragmentShaderSource = document.getElementById("fragment-shader-a").innerHTML;
    g_gl.coordShaderProgram = createProgram(g_gl, vertexShaderSource, fragmentShaderSource);
    g_gl.xzplaneShaderProgram = g_gl.coordShaderProgram;
    g_gl.cylinderShaderProgram = g_gl.coordShaderProgram;
    g_gl.pyramidShaderProgram = g_gl.coordShaderProgram;

    // b-shader:
    vertexShaderSource = document.getElementById("vertex-shader-b").innerHTML;
    fragmentShaderSource = document.getElementById("fragment-shader-b").innerHTML;
    g_gl.cubeShaderProgram = createProgram(g_gl, vertexShaderSource, fragmentShaderSource);
    g_gl.circleShaderProgram = g_gl.cubeShaderProgram;

    if (!g_gl.coordShaderProgram || !g_gl.cubeShaderProgram) {
        console.log('Feil ved initialisering av shaderkoden.');
        return;
    }

    // AKTIVERER DYBDETEST:
    g_gl.enable(g_gl.DEPTH_TEST);
    g_gl.depthFunc(g_gl.LESS);

    // Initialiserer matriser:
    g_modelMatrix = new Matrix4();
    g_viewMatrix = new Matrix4();
    g_modelviewMatrix = new Matrix4();
    g_projectionMatrix = new Matrix4();

    // Initialiserer buffer:
    initCoordBuffers();
    initXZPlaneBuffers();
    initCubeBuffers();
    initCircleBuffers();
    initCylinderBuffers([255,165,0,1]);

    // Bakgrunn:
    g_gl.clearColor(1, 1, 1, 1.0);

    // FPS:
    fpsData.noFrames = 0;
    fpsData.lastTimestamp = 0;

    draw();
}
