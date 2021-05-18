/*
	Et "vippbart" terreng/plan.
 */
import * as THREE from "../../lib/three/build/three.module.js";

let physicsWorld;
let tmpTrans;
let colGroupPlane = 1, colGroupBall = 2, colGroupTriangle = 4;
let rigidBodies = [];
let terrainRigidBody;
export const DEBUG = false;

export function setupPhysicsWorld(){
	tmpTrans = new Ammo.btTransform();
	let collisionConfiguration  = new Ammo.btDefaultCollisionConfiguration(),
		dispatcher          = new Ammo.btCollisionDispatcher(collisionConfiguration),
		overlappingPairCache= new Ammo.btDbvtBroadphase(),
		solver              = new Ammo.btSequentialImpulseConstraintSolver();
	physicsWorld           = new Ammo.btDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
	physicsWorld.setGravity(new Ammo.btVector3(0, -9.81, 0));
}

export function updatePhysics( deltaTime ){
	if (!tmpTrans)
		return;
	// Step physics world:
	physicsWorld.stepSimulation( deltaTime, 10 );
	// Update rigid bodies
	for ( let i = 0; i < rigidBodies.length; i++ ) {
		let objThree = rigidBodies[ i ];
		let objAmmo = objThree.userData.physicsBody;
		let ms = objAmmo.getMotionState();
		if ( ms ) {
			ms.getWorldTransform( tmpTrans );
			let p = tmpTrans.getOrigin();
			let q = tmpTrans.getRotation();
			objThree.position.set( p.x(), p.y(), p.z() );
			objThree.quaternion.set( q.x(), q.y(), q.z(), q.w() );
		}
	}
}

// Flatt terreng/plan (uten hull e.l.). Bruk f.eks. Shape og ExtrudeBuffferGeometry til dette.
// Må pakke terreng-shapen inn i en btCompoundShape for at planet skal vippe om sentrum.
// Lager derfor også en Three.Group som inneholder et mesh basert på BoxBufferGeometry.
export function createTiltableTerrain(scene){

	//Ammo-konteiner:
	let compoundShape = new Ammo.btCompoundShape();

	//Three-konteiner:
	let groupMesh = new THREE.Group();
	groupMesh.position.x = 0
	groupMesh.position.y = 0;
	groupMesh.position.z = 0;

	// Størrelser på "terrenget"/planet:
	let WIDTH=100, HEIGHT=2, DEPTH=100;

	// Lage Three BoxBufferGeometry (ev. Shape & ExtrudeBuffferGeometry):
	let terrainGeometry = new THREE.BoxBufferGeometry( WIDTH, HEIGHT,DEPTH, 5, 5);
	let terrainMaterial = new THREE.MeshPhongMaterial( { color: 0xC709C7, side: THREE.DoubleSide } );
	let terrainMesh = new THREE.Mesh( terrainGeometry, terrainMaterial );
	terrainMesh.scale.set(1,1,1,);
	terrainMesh.receiveShadow = true;
	groupMesh.add( terrainMesh );

	// Ammo (indre) Shape-objekt & transformasjon i forhold til compoundShape. compoundShape.addChildShape():
	let ammoTerrainShape = new Ammo.btBoxShape(new Ammo.btVector3(WIDTH/2, HEIGHT/2, DEPTH/2));
	let shapeTrans = new Ammo.btTransform();
	shapeTrans.setIdentity();
	shapeTrans.setOrigin(new Ammo.btVector3(terrainMesh.position.x,terrainMesh.position.y,terrainMesh.position.z));
	let terrainQuat = terrainMesh.quaternion;
	shapeTrans.setRotation( new Ammo.btQuaternion(terrainQuat.x, terrainQuat.y, terrainQuat.z, terrainQuat.w) );
	compoundShape.addChildShape(shapeTrans, ammoTerrainShape);

	// Ammo compoundShape, transformasjon & rigidBody:
	let massTerrain = 0;
	let compoundShapeTrans = new Ammo.btTransform();
	compoundShapeTrans.setIdentity();
	compoundShapeTrans.setOrigin(new Ammo.btVector3(groupMesh.position.x,groupMesh.position.y,groupMesh.position.z));
	let quatCompound = groupMesh.quaternion;
	compoundShapeTrans.setRotation( new Ammo.btQuaternion( quatCompound.x, quatCompound.y, quatCompound.z, quatCompound.w ) );
	let motionState = new Ammo.btDefaultMotionState( compoundShapeTrans );
	let localInertia = new Ammo.btVector3( 0, 0, 0 );
	compoundShape.setLocalScaling(new Ammo.btVector3(groupMesh.scale.x,groupMesh.scale.y, groupMesh.scale.x));
	compoundShape.calculateLocalInertia( massTerrain, localInertia );
	let rbInfo = new Ammo.btRigidBodyConstructionInfo( massTerrain, motionState, compoundShape, localInertia );
	terrainRigidBody = new Ammo.btRigidBody(rbInfo);
	terrainRigidBody.setFriction(0.3);
	terrainRigidBody.setRestitution(0.03);
	terrainRigidBody.setCollisionFlags(terrainRigidBody.getCollisionFlags() | 2);   // BODYFLAG_KINEMATIC_OBJECT = 2 betyr kinematic object, masse=0 men kan flyttes!!
	terrainRigidBody.setActivationState(4);   // Never sleep, BODYSTATE_DISABLE_DEACTIVATION = 4
	scene.add(groupMesh);

	// Legg til physicsWorld:
	physicsWorld.addRigidBody( terrainRigidBody, colGroupPlane,  colGroupBall | colGroupTriangle);
	physicsWorld.updateSingleAabb(terrainRigidBody) ;
	groupMesh.userData.physicsBody = terrainRigidBody;
	rigidBodies.push(groupMesh);
}

export function createRandomBall(scene){
	let xPos = -14 + Math.random() * 28;
	let zPos = 15 + Math.random() * -50;
	let pos = {x: xPos, y: 50, z: zPos};
	createBall(scene, pos, 0xff0505, 1);
}

export function createBall(scene, pos, color, mass){
	let radius = 1;
	let quat = {x: 0, y: 0, z: 0, w: 1};

	//AMMO:
	let transform = new Ammo.btTransform();
	transform.setIdentity();
	transform.setOrigin( new Ammo.btVector3( pos.x, pos.y, pos.z ) );
	transform.setRotation( new Ammo.btQuaternion( quat.x, quat.y, quat.z, quat.w ) );
	let motionState = new Ammo.btDefaultMotionState( transform );
	let ballShape = new Ammo.btSphereShape( radius );
	ballShape.setMargin( 0.05 );
	let localInertia = new Ammo.btVector3( 0, 0, 0 );
	ballShape.calculateLocalInertia( mass, localInertia );
	let rbInfo = new Ammo.btRigidBodyConstructionInfo( mass, motionState, ballShape, localInertia );
	let sphereRigidBody = new Ammo.btRigidBody( rbInfo );
	sphereRigidBody.setRestitution(0.01);
	sphereRigidBody.setFriction(0.2);
	physicsWorld.addRigidBody( sphereRigidBody, colGroupBall, colGroupBall | colGroupPlane | colGroupTriangle);

	//THREE
	let ballMesh = new THREE.Mesh(new THREE.SphereBufferGeometry(radius, 32, 32), new THREE.MeshPhongMaterial({color: color}));
	let origin = transform.getOrigin();
	let orientation = transform.getRotation();
	ballMesh.position.set(origin.x(), origin.y(), origin.z());
	ballMesh.setRotationFromQuaternion(new THREE.Quaternion(orientation.x(), orientation.y(), orientation.z(), orientation.w()));
	ballMesh.castShadow = true;
	ballMesh.receiveShadow = true;
	scene.add(ballMesh);
	ballMesh.userData.physicsBody = sphereRigidBody;
	ballMesh.userData.name = 'ballMesh';
	rigidBodies.push(ballMesh);
}

function toRadians(angle) {
	return Math.PI/180 * angle;
}

// axisNo: 1=x, 2=y, 3=z
export function tiltGameBoard(axisNo, angle) {
	let axis;
	switch (axisNo) {
		case 1:
			axis = new THREE.Vector3( 1, 0, 0 );
			break;
		case 2:
			axis = new THREE.Vector3( 0,1, 0);
			break;
		case 3:
			axis = new THREE.Vector3( 0,0, 1);
			break;
		default:
			axis = new THREE.Vector3( 1, 0, 0 );
	}
	// Henter gjeldende transformasjon:
	let terrainTransform = new Ammo.btTransform();
	let terrainMotionState = terrainRigidBody.getMotionState();
	terrainMotionState.getWorldTransform( terrainTransform );
	let ammoRotation = terrainTransform.getRotation();

	// Roter gameBoardRigidBody om en av aksene (bruker Three.Quaternion til dette):
	let threeCurrentQuat = new THREE.Quaternion(ammoRotation.x(), ammoRotation.y(), ammoRotation.z(), ammoRotation.w());
	let threeNewQuat = new THREE.Quaternion();
	threeNewQuat.setFromAxisAngle(axis, toRadians(angle));
	// Slår sammen eksisterende rotasjon med ny/tillegg.
	let resultQuaternion = threeCurrentQuat.multiply(threeNewQuat);
	// Setter ny rotasjon på ammo-objektet:
	terrainTransform.setRotation( new Ammo.btQuaternion( resultQuaternion.x, resultQuaternion.y, resultQuaternion.z, resultQuaternion.w ) );
	terrainMotionState.setWorldTransform(terrainTransform);
}
