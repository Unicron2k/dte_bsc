import * as THREE from '../../lib/three/build/three.module.js';
import {Object3D} from "../../lib/three/build/three.module.js";
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';


//Some globals
let renderer;
let scene;
let camera;
let lastTime = 0.0;
let controls;
let SIZE = 200;
let currentlyPressedKeys = {};

//Drone
let drone;
let droneSpeed = 0.0;
let speedVector = new THREE.Vector3(0,0,-5);
let positionVector = new THREE.Vector3(0,0,0);
let delta = Math.PI/100;
let axis = new THREE.Vector3( 0, 1, 0 );
let droneGimbal;
let droneCamera;
let rotLF;
let rotRF;
let rotLR;
let rotRR;
let rotorSpeed = 0.0;
let rotorAngle = 0.0;
let isRotAccel = false;
let rotorWindUpSpeed = 5.0;
let rotorWindDownSpeed = 3.0;
let rotorMaxSpeed = 6*Math.PI;
let qPressedLastFrame = false;
let isIdling = false;
let rotLeft = false;
let rotRight = false;
let moveForward = false;
let moveBackward = false;
let moveLeft = false;
let moveRight = false;
let tBlackPlastic = new THREE.TextureLoader().load('./res/plastic_black_512_512.jpg')
let tWhitePlastic = new THREE.TextureLoader().load('./res/plastic_white_2_512_512.jpg')
let tSpiral = new THREE.TextureLoader().load('./res/spiral_512_512.jpg')


export function main() {
    let mycanvas = document.getElementById('webgl');
    scene = new THREE.Scene();

    renderer = new THREE.WebGLRenderer({canvas:mycanvas, antialias:true});
    renderer.getContext().getExtension('EXT_color_buffer_half_float');
    renderer.setClearColor(0xBFD104, 0xff);
    renderer.setSize(window.innerWidth, window.innerHeight);

    renderer.shadowMap.enabled = true;
    renderer.shadowMap.soft = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;

    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 10000);
    camera.position.x = 30;
    camera.position.y = 70;
    camera.position.z = 150;
    camera.up = new THREE.Vector3(0, 1, 0);
    let target = new THREE.Vector3(0.0, 0.0, 0.0);
    camera.lookAt(target);


    // LIGHTS
    //Brightens things up a little :)
    //scene.add(new THREE.AmbientLight(0x666666));
    let light;
    light = new THREE.DirectionalLight(0xffffff, 0.5);
    light.position.set(150, 200, 50);
    light.position.multiplyScalar(1.3);
    light.shadow.mapSize.width = 1024;
    light.shadow.mapSize.height = 1024;
    light.castShadow = true;

    let d = 250;
    light.shadow.camera.left = -d;
    light.shadow.camera.right = d;
    light.shadow.camera.top = d;
    light.shadow.camera.bottom = -d;
    light.shadow.camera.far = 500;
    light.shadow.darkness = 0.2;

    let shadowHelper = new THREE.CameraHelper(light.shadow.camera );
    scene.add(shadowHelper)
    scene.add(light);


    let light2;
    light2 = new THREE.SpotLight(0xffffff, 1.0, 300, Math.PI/18, 0.4, 0.1);
    light2.position.set(100, 200, -100);
    light2.castShadow = true;
    light2.shadow.mapSize.width = 512;
    light2.shadow.mapSize.height = 512;

    let spotlightHelper = new THREE.SpotLightHelper(light2);
    spotlightHelper.color = 0xFF0000;

    spotlightHelper.update();
    scene.add(spotlightHelper);
    scene.add(light2);


    addModels();
    addCoordSystem();
    addControls();

    window.addEventListener('resize', onWindowResize, false);
    document.addEventListener('keyup', handleKeyUp, false);
    document.addEventListener('keydown', handleKeyDown, false);

    alert("Controls:\n\n" +
        "Q - Start/stop motors\n" +
        "Left shift - Increase/decrease propeller speed\n" +
        "WASD - Move drone\n" +
        "IJKL - Pan and tilt drone-camera\n\n" +
        "Good luck pilot!");

    animate();
}

function handleKeyUp(event) {
    if (event.defaultPrevented) return;
    currentlyPressedKeys[event.code] = false;
    event.preventDefault();
}

function handleKeyDown(event) {
    if (event.defaultPrevented) return;
    currentlyPressedKeys[event.code] = true;
    event.preventDefault();
}

function addModels() {

    addPlane();
    addDrone();
}

function addPlane(){
    // XZ-Plane
    let tGrass = new THREE.TextureLoader().load('./res/grass_512_512.jpg')
    tGrass.wrapS = THREE.RepeatWrapping;
    tGrass.wrapT = THREE.RepeatWrapping;
    tGrass.repeat.x = 2;
    tGrass.repeat.y = 2;
    let gPlane = new THREE.PlaneGeometry( SIZE*2, SIZE*2 );
    let mPlane = new THREE.MeshPhongMaterial( {/**/map: tGrass/*/ color: 0x909090/**/} );
    let meshPlane = new THREE.Mesh( gPlane, mPlane);
    meshPlane.rotation.x = -Math.PI / 2;
    meshPlane.receiveShadow = true;
    scene.add(meshPlane);

}

function getRotor(){
    let rotor = new Object3D();

    tSpiral.wrapS = THREE.WrapAroundEnding;
    tSpiral.wrapT = THREE.WrapAroundEnding;
    /*
    tBlade.wrapS = THREE.RepeatWrapping;
    tBlade.wrapT = THREE.RepeatWrapping;
    tBlade.repeat.x = 8;
    tBlade.repeat.y = 8;
    */

    //Rotor-body
    let points = [];
    for (let x = 0; x < 1; x=x+0.01) {
        let y = Math.pow(x,2.5)*2;
        points.push(new THREE.Vector2(x,y));
    }
    let gRotorBody = new THREE.LatheGeometry(points, 128, 0, 2 * Math.PI);
    let mRotorBody = new THREE.MeshLambertMaterial({map: tSpiral});
    let meshRotorBody = new THREE.Mesh(gRotorBody, mRotorBody);
    meshRotorBody.rotation.z = Math.PI;
    meshRotorBody.position.set(0,1.95,0);
    meshRotorBody.castShadow = true;
    rotor.add(meshRotorBody);

    let gCircle = new THREE.CircleGeometry(0.99,128);
    let mCircle = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshCircle = new THREE.Mesh(gCircle, mCircle);
    meshCircle.castShadow = true;
    meshCircle.rotation.set(Math.PI/2,0,0);
    rotor.add(meshCircle);

    let gBlade = new THREE.BoxGeometry(8, 0.2, 1.2);
    let mBlade = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshBlade = new THREE.Mesh(gBlade, mBlade);
    meshBlade.position.set(4,1,0)
    meshBlade.rotation.x = Math.PI/6;
    meshBlade.castShadow=true;
    rotor.add(meshBlade);
    let meshBlade2 = meshBlade.clone();
    meshBlade2.position.set(-4,1,0)
    meshBlade2.rotation.x = -Math.PI/6;
    meshBlade2.castShadow=true;
    rotor.add(meshBlade2);

    let gTip = new THREE.CylinderGeometry(0.6, 0.6, 0.2, 24, 1, false, 0, Math.PI);
    let mTip = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshTip = new THREE.Mesh(gTip, mTip);
    meshTip.position.set(8,1,0)
    meshTip.rotation.x = Math.PI/6;
    meshTip.castShadow=true;
    rotor.add(meshTip);
    let meshTip2 = meshTip.clone();
    meshTip2.position.set(-8,1,0)
    meshTip2.rotation.set(-Math.PI/6,0,Math.PI);
    meshTip2.castShadow=true;
    rotor.add(meshTip2);

    rotor.name = "rotor";
    return rotor;
}

function getArm(){
    let droneArm = new THREE.Object3D;

    //Arm
    let gArm = new THREE.CylinderGeometry(0.4,0.4,16,16);
    let mArm = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshArm = new THREE.Mesh(gArm, mArm);
    meshArm.rotation.x = Math.PI/2
    meshArm.position.set(0,0,0);
    meshArm.castShadow = true;
    droneArm.add(meshArm);

    //Motor
    let gMotorBody = new THREE.CylinderGeometry(1,1,2,16);
    let mMotorBody = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshMotorBody = new THREE.Mesh(gMotorBody, mMotorBody);
    meshMotorBody.position.set(0,0,-8);
    meshMotorBody.castShadow = true;
    droneArm.add(meshMotorBody);

    //Motor-shaft
    let gMotorShaft = new THREE.CylinderGeometry(0.1,0.1,0.2,16);
    let mMotorShaft = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshMotorShaft = new THREE.Mesh(gMotorShaft, mMotorShaft);
    meshMotorShaft.position.set(0,1.1,-8);
    meshMotorShaft.castShadow = true;
    droneArm.add(meshMotorShaft);

    return droneArm;
}

function addDrone(){
    drone = new Object3D();
    drone.name = "drone";
    droneGimbal = new Object3D();
    droneGimbal.name = "gimbal";
    droneCamera = new Object3D();
    droneCamera.name = "camera";

    //Body
    //Due to the way the box is generated, textures look a bit weird... probably lacking proper UVS
    let gDroneBody = createBoxWithRoundedEdges(7,2,7, Math.PI/6, 6);
    let mDroneBody = new THREE.MeshPhongMaterial({map: tWhitePlastic});
    let meshDroneBody = new THREE.Mesh(gDroneBody, mDroneBody);
    meshDroneBody.position.set(0,8,0);
    meshDroneBody.castShadow = true;
    drone.add(meshDroneBody);

    let gFoot1 = new THREE.CylinderGeometry(0.2,0.2,8,16);
    let mFoot1 = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshFoot1 = new THREE.Mesh(gFoot1, mFoot1);
    meshFoot1.rotation.set(-Math.PI/12,0,Math.PI/12);
    meshFoot1.position.set(4,3.7,4);
    meshFoot1.castShadow = true;
    drone.add(meshFoot1);

    let gFoot2 = new THREE.CylinderGeometry(0.2,0.2,8,16);
    let mFoot2 = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshFoot2 = new THREE.Mesh(gFoot2, mFoot2);
    meshFoot2.rotation.set(-Math.PI/12,0,-Math.PI/12);
    meshFoot2.position.set(-4,3.7,4);
    meshFoot2.castShadow = true;
    drone.add(meshFoot2);

    let gFoot3 = new THREE.CylinderGeometry(0.2,0.2,8,16);
    let mFoot3 = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshFoot3 = new THREE.Mesh(gFoot3, mFoot3);
    meshFoot3.rotation.set(Math.PI/12,0,-Math.PI/12);
    meshFoot3.position.set(-4,3.7,-4);
    meshFoot3.castShadow = true;
    drone.add(meshFoot3);

    let gFoot4 = new THREE.CylinderGeometry(0.2,0.2,8,16);
    let mFoot4 = new THREE.MeshLambertMaterial({map: tWhitePlastic});
    let meshFoot4 = new THREE.Mesh(gFoot4, mFoot4);
    meshFoot4.rotation.set(Math.PI/12,0,Math.PI/12);
    meshFoot4.position.set(4,3.7,-4);
    meshFoot4.castShadow = true;
    drone.add(meshFoot4);


    //Arms and rotors
    //LeftFront
    let armLF = getArm();
    armLF.rotation.y = Math.PI/4;
    armLF.position.set(-8,8,-8);
    drone.add(armLF);
    rotLF = getRotor();
    rotLF.name = "rotorLF";
    rotLF.position.set(-13.66,9.2,-13.66);
    drone.add(rotLF);

    //Right Front
    let armRF = getArm();
    armRF.rotation.y = -Math.PI/4;
    armRF.position.set(8,8,-8);
    drone.add(armRF);
    rotRF = getRotor();
    rotRF.applyMatrix(new THREE.Matrix4().makeScale(-1, 1, 1));
    rotRF.name = "rotorRF";
    rotRF.position.set(13.66,9.2,-13.66);
    drone.add(rotRF);

    //LeftRear
    let armLR = getArm();
    armLR.rotation.y = Math.PI - Math.PI/4;
    armLR.position.set(-8,8,8);
    drone.add(armLR);
    rotLR = getRotor();
    rotLR.applyMatrix(new THREE.Matrix4().makeScale(-1, 1, 1));
    rotLR.position.set(-13.66,9.2,13.66);
    rotLR.name = "rotorLR";
    drone.add(rotLR);

    //RightRear
    let armRR = getArm();
    armRR.rotation.y = Math.PI + Math.PI/4;
    armRR.position.set(8,8,8);
    drone.add(armRR);
    rotRR = getRotor();
    rotRR.position.set(13.66,9.2,13.66);
    rotRR.name = "rotorRR";
    drone.add(rotRR);



    //Camera
    let gGimbalMount = new THREE.CylinderGeometry(0.5,0.5,1.5,16);
    let mGimbalMount = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshGimbalMount = new THREE.Mesh(gGimbalMount, mGimbalMount);
    meshGimbalMount.position.set(0,7,0);
    meshGimbalMount.castShadow = true;
    droneGimbal.add(meshGimbalMount);

    let gGimbalArm1 = new THREE.BoxGeometry(2,0.2,1,16);
    let mGimbalArm1 = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshGimbalArm1 = new THREE.Mesh(gGimbalArm1, mGimbalArm1);
    meshGimbalArm1.position.set(-1,6.35,0);
    meshGimbalArm1.castShadow = true;
    droneGimbal.add(meshGimbalArm1);

    let gGimbalArm2 = new THREE.BoxGeometry(2,0.2,1,16);
    let mGimbalArm2 = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshGimbalArm2 = new THREE.Mesh(gGimbalArm2, mGimbalArm2);
    meshGimbalArm2.rotation.z = Math.PI/2;
    meshGimbalArm2.position.set(-1.9,5.45,0);
    meshGimbalArm2.castShadow = true;
    droneGimbal.add(meshGimbalArm2);

    let gCameraMount = new THREE.CylinderGeometry(0.50,0.50,0.8,16);
    let mCameraMount = new THREE.MeshLambertMaterial({map: tBlackPlastic});
    let meshCameraMount = new THREE.Mesh(gCameraMount, mCameraMount);
    meshCameraMount.rotation.z = Math.PI/2;
    meshCameraMount.position.set(-1.60,4.5,0);
    meshCameraMount.castShadow = true;
    droneGimbal.add(meshCameraMount);



    //Camera
    //Due to the way the box is generated, textures look a bit weird... probably lacking proper UVS
    let gCameraBody = createBoxWithRoundedEdges(2.5,2.5,1.5, Math.PI/12, 6);
    let mCameraBody = new THREE.MeshPhongMaterial({map: tBlackPlastic});
    let meshCameraBody = new THREE.Mesh(gCameraBody, mCameraBody);
    meshCameraBody.castShadow = true;
    droneCamera.add(meshCameraBody);

    let gCameraLens = new THREE.CylinderGeometry(0.5,0.5,0.2,16);
    let mCameraLens = new THREE.MeshLambertMaterial({color: 0x090909});
    let meshCameraLens = new THREE.Mesh(gCameraLens, mCameraLens);
    meshCameraLens.rotation.x = Math.PI/2
    meshCameraLens.position.z=-0.8;
    meshCameraLens.castShadow = true;
    droneCamera.add(meshCameraLens);
    droneCamera.position.y = 4.5;


    droneGimbal.add(droneCamera);
    drone.add(droneGimbal);
    scene.add(drone);
}

// Source: https://discourse.threejs.org/t/round-edged-box/1402
// No licensing attached, assuming non-profit
function createBoxWithRoundedEdges( width, height, depth, radius0, smoothness ) {
    let shape = new THREE.Shape();
    let eps = 0.00001;
    let radius = radius0 - eps;
    shape.absarc( eps, eps, eps, -Math.PI / 2, -Math.PI, true );
    shape.absarc( eps, height -  radius * 2, eps, Math.PI, Math.PI / 2, true );
    shape.absarc( width - radius * 2, height -  radius * 2, eps, Math.PI / 2, 0, true );
    shape.absarc( width - radius * 2, eps, eps, 0, -Math.PI / 2, true );
    let geometry = new THREE.ExtrudeBufferGeometry( shape, {
        depth: depth - radius0 * 2,
        bevelEnabled: true,
        bevelSegments: smoothness * 2,
        steps: 1,
        bevelSize: radius,
        bevelThickness: radius0,
        curveSegments: smoothness
    });
    geometry.center();
    return geometry;
}

function addControls() {
    controls = new OrbitControls(camera);
    controls.addEventListener( 'change', render);
    controls.rotateSpeed = 1.0;
    controls.zoomSpeed = 10;
    controls.panSpeed = 0.8;

    controls.noZoom = false;
    controls.noPan = false;

    controls.staticMoving = true;
    controls.dynamicDampingFactor = 0.3;
}

function addCoordSystem() {
    addAxis(1); //x-aksen.
    addAxis(2); //y-aksen.
    addAxis(3); //z-aksen.
}

function addAxis(axis) {
    let fromNeg=new THREE.Vector3( 0, 0, 0 );
    let toNeg=new THREE.Vector3( 0, 0, 0 );
    let fromPos=new THREE.Vector3( 0, 0, 0 );
    let toPos=new THREE.Vector3( 0, 0, 0 );
    let axiscolor = 0x000000;

    switch (axis) {
        case 1: //x-aksen
            fromNeg=new THREE.Vector3( -SIZE, 0, 0 );
            toNeg=new THREE.Vector3( 0, 0, 0 );
            fromPos=new THREE.Vector3( 0, 0, 0 );
            toPos=new THREE.Vector3( SIZE, 0, 0 );
            axiscolor = 0xff0000;
            break;
        case 2: //y-aksen
            fromNeg=new THREE.Vector3( 0, -SIZE, 0 );
            toNeg=new THREE.Vector3( 0, 0, 0 );
            fromPos=new THREE.Vector3( 0, 0, 0 );
            toPos=new THREE.Vector3( 0, SIZE, 0 );
            axiscolor = 0x00ff00;
            break;
        case 3: //z-aksen
            fromNeg=new THREE.Vector3( 0, 0, -SIZE );
            toNeg=new THREE.Vector3( 0, 0, 0 );
            fromPos=new THREE.Vector3( 0, 0, 0 );
            toPos=new THREE.Vector3( 0, 0, SIZE );
            axiscolor = 0x0000ff;
            break;
    }

    let posMat = new THREE.LineBasicMaterial({ linewidth: 2, color: axiscolor });
    let negMat = new THREE.LineDashedMaterial({ linewidth: 2, color: axiscolor, dashSize: 0.5, gapSize: 0.1 });

    let gNeg = new THREE.Geometry();
    gNeg.vertices.push( fromNeg );
    gNeg.vertices.push( toNeg );
    let coordNeg = new THREE.Line(gNeg, negMat, THREE.LineSegments);
    coordNeg.computeLineDistances(); // NB!
    scene.add(coordNeg);

    let gPos = new THREE.Geometry();
    gPos.vertices.push( fromPos );
    gPos.vertices.push( toPos );
    let coordPos = new THREE.Line(gPos, posMat, THREE.LineSegments);
    coordPos.computeLineDistances();
    scene.add(coordPos);
}

function animate(currentTime) {
    requestAnimationFrame(animate);
    if (currentTime === undefined)
        currentTime = 0;
    let elapsed = 0.0;
    if (lastTime !== 0.0)
        elapsed = (currentTime - lastTime) / 1000;
    lastTime = currentTime;


    //Spin rotors
    let rotMax;
    let rotMin;
    let isWindingUp;
    //Should we idle?
    if (isIdling) {
        //Should we accelerate rotors above idle-state?
        if (isRotAccel) {
            //Yes
            rotMax = rotorMaxSpeed
            rotMin = Math.PI;
            isWindingUp = true;
        } else if (rotorSpeed > Math.PI) {
            //Not accelerating rotors, but they are moving faster than idle-state -> decelerate them
            rotMax = rotorMaxSpeed
            rotMin = Math.PI;
            isWindingUp = false;
        } else {
            //No, We are in an idle-state, so accelerate to idle
            rotMax = Math.PI;
            rotMin = 0;
            isWindingUp = true;
        }
    } else {
        //No, decelerate rotors to zero
        rotMax = rotorMaxSpeed;
        rotMin = 0;
        isWindingUp = false;
    }
    if (isWindingUp) {
        //wind up
        rotorSpeed += rotorWindUpSpeed * elapsed;
        if (rotorSpeed > rotMax)
            rotorSpeed = rotMax;
    } else {
        //wind down
        rotorSpeed -= rotorWindDownSpeed * elapsed;
        if (rotorSpeed < rotMin)
            rotorSpeed = rotMin;
    }

    //TODO:
    //Could simulate air-resistance/internal friction by calculating rotation-angle for each
    //rotor separately multiplied by a *random* friction-coefficient.
    rotorAngle = rotorAngle + (rotorSpeed * elapsed);
    rotorAngle %= (Math.PI * 2);

    //Update rotor-angle;
    rotLF.rotation.y = rotorAngle;
    rotRR.rotation.y = rotorAngle;
    rotRF.rotation.y = -rotorAngle;
    rotLR.rotation.y = -rotorAngle;


    //We'll keep gravity-simulation preeeeeetty simple/basic
    let dPos = (rotorSpeed - 9.81) * elapsed * 4;
    drone.position.y += dPos;
    if (drone.position.y >= 100) {
        drone.position.y = 100;
    }
    if (drone.position.y < 0) {
        drone.position.y = 0;
    }



    //Can we move?
    if (isIdling && dPos > 0) {
        //Yes
        if (rotLeft) {
            let matrix = new THREE.Matrix4().makeRotationAxis( axis, delta );
            speedVector.applyMatrix4( matrix );
        }
        if (moveBackward) {
            droneSpeed-=0.01;
            if (droneSpeed<=-1)
                droneSpeed = -1;
        }
        if (moveForward) {
            droneSpeed+=0.01;
            if (droneSpeed>=1)
                droneSpeed = 1;
        }
        if (rotRight) {
            let matrix = new THREE.Matrix4().makeRotationAxis( axis, -delta );
            speedVector.applyMatrix4( matrix );
        }
        //TODO: Implement strafing
        if (moveLeft) {
        }
        if (moveRight) {
        }


    } else {
        //No
        //are we moving?
        if(Math.abs(droneSpeed)>0)
            //Yes, slow down.
            droneSpeed *= 0.98; //Magic speeddown-number!
        if (Math.abs(droneSpeed)<=0.00001 || drone.position.y === 0)
            //Assuming no when below this speed & height===0;.
            droneSpeed = 0;
    }

    positionVector.x = positionVector.x + (speedVector.x * droneSpeed);
    positionVector.z = positionVector.z + (speedVector.z * droneSpeed);
    drone.position.x = positionVector.x;
    drone.position.z = positionVector.z;
    drone.rotation.y = Math.atan2(speedVector.x, speedVector.z) - Math.PI;


    keyCheck();
    controls.update();
    render();
}

function keyCheck() {
    //Rotates drone-camera a fixed amount per frame
    //TODO: take elapsed time into consideration
    if (currentlyPressedKeys["KeyI"]) {
        droneCamera.rotation.x += Math.PI/12;
        if (droneCamera.rotation.x >= Math.PI/2)
            droneCamera.rotation.x = Math.PI/2;
    }
    if (currentlyPressedKeys["KeyJ"]) {
        droneGimbal.rotation.y += Math.PI/12;
        if(droneGimbal.rotation.y>=+Math.PI+0.1)
            droneGimbal.rotation.y=+Math.PI+0.1;
    }
    if (currentlyPressedKeys["KeyK"]) {
        droneCamera.rotation.x -= Math.PI/12;
        if (droneCamera.rotation.x <= -Math.PI/2)
            droneCamera.rotation.x = -Math.PI/2;
    }
    if (currentlyPressedKeys["KeyL"]) {
        droneGimbal.rotation.y -= Math.PI/12;
        if(droneGimbal.rotation.y<=-Math.PI-0.1)
            droneGimbal.rotation.y=-Math.PI-0.1;
    }


    //TODO fix this
    rotLeft = currentlyPressedKeys["KeyA"];
    rotRight = currentlyPressedKeys["KeyD"];
    moveForward = currentlyPressedKeys["KeyW"];
    moveBackward = currentlyPressedKeys["KeyS"];
    moveLeft = currentlyPressedKeys["KeyZ"];
    moveRight = currentlyPressedKeys["KeyX"];
    isRotAccel = currentlyPressedKeys["ShiftLeft"];

    //Toggle motors on/off
    if (currentlyPressedKeys["KeyQ"]) {	//D
        if(!qPressedLastFrame){
            isIdling = !isIdling;
        }
        qPressedLastFrame = true;
    } else {
        qPressedLastFrame = false;
    }
}

function render() {
    renderer.render(scene, camera);
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    render();
}
