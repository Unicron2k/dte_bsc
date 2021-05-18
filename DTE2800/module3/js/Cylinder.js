"use strict";
class Cylinder {
    constructor(gl, camera, color) {
        this.gl = gl;
        this.camera = camera;
        if (!color)
            this.color = {red:0.8, green:0.4, blue:0.6, alpha:1};
        else
            this.color = color;

        this.cylinderFloat32Vertices = null;
        this.vertexBufferCylinder = null;
    }

    initBuffers() {
        this.initCylinderVertices();
        this.vertexBufferCylinder = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexBufferCylinder);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.cylinderFloat32Vertices, this.gl.STATIC_DRAW);

        this.vertexBufferCylinder.itemSize = 3 + 4;
        this.vertexBufferCylinder.numberOfItems = this.cylinderFloat32Vertices.length/7;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initCylinderVertices(radius=1, width=1, thetaStep =45) {
        //This works, but generates more vertices than strictly necessary...

        thetaStep = thetaStep * (Math.PI /180.0);
        let theta = 0;
        let y1, z1, y2, z2;
        let cylinderTempVertices = [];
        let r=this.color.red, g=this.color.green, b=this.color.blue, a=this.color.alpha;

        while(theta<2*Math.PI){
            // Calculate cartesian coordinates per step
            y1 = radius * Math.cos(theta);
            z1 = radius * Math.sin(theta);
            theta += thetaStep;
            y2 = radius * Math.cos(theta);
            z2 = radius * Math.sin(theta);

            // Push "pie-cuts" of vertices into the array
            cylinderTempVertices.push(
                // Front triangle
                -width/2, 0.0, 0.0,   r,g,b,a,
                -width/2, y1, z1,   r,g,b,a,
                -width/2, y2, z2,   r,g,b,a,


                // Side-triangle 1
                width/2, y1, z1,   b,g,r,a,
                -width/2, y2, z2,   b,g,r,a,
                -width/2, y1, z1,   b,g,r,a,


                //Side-triangle 2
                -width/2, y2, z2,   b,g,r,a,
                width/2, y1, z1,   b,g,r,a,
                width/2, y2, z2,   b,g,r,a,


                // Back triangle
                width/2, y2, z2,   r,g,b,a,
                width/2, y1, z1,   r,g,b,a,
                width/2, 0.0, 0.0,   r,g,b,a,
            );
        }
        this.cylinderFloat32Vertices = new Float32Array(cylinderTempVertices);
    }

    handleKeys(elapsed) {
        // Implemented if necessary
    }

    draw(elapsed, modelMatrix) {
        // Reset the camera
        this.camera.setCamera();

        //Rebind the buffers
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexBufferCylinder);
        let a_Position = this.gl.getAttribLocation(this.gl.program, 'a_Position');
        let stride = (3 + 4) * 4;
        this.gl.vertexAttribPointer(a_Position, 3, this.gl.FLOAT, false, stride, 0);
        this.gl.enableVertexAttribArray(a_Position);

        let a_Color = this.gl.getAttribLocation(this.gl.program, 'a_Color');
        let colorOffset = 3 * 4;
        this.gl.vertexAttribPointer(a_Color, 4, this.gl.FLOAT, false, stride, colorOffset);
        this.gl.enableVertexAttribArray(a_Color);

        //Get the modelviewmatrix
        let modelviewMatrix = this.camera.getModelViewMatrix(modelMatrix);

        // Reconnects the shaders
        let u_modelviewMatrix = this.gl.getUniformLocation(this.gl.program, "u_modelviewMatrix");   // HER!!
        let u_projectionMatrix = this.gl.getUniformLocation(this.gl.program, "u_projectionMatrix"); // HER!!

        // Sends the projection-and-modelview-matrix to the shader
        this.gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
        this.gl.uniformMatrix4fv(u_projectionMatrix, false, this.camera.projectionMatrix.elements);

        //Draw the cylinder
        this.gl.drawArrays(this.gl.TRIANGLES, 0, this.vertexBufferCylinder.numberOfItems);
    }
}