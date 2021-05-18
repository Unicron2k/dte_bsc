// Vertex shader program
let VSHADER_SOURCE =
    'attribute vec3 a_Position;\n' +
	'uniform mat4 u_modelviewMatrix;\n' +
	'uniform mat4 u_projectionMatrix;\n' +
    'void main() {\n' +
    '  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position,1.0);\n' +
    '}\n';

// Fragment shader program
let FSHADER_SOURCE =
    'precision mediump float;\n' +
    'uniform vec4 u_FragColor;\n' +
    'void main() {\n' +
    '  gl_FragColor = u_FragColor;\n' +
    '}\n';

//Globale variabler:
let coneVertices= null;
let coneIndices = null;
let indexBuffer = null;
let vertexBuffer = null;

// "Pekere" som brukes til å sende matrisene til shaderen:
let u_modelviewMatrix = null;
let u_projectionMatrix = null;
let a_Position = null;

// Matrisene:
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null; //sammenslått modell- og viewmatrise.
let projectionMatrix = null;

// View-matrix (Camera-position)
let camPosX =5, camPosY=5, camPosZ=5;
let lookAtX=0, lookAtY=0, lookAtZ=0;
let upX=0, upY=1, upZ=0;

function main() {
	// Hent <canvas> elementet
	let canvas = document.getElementById('webgl');

	modelMatrix = new Matrix4();
	viewMatrix = new Matrix4();
	modelviewMatrix = new Matrix4();
	projectionMatrix = new Matrix4();

	// Rendering context for WebGL:
	let gl = getWebGLContext(canvas);
	if (!gl) {
		console.log('Fikk ikke tak i rendering context for WebGL');
		return;
	}
	// Initialiser shadere:
	if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
		console.log('Feil ved initialisering av shaderkoden.');
		return;
	}

	//Kopler farge:
	let u_FragColor = gl.getUniformLocation(gl.program, 'u_FragColor');
	if (u_FragColor < 0) {
		console.log('Fant ikke uniform-parametret u_FragColor i shaderen!?');
		return;
	}
	let rgba = [0.3,0.5,1.0,1.0];
	gl.uniform4f(u_FragColor, rgba[0],rgba[1],rgba[2],rgba[3]);
	gl.clearColor(1.0, 1.0, 1.0, 1.0);
	gl.clear(gl.COLOR_BUFFER_BIT);

	//Initialiserer verteksbuffer:
	initBuffers(gl);

	// Binding shaders:
	u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
	u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');
	a_Position = gl.getAttribLocation(gl.program, 'a_Position');

	// Bind the vertexBuffer to the shader
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_Position);

	// Set the view-matrix (where to look)
	viewMatrix.setLookAt(camPosX, camPosY, camPosZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
	// Set the projection-matrix (how it should look)
	projectionMatrix.setPerspective(45, canvas.width / canvas.height, 1, 1000);

	// Modify where the model is and how it looks
	modelMatrix.setTranslate(0, 0, 0);
	modelMatrix.rotate(0, 0, 0, 1);
	modelMatrix.scale(1.0,1.0,1.0);
	modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkefølge!

	// Sends the matrices to the shaders
	gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
	gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);


	// Tegner firkanten vha. indeksbuffer og drawElements():
	gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indexBuffer);
	gl.drawElements(gl.TRIANGLE_STRIP, coneIndices.length, gl.UNSIGNED_SHORT,0);
}

//NB! Legg merke til kall på bindBuffer(..., null)
function initBuffers(gl) {
  //n stk 3D vertekser:
  coneVertices = new Float32Array([
	0.75, 0, 0,
	-0.75, 0.5, 0,
	-0.75, 0.4045085,  0.2938925,
	-0.75, 0.1545085,  0.4755285,
	-0.75, -0.1545085, 0.4755285,
	-0.75, -0.4045085, 0.2938925,
	-0.75, -0.5, 0.0,
	-0.75, -0.4045085, -0.2938925,
	-0.75, -0.1545085, -0.4755285,
	-0.75, 0.1545085,  -0.4755285,
	-0.75, 0.4045085,  -0.2938925
  ]);
  //Indekser som utgjår en Cone:
  coneIndices = new Uint16Array([
     0, 1, 2,
     0, 2, 3,
     0, 3, 4,
     0, 4, 5,
     0, 5, 6,
     0, 6, 7,
     0, 7, 8,
     0, 8, 9,
     0, 9, 10,
     0, 10, 1
  ]);

  // Verteksbuffer:
  vertexBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, coneVertices, gl.STATIC_DRAW);
  gl.bindBuffer(gl.ARRAY_BUFFER, null);

  //Indeksbuffer:
  indexBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indexBuffer);
  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, coneIndices, gl.STATIC_DRAW);
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);
}