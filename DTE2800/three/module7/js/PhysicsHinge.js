import * as THREE from "../../lib/three/build/three.module.js";
export class PhysicsHinge {
	constructor(physicsWorld, scene) {
		this.scene = scene;
		this.physicsWorld = physicsWorld;

		this.boardRotAngle = 0; //Math.PI/8;      // Dersom planet skal roteres.
		this.boardRotAxis = {x: 1, y:0, z: 0};
		this.IMPULSE_FORCE_STICK = 10;

		this.rbAnchor = undefined;
		this.anchorMesh = undefined;
		this.rbStick = undefined;
		this.stickMesh = undefined;
		this.springAnchor = undefined;
	}

	// Pinnen er forankret i kula (som står i ro, dvs. masse=0).
	// Man bestemmer selv om ankret (Kula) skal tegnes/vises.
	// Pinnen kan beveges - gjøres vha. applyCentralImpulse
	create(hingePos, height=15) {
		let posStick = !hingePos?{x: 0, y: 0, z: 0}:hingePos;     // Cube
		hingePos.y+=height/2
		let scaleStick = {x: Math.abs(hingePos.x) - 0.1, y: height, z: 1};   // Størrelse på pinnen.
		let massStick = 2;                     // Kuben/"stikka" festes til kula og skal kunne rotere. Må derfor ha masse.

		let posAnchor = {x: hingePos.x, y: hingePos.y, z: 0};    // Sphere, forankringspunkt.
		let radiusAnchor = 1;                         // Størrelse på kula.
		let massAnchor = 0;                   // Sphere, denne skal stå i ro.

		let transform = new Ammo.btTransform();

		//ThreeJS, kule:
		let threeQuat = new THREE.Quaternion();  // Roterer i forhold til planet (dersom satt).
		threeQuat.setFromAxisAngle( new THREE.Vector3( this.boardRotAxis.x, this.boardRotAxis.y, this.boardRotAxis.z ), this.boardRotAngle);
		this.anchorMesh = new THREE.Mesh(
			new THREE.CylinderBufferGeometry(radiusAnchor, radiusAnchor, height+height*0.1),
			new THREE.MeshPhongMaterial({color: "#640000"}));
		this.anchorMesh.position.set(posAnchor.x, posAnchor.y, posAnchor.z);
		this.anchorMesh.setRotationFromQuaternion(threeQuat);
		this.anchorMesh.castShadow = true;
		this.anchorMesh.receiveShadow = true;
		this.scene.add(this.anchorMesh);
		//AmmoJS, kule:
		transform.setIdentity();
		transform.setOrigin( new Ammo.btVector3( posAnchor.x, posAnchor.y, posAnchor.z ) );
		let btQuat1 = new Ammo.btQuaternion();
		btQuat1.setRotation(new Ammo.btVector3(this.boardRotAxis.x, this.boardRotAxis.y, this.boardRotAxis.z), this.boardRotAngle);
		transform.setRotation( btQuat1 );
		let motionState = new Ammo.btDefaultMotionState( transform );
		let anchorColShape = new Ammo.btSphereShape( radiusAnchor );
		let localInertia = new Ammo.btVector3( 0, 0, 0 );
		anchorColShape.calculateLocalInertia( massAnchor, localInertia );
		let rbInfoAnchor = new Ammo.btRigidBodyConstructionInfo( massAnchor, motionState, anchorColShape, localInertia );
		this.rbAnchor = new Ammo.btRigidBody( rbInfoAnchor );
		this.rbAnchor.setRestitution(0.4);
		this.rbAnchor.setFriction(0.6);
		this.physicsWorld.addRB(this.rbAnchor,
			this.physicsWorld.groups.colGroupHingeSphere,
			this.physicsWorld.groups.colGroupBall);
		this.anchorMesh.userData.physicsBody = this.rbAnchor;
		this.physicsWorld.rigidBodies.push(this.anchorMesh);

		//ThreeJS, kube/stick:
		this.stickMesh = new THREE.Mesh(new THREE.BoxBufferGeometry(), new THREE.MeshPhongMaterial({color: '#f78a1d'}));
		this.stickMesh.position.set(posStick.x, posStick.y, posStick.z);
		this.stickMesh.scale.set(scaleStick.x, scaleStick.y, scaleStick.z);
		this.stickMesh.setRotationFromQuaternion(threeQuat);
		this.stickMesh.castShadow = true;
		this.stickMesh.receiveShadow = true;
		this.scene.add(this.stickMesh);
		//AmmoJS, kube/stick:
		transform.setIdentity();
		transform.setOrigin( new Ammo.btVector3( posStick.x, posStick.y, posStick.z ) );
		let btQuat2 = new Ammo.btQuaternion();
		btQuat2.setRotation(new Ammo.btVector3(this.boardRotAxis.x, this.boardRotAxis.y, this.boardRotAxis.z), this.boardRotAngle); // + Math.PI/8);
		transform.setRotation( btQuat2 );
		motionState = new Ammo.btDefaultMotionState( transform );
		let stickColShape = new Ammo.btBoxShape( new Ammo.btVector3( scaleStick.x * 0.5, scaleStick.y * 0.5, scaleStick.z * 0.5 ) );
		localInertia = new Ammo.btVector3( 0, 0, 0 );
		stickColShape.calculateLocalInertia( massStick, localInertia );
		let rbInfoStick = new Ammo.btRigidBodyConstructionInfo( massStick, motionState, stickColShape, localInertia );
		this.rbStick = new Ammo.btRigidBody(rbInfoStick);
		this.rbStick.setRestitution(0.4);
		this.rbStick.setFriction(0.6);
		this.physicsWorld.addRB(this.rbStick,
			this.physicsWorld.groups.colGroupStick,
			this.physicsWorld.groups.colGroupBall |
			this.physicsWorld.groups.colGroupPlane |
			this.physicsWorld.groups.colGroupBox);
		this.stickMesh.userData.physicsBody = this.rbStick;

		//Ammo, hengsel: SE F.EKS: https://www.panda3d.org/manual/?title=Bullet_Constraints#Hinge_Constraint:
		let anchorPivot = new Ammo.btVector3( 0, 0, 0 );
		let stickPivot = new Ammo.btVector3( scaleStick.x *0.5*(hingePos.x<0?-1:1), 0, 0 );
		const anchorAxis = new Ammo.btVector3(0,1,0);
		const stickAxis = new Ammo.btVector3(0,1,0);
		let hingeConstraint = new Ammo.btHingeConstraint(this.rbAnchor, this.rbStick, anchorPivot, stickPivot, anchorAxis, stickAxis, false);

		//https://gamedev.stackexchange.com/questions/71436/what-are-the-parameters-for-bthingeconstraintsetlimit
		let maxHingePivot = Math.PI / 2.5;
		let _softness = 0.9;
		let _biasFactor = 0.3;
		let _relaxationFactor = 1.0;
		hingeConstraint.setLimit(-maxHingePivot, maxHingePivot, _softness, _biasFactor, _relaxationFactor);
		this.physicsWorld.addConstraint( hingeConstraint, false );
		this.physicsWorld.rigidBodies.push(this.stickMesh);
	}

	keyCheck(deltaTime, currentlyPressedKeys) {
		if (!this.rbStick || !this.rbAnchor)
			return;
		if (!this.anchorMesh || !this.stickMesh)
			return;

		// NB! Denne er viktig. rigid bodies "deaktiveres" når de blir stående i ro, må aktiveres før man bruke applyCentralImpulse().
		this.rbStick.activate(true);
		this.rbAnchor.activate(true);

		// ROTERER i forhold til PLANET, bruker kvaternion:
		let threeQuat = new THREE.Quaternion();
		threeQuat.setFromAxisAngle( new THREE.Vector3( this.boardRotAxis.x, this.boardRotAxis.y, this.boardRotAxis.z ), this.boardRotAngle);

		// Beregner impuls-vektorene:

		let tmpTrans = new Ammo.btTransform();
		// STICKEN / KUBEN:
		// 1. Henter gjeldende rotasjon for "sticken"/kuben (Ammo):
		let ms1 = this.rbStick.getMotionState();
		ms1.getWorldTransform( tmpTrans );      // NB! worldTRANSFORM!
		let q1 = tmpTrans.getRotation();        // q1 inneholder nå stickens rotasjon.


		// 4. Lager vektorer som står vinkelrett på threeDirectionVectorStick vha. mesh.getWorldDirection():
		// Disse brukes igjen til å dytte sticken vha. applyCentralImpulse()
		let threeDir2 = new THREE.Vector3();
		this.stickMesh.getWorldDirection(threeDir2);  // NB! worldDIRECTION! Gir en vektor som peker mot Z. FRA DOC: Returns a vector representing the direction of object's positive z-axis in world space.
		let threeDir3 = new THREE.Vector3(-threeDir2.x, -threeDir2.y, -threeDir2.z);

		// 6. "Dytter" sticken:
		if (currentlyPressedKeys["KeyV"]) {	//V
			let rdv1 = new Ammo.btVector3(threeDir2.x*this.IMPULSE_FORCE_STICK , threeDir2.y*this.IMPULSE_FORCE_STICK , threeDir2.z*this.IMPULSE_FORCE_STICK );
			this.rbStick.applyCentralImpulse( rdv1, this.IMPULSE_FORCE_STICK );
		}
		if (currentlyPressedKeys["KeyB"]) {	//B
			let rdv2 = new Ammo.btVector3(threeDir3.x*this.IMPULSE_FORCE_STICK , threeDir3.y*this.IMPULSE_FORCE_STICK , threeDir3.z*this.IMPULSE_FORCE_STICK );
			this.rbStick.applyCentralImpulse( rdv2, this.IMPULSE_FORCE_STICK );
		}
	}
}
