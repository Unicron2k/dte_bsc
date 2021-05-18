// Globale letiabler:

// Vertex shader program
let VSHADER_SOURCE =
    'attribute vec3 a_Position;\n' +		//Dersom vec4 trenger vi ikke vec4(a_Position, 1.0) under.
    'attribute vec4 a_Color;\n' +
    'varying vec4 v_Color;\n' +
    'uniform mat4 u_modelviewMatrix;\n' +
    'uniform mat4 u_projectionMatrix;\n' +
    'void main() {\n' +
    '  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position,1.0);\n' +
    '  v_Color = a_Color;\n' +
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

let COORD_BOUNDARY = 100;

// Verteks,color-buffers:
let cubeVertexBuffer = null;
let cubeColorBuffer = null;

// "Pekere" som brukes til å sende matrisene til shaderen:
let u_modelviewMatrix = null;
let u_projectionMatrix = null;

// Matrisene:
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null; //sammenslått modell- og viewmatrise.
let projectionMatrix = null;

// View-matrix (Camera-position)
let camPosX =25, camPosY=60, camPosZ=100;
let lookAtX=0, lookAtY=0, lookAtZ=0;
let upX=0, upY=1, upZ=0;

// Tar vare på tastetrykk:
let currentlyPressedKeys = [];

//Variabel for � beregne og vise FPS:
let fpsData = {};//{}; //Setter fpsData til en tomt objekt.
let lastTime = 0.0;

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


    //Handle camera input keys
    document.addEventListener('keyup', handleKeyUp, false);
    document.addEventListener('keydown', handleKeyDown, false);

    return true;
}

function initBuffer() {

    // Vertex-buffer:
    let triangleVertices = new Float32Array([
        //NB! ClockWise!!
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

    cubeVertexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, triangleVertices, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ARRAY_BUFFER, null);


    // Color-buffer
    let colors = new Float32Array([
        //Forsiden:
        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,

        1.0, 0.0, 0.0, 1,
        1.0, 0.0, 0.0, 1,
        1.0, 0.0, 0.0, 1,

        //Høyre side:
        0.0, 1.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,

        0.0, 1.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,

        //Baksiden:
        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,

        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,
        1.0, 1.0, 0.0, 1,

        //Venstre side:
        0.0, 0.0, 1.0, 1,
        0.0, 0.0, 1.0, 1,
        0.0, 0.0, 1.0, 1,

        0.0, 0.0, 1.0, 1,
        0.0, 0.0, 1.0, 1,
        0.0, 0.0, 1.0, 1,

        //Topp
        1.0, 0.0, 1.0, 1,
        1.0, 0.0, 1.0, 1,
        1.0, 0.0, 1.0, 1,

        1.0, 0.0, 1.0, 1,
        1.0, 0.0, 1.0, 1,
        1.0, 0.0, 1.0, 1,

        //Bunn:
        0.5, 0.7, 0.3, 1,
        0.5, 0.7, 0.3, 1,
        0.5, 0.7, 0.3, 1,

        0.5, 0.7, 0.3, 1,
        0.5, 0.7, 0.3, 1,
        0.5, 0.7, 0.3, 1
    ]);
    cubeColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cubeColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, colors, gl.STATIC_DRAW);
    gl.bindBuffer(gl.ARRAY_BUFFER, null);



    //KOORDINATSYSTEM:
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
    coordPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, coordPositions, gl.STATIC_DRAW);
    coordPositionBuffer.itemSize = 3; 		// NB!!
    coordPositionBuffer.numberOfItems = 6; 	// NB!!
    gl.bindBuffer(gl.ARRAY_BUFFER, null);	// NB!! M� kople fra n�r det opereres med flere buffer! Kopler til i draw().

    //Ulike farge for hver akse:
    var coordColors = new Float32Array([
        1.0, 0.0, 0.0, 1,   // X-akse
        1.0, 0.0, 0.0, 1,
        0.0, 1.0, 0.0, 1,   // Y-akse
        0.0, 1.0, 0.0, 1,
        0.0, 0.0, 1.0, 1,   // Z-akse
        0.0, 0.0, 1.0, 1
    ]);
    //Fargebuffer: oppretter, binder og skriver data til bufret:
    coordColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, coordColors, gl.STATIC_DRAW);
    coordColorBuffer.itemSize = 4; 			// 4 float per farge.
    coordColorBuffer.numberOfItems = 6; 	// 6 farger.
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

}

function bindShaderParameters() {

    // Matriser: u_modelviewMatrix & u_projectionMatrix
    u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
    u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');

    return true;
}

function draw(currentTime) {
    // Clear the color-buffer
    gl.clear(gl.COLOR_BUFFER_BIT);

    if (currentTime === undefined)
        currentTime = 0; 	//Udefinert første gang.
    //Beregner og viser FPS:
    if (currentTime - fpsData.forrigeTidsstempel >= 1000) { //dvs. et sekund har forl�pt...
        //Viser FPS i .html ("fps" er definert i .html fila):
        document.getElementById("fps").innerHTML = fpsData.antallFrames;
        fpsData.antallFrames = 0;
        fpsData.forrigeTidsstempel = currentTime; //Brukes for � finne ut om det har g�tt 1 sekund - i s� fall beregnes FPS p� nytt.
    }
    //Tar h�yde for varierende frame rate:
    let elapsed = 0.0;			// Forl�pt tid siden siste kalle p� draw().
    if (lastTime !== 0.0)		// F�rst gang er lastTime = 0.0.
        elapsed = (currentTime - lastTime)/1000; // Deler p� 1000 for � operere med sekunder.
    lastTime = currentTime;
    //Øker antall frames med 1
    fpsData.antallFrames++;

    // BackfaceCulling:
    /*
	gl.frontFace(gl.CW);		//indikerer at trekanter med vertekser angitt i CW er front-facing!
	gl.enable(gl.CULL_FACE);	//enabler culling.
	gl.cullFace(gl.BACK);		//culler baksider.
    */

    handleKeys(elapsed);
    drawCoord()
    drawCube();
    updateCamera();
    requestAnimFrame(draw);
}


function drawCube(){

    gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexBuffer);
    let a_Position = gl.getAttribLocation(gl.program, "a_Position");
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    gl.bindBuffer(gl.ARRAY_BUFFER, cubeColorBuffer);
    let a_Color = gl.getAttribLocation(gl.program, "a_Color");
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);

    // Definerer modellmatrisa (translasjon):
    //modelMatrix.setIdentity();
    //gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);
    modelMatrix.setTranslate(6, -8, 0);
    modelMatrix.rotate(45, 0, 0, 1);
    modelMatrix.scale(0.5,0.5,0.5);
    modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkefølge!
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);


    // Drawing TRIANGLE_STRIP
    gl.drawArrays(gl.TRIANGLES, 0, 3);
    gl.drawArrays(gl.TRIANGLES, 3, 3);
    gl.drawArrays(gl.TRIANGLES, 6, 3);
    gl.drawArrays(gl.TRIANGLES, 9, 3);
    gl.drawArrays(gl.TRIANGLES, 12, 3);
    gl.drawArrays(gl.TRIANGLES, 15, 3);
    gl.drawArrays(gl.TRIANGLES, 18, 3);
    gl.drawArrays(gl.TRIANGLES, 21, 3);
    gl.drawArrays(gl.TRIANGLES, 24, 3);
    gl.drawArrays(gl.TRIANGLES, 27, 3);
    gl.drawArrays(gl.TRIANGLES, 30, 3);
    gl.drawArrays(gl.TRIANGLES, 33, 3);

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
    gl.clearColor(0.8, 0.8, 0.6, 1.0); //RGBA


    fpsData.antallFrames = 0;
    fpsData.forrigeTidsstempel = 0;
    // Tegn!
    draw();
}




function handleKeyUp(event) {
    if (event.defaultPrevented) {
        return; // Do nothing if event already handled
    }
    currentlyPressedKeys[event.code] = false;
    event.preventDefault();
}

function handleKeyDown(event) {
    if (event.defaultPrevented) {
        return; // Do nothing if event already handled
    }
    currentlyPressedKeys[event.code] = true;
    event.preventDefault();
}

function handleKeys(elapsed) {

    let camPosVec = vec3.fromValues(camPosX, camPosY, camPosZ);
    //Enkel rotasjon av kameraposisjonen:
    if (currentlyPressedKeys["KeyA"]) {
        rotateVector(2, camPosVec, 0, 1, 0);  //Roterer camPosVec 2 grader om y-aksen.
    }
    if (currentlyPressedKeys["KeyD"]) {
        rotateVector(-2, camPosVec, 0, 1, 0);  //Roterer camPosVec 2 grader om y-aksen.
    }
    if (currentlyPressedKeys["KeyW"]) {
        rotateVector(2, camPosVec, 1, 0, 0);  //Roterer camPosVec 2 grader om x-aksen.
    }
    if (currentlyPressedKeys["KeyS"]) {
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
    // VIEW-matrisa:
    // cuon-utils: Matrix4.prototype.setLookAt = function(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ)
    viewMatrix.setLookAt(camPosX, camPosY, camPosZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);

    // PROJECTION-matrisa:
    // cuon-utils: Matrix4.prototype.setPerspective = function(fovy, aspect, near, far)
    projectionMatrix.setPerspective(45, canvas.width / canvas.height, 1, 1000);
}

function drawCoord() {
    //NB! M� sette a_Position p� nytt ETTER at buffer er bundet:
    gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
    let a_Position = gl.getAttribLocation(gl.program, "a_Position");
    gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Position);

    gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
    let a_Color = gl.getAttribLocation(gl.program, "a_Color");
    gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_Color);


    modelMatrix.setIdentity();
    // Sl�r sammen modell & view til modelview-matrise:
    modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkef�lge!

    // Sender matriser til shader:
    gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
    gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

    // Tegner koordinatsystem:
    gl.drawArrays(gl.LINES, 0, coordPositionBuffer.numberOfItems);
}

