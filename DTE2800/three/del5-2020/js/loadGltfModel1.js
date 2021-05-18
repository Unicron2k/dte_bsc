import * as THREE from '../../lib/three/build/three.module.js';
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from '../../lib/three/examples/jsm/loaders/GLTFLoader.js';
import { SkeletonUtils } from '../../lib/three/examples/jsm/utils/SkeletonUtils.js';
import { addCoordSystem} from "../../lib/wfa-coord.js";

// https://threejs.org/docs/#manual/en/introduction/Loading-3D-models
// gltf viewer: https://gltf-viewer.donmccurdy.com/
let models;
let renderer;
let scene;
let camera;

export function main() {
	const canvas = document.getElementById('webgl');
	renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
	renderer.setSize(window.innerWidth, window.innerHeight);

	const fov = 45;
	const aspect = 2;  // the canvas default
	const near = 0.01;
	const far = 10000;
	camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
	camera.position.set(200, 200, 400);

	const controls = new OrbitControls(camera, canvas);
	controls.target.set(0, 5, 0);
	controls.update();

	scene = new THREE.Scene();
	scene.background = new THREE.Color(0xBFD104);

	// Funksjon i funksjon for å enkelt legge til flere lyskilder.
	// ...pos indikerer vilkårlig antall parametre.
	function addLight(...pos) {
		const color = 0xFFFFFF;
		const intensity = 1;
		const light = new THREE.DirectionalLight(color, intensity);
		light.position.set(...pos);
		scene.add(light);
		scene.add(light.target);
	}
	// Bruker funksjonen over:
	addLight(50, 50, 200);
	addLight(-50, 50, -200);
	addLight(0, 500, -200);

	addCoordSystem(scene);

	// Starter nedlasting:
	loadModels();
}

function loadModels() {
	// Bruker en loading manager:
	const manager = new THREE.LoadingManager();
	manager.onLoad = init;

	// Objekt som holder på info om flere modeller:
	models = {
		mantis: {url: './models/mantis/scene.gltf'},
		pc_nightmare_mushroom: {url: './models/pc_nightmare_mushroom/scene.gltf'},
		mysphere1: {url: './models/mysphere1.glb'},
	};

	// Laster glTF-modeller som ligger i models:
	const gltfLoader = new GLTFLoader(manager);
	for (const model of Object.values(models)) {
		gltfLoader.load(model.url, (gltf) => {
			model.gltf = gltf;
		});
	}
	animate();
}

// Kjøres etter at modellene er lastet:
function init() {
	// hide the loading bar
	const loadingElem = document.querySelector('#loading');
	loadingElem.style.display = 'none';

	// Legger modellene inn i hvert sitt Object3D-objekt.
	// GJennomløper aller modeller i models-objektet, kloner scene-objektet til lastet modell,
	// legger i et Object3D-objekt (root), legger til scene:
	Object.values(models).forEach((model, ndx) => {
		const clonedScene = SkeletonUtils.clone(model.gltf.scene);
		const root = new THREE.Object3D();
		root.add(clonedScene);
		scene.add(root);
	});
}

function animate(now) {
	requestAnimationFrame(animate);
	renderer.render(scene, camera);
}
