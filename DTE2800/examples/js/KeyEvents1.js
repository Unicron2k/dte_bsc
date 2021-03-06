// NB! Se log for output.

// Globale variabler:

// Vertex shader program
let VSHADER_SOURCE =
  'attribute vec3 a_Position;\n' +  //Dersom vec4 trenger vi ikke vec4(a_Position, 1.0) under.
  'uniform mat4 u_modelviewMatrix;\n' +
  'uniform mat4 u_projectionMatrix;\n' +
  'void main() {\n' +
  '  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position, 1.0);\n' +
  '}\n';

// Fragment shader program
let FSHADER_SOURCE =
  'precision mediump float;\n' +
  'uniform vec4 u_FragColor;\n' + 	// bruker prefiks u_ for å indikere uniform
  'void main() {\n' +
  '  gl_FragColor = u_FragColor;\n' + // Fargeverdi.
  '}\n';

let gl = null;
let canvas = null;

// Verteksbuffer:
let vertexBuffer = null;

// "Pekere" som brukes til å sende matrisene til shaderen:
let u_modelviewMatrix = null;
let u_projectionMatrix = null;

// Matrisene:
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null;
let projectionMatrix = null;

// Animasjon:
let angle = 0.0;
let lastTime = 0.0;
let scale = 1.0;

//Variabel for å beregne og vise FPS:
let fpsData = new Object();//{}; //Setter fpsData til en tomt objekt.

function initContext() {
	// Hent <canvas> elementet
	canvas = document.getElementById('webgl');

	// Rendering context for WebGL:
	gl = getWebGLContext(canvas);
	if (!gl) {
		console.log('Fikk ikke tak i rendering context for WebGL');
		return false;
	}

	document.addEventListener('keydown', handleKeyDown, false);
	document.addEventListener('keyup', handleKeyUp, false);
	document.addEventListener('keypress', handleKeyPress, false);
	return true;
}

function handleKeyDown(event) {
	//keyCode: representerer fysisk knapp på tastaturet (dersom A trykkes er denne 65, samme enten det er stor eller liten A trykjes).
	//keyChar: representerer tegnsettkode til tegnet som trykkes (vil være ulik for liten og stor A).
	//Se consollet til Firebug i Firefox:
	console.log("Keydown, keyCode=%d, charCode=%d", event.keyCode, event.charCode);


	//Ved trykk på pil opp/ned skalerer vi trekanten:
	if (event.keyCode == 38) {  //pil opp
		scale += 0.1;
	}
	if (event.keyCode == 40) {  //pil ned
		scale -= 0.1;
	}
}

function handleKeyUp(event) {
	//keyCode: representerer fysisk knapp på tastaturet (dersom A trykkes er denne 65, samme enten det er stor eller liten A trykjes).
	//keyChar: representerer tegnsettkode til tegnet som trykkes (vil være ulik for liten og stor A).
	console.log("Keyup, keyCode=%d, charCode=%d", event.keyCode, event.charCode);
}

function handleKeyPress(event) {
	//keyCode: representerer fysisk knapp på tastaturet (dersom A trykkes er denne 65, samme enten det er stor eller liten A trykjes).
	//keyChar: representerer tegnsettkode til tegnet som trykkes (vil være ulik for liten og stor A).
	console.log("Keypress, keyCode=%d, charCode=%d", event.keyCode, event.charCode);
}

function initBuffer() {
	// 3 stk 3D vertekser:
	let triangleVertices = new Float32Array([
       -10, -10, 0,
       0, 10, 0,
       10, -10, 0
    ]);

	// Verteksbuffer:
	vertexBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, triangleVertices, gl.STATIC_DRAW);

	vertexBuffer.itemSize = 3; // NB!!
	vertexBuffer.numberOfItems = 3; // NB!!
	gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

function bindShaderParameters() {
	// Kopler shaderparametre med Javascript-variabler:

	// Farge: u_FragColor (bruker samme farge på alle piksler/fragmenter):
	let u_FragColor = gl.getUniformLocation(gl.program, 'u_FragColor');
	if (u_FragColor < 0) {
		console.log('Fant ikke uniform-parametret u_FragColor i shaderen!?');
		return false;
	}
	let rgba = [ 0.3, 0.5, 1.0, 1.0 ];
	gl.uniform4f(u_FragColor, rgba[0], rgba[1], rgba[2], rgba[3]);

	// Matriser: u_modelviewMatrix & u_projectionMatrix
	u_modelviewMatrix = gl.getUniformLocation(gl.program, 'u_modelviewMatrix');
	u_projectionMatrix = gl.getUniformLocation(gl.program, 'u_projectionMatrix');

	return true;
}

function draw(currentTime) {

	//Sørger for at draw kalles på nytt:
	requestAnimFrame(draw);

	if (currentTime == undefined)
		currentTime = 0; 	//Udefinert første gang.

	//Beregner og viser FPS:
	if (currentTime - fpsData.forrigeTidsstempel >= 1000) { //dvs. et sekund har forløpt...
		//Viser FPS i .html ("fps" er definert i .html fila):
		document.getElementById("fps").innerHTML = fpsData.antallFrames;
		fpsData.antallFrames = 0;
		fpsData.forrigeTidsstempel = currentTime; //Brukes for å finne ut om det har gått 1 sekund - i så fall beregnes FPS på nytt.
	}

	//Tar høyde for varierende frame rate:
	let rotationsSpeed = 60; 	// Bestemmer hvor fort trekanten skal rotere (uavhengig av FR).
	let elapsed = 0.0;			// Forløpt tid siden siste kalle på draw().
	if (lastTime != 0.0)		// Først gang er lastTime = 0.0.
      elapsed = (currentTime - lastTime)/1000; // Deler på 1000 for å operere med sekunder.

    lastTime = currentTime;		// Setter lastTime til currentTime.
    angle = angle + (rotationsSpeed * elapsed); // Gir ca 60 graders rotasjon per sekund - og 6 sekunder for en hel rotasjon.
	angle %= 360;				// "Rull rundt" dersom angle >= 360 grader.

	gl.clear(gl.COLOR_BUFFER_BIT);

	gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
	// Posisjon: a_Position
	let a_Position = gl.getAttribLocation(gl.program, 'a_Position');
	gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_Position);

	// Definerer modellmatrisa (rotasjon):
	modelMatrix.setRotate(angle, 0, 0, 1);
	modelMatrix.scale(scale, scale, scale);

	// Definerer en viewmatrise (kamera):
	// cuon-utils: Matrix4.prototype.setLookAt = function(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
	viewMatrix.setLookAt(0, 0, 100, 0, 0, 0, 0, 1, 0);

	// Slår sammen modell & view til modelview-matrise:
	modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkefølge!

	// Definerer en projeksjonsmatrise (frustum):
	// cuon-utils: Matrix4.prototype.setPerspective = function(fovy, aspect, near, far)
	projectionMatrix.setPerspective(45, canvas.width / canvas.height, 1, 100);

	// Sender matriser til shader:
	gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
	gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

	// Tegner trekanten:
	gl.drawArrays(gl.TRIANGLES, 0, vertexBuffer.numberOfItems);

	//Øker antall frames med 1
	fpsData.antallFrames++;
}

function main() {

	if (!initContext())
		return;

	let uri = document.baseURI;
	document.getElementById("uri").innerHTML = uri;

	// Initialiser shadere (cuon-utils):
	if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
		console.log('Feil ved initialisering av shaderkoden.');
		return;
	}

	//Initialiserer matrisen:
	modelMatrix = new Matrix4();
	viewMatrix = new Matrix4();
	modelviewMatrix = new Matrix4();
	projectionMatrix = new Matrix4();

	// Initialiserer verteksbuffer:
	initBuffer();

	// Binder shaderparametre:
	if (!bindShaderParameters())
		return;

	// Setter bakgrunnsfarge:
	gl.clearColor(0.3, 0.5, 0.4, 1.0); //RGBA

	// Initialiserer variabel for beregning av FPS:
	fpsData.antallFrames = 0;
	fpsData.forrigeTidsstempel = 0;

	// Start animasjonsløkke:
	draw();
}
