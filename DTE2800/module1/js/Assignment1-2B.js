// Vertex shader program.
// Her er point-size fjernet, kun aktuell når man tegner punkter.
let VSHADER_SOURCE =
    'attribute vec4 a_Position;\n' +
    'attribute vec4 a_Color;\n' +
    'varying vec4 v_Color;\n' +
    'void main() {\n' +
    '  gl_Position = a_Position;\n' + 	// Verteksen.
    '  v_Color = a_Color;\n' +
    '}\n';

// Fragment shader program
let FSHADER_SOURCE =
    'precision mediump float;\n' +
    'varying vec4 v_Color;\n' +
    'void main() {\n' +
    '  gl_FragColor = v_Color;\n' + // Fargeverdi.
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

    //Initialiserer verteksbuffer:
    let n = initVertexBuffers(gl);

    //Tømmer skjermen, setter fargen på canvaset til RGBA:
    gl.clearColor(1.0, 1.0, 1.0, 1.0);
    gl.clear(gl.COLOR_BUFFER_BIT);


    // Tegner trekantene til koordinatsystemet:
    gl.drawArrays(gl.TRIANGLES, 4,3);
    gl.drawArrays(gl.TRIANGLES, 7,3);
    gl.drawArrays(gl.TRIANGLES, 10,3);
    gl.drawArrays(gl.TRIANGLES, 13,3);

    // Tegner hoved-pilen
    gl.drawArrays(gl.TRIANGLES, 16,3);
    gl.drawArrays(gl.TRIANGLES, 17,3);
    gl.drawArrays(gl.TRIANGLES, 19,3);
    gl.drawArrays(gl.TRIANGLES, 19,3);
    gl.drawArrays(gl.TRIANGLES, 22,3);

    // Tegner Linjene til koordinatsystemet:
    //Tegner de
    gl.drawArrays(gl.LINES, 0, 2);
    gl.drawArrays(gl.LINES, 2, 2);

}

function initVertexBuffers(gl) {
    //3 stk 2D vertekser:
    let vertices = new Float32Array([
        //x-line
        -0.95, 0.0, 0,
        0.95, 0.0, 0,
        //y-line
        0.0, -0.95, 0,
        0.0, 0.95, 0,

        //left x arrow
        -0.95, 0.0, 0,
        -0.93, 0.02, 0,
        -0.93, -0.02, 0,

        //right x arrow
        0.95, 0.0, 0,
        0.93, -0.02, 0,
        0.93, 0.02, 0,

        //bottom y arrow
        0.0, -0.95, 0,
        0.02, -0.93, 0,
        -0.02, -0.93, 0,

        //top y arrow
        0.0, 0.95, 0,
        -0.02, 0.93, 0,
        0.02, 0.93, 0,

        //main arrow
        -0.8, 0.25, 0, //16
        -0.55, 0, 0,
        0.3, 0.25, 0,
        0.3, -0.25, 0, //19
        -0.8, -0.25, 0,
        -0.55, 0, 0,

        0.3, -0.625, 0,
        0.3, 0.625, 0,
        0.8, 0, 0

    ]);
    let n = vertices.length / 3; // Antall vertekser, hver verteks består av 3 floats.
    // Oppretter et bufferobjekt:
    let positionBuffer = gl.createBuffer();
    if (!positionBuffer) {
        console.log('Fikk ikke laget et bufferobjekt!?');
        return -1;
    }
    // Binder bufferobjektet:
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
    // Skriver til bufferobjektet:
    gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
    // Finner posisjonen til a_Position i shaderen:
    let posAttrib = gl.getAttribLocation(gl.program, 'a_Position');
    if (posAttrib < 0) {
        console.log('Fant ikke parametret a_Position i shaderen!?');
        return -1;
    }
    // Kople verteksattributtett til bufferobjektet:
    let floatsPerVertex = 3;
    gl.vertexAttribPointer(posAttrib, floatsPerVertex, gl.FLOAT, false, 0, 0);
    // Enabler verteksshaderattributtpekeren:
    gl.enableVertexAttribArray(posAttrib);



    let colors = new Float32Array([
        //x-line
        1.0, 0.0, 0.0, 1.0,
        1.0, 0.0, 0.0, 1.0,

        //y-line
        0.0, 1.0, 0.0, 1.0,
        0.0, 1.0, 0.0, 1.0,

        //left x arrow
        1.0, 0.0, 0.0, 1.0,
        1.0, 0.0, 0.0, 1.0,
        1.0, 0.0, 0.0, 1.0,

        //right x arrow
        1.0, 0.0, 0.0, 1.0,
        1.0, 0.0, 0.0, 1.0,
        1.0, 0.0, 0.0, 1.0,

        //bottom y arrow
        0.0, 1.0, 0.0, 1.0,
        0.0, 1.0, 0.0, 1.0,
        0.0, 1.0, 0.0, 1.0,

        //top y arrow
        0.0, 1.0, 0.0, 1.0,
        0.0, 1.0, 0.0, 1.0,
        0.0, 1.0, 0.0, 1.0,

        //main arrow
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0,
        0.0, 0.7, 0.7, 1.0
    ]);
    let colorBuffer = gl.createBuffer();
    if (!colorBuffer) {
        console.log('Fikk ikke laget et colorBuffer-objekt!?');
        return -1;
    }
    // Binder bufferobjektet:
    gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
    // Skriver til bufferobjektet:
    gl.bufferData(gl.ARRAY_BUFFER, colors, gl.STATIC_DRAW);
    // Finner fargen til a_Color i shaderen:
    let colorAttrib = gl.getAttribLocation(gl.program, 'a_Color');
    if (colorAttrib < 0) {
        console.log('Fant ikke parametret a_Color i shaderen!?');
        return -1;
    }
    // Kople verteksattributtett til bufferobjektet:
    let floatsPerColor = 4;
    gl.vertexAttribPointer(colorAttrib, floatsPerColor, gl.FLOAT, false, 0, 0);
    // Enabler verteksshaderattributtpekeren:
    gl.enableVertexAttribArray(colorAttrib)


    // Kopler fra bufferobjektet:
    gl.bindBuffer(gl.ARRAY_BUFFER, null);

    return n;
}