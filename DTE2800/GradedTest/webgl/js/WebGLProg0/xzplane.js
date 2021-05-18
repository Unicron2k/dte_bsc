"use strict";
/*
    buffer og draw for et XZ-plan.
*/
// Verteksbuffer:
let xzplanePositionBuffer = undefined;
let xzplaneColorBuffer = undefined;
let xzPlaneWidth = 500;
let xzPlaneHeight = 500;

function initXZPlaneBuffers() {
    if (xzplanePositionBuffer) // Enkel sjekk om bufferet allerede er initialisert.
        return;

    let xzplanePositions = new Float32Array([
        -xzPlaneWidth / 2, 0, xzPlaneHeight / 2,
        xzPlaneWidth / 2, 0, xzPlaneHeight / 2,
        -xzPlaneWidth / 2, 0, -xzPlaneHeight / 2,
        xzPlaneWidth / 2, 0, -xzPlaneHeight / 2
    ]);
    // Farger:
    let xzplaneColors = new Float32Array([
        0.3, 0.5, 0.2, 1,
        0.3, 0.5, 0.2, 1,
        0.3, 0.5, 0.2, 1,
        0.3, 0.5, 0.2, 1
    ]);
    // Position buffer:
    xzplanePositionBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, xzplanePositionBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, xzplanePositions, g_gl.STATIC_DRAW);
    xzplanePositionBuffer.itemSize = 3; // NB!!
    xzplanePositionBuffer.numberOfItems = 4; // NB!!
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);
    // Color buffer:
    xzplaneColorBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, xzplaneColorBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, xzplaneColors, g_gl.STATIC_DRAW);
    xzplaneColorBuffer.itemSize = 4; // NB!!
    xzplaneColorBuffer.numberOfItems = 4; // NB!!
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);
}

function drawXZPlane(elapsed) {
    setupCamera();

    g_gl.useProgram(g_gl.xzplaneShaderProgram);   // Se mainapp.js

    // Kopler matriseshaderparametre med tilsvarende Javascript-variabler:
    let u_modelviewMatrix = g_gl.getUniformLocation(g_gl.xzplaneShaderProgram, "u_modelviewMatrix");
    let u_projectionMatrix = g_gl.getUniformLocation(g_gl.xzplaneShaderProgram, "u_projectionMatrix");

    //Binder buffer og parametre:
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, xzplanePositionBuffer);
    let a_position = g_gl.getAttribLocation(g_gl.xzplaneShaderProgram, "a_position");
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_position);

    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, xzplaneColorBuffer);
    let a_color = g_gl.getAttribLocation(g_gl.xzplaneShaderProgram, "a_color");
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_color);

    // Slår sammen modell & view til modelview-matrise:
    g_modelviewMatrix = g_viewMatrix.multiply(g_modelMatrix); // NB! rekkefølge!

    // Sender matriser til shader:
    g_gl.uniformMatrix4fv(u_modelviewMatrix, false, g_modelviewMatrix.elements);
    g_gl.uniformMatrix4fv(u_projectionMatrix, false, g_projectionMatrix.elements);

    // Tegner:
    g_gl.drawArrays(g_gl.TRIANGLE_STRIP, 0, xzplanePositionBuffer.numberOfItems);
}
