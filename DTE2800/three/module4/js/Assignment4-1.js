/**
 * Legger til SKYGGE vha. SpotLight
 *
 * Bruker standard javascript / WebGL-inputh�ndtering (keyboard)
 * => wasd + pil opp / ned (fart)
 *
 * SE: http://learningthreejs.com/blog/2012/01/20/casting-shadows/
 */

//Globale varianbler:
let renderer;
let scene;
let camera;

//rotasjoner
let lastTime = 0.0;

//Roter & zoom:
let controls; //rotere, zoome hele scenen.

let SIZE = 200;

let coffeeCup;
let cupSpeed = 1.4;

//Tar vare p� tastetrykk:
let currentlyPressedKeys = {};

import * as THREE from '../../lib/three/build/three.module.js';
import { TrackballControls } from '../../lib/three/examples/jsm/controls/TrackballControls.js';

export function main() {
    let mycanvas = document.getElementById('webgl');
    scene = new THREE.Scene();

    renderer = new THREE.WebGLRenderer({canvas:mycanvas, antialias:true});
    renderer.getContext().getExtension('EXT_color_buffer_half_float');
    renderer.setClearColor(0xBFD104, 0xff);  //farge, alphaverdi.
    renderer.setSize(window.innerWidth, window.innerHeight);

    renderer.shadowMap.enabled = true;
    renderer.shadowMap.soft = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap; //THREE.BasicShadowMap;

    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 10000);
    camera.position.x = 230;
    camera.position.y = 400;
    camera.position.z = 350;
    camera.up = new THREE.Vector3(0, 1, 0);
    let target = new THREE.Vector3(0.0, 0.0, 0.0);
    camera.lookAt(target);


    // LIGHTS
    scene.add(new THREE.AmbientLight(0x666666));
    let light;
    light = new THREE.DirectionalLight(0xffffff, 1.0);
    light.position.set(300, 400, 100);
    light.position.multiplyScalar(1.3);
    light.shadow.mapSize.width = 512;
    light.shadow.mapSize.height = 512;
    light.castShadow = true;

    let d = 250;
    light.shadow.camera.left = -d;
    light.shadow.camera.right = d;
    light.shadow.camera.top = d;
    light.shadow.camera.bottom = -d;
    light.shadow.camera.far = 1000;
    light.shadow.darkness = 0.2;

    let shadowHelper = new THREE.CameraHelper(light.shadow.camera );
    scene.add(shadowHelper)
    scene.add(light);

    addModels();
    addCoordSystem();
    addControls();

    window.addEventListener('resize', onWindowResize, false);
    document.addEventListener('keyup', handleKeyUp, false);
    document.addEventListener('keydown', handleKeyDown, false);

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
    // XZ-Plane:
    let gPlane = new THREE.PlaneGeometry( SIZE*2, SIZE*2 );
    let mPlane = new THREE.MeshPhongMaterial( {color: 0x6C6C6C} );
    let meshPlane = new THREE.Mesh( gPlane, mPlane);
    meshPlane.rotation.x = -Math.PI / 2;
    meshPlane.receiveShadow = true;
    scene.add(meshPlane);

    addCube(SIZE-5,5,SIZE-5,10);
    addCube(-SIZE+5,5,SIZE-5,10);
    addCube(SIZE-5,5,-SIZE+5,10);
    addCube(-SIZE+5,5,-SIZE+5,10);
    addTorus(0,50,0);
    addSphere(80,5,60,5);
    addCone(-120,25,-120,25, 50);
    addPyramid(120, 25, -120,25,50);
    addCoffeeCup(-50,0,-50);
}

function addCube(x=0, y=0, z=0, size = 10) {

    let gCube = new THREE.BoxGeometry(size, size, size);
    let mCube = new THREE.MeshLambertMaterial({color:0x00de88});
    let meshCube = new THREE.Mesh(gCube, mCube);
    meshCube.castShadow=true;
    meshCube.name = "cube";
    meshCube.position.x = x;
    meshCube.position.y = y;
    meshCube.position.z = z;

    scene.add(meshCube);
}

function addTorus(x=0, y=0, z=0, radius=50){
    let gTorus = new THREE.TorusGeometry(radius, 10, 20,20,);
    let mTorus = new THREE.MeshLambertMaterial({color:0x00FFFF});
    let meshTorus = new THREE.Mesh(gTorus, mTorus);
    meshTorus.castShadow=true;
    meshTorus.name = "Torus";
    meshTorus.rotation.x = Math.PI/2;
    meshTorus.position.x = x;
    meshTorus.position.y = y;
    meshTorus.position.z = z;

    scene.add(meshTorus);
}

function addSphere(x=0, y=0, z=0, radius=5){
    let gSphere = new THREE.SphereGeometry(radius, 10, 20);
    let mSphere = new THREE.MeshLambertMaterial({ color:0xFFFF00});
    let meshSphere = new THREE.Mesh(gSphere, mSphere);
    meshSphere.castShadow=true;
    meshSphere.name = "Sphere";
    meshSphere.position.x = x;
    meshSphere.position.y = y;
    meshSphere.position.z = z;

    scene.add(meshSphere);
}

function addCone(x=0, y=0, z=0, radius=5, height=10){
    let gCone = new THREE.ConeGeometry(radius, height, 20);
    let mCone = new THREE.MeshLambertMaterial({ color:0xFFFF00});
    let meshCone = new THREE.Mesh(gCone, mCone);
    meshCone.castShadow=true;
    meshCone.name = "Cone";
    meshCone.position.x = x;
    meshCone.position.y = y;
    meshCone.position.z = z;

    scene.add(meshCone);
}

function addPyramid(x=0, y=0, z=0, radius=5, height=10){
    let gPyramid = new THREE.ConeGeometry(radius, height, 4);
    let mPyramid = new THREE.MeshLambertMaterial({ color:0xFFFF00});
    let meshPyramid = new THREE.Mesh(gPyramid, mPyramid);
    meshPyramid.castShadow=true;
    meshPyramid.name = "Pyramid";
    meshPyramid.position.x = x;
    meshPyramid.position.y = y;
    meshPyramid.position.z = z;

    scene.add(meshPyramid);
}

function addCoffeeCup(x=0, y=0, z=0, size=10){

    coffeeCup = new THREE.Object3D();

    // Bunnen/rota:
    let gBottom = new THREE.CylinderGeometry( 0.4, 0.4, 0.05, 32 );
    let mBottom = new THREE.MeshLambertMaterial({color:0xaaaa00});
    let meshBottom = new THREE.Mesh(gBottom, mBottom);
    meshBottom.castShadow = true;
    meshBottom.side = THREE.DoubleSide;
    coffeeCup.add(meshBottom);

    // Koppen/Lathe:
    let points = [];
    for (let x = 0; x < 1; x=x+0.01) {
        let y = Math.pow(x,5)*2;
        points.push(new THREE.Vector2(x,y));
    }
    let gCup = new THREE.LatheGeometry(points, 128, 0, 2 * Math.PI);
    let mCup = new THREE.MeshLambertMaterial({color: 0xaaaa00, side: THREE.DoubleSide});
    let meshCup = new THREE.Mesh(gCup, mCup)
    meshCup.castShadow = true;
    meshCup.side = THREE.DoubleSide;
    coffeeCup.add(meshCup);

    // Hanken:
    let gTorus = new THREE.TorusGeometry(15, 3,50,50, Math.PI)
    let mTorus = new THREE.MeshLambertMaterial({color: 0xaaaa00});
    let meshTorus = new THREE.Mesh(gTorus, mTorus);
    meshTorus.castShadow = true;
    meshTorus.rotation.z = -Math.PI/2 - Math.PI/14;
    meshTorus.position.set(0.82,1,0)
    meshTorus.scale.x=0.035;
    meshTorus.scale.y=0.035;
    meshTorus.scale.z=0.035;
    coffeeCup.add(meshTorus);

    let gCircle = new THREE.CircleGeometry(0.96,128);
    let mCircle = new THREE.MeshLambertMaterial({color: 0x5e4624});
    let meshCircle = new THREE.Mesh(gCircle, mCircle);
    meshCircle.castShadow = true;
    meshCircle.rotation.set(-Math.PI/2,0,0);
    meshCircle.position.y = 1.65;
    coffeeCup.add(meshCircle);

    coffeeCup.position.x = x;
    coffeeCup.position.y = y;
    coffeeCup.position.z = z;
    coffeeCup.scale.set(size,size,size)
    coffeeCup.castShadow = false;  //<-- Practicly useless, as it doesn't (force)enable/disable shadow-casting for an object consisting of multiple meshes, as demonstrated here...

    scene.add(coffeeCup);
}

function addControls() {
    controls = new TrackballControls(camera);
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
        elapsed = (currentTime - lastTime)/1000;
    lastTime = currentTime;


    //Sjekker input:
    keyCheck();
    //Oppdater trackball-kontrollen:
    controls.update();
    //Tegner scenen med gitt kamera:
    render();
}

function keyCheck() {


    // moves relative to coordinate-grid, not camera.
    // does not account for diagonal movement-speed being sqrt(2*cupSpeed) and not cupSpeed.
    if (currentlyPressedKeys["KeyW"]) {	//W
        coffeeCup.position.z -= cupSpeed;
    }
    if (currentlyPressedKeys["KeyA"]) { //A
        coffeeCup.position.x -= cupSpeed;
    }
    if (currentlyPressedKeys["KeyS"]) {	//S
        coffeeCup.position.z += cupSpeed;
    }
    if (currentlyPressedKeys["KeyD"]) {	//D
        coffeeCup.position.x += cupSpeed;
    }
}

function render() {
    renderer.render(scene, camera);
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    controls.handleResize();
    render();
}
