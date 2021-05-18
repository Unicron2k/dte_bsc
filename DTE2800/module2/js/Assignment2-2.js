// Globale letiabler:

// Vertex shader program
let VSHADER_SOURCE =
    'attribute vec3 a_Position;\n' +		//Dersom vec4 trenger vi ikke vec4(a_Position, 1.0) under.
    'attribute vec4 a_Color;\n' +
    'attribute float a_PointSize; \n' +
    'varying vec4 v_Color;\n' +
    'uniform mat4 u_modelviewMatrix;\n' +
    'uniform mat4 u_projectionMatrix;\n' +
    'void main() {\n' +
    '  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position,1.0);\n' +
    '  v_Color = a_Color;\n' +
    '  gl_PointSize = a_PointSize;\n' +
    '}\n';

// Fragment shader program
let FSHADER_SOURCE =
    'precision mediump float;\n' +
    'varying vec4 v_Color;\n' +
    'void main() {\n' +
    '  gl_FragColor = v_Color;\n' + // Fargeverdi.
    '}\n';

let gl = null;
let canvas = null;

// Verteks,color-buffers:
let vertexBuffer = null;
let colorBuffer = null
let a_PointSize = null;

// "Pekere" som brukes til å sende matrisene til shaderen:
let u_modelviewMatrix = null;
let u_projectionMatrix = null;

// Matrisene:
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null; //sammenslått modell- og viewmatrise.
let projectionMatrix = null;

function init() {
    // Hent <canvas> elementet
    canvas = document.getElementById('webgl');

    // Rendering context for WebGL:
    gl = getWebGLContext(canvas);
    if (!gl) {
        console.log('Fikk ikke tak i rendering context for WebGL');
        return false;
    }

    modelMatrix = new Matrix4();
    viewMatrix = new Matrix4();
    modelviewMatrix = new Matrix4();
    projectionMatrix = new Matrix4();

    return true;
}

function initBuffer() {

    // Vertex-buffer:
    let tempArray=[   //NB! ClockWise!!
        //TRIANGLE_STRIP, 0-4
        -0, -10, 0,
        10, 10, 0,
        20, -10, 0,
        30, 10, 0,
        40, -10, 0,

        //LINE_STRIP, 5-8
        -35, 0, 0,
        -25, 0, 0,
        -25, -10, 0,
        -15, -10, 0,

        //LINES-1, 9-10
        30, 30, 0,
        -30, -30, 0,
        //LINES-2, 11-12
        -30, 30, 0,
        30, -30, 0,
    ];

    let numElements = tempArray.length;
    for ( let i=numElements; i<numElements+(10); i++){
        tempArray.push(Math.floor((((Math.random()*2)-1)*30)+1)); // X-value
        tempArray.push(Math.floor((((Math.random()*2)-1)*30)+1)); // Y-value
        tempArray.push(0); // Z-Value
    }
    let triangleVertices = new Float32Array(tempArray);
    vertexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, triangleVertices, gl.STATIC_DRAW);


    // Color-buffer
    let colors = new Float32Array([
        // TRIANGLE_STRIP, 0-4
        0.0, 0.9, 0.7, 1.0,
        0.0, 0.0, 0.7, 1.0,
        0.7, 0.0, 0.7, 1.0,
        0.0, 0.7, 0.0, 1.0,
        0.0, 0.7, 0.0, 1.0,

        // LINE_STRIP, 5-8
        0.1, 0.5, 0.7, 0.0,
        0.3, 0.5, 0.5, 0.0,
        0.6, 0.5, 0.3, 0.0,
        0.9, 0.5, 0.1, 0.0,

        // LINES-1, 9-10
        0.5, 0.9, 0.5, 0.0,
        0.9, 0.5, 0.9, 0.0,

        // LINES-2, 11-12
        0.2, 0.2, 0.2, 0.0,
        0.5, 0.9, 0.3, 0.0,

        // POINT
        0.2, 0.8, 0.3, 0.0,
        0.9, 0.6, 0.8, 0.0,
        0.2, 0.2, 0.8, 0.0,
        0.8, 0.3, 0.8, 0.0,
        0.8, 0.5, 0.7, 0.0,
        0.3, 0.9, 0.1, 0.0,
        0.8, 0.2, 0.3, 0.0,
        0.7, 0.2, 0.0, 0.0,
        0.9, 0.0, 0.9, 0.0,
        0.8, 0.2, 0.8, 0.0
    ]);
    colorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, colors, gl.STATIC_DRAW);

    // Release the binding to the buffer
    gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

function bindShaderParameters() {

    // Matriser: u_modelviewMatrix & u_projectionMatrix
    u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
    u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');

    // Bind the vertex-buffer to shader-parameter a_Position
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
    let a_Position = gl.getAttribLocation(gl.program, 'a_Position');
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    // Bind the color-buffer to shader-parameter a_Color
    gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
    let a_Color = gl.getAttribLocation(gl.program, 'a_Color');
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color)

    // Bind a_PointSize to a variable
    a_PointSize = gl.getAttribLocation(gl.program, 'a_PointSize');

    return true;
}

function draw() {
    // Clear the color-buffer
    gl.clear(gl.COLOR_BUFFER_BIT);

    // BackfaceCulling:
    /*
	gl.frontFace(gl.CW);		//indikerer at trekanter med vertekser angitt i CW er front-facing!
	gl.enable(gl.CULL_FACE);	//enabler culling.
	gl.cullFace(gl.BACK);		//culler baksider.
    */

    // Definerer modellmatrisa (translasjon):
    modelMatrix.setIdentity();//
    //modelMatrix.setRotate(45, 0, 0, 1);
    //modelMatrix.scale(2, 2, 2);
    //modelMatrix.translate(30, 30, 2);
    //modelMatrix.setTranslate(-30, 0, 0);

    // Definerer en viewmatrise (kamera):
    // cuon-utils: Matrix4.prototype.setLookAt = function(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
    let eyeX=0, eyeY=0, eyeZ=100;
    let lookX=0, lookY=0, lookZ=0;
    let upX=0, upY=1, upZ=0;
    viewMatrix.setLookAt(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

    // Definerer Projection-matrix
    projectionMatrix.setPerspective(45, canvas.width / canvas.height, 99, 1000);
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

    //modelMatrix.setTranslate(0, 0, 0);
    //modelMatrix.rotate(0, 0, 0, 1);
    modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkefølge!
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);


    // Drawing TRIANGLE_STRIP
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, 5);

    // Drawing TRIANGLE_STRIP
    gl.drawArrays(gl.LINE_STRIP, 5, 4);

    // Drawing TRIANGLE_STRIP
    gl.drawArrays(gl.LINES, 9, 2);

    // Drawing TRIANGLE_STRIP
    gl.drawArrays(gl.LINES, 11, 2);

    // Drawing POINTS
    gl.vertexAttrib1f(a_PointSize, 4.0);
    gl.drawArrays(gl.POINTS, 13, 1);
    gl.drawArrays(gl.POINTS, 14, 1);
    gl.drawArrays(gl.POINTS, 15, 1);
    gl.drawArrays(gl.POINTS, 16, 1);
    gl.drawArrays(gl.POINTS, 17, 1);
    gl.drawArrays(gl.POINTS, 18, 1);
    gl.drawArrays(gl.POINTS, 19, 1);
    gl.drawArrays(gl.POINTS, 20, 1);
    gl.drawArrays(gl.POINTS, 21, 1);
    gl.drawArrays(gl.POINTS, 22, 1);
}

function main() {

    if (!init())
        return;

    // Initialiser shadere (cuon-utils):
    if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
        console.log('Feil ved initialisering av shaderkoden.');
        return;
    }

    // Initialiserer verteksbuffer:
    initBuffer();

    // Binder shaderparametre:
    if (!bindShaderParameters())
        return;

    // Setter bakgrunnsfarge:
    gl.clearColor(0.3, 0.2, 0.4, 1.0); //RGBA

    // Tegn!
    draw();
}
