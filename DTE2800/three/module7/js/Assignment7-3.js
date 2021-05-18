// Contains code from example-code "TiltableTerrain" made by Werner Farstad
"use strict";
import * as THREE from "../../lib/three/build/three.module.js";
import { OrbitControls } from '../../lib/three/examples/jsm/controls/OrbitControls.js';
import Stats from "../../lib/three/examples/jsm/libs/stats.module.js";
import {PhysicsWorld} from "./PhysicsWorld.js";
import {GameBoard} from "./GameBoard.js";
import {Marble} from "./Marble.js";
import {Axis} from "./Constants.js";

export class MarbleGame {
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
        this.physicsWorld = new PhysicsWorld();
        this.physicsWorld.setup();

        this.setupGraphics();
        this.addControls();

        this.gameBoard = new GameBoard(this.scene, this.physicsWorld);
        this.gameBoard.createGameBoard();

        this.marble = new Marble(this.scene, this.physicsWorld);
        this.marble.createMarble({x:43,y:50,z:-43}, 1)

        
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

        //Add directional light
        let dirLight1 = new THREE.DirectionalLight(0xffffff, 1);
        dirLight1.color.setHSL(0.1, 1, 0.95);
        dirLight1.position.set(-0.1, 1.75, 0.1);
        dirLight1.position.multiplyScalar(100);

        dirLight1.castShadow = true;
        let dLight = 500;
        let sLight = dLight;
        dirLight1.shadow.camera.left = -sLight;
        dirLight1.shadow.camera.right = sLight;
        dirLight1.shadow.camera.top = sLight;
        dirLight1.shadow.camera.bottom = -sLight;
        dirLight1.shadow.camera.near = dLight / 30;
        dirLight1.shadow.camera.far = dLight;
        dirLight1.shadow.mapSize.x = 1024 * 2;
        dirLight1.shadow.mapSize.y = 1024 * 2;
        this.scene.add(dirLight1);

        let dirLight2 = new THREE.DirectionalLight(0xffffff, 1);
        dirLight2.position.set(0.1, 0.1, 10);
        dirLight2.castShadow = true;
        this.scene.add(dirLight2);

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
        if (this.currentlyPressedKeys["KeyH"]) {	//H
            this.marble.createRandomMarble(this.scene);
        }
        // Rotasjon om Z:
        if (this.currentlyPressedKeys["KeyA"]) {	//A
            this.gameBoard.tilt(Axis.z, 0.5);

        }
        if (this.currentlyPressedKeys["KeyD"]) {	//D
            this.gameBoard.tilt(Axis.z, -0.5);
        }
        // Rotasjon om X:
        if (this.currentlyPressedKeys["KeyW"]) {	//W
            this.gameBoard.tilt(Axis.x, -0.5);
        }
        if (this.currentlyPressedKeys["KeyS"]) {	//S
            this.gameBoard.tilt(Axis.x, 0.5);
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
        this.physicsWorld.update(deltaTime);

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

//Legger til roter/zoom av scenen:
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
}
