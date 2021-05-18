// Contains code sourced from Werner Farstad
"use strict";
class XZPlane {
    constructor(gl, camera, size= 50) {
        this.gl = gl;
        this.camera = camera;
        this.xzPlaneSize = size;

        this.xzplanePositionBuffer = null;
        this.xzplaneNormalBuffer = null;
        this.xzplaneTextureBuffer = null;
        this.xzPlaneTexture = null;
        this.texturesLoaded = false;
        this.textureUrl = "textures/planeTexture.png";
    }

    initBuffers() {
        this.initTexture();
        this.initVertexes();
        this.initNormals();
    }

    initVertexes(){
        let xzplanePositions = new Float32Array([
            -this.xzPlaneSize, 0, this.xzPlaneSize,
            this.xzPlaneSize, 0, this.xzPlaneSize,
            -this.xzPlaneSize, 0, -this.xzPlaneSize,

            this.xzPlaneSize, 0, this.xzPlaneSize,
            -this.xzPlaneSize, 0, -this.xzPlaneSize,
            this.xzPlaneSize, 0, -this.xzPlaneSize,
        ]);
        this.xzplanePositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplanePositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, xzplanePositions, this.gl.STATIC_DRAW);
        this.xzplanePositionBuffer.itemSize = 3;
        this.xzplanePositionBuffer.numberOfItems = 6;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initNormals() {
        let xzplaneNormals = new Float32Array([
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,

            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
        ]);
        this.xzplaneNormalBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneNormalBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, xzplaneNormals, this.gl.STATIC_DRAW);
        this.xzplaneNormalBuffer.itemSize = 3;
        this.xzplaneNormalBuffer.numberOfItems = 6;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initTextureBuffer(xzPlaneTextureImage){
        let xzPlaneUVs = new Float32Array([
            0,0,
            1,0,
            0,1,

            1,0,
            0,1,
            1,1
        ]);

        //TEKSTUR-RELATERT:
        this.xzPlaneTexture = this.gl.createTexture();
        //Teksturbildet er nå lastet fra server, send til GPU:
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.xzPlaneTexture);

        //Unngaa at bildet kommer opp-ned:
        this.gl.pixelStorei(this.gl.UNPACK_FLIP_Y_WEBGL, true);
        this.gl.pixelStorei(this.gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, true);   //NB! FOR GJENNOMSIKTIG BAKGRUNN!! Sett også this.gl.blendFunc(this.gl.ONE, this.gl.ONE_MINUS_SRC_ALPHA);

        //Laster teksturbildet til GPU/shader:
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, xzPlaneTextureImage);

        //Teksturparametre:
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.NEAREST);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.NEAREST);

        this.gl.bindTexture(this.gl.TEXTURE_2D, null);

        this.xzplaneTextureBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneTextureBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, xzPlaneUVs, this.gl.STATIC_DRAW);
        this.xzplaneTextureBuffer.itemSize = 2;
        this.xzplaneTextureBuffer.numberOfItems = 6;
    }

    handleKeys(elapsed) {
        // Implemented if necessary
    }

    isPowerOfTwo(value) {
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

    initTexture(){
        const image = new Image();
        // onload-event:
        image.onload = () => {
            if (this.isPowerOfTwo(image.width) && this.isPowerOfTwo(image.height)) {
                this.initTextureBuffer(image);
                this.texturesLoaded = true;
            } else {
                alert("Inavalid texture dimensions!");
            }
        };
        // onerror-event:
        image.onerror = () => {
            alert("Could not locate texture " + this.textureUrl);
        }
        // Starter nedlasting...
        image.src = this.textureUrl;
    }

    draw(elapsed, lights) {
        if(!this.texturesLoaded)
            return;


        //Specify which shader to use
        this.gl.useProgram(this.gl.phongShaderProgram);

        //Teksturspesifikt:
        //Bind til teksturkoordinatparameter i shader:
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneTextureBuffer);
        let a_TextureCoord = this.gl.getAttribLocation(this.gl.phongShaderProgram, "a_TextureCoord");
        this.gl.vertexAttribPointer(a_TextureCoord, this.xzplaneTextureBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_TextureCoord);
        //Aktiver teksturenhet (0):
        this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.xzPlaneTexture);
        //Send inn verdi som indikerer hvilken teksturenhet som skal brukes (her 0):
        let samplerLoc = this.gl.getUniformLocation(this.gl.phongShaderProgram, "uSampler");
        this.gl.uniform1i(samplerLoc, 0);
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplanePositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.xzplanePositionBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.xzplaneNormalBuffer);
        let a_Normal = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Normal');
        if (a_Normal !== -1) {  //-1 dersom a_Normal ikke er i bruk i shaderen.
            this.gl.vertexAttribPointer(a_Normal, this.xzplaneNormalBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
            this.gl.enableVertexAttribArray(a_Normal);
        }
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        //Lysvariabler:
        let u_LightPos = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_lightPosition');
        let u_LightDirection = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_lightDirection');
        let u_DiffuseLightColor = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_diffuseLightColor');
        let u_AmbientLightColor = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_ambientLightColor');
        let u_SpecularLightColor = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_specularLightColor');
        let u_cameraPosition = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_cameraPosition');
        let cameraPosition = [this.camera.camPosX,this.camera.camPosY,this.camera.camPosZ];

        //Gi verdi til lysvariablene:
        this.gl.uniform3fv(u_LightPos, lights.lightPosition);
        this.gl.uniform3fv(u_LightDirection, lights.lightDirection);
        this.gl.uniform3fv(u_DiffuseLightColor, lights.diffuseLightColor);
        this.gl.uniform3fv(u_AmbientLightColor, lights.ambientLightColor);
        this.gl.uniform3fv(u_SpecularLightColor, lights.specularLightColor);
        this.gl.uniform3fv(u_cameraPosition, cameraPosition);

        // Matriser:
        let u_normalMatrix = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_normalMatrix');
        let u_modelMatrix = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_modelMatrix');	//NB!!!!
        let u_modelviewMatrix = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_modelviewMatrix');
        let u_projectionMatrix = this.gl.getUniformLocation(this.gl.phongShaderProgram, 'u_projectionMatrix');


        this.camera.setCamera();
        let modelMatrix = new Matrix4().setIdentity();
        // Get the modelviewMatrix
        let modelviewMatrix = this.camera.getModelViewMatrix(modelMatrix);


        // Sender matriser til shader:
        this.gl.uniformMatrix4fv(u_modelMatrix, false, modelMatrix.elements);
        this.gl.uniformMatrix4fv(u_modelviewMatrix, false, modelviewMatrix.elements);
        this.gl.uniformMatrix4fv(u_projectionMatrix, false, this.camera.projectionMatrix.elements);

        //Beregner og sender inn matrisa som brukes til å transformere normalvektorene:
        let normalMatrix = mat3.create();
        mat3.normalFromMat4(normalMatrix, modelMatrix.elements);  //NB!!! mat3.normalFromMat4! SE: gl-matrix.js
        this.gl.uniformMatrix3fv(u_normalMatrix, false, normalMatrix);

        // Draw the plane
        this.gl.drawArrays(this.gl.TRIANGLE_STRIP, 0, this.xzplanePositionBuffer.numberOfItems);
    }
}


