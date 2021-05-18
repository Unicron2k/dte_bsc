// Contains code from example-code "TiltableTerrain" made by Werner Farstad
"use strict";
import * as THREE from "../../lib/three/build/three.module.js";
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import Stats from "../../lib/three/examples/jsm/libs/stats.module.js";

export class ShaderMaterialTest {
	constructor(){
		//Globale variabler

		this.clock = new THREE.Clock();
		this.scene = {};
		this.renderer = {};
		this.controls = {};
		this.camera = {};
		this.currentlyPressedKeys = [];

		document.addEventListener('keyup', this.handleKeyUp.bind(this), {passive: false});
		document.addEventListener('keydown', this.handleKeyDown.bind(this), {passive: false});
		document.addEventListener('resize', this.onWindowResize.bind(this), {passive: false});

		// Stats:
		this.stats = new Stats();
		this.stats.showPanel( 0 ); // 0: fps, 1: ms, 2: mb, 3+: custom
		document.body.appendChild( this.stats.dom );

	}
	start() {
		this.setupGraphics();
		this.addControls();

		this.addSphere(5, '#FFAA00');

		this.animate();
	}

	setupGraphics() {

		//create the scene
		this.scene = new THREE.Scene();
		this.scene.background = new THREE.Color(0xffffff);

		//create camera
		this.camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 0.2, 5000);
		this.camera.position.set(15, 30, 50);
		this.camera.lookAt(new THREE.Vector3(0, 0, 0));

		//Setup the renderer
		this.renderer = new THREE.WebGLRenderer({antialias: true});
		this.renderer.setClearColor(0xbfd1e5);
		this.renderer.setPixelRatio(window.devicePixelRatio);
		this.renderer.setSize(window.innerWidth, window.innerHeight);
		document.body.appendChild(this.renderer.domElement);

		this.renderer.gammaInput = true;
		this.renderer.gammaOutput = true;

		this.renderer.shadowMap.enabled = true;
	}

	keyCheck(elapsed) {
		if (this.currentlyPressedKeys["KeyA"]) {
		}
		if (this.currentlyPressedKeys["KeyD"]) {
		}
		if (this.currentlyPressedKeys["KeyW"]) {
		}
		if (this.currentlyPressedKeys["KeyS"]) {
		}
	}

	onWindowResize() {
		this.camera.aspect = window.innerWidth / window.innerHeight;
		this.camera.updateProjectionMatrix();
		this.renderer.setSize(window.innerWidth, window.innerHeight);
		this.controls.handleResize();
		this.animate();
	}

	animate() {
		requestAnimationFrame(this.animate.bind(this)); //Merk bind()

		this.stats.begin();

		let deltaTime = this.clock.getDelta();

		//Sjekker input:
		this.keyCheck(deltaTime);

		//Tegner scenen med gitt kamera:
		this.render();

		//Oppdater trackball-kontrollen:
		if (this.controls)
			this.controls.update();

		this.stats.end();
	}

	render() {
		if (this.renderer)
			this.renderer.render(this.scene, this.camera);
	}

	handleKeyUp(event) {
		if (event.defaultPrevented) return;
		this.currentlyPressedKeys[event.code] = false;
		event.preventDefault();
	}

	handleKeyDown(event) {
		if (event.defaultPrevented) return;
		this.currentlyPressedKeys[event.code] = true;
		event.preventDefault();
	}

	addControls() {
		this.controls = new OrbitControls(this.camera);
		this.controls.addEventListener('change', this.render);
		this.controls.rotateSpeed = 1.0;
		this.controls.zoomSpeed = 10;
		this.controls.panSpeed = 0.8;
		this.controls.noZoom = false;
		this.controls.noPan = false;
		this.controls.staticMoving = true;
		this.controls.dynamicDampingFactor = 0.3;
	}

	addSphere(radius, color){

		let sphereUniforms = {
			u_lightDirection: { value: [-10, 6, 10] },
			u_ambientLightColor: { value: [0.5, 0.1, 0.9] },
			u_diffuseLightColor: { value: [0.5, 0.1, 0.9] },
		}

		let sphereGeometry = new THREE.SphereBufferGeometry(radius, 32, 32);
		let sphereMaterial = new THREE.ShaderMaterial({
			uniforms: sphereUniforms,
			vertexShader: document.getElementById( 'sphere-vertex-shader' ).innerHTML,
			fragmentShader: document.getElementById( 'sphere-fragment-shader' ).innerHTML,

		});

		//sphereMaterial = new THREE.MeshPhongMaterial({color:'#FF00AA'});
		this.sphereMesh = new THREE.Mesh(sphereGeometry, sphereMaterial);

		this.sphereMesh.position.set(0,0,0);
		this.sphereMesh.castShadow = true;
		this.sphereMesh.receiveShadow = true;
		this.scene.add(this.sphereMesh);
	}
}
