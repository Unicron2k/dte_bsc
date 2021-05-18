// Globale variabler:

let VSHADER_PYRAMID_SOURCE =
	'attribute vec3 a_Position;\n'+
	'uniform mat4 u_modelviewMatrix;\n'+
	'uniform mat4 u_projectionMatrix;\n'+
	'uniform vec3 u_ambientLightColor;\n'+
	'uniform vec3 u_ambientMaterialColor;\n'+
	'varying vec3 v_Light;\n'+
	'void main() {\n'+
	'  v_Light = u_ambientLightColor * u_ambientMaterialColor;\n'+
	'  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position, 1.0);\n'+
	'}\n';

// Fragmentshader for kuben:
let FSHADER_CUBE_SOURCE =
	'precision mediump float;\n' +
	'varying vec3 v_Light;\n' +
	'void main() {\n' +
	'  gl_FragColor = vec4(v_Light.rgb, 1.0);\n' +
	'}\n';

// Verteksshader for koordinatsystemet:
let VSHADER_COORD_SOURCE =
	'attribute vec3 a_Position;\n' +	// Innkommende verteksposisjon.
	'attribute vec4 a_Color;\n' +		// Innkommende verteksfarge.
	'uniform mat4 u_modelviewMatrix;\n' +
	'uniform mat4 u_projectionMatrix;\n' +
	'varying vec4 v_Color;\n' +			// NB! Bruker varying.
	'void main() {\n' +
	'  gl_Position = u_projectionMatrix * u_modelviewMatrix * vec4(a_Position,1.0);\n' +
	'  v_Color = a_Color;\n' + 			// NB! Setter varying = innkommende verteksfarge.
	'}\n';

// Fragmentshader for koordinatsystemet:
let FSHADER_COORD_SOURCE =
	'precision mediump float;\n' +
	'varying vec4 v_Color;\n' +			// NB! Interpolert fargeverdi.
	'void main() {\n' +
	'  gl_FragColor = v_Color;\n' + 	// Setter gl_FragColor = Interpolert fargeverdi.
	'}\n';

let gl = null;
let canvas = null;

// Kameraposisjon:
let camPosX = 5;
let camPosY = 7;
let camPosZ = 10;
// Kamera ser mot ...
let lookAtX = 0;
let lookAtY = 0;
let lookAtZ = 0;
// Kameraorientering:
let upX = 0;
let upY = 1;
let upZ = 0;

// Tar vare på tastetrykk:
let currentlyPressedKeys = [];

// Verteksbuffer:
let pyramidPositionBuffer = null;
let pyramidTextureBuffer = null;
let pyramidTexture = null;
let pyramidNormalBuffer = null;

let coordPositionBuffer = null;
let coordColorBuffer = null;

let COORD_BOUNDARY = 100;

// "Pekere" som brukes til � sende matrisene til shaderen:
let u_modelviewMatrix = null;
let u_projectionMatrix = null;

// Matrisene:
let modelMatrix = null;
let viewMatrix = null;
let modelviewMatrix = null;
let projectionMatrix = null;

//Animasjon:
let yRot = 0.0;
let orbRot = 0.0;
let lastTime = 0.0;
let scale = 1.0;

//Lysvariablene:
let lightDirection = [-10, 6.0, 10.0];
//let lightDirection = [1.0, 0.0, 0.0];
let ambientLightColor = [0.3, 0.3, 0.3];
let diffuseLightColor = [0.8, 0.8, 0.8];

//Variabel for � beregne og vise FPS:
let fpsData = {};//{}; //Setter fpsData til en tomt objekt.

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
	// VIEW-matrisa:
	// cuon-utils: Matrix4.prototype.setLookAt = function(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ)
	viewMatrix.setLookAt(camPosX, camPosY, camPosZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);

	// PROJECTION-matrisa:
	// cuon-utils: Matrix4.prototype.setPerspective = function(fovy, aspect, near, far)
	projectionMatrix.setPerspective(45, canvas.width / canvas.height, 1, 1000);
}

function initCoordBuffer() {
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

	//Ulike farge for hver akse:
	let coordColors = new Float32Array([
		1.0, 0.0, 0.0, 1,   // X-akse
		1.0, 0.0, 0.0, 1,
		0.0, 1.0, 0.0, 1,   // Y-akse
		0.0, 1.0, 0.0, 1,
		0.0, 0.0, 1.0, 1,   // Z-akse
		0.0, 0.0, 1.0, 1
	]);

	// Verteksbuffer for koordinatsystemet:
	coordPositionBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, coordPositions, gl.STATIC_DRAW);
	coordPositionBuffer.itemSize = 3; 		// NB!!
	coordPositionBuffer.numberOfItems = 6; 	// NB!!
	gl.bindBuffer(gl.ARRAY_BUFFER, null);	// NB!! M� kople fra n�r det opereres med flere buffer! Kopler til i draw().

	//Fargebuffer: oppretter, binder og skriver data til bufret:
	coordColorBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, coordColors, gl.STATIC_DRAW);
	coordColorBuffer.itemSize = 4; 			// 4 float per farge.
	coordColorBuffer.numberOfItems = 6; 	// 6 farger.
	gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

function initPyramidBuffer(textureImage) {
	let pyramidPositions = new Float32Array([
		//Forsiden (pos):
		-1, -1, 1,
		1, -1, 1,
		-0, 2, 0,

		//H�yre side:
		-0, 2, 0,
		1, -1, 1,
		1, -1, -1,

		//Baksiden:
		0, 2, 0,
		1, -1, -1,
		-1, -1, -1,

		//Venstre side:
		-1, -1, 1,
		0, 2, 0,
		-1, -1, -1,

		//Bunn:

		-1, -1, -1,
		1, -1, 1,
		-1, -1, 1,

		-1, -1, -1,
		1, -1, -1,
		1, -1, 1
	]);

	//Setter uv-koordinater for hver enkelt side av terningen vha. en enkel tekstur.
	//Teksturen / .png-fila må se slik ut, dvs. 2 linjer og 3 kolonner, der hver celle
	//inneholder et "bilde" av et tall (1-6).
	// -------------
	// | 1 | 2 | 3 |
	// |-----------|
	// | 4 | 5 | 6 |
	// -------------

	//Holder etter hvert p� alle uv-koordinater for terningen.
	let uvCoords = [];
	//Front (1-tallet):
	let t1=[0.1,1];
	let bl1=[0,0];
	let br1=[0.2,0];
	uvCoords = uvCoords.concat(bl1, br1, t1);

	//H�yre side (2-tallet):
	let t2=[0.3,1];
	let bl2=[0.2,0.0];
	let br2=[0.4,0];
	uvCoords = uvCoords.concat(t2, bl2, br2);

	//Baksiden (6-tallet):
	let t3=[0.5,1];
	let bl3=[0.4,0];
	let br3=[0.6,0];
	uvCoords = uvCoords.concat(t3, bl3, br3);

	//Venstre (5-tallet):
	let t4=[0.7,1];
	let bl4=[0.6,0];
	let br4=[0.8,0];
	uvCoords = uvCoords.concat(bl4, t4, br4);

	//Toppen (3-tallet):
	let tl5=[0.8,1];
	let bl5=[0.8,0];
	let tr5=[1,1];
	let br5=[1,0];
	uvCoords = uvCoords.concat(bl5, tr5, tl5, bl5, br5, tr5);
	let pyramidUVs = new Float32Array(uvCoords);

	// POSISJON:
	pyramidPositionBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidPositionBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, pyramidPositions, gl.STATIC_DRAW);
	pyramidPositionBuffer.itemSize = 3;
	pyramidPositionBuffer.numberOfItems = 18;
	gl.bindBuffer(gl.ARRAY_BUFFER, null);

	//TEKSTUR-RELATERT:
	pyramidTexture = gl.createTexture();
	//Teksturbildet er nå lastet fra server, send til GPU:
	gl.bindTexture(gl.TEXTURE_2D, pyramidTexture);

	//Unngaa at bildet kommer opp-ned:
	gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
	gl.pixelStorei(gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, true);   //NB! FOR GJENNOMSIKTIG BAKGRUNN!! Sett også gl.blendFunc(gl.ONE, gl.ONE_MINUS_SRC_ALPHA);

	//Laster teksturbildet til GPU/shader:
	gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, textureImage);

	//Teksturparametre:
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);

	gl.bindTexture(gl.TEXTURE_2D, null);

	pyramidTextureBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidTextureBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, pyramidUVs, gl.STATIC_DRAW);
	pyramidTextureBuffer.itemSize = 2;
	pyramidTextureBuffer.numberOfItems = 18;


	// NORMALVEKTORER:
	let pyramidNormals = [];
	for (let i =0; i<pyramidPositions.length; i+=9) {
		// Opprette en vektor fra verdier:
		let uv1 = vec3.fromValues(pyramidPositions[i], pyramidPositions[i+1], pyramidPositions[i+2]);
		let uv2 = vec3.fromValues(pyramidPositions[i+3], pyramidPositions[i+4], pyramidPositions[i+5]);
		let uv3 = vec3.fromValues(pyramidPositions[i+6], pyramidPositions[i+7], pyramidPositions[i+8]);

		// Tom vektor:
		let kv1 = vec3.create();
		let kv2 = vec3.create();

		// Differanse mellom to vektorer:
		vec3.subtract(kv1, uv1, uv2);
		vec3.subtract(kv2, uv1, uv3);

		// Kryssprodukt av to vektorer:
		let normalVektor = vec3.create();

		// Kryssprodukt mellom kv1 og kv2:
		vec3.cross(normalVektor, kv1, kv2);

		// Normalisert normalvektor:
		let normalizedNormal = vec3.create();
		vec3.normalize(normalizedNormal, normalVektor);

		pyramidNormals.push(normalizedNormal[0], normalizedNormal[1], normalizedNormal[2]);
		pyramidNormals.push(normalizedNormal[0], normalizedNormal[1], normalizedNormal[2]);
		pyramidNormals.push(normalizedNormal[0], normalizedNormal[1], normalizedNormal[2]);
	}
	//reuse the old name :)
	pyramidNormals = new Float32Array(pyramidNormals);

	pyramidNormalBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidNormalBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, pyramidNormals, gl.STATIC_DRAW);
	pyramidNormalBuffer.itemSize = 3;
	pyramidNormalBuffer.numberOfItems = pyramidNormals.length;
	gl.bindBuffer(gl.ARRAY_BUFFER, null);
}

//NB! Denne tar i mot aktuelt shaderprogram som parameter:
function bindShaderParameters(shaderProgram) {
	// Kopler shaderparametre med Javascript-variabler:
	// Matriser: u_modelviewMatrix & u_projectionMatrix
	u_modelviewMatrix = gl.getUniformLocation(shaderProgram, "u_modelviewMatrix");
	u_projectionMatrix = gl.getUniformLocation(shaderProgram, "u_projectionMatrix");
	return true;
}

function handleKeys(elapsed) {

	let camPosVec = vec3.fromValues(camPosX, camPosY, camPosZ);
	//Enkel rotasjon av kameraposisjonen:
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
	if (currentlyPressedKeys["KeyV"]) {
		vec3.scale(camPosVec, camPosVec, 1.05);
	}
	if (currentlyPressedKeys["KeyB"]) {
		vec3.scale(camPosVec, camPosVec, 0.95);
	}

	camPosX = camPosVec[0];
	camPosY = camPosVec[1];
	camPosZ = camPosVec[2];
	setupCamera();
}

function drawCoord() {
	// NB! PASS PÅ DENNE DERSOM FLERE SHADERPAR ER I BRUK!
	// Binder shaderparametre:
	if (!bindShaderParameters(gl.coordShaderProgram))
		return;
	gl.useProgram(gl.coordShaderProgram);

	gl.bindBuffer(gl.ARRAY_BUFFER, coordPositionBuffer);
	let a_Position = gl.getAttribLocation(gl.coordShaderProgram, "a_Position");
	gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_Position);

	gl.bindBuffer(gl.ARRAY_BUFFER, coordColorBuffer);
	let a_Color = gl.getAttribLocation(gl.coordShaderProgram, "a_Color");
	gl.vertexAttribPointer(a_Color, 4, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_Color);

	//Still inn kamera:
	setupCamera();

	modelMatrix.setIdentity();
	// Sl�r sammen modell & view til modelview-matrise:
	modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkef�lge!

	// Sender matriser til shader:

	gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
	gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

	// Tegner koordinatsystem:
	gl.drawArrays(gl.LINES, 0, coordPositionBuffer.numberOfItems);
}

function drawPyramid() {

	// NB! PASS PÅ DENNE DERSOM FLERE SHADERPAR ER I BRUK!
	// Binder shaderparametre:
	if (!bindShaderParameters(gl.pyramidShaderProgram))
		return;
	gl.useProgram(gl.pyramidShaderProgram);

	//Teksturspesifikt:
	//Bind til teksturkoordinatparameter i shader:
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidTextureBuffer);
	let a_TextureCoord = gl.getAttribLocation(gl.pyramidShaderProgram, "a_TextureCoord");
	gl.vertexAttribPointer(a_TextureCoord, pyramidTextureBuffer.itemSize, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_TextureCoord);
	//Aktiver teksturenhet (0):
	gl.activeTexture(gl.TEXTURE0);
	gl.bindTexture(gl.TEXTURE_2D, pyramidTexture);
	//Send inn verdi som indikerer hvilken teksturenhet som skal brukes (her 0):
	let samplerLoc = gl.getUniformLocation(gl.pyramidShaderProgram, "uSampler");
	gl.uniform1i(samplerLoc, 0);
	gl.bindBuffer(gl.ARRAY_BUFFER, null);

	//Posisjonsspesifikt:
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidPositionBuffer);
	let a_Position = gl.getAttribLocation(gl.pyramidShaderProgram, "a_Position");
	gl.vertexAttribPointer(a_Position, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(a_Position);
	gl.bindBuffer(gl.ARRAY_BUFFER, null);

	// Normalvektor:
	gl.bindBuffer(gl.ARRAY_BUFFER, pyramidNormalBuffer);
	let a_Normal = gl.getAttribLocation(gl.pyramidShaderProgram, 'a_Normal');
	if (a_Normal !== -1) {  //-1 dersom a_Normal ikke er i bruk i shaderen.
		gl.vertexAttribPointer(a_Normal, pyramidNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);
		gl.enableVertexAttribArray(a_Normal);
	}
	gl.bindBuffer(gl.ARRAY_BUFFER, null);

	//Lysvariabler:
	let u_DiffuseLightColor = gl.getUniformLocation(gl.pyramidShaderProgram, 'u_diffuseLightColor');
	let u_AmbientLightColor = gl.getUniformLocation(gl.pyramidShaderProgram, 'u_ambientLightColor');
	let u_lightDirection = gl.getUniformLocation(gl.pyramidShaderProgram, 'u_lightDirection');

	let u_normalMatrix = gl.getUniformLocation(gl.pyramidShaderProgram, 'u_normalMatrix');

	gl.uniform3fv(u_lightDirection, lightDirection);
	gl.uniform3fv(u_AmbientLightColor, ambientLightColor);
	gl.uniform3fv(u_DiffuseLightColor, diffuseLightColor);

	//Still inn kamera:
	setupCamera();

	//M=I*T*O*R*S, der O=R*T
	modelMatrix.setIdentity();
	//Roter  om egen y-akse:
	modelMatrix.rotate(yRot, 0, 1, 0);
	// Sl�r sammen modell & view til modelview-matrise:
	modelviewMatrix = viewMatrix.multiply(modelMatrix); // NB! rekkef�lge!
	// Sender matriser til shader:
	gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
	gl.uniformMatrix4fv(u_projectionMatrix, false, projectionMatrix.elements);

	//Beregner og sender inn matrisa som brukes til å transformere normalvektorene:
	let normalMatrix = mat3.create();
	mat3.normalFromMat4(normalMatrix, modelMatrix.elements);  //NB!!! mat3.normalFromMat4! SE: gl-matrix.js
	gl.uniformMatrix3fv(u_normalMatrix, false, normalMatrix);

	// Tegner trekanten:
	gl.drawArrays(gl.TRIANGLES, 0, pyramidPositionBuffer.numberOfItems);
}

function draw(currentTime) {

	//Sørger for at draw kalles p� nytt:
	requestAnimFrame(draw);

	// GJENNOMSIKTIGHET:
	// Aktiverer fargeblanding (&indirekte gjennomsiktighet):
	//gl.enable(gl.BLEND);
	// Angir blandefunksjon:
	//gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);

	//NB! Backface Culling:
	gl.frontFace(gl.CCW);		//indikerer at trekanter med vertekser angitt i CCW er front-facing!
	gl.enable(gl.CULL_FACE);	//enabler culling.
	gl.cullFace(gl.BACK);		//culler baksider.

	if (currentTime === undefined)
		currentTime = 0; 	//Udefinert f�rste gang.

	//Beregner og viser FPS:
	if (currentTime - fpsData.forrigeTidsstempel >= 1000) { //dvs. et sekund har forl�pt...
		//Viser FPS i .html ("fps" er definert i .html fila):
		document.getElementById("fps").innerHTML = fpsData.antallFrames;
		fpsData.antallFrames = 0;
		fpsData.forrigeTidsstempel = currentTime; //Brukes for � finne ut om det har g�tt 1 sekund - i s� fall beregnes FPS p� nytt.
	}

	//Tar høyde for varierende frame rate:
	let elapsed = 0.0;			// Forl�pt tid siden siste kalle p� draw().
	if (lastTime !== 0.0)		// F�rst gang er lastTime = 0.0.
		elapsed = (currentTime - lastTime)/1000; // Deler p� 1000 for � operere med sekunder.
	lastTime = currentTime;						// Setter lastTime til currentTime.

	let yRotSpeed = 60; 	// Bestemmer hvor fort trekanten skal rotere (uavhengig av FR).
	yRot = yRot + (yRotSpeed * elapsed); 	// Gir ca 60 graders rotasjon per sekund - og 6 sekunder for en hel rotasjon.
	yRot %= 360;								// "Rull rundt" dersom yRot >= 360 grader.

	let orbRotSpeed = 5; 	// Bestemmer hvor fort trekanten skal rotere (uavhengig av FR).
	orbRot = orbRot + (orbRotSpeed * elapsed); 	// Gir ca 60 graders rotasjon per sekund - og 6 sekunder for en hel rotasjon.
	orbRot %= 360;								// "Rull rundt" dersom yRot >= 360 grader.

	//Rensk skjermen:
	gl.clear(gl.COLOR_BUFFER_BIT);

	// LESE BRUKERINPUT;
	handleKeys(elapsed);

	//TEGNER:
	drawCoord();
	drawPyramid();

	//Øker antall frames med 1
	fpsData.antallFrames++;
}

// Teksturen er nå lastet, fortsetter:
function textureLoadedContinue(textureImage) {
	// Initialiserer verteksbuffer:
	initPyramidBuffer(textureImage);
	initCoordBuffer();
	// Start animasjonsløkka:
	draw();
}

function initContext() {
	// Hent <canvas> elementet
	canvas = document.getElementById('webgl');

	// Rendering context for WebGL:
	gl = getWebGLContext(canvas);
	if (!gl) {
		console.log("Fikk ikke tak i rendering context for WebGL");
		return false;
	}

	document.addEventListener('keyup', handleKeyUp, false);
	document.addEventListener('keydown', handleKeyDown, false);

	return true;
}

// Sjekker om value er POT
function isPowerOfTwo1(value) {
	if (value === 0)
		return false;
	while (value !== 1)
	{
		if (value % 2 !== 0)
			return false;
		value = value/2;
	}
	return true;
}

function main() {

	if (!initContext())
		return;

	// Initialiser shadere (cuon-utils).
	// I dette eksemplet brukes to ulike shaderpar.

	// Coord-shader (fra html-fila):
	let coordVertexShaderSource = document.getElementById("coord-vertex-shader").innerHTML;
	let coordFragmentShaderSource = document.getElementById("coord-fragment-shader").innerHTML;
	gl.coordShaderProgram = createProgram(gl, coordVertexShaderSource, coordFragmentShaderSource);
	if (!gl.coordShaderProgram) {
		console.log('Feil ved initialisering av shaderkoden til coord. Sjekk shaderkoden.');
		return;
	}
	// Pyramid-shader:
	let pyramidVertexShaderSource = document.getElementById("pyramid-vertex-shader").innerHTML;
	let pyramidFragmentShaderSource = document.getElementById("pyramid-fragment-shader").innerHTML;
	gl.pyramidShaderProgram = createProgram(gl, pyramidVertexShaderSource, pyramidFragmentShaderSource);
	if (!gl.pyramidShaderProgram) {
		console.log('Feil ved initialisering av shaderkoden. Sjekk shaderkoden.');
		return;
	}

	//Initialiserer matrisene:
	modelMatrix = new Matrix4();
	viewMatrix = new Matrix4();
	modelviewMatrix = new Matrix4();
	projectionMatrix = new Matrix4();

	// Setter bakgrunnsfarge:
	gl.clearColor(0.8, 0.8, 0.8, 1.0); //RGBA
	gl.enable(gl.DEPTH_TEST);
	gl.depthFunc(gl.LESS);

	// Laster ned teksturfil fra server, fortsetter i textureLoadedContinue() når nedlastinga er ferdig:
	//let textureUrl = "textures/placeholder.png";
	let textureUrl = "textures/pyramid_textures.png";
	const image = new Image();
	// onload-event:
	image.onload = function() {
		if (isPowerOfTwo1(image.width) && isPowerOfTwo1(image.height)) {
			document.getElementById("img-width").innerHTML = image.width;
			document.getElementById("img-height").innerHTML = image.height;
			textureLoadedContinue(image);
		} else {
			alert("Teksturens høyde og/eller bredde er ikke POT!");
		}
	};
	// onerror-event:
	image.onerror = function() {
		alert("Finner ikke : " + textureUrl);
	}
	// Starter nedlasting...
	image.src = textureUrl;
}
