"use strict";
/*
    buffer og draw for en sylinder.
*/

let cylinderTopVertexBuffer = null;
let cylinderBottomVertexBuffer = null;
let cylinderWallVertexBuffer = null;
let cylinderTopVertices;
let cylinderBottomVertices;
let cylinderWallsVertices;
let cylNoTopVertices = 0;
let cylNoBottomVertices = 0;
let cylNoWallVertices = 0;
let cylinderWallVertices = [];	//Tegnes vha. TRIANGLES

function createRectangleVertices(phi, yVal, step, r, g, b, a) {
    let x, y, z;
    //v1:
    x = Math.cos(phi);  //cos(0)=1
    y = yVal;			//1
    z = -Math.sin(phi); //sin(0)=0
    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;

    //v2:
    x = Math.cos(phi);  //cos(0)=1
    y = -yVal;			//-1
    z = -Math.sin(phi); //sin(0)=0
    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;

    //v3:
    x = Math.cos(phi+step);
    y = -yVal;			//-1
    z = -Math.sin(phi+step);
    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;

    //v4:
    x = Math.cos(phi);  //cos(0)=1
    y = yVal;			//1
    z = -Math.sin(phi); //sin(0)=0

    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;

    //v5
    x = Math.cos(phi+step);
    y = -yVal;			//-1
    z = -Math.sin(phi+step);
    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;

    //v6
    x = Math.cos(phi+step);
    y = yVal;			//1
    z = -Math.sin(phi+step);
    cylinderWallVertices = cylinderWallVertices.concat(x,y,z, r,g,b,a);
    cylNoWallVertices++;
}

function initCylinderVertices(color) {
    let r=0.3, g=0.5, b=1, a=1;
    if (color && color.length===4)
        r=color[0], g=color[1], b=color[2], a=color[3];
    let toPI = 2*Math.PI;
    let verticesTop = [];	//Tegnes vha. TRIANGLE_FAN
    let stepGrader = 10;
    let step = (Math.PI / 180) * stepGrader;
    let x=0,y=1,z=0;

    //Top:
    //Senterpunkt:
    x=0;y=1;z=0;
    verticesTop = verticesTop.concat(x,y,z, r,g,b,a);
    cylNoTopVertices++;
    for (let phi = 0.0; phi <= toPI; phi += step)
    {
        x = Math.cos(phi);
        y = 1;
        z = Math.sin(phi);

        verticesTop = verticesTop.concat(x,y,z, r,g,b,a);
        cylNoTopVertices++;
    }
    cylinderTopVertices = new Float32Array(verticesTop);

    //Bunn:
    //Senterpunkt:
    x=0;y=-1;z=0;
    let verticesBottom = [];	//Tegnes vha. TRIANGLE_FAN
    verticesBottom = verticesBottom.concat(x,y,z, r,g,b,a);
    cylNoBottomVertices++;
    for (let phi = 0.0; phi <= toPI; phi += step)
    {
        x = Math.cos(phi);
        y = -1;
        z = Math.sin(phi);

        verticesBottom = verticesBottom.concat(x,y,z, r,g,b,a);
        cylNoBottomVertices++;
    }
    cylinderBottomVertices = new Float32Array(verticesBottom);

    //Veggene:
    let cylinderHeight = 1;
    let phi;
    for (phi=0; phi<=toPI; phi+=step) {
        createRectangleVertices(phi, cylinderHeight, step, r, g, b, a);
    }
    cylinderWallsVertices = new Float32Array(cylinderWallVertices);
}

function initCylinderBuffers(color) {
    if (cylinderTopVertexBuffer!==null) // Enkel sjekk om bufferet allerede er initialisert.
        return;
    initCylinderVertices(color);

    //Sylinder-topp:
    cylinderTopVertexBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderTopVertexBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, cylinderTopVertices, g_gl.STATIC_DRAW);

    cylinderTopVertexBuffer.itemSize = 3 + 4;
    cylinderTopVertexBuffer.numberOfItems = cylNoTopVertices; //= antall vertekser
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);

    //Sylinder-bunn:
    cylinderBottomVertexBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderBottomVertexBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, cylinderBottomVertices, g_gl.STATIC_DRAW);

    cylinderBottomVertexBuffer.itemSize = 3 + 4;
    cylinderBottomVertexBuffer.numberOfItems = cylNoBottomVertices; //= antall vertekser
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);

    //Sylinder-vegger:
    cylinderWallVertexBuffer = g_gl.createBuffer();
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderWallVertexBuffer);
    g_gl.bufferData(g_gl.ARRAY_BUFFER, cylinderWallsVertices, g_gl.STATIC_DRAW);

    cylinderWallVertexBuffer.itemSize = 3 + 4;
    cylinderWallVertexBuffer.numberOfItems = cylNoWallVertices; //= antall vertekser
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, null);
}

function drawCylinder(elapsed) {
    setupCamera();
    g_gl.useProgram(g_gl.cylinderShaderProgram);   // Se mainapp.js

    // Kopler matriseshaderparametre med tilsvarende Javascript-variabler:
    let u_modelviewMatrix = g_gl.getUniformLocation(g_gl.cylinderShaderProgram, "u_modelviewMatrix");
    let u_projectionMatrix = g_gl.getUniformLocation(g_gl.cylinderShaderProgram, "u_projectionMatrix");

    g_modelviewMatrix = g_viewMatrix.multiply(g_modelMatrix);

    // Sender matriser til shader:
    g_gl.uniformMatrix4fv(u_modelviewMatrix, false, g_modelviewMatrix.elements);
    g_gl.uniformMatrix4fv(u_projectionMatrix, false, g_projectionMatrix.elements);

    //TOPPEN:
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderTopVertexBuffer);
    // Kople posisjonsparametret til bufferobjektet: 3=ant. floats per posisjon/verteks.
    let a_position = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_position');
    let stride = (3 + 4) * 4;
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, stride, 0);
    g_gl.enableVertexAttribArray(a_position);
    // Kople fargeparametret til bufferobjektet: 4=ant. floats per farge/verteks
    let a_color = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_color');
    let colorOfset = 3 * 4; //12= offset, start på color-info innafor verteksinfoen.
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, stride, colorOfset);
    g_gl.enableVertexAttribArray(a_color);
    g_gl.drawArrays(g_gl.TRIANGLE_FAN, 0, cylinderTopVertexBuffer.numberOfItems);

    //BUNNEN:
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderBottomVertexBuffer);
    a_position = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_position');
    stride = (3 + 4) * 4;
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, stride, 0);
    g_gl.enableVertexAttribArray(a_position);
    // Kople fargeparametret til bufferobjektet: 4=ant. Floats per verteks
    a_color = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_color');
    colorOfset = 3 * 4; //12= offset, start på color-info innafor verteksinfoen.
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, stride, colorOfset);
    g_gl.enableVertexAttribArray(a_color);
    g_gl.drawArrays(g_gl.TRIANGLE_FAN, 0, cylinderBottomVertexBuffer.numberOfItems);

    //VEGGENE:
    g_gl.bindBuffer(g_gl.ARRAY_BUFFER, cylinderWallVertexBuffer);
    a_position = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_position');
    stride = (3 + 4) * 4;
    g_gl.vertexAttribPointer(a_position, 3, g_gl.FLOAT, false, stride, 0);
    g_gl.enableVertexAttribArray(a_position);
    // Kople fargeparametret til bufferobjektet: 4=ant. Floats per verteks
    a_color = g_gl.getAttribLocation(g_gl.cylinderShaderProgram, 'a_color');
    colorOfset = 3 * 4; //12= offset, start p� color-info innafor verteksinfoen.
    g_gl.vertexAttribPointer(a_color, 4, g_gl.FLOAT, false, stride, colorOfset);
    g_gl.enableVertexAttribArray(a_color);

    g_gl.drawArrays(g_gl.TRIANGLES, 0, cylinderWallVertexBuffer.numberOfItems);
    //g_gl.drawArrays(g_gl.LINES, 0, cylinderWallVertexBuffer.numberOfItems);
}
