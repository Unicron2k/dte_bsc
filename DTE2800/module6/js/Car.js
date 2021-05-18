// Contains code for model-generation graciously donated by Andy
"use strict";

class Car {
    constructor(gl, camera, textureUrl, posX=0, posZ=0, angle=0) {
        this.gl = gl;
        this.camera = camera;

        this.carPositionBuffer = null;
        this.carNormalBuffer = null;
        this.carTextureBuffer = null;
        this.carTexture = null;
        this.texturesLoaded = false;
        this.textureUrl = textureUrl;
        this.carVertices = null;
        this.uvCoords = null;
        this.posX = posX;
        this.posZ = posZ;
        this.angle = angle;
        this.steeringRot = 0;
        this.wheelFrontRight = null;
        this.wheelRearRight = null;
        this.wheelFrontLeft = null;
        this.wheelRearLeft = null;

    }

    initBuffers() {
        this.initTexture();
        this.initVertexes();
        this.initNormals();


        this.wheelFrontRight = new Wheel(this.gl, this.camera, 2, +2, 90);
        this.wheelRearRight = new Wheel(this.gl, this.camera, -3, +2, 90);
        this.wheelFrontLeft = new Wheel(this.gl, this.camera, 2, -2, 90);
        this.wheelRearLeft = new Wheel(this.gl, this.camera, -3, -2, 90);

        this.wheelFrontRight.initBuffers();
        this.wheelRearRight.initBuffers();
        this.wheelFrontLeft.initBuffers();
        this.wheelRearLeft.initBuffers();

    }

    initVertexes(){
        this.carVertices = new Float32Array([

            // 1_RightSide
            -3, 1, 1,
            -3, 0, 1,
            2, 0, 1,
            -3, 1, 1,
            2, 0, 1,
            2, 1, 1,

            // 2_Front
            2, 1, 1,
            2, 0, 1,
            2, 0, -1,
            2, 1, 1,
            2, 0, -1,
            2, 1, -1,

            // 3_rightWindow
            0.5, 1.25, 1,
            0.5, 1, 1,
            2, 1, 1,
            0.5, 1.25, 1,
            2, 1, 1,
            2, 1.25, 1,

            // 4_rightWindow
            0.5, 2.25, 1,
            0.5, 1.25, 1,
            1.25, 1.25, 1,
            0.5, 2.25, 1,
            1.25, 1.25, 1,
            1.25, 2.25, 1,

            // 5_rightWindow
            1.25, 2.25, 1,
            1.25, 1.25, 1,
            2, 1.25, 1,

            // 6_frontWindow
            1.25, 2.25, 1,
            2, 1.25, 1,
            2, 1.25, -1,
            1.25, 2.25, 1,
            2, 1.25, -1,
            1.25, 2.25, -1,

            // 7
            2, 1.25, 1,
            2, 1, 1,
            2, 1, -1,
            2, 1.25, 1,
            2, 1, -1,
            2, 1.25, -1,

            // Roof
            0.5, 2.25, 1,
            1.25, 2.25, 1,
            1.25, 2.25, -1,
            0.5, 2.25, 1,
            1.25, 2.25, -1,
            0.5, 2.25, -1,

            // BackWindow
            0.5, 2.25, -1,
            0.5, 1, -1,
            0.5, 1, 1,
            0.5, 2.25, -1,
            0.5, 1, 1,
            0.5, 2.25, 1,

            // 3_leftWindow_bottom
            //bottom_right
            2, 1, -1,//left
            0.5, 1, -1,//bottom
            0.5, 1.25, -1,//top

            //top_left
            2, 1.25, -1,//top
            2, 1, -1,//bottom
            0.5, 1.25, -1,//right

            // 4_leftWindow_top
            //bottom_right
            1.25, 1.25, -1,//left
            0.5, 1.25, -1,//bottom
            0.5, 2.25, -1,//top

            //top_left
            1.25, 2.25, -1,//top
            1.25, 1.25, -1,//bottom
            0.5, 2.25, -1,//right

            // 5_leftWindow
            1.25, 1.25, -1,
            1.25, 2.25, -1,
            2, 1.25, -1,

            // 8_Bed
            -3, 1, -1,
            -3, 1, 1,
            0.5, 1, 1,
            -3, 1, -1,
            0.5, 1, 1,
            0.5, 1, -1,

            // 9_Back
            -3, 1, -1,
            -3, 0, -1,
            -3, 0, 1,
            -3, 1, -1,
            -3, 0, 1,
            -3, 1, 1,

            // 2_LeftSide
            2, 1, -1,
            2, 0, -1,
            -3, 0, -1,
            2, 1, -1,
            -3, 0, -1,
            -3, 1, -1,

            // Bottom
            -3, 0, 1,
            -3, 0, -1,
            2, 0, -1,
            -3, 0, 1,
            2, 0, -1,
            2, 0, 1
        ]);

        this.carPositionBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carPositionBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.carVertices, this.gl.STATIC_DRAW);
        this.carPositionBuffer.itemSize = 3;
        this.carPositionBuffer.numberOfItems = this.carVertices.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initNormals() {
        let carNormals = [];
        for (let i =0; i<this.carVertices.length; i+=9) {
            // Opprette en vektor fra verdier:
            let uv1 = vec3.fromValues(this.carVertices[i], this.carVertices[i+1], this.carVertices[i+2]);
            let uv2 = vec3.fromValues(this.carVertices[i+3], this.carVertices[i+4], this.carVertices[i+5]);
            let uv3 = vec3.fromValues(this.carVertices[i+6], this.carVertices[i+7], this.carVertices[i+8]);

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

        this.carNormalBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carNormalBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, carNormals, this.gl.STATIC_DRAW);
        this.carNormalBuffer.itemSize = 3;
        this.carNormalBuffer.numberOfItems = carNormals.length/3;
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);
    }

    initTextureBuffer(xzPlaneTextureImage){
        let carUVs = [];
        // Setter opp koordinater for husets tekstur
        {
            // 1
            let tl1 = [0, 0.4];
            let bl1 = [0, 0];
            let tr1 = [0.5, 0.4];
            let br1 = [0.5, 0];
            carUVs = carUVs.concat(tl1, bl1, br1, tl1, br1, tr1);

            // 2
            let tl2 = [0, 0.8];
            let bl2 = [0, 0.5];
            let tr2 = [0.25, 0.8];
            let br2 = [0.25, 0.5];
            carUVs = carUVs.concat(tl2, bl2, br2, tl2, br2, tr2);

            // 3
            let tl3 = [0.25, 0.5];
            let bl3 = [0.25, 0.4];
            let tr3 = [0.5, 0.5];
            let br3 = [0.5, 0.4];
            carUVs = carUVs.concat(tl3, bl3, br3, tl3, br3, tr3);

            // 4
            let tl4 = [0.25, 0.75];
            let bl4 = [0.25, 0.5];
            let tr4 = [0.375, 0.75];
            let br4 = [0.375, 0.5];
            carUVs = carUVs.concat(tl4, bl4, br4, tl4, br4, tr4);

            // 5
            let bl5 = [0.375, 0.5];
            let t5 = [0.375, 0.75];
            let br5 = [0.5, 0.5];
            carUVs = carUVs.concat(t5, bl5, br5);

            // 6
            let tl6 = [0, 1];
            let bl6 = [0, 0.9];
            let tr6 = [0.15, 1];
            let br6 = [0.15, 0.9];
            carUVs = carUVs.concat(tl6, bl6, br6, tl6, br6, tr6);

            // 7
            let tl7 = [0.15, 1];
            let bl7 = [0.15, 0.9];
            let tr7 = [0.25, 1];
            let br7 = [0.25, 0.9];
            carUVs = carUVs.concat(tl7, bl7, br7, tl7, br7, tr7);

            // Roof
            let tlRoof = [0.15, 0.9];
            let blRoof = [0.15, 0.8];
            let trRoof = [0.2, 0.9];
            let brRoof = [0.2, 0.8];
            carUVs = carUVs.concat(tlRoof, blRoof, brRoof, tlRoof, brRoof, trRoof);

            // BackWindows
            let tl10 = [0.5, 1];
            let bl10 = [0.5, 0.9];
            let tr10 = [0.65, 1];
            let br10 = [0.65, 0.9];
            carUVs = carUVs.concat(tl10, bl10, br10, tl10, br10, tr10);

            // LeftWindow
            carUVs = carUVs.concat(br3, bl3, tl3, tr3, br3, tl3);
            carUVs = carUVs.concat(br4, bl4, tl4, tr4, br4, tl4);
            carUVs = carUVs.concat(bl5, t5, br5);

            // 8
            let tl8 = [0.25, 1];
            let bl8 = [0.25, 0.8];
            let tr8 = [0.5, 1];
            let br8 = [0.5, 0.8];
            carUVs = carUVs.concat(tl8, bl8, br8, tl8, br8, tr8);

            // 9
            let tl9 = [0.5, 0.8];
            let bl9 = [0.5, 0.5];
            let tr9 = [0.75, 0.8];
            let br9 = [0.75, 0.5];
            carUVs = carUVs.concat(tl9, bl9, br9, tl9, br9, tr9);

            // LeftSide
            carUVs = carUVs.concat(tl1, bl1, br1, tl1, br1, tr1);

            // Bottom
            let tl11 = [0.5, 0.4];
            let bl11 = [0.5, 0];
            let tr11 = [1, 0.4];
            let br11 = [1, 0];
            carUVs = carUVs.concat(tl11, bl11, br11, tl11, br11, tr11);
        }
        carUVs = new Float32Array(carUVs);

        //TEKSTUR-RELATERT:
        this.carTexture = this.gl.createTexture();
        //Teksturbildet er nå lastet fra server, send til GPU:
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.carTexture);

        //Unngaa at bildet kommer opp-ned:
        this.gl.pixelStorei(this.gl.UNPACK_FLIP_Y_WEBGL, true);
        this.gl.pixelStorei(this.gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, true);   //NB! FOR GJENNOMSIKTIG BAKGRUNN!! Sett også this.gl.blendFunc(this.gl.ONE, this.gl.ONE_MINUS_SRC_ALPHA);

        //Laster teksturbildet til GPU/shader:
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, xzPlaneTextureImage);

        //Teksturparametre:
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.NEAREST);
        this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.NEAREST);

        this.gl.bindTexture(this.gl.TEXTURE_2D, null);

        this.carTextureBuffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carTextureBuffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, carUVs, this.gl.STATIC_DRAW);
        this.carTextureBuffer.itemSize = 2;
        this.carTextureBuffer.numberOfItems = carUVs.length/2;
    }

    handleKeys(elapsed, currentlyPressedKeys) {
        if (currentlyPressedKeys["KeyQ"] && !(this.steeringRot>=45)) {
            this.steeringRot+=1;
        }
        if (currentlyPressedKeys["KeyE"] && !(this.steeringRot<=-45)) {
            this.steeringRot-=1;
        }
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
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carTextureBuffer);
        let a_TextureCoord = this.gl.getAttribLocation(this.gl.phongShaderProgram, "a_TextureCoord");
        this.gl.vertexAttribPointer(a_TextureCoord, this.carTextureBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_TextureCoord);
        //Aktiver teksturenhet (0):
        this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, this.carTexture);
        //Send inn verdi som indikerer hvilken teksturenhet som skal brukes (her 0):
        let samplerLoc = this.gl.getUniformLocation(this.gl.phongShaderProgram, "uSampler");
        this.gl.uniform1i(samplerLoc, 0);
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, null);

        // Rebind the buffers and shaders
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carPositionBuffer);
        let a_Position = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Position');
        this.gl.vertexAttribPointer(a_Position, this.carPositionBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(a_Position);

        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.carNormalBuffer);
        let a_Normal = this.gl.getAttribLocation(this.gl.phongShaderProgram, 'a_Normal');
        if (a_Normal !== -1) {  //-1 dersom a_Normal ikke er i bruk i shaderen.
            this.gl.vertexAttribPointer(a_Normal, this.carNormalBuffer.itemSize, this.gl.FLOAT, false, 0, 0);
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
        modelMatrix.translate(this.posX, 0.5, this.posZ);
        modelMatrix.rotate(this.angle, 0,1,0);
        modelMatrix.scale(2,2,2);
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
        this.gl.drawArrays(this.gl.TRIANGLES, 0, this.carPositionBuffer.numberOfItems);



        //HacketyHack, make the wheels a part of the car!
        this.wheelFrontRight.setSteeringRot(this.steeringRot);
        this.wheelFrontLeft.setSteeringRot(this.steeringRot);
        this.wheelFrontRight.draw(elapsed, lights, this.posX, this.posZ, this.angle);
        this.wheelRearRight.draw(elapsed, lights, this.posX, this.posZ, this.angle);
        this.wheelFrontLeft.draw(elapsed, lights, this.posX, this.posZ, this.angle);
        this.wheelRearLeft.draw(elapsed, lights, this.posX, this.posZ, this.angle);

    }
}
