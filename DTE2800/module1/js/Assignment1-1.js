// HelloPoint2.js. 
// Tegner et punkt...

// Vertex shader program
let VSHADER_SOURCE =
	'attribute vec4 a_Position;\n' +
	'attribute float a_PointSize; \n' +
	'void main() {\n' +
	'gl_Position = a_Position;\n' + 	// Verteksen.
	//'gl_PointSize = 20.0;\n' +         // Setter punktst?rrelse.
	'gl_PointSize = a_PointSize;\n' +
	'}\n';

// Fragment shader program
let FSHADER_SOURCE =
	'void main() {\n' +
	'  gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n' + // Fargeverdi.
	'}\n';

function main() {
	// Hent <canvas> elementet
	let canvas = document.getElementById('webgl');

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

	// Get the storage location of attribute letiable
	let a_Position = gl.getAttribLocation(gl.program, 'a_Position');
	if (a_Position < 0) {
		console.log('Fant ikke parametret a_Position i shaderen!?');
		return;
	}
	let a_PointSize = gl.getAttribLocation(gl.program, 'a_PointSize');
	if (a_PointSize < 0) {
		console.log('Fant ikke parametret a_PoinSize i shaderen!?');
		return;
	}

	// Send vertexposisjonen til parametret a_Position i shaderen:
	gl.vertexAttrib4f(a_Position, -0.2, -0.3, 0.0, 1.0);
	gl.vertexAttrib1f(a_PointSize, 20.0);

	// Set the color for clearing <canvas>
	gl.clearColor(0.3, 0.7, 0.5, 1.0);
	// Clear <canvas>
	gl.clear(gl.COLOR_BUFFER_BIT);

	// Tegner et punkt:
	gl.drawArrays(gl.POINTS, 0, 1);
}