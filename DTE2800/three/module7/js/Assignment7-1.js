import * as THREE from '../../lib/three/build/three.module.js';
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import {getHeightData} from "../../lib/wfa-utils.js";


//Some globals
let renderer;
let scene;
let camera;
let lastTime = 0.0;
let controls;
let SIZE = 300;
let currentlyPressedKeys = {};
let cubes = [];


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
}

function addPlane() {
    getHeightData('./res/heightmap_300_300.png', SIZE, SIZE, loadPlane)
}
function loadPlane(heightMapData){
    // XZ-Plane
    let tGrass = new THREE.TextureLoader().load('./res/grass_512_512.jpg')
    tGrass.wrapS = THREE.RepeatWrapping;
    tGrass.wrapT = THREE.RepeatWrapping;
    tGrass.repeat.x = 2;
    tGrass.repeat.y = 2;
    let gPlane = new THREE.PlaneGeometry( SIZE, SIZE ,299,299);
    let mPlane = new THREE.MeshLambertMaterial( {/**/map: tGrass/*/ color: 0x909090/**/,
                                                            side: THREE.DoubleSide,
                                                            /**/wireframe: false/**/} );

    for(let index =0; index < gPlane.vertices.length; index++){
        gPlane.vertices[index].z = heightMapData[index]*0.3;//lets scale it down a bit :P
    }

    let meshPlane = new THREE.Mesh( gPlane, mPlane);
    meshPlane.rotation.x = -Math.PI / 2;
    meshPlane.receiveShadow = true;
    scene.add(meshPlane);
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


    cubes.forEach(cube => {
        if(cube.meshCube.position.y<=0) return;
        let dY = (cube.v0*elapsed+0.5*9.81*elapsed*elapsed);
        let newY = cube.meshCube.position.y - (cube.v0*elapsed+0.5*9.81*elapsed*elapsed);
        cube.setPos(cube.randomPos.x, newY, cube.randomPos.z);
        cube.v0 = dY/elapsed;
    });

    keyCheck();
    controls.update();
    render();
}



function addRandomCube() {
    let cube = new Cube()
    cubes.push(cube);
    scene.add(cubes[cubes.length-1].meshCube);
}

function keyCheck() {
    if (currentlyPressedKeys["Space"]) {
        addRandomCube();
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


class Cube{
    constructor(){
        this.someval=10;
        this.v0=0;
        let randomSize = this.randomNum(3,5);
        this.randomPos = {x:this.randomNum(-SIZE/2,SIZE/2), y:100, z:this.randomNum(-SIZE/2,SIZE/2)};
        this.randomRot = {x:(2*Math.PI/360)*this.randomNum(0,360), y:(2*Math.PI/360)*this.randomNum(0,360), z:(2*Math.PI/360)*this.randomNum(0,360)};
        this.cubeColor = '0xFF0055';
        let gCube = new THREE.CubeGeometry(randomSize, randomSize, randomSize, 1, 1, 1);
        let mCube = new THREE.MeshLambertMaterial({color: this.cubeColor});
        this.meshCube = new THREE.Mesh(gCube, mCube);
        this.meshCube.rotation.set(this.randomRot.x, this.randomRot.y, this.randomRot.z);
        this.meshCube.position.set(this.randomPos.x, this.randomPos.y, this.randomPos.z);
        this.meshCube.receiveShadow = true;
    }
    setPos(x=0, y=0, z=0){
        this.meshCube.position.set(x,y,z);
    }

    randomNum(min=0, max=1){
        return Math.floor((Math.random()*(max-min))+min+0.5);
    }
}