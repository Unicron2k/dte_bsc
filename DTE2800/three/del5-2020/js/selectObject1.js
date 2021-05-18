/**
 * Velge objekt vha musepeker.
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
let clock = new THREE.Clock();

let objects = [];
let raycaster;
let mouse;
let particleMaterial;

export function main() {
	//Henter referanse til canvaset:
	let mycanvas = document.getElementById('webgl');

	//Lager en scene:
	scene = new THREE.Scene();

	//Lager et rendererobjekt (og setter størrelse):
	renderer = new THREE.WebGLRenderer({canvas:mycanvas, antialias:true});
	renderer.setClearColor(0xBFD104, 0xff);  //farge, alphaverdi.
	renderer.setSize(window.innerWidth, window.innerHeight);
	renderer.shadowMap.enabled = true; //NB!
	renderer.shadowMap.type = THREE.PCFSoftShadowMap; //THREE.BasicShadowMap;

	//Oppretter et kamera:
	camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 10000);
	camera.position.x = 130;
	camera.position.y = 300;
	camera.position.z = 250;
	camera.up = new THREE.Vector3(0, 1, 0);
    let target = new THREE.Vector3(0.0, 0.0, 0.0);
    camera.lookAt(target);

    //Lys:
	let spotLight = new THREE.SpotLight(0xffffff); //hvitt lys
	spotLight.position.set( 0, 400, 0 );
	spotLight.castShadow = true;
	scene.add(spotLight);

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

	particleMaterial = new THREE.SpriteMaterial({ color: Math.random() * 0xffffff, opacity: 1 });


    //Input - standard Javascript / WebGL:
    raycaster = new THREE.Raycaster();
    mouse = new THREE.Vector2();

	//Events:
	window.addEventListener('resize', onWindowResize, false);
	document.addEventListener('mousedown', onDocumentMouseDown, false);
    document.addEventListener('touchstart', onDocumentTouchStart, false);

	animate();
}

//Eventmetode:
function onDocumentTouchStart(event) {
    event.preventDefault();
    event.clientX = event.touches[0].clientX;
    event.clientY = event.touches[0].clientY;
    onDocumentMouseDown( event );
}

//Eventmetode:
function onDocumentMouseDown(event) {
    event.preventDefault();

	// Se: https://threejs.org/docs/index.html#api/en/core/Raycaster.
	// mouse.x og y skal være NDC, dvs. ligge i området -1 til 1.
    mouse.x = (event.clientX / renderer.domElement.clientWidth) * 2 - 1;
    mouse.y = -(event.clientY / renderer.domElement.clientHeight) * 2 + 1;
    //Ray/stråle fra klikkposisjon til kamera:
    raycaster.setFromCamera(mouse, camera); // Raycaster
    let intersects = raycaster.intersectObjects(objects);

    //Sjekker om strålen treffer noen av objekene:
    if (intersects.length > 0) {
        //Endrer farge på det første objektet som er klikket på som strålen treffer:
        intersects[0].object.material.color.setHex(Math.random() * 0xffffff);
        //Viser en "partikkel":
        let particle = new THREE.Sprite(particleMaterial);
        particle.position.copy(intersects[0].point);
        particle.scale.x = particle.scale.y = 16;
        scene.add(particle);
    }
}

function addModels() {
    //Wireframe-materiale:
    //let mCubeMat1 = new THREE.MeshBasicMaterial({ color: 0x000000, wireframe: true, wireframe_linewidth: 10 });
    //Geo:
    let geometry = new THREE.BoxGeometry(100, 100, 100);
    for (let i = 0; i < 10; i++) {
        //Fargemateriale:
        let mCubeMat2 = new THREE.MeshBasicMaterial({ color: Math.random() * 0xffffff, opacity: 0.5 });
        //Mesh:
        //let object = THREE.SceneUtils.createMultiMaterialObject(geometry, [mCubeMat1, mCubeMat2]);
        let object = new THREE.Mesh(geometry, mCubeMat2);

        object.position.x = Math.random() * 800 - 400;
        object.position.y = Math.random() * 800 - 400;
        object.position.z = Math.random() * 800 - 400;

        object.scale.x = Math.random() * 2 + 1;
        object.scale.y = Math.random() * 2 + 1;
        object.scale.z = Math.random() * 2 + 1;

        object.rotation.x = Math.random() * 2 * Math.PI;
        object.rotation.y = Math.random() * 2 * Math.PI;
        object.rotation.z = Math.random() * 2 * Math.PI;

        scene.add(object);

        objects.push(object);

    }
}

//Legger til roter/zoom av scenen:
function addControls() {
    //NB! Viktit med renderer.domElement her pga. dat-gui (hvis ikke henger kontrollen fast i musepekeren).
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

	//Oppdater trackball-kontrollen:
	//controls.update();

	//Tegner scenen med gitt kamera:
	render();
};


let theta = 0;
let radius = 600;

function render()
{
    theta += 0.1;

    camera.position.x = radius * Math.sin(THREE.Math.degToRad(theta));
    camera.position.y = radius * Math.sin(THREE.Math.degToRad(theta));
    camera.position.z = radius * Math.cos(THREE.Math.degToRad(theta));
    camera.lookAt(scene.position);

    renderer.render(scene, camera);
}

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    //controls.handleResize();
    render();
}
