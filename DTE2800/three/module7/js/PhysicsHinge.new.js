import * as THREE from "../../lib/three/build/three.module.js";
export class PhysicsHinge {
	constructor(physicsWorld, scene){
		this.physicsWorld = physicsWorld;
		this.scene = scene;

		this.boardRotAngle = 0; //Math.PI/8;      // Dersom planet skal roteres.
		this.boardRotAxis = {x: 1, y:0, z: 0};
		this.IMPULSE_FORCE_STICK = 150;

		this.rbAnchor = undefined;
		this.anchorMesh = undefined;
		this.rbStick = undefined;
		this.stickMesh = undefined;
		this.hingeConstraint = undefined;
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
			new THREE.MeshPhongMaterial({color: "#640000", transparent: true, opacity: 0.5}));
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
		this.stickMesh = new THREE.Mesh(new THREE.BoxBufferGeometry(), new THREE.MeshPhongMaterial({color: 0xf78a1d}));
		this.stickMesh.wrap
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
		let stickPivot = new Ammo.btVector3( scaleStick.x*0.5*(hingePos.x<0?-1:1), 0, 0 );
		const anchorAxis = new Ammo.btVector3(0,1,0);
		const stickAxis = new Ammo.btVector3(0,1,0);
		this.hingeConstraint = new Ammo.btHingeConstraint(this.rbAnchor, this.rbStick, anchorPivot, stickPivot, anchorAxis, stickAxis, false);

		//https://gamedev.stackexchange.com/questions/71436/what-are-the-parameters-for-bthingeconstraintsetlimit
		let maxHingePivot = Math.PI / 2.5;
		let _softness = 0.9;
		let _biasFactor = 0.3;
		let _relaxationFactor = 1.0;
		this.hingeConstraint.setLimit(-maxHingePivot, maxHingePivot, _softness, _biasFactor, _relaxationFactor);

		this.physicsWorld.addConstraint( this.hingeConstraint, false );
		this.physicsWorld.rigidBodies.push(this.stickMesh);







		let springMass = 10;
		let springPos = {x: 0, y: 0, z: 0};
		let springScale = {x: 0, y: 2, z: 2};

		//boxMesh
		let springAnchorMesh = new THREE.Mesh(new THREE.BoxBufferGeometry(), new THREE.MeshPhongMaterial({color: "#b31414"}));
		springAnchorMesh.position.set(springPos.x, springPos.y, springPos.z);
		springAnchorMesh.scale.set(springScale.x, springScale.y, springScale.z);
		this.scene.add(springAnchorMesh);

		// boxRB
		let boxShape = new Ammo.btBoxShape(new Ammo.btVector3(springScale.x * 0.5, springScale.y * 0.5, springScale.z * 0.5));
		let springAnchorTransform = new Ammo.btTransform();
		springAnchorTransform.setIdentity();
		springAnchorTransform.setOrigin(new Ammo.btVector3(springPos.x, springPos.y, springPos.z));
		let springMotionState = new Ammo.btDefaultMotionState(springAnchorTransform);
		let localSpringInertia = new Ammo.btVector3(0, 0, 0);
		boxShape.calculateLocalInertia(springMass, localSpringInertia);
		let rbInfo = new Ammo.btRigidBodyConstructionInfo(springMass, springMotionState, boxShape, localSpringInertia);
		let rbSpringAnchor = new Ammo.btRigidBody(rbInfo);
		rbSpringAnchor.setRestitution(0.0);
		rbSpringAnchor.setFriction(0.0);


		springAnchorMesh.userData.physicsBody = rbSpringAnchor;
		this.physicsWorld.rigidBodies.push(springAnchorMesh);

		//https://stackoverflow.com/questions/46671809/how-to-make-a-spring-constraint-with-buwllet-physics
		let transform1 = new Ammo.btTransform();
		transform1.setIdentity();
		transform1.setOrigin(new Ammo.btVector3(0, 0, 0));
		let transform2 = new Ammo.btTransform();
		transform2.setIdentity();
		transform2.setOrigin(new Ammo.btVector3(0, 0, 0));

		let springConstraint = new Ammo.btGeneric6DofSpringConstraint(
			this.rbStick,
			rbSpringAnchor,
			transform1,
			transform2,
			true);

		// Removing any restrictions on the y-coordinate of the hanging box
		// by setting the lower limit above the upper one.
		springConstraint.setLinearLowerLimit(new Ammo.btVector3(0.0, 0.0, -10.0));
		springConstraint.setLinearUpperLimit(new Ammo.btVector3(0.0, 0.0, 10.0));

		// NB! Disse er viktig for at ikke den hengende kuben ikke skal rotere om alle akser!!
		// Disse gjør at den hengende boksen ikke roterer når den er festet til en constraint (se side 130 i Bullet-boka).
		springConstraint.setAngularLowerLimit(new Ammo.btVector3(0, 0.0, 0.0));
		springConstraint.setAngularUpperLimit(new Ammo.btVector3(0, 0.0, 0.0));

		springConstraint.enableSpring(2, true);    // Translation on y-axis

		let doorStiffness = 10;
		let doorDamping = 0.9;
		springConstraint.setStiffness(2, doorStiffness);
		springConstraint.setDamping(2, doorDamping);

		this.physicsWorld.addConstraint(springConstraint, false);

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

		// 2. Lager en (THREE) vektor som peker i samme retning som sticken:
		let threeDirectionVectorStick = new THREE.Vector3(1,0,0);
		//   2.1 Lager en THREE-kvaternion for rotasjon basert på Ammo-kvaternionen (q1) over:
		let threeQuaternionStick = new THREE.Quaternion(q1.x(), q1.y(), q1.z(), q1.w());
		//   2.2 Roterer (THREE) retningsvektoren slik at den peker i samme retning som sticken:
		threeDirectionVectorStick.applyQuaternion(threeQuaternionStick);


		// 4. Lager vektorer som står vinkelrett på threeDirectionVectorStick vha. mesh.getWorldDirection():
		// Disse brukes igjen til å dytte sticken vha. applyCentralImpulse()
		let threeDir2 = new THREE.Vector3();
		this.stickMesh.getWorldDirection(threeDir2);  // NB! worldDIRECTION! Gir en vektor som peker mot Z. FRA DOC: Returns a vector representing the direction of object's positive z-axis in world space.
		let threeDir3 = new THREE.Vector3(-threeDir2.x, -threeDir2.y, -threeDir2.z);

		// 6. "Dytter" sticken:
		if (currentlyPressedKeys[86]) {	//V
			let rdv1 = new Ammo.btVector3(threeDir2.x*this.IMPULSE_FORCE_STICK , threeDir2.y*this.IMPULSE_FORCE_STICK , threeDir2.z*this.IMPULSE_FORCE_STICK );
			this.rbStick.applyCentralImpulse( rdv1, this.IMPULSE_FORCE_STICK );
		}
		if (currentlyPressedKeys[66]) {	//B
			let rdv2 = new Ammo.btVector3(threeDir3.x*this.IMPULSE_FORCE_STICK , threeDir3.y*this.IMPULSE_FORCE_STICK , threeDir3.z*this.IMPULSE_FORCE_STICK );
			this.rbStick.applyCentralImpulse( rdv2, this.IMPULSE_FORCE_STICK );
		}
	}

	/**
	 * Update params for the hingeConstraint on door
	 */

}