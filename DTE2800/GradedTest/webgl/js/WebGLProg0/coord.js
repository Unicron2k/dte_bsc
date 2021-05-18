"use strict";
/*
    Buffer og draw() for koordinatsystemet.
*/
// Koordinatsystemet.
let coordPositionBuffer = undefined;
let coordColorBuffer = undefined;
let COORD_BOUNDARY = 1000;

function initCoordBuffers() {
    if (coordPositionBuffer) // Enkel sjekk om bufferet allerede er initialisert.
        return;

    let coordPositions = new Float32Array([
        //x-aksen
        -COORD_BOUNDARY, 0.0, 0.0,
        COORD_BOUNDARY, 0.0, 0.0,

        //y-aksen:
        0.0, COORD_BOUNDARY, 0.0,
        0.0, -COORD_BOUNDARY, 0.0,

        //z-aksen:
        0.0, 0.0, COORD_BOUNDARY,
        0.0, 0.0, -COORD_BOUNDARY,
    ]);

    // Verteksbuffer for koordinatsystemet:
    coordPositionBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, coordPositionBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, coordPositions, g_gl.STATIC_DRAW);
    coordPositionBuffer.itemSize = 3; 		// NB!!
    coordPositionBuffer.numberOfItems = 6; 	// NB!!
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);	// NB!! M� kople fra n�r det opereres med flere buffer! Kopler til i draw().

    // Fargebuffer: oppretter, binder og skriver data til bufret:
    let coordColors = new Float32Array([
        1.0, 0.0, 0.0, 1,   // X-akse
        1.0, 0.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,   // Y-akse
        0.0, 1.0, 0.0, 1,
        0.0, 0.0, 1.0, 1,   // Z-akse
        0.0, 0.0, 1.0, 1
    ]);
    coordColorBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, coordColorBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, coordColors, g_gl.STATIC_DRAW);
    coordColorBuffer.itemSize = 4; 			// 4 float per farge.
    coordColorBuffer.numberOfItems = 6; 	// 6 farger.
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);
}

function drawCoord() {
    setupCamera();

    g_gl.useProgram(g_gl.coordShaderProgram);   // Se mainapp.js

    // Kopler matriseshaderparametre med tilsvarende Javascript-variabler:
    let u_modelviewMatrix = g_gl.getUniformLocation(g_gl.coordShaderProgram, "u_modelviewMatrix");
    let u_projectionMatrix = g_gl.getUniformLocation(g_gl.coordShaderProgram, "u_projectionMatrix");

    g_modelMatrix.setIdentity();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, coordPositionBuffer);
    let a_position = g_gl.getAttribLocation(g_gl.coordShaderProgram, "a_position");
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_position);

    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, coordColorBuffer);
    let a_color = g_gl.getAttribLocation(g_gl.coordShaderProgram, "a_color");
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_color);

    g_modelviewMatrix = g_viewMatrix.multiply(g_modelMatrix);
    g_gl.uniformMatrix4fv(u_modelviewMatrix, false, g_modelviewMatrix.elements);
    g_gl.uniformMatrix4fv(u_projectionMatrix, false, g_projectionMatrix.elements);
    g_gl.drawArrays(g_gl.LINES, 0, coordPositionBuffer.numberOfItems);
}
