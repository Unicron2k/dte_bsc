// Contains code for model-generation graciously donated by Andy
"use strict";
class House {
    constructor(gl, camera, textureUrl, posX=0, posZ=0, angle=0, size = 3) {
        this.gl = gl;
        this.camera = camera;

        this.housePositionBuffer = null;
        this.houseNormalBuffer = null;
        this.houseTextureBuffer = null;
        this.houseTexture = null;
        this.texturesLoaded = false;
        this.textureUrl = textureUrl;
        this.houseVertices = new Float32Array([
            // 1
            -2, 1, 1,
            -2, -1, 1,
            2, -1, 1,
            -2, 1, 1,
            2, -1, 1,
            2, 1, 1,

            // 2
            2, 1, 1,
            2, -1, 1,
            2, -1, -1,
            2, 1, 1,
            2, -1, -1,
            2, 1, -1,

            // 3
            2, -1, -1,
            -2, -1, -1,
            2, 1, -1,
            -2, -1, -1,
            -2, 1, -1,
            2, 1, -1,

            // 4
            -2, -1, -1,
            -2, 1, 1,
            -2, 1, -1,
            -2, -1, 1,
            -2, 1, 1,
            -2, -1, -1,

            // 5
            2, 2, 0,
            2, 1, 1,
            2, 1, -1,

            // 6
            -2, 2, 0,
            -2, 1, -1,
            -2, 1, 1,

            // 7
            -2, 2, 0,
            -2, 1, 1,
            2, 1, 1,
            -2, 2, 0,
            2, 1, 1,
            2, 2, 0,

            // 8
            2, 2, 0,
            2, 1, -1,
            -2, 1, -1,
            2, 2, 0,
            -2, 1, -1,
            -2, 2, 0
        ]);
        this.uvCoords = null;
        this.posX = posX;
        this.posZ = posZ;
        this.angle = angle;
        this.size = size;

    }

    initBuffers() {
        this.initTexture();
        this.initNormals();
        this.initVertexes();
    }

    initVertexes(){
        this.housePositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.housePositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.houseVertices, this.gl.STATIC_DRAW);
        this.housePositionBuffer.itemSize = 3;
        this.housePositionBuffer.numberOfItems = this.houseVertices.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initNormals() {
        let houseNormals = [];
        for (let i =0; i<this.houseVertices.length; i+=9) {
            // Opprette en vektor fra verdier:
            let uv1 = vec3.fromValues(this.houseVertices[i], this.houseVertices[i+1], this.houseVertices[i+2]);
            let uv2 = vec3.fromValues(this.houseVertices[i+3], this.houseVertices[i+4], this.houseVertices[i+5]);
            let uv3 = vec3.fromValues(this.houseVertices[i+6], this.houseVertices[i+7], this.houseVertices[i+8]);

            // Tom vektor:
            let kv1 = vec3.create();
            let kv2 = vec3.create();

            // Differanse mellom to vektorer:
            vec3.subtract(kv1, uv1, uv2);
            vec3.subtract(kv2, uv1, uv3);

            // Kryssprodukt av to vektorer:
            let cross = vec3.create();

            // Kryssprodukt mellom kv1 og kv2:
            vec3.cross(cross, kv1, kv2);

            // Normalisert normalvektor:
            let normalizedCross = vec3.create();
            vec3.normalize(normalizedCross, cross);

            houseNormals.push(
                normalizedCross[0], normalizedCross[1], normalizedCross[2],
                normalizedCross[0], normalizedCross[1], normalizedCross[2],
                normalizedCross[0], normalizedCross[1], normalizedCross[2]
            );
        }
        //reuse the old name :)
        houseNormals = new Float32Array(houseNormals);

        this.houseNormalBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.houseNormalBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, houseNormals, this.gl.STATIC_DRAW);
        this.houseNormalBuffer.itemSize = 3;
        this.houseNormalBuffer.numberOfItems = houseNormals.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initTextureBuffer(xzPlaneTextureImage){
        let houseUVs = [];
        // Setter opp koordinater for husets tekstur
        {
            // 1
            let tl1 = [0, 0.75];
            let bl1 = [0, 0.375];
            let tr1 = [0.375, 0.75];
            let br1 = [0.375, 0.375];
            houseUVs = houseUVs.concat(tl1, bl1, br1, tl1, br1, tr1);

            // 2
            let tl2 = [0.375, 0.75];
            let bl2 = [0.375, 0.375];
            let tr2 = [0.625, 0.75];
            let br2 = [0.625, 0.375];
            houseUVs = houseUVs.concat(tl2, bl2, br2, tl2, br2, tr2);

            // 3
            let tl3 = [0.625, 0.75];
            let bl3 = [0.625, 0.375];
            let tr3 = [1, 0.75];
            let br3 = [1, 0.375];
            houseUVs = houseUVs.concat(bl3, br3, tl3, br3, tr3, tl3);

            // 4
            let tl4 = [0.375, 0.375];
            let bl4 = [0.375, 0];
            let tr4 = [0.625, 0.375];
            let br4 = [0.625, 0];
            houseUVs = houseUVs.concat(bl4, tr4, tl4, br4, tr4, bl4);

            // 5
            let bl5 = [0.375, 0.75];
            let t5 = [0.5, 1];
            let br5 = [0.625, 0.75];
            houseUVs = houseUVs.concat(t5, bl5, br5);

            // 6
            let bl6 = [0.625, 0];
            let t6 = [0.75, 0.25];
            let br6 = [0.875, 0];
            houseUVs = houseUVs.concat(t6, bl6, br6);

            // 7_8
            let tl7_8 = [0, 0.3125];
            let bl7_8 = [0, 0];
            let tr7_8 = [0.375, 0.3125];
            let br7_8 = [0.375, 0];
            houseUVs = houseUVs.concat(tl7_8, bl7_8, br7_8, tl7_8, br7_8, tr7_8);
            houseUVs = houseUVs.concat(tl7_8, bl7_8, br7_8, tl7_8, br7_8, tr7_8);
        }
        houseUVs = new Float32Array(houseUVs);

        //TEKSTUR-RELATERT:
        this.houseTexture = this.gl.createTexture();
        //Teksturbildet er nå lastet fra server, send til GPU:
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.houseTexture);

        //Unngaa at bildet kommer opp-ned:
        this.gl.pixelStorei(this.gl.UNPACK_FLIP_Y_WEBGL, true);
        this.gl.pixelStorei(this.gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, true);   //NB! FOR GJENNOMSIKTIG BAKGRUNN!! Sett også this.gl.blendFunc(this.gl.ONE, this.gl.ONE_MINUS_SRC_ALPHA);

        //Laster teksturbildet til GPU/shader:
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, xzPlaneTextureImage);

        //Teksturparametre:
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.NEAREST);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.NEAREST);

        this.gl.bindTexture(this.gl.TEXTURE_2D, null);

        this.houseTextureBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.houseTextureBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, houseUVs, this.gl.STATIC_DRAW);
        this.houseTextureBuffer.itemSize = 2;
        this.houseTextureBuffer.numberOfItems = houseUVs.length/2;
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
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.houseTextureBuffer);
        let a_TextureCoord = this.gl.getAttribLocation(this.gl.phongShaderProgram, "a_TextureCoord");
        this.gl.vertexAttribPointer(a_TextureCoord, this.houseTextureBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_TextureCoord);
        //Aktiver teksturenhet (0):
        this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.houseTexture);
        //Send inn verdi som indikerer hvilken teksturenhet som skal brukes (her 0):
        let samplerLoc = this.gl.getUniformLocation(this.gl.phongShaderProgram, "uSampler");
        this.gl.uniform1i(samplerLoc, 0);
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.housePositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.housePositionBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.houseNormalBuffer);
        let a_Normal = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Normal');
        if (a_Normal !== -1) {  //-1 dersom a_Normal ikke er i bruk i shaderen.
            this.gl.vertexAttribPointer(a_Normal, this.houseNormalBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
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
        modelMatrix.translate(this.posX, 2, this.posZ);
        modelMatrix.rotate(this.angle, 0,1,0);
        modelMatrix.scale(this.size,this.size,this.size);
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
        this.gl.drawArrays(this.gl.TRIANGLES, 0, this.housePositionBuffer.numberOfItems);
    }
}


