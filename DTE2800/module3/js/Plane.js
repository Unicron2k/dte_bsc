"use strict";
class XZPlane {
    constructor(gl, camera, canvas) {
        this.gl = gl;
        this.camera = camera;
        this.canvas = canvas;

        this.xzplanePositionBuffer = null;
        this.xzplaneColorBuffer = null;
    }

    initBuffers() {
        let width = this.canvas.width;
        let height = this.canvas.height;

        // Position
        let xzplanePositions = new Float32Array([
            width / 2, 0, height / 2,
            -width / 2, 0, height / 2,
            width / 2, 0, -height / 2,
            -width / 2, 0, -height / 2
        ]);
        // Colors
        let xzplaneColors = new Float32Array([
            1, 0.222, 0, 0.6,
            1, 0.222, 0, 0.6,
            1, 0.222, 0, 0.6,
            1, 0.222, 0, 0.6
        ]);
        // xzplanePosition-buffer:
        this.xzplanePositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplanePositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, xzplanePositions, this.gl.STATIC_DRAW);
        this.xzplanePositionBuffer.itemSize = 3;
        this.xzplanePositionBuffer.numberOfItems = 4;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);


        // xzplaneColor-buffer:
        this.xzplaneColorBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneColorBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, xzplaneColors, this.gl.STATIC_DRAW);
        this.xzplaneColorBuffer.itemSize = 4;
        this.xzplaneColorBuffer.numberOfItems = 4;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    handleKeys(elapsed) {
        // Implemented if necessary
    }

    draw(elapsed) {
        this.camera.setCamera();

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplanePositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.program, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.xzplanePositionBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneColorBuffer);
        let a_Color = this.gl.getAttribLocation(this.gl.program, 'a_Color');
        this.gl.vertexAttribPointer(a_Color, this.xzplaneColorBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Color);

        let modelMatrix = new Matrix4().setIdentity();

        // Get the modelviewmatrix
        let modelviewMatrix = this.camera.getModelViewMatrix(modelMatrix);

        // Reconnect shader-parameters
        let u_modelviewMatrix = this.gl.getUniformLocation(this.gl.program, "u_modelviewMatrix");
        let u_projectionMatrix = this.gl.getUniformLocation(this.gl.program, "u_projectionMatrix");

        // Sends the projection-and-modelview-matrix to the shader
        this.gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
        this.gl.uniformMatrix4fv(u_projectionMatrix, false, this.camera.projectionMatrix.elements);

        // Draw the plane
        this.gl.drawArrays(this.gl.TRIANGLE_STRIP, 0, this.xzplanePositionBuffer.numberOfItems);
    }
}


