// Contains code sourced from Werner Farstad
"use strict";
class LightSourceIndicator {

    constructor(gl, camera){
        this.gl = gl;
        this.camera = camera;

        this.lightSourcePositionBuffer=null;
        this.cubePositions=null;
    }

    initLightSourceBuffer() {
        let cubeVertices = new Float32Array([
            //Forsiden (pos):
            -1, 1, 1,
            -1, -1, 1,
            1, -1, 1,

            -1, 1, 1,
            1, -1, 1,
            1, 1, 1,

            //H�yre side:
            1, 1, 1,
            1, -1, 1,
            1, -1, -1,

            1, 1, 1,
            1, -1, -1,
            1, 1, -1,

            //Baksiden:
            1, -1, -1,
            -1, -1, -1,
            1, 1, -1,

            -1, -1, -1,
            -1, 1, -1,
            1, 1, -1,

            //Venstre side:
            -1, -1, -1,
            -1, 1, 1,
            -1, 1, -1,

            -1, -1, 1,
            -1, 1, 1,
            -1, -1, -1,

            //Topp:
            -1, 1, 1,
            1, 1, 1,
            -1, 1, -1,

            -1, 1, -1,
            1, 1, 1,
            1, 1, -1,

            //Bunn:
            -1, -1, -1,
            1, -1, 1,
            -1, -1, 1,

            -1, -1, -1,
            1, -1, -1,
            1, -1, 1
        ]);

        this.lightSourcePositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.lightSourcePositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, cubeVertices, this.gl.STATIC_DRAW);
        this.lightSourcePositionBuffer.itemSize = 3;
        this.lightSourcePositionBuffer.numberOfItems = 36;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    draw(elapsed, lights) {
        // Bind og velg rett shader:

        this.gl.useProgram(this.gl.lightSourceShaderProgram);

        // Posisjon:
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.lightSourcePositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.lightSourceShaderProgram, "a_Position");
        this.gl.vertexAttribPointer(a_Position, 3, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        //Kopler til fragmentshader-fargeattributt:
        let u_FragColor = this.gl.getUniformLocation(this.gl.lightSourceShaderProgram, 'u_FragColor');
        let rgba = [254.0/256, 250.0/256, 37.0/256, 1.0];  // gult!
        this.gl.uniform4f(u_FragColor, rgba[0], rgba[1], rgba[2], rgba[3]);

        // Kamera & matriser:
        this.camera.setCamera();
        let modelMatrix = new Matrix4().setIdentity();
        modelMatrix.translate(lights.lightPosition[0], lights.lightPosition[1], lights.lightPosition[2]);
        modelMatrix.scale(0.5, 0.5, 0.5);
        let modelviewMatrix = this.camera.getModelViewMatrix(modelMatrix);

        let u_modelviewMatrix = this.gl.getUniformLocation(this.gl.lightSourceShaderProgram, 'u_modelviewMatrix');
        let u_projectionMatrix = this.gl.getUniformLocation(this.gl.lightSourceShaderProgram, 'u_projectionMatrix');
        this.gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
        this.gl.uniformMatrix4fv(u_projectionMatrix, false, this.camera.projectionMatrix.elements);

        // Tegner:
        this.gl.drawArrays(this.gl.TRIANGLES, 0, this.lightSourcePositionBuffer.numberOfItems);
    }
}