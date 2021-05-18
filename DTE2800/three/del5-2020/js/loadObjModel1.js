/**
 * Basert på info fra: Learning Three.js: The Javascript 3D Librarry for WebGL.
 *
 */
import * as THREE from '../../lib/three/build/three.module.js';
import { TrackballControls } from '../../lib/three/examples/jsm/controls/TrackballControls.js';
import { OBJLoader } from '../../lib/three/examples/jsm/loaders/OBJLoader.js';
import { MTLLoader } from '../../lib/three/examples/jsm/loaders/MTLLoader.js';
// import Stats from '../../lib/three/examples/jsm/libs/stats.module.js';

//Globale let ianbler:
let  renderer;
let  scene;
let  camera;

//Roter & zoom:
let  controls; //rotere, zoone hele scenen.

//Tar let e p� tastetrykk:
let  currentlyPressedKeys = {};

export function main() {

	//Henter referanse til canvaset:
	let  mycanvas = document.getElementById('webgl');

	//Lager en scene:
	scene = new THREE.Scene();

	//Lager et rendererobjekt (og setter st�rrelse):
	renderer = new THREE.WebGLRenderer({
		canvas: mycanvas,
		antialias: true
	});
	renderer.setClearColor(0xBFD104, 0xff);  		//farge, alphaverdi.
	//renderer.setClearColor(0x000000, 0xff); 		//farge, alphaverdi.
	renderer.setSize(window.innerWidth, window.innerHeight);
	renderer.shadowMap.enabled = true; //NB!
	renderer.shadowMap.type = THREE.PCFSoftShadowMap; //THREE.BasicShadowMap;

	//Oppretter et kamera:
	camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
	camera.position.x = 5;
	camera.position.y = 15;
	camera.position.z = 15;
	camera.up = new THREE.Vector3(0, 1, 0);
	let  target = new THREE.Vector3(0.0, 0.0, 0.0);
	camera.lookAt(target);

	//Lys:
	let spotLight = new THREE.SpotLight(0xffffff); //hvitt lys
	spotLight.position.set(0, 400, 0);
	spotLight.castShadow = true;
	scene.add(spotLight);

	//Retningsorientert lys:
	let  directionalLight1 = new THREE.DirectionalLight(0xffffff, 1.0); //farge, intensitet (1=default)
	directionalLight1.position.set(100, 100, 100);
	scene.add(directionalLight1);

	let  directionalLight2 = new THREE.DirectionalLight(0xffffff, 1.0); //farge, intensitet (1=default)
	directionalLight2.position.set(-50, -10, -100);
	scene.add(directionalLight2);

	//Legg modeller til scenen:
	loadModel();

	//Koordinatsystem:
	//addCoordSystem(scene);

	//Roter/zoom hele scenen:
	addControls();

	window.addEventListener('resize', onWindowResize, false);
	//Input - standard Javascript / WebGL:
	document.addEventListener('keyup', handleKeyUp, false);
	document.addEventListener('keydown', handleKeyDown, false);

	animate();
}

function handleKeyUp(event) {
	currentlyPressedKeys[event.keyCode] = false;
}

function handleKeyDown(event) {
	currentlyPressedKeys[event.keyCode] = true;
}

//Kalles også fra redraw() i datgui-kontrollen:
function createMesh(geo, mat) {
	mesh = new THREE.Mesh(geo, mat);
	return mesh;
}

//Bruker OBJLoader:
function loadModel() {

	//loadGeoOnlyModel();
	loadGeoAndMaterialModel();

}

function loadGeoOnlyModel() {
	let  loader = new OBJLoader();

	//Laster kun geometrien dvs. obj-fila:
	loader.load('models/ant1.obj', function (loadedMesh) {
		let  material = new THREE.MeshLambertMaterial({color: 0x5C3A21});
		/*
		let textureMap = new THREE.TextureLoader().load("textures/wool_texture_knit_fabric_small.png");
		textureMap.wrapS = THREE.RepeatWrapping;
		textureMap.wrapT = THREE.RepeatWrapping;
		textureMap.repeat.x = 100;
		textureMap.repeat.y = 100;
		let material = new THREE.MeshPhongMaterial( {map : textureMap, side: THREE.DoubleSide, wireframe: false, color: 0x0008ED} );
		*/
		// loadedMesh er en gruppe med mesh.
		// Setter samme materiale på alle disse.
		// Beregn normaler på nytt for korrekt shading.
		loadedMesh.children.forEach(function (child) {
			child.material = material;
			child.geometry.computeFaceNormals();
			child.geometry.computeVertexNormals();
		});

		loadedMesh.scale.set(1, 1, 1);
		loadedMesh.rotation.x = -0.3;
		loadedMesh.position.x = 0;
		scene.add(loadedMesh);
	});
}

//Bruker MTLLoader & OBJLoader
function loadGeoAndMaterialModel() {
	let  mesh = null;

	let  mtlLoader = new MTLLoader();
	let  modelName = 'gubbe';  //gubbe, male, ant1, ftorso

	//Laster først materiale:
	mtlLoader.load('models/' + modelName + '.mtl', function (materials) {
		materials.preload();
		let  objLoader = new OBJLoader();
		objLoader.setMaterials(materials);

		//...deretter geometrien:
		objLoader.load('models/' + modelName + '.obj', function (object) {
			mesh = object;
			mesh.position.y = 0;
			mesh.scale.set(1, 1, 1);

			//Plukkrt ut deler av modellen, for animasjon f.eks.:
			if (modelName === 'gubbe') {
				mesh.scale.set(0.1, 0.1, 0.1);

				let  arm = mesh.getObjectByName("BOPED_L_arm_fore"); //.children[2].children[0];
				arm.rotation.y = Math.PI / 3;
				arm.material.opacity = 0.6;
				arm.material.transparent = true;
				arm.material.depthTest = false;
				arm.material.side = THREE.DoubleSide;
				let  finger = mesh.getObjectByName("BOPED_R_hand_finger11");
				finger.material.opacity = 0.6;
				finger.material.transparent = true;
				finger.rotation.z = Math.PI / 4;
			}

			scene.add(mesh);

		});

	});
}


// Legger til roter/zoom av scenen:
function addControls() {
	//NB! Viktig med renderer.domElement her pga. dat-gui (hvis ikke henger kontrollen fast i musepekeren).
	controls = new TrackballControls(camera, renderer.domElement);
	controls.addEventListener('change', render);
	controls.rotateSpeed = 1.0;
	controls.zoomSpeed = 10;
	controls.panSpeed = 0.8;

	controls.noZoom = false;
	controls.noPan = false;

	controls.staticMoving = true;
	controls.dynamicDampingFactor = 0.3;
}

function animate(currentTime) {

	requestAnimationFrame(animate);

	//Sjekker input:
	keyCheck(currentTime);

	//Oppdater trackball-kontrollen:
	controls.update();

	//Tegner scenen med gitt kamera:
	render();

};

//Sjekker tastaturet:
function keyCheck(elapsed) {

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
