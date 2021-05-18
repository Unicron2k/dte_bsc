/*
    Utgangspunkt eTest 2020.
    Tegner diverse.
*/
"use strict";
import * as THREE from '../lib/three/build/three.module.js';
import {OrbitControls} from '../lib/three/examples/jsm/controls/OrbitControls.js';
import { addCoordSystem} from "../lib/wfa-coord.js";
export class ThreeTest0_OO {


    constructor() {
        //Globale varianbler:

        this.scene = {};
        this.renderer = {};
        this.controls = {}; //rotere, zoone hele scenen.
        this.camera = {};
        this.currentlyPressedKeys = [];
        this.clock = new THREE.Clock();
    }

    main() {
        document.addEventListener('keyup', this.onKeyUp.bind(this), {passive: false});
        document.addEventListener('keydown', this.onKeyDown.bind(this), {passive: false});
        document.addEventListener('resize', this.onWindowResize.bind(this), {passive: false});

        //Henter referanse til canvaset:
        let mycanvas = document.getElementById('webgl');

        //Lager en scene:
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0xBFD104);
        this.renderer = new THREE.WebGLRenderer({canvas: mycanvas, antialias: true});
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.shadowMap.enabled = true;
        this.renderer.shadowMapSoft = true;
        this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;

        //Kamera:
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 10000);
        this.camera.position.z = 1;

        addCoordSystem(this.scene);
        this.addControls();
        this.addSkybox();
        this.animate();
    }

    animate(time) {
        requestAnimationFrame(this.animate.bind(this));
        let delta = this.clock.getDelta();
        this.render();
    }

    render() {
        if (this.renderer)
            this.renderer.render(this.scene, this.camera);
    }

    onWindowResize() {
        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.controls.handleResize();
        this.render();
    }

    onKeyUp(event) {
        if (event.defaultPrevented) return;
        this.currentlyPressedKeys[event.code] = false;
        event.preventDefault();
    }

    onKeyDown(event) {
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

    addSkybox(){
        let path = './textures';
        this.scene.background = new THREE.CubeTextureLoader().load([
            path+'/posx.jpg',
            path+'/negx.jpg',
            path+'/posy.jpg',
            path+'/negy.jpg',
            path+'/posz.jpg',
            path+'/negz.jpg',
        ]);
    }
}