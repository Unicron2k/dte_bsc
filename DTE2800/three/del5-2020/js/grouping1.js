/**
 * Gruppering av objekter.
 *
 */
import * as THREE from "../../lib/three/build/three.module.js";
import { TrackballControls } from '../../lib/three/examples/jsm/controls/TrackballControls.js';
import { addCoordSystem} from "../../lib/wfa-coord.js";

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
	scene.add(shadowCamera);

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

	// Events:
    window.addEventListener('resize', onWindowResize, {passive: false});
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

    //Group
	let gruppe = new THREE.Group();

	//Kube 1:
	let gCube = new THREE.BoxGeometry(40, 40, 40);
	let tCube = new THREE.TextureLoader().load('textures/bird1.png');
	let mCube = new THREE.MeshPhongMaterial({map : tCube});
	let cube = new THREE.Mesh(gCube, mCube);
	cube.name = "cubeOne";
	//Legger kuben til gruppa:
	cube.position.x = -70;
	cube.position.y = 20;
	cube.position.z = -100;
	cube.castShadow = true;
	gruppe.add(cube);

    //Kube 2:
	let gCube1 = new THREE.BoxGeometry(40, 40, 40);
	let tCube1 = new THREE.TextureLoader().load('textures/metal1.jpg');
	let mCube1 = new THREE.MeshPhongMaterial({map : tCube1});
	let cube1 = new THREE.Mesh(gCube1, mCube1);
	cube1.name = "cubeTwo";
	//Legger kuben til gruppa:
	cube1.position.x = 70;
	cube1.position.y = 20;
	cube1.position.z = -100;
	cube1.castShadow = true;
	gruppe.add(cube1);

    //Pil:
	let dir = new THREE.Vector3(0, 1, 0);
	let origin = gruppe.position; //new THREE.Vector3(10, 0, 0);
	let length = 50;
	let hex = 0xff0000;
	let arrowHelper = new THREE.ArrowHelper(dir, origin, length, hex);
	//Legger pila til gruppa:
	gruppe.add(arrowHelper);

	// Posisjonererer guppa:
	gruppe.position.x = -10;
	gruppe.position.y = 0;
	gruppe.position.z = 0;

	scene.add(gruppe);
}

//Legger til roter/zoom av scenen:
function addControls() {
    //NB! Viktig med renderer.domElement her pga. dat-gui (hvis ikke henger kontrollen fast i musepekeren).
    controls = new TrackballControls(camera, renderer.domElement);
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

    //Høyde (V/B):
    if (currentlyPressedKeys[86]) { //V

    }
    if (currentlyPressedKeys[66]) {	//B

    }
}

function render()
{
     renderer.render(scene, camera);
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    controls.handleResize();
    render();
}
