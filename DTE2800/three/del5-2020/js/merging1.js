/**
 * "Merging" / sammensl�ing av objekter.
 *
 */
import * as THREE from "../../lib/three/build/three.module.js";
import { TrackballControls } from '../../lib/three/examples/jsm/controls/TrackballControls.js';
import { addCoordSystem} from "../../lib/wfa-coord.js";
import Stats from '../../lib/three/examples/jsm/libs/stats.module.js';
//Globale varianbler:
let renderer;
let scene;
let camera;

//Roter & zoom:
let controls; //rotere, zoone hele scenen.

let SIZE = 200;

//Tar vare p� tastetrykk:
let currentlyPressedKeys = {};

//Klokke:
let clock = new THREE.Clock();

let render_stats;

export function main() {
	//Henter referanse til canvaset:
	let mycanvas = document.getElementById('webgl');

	//Lager en scene:
	scene = new THREE.Scene();

	//Lager et rendererobjekt (og setter st�rrelse):
	renderer = new THREE.WebGLRenderer({canvas:mycanvas, antialias:true});
	renderer.setClearColor(0xBFD104, 0xff);  //farge, alphaverdi.
	renderer.setSize(window.innerWidth, window.innerHeight);
	renderer.shadowMap.enabled = true; //NB!
	renderer.shadowMap.type = THREE.PCFSoftShadowMap; //THREE.BasicShadowMap;

    //FPS / stats;
	render_stats = new Stats();
	render_stats.domElement.style.position = 'absolute';
	render_stats.domElement.style.top = '0px';
	render_stats.domElement.style.zIndex = 100;
	document.getElementById('viewport').appendChild(render_stats.domElement);

	//Oppretter et kamera:
	camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
	camera.position.x = 130;
	camera.position.y = 200;
	camera.position.z = 150;
	camera.up = new THREE.Vector3(0, 1, 0);
    let target = new THREE.Vector3(0.0, 0.0, 0.0);
    camera.lookAt(target);

    //Lys:
	let spotLight = new THREE.SpotLight(0xffffff); //hvitt lys
	spotLight.position.set( 0, 400, 0 );
	spotLight.castShadow = true;
	spotLight.shadow.mapSize.width = 1024;
	spotLight.shadow.mapSize.height = 1024;
	spotLight.shadow.camera.near = 200;
	spotLight.shadow.camera.far = 410;
	scene.add(spotLight);
	let shadowCamera = new THREE.CameraHelper( spotLight.shadow.camera )
	//scene.add(shadowCamera);

	//Retningsorientert lys:
	let directionalLight1 = new THREE.DirectionalLight(0xffffff, 1.0); //farge, intensitet (1=default)
	directionalLight1.position.set(2, 1, 4);
	scene.add(directionalLight1);

	let directionalLight2 = new THREE.DirectionalLight(0xffffff, 1.0); //farge, intensitet (1=default)
	directionalLight2.position.set(-2, 1, -4);
	scene.add(directionalLight2);

	//Legg modeller til scenen:
	addModels();

	//Koordinatsystem:
	addCoordSystem(scene);

	//Roter/zoom hele scenen:
	addControls();

    //H�ndterer endring av vindusst�rrelse:
    window.addEventListener('resize', onWindowResize, {passive: false});

    //Input - standard Javascript / WebGL:
    document.addEventListener('keyup', handleKeyUp, {passive: false});
	document.addEventListener('keydown', handleKeyDown, {passive: false});

	animate();
}

function handleKeyUp(event) {
	currentlyPressedKeys[event.keyCode] = false;
}

function handleKeyDown(event) {
	currentlyPressedKeys[event.keyCode] = true;
}

function addModels() {
	//Plan:
	let gPlane = new THREE.PlaneGeometry( SIZE*2, SIZE*2 );
	let mPlane = new THREE.MeshLambertMaterial( {color: 0xA0A0A0, side: THREE.DoubleSide } );
	let meshPlane = new THREE.Mesh( gPlane, mPlane);
	meshPlane.rotation.x = Math.PI / 2;
	meshPlane.receiveShadow = true;	//NB!
	scene.add(meshPlane);

    //Uavhengige kuber:
	let cubeMaterial = new THREE.MeshNormalMaterial({ transparent: true, opacity: 0.5 });

    let numCubes = 5000;
    //Individuelle kuber:
    //addGeos(false, numCubes, cubeMaterial);

    //Sammenslåtte kuber:
    addGeos(true, numCubes, cubeMaterial);

}

function addGeos(merge, numCubes, cubeMaterial) {
    let geometry = new THREE.Geometry();

    if (merge) {
        for (let i = 0; i < numCubes; i++) {
            let cubeMesh = addCube(cubeMaterial);
            cubeMesh.updateMatrix();        //Sørger for at kuben blir riktig plassert i det sammenslåtte meshet.
			geometry.merge(cubeMesh.geometry, cubeMesh.matrix);
        }
        let mergedObject = new THREE.Mesh(geometry, cubeMaterial);
	    mergedObject.castShadow = true;
        scene.add(mergedObject);
    } else {
        for (let i = 0; i < numCubes; i++) {
            scene.add(addCube(cubeMaterial));
        }
    }
}

function addCube(cubeMaterial) {
    let cubeSize = 1.0;
    let cubeGeometry = new THREE.BoxGeometry(cubeSize, cubeSize, cubeSize);
    let cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
    cube.castShadow = true;
    // Tilfeldig plassert:
    cube.position.x = -60 + Math.round((Math.random() * 100));
    cube.position.y = Math.round((Math.random() * 10));
    cube.position.z = -150 + Math.round((Math.random() * 175));
    return cube;
}

//Legger til roter/zoom av scenen:
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

//function animate(currentTime) {
function animate() {
	requestAnimationFrame(animate);

	let elapsed = clock.getDelta(); 	// Forl�pt tid siden siste kall p� draw().

	//Sjekker input:
	keyCheck(elapsed);

	//Oppdater trackball-kontrollen:
	controls.update();

	//Tegner scenen med gitt kamera:
	render();
};

//Sjekker tastaturet:
function keyCheck(elapsed) {

	if (currentlyPressedKeys[65]) { //A

    }
    if (currentlyPressedKeys[83]) {	//S

    }
    if (currentlyPressedKeys[87]) {	//W

    }
    if (currentlyPressedKeys[68]) {	//D

    }

    //H�yde (V/B):
    if (currentlyPressedKeys[86]) { //V

    }
    if (currentlyPressedKeys[66]) {	//B

    }
}

function render()
{
    renderer.render(scene, camera);
    render_stats.update();
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    controls.handleResize();
    render();
}
