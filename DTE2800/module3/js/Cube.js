"use strict";
class Cube {
    constructor(gl, camera, color) {
        this.gl = gl;
        this.camera = camera;
        if (!color){
            // Color-buffer
            // Using this color-matrix allows one to more easily see the cuboidal shapes without proper lighting/shading.
            this.cubeColors = new Float32Array([
                /*
                1.0, 0.125, 0.0, 1,
                0.0, 0.250, 1.0, 1,
                1.0, 0.375, 0.0, 1,
                0.0, 0.500, 1.0, 1,
                1.0, 0.625, 0.0, 1,
                0.0, 0.750, 1.0, 1,
                1.0, 0.875, 0.0, 1,
                0.0, 1.000, 1.0, 1
                 */
                0.0,0.5,1.0,1.0,
                0.0,0.5,1.0,1.0,
                1.0,0.5,0.0,1.0,
                1.0,0.5,0.0,1.0,
                1.0,0.5,0.0,1.0,
                1.0,0.5,0.0,1.0,
                0.0,0.5,1.0,1.0,
                0.0,0.5,1.0,1.0
            ]);
        } else {
            let tempColor = [];
            for(let i =0; i<8; i++){
                Array.prototype.push.apply(tempColor, color)
            }
            this.cubeColors = new Float32Array(tempColor);
        }
        this.cubeVertexBuffer = null;
        this.cubeIndexBuffer = null;
        this.cubeColorBuffer = null;
    }

    initBuffers() {
        // Vertex-buffer:
        let cubeVertices = new Float32Array([
            -1, -1,  1,//FBL 0
            -1,  1,  1,//FTL 1
            1,  1,  1,//FTR 2
            1, -1,  1,//FBR 3

            -1, -1, -1,//BBL 4
            -1,  1, -1,//BTL 5
            1,  1, -1,//BTR 6
            1, -1, -1 //BBR 7
        ]);

        // Indexes from the vertex-buffer that defines a cube
        let cubeIndices = new Uint16Array([
            // Front
            3,0,2,
            0,2,1,
            // Top
            2,1,6,
            1,6,5,
            // Left
            5,1,4,
            1,4,0,
            // Bottom
            4,0,3,
            3,4,7,
            // Right
            7,3,2,
            2,7,6,
            // Back
            7,6,5,
            5,7,4
        ]);


        // Bind vertex buffer
        this.cubeVertexBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.cubeVertexBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, cubeVertices, this.gl.STATIC_DRAW);
        this.cubeVertexBuffer.numberOfItems = 3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        // Bind index-buffer:
        this.cubeIndexBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, this.cubeIndexBuffer);
        this.gl.bufferData(this.gl.ELEMENT_ARRAY_BUFFER, cubeIndices, this.gl.STATIC_DRAW);
        this.cubeIndexBuffer.numberOfItems = cubeIndices.length;
        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, null);

        // Bind color-buffer
        this.cubeColorBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.cubeColorBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.cubeColors, this.gl.STATIC_DRAW);
        this.cubeColorBuffer.itemSize = 4
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    handleKeys(elapsed) {
        // Implemented if necessary
    }

    draw(elapsed, modelMatrix) {
        // Reset the camera
        this.camera.setCamera();

        // Reconnect shader-parameters
        let u_modelviewMatrix = this.gl.getUniformLocation(this.gl.program, "u_modelviewMatrix");   // HER!!
        let u_projectionMatrix = this.gl.getUniformLocation(this.gl.program, "u_projectionMatrix"); // HER!!

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.cubeVertexBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.program, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.cubeVertexBuffer.numberOfItems, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.cubeColorBuffer);
        let a_Color = this.gl.getAttribLocation(this.gl.program, 'a_Color');
        this.gl.vertexAttribPointer(a_Color, this.cubeColorBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Color);

        // Re-binds the index-buffer
        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, this.cubeIndexBuffer);

        // Get the modelviewmatrix
        let modelviewMatrix = this.camera.getModelViewMatrix(modelMatrix);

        // Sends the projection-and-modelview-matrix to the shader
        this.gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
        this.gl.uniformMatrix4fv(u_projectionMatrix, false, this.camera.projectionMatrix.elements);

        // Draws the cube via index-buffer and drawElements()
        this.gl.drawElements(this.gl.TRIANGLE_STRIP, this.cubeIndexBuffer.numberOfItems, this.gl.UNSIGNED_SHORT, 0);
    }
}


