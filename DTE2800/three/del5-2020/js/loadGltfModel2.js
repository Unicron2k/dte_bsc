import * as THREE from '../../lib/three/build/three.module.js';
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from '../../lib/three/examples/jsm/loaders/GLTFLoader.js';
import { SkeletonUtils } from '../../lib/three/examples/jsm/utils/SkeletonUtils.js';
import { addCoordSystem} from "../../lib/wfa-coord.js";

// https://threejs.org/docs/#manual/en/introduction/Loading-3D-models
// gltf viewer: https://gltf-viewer.donmccurdy.com/

let models;
const mixers = [];
const mixerInfos = [];
let renderer;
let scene;
let camera;

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
	camera.position.set(200, 200, 400);

	const controls = new OrbitControls(camera, canvas);
	controls.target.set(0, 5, 0);
	controls.update();

	scene = new THREE.Scene();
	scene.background = new THREE.Color(0xBFD104);

	function addLight(...pos) {
		const color = 0xFFFFFF;
		const intensity = 1;
		const light = new THREE.DirectionalLight(color, intensity);
		light.position.set(...pos);
		scene.add(light);
		scene.add(light.target);
	}
	addLight(50, 50, 200);
	addLight(-50, 50, -200);
	addLight(0, 500, -200);

	window.addEventListener('keydown', (e) => {
		const mixerInfo = mixerInfos[e.keyCode - 49];
		if (!mixerInfo) {
			return;
		}
		playNextAction(mixerInfo);
	});

	addCoordSystem(scene);

	loadModels();
}

function loadModels() {
	const manager = new THREE.LoadingManager();
	manager.onLoad = init;  //Kjører init() under når modellene er ferdig lastet.

	const progressbarElem = document.querySelector('#progressbar');
	manager.onProgress = (url, itemsLoaded, itemsTotal) => {
		progressbarElem.style.width = `${itemsLoaded / itemsTotal * 100 | 0}%`;
	};

	models = {
		//mantis: {url: './models/mantis/scene.gltf'},
		//pc_nightmare_mushroom: {url: './models/pc_nightmare_mushroom/scene.gltf'},
		Cow7: {url: './models/Cow7.glb'},
		// running_animation: {url: './models/running_animation/scene.gltf'},
		//viernes: {url: './models/viernes/scene.gltf'},
		//primary_ion_drive: {url: './models/primary_ion_drive/scene.gltf'},
	};

	// Bruker GLTFLoader for å laste .gltf / .glb filer.
	const gltfLoader = new GLTFLoader(manager);
	// Laster alle modellene gitt i models-objektet:
	for (const model of Object.values(models)) {
		gltfLoader.load(model.url, (gltf) => {
			model.gltf = gltf;
		});
	}
	animate();
}

// Gjennomløper alle modellene gitt i models-objektet.
// Henter alle animasjoner fra gltf-objektet og lager en "animations"-property per model.
function prepModelsAndAnimations() {
	Object.values(models).forEach(model => {
		const animsByName = {};

		/*
			model.gltf.animations inneholder Animation Clips ("An AnimationClip is a reusable set of keyframe tracks which represent an animation." : Fra: https://threejs.org/docs/#api/en/animation/AnimationClip
			Each AnimationClip usually holds the data for a certain activity of the object.
			If the mesh is a character, for example, there may be one AnimationClip for a walkcycle,
			a second for a jump, a third for sidestepping and so on.
			SE OGSÅ: https://threejs.org/docs/#manual/en/introduction/Animation-system
		 */
		model.gltf.animations.forEach((clip) => {   // clip er et AnimationClip-objekt.
			animsByName[clip.name] = clip;      // Gir f.eks.: {'anim1', clip1} osv.
		});
		model.animations = animsByName;         // Gir f.eks. mantis: {url: './models/mantis/scene.gltf', animations: { . . . }},

		/*
		let m = model.gltf.scene.children[0];
		m.position.set(-20, 30, -20);
		//m.rotation.set ( 0, -1.5708, 0 );
		m.scale.set ( 2, 2, 2 );
		 */
	});
}

function init() {
	// hide the loading bar
	const loadingElem = document.querySelector('#loading');
	loadingElem.style.display = 'none';

	// Omstrukturerer...
	prepModelsAndAnimations();

	// Gjennomløper alle modellene gitt i models-objektet og putter hver gltf-modell inn i et Object3D-objekt (roor).
	Object.values(models).forEach((model, ndx) => {
		const clonedScene = SkeletonUtils.clone(model.gltf.scene);
		const root = new THREE.Object3D();
		root.add(clonedScene);
		scene.add(root);
		root.position.x = (ndx - 3) * 6;

		// Bruker AnimationMixer til å vise animasjoner, AnimationClips.
		// The stored data forms only the basis for the animations - actual playback is controlled by the AnimationMixer.
		const mixer = new THREE.AnimationMixer(clonedScene);

		// .map() lager her et array med AnimationAction-objekter:
		/*
			FRA: https://threejs.org/docs/#manual/en/introduction/Animation-system
			The AnimationMixer itself has only very few (general) properties and methods, because it can be controlled by the AnimationActions.
			By configuring an AnimationAction you can determine when a certain AnimationClip shall be played, paused or stopped on one of the mixers,
			if and how often the clip has to be repeated, whether it shall be performed with a fade or a time scaling,
			and some additional things, such crossfading or synchronizing.
		 */
		const actions = Object.values(model.animations).map((clip) => {
			return mixer.clipAction(clip);
		});
		const mixerInfo = {
			mixer,
			actions,
			actionNdx: -1,
		};
		mixerInfos.push(mixerInfo);
		playNextAction(mixerInfo);
		/*
		const firstClip = Object.values(model.animations)[3];
		const action = mixer.clipAction(firstClip);
		action.play();
		mixers.push(mixer);
		*/
	});
}

function playNextAction(mixerInfo) {
	const {actions, actionNdx} = mixerInfo;
	const nextActionNdx = (actionNdx + 1) % actions.length;
	mixerInfo.actionNdx = nextActionNdx;
	actions.forEach((action, ndx) => {
		const enabled = ndx === nextActionNdx;
		action.enabled = enabled;
		if (enabled) {
			action.play();
		}
	});
}


let then = 0;
function animate(now) {
	requestAnimationFrame(animate);

	now *= 0.001;  // convert to seconds
	const deltaTime = now - then;
	then = now;
	for (const {mixer} of mixerInfos) {
		mixer.update(deltaTime);
	}
	renderer.render(scene, camera);
}
