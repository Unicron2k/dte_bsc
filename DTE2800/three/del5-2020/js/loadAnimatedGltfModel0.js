import * as THREE from '../../lib/three/build/three.module.js';
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from '../../lib/three/examples/jsm/loaders/GLTFLoader.js';
import { SkeletonUtils } from '../../lib/three/examples/jsm/utils/SkeletonUtils.js';
import { addCoordSystem} from "../../lib/wfa-coord.js";

// https://threejs.org/docs/#manual/en/introduction/Loading-3D-models
// gltf viewer: https://gltf-viewer.donmccurdy.com/
let renderer;
let scene;
let camera;
let gltfModel = {};
let animationMixer;
let clock = new THREE.Clock();

export function main() {
	const canvas = document.getElementById('webgl');
	renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
	renderer.setClearColor(0xBFD104, 0xff);  //farge, alphaverdi.
	renderer.setSize(window.innerWidth, window.innerHeight);

	const fov = 45;
	const aspect = 2;  // the canvas default
	const near = 0.01;
	const far = 10000;
	camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
	camera.position.set(10, 10, 10);

	const controls = new OrbitControls(camera, canvas);
	controls.target.set(0, 5, 0);
	controls.update();

	scene = new THREE.Scene();
	scene.background = new THREE.Color(0xBFD104);

	addLight(50, 50, 200);
	addLight(-50, 50, -200);
	addLight(0, 500, -200);
	addCoordSystem(scene);

	loadModels();
}

function addLight(...pos) {
	const color = 0xFFFFFF;
	const intensity = 1;
	const light = new THREE.DirectionalLight(color, intensity);
	light.position.set(...pos);
	scene.add(light);
	scene.add(light.target);
}

function loadModels() {
	const manager = new THREE.LoadingManager();
	manager.onLoad = init;  //Kjører init() under når modellene er ferdig lastet.

	// Viser progressbar:
	const progressbarElem = document.querySelector('#progressbar');
	manager.onProgress = (url, itemsLoaded, itemsTotal) => {
		progressbarElem.style.width = `${itemsLoaded / itemsTotal * 100 | 0}%`;
	};

	// Bruker GLTFLoader for å laste .gltf / .glb filer.
	const gltfLoader = new GLTFLoader(manager);
	gltfLoader.load('./models/animation1.glb', (gltf) => {
		gltfModel = gltf;
	});
	/*
	gltfLoader.load('./models/running_animation/scene.gltf', (gltf) => {
		gltfModel = gltf;
	});
	*/
	animate();
}

function init() {
	// hide the loading bar
	const loadingElem = document.querySelector('#loading');
	loadingElem.style.display = 'none';

	// Kloner gltf og putter inn i et Object3D-objekt:
	const clonedScene = SkeletonUtils.clone(gltfModel.scene);
	const root = new THREE.Object3D();
	root.add(clonedScene);
	scene.add(root);

	// Bruker AnimationMixer til å vise animasjoner, AnimationClip's.
	// The stored data forms only the basis for the animations - actual playback is controlled by
	// the AnimationMixer.
	animationMixer = new THREE.AnimationMixer(clonedScene);
	/*
		gltfModel.animations inneholder Animation Clips ("An AnimationClip is a reusable set of keyframe tracks which represent an animation." : Fra: https://threejs.org/docs/#api/en/animation/AnimationClip )

		Each AnimationClip usually holds the data for a certain activity of the object.
		If the mesh is a character, for example, there may be one AnimationClip for a walkcycle,
		a second for a jump, a third for sidestepping and so on.
	    https://threejs.org/docs/#manual/en/introduction/Animation-system
	 */
	let clips = gltfModel.animations;
	for (let i=0; i < clips.length; i++) {
		console.log(clips[i].name); // Jump, Death, WalkSlow, Idle, Walk
	}

	// Henter clip vha. navn:
	//const walkClip = THREE.AnimationClip.findByName( clips, 'WalkSlow' );
	//const deathClip = THREE.AnimationClip.findByName( clips, 'Death' );
	//const walkSlowClip = THREE.AnimationClip.findByName( clips, 'WalkSlow' );
	//const action0 = animationMixer.clipAction( walkSlowClip );
	//action0.play();

	// Henter clip vha. index:
	const jumpClip = gltfModel.animations[0];
	const action0 = animationMixer.clipAction( jumpClip );
	action0.play();

	// Henter AnimationAction-objektet knyttet til aktuelt clip.
	// AnimationAction-objektet brukes til å kontrollere klippet:
	const animationAction1 = animationMixer.clipAction(jumpClip);
	animationAction1.setDuration(5).setLoop(THREE.LoopOnce).play();

}

function animate(now) {
	requestAnimationFrame(animate);
	const deltaTime = clock.getDelta();
	if (animationMixer)
		animationMixer.update(deltaTime);
	renderer.render(scene, camera);
}
