import * as THREE from "../../lib/three/build/three.module.js";
export class PhysicsWall {
    constructor(physicsWorld, scene) {
        this.scene = scene;
        this.physicsWorld = physicsWorld;

        this.rigidBody = undefined;
        this.mesh = undefined;
    }

    create(pos, color, mass, size){
        // Inputkontroll:
        if (!pos) pos = {x:0, y:0, z:0};
        if (mass===undefined || mass===null) mass = 3;
        if (!color) color = '#f78a1d';
        if (!size) size = {x: 1, y: 1, z:1};
        pos.y+=size.y/2;

        // AMMO:
        let boxShape = new Ammo.btBoxShape( new Ammo.btVector3( size.x/2, size.y/2, size.z/2) );  //NB! Delt på 2!!
        let transform = new Ammo.btTransform();
        transform.setIdentity();
        transform.setOrigin( new Ammo.btVector3( pos.x, pos.y, pos.z ) );

        let motionState = new Ammo.btDefaultMotionState( transform );
        let localInertia = new Ammo.btVector3( 0, 0, 0 );
        this.rigidBody = new Ammo.btRigidBody(new Ammo.btRigidBodyConstructionInfo(mass, motionState, boxShape, localInertia));
        this.rigidBody.setRestitution(0.7);
        this.rigidBody.setFriction(0.5);

        // Kollisjonsfiltrering: Denne RB tilhører gruppa Box og skal kunne
        // kollidere med objekter i gruppene Box, Plane, Sphere m.fl.
        this.physicsWorld.addRB(this.rigidBody,
            this.physicsWorld.groups.colGroupBox,
            this.physicsWorld.groups.colGroupBox |
            this.physicsWorld.groups.colGroupPlane |
            this.physicsWorld.groups.colGroupBall |
            this.physicsWorld.groups.colGroupStick
        );

        // THREE:
        this.mesh = new THREE.Mesh(new THREE.BoxBufferGeometry(), new THREE.MeshPhongMaterial({color: color}));
        this.mesh.position.set(pos.x, pos.y, pos.z);
        this.mesh.scale.set(size.x, size.y, size.z);
        this.mesh.castShadow = true;

        this.mesh.userData.physicsBody = this.rigidBody;
        this.physicsWorld.rigidBodies.push(this.mesh);
        this.scene.add(this.mesh);
    }

    keyCheck(deltaTime, currentlyPressedKeys) {
        if (!this.rigidBody || !this.mesh)
            return;

        this.rigidBody.activate(true);

        this.rigidBody.applyAxisAngle(2,10);
    }
}
