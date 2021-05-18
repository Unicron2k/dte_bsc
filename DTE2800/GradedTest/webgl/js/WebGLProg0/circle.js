"use strict";
/*
    buffer og draw for en flat sirkel.
*/
let circleFloat32Vertices = undefined;
let circleVertexBuffer = undefined;
let circleNoVertices = 0;
let circleColorBuffer = undefined;
let angle=0;

function initCircleVertices() {
    let toPI = 2*Math.PI;
    let circleVertices = [];	//Tegnes vha. TRIANGLE_FAN
    let stepGrader = 5;
    let step = (Math.PI / 180) * stepGrader;

    // Senterpunkt:
    let x=0, y=0, z=0;
    circleVertices = circleVertices.concat(x,y,z); //NB! bruk av concat!!
    circleNoVertices++;
    for (let phi = 0.0; phi <= toPI; phi += step)
    {
        x = Math.cos(phi);
        y = 0;
        z = Math.sin(phi);

        circleVertices = circleVertices.concat(x,y,z);
        circleNoVertices++;
    }
    circleFloat32Vertices = new Float32Array(circleVertices);
}

function initCircleBuffers() {
    if (circleVertexBuffer) // Enkel sjekk om bufferet allerede er initialisert.
        return;
    initCircleVertices();
    circleVertexBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, circleVertexBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, circleFloat32Vertices, g_gl.STATIC_DRAW);

    circleVertexBuffer.itemSize = 3;
    circleVertexBuffer.numberOfItems = circleNoVertices; //= antall vertekser
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);
}

function drawCircle(elapsed, color=[0.2, 0.2, 0.2, 1]) {
    setupCamera();
    g_gl.useProgram(g_gl.circleShaderProgram);   // Se mainapp.js

    // Kopler matriseshaderparametre med tilsvarende Javascript-variabler:
    let u_modelviewMatrix = g_gl.getUniformLocation(g_gl.circleShaderProgram, "u_modelviewMatrix");
    let u_projectionMatrix = g_gl.getUniformLocation(g_gl.circleShaderProgram, "u_projectionMatrix");

    //Oppgave 1A from here
    let tempColors = [];
    for(let i=0; i<circleVertexBuffer.numberOfItems ; i++){
        tempColors.push(color[0], color[1], color[2], color[3], )
    }
    let circleColors = new Float32Array(tempColors);
    circleColorBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, circleColorBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, circleColors, g_gl.STATIC_DRAW);
    circleColorBuffer.itemSize = 4;
    circleColorBuffer.numberOfItems = 1;
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);

    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, circleColorBuffer);
    let a_color = g_gl.getAttribLocation(g_gl.circleShaderProgram, "a_color");
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, 0, 0);
    g_gl.enableVertexAttribArray(a_color);
    //to here



    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, circleVertexBuffer);
    g_modelviewMatrix = g_viewMatrix.multiply(g_modelMatrix); // NB! rekkefølge!
    g_gl.uniformMatrix4fv(u_modelviewMatrix, false, g_modelviewMatrix.elements);
    g_gl.uniformMatrix4fv(u_projectionMatrix, false, g_projectionMatrix.elements);

    let a_position = g_gl.getAttribLocation(g_gl.circleShaderProgram, 'a_position');
    let stride = 3 * 4;
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, stride, 0);
    g_gl.enableVertexAttribArray(a_position);

    g_gl.drawArrays(g_gl.TRIANGLE_FAN, 0, circleVertexBuffer.numberOfItems);
    //g_gl.drawArrays(g_gl.LINE_STRIP, 1, circleVertexBuffer.numberOfItems-1);
}
