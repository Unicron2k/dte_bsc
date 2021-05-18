// Contains code graciously donated by Andy
"use strict";

class Wheel {
    constructor(gl, camera, posX=0, posZ=0, angle= 0) {
        this.gl = gl;
        this.camera = camera;

        this.wheelPositionBuffer = null;
        this.wheelNormalBuffer = null;
        this.wheelTextureBuffer = null;
        this.wheelTexture = null;
        this.texturesLoaded = false;
        this.textureUrl = "textures/dark_noise.png";
        this.wheelVertices = null;
        this.uvCoords = null;
        this.posX = posX;
        this.posZ = posZ;
        this.angle = angle;
        this.steeringRot =0;

    }

    initBuffers() {
        this.initTexture();
        this.initVertexes();
        this.initNormals();
    }

    initVertexes(){
        let radius=1, width=1, thetaStep = 20;
        thetaStep = thetaStep * (Math.PI /180.0);
        let theta = 0;
        let y1, z1, y2, z2;
        let cylinderTempVertices = [];

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

                -width/2, y1, z1,
                -width/2, 0.0, 0.0,
                -width/2, y2, z2,

                // Side-triangle 1
                -width/2, y2, z2,
                width/2, y1, z1,
                -width/2, y1, z1,


                //Side-triangle 2
                width/2, y1, z1,
                -width/2, y2, z2,
                width/2, y2, z2,


                // Back triangle
                width/2, y1, z1,
                width/2, y2, z2,
                width/2, 0.0, 0.0,
            );
        }
        this.wheelVertices = new Float32Array(cylinderTempVertices);


        this.wheelPositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelPositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.wheelVertices, this.gl.STATIC_DRAW);
        this.wheelPositionBuffer.itemSize = 3;
        this.wheelPositionBuffer.numberOfItems = this.wheelVertices.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initNormals() {
        let carNormals = [];
        for (let i =0; i<this.wheelVertices.length; i+=9) {
            // Opprette en vektor fra verdier:
            let uv1 = vec3.fromValues(this.wheelVertices[i], this.wheelVertices[i+1], this.wheelVertices[i+2]);
            let uv2 = vec3.fromValues(this.wheelVertices[i+3], this.wheelVertices[i+4], this.wheelVertices[i+5]);
            let uv3 = vec3.fromValues(this.wheelVertices[i+6], this.wheelVertices[i+7], this.wheelVertices[i+8]);

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

            carNormals.push(
                normalizedCross[0], normalizedCross[1], normalizedCross[2],
                normalizedCross[0], normalizedCross[1], normalizedCross[2],
                normalizedCross[0], normalizedCross[1], normalizedCross[2]
            );
        }
        //reuse the old name :)
        carNormals = new Float32Array(carNormals);

        this.wheelNormalBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelNormalBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, carNormals, this.gl.STATIC_DRAW);
        this.wheelNormalBuffer.itemSize = 3;
        this.wheelNormalBuffer.numberOfItems = carNormals.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initTextureBuffer(xzPlaneTextureImage){
        let wheelUvs = [];
        for(let i=0; i<this.wheelVertices.length; i+=9){
            wheelUvs.push(0,1,0,0,1,1,1,0)
        }
        wheelUvs = new Float32Array(wheelUvs);

        //TEKSTUR-RELATERT:
        this.wheelTexture = this.gl.createTexture();
        //Teksturbildet er nå lastet fra server, send til GPU:
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.wheelTexture);

        //Unngaa at bildet kommer opp-ned:
        this.gl.pixelStorei(this.gl.UNPACK_FLIP_Y_WEBGL, true);
        this.gl.pixelStorei(this.gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, true);   //NB! FOR GJENNOMSIKTIG BAKGRUNN!! Sett også this.gl.blendFunc(this.gl.ONE, this.gl.ONE_MINUS_SRC_ALPHA);

        //Laster teksturbildet til GPU/shader:
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, xzPlaneTextureImage);

        //Teksturparametre:
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.NEAREST);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.NEAREST);

        this.gl.bindTexture(this.gl.TEXTURE_2D, null);

        this.wheelTextureBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelTextureBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, wheelUvs, this.gl.STATIC_DRAW);
        this.wheelTextureBuffer.itemSize = 2;
        this.wheelTextureBuffer.numberOfItems = wheelUvs.length/2;
    }

    handleKeys(elapsed) {
        // Implemented if necessary
    }

    setSteeringRot(angle){
        this.steeringRot=angle;
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

    draw(elapsed, lights, posX, posZ, angle) {
        if(!this.texturesLoaded)
            return;


        //Specify which shader to use
        this.gl.useProgram(this.gl.phongShaderProgram);

        //Teksturspesifikt:
        //Bind til teksturkoordinatparameter i shader:
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelTextureBuffer);
        let a_TextureCoord = this.gl.getAttribLocation(this.gl.phongShaderProgram, "a_TextureCoord");
        this.gl.vertexAttribPointer(a_TextureCoord, this.wheelTextureBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_TextureCoord);
        //Aktiver teksturenhet (0):
        this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.wheelTexture);
        //Send inn verdi som indikerer hvilken teksturenhet som skal brukes (her 0):
        let samplerLoc = this.gl.getUniformLocation(this.gl.phongShaderProgram, "uSampler");
        this.gl.uniform1i(samplerLoc, 0);
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelPositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.wheelPositionBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.wheelNormalBuffer);
        let a_Normal = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Normal');
        if (a_Normal !== -1) {  //-1 dersom a_Normal ikke er i bruk i shaderen.
            this.gl.vertexAttribPointer(a_Normal, this.wheelNormalBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
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



        modelMatrix.translate(posX, 0, posZ);
        modelMatrix.rotate(angle, 0,1,0);

        modelMatrix.translate(this.posX, 0.5, this.posZ);
        modelMatrix.rotate(this.angle+this.steeringRot, 0,1,0);
        modelMatrix.scale(0.5, 0.5, 0.5);

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
        this.gl.drawArrays(this.gl.TRIANGLES, 0, this.wheelPositionBuffer.numberOfItems);
    }
}
