/*
    Utgangspunkt eTest 2020.
    Tegner diverse.
*/
//Globale varianbler:
let renderer;
let scene;
let camera;

let controls; //rotere, zoone hele scenen.
const clock = new THREE.Clock();

import * as THREE from '../lib/three/build/three.module.js';
import {OrbitControls} from '../lib/three/examples/jsm/controls/OrbitControls.js';
import { addCoordSystem} from "../lib/wfa-coord.js";

export function main() {
    window.addEventListener('resize', onWindowResize, false);

    //Henter referanse til canvaset:
    let mycanvas = document.getElementById('webgl');

    //Lager en scene:
    scene = new THREE.Scene();
    scene.background = new THREE.Color( 0xBFD104 );
    renderer = new THREE.WebGLRenderer({ canvas: mycanvas, antialias: true });
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.shadowMap.enabled = true;
    renderer.shadowMapSoft = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;

    //Kamera:
    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 10000);
    camera.position.z = 1;

    addCoordSystem(scene);
    animate();
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
    controls.handleResize();
    render();
}

function render() {
    renderer.render(scene, camera);
}

function animate(time) {
    requestAnimationFrame(animate);
    let delta = clock.getDelta();
    render();
}