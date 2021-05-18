"use strict";
/*
    buffer og draw for en kube.
*/
let cubePositionBuffer = undefined;
let cubeColorBuffer = undefined;

function initCubeBuffers() {
    if (cubePositionBuffer) // Enkel sjekk om bufferet allerede er initialisert.
        return;
    let cubePositions = new Float32Array([
        //Forsiden (pos):
        -1, 1, 1,
        -1,-1, 1,
        1,-1, 1,

        -1,1,1,
        1, -1, 1,
        1,1,1,

        //Høyre side:
        1,1,1,
        1,-1,1,
        1,-1,-1,

        1,1,1,
        1,-1,-1,
        1,1,-1,

        //Baksiden (pos):
        1,-1,-1,
        -1,-1,-1,
        1, 1,-1,

        -1,-1,-1,
        -1,1,-1,
        1,1,-1,

        //Venstre side:
        -1,-1,-1,
        -1,1,1,
        -1,1,-1,

        -1,-1,1,
        -1,1,1,
        -1,-1,-1,

        //Topp:
        -1,1,1,
        1,1,1,
        -1,1,-1,

        -1,1,-1,
        1,1,1,
        1,1,-1,

        //Bunn:
        -1,-1,-1,
        -1,-1,1,
        1,-1,1,

        -1,-1,-1,
        1,-1,1,
        1,-1,-1
    ]);
    cubePositionBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cubePositionBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, cubePositions, g_gl.STATIC_DRAW);
    cubePositionBuffer.itemSize = 3;
    cubePositionBuffer.numberOfItems = 36;
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);

}

function drawCube(elapsed, color =[0.4, 0.4, 0.4, 1]) {
    setupCamera();

    g_gl.useProgram(g_gl.cubeShaderProgram);   // Se mainapp.js
    // Kopler matriseshaderparametre med tilsvarende Javascript-variabler:
    let u_modelviewMatrix = g_gl.getUniformLocation(g_gl.cubeShaderProgram, "u_modelviewMatrix");
    let u_projectionMatrix = g_gl.getUniformLocation(g_gl.cubeShaderProgram, "u_projectionMatrix");

    //Oppgave 1A from here
    let tempColors = [];
    for(let i=0; i<cubePositionBuffer.numberOfItems; i++){
        tempColors.push(color[0], color[1], color[2], color[3], )
    }
    let cubeColors = new Float32Array(tempColors);
    cubeColorBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cubeColorBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, cubeColors, g_gl.STATIC_DRAW);
    cubeColorBuffer.itemSize = 4;
    cubeColorBuffer.numberOfItems = 1;
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);

    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cubeColorBuffer);
    let a_color = g_gl.getAttribLocation(g_gl.cubeShaderProgram, "a_color");
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_color);
    //to here

    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cubePositionBuffer);
    let a_position = g_gl.getAttribLocation(g_gl.cubeShaderProgram, 'a_position');
    g_gl.vertexAttribPointer(a_position, cubePositionBuffer.itemSize, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_position);

    g_modelviewMatrix = g_viewMatrix.multiply(g_modelMatrix);
    g_gl.uniformMatrix4fv(u_modelviewMatrix, false, g_modelviewMatrix.elements);
    g_gl.uniformMatrix4fv(u_projectionMatrix, false, g_projectionMatrix.elements);
    g_gl.drawArrays(g_gl.TRIANGLES, 0, cubePositionBuffer.numberOfItems);
    //g_gl.drawArrays(g_gl.LINE_STRIP, 0, cubePositionBuffer.numberOfItems);
}

